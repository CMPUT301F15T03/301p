package ca.ualberta.cmput301.t03.trading;

import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * Created by ross on 15-10-29.
 */
public class TradeStateOffered implements TradeState {
    @Override
    public Boolean isClosed() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isOpen() {
        return !isClosed();
    }

    @Override
    public void offer(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Offered trade cannot be offered");
    }

    @Override
    public void cancel(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Offered trade cannot be cancelled");
    }

    @Override
    public void accept(Trade trade) {
        trade.setState(new TradeStateAccepted());
        /**
         * TODO perform required tasks on accept
         * - update elasticsearch
         * - notify observers
         */
        throw new NotImplementedException();
    }

    @Override
    public void decline(Trade trade) {
        trade.setState(new TradeStateDeclined());
        /**
         * TODO perform required tasks on decline
         * - update elasticsearch
         * - notify observers
         */
        throw new NotImplementedException();
    }

}
