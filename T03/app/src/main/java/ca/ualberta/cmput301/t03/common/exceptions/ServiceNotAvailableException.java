package ca.ualberta.cmput301.t03.common.exceptions;

/**
 * Created by rishi on 15-11-01.
 */
public class ServiceNotAvailableException extends RuntimeException {
    public ServiceNotAvailableException() {
    }

    public ServiceNotAvailableException(String detailMessage) {
        super(detailMessage);
    }

    public ServiceNotAvailableException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ServiceNotAvailableException(Throwable throwable) {
        super(throwable);
    }
}
