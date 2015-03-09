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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import com.google.common.base.Optional;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

public class OptionalArgumentFactory implements ArgumentFactory<Optional<Object>>
{
    @Override
    public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx)
    {
        return value instanceof Optional;
    }

    @Override
    public Argument build(final Class<?> expectedType, final Optional<Object> value, final StatementContext ctx)
    {
        return new Argument()
        {
            @Override
            public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException
            {
                if (value != null && value.isPresent())
                {
                    statement.setObject(position, value.get());
                }
                else
                {
                    statement.setNull(position, Types.OTHER);
                }
            }
        };
    }
}
