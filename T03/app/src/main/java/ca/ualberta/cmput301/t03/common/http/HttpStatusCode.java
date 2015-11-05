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

package ca.ualberta.cmput301.t03.common.http;

/**
 * Major HTTP response status codes.
 * Created by rishi on 15-10-31.
 */
public enum HttpStatusCode {
    OK(200),
    CREATED(201),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    NOT_FOUND(404);

    private int statusCode;

    HttpStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Returns the int status code for the HTTP status code.
     * @return The integer associated to the HTTP status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%d", statusCode);
    }

    /**
     * Checks if the passed int status code corresponds to an error code or not.
     * @param errorCode The error code to be checked.
     * @return True, if the code corresponds to an error code.
     */
    public static boolean isErrorCode(int errorCode) {
        return errorCode >= 400;
    }

    /**
     * Checks if this {@link HttpStatusCode} corresponds to an error code or not.
     * @return True, if the code corresponds to an error code.
     */
    public boolean isErrorCode() {
        return isErrorCode(this.getStatusCode());
    }

}
