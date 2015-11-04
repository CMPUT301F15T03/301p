package ca.ualberta.cmput301.t03.inventory;

import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import ca.ualberta.cmput301.t03.Filter;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;

/**
 * Created by ross on 15-10-29.
 */
public class BrowseInventoryController {
    public final static String FRIEND_NAME = null;
    public final static String ITEM_NAME = null;

    BrowsableInventories browsableInventories;
    Context context;
    Configuration configuration;
    DataManager dataManager;

    public BrowseInventoryController(Context context, BrowsableInventories browsableInventories) {
        this.context = context;
        this.browsableInventories = browsableInventories;
        this.configuration = new Configuration(context);
        this.dataManager = new HttpDataManager(context);
    }

    public void addFriendFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    public void inspectItem(Item item){
        Intent intent = new Intent(context, InspectItemView.class);
        intent.putExtra("inventory/inspect/item", Parcels.wrap(item));
        context.startActivity(intent);
    }
}
