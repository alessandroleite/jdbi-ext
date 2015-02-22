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
package io.dohko.jdbi.domain;

import org.joda.time.DateTime;

import com.google.common.base.Objects;

public class Person
{
    private String name;
    private DateTime birthdate;

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public Person setName(String name)
    {
        this.name = name;
        return this;
    }

    /**
     * @return the birthdate
     */
    public DateTime getBirthdate()
    {
        return birthdate;
    }

    /**
     * @param birthdate
     *            the birthdate to set
     */
    public Person setBirthdate(DateTime birthdate)
    {
        this.birthdate = birthdate;
        return this;
    }

    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(this.getName(), this.getBirthdate()) * 31;                
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        
        
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        
        Person other = (Person) obj;
        return Objects.equal(this.getName(), other.getName()) &&
               Objects.equal(this.getBirthdate(), other.getBirthdate());
    }
}
