package ca.ualberta.cmput301.t03.common.exceptions;

/**
 * Created by rishi on 15-10-29.
 */
public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super();
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }
}
