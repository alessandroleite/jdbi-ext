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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.util.TypedMapper;

public class BigIntegerMapper extends TypedMapper<BigInteger>
{
    /**
     * 
     */
    public static final BigIntegerMapper FIRST = new BigIntegerMapper();

    /**
     * Creates a new {@link BigIntegerMapper} instance.
     */
    public BigIntegerMapper()
    {
        super();
    }

    /**
     * Creates a new {@link BigIntegerMapper} with the given column index.
     * @param index the column index
     */
    public BigIntegerMapper(int index)
    {
        super(index);
    }
    
    /**
     * Creates a new {@link BigIntegerMapper} with the given column name.
     * 
     * @param columnName the column name
     */
    public BigIntegerMapper(String columnName)
    {
        super(columnName);
    }

    @Override
    protected BigInteger extractByName(ResultSet r, String name) throws SQLException
    {
        final BigDecimal value = r.getBigDecimal(name);
        return value != null ? value.toBigInteger() : null;
    }

    @Override
    protected BigInteger extractByIndex(ResultSet r, int index) throws SQLException
    {
        final BigDecimal value = r.getBigDecimal(index);
        return value != null ? value.toBigInteger() : null;
    }
}
