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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.google.common.base.Optional;

import io.dohko.jdbi.args.JodaDateTimeMapper;
import io.dohko.jdbi.binders.BindBean;
import io.dohko.jdbi.domain.Person;
import io.dohko.jdbi.domain.repository.PersonRepository.PersonResultSetMapper;
import io.dohko.jdbi.stereotype.Repository;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


@Repository
@RegisterMapper(PersonResultSetMapper.class)
public interface PersonRepository
{
    /**
     * Inserts a new {@link Person} in the repository
     * @param person the {@link Person} to persist. It might not be <code>null</code>.
     */
    @SqlUpdate("INSERT INTO person (name, birthdate) VALUES (:name, :birthdate)")
    void insert(@BindBean Person person);

    /**
     * Finds and returns a person with a given name.
     * 
     * @param name the name to find.
     * @return the {@link Person} found with the given name or {@link Optional#absent()} if no one exists.
     */
    @SqlQuery("SELECT name, birthdate FROM person where lower(name) = lower(:name)")
    @SingleValueResult
    Optional<Person> findByName(@Bind("name") String name);

    /**
     * Returns all person available in the repository.
     * @return a non-null {@link List} with the records available in the repository.
     */
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
