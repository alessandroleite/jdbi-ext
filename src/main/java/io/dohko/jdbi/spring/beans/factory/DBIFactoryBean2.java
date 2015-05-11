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

import javax.sql.DataSource;

import io.dohko.jdbi.BigIntegerArgumentFactory;
import io.dohko.jdbi.JodaTimeArgumentFactory;
import io.dohko.jdbi.OptionalArgumentFactory;
import io.dohko.jdbi.OptionalContainerFactory;
import io.dohko.jdbi.args.JodaDateTimeMapper;
import io.dohko.jdbi.util.BigIntegerMapper;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.spring.DBIFactoryBean;

/**
 * Creates a {@link DBI} instance and registers the factories: {@link JodaTimeArgumentFactory}, the {@link OptionalArgumentFactory}, and he
 * {@link OptionalContainerFactory}, as well as the type mapper: {@link JodaDateTimeMapper}.
 */
public class DBIFactoryBean2 extends DBIFactoryBean
{
    /**
     * Instantiates this class.
     */
    public DBIFactoryBean2()
    {
        super();
    }

    /**
     * Creates an instance of this class using the given {@link DataSource}.
     * 
     * <p>
     * Calling this method with a <code>null</code> {@link DataSource} reference demands the invocation of the method
     * {@link #setDataSource(DataSource)}.
     * 
     * @param dataSource
     *            the data source to use. It may be <code>null</code>, however the method {@link #setDataSource(DataSource)} must be called later.
     */
    public DBIFactoryBean2(DataSource dataSource)
    {
        this.setDataSource(dataSource);
    }

    @Override
    public Object getObject() throws Exception
    {
        final DBI dbi = DBI.class.cast(super.getObject());

        dbi.registerArgumentFactory(new JodaTimeArgumentFactory());
        dbi.registerArgumentFactory(new OptionalArgumentFactory());
        dbi.registerArgumentFactory(new BigIntegerArgumentFactory());
        dbi.registerContainerFactory(new OptionalContainerFactory());

        dbi.registerMapper(new BigIntegerMapper());
        dbi.registerMapper(new JodaDateTimeMapper());

        return dbi;
    }
}
