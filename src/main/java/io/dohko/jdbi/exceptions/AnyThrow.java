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
package io.dohko.jdbi.exceptions;

public final class AnyThrow
{
    /**
     * Avoid unnecessary instances.
     */
    private AnyThrow()
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Wraps a checked exception as an unchecked exception without creating a {@link RuntimeException}.
     * 
     * <p>
     * This is useful to avoid polluting the log with unnecessary stack traces.
     * 
     * @param exception
     *            the exception to be wrapped as an unchecked exception.
     */
    public static void throwUncheked(Throwable exception)
    {
        AnyThrow.<RuntimeException> throwAny(exception);
    }

    /**
     * Throws the given exception ({@code e}) a the given type.
     * 
     * @param e
     *            the exception to be throw as a new type.
     * @param <E>
     *            the exception type to thrown.
     * @throws E
     *             the exception that will be throw.
     */
    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAny(Throwable e) throws E
    {
        throw (E) e;
    }
}
