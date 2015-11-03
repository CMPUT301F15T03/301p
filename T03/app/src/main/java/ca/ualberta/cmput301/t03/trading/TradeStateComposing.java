package ca.ualberta.cmput301.t03.trading;

import ca.ualberta.cmput301.t03.common.exceptions.NotImplementedException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * Created by ross on 15-10-29.
 */
public class TradeStateComposing implements TradeState {
    @Override
    public Boolean isClosed() {
        return Boolean.FALSE;
    }

    @Override
    public Boolean isOpen() {
        return !isClosed();
    }

    @Override
    public void offer(Trade trade) {
        trade.setState(new TradeStateOffered());
        /**
         * TODO perform tasks needed on offer
         * - notify borrower, either
         *   - passively: update elasticsearch via datamanager
         *   - actively: update elasticsearch, trigger actual notification
         *   - TODO note: Trades drawer tab should have a (1) when there's new info
         */
        throw new NotImplementedException();
    }

    @Override
    public void cancel(Trade trade) {
        trade.setState(new TradeStateCancelled());
        /**
         * TODO perform required tasks on cancel
         * - update elasticsearch
         * - notify observers
         */
        throw new NotImplementedException();
    }

    @Override
    public void accept(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Trade being composed cannot be accepted");
    }

    @Override
    public void decline(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Trade being composed cannot be declined");
    }
}
