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
package io.dohko.jdbi.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import io.dohko.jdbi.PrimitivesMapperFactory2;
import io.dohko.jdbi.exceptions.AnyThrow;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

@SuppressWarnings("rawtypes")
public class FigureItOutResultSetMapper2 implements ResultSetMapper<Object>
{
    /**
     * 
     */
    private static final PrimitivesMapperFactory2 factory = new PrimitivesMapperFactory2();

    @Override
    public Object map(int index, ResultSet r, StatementContext ctx) throws SQLException
    {
        Method m = ctx.getSqlObjectMethod();
        Class<?> rt = m.getReturnType();
        GetGeneratedKeys ggk = m.getAnnotation(GetGeneratedKeys.class);
        String keyColumn = ggk.columnName();

        ResultSetMapper f;

        if (!"".equals(keyColumn))
        {
            f = figureOutMapper(rt, keyColumn);
        }
        else
        {
            f = factory.mapperFor(rt, ctx);
        }

        return f.map(index, r, ctx);
    }
    
    /**
     * Returns a mapper for the given type.
     * 
     * @param keyType the type to returns its mapper
     * @param keyColumn the column name
     * @return a mapper for the given type
     */
    ResultSetMapper figureOutMapper(Class keyType, String keyColumn)
    {
        Map<Class, ResultSetMapper> primitiveMappers = PrimitivesMapperFactory2.getPrimitiveMappers();

        ResultSetMapper f = primitiveMappers.get(keyType);

        try
        {
            Constructor<? extends ResultSetMapper> constructor = f.getClass().getConstructor(String.class);
            constructor.setAccessible(true);
            f = constructor.newInstance(keyColumn);
        }
        catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | 
                IllegalArgumentException | InvocationTargetException e)
        {
            AnyThrow.throwUncheked(e);
        }
       
        return f;
    }
}
