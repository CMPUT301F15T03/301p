package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.parceler.Parcels;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.trading.TradeOfferComposeActivity;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class InspectItemController {
    private Item itemModel;
    private View v;
    private Activity activity;
    private Inventory inventory;
    private User owner;

    public InspectItemController(View v, Activity activity, User owner, Inventory inventory, Item item) {
        this.v = v;
        this.activity = activity;
        this.owner = owner;
        this.inventory = inventory;
        itemModel = item;
    }

    public void proposeTradeButtonClicked() {
        Intent i = new Intent(activity.getBaseContext(), TradeOfferComposeActivity.class);
        i.putExtra("trade/compose/borrower", Parcels.wrap(PrimaryUser.getInstance()));
        i.putExtra("trade/compose/owner", Parcels.wrap(owner));
        i.putExtra("trade/compose/item", Parcels.wrap(itemModel));
        activity.startActivity(i);
    }
}
