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

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.IDBI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JDBITest
{
    protected ApplicationContext context;
    
    @Before
    public void setUp()
    {
        context = new ClassPathXmlApplicationContext("classpath*:applicationContext-test.xml");
        
        try (Handle handle = context.getBean(IDBI.class).open())
        {
            handle.createCall("CREATE TABLE if not exists person (name varchar(100) not null primary key, birthdate datetime)").invoke();
            handle.createStatement("INSERT INTO person (name, birthdate) VALUES (?, ?)")
                  .bind(0, "Isaque")
                  .bind(1, new DateTime(2007, 10, 27, 13, 10))
                  .execute();
            
            handle.createStatement("INSERT INTO person (name, birthdate) VALUES (?, ?)")
                  .bind(0, "Sophie")
                  .bind(1, new DateTime(2008, 8, 05, 22, 05))
                  .execute();
            
            handle.createStatement("INSERT INTO person (name) VALUES (?)")
                  .bind(0, "Alice")
                  .execute();
        }
    }
    
    protected <T> T getRepository(Class<T> type)
    {
        return this.context.getBean(type);
    }
    
    @After
    public void tearDown()
    {
        try(Handle handle =  this.context.getBean(IDBI.class).open())
        {
            handle.createScript("DROP TABLE IF EXISTS person").execute();
        }
    }
}
