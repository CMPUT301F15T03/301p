package ca.ualberta.cmput301.t03.commontesting;

import junit.framework.AssertionFailedError;

import ca.ualberta.cmput301.t03.common.exceptions.ExceptionUtils;

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
            throw new AssertionFailedError(
                    String.format(
                            "Expected exception of type '%s', but found '%s'.\nThrown exception's stack trace:\n"
                            + "-----BEGIN-----\n%s\n-----END-----",
                    expectedExceptionType.getName(), exception.getClass().getName(), ExceptionUtils.getStackTrace(exception)));
        }

        throw new AssertionFailedError(String.format("Expected exception of type '%s, but none thrown.",
                expectedExceptionType.getName()));
    }
}
