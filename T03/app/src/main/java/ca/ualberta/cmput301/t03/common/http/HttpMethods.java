package ca.ualberta.cmput301.t03.common.http;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;

/**
 * Created by rishi on 15-10-31.
 */
public enum HttpMethods {
    GET("GET"), PUT("PUT"), POST("POST"), DELETE("DELETE");

    private final String methodName;

    HttpMethods(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return methodName;
    }

    public void setMethod(HttpURLConnection connection) {
        try {
            connection.setRequestMethod(methodName);
        } catch (ProtocolException e) {
            throw new NotImplementedException(String.format("Dev note: Typo in the HttpMethod '%s'", methodName), e);
        }
    }
}
