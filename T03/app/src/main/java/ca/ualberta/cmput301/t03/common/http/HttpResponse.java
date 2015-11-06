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

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * A class modelling the response to an HTTP request.
 * Created by rishi on 15-10-31.
 */
public class HttpResponse {

    private int responseCode;
    private byte[] contents;

    /**
     * Creates an instance of {@link HttpResponse}/
     *
     * @param responseCode The response's status code.
     * @param contents     The contents of the response. Can be null if the response had no contents.
     */
    public HttpResponse(int responseCode, byte[] contents) {
        this.responseCode = responseCode;
        this.contents = contents;
    }

    /**
     * Creates an empty {@link HttpResponse} object with null contents and "-1" (invalid) status code.
     */
    public HttpResponse() {
        this(-1, null);
    }

    /**
     * Reads the {@link HttpResponse} data from an {@link HttpURLConnection}.
     *
     * @param httpConnection The {@link HttpURLConnection} from which the response is to be read.
     * @throws IOException Thrown, if the network connection fails.
     */
    public void readFromHttpURLConnection(HttpURLConnection httpConnection) throws IOException {
        responseCode = httpConnection.getResponseCode();

        InputStream in;
        if (HttpStatusCode.isErrorCode(responseCode)) {
            in = new BufferedInputStream(httpConnection.getErrorStream());
        } else {
            in = new BufferedInputStream(httpConnection.getInputStream());
        }

        contents = IOUtils.toByteArray(in);
        in.close();

    }

    /**
     * Getter for the response contents.
     *
     * @return
     */
    public byte[] getContents() {
        return contents;
    }

    /**
     * Getter for the response's status code.
     *
     * @return
     */
    public int getResponseCode() {
        return responseCode;
    }

}
