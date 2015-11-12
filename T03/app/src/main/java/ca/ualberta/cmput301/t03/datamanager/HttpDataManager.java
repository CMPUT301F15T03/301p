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

package ca.ualberta.cmput301.t03.datamanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.common.http.HttpClient;
import ca.ualberta.cmput301.t03.common.http.HttpResponse;
import ca.ualberta.cmput301.t03.common.http.HttpStatusCode;

/**
 * A {@link JsonDataManager} that uses the ElasticSearch server as the storage media.
 * Created by rishi on 15-10-30.
 */
public class HttpDataManager extends JsonDataManager {
    private static final String LOG_TAG = "HTTPDataManager";

    private final HttpClient client;

    /**
     * Creates an instance of {@link HttpDataManager}.
     *
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public HttpDataManager(boolean useExplicitExposeAnnotation) {
        super(useExplicitExposeAnnotation);
        String rootUrl = TradeApp.getInstance().getString(R.string.httpDataManagerRootUrl);
        try {
            client = new HttpClient(Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl"));
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new HttpDataManagerInitializationException(e.getMessage());
        }
    }

    /**
     * Creates an instance of the {@link HttpDataManager}. The manager will set the value of
     * "useExplicitExposeAnnotation" to false.
     */
    public HttpDataManager() {
        this(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean keyExists(DataKey key) throws IOException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        HttpResponse response = client.makeGetRequest(key.toString());

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
     * {@inheritDoc}
     */
    @Override
    public <T> T getData(DataKey key, Type typeOfT) throws IOException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        HttpResponse response = client.makeGetRequest(key.toString());

        if (response.getResponseCode() == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            throw new DataKeyNotFoundException(key.toString());
        }
        if (response.getResponseCode() == HttpStatusCode.OK.getStatusCode()) {
            String sourceJson = extractSourceFromElasticSearchHttpResponse(response);
            return deserialize(sourceJson, typeOfT);
        }

        throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the GET Elastic Search endpoint.",
                response.getResponseCode()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void writeData(DataKey key, T obj, Type typeOfT) throws IOException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        byte[] requestContents = serialize(obj, typeOfT).getBytes();
        HttpResponse response = client.makePutRequest(key.toString(), requestContents);

        if (response.getResponseCode() != HttpStatusCode.OK.getStatusCode() &&
                response.getResponseCode() != HttpStatusCode.CREATED.getStatusCode()) {
            throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the PUT Elastic Search endpoint.",
                    response.getResponseCode()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteIfExists(DataKey key) throws IOException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        HttpResponse response = client.makeDeleteRequest(key.toString());
        int statusCode = response.getResponseCode();

        if (statusCode != HttpStatusCode.OK.getStatusCode() && statusCode != HttpStatusCode.NOT_FOUND.getStatusCode()) {
            throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the DELETE Elastic Search endpoint.",
                    response.getResponseCode()));
        }
    }

    /**
     * True, if the phone is online, else false.
     *
     * @return True, if the phone is online, else false.
     */
    @Override
    public boolean isOperational() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) TradeApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Returns true.
     */
    @Override
    public boolean requiresNetwork() {
        return true;
    }

    private String extractSourceFromElasticSearchHttpResponse(HttpResponse response) {
        String responseContents = new String(response.getContents());
        JsonParser jp = new JsonParser();
        JsonElement responseContentsJSON = jp.parse(responseContents);
        return responseContentsJSON.getAsJsonObject().getAsJsonObject("_source").toString();
    }
}

