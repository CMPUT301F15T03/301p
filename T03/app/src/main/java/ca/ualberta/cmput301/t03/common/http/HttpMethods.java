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

import java.net.HttpURLConnection;
import java.net.ProtocolException;

import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;

/**
 * Created by rishi on 15-10-31.
 */
public enum HttpMethods {
    GET("GET"), PUT("PUT"), POST("POST"), DELETE("DELETE");

    private final String methodName;

    HttpMethods(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return methodName;
    }

    public void setMethod(HttpURLConnection connection) {
        try {
            connection.setRequestMethod(methodName);
        } catch (ProtocolException e) {
            throw new NotImplementedException(String.format("Dev note: Typo in the HttpMethod '%s'", methodName), e);
        }
    }
}
