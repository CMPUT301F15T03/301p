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

package ca.ualberta.cmput301.t03.datamanager.elasticsearch;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import com.udeyrishi.simplehttpclient.HttpClient;
import com.udeyrishi.simplehttpclient.HttpResponse;
import com.udeyrishi.simplehttpclient.HttpStatusCode;

/**
 * A set for helped APIs for interacting with Elastic Search server.
 * Created by rishi on 15-11-12.
 */
public class ElasticSearchHelper {
    private static final String LOG_TAG = "HTTPDataManager";

    private final HttpClient client;

    /**
     * Creates an instance of {@link ElasticSearchHelper}. The root URL is retrieved from the
     * {@link ca.ualberta.cmput301.t03.R.string#elasticSearchRootUrl}.
     */
    public ElasticSearchHelper() {
        String rootUrl = TradeApp.getContext().getString(R.string.elasticSearchRootUrl);
        try {
            client = new HttpClient(Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl"));
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new ElasticSearchHelperInitializationException(e.getMessage());
        }
    }

    /**
     * Makes an HTTP GET request at the URL formed with the provided suffix, and returns the
     * received JSON String if an HTTP OK is received. If HTTP NOT-FOUND is received,
     * {@link android.content.res.Resources.NotFoundException} is thrown.
     * @param suffix The suffix to be used for making the request.
     * @return The retrieved JSON string if the HTTP OK is received.
     * @throws IOException Thrown if the network communication fails.
     */
    public String getJson(String suffix) throws IOException {
        HttpResponse response = client.makeGetRequest(suffix);

        if (response.getResponseCode() == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            throw new Resources.NotFoundException(suffix);
        }
        if (response.getResponseCode() == HttpStatusCode.OK.getStatusCode()) {
            return extractSourceFromElasticSearchHttpResponse(response);
        }

        throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the GET Elastic Search endpoint.",
                response.getResponseCode()));
    }

    /**
     * Makes an HTTP PUT request at the URL formed with the provided suffix, and checks for the
     * response code to be a successful one.
     * @param json The JSON string to be sent.
     * @param suffix The suffix to be used for making the request.
     * @return The response to the request.
     * @throws IOException Thrown if the network communication fails.
     */
    public String putJson(String json, String suffix) throws IOException {
        byte[] requestContents = json.getBytes();
        HttpResponse response = client.makePutRequest(suffix, requestContents);
        return extractResponseString(response);
    }

    /**
     * Makes an HTTP POST request at the URL formed with the provided suffix, and checks for the
     * response code to be a successful one.
     * @param json The JSON string to be sent.
     * @param suffix The suffix to be used for making the request.
     * @return The response to the request.
     * @throws IOException Thrown if the network communication fails.
     */
    public String postJson(String json, String suffix) throws IOException {
        byte[] requestContents = json.getBytes();
        HttpResponse response = client.makePostRequest(suffix, requestContents);
        return extractResponseString(response);
    }

    /**
     * Makes an HTTP GET request at the path formed with provided suffix, and checks for the response
     * to see if the path exists.
     * @param suffix The suffix to be used for making the request.
     * @return If the HTTP response is OK, returns true. If the response is NOT-FOUND, returns false.
     * @throws IOException Thrown if the network communication fails.
     */
    public boolean checkPathExists(String suffix) throws IOException {
        HttpResponse response = client.makeGetRequest(suffix);

        if (response.getResponseCode() == HttpStatusCode.OK.getStatusCode()) {
            return true;
        }
        if (response.getResponseCode() == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            return false;
        }

        throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the GET Elastic Search endpoint.",
                response.getResponseCode()));
    }

    /**
     * Sends an HTTP DELETE request at the path formed with the provided suffix.
     * @param suffix The suffix to be used for making the request.
     * @return True, if the response is OK (i.e., path existed). False, if the response is NOT-FOUND
     *         (i.e., path didn't exist).
     * @throws IOException Thrown if the network communication fails.
     */
    public boolean sendDeleteRequestAtPath(String suffix) throws IOException {
        HttpResponse response = client.makeDeleteRequest(suffix);
        int statusCode = response.getResponseCode();

        if (statusCode == HttpStatusCode.OK.getStatusCode()) {
            return true;
        }
        if (statusCode == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            return false;
        }

        throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the DELETE Elastic Search endpoint.",
                response.getResponseCode()));
    }

    private String extractSourceFromElasticSearchHttpResponse(HttpResponse response) {
        String responseContents = new String(response.getContents());
        JsonParser jp = new JsonParser();
        JsonElement responseContentsJSON = jp.parse(responseContents);
        return responseContentsJSON.getAsJsonObject().getAsJsonObject("_source").toString();
    }


    @NonNull
    private String extractResponseString(HttpResponse response) {
        String requestResponse = new String(response.getContents());
        if (response.getResponseCode() != HttpStatusCode.OK.getStatusCode() &&
                response.getResponseCode() != HttpStatusCode.CREATED.getStatusCode()) {
            throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the POST/PUT Elastic Search endpoint.: %s",
                    response.getResponseCode(), new String(response.getContents())));
        }

        return requestResponse;
    }
}
