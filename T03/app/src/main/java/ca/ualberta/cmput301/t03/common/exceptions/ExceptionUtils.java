package ca.ualberta.cmput301.t03.common.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by rishi on 15-10-30.
 */
public class ExceptionUtils {

    // Source: http://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string
    public static String getStackTrace(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        String stackTrace = sw.toString();
        pw.close();
        return stackTrace;
    }
}
