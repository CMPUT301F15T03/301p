package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.content.Intent;

import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.AddItemView;
import ca.ualberta.cmput301.t03.inventory.Inventory;

/**
 * Created by ross on 15-10-29.
 */
public class UserInventoryController {
    Inventory inventory;
    Context context;
    Configuration configuration;
    DataManager dataManager;

    public UserInventoryController(Context context, Inventory inventory) {
        this.inventory = inventory;
        this.context = context;

        configuration = new Configuration(context);
        try {
            dataManager = new HttpDataManager(context);
        } catch (MalformedURLException e) {
            throw new RuntimeException("There has been a issue contacting the application server.");
        }
    }

    public void addItemButtonClicked() {
//        throw new UnsupportedOperationException();
        Intent intent = new Intent(context, AddItemView.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
        context.startActivity(intent);

    }
}
