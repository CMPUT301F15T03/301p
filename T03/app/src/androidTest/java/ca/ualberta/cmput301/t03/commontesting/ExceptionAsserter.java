package ca.ualberta.cmput301.t03.commontesting;

/**
 * Created by rishi on 15-10-30.
 */
public class ExceptionAsserter {

    public static <TException extends Exception> void assertThrowsException(Runnable runnable,
                                                                            Class<TException> expectedExceptionType) {
        try {
            runnable.run();
        }
        catch(Exception exception) {
            if (expectedExceptionType.isInstance(exception)) {
                return;
            }
            throw exception;
        }
    }
}
