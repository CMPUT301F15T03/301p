package ca.ualberta.cmput301.t03.common.exceptions;

/**
 * Created by rishi on 15-10-30.
 */
public class ArgumentNullException extends IllegalArgumentException {
    public ArgumentNullException() {
        super();
    }

    public ArgumentNullException(String message) {
        super(message);
    }

    public ArgumentNullException(Throwable cause) {
        super(cause);
    }

    public ArgumentNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
