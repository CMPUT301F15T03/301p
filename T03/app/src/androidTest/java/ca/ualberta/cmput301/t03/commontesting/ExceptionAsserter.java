/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of {ApplicationName}
 *
 * {ApplicationName} is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.commontesting;

import junit.framework.AssertionFailedError;

import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;

/**
 * Supplementary test methods to the JUnit test library for testing expected exceptions.
 * Created by rishi on 15-10-30.
 */
public class ExceptionAsserter {

    /**
     * Asserts that a {@link Runnable} throws an expected exception. Asserts true if an exception of
     * the provided class, or a child class is thrown.
     * @param runnable The runnable to be tested.
     * @param expectedExceptionType The {@link Class} for the expected exception.
     * @param <TException> The generic type for the expected exception.
     */
    public static <TException extends Exception> void assertThrowsException(Runnable runnable,
                                                                            Class<TException> expectedExceptionType) {
        try {
            runnable.run();
        }
        catch(Exception exception) {
            if (expectedExceptionType.isInstance(exception)) {
                return;
            }
            throw new AssertionFailedError(
                    String.format(
                            "Expected exception of type '%s', but found '%s'.\nThrown exception's stack trace:\n"
                            + "-----BEGIN-----\n%s\n-----END-----",
                    expectedExceptionType.getName(), exception.getClass().getName(), ExceptionUtils.getStackTrace(exception)));
        }

        throw new AssertionFailedError(String.format("Expected exception of type '%s', but none thrown.",
                expectedExceptionType.getName()));
    }
}
