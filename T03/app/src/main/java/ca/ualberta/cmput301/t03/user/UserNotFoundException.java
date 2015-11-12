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

package ca.ualberta.cmput301.t03.user;

/**
 * Exception raised when a user is not found.
 *
 * Thrown in controller, caught in view.
 */
public class UserNotFoundException extends Exception {

    /**
     * Creates a UserNotFoundException without any message.
     *
     * It is preferable to use the String: constructor instead.
     */
    public UserNotFoundException() {
        super();
    }

    /**
     * Creates a UserNotFoundException with a custom detail message.
     * @param detailmessage More details about the exception
     */
    public UserNotFoundException(String detailmessage) {
        super(detailmessage);
    }

}
