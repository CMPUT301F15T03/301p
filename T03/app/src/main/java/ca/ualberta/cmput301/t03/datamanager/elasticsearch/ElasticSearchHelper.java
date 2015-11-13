package ca.ualberta.cmput301.t03.datamanager.elasticsearch;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.common.http.HttpClient;
import ca.ualberta.cmput301.t03.common.http.HttpResponse;
import ca.ualberta.cmput301.t03.common.http.HttpStatusCode;

/**
 * Created by rishi on 15-11-12.
 */
public class ElasticSearchHelper {
    private static final String LOG_TAG = "HTTPDataManager";

    private final HttpClient client;

    public ElasticSearchHelper() {
        String rootUrl = TradeApp.getContext().getString(R.string.httpDataManagerRootUrl);
        try {
            client = new HttpClient(Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl"));
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, e.getMessage());
            throw new ElasticSearchHelperInitializationException(e.getMessage());
        }
    }

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

    public void writeJson(String json, String suffix) throws IOException {
        byte[] requestContents = json.getBytes();
        HttpResponse response = client.makePutRequest(suffix, requestContents);

        if (response.getResponseCode() != HttpStatusCode.OK.getStatusCode() &&
                response.getResponseCode() != HttpStatusCode.CREATED.getStatusCode()) {
            throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the PUT Elastic Search endpoint.",
                    response.getResponseCode()));
        }
    }

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
}
