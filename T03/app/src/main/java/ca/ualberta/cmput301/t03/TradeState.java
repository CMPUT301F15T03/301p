package ca.ualberta.cmput301.t03;

/**
 * Created by ross on 15-10-29.
 */
public interface TradeState {
    void offer(Trade trade);
    void cancel(Trade trade);
    void accept(Trade trade);
    void decline(Trade trade);
    void invalidate(Trade trade);
}
