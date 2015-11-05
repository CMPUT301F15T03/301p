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

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by rishi on 15-10-31.
 */
// Source: http://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
public class HttpClient {

    private URL root;

    public HttpClient(String root) throws MalformedURLException {
        this(new URL(root));
    }

    public HttpClient(URL root) {
        this.root = root;
    }

    public HttpResponse makeGetRequest(String suffix) throws IOException {
        return makeDataLessRequest(suffix, HttpMethods.GET);
    }

    public HttpResponse makeDeleteRequest(String suffix) throws IOException {
        return makeDataLessRequest(suffix, HttpMethods.DELETE);
    }

    public HttpResponse makePostRequest(String suffix, byte[] postData) throws IOException {
        return makeSendDataRequest(suffix, postData, HttpMethods.POST);
    }

    public HttpResponse makePutRequest(String suffix, byte[] putData) throws IOException {
        return makeSendDataRequest(suffix, putData, HttpMethods.PUT);
    }

    private HttpResponse makeDataLessRequest(String suffix, HttpMethods method) throws IOException {
        HttpURLConnection httpConnection = openHttpURLConnection(suffix);
        method.setMethod(httpConnection);
        httpConnection.setDoInput(true);

        HttpResponse response = new HttpResponse();
        response.readFromHttpURLConnection(httpConnection);
        httpConnection.disconnect();

        return response;
    }

    private HttpResponse makeSendDataRequest(String suffix, byte[] dataToBeSent, HttpMethods sendMethod) throws IOException {
        HttpURLConnection httpConnection = openHttpURLConnection(suffix);
        sendMethod.setMethod(httpConnection);
        httpConnection.setDoOutput(true);
        httpConnection.setDoInput(true);

        DataOutputStream out = new DataOutputStream(httpConnection.getOutputStream());
        out.write(dataToBeSent);
        out.flush();
        out.close();

        HttpResponse response = new HttpResponse();
        response.readFromHttpURLConnection(httpConnection);
        httpConnection.disconnect();

        return response;
    }

    private HttpURLConnection openHttpURLConnection(String suffix) throws IOException {
        URL queryUrl = new URL(root, suffix);
        return (HttpURLConnection) queryUrl.openConnection();
    }
}
