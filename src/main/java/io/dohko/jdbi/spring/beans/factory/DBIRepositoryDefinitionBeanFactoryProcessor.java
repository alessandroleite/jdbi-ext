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

import io.dohko.jdbi.stereotype.Repository;

import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Strings.*;

/**
 * Dynamically creates a bean for each type annotated with {@link Repository}. The bean is created through the {@link JdbiRepositoryFactoryBean}
 * class, which has the dependency for the <em>dbi</em> bean.
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
 *       &#x40;SqlQuery("SELECT * FROM bar where x = true")
 *       Bar getBar();
 *   }
 * </pre>
 * 
 * <br><br>
 * <h3>Registering this bean processor</h3>
 * 
 * <pre>
 *   &lt;bean id="repositoryFactoryBeanProcessor"  class="io.dohko.jdbi.spring.beans.factory.DBIRepositoryDefinitionBeanFactoryProcessor" depends-on="dbi"&gt;
 *     &lt;constructor-arg value="com.acme" /&gt;
 *   &lt; /bean&gt;
 * </pre>
 */
public class DBIRepositoryDefinitionBeanFactoryProcessor implements BeanFactoryPostProcessor
{
    private final String _basePackageName;
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

        final Set<Class<?>> repositoryTypes = reflections.getTypesAnnotatedWith(Repository.class);

        for (Class<?> type : repositoryTypes)
        {
            AbstractBeanDefinition bean = BeanDefinitionBuilder.rootBeanDefinition(JdbiRepositoryFactoryBean.class)
                    .setScope(_scope)
                    .addDependsOn("dbi")
                    .addConstructorArgReference("dbi")
                    .addConstructorArgValue(type)
                    .getBeanDefinition();

            final String beanName = type.getSimpleName().substring(0, 1).toLowerCase().concat(type.getSimpleName().substring(1));
            
            ((DefaultListableBeanFactory) beanFactory).registerBeanDefinition(beanName, bean);
        }
    }
}
