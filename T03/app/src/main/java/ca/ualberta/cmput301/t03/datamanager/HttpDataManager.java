package ca.ualberta.cmput301.t03.datamanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.R;
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

    private final HttpClient client;
    private final Context context;

    /**
     * Creates an instance of {@link HttpDataManager}.
     * @param context The context to be used for checking the network status of the phone.
     * @param useExplicitExposeAnnotation True, if the @expose annotations are to be explicitly used,
     *                                    else false. If this is set to true, only the fields with
     *                                    the annotation @expose will be serialized/de-serialized.
     */
    public HttpDataManager(Context context, boolean useExplicitExposeAnnotation) throws MalformedURLException {
        super(useExplicitExposeAnnotation);
        this.context = Preconditions.checkNotNull(context, "context");
        String rootUrl = context.getString(R.string.httpDataManagerRootUrl);
        client = new HttpClient(Preconditions.checkNotNullOrWhitespace(rootUrl, "rootUrl"));
    }

    /**
     * Creates an instance of the {@link HttpDataManager}. The "useExplicitExposeAnnotation"
     * value is set to false.
     * @param context The context to be used for checking the network status of the phone.
     */
    public HttpDataManager(Context context) throws MalformedURLException {
        this(context, false);
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

    /**
     * True, if the phone is online, else false.
     * @return True, if the phone is online, else false.
     */
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
    }
}

