package ca.ualberta.cmput301.t03.trading;

import android.content.Context;
import android.util.Log;

import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * Created by ross on 15-10-29.
 */
public class TradeOfferReviewController {
    private final String logTAG = "TradeOfferReview";

    private Context context;
    private Trade model;

    public TradeOfferReviewController(Context context, Trade model) {
        this.context = context;
        this.model = model;
    }

    public void acceptTrade() {
        try {
            model.accept();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    public void declineTrade() {
        try {
            model.decline();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    public void declineAndCounterTrade() {
        try {
            model.decline();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
        /**
         * TODO perform required tasks for counter offer
         * - create a new trade
         * - send to compose trade view
         */
    }
}