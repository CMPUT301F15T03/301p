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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by rishi on 15-10-31.
 */
public class HttpClientTests extends TestCase {
    private HttpClient client;

    public void setUp() {
        try {
            client = new HttpClient("http://cmput301.softwareprocess.es:8080/cmput301f15t03/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void testMakePutThenGetRequest() {
        String json = "{\"foo\" : \"bar\"}";
        HttpResponse response;
        try {
            response = client.makePutRequest("testdto/123", json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(HttpStatusCode.OK.getStatusCode() == response.getResponseCode() ||
                HttpStatusCode.CREATED.getStatusCode() == response.getResponseCode());

        String responseData = new String(response.getContents());
        ElasticSearchResponse<HashMap<String, String>> responseJson = new Gson().fromJson(responseData, new TypeToken<ElasticSearchResponse<HashMap<String, String>>>(){}.getType());
        assertEquals("123", responseJson.getId());

        try {
            response = client.makeGetRequest("testdto/123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatusCode.OK.getStatusCode(), response.getResponseCode());
        responseData = new String(response.getContents());
        responseJson = new Gson().fromJson(responseData, new TypeToken<ElasticSearchResponse<HashMap<String, String>>>(){}.getType());
        assertEquals("123", responseJson.getId());
        assertTrue(responseJson.getIsFound());
        assertEquals("bar", responseJson.getSource().get("foo"));

        try {
            response = client.makeDeleteRequest("testdto/123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatusCode.OK.getStatusCode(), response.getResponseCode());
    }

    public void testMakeDeleteRequest() {
        String json = "{\"foo\" : \"bar\"}";
        HttpResponse response;
        try {
            response = client.makePutRequest("testdto/123", json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(HttpStatusCode.OK.getStatusCode() == response.getResponseCode() ||
                HttpStatusCode.CREATED.getStatusCode() == response.getResponseCode());

        try {
            response = client.makeDeleteRequest("testdto/123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatusCode.OK.getStatusCode(), response.getResponseCode());
        String responseData = new String(response.getContents());
        ElasticSearchResponse<HashMap<String, String>> responseJson = new Gson().fromJson(responseData, new TypeToken<ElasticSearchResponse<HashMap<String, String>>>(){}.getType());
        assertEquals("123", responseJson.getId());
        assertTrue(responseJson.getIsFound());
        assertNull(responseJson.getSource());

        try {
            response = client.makeGetRequest("testdto/123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatusCode.NOT_FOUND.getStatusCode(), response.getResponseCode());
        responseData = new String(response.getContents());
        responseJson = new Gson().fromJson(responseData, new TypeToken<ElasticSearchResponse<HashMap<String, String>>>(){}.getType());
        assertEquals("123", responseJson.getId());
        assertFalse(responseJson.getIsFound());
    }

    public void testMakePostThenGetRequest() {
        String json = "{\"foo\" : \"bar\"}";
        HttpResponse response;
        try {
            response = client.makePostRequest("testdto/123", json.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertTrue(HttpStatusCode.OK.getStatusCode() == response.getResponseCode() ||
                HttpStatusCode.CREATED.getStatusCode() == response.getResponseCode());

        String responseData = new String(response.getContents());
        ElasticSearchResponse<HashMap<String, String>> responseJson = new Gson().fromJson(responseData, new TypeToken<ElasticSearchResponse<HashMap<String, String>>>(){}.getType());
        assertEquals("123", responseJson.getId());

        try {
            response = client.makeGetRequest("testdto/123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatusCode.OK.getStatusCode(), response.getResponseCode());
        responseData = new String(response.getContents());
        responseJson = new Gson().fromJson(responseData, new TypeToken<ElasticSearchResponse<HashMap<String, String>>>(){}.getType());
        assertEquals("123", responseJson.getId());
        assertTrue(responseJson.getIsFound());
        assertEquals("bar", responseJson.getSource().get("foo"));

        try {
            response = client.makeDeleteRequest("testdto/123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        assertEquals(HttpStatusCode.OK.getStatusCode(), response.getResponseCode());
    }
}
