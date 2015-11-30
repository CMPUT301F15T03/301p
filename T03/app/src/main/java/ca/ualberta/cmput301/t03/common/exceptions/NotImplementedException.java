/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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
 * An {@link RuntimeException} thrown when an operation is not yet implemented.
 * Created by rishi on 15-10-29.
 */
public class NotImplementedException extends RuntimeException {

    /**
     * Creates an instance of {@link NotImplementedException}.
     */
    public NotImplementedException() {
        super();
    }

    /**
     * Creates an instance of {@link NotImplementedException}.
     *
     * @param message The detailed message describing the exception.
     */
    public NotImplementedException(String message) {
        super(message);
    }

    /**
     * Creates an instance of {@link NotImplementedException}.
     *
     * @param cause The cause of this exception.
     */
    public NotImplementedException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates an instance of {@link NotImplementedException}.
     *
     * @param message The detailed message describing the exception.
     * @param cause   The cause of this exception.
     */
    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
