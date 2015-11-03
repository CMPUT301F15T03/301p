package ca.ualberta.cmput301.t03.trading;

/**
 * Created by ross on 15-10-29.
 */
public interface TradeState {
    Boolean isClosed();
    Boolean isOpen();

    void offer(Trade trade) throws IllegalTradeStateTransition;
    void cancel(Trade trade) throws IllegalTradeStateTransition;
    void accept(Trade trade) throws IllegalTradeStateTransition;
    void decline(Trade trade) throws IllegalTradeStateTransition;
}
