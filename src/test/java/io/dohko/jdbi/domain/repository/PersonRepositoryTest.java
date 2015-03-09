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

import com.google.common.base.Optional;

import io.dohko.jdbi.JDBITest;
import io.dohko.jdbi.domain.Person;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class PersonRepositoryTest extends JDBITest
{
    /**
     * The repository instance to use.
     */
    private PersonRepository _repository;
    
    @Override
    @Before
    public void setUp()
    {
        super.setUp();
        this._repository = this.getBean(PersonRepository.class);
    }
    
    /**
     * Tests the insert and retrieve method of the repository.
     */
    @Test
    public void insertOnePerson()
    {
        assertThat(this._repository).isNotNull();
        
        final Person person = new Person().setBirthdate(new DateTime()).setName("Miguel");
        this._repository.insert(person);
        
        Optional<Person> present = this._repository.findByName(person.getName());
        
        assertThat(present).isNotNull();
        assertThat(present.isPresent()).isTrue();
        assertThat(present.get()).isEqualTo(person);
    }
    
    /**
     * Tests if an unknown person birthdate is represented as <code>null</code>.  
     */
    @Test
    public void findOnePersonWithUknownBirthdate()
    {
        assertThat(this._repository).isNotNull();
        
        Optional<Person> person = _repository.findByName("Alice");
        
        assertThat(person).isNotNull();
        assertThat(person.isPresent()).isTrue();
        assertThat(person.get().getBirthdate()).isNull();
        assertThat(person.get().getName()).isEqualTo("Alice");
    }
}
