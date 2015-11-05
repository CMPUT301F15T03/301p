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

package ca.ualberta.cmput301.t03.common.exceptions;

/**
 * {@link IllegalArgumentException} thrown when an argument to a method is null.
 * Created by rishi on 15-10-30.
 */
public class ArgumentNullException extends IllegalArgumentException {

    /**
     * Creates an instance of {@link ArgumentNullException}.
     */
    public ArgumentNullException() {
        super();
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     * @param message The detail message for this exception.
     */
    public ArgumentNullException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     * @param cause The cause of this exception, may be {@code null}.
     */
    public ArgumentNullException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance of {@link ArgumentNullException}.
     * @param message The detail message for this exception.
     * @param cause The cause of this exception, may be {@code null}.
     */
    public ArgumentNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
