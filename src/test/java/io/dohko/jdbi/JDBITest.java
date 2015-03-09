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
    /**
     * The context created at the {@link #setUp()} method.
     */
    private ApplicationContext _context;
    
    /**
     * Creates the application context and the required resource demanded by the tests.
     */
    @Before
    public void setUp()
    {
        _context = new ClassPathXmlApplicationContext("classpath*:applicationContext-test.xml");
        
        try (Handle handle = _context.getBean(IDBI.class).open())
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
    
    /**
     * Returns an instance of the given type.
     * 
     * @param requiredType type the bean must match; can be an interface or superclass. <code>null</code>-value is prohibited 
     * @param <T> type of to be returned.
     * @return the bean instance that uniquely matches the given object type.  
     */
    protected <T> T getBean(Class<T> requiredType)
    {
        return this._context.getBean(requiredType);
    }
    
    /**
     * Drops the the tables created at the {@link #setUp()} method.
     */
    @After
    public void tearDown()
    {
        try (Handle handle = this._context.getBean(IDBI.class).open())
        {
            handle.createScript("DROP TABLE IF EXISTS person").execute();
        }
    }
}
