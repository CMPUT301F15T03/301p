package ca.ualberta.cmput301.t03.trading;

import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * Created by ross on 15-10-29.
 */
public interface TradeState {
    Boolean isClosed();

    Boolean isOpen();

    Boolean isEditable();

    void offer(Trade trade) throws IllegalTradeStateTransition;

    void cancel(Trade trade) throws IllegalTradeStateTransition;

    void accept(Trade trade) throws IllegalTradeStateTransition;

    void decline(Trade trade) throws IllegalTradeStateTransition;
}
