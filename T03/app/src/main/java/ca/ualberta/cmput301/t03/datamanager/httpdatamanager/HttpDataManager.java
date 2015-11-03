package ca.ualberta.cmput301.t03.datamanager.httpdatamanager;

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
import ca.ualberta.cmput301.t03.common.Preconditions;
import ca.ualberta.cmput301.t03.common.exceptions.HttpDataManagerInitializationException;
import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.common.http.HttpClient;
import ca.ualberta.cmput301.t03.common.http.HttpResponse;
import ca.ualberta.cmput301.t03.common.http.HttpStatusCode;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataKeyNotFoundException;
import ca.ualberta.cmput301.t03.datamanager.JsonDataManager;

/**
 * Created by rishi on 15-10-30.
 */
public class HttpDataManager extends JsonDataManager {
    private final String logTAG = "HTTPDataManager";

    private final HttpClient client;
    private final Context context;

    public HttpDataManager(Context context, boolean useExplicitExposeAnnotation) {
        super(useExplicitExposeAnnotation);
        this.context = Preconditions.checkNotNull(context, "context");
        String rootUrl = context.getString(R.string.httpDataManagerRootUrl);
        try {
            client = new HttpClient(Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl"));
        } catch (MalformedURLException e) {
            Log.e(logTAG, e.getMessage());
            throw new HttpDataManagerInitializationException(e.getMessage());
        }
    }

    public HttpDataManager(Context context) {
        this(context, false);
    }

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

    @Override
    public boolean deleteIfExists(DataKey key) throws IOException {
        if (!isOperational()) {
            throw new ServiceNotAvailableException("HttpDataManager is not operational. Cannot perform this operation.");
        }

        HttpResponse response = client.makeDeleteRequest(key.toString());

        if (response.getResponseCode() == HttpStatusCode.OK.getStatusCode()) {
            return true;
        }
        else if (response.getResponseCode() == HttpStatusCode.NOT_FOUND.getStatusCode()) {
            return false;
        }
        else {
            throw new NotImplementedException(String.format("Dev note: Unexpected response '%d' from the DELETE Elastic Search endpoint.",
                    response.getResponseCode()));
        }
    }

    @Override
    public boolean isOperational() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private String extractSourceFromElasticSearchHttpResponse(HttpResponse response) {
        String responseContents = new String(response.getContents());
        JsonParser jp = new JsonParser();
        JsonElement responseContentsJSON = jp.parse(responseContents);
        return responseContentsJSON.getAsJsonObject().getAsJsonObject("_source").toString();
//        Type mapType = new TypeToken<ElasticSearchResponse<Object>>(){}.getType();
//        ElasticSearchResponse<Object> elasticSearchResponse = deserialize(responseContents, mapType);
//        LinkedTreeMap map = (LinkedTreeMap)elasticSearchResponse.getSource();
//        return serialize(map, new TypeToken<LinkedTreeMap>(){}.getType());
    }
}

