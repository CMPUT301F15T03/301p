package ca.ualberta.cmput301.t03.inventory;

import android.content.Context;
import android.content.Intent;

import java.net.MalformedURLException;

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
        configuration = new Configuration(context);
        try {
            dataManager = new HttpDataManager(context);
        } catch (MalformedURLException e) {
            throw new RuntimeException("There has been a issue contacting the application server.");
        }
    }

    public void addFriendFilter(Filter filter) {
        throw new UnsupportedOperationException();
    }

    public void inspectItem(Item item){
        Intent intent = new Intent(context, InspectItemView.class);
//        intent.putExtra(FRIEND_NAME, item.getItemName());
//        intent.putExtra(ITEM_NAME, item.getItemName());
        context.startActivity(intent);
    }
}
