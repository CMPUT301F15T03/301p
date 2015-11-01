package ca.ualberta.cmput301.t03.common;

import java.util.Collection;

import ca.ualberta.cmput301.t03.common.exceptions.ArgumentNullException;

/**
 * Created by rishi on 15-10-30.
 */
public class Preconditions {
    public static <T> T checkNotNull(T obj, String argumentName) throws ArgumentNullException {
        if (obj == null) {
            throw new ArgumentNullException(String.format("Object '%s' can't be null.", argumentName));
        }
        return obj;
    }

    public static <T extends Collection<?>> T checkNotNullOrEmpty(T obj, String argumentName) throws IllegalArgumentException {
        checkNotNull(obj, argumentName);
        if (obj.size() == 0) {
            throw new IllegalArgumentException(String.format("Object '%s' can't be empty.", argumentName));
        }
        return obj;
    }

    public static String checkNotNullOrWhitespace(String str, String argumentName) throws IllegalArgumentException {
        checkNotNull(str, argumentName);
        if (str.trim().isEmpty()) {
            throw new IllegalArgumentException(String.format("String '%s' can't be whitespace.", argumentName));
        }
        return str;
    }
}
