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
