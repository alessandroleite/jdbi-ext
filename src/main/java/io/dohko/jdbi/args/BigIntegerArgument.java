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
package io.dohko.jdbi.args;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;

/**
*
* An {@link Argument} for {@link BigInteger} objects.
*/
public class BigIntegerArgument implements Argument
{
    /**
     * 
     */
    private final BigInteger _value;

    /**
     * @param value a {@link BigInteger} value. It may be <code>null</code>.
     */
    public BigIntegerArgument(BigInteger value)
    {
        this._value = value;
    }

    @Override
    public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException
    {
        if (_value == null)
        {
            statement.setNull(position, Types.BIGINT);
        }
        else 
        {
            statement.setLong(position, _value.longValue());
        }
    }

}
