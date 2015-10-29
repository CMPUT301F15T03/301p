package ca.ualberta.cmput301.t03;

/**
 * Created by ross on 15-10-29.
 */
public class TradeStateOffered implements TradeState {
    @Override
    public void offer(Trade trade) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cancel(Trade trade) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void accept(Trade trade) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void decline(Trade trade) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void invalidate(Trade trade) {
        throw new UnsupportedOperationException();
    }
}
