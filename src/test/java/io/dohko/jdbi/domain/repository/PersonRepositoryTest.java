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

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;

import static org.assertj.core.api.Assertions.*;

import io.dohko.jdbi.JDBITest;
import io.dohko.jdbi.domain.Person;

public class PersonRepositoryTest extends JDBITest
{
    private PersonRepository repository;
    
    @Before
    public void setUp()
    {
        super.setUp();
        
        this.repository = this.makeRepository(PersonRepository.class);
    }
    
    @Test
    public void insertOnePerson()
    {
        final Person person = new Person().setBirthdate(new DateTime()).setName("Miguel");
        this.repository.insert(person);
        
        Optional<Person> present = this.repository.findByName(person.getName());
        
        assertThat(present).isNotNull();
        assertThat(present.isPresent()).isTrue();
        assertThat(present.get()).isEqualTo(person);
    }
    
    @Test
    public void findOnePersonWithUknownBirthdate()
    {
        Optional<Person> person = repository.findByName("Alice");
        
        assertThat(person).isNotNull();
        assertThat(person.isPresent()).isTrue();
        assertThat(person.get().getBirthdate()).isNull();
        assertThat(person.get().getName()).isEqualTo("Alice");
    }
}
