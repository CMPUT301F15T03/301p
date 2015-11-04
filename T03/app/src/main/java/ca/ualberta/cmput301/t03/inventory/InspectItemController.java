package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class InspectItemController {
    private Item itemModel;
    private View v;
    private Activity activity;
    private Inventory inventory;

    public InspectItemController(View v, Activity activity, Inventory inventory, Item item) {
        this.v = v;
        this.activity = activity;
        this.inventory = inventory;
        itemModel = item;
    }

    public void proposeTradeButtonClicked() {
        Toast.makeText(activity.getBaseContext(), "Propose trade!", Toast.LENGTH_LONG).show();
    }
}
