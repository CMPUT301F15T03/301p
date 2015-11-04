package ca.ualberta.cmput301.t03.trading.exceptions;

/**
 * Created by ross on 15-11-03.
 */
public class IllegalTradeModificationException extends Throwable {
    public IllegalTradeModificationException(String msg) {
        super(msg);
    }
}
