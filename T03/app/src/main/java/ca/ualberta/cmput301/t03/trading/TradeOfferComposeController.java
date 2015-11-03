package ca.ualberta.cmput301.t03.trading;

import android.content.Context;
import android.util.Log;

import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;

/**
 * Created by ross on 15-10-29.
 */
public class TradeOfferComposeController {
    private final String logTAG = "TradeOfferCompose";
    private Context context;

    private Trade model;

    public TradeOfferComposeController(Context context, Trade model) {
        this.model = model;
        this.context = context;
    }

    public void offerTrade() {
        try {
            model.offer();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    public void cancelTrade() {
        try {
            model.cancel();
        } catch (IllegalTradeStateTransition illegalTradeStateTransition) {
            Log.e(logTAG, illegalTradeStateTransition.getMessage());
        }
    }

    public void addBorrowerItem(Item item) {
        model = new Trade(model.getBorrower(), model.getOwner(),
                model.getBorrowersItems(), model.getOwnersItems(), this.context);
    }
}
