package ca.ualberta.cmput301.t03.common.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import junit.framework.TestCase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.ElasticSearchResponse;

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
