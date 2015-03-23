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
package io.dohko.jdbi.util;

import java.io.Closeable;
import java.math.BigInteger;

import io.dohko.jdbi.JDBITest;
import io.dohko.jdbi.stereotype.Repository;

import org.junit.Test;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.IDBI;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import static org.assertj.core.api.Assertions.assertThat;

public class BigIntegerMapperTest extends JDBITest
{
    @Repository
    public interface FooRepository extends Closeable
    {
        /**
         * Inserts the given name and returns the generated value.
         * 
         * @param name
         *            a name
         * @return returns the generated key value.
         */
        @GetGeneratedKeys(value = BigIntegerMapper.class)
        @SqlUpdate("INSERT INTO foo (name) VALUES (:name)")
        BigInteger insert(@Bind("name") String name);
    }
    
    @Override
    public void setUp()
    {
        super.setUp();

        try (Handle h = getBean(IDBI.class).open())
        {
            h.createCall("create table foo (id bigserial not null, name varchar(20))").invoke();
        }
    }

    /**
     * Tests if the system can returns a generated key as {@link BigInteger}. 
     */
    @Test
    public void insertOneBarRecord()
    {
        BigInteger id = getBean(FooRepository.class).insert("bar");
        
        assertThat(id).isNotNull();
    }
}
