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

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class PostgresStringArrayArgumentFactory implements ArgumentFactory<SqlArray<String>>
{
    @Override
    public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx)
    {
        return value instanceof SqlArray && ((SqlArray<?>) value).getType().isAssignableFrom(String.class);
    }

    @Override
    public Argument build(final Class<?> expectedType, final SqlArray<String> value, final StatementContext ctx)
    {
        return new Argument()
        {
            public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException
            {
                Array ary = ctx.getConnection().createArrayOf("varchar", value.getElements());
                statement.setArray(position, ary);
            }
        };
    }
}
