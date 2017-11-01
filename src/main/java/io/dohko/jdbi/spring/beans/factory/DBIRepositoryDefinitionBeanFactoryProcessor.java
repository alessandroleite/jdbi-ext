/**
 *     Copyright (C) 2015  the original author or authors.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License,
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package io.dohko.jdbi.spring.beans.factory;

import java.util.Set;

import javax.sql.DataSource;

import io.dohko.jdbi.stereotype.Repository;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.IDBI;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Dynamically creates a bean for each annotated type with {@link Repository}. The bean is created through the {@link JdbiRepositoryFactoryBean}
 * class, which has the dependency for the <em>dbi</em> bean. The dbi bean can be previously defined or, we can delegates its definition 
 * for the application. In this case, we only need to defined a {@link DataSource} bean.  
 * 
 * <p>As usual, the registered beans can be accessed by name or by type. In this case, the name is the simple type name starting with a lower case
 * character. For instance, the bean's name for the type <code>com.acme.FooRepository</code> is <em>fooRepository</em>.
 * 
 * <p>To use this class, you must register it as a Spring's bean and annotate your DBI repository types with the annotation {@link Repository}.
 * 
 * <br><br>
 * <h3>Creating a repository </h3>
 * 
 * <pre>
 *   &#x40;Repository
 *   public interface FooRepository
 *   {
 *       &#x40;SqlQuery("SELECT * FROM bar where x = :foo")
 *       Bar getBar(Object foo);
 *   }
 * </pre>
 * 
 * <br><br>
 * <h3>Registering this bean processor</h3>
 * 
 * <pre>
 *   &lt;bean id="repositoryFactoryBeanProcessor"  class="io.dohko.jdbi.spring.beans.factory.DBIRepositoryDefinitionBeanFactoryProcessor"
 *            depends-on="dataSource" &gt;
 *     &lt;constructor-arg value="com.acme" /&gt;
 *   &lt; /bean&gt;
 * </pre>
 */
public class DBIRepositoryDefinitionBeanFactoryProcessor implements BeanFactoryPostProcessor
{
    /**
     * The base-package to scan the types annotated as a {@link Repository}.
     */
    private final String _basePackageName;
    
    /**
     * The bean scope. It might not be <code>null</code>.
     */
    private final String _scope;
    
    /**
     * @param packageName the base-package name to scan the types annotated with {@link Repository}. It might not be <code>null</code> or empty.
     * @param scope defines the scope of the beans.  It might not be <code>null</code> or empty.
     * @throws IllegalStateException if the package name or the scope is <code>null</code> or <code>empty</code>.
     */
    public DBIRepositoryDefinitionBeanFactoryProcessor(final String packageName, final String scope)
    {
        this._basePackageName = packageName;
        this._scope = scope;
        
        checkState(!isNullOrEmpty(this._basePackageName), "Base package's name must not be null or empty");
        checkState(!isNullOrEmpty(this._scope), "The beans' scope must not be null or empty");
    }
    
    /**
     * Creates a instance of this class with a singleton bean scope.
     * @param packageName the base-package name to scan the types annotated with {@link Repository}. It might not be <code>null</code> or empty.
     */
    public DBIRepositoryDefinitionBeanFactoryProcessor(final String packageName)
    {
        this(packageName, BeanDefinition.SCOPE_SINGLETON);
    }
    
    @Override
    public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException
    {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forPackage(_basePackageName))
                .addScanners(new TypeElementsScanner(), new TypeAnnotationsScanner()));
        
        defineIdbiBeanIfUndefined(beanFactory);
        
        final Set<Class<?>> repositoryTypes = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> type : repositoryTypes)
        {
            AbstractBeanDefinition bean = BeanDefinitionBuilder.rootBeanDefinition(JdbiRepositoryFactoryBean.class)
                    .setScope(_scope)
                    .addDependsOn("dbi")
                    .addConstructorArgReference("dbi")
                    .addConstructorArgValue(type)
                    .getBeanDefinition();

            final String beanName = getBeanNameFor(type);
            
            ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(beanName, bean);
        }
    }

    private String getBeanNameFor(Class<?> type) 
    {
    	String name = type.getAnnotation(Repository.class).value();
        final String beanName = name == null || name.trim().isEmpty() ?
        		  type.getSimpleName().substring(0, 1).toLowerCase().concat(type.getSimpleName().substring(1)) :
        		  name;

		return beanName;
	}

	/**
     * Defines an {@link IDBI} bean if and only if it is undefined.
     * <p> 
     * 
     * @param beanFactory  the bean factory used by the application context
     */
    private void defineIdbiBeanIfUndefined(ConfigurableListableBeanFactory beanFactory)
    {
        try
        {
            beanFactory.getBean(IDBI.class);
        }
        catch (NoSuchBeanDefinitionException undefinedIdbiBean)
        {
            try
            {
                beanFactory.getBean(DBI.class);
            }
            catch (NoSuchBeanDefinitionException undefinedDbiBean)
            {
                AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.rootBeanDefinition(DBIFactoryBean2.class)
                        .addConstructorArgValue(beanFactory.getBean(DataSource.class))
                        .setAutowireMode(AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE)
                        .setScope(BeanDefinition.SCOPE_SINGLETON)
                        .getBeanDefinition();
                
                ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition("dbi", beanDefinition);
            }
        }
    }
}
