package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.view.View;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class EditItemController {
    private Item itemModel;
    private View v;
    private Activity activity;
    private Inventory inventory;

    public EditItemController(View v, Activity activity, Inventory inventory, Item item) {
        this.v = v;
        this.activity = activity;
        this.inventory = inventory;
        itemModel = item;
    }

    public void saveItemToInventory() {

        throw new UnsupportedOperationException();

    }

    public void saveItemToInventoryButtonClicked() {
        saveItemToInventory();
    }

}
