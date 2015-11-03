package ca.ualberta.cmput301.t03.trading;

/**
 * Created by ross on 15-11-02.
 */
public class IllegalTradeStateTransition extends Throwable {
    public IllegalTradeStateTransition(String s) {
        super(s);
    }
}
