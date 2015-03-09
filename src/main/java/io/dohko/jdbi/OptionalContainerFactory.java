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
package io.dohko.jdbi;

import com.google.common.base.Optional;

import org.skife.jdbi.v2.ContainerBuilder;
import org.skife.jdbi.v2.tweak.ContainerFactory;


public class OptionalContainerFactory implements ContainerFactory<Optional<?>>
{
    @Override
    public boolean accepts(Class<?> type)
    {
        return Optional.class.isAssignableFrom(type);
    }

    @Override
    public ContainerBuilder<Optional<?>> newContainerBuilderFor(Class<?> type)
    {
        return new ContainerBuilder<Optional<?>>()
        {
            private Optional<?> _optional = Optional.absent();
            
            @Override
            public ContainerBuilder<Optional<?>> add(Object it)
            {
                this._optional = Optional.fromNullable(it);
                
                return this;
            }

            @Override
            public Optional<?> build()
            {
                return _optional;
            }
        };
    }
}
