package ca.ualberta.cmput301.t03.common.exceptions;

/**
 * Created by ross on 15-11-03.
 */
public class HttpDataManagerInitializationException extends RuntimeException {
    public HttpDataManagerInitializationException() {
        super();
    }
    public HttpDataManagerInitializationException(String message) {
        super(message);
    }
}
