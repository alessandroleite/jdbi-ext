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

import static java.util.Objects.requireNonNull;

import javax.annotation.Nonnull;

import org.springframework.beans.factory.FactoryBean;

public class JdbiRepositoryFactoryBean<T> implements FactoryBean<T>
{
    /**
     * The repository type. It might not be <code>null</code>.
     */
    private final Class<T> _repositoryClass;
    
    /**
     * The {@link IDBI} instance to create the repository.
     */
    private final org.skife.jdbi.v2.IDBI _dbi;
    
    /**
     * @param dbi a dbi instance. It might not be <code>null</code>.
     * @param repositoryClazz the repository type. It might not be <code>null</code>.
     */
    public JdbiRepositoryFactoryBean(@Nonnull org.skife.jdbi.v2.DBI dbi, @Nonnull Class<T> repositoryClazz)
    {
        this._dbi = requireNonNull(dbi);
        this._repositoryClass = requireNonNull(repositoryClazz);
    }

    @Override
    public T getObject() throws Exception
    {
        return _dbi.open(_repositoryClass);
    }

    @Override
    public Class<?> getObjectType()
    {
        return _repositoryClass;
    }

    @Override
    public boolean isSingleton()
    {
        return true;
    }
}
