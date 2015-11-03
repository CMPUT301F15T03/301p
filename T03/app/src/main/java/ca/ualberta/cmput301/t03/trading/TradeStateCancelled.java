package ca.ualberta.cmput301.t03.trading;

/**
 * Created by ross on 15-11-02.
 */
public class TradeStateCancelled implements TradeState {
    @Override
    public Boolean isClosed() {
        return Boolean.TRUE;
    }

    @Override
    public Boolean isOpen() {
        return !isClosed();
    }

    @Override
    public void offer(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Cancelled trade cannot be offered");
    }

    @Override
    public void cancel(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Cancelled trade cannot be cancelled");
    }

    @Override
    public void accept(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Cancelled trade cannot be accepted");
    }

    @Override
    public void decline(Trade trade) throws IllegalTradeStateTransition {
        throw new IllegalTradeStateTransition("Cancelled trade cannot be declined");
    }

}