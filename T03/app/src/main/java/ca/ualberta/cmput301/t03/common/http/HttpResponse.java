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
    private byte[] contents;

    public HttpResponse(int responseCode, byte[] contents) {
        this.responseCode = responseCode;
        this.contents = contents;
    }

    public HttpResponse() {
        this(-1, null);
    }

    public void readFromHttpURLConnection(HttpURLConnection httpConnection) throws IOException {
        responseCode = httpConnection.getResponseCode();

        InputStream in;
        if (responseCode >= HttpStatusCode.BAD_REQUEST.getStatusCode()) {
            in = new BufferedInputStream(httpConnection.getErrorStream());
        }
        else {
            in = new BufferedInputStream(httpConnection.getInputStream());
        }

        contents = IOUtils.toByteArray(in);
        in.close();

    }

    public byte[] getContents() {
        return contents;
    }

    public int getResponseCode() {
        return responseCode;
    }

}
