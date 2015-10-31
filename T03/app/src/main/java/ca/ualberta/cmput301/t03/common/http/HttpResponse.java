package ca.ualberta.cmput301.t03.common.http;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * Created by rishi on 15-10-31.
 */
public class HttpResponse {

    private int responseCode;
    private byte[] data;

    public HttpResponse(int responseCode, byte[] data) {
        this.responseCode = responseCode;
        this.data = data;
    }

    public HttpResponse() {
        this(-1, null);
    }

    public void readFromHttpURLConnection(HttpURLConnection httpConnection) throws IOException {
        responseCode = httpConnection.getResponseCode();
        InputStream in = new BufferedInputStream(httpConnection.getInputStream());
        data = IOUtils.toByteArray(in);
        in.close();
    }

    public byte[] getData() {
        return data;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
