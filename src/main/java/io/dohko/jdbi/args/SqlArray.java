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

import java.util.Collection;

import static java.util.Arrays.asList;

import com.google.common.collect.Iterables;
import static com.google.common.collect.Lists.newArrayList;

public class SqlArray<T>
{
    /**
     * 
     */
    private final Object[] _elements;
    
    /**
     * 
     */
    private final Class<T> _type;

    /**
     * @param type the array type
     * @param elements the array elements
     */
    public SqlArray(Class<T> type, Collection<T> elements)
    {
        this._elements = Iterables.toArray(elements, type);
        this._type = type;
    }

    /**
     * Returns a instance of {@link SqlArray} for the given array.
     * 
     * @param type array's type. It might not be <code>null</code>
     * @param elements The array elements
     * @param <T> the elements' type
     * @return an instance of this class
     */
    @SafeVarargs
    public static <T> SqlArray<T> arrayOf(Class<T> type, T... elements)
    {
        return new SqlArray<T>(type, asList(elements));
    }

    /**
     * 
     * @param type the the type of the array's elements
     * @param elements the array elements
     * @param <T> the elements' type
     * @return an {@link SqlArray} instance.
     */
    public static <T> SqlArray<T> arrayOf(Class<T> type, Iterable<T> elements)
    {
        return new SqlArray<T>(type, newArrayList(elements));
    }

    /**
     * Returns the this array elements.
     * 
     * @return a non-null array representing the value of this array.
     */
    public Object[] getElements()
    {
        return _elements;
    }

    /**
     * Returns the type of the array's elements.
     * 
     * @return the type of the array's elements. 
     */
    public Class<T> getType()
    {
        return _type;
    }
}
