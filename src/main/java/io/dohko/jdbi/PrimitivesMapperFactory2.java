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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.dohko.jdbi.util.BigIntegerMapper;

import org.skife.jdbi.v2.ResultSetMapperFactory;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.util.BigDecimalMapper;
import org.skife.jdbi.v2.util.BooleanMapper;
import org.skife.jdbi.v2.util.ByteArrayMapper;
import org.skife.jdbi.v2.util.ByteMapper;
import org.skife.jdbi.v2.util.DoubleMapper;
import org.skife.jdbi.v2.util.FloatMapper;
import org.skife.jdbi.v2.util.IntegerMapper;
import org.skife.jdbi.v2.util.LongMapper;
import org.skife.jdbi.v2.util.ShortMapper;
import org.skife.jdbi.v2.util.StringMapper;
import org.skife.jdbi.v2.util.TimestampMapper;
import org.skife.jdbi.v2.util.URLMapper;

/**
 * Result set mapper factory which knows how to construct java primitive types.
 * <p>
 * This class is similar to {@link org.skife.jdbi.v2.PrimitivesMapperFactory} but with a mapper for {@link BigInteger} types.
 */
@SuppressWarnings("rawtypes")
public class PrimitivesMapperFactory2 implements ResultSetMapperFactory
{
    /**
     * 
     */
    private static final Map<Class, ResultSetMapper> mappers = new HashMap<Class, ResultSetMapper>();

    static
    {
        mappers.put(BigDecimal.class, BigDecimalMapper.FIRST);
        mappers.put(BigInteger.class, BigIntegerMapper.FIRST);

        mappers.put(Boolean.class, BooleanMapper.FIRST);
        mappers.put(boolean.class, BooleanMapper.FIRST);

        mappers.put(byte[].class, ByteArrayMapper.FIRST);

        mappers.put(short.class, ShortMapper.FIRST);
        mappers.put(Short.class, ShortMapper.FIRST);

        mappers.put(Float.class, FloatMapper.FIRST);
        mappers.put(float.class, FloatMapper.FIRST);

        mappers.put(Double.class, DoubleMapper.FIRST);
        mappers.put(double.class, DoubleMapper.FIRST);

        mappers.put(Byte.class, ByteMapper.FIRST);
        mappers.put(byte.class, ByteMapper.FIRST);

        mappers.put(URL.class, URLMapper.FIRST);

        mappers.put(int.class, IntegerMapper.FIRST);
        mappers.put(Integer.class, IntegerMapper.FIRST);

        mappers.put(long.class, LongMapper.FIRST);
        mappers.put(Long.class, LongMapper.FIRST);

        mappers.put(Timestamp.class, TimestampMapper.FIRST);

        mappers.put(String.class, StringMapper.FIRST);
    }

    @Override
    public boolean accepts(Class type, StatementContext ctx)
    {
        return mappers.containsKey(type);
    }

    @Override
    public ResultSetMapper mapperFor(Class type, StatementContext ctx)
    {
        return mappers.get(type);
    }

    /**
     * Returns a read-only {@link Map} with the registered primitive mappers.
     * 
     * @return an unmodifiable {@link Map} with the defined mappers.
     */
    public static Map<Class, ResultSetMapper> getPrimitiveMappers()
    {
        return Collections.unmodifiableMap(mappers);
    }
}
