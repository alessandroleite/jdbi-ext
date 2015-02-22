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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.util.TypedMapper;

/**
 * A {@link TypedMapper} to map Joda {@link Date} objects from a {@link ResultSet}.
 */
public class JodaDateTimeMapper extends TypedMapper<DateTime>
{
    @Override
    public DateTime extractByName(ResultSet r, String name) throws SQLException
    {
        return toJodaDateTime(r, r.getTimestamp(name));
    }

    @Override
    protected DateTime extractByIndex(ResultSet r, int index) throws SQLException
    {
        return toJodaDateTime(r, r.getTimestamp(index));
    }

    private DateTime toJodaDateTime(ResultSet r, Timestamp timestamp) throws SQLException
    {
        return r.wasNull() ? null : new DateTime(timestamp.getTime());
    }
}
