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
package io.dohko.jdbi.domain.repository;

import io.dohko.jdbi.args.JodaDateTimeMapper;
import io.dohko.jdbi.binders.BindBean;
import io.dohko.jdbi.domain.Person;
import io.dohko.jdbi.domain.repository.PersonRepository.PersonResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.google.common.base.Optional;

@RegisterMapper(PersonResultSetMapper.class)
public interface PersonRepository
{
    @SqlUpdate("INSERT INTO person (name, birthdate) VALUES (:name, :birthdate)")
    void insert(@BindBean Person person);

    @SqlQuery("SELECT name, birthdate FROM person where lower(name) = lower(:name)")
    @SingleValueResult
    Optional<Person> findByName(@Bind("name") String name);
    
    @SqlQuery("SELECT name, birthdate FROM person order by lower(name)")
    List<Person> listAll();
    
    class PersonResultSetMapper implements ResultSetMapper<Person>
    {
        @Override
        public Person map(int index, ResultSet r, StatementContext ctx) throws SQLException
        {
            return new Person().setBirthdate(new JodaDateTimeMapper().extractByName(r, "birthdate"))
                               .setName(r.getString("name"));
        }
    }
}
