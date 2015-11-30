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
 * A {@link RuntimeException} thrown when a service is not available.
 * Created by rishi on 15-11-01.
 */
public class ServiceNotAvailableException extends Exception {

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     */
    public ServiceNotAvailableException() {
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param detailMessage The detailed message describing the exception.
     */
    public ServiceNotAvailableException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param detailMessage The detailed message describing the exception.
     * @param cause         The cause of this exception.
     */
    public ServiceNotAvailableException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }

    /**
     * Creates an instance of {@link ServiceNotAvailableException}.
     *
     * @param cause The cause of this exception.
     */
    public ServiceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
