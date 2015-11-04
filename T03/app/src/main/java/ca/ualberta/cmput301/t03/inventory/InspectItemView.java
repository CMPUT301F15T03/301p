package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.user.User;

public class InspectItemView extends AppCompatActivity {
    private Item itemModel;
    private InspectItemController controller;
    private Button proposeTradeButton;

    private Inventory inventoryModel;

    private User user;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_inspect_item_view);

        //String itemNameClicked = getIntent().getStringExtra("ITEM_NAME");

        Configuration c = new Configuration(this.getBaseContext());
        c.getApplicationUserName();

        user = PrimaryUser.getInstance();

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inventoryModel = user.getInventory();
                    ArrayList<Item> items =  inventoryModel.getItems();

                    // get the actual item model clicked
                    itemModel = items.get(0);

                    // populate with fields
                    final EditText itemNameText = (EditText) findViewById(R.id.itemName);
                    final EditText itemQuantityText = (EditText) findViewById(R.id.itemQuantity);
                    final EditText itemQualityText = (EditText) findViewById(R.id.itemQuality);
                    final EditText itemCategoryText = (EditText) findViewById(R.id.itemCategory);
                    final CheckBox itemIsPrivateCheckBox = (CheckBox) findViewById(R.id.itemPrivateCheckBox);
                    final EditText itemDescriptionText = (EditText) findViewById(R.id.itemDescription);

                    // reference, accessed October 3, 2015
                    // http://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            itemNameText.setText(itemModel.getItemName());
                            itemQuantityText.setText(String.valueOf(itemModel.getItemQuantity()));
                            itemQualityText.setText(itemModel.getItemQuality());
                            itemCategoryText.setText(itemModel.getItemCategory());
                            itemIsPrivateCheckBox.setChecked(itemModel.isItemIsPrivate());
                            itemDescriptionText.setText(itemModel.getItemDescription());
                        }
                    });
                    //TODO: unique ids for items, or use ITEM_NAME from getExtra
                    controller = new InspectItemController(findViewById(R.id.edit_item_view), activity, inventoryModel, items.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        worker.start();

        // set EditText views to be uneditable
        this.findViewById(R.id.itemName).setFocusable(false);
        this.findViewById(R.id.itemQuality).setFocusable(false);
        this.findViewById(R.id.itemQuantity).setFocusable(false);
        this.findViewById(R.id.itemCategory).setFocusable(false);
        this.findViewById(R.id.itemPrivateCheckBox).setFocusable(false);
        this.findViewById(R.id.itemPrivateCheckBox).setEnabled(false);
        this.findViewById(R.id.itemDescription).setFocusable(false);

        /* BUTTON LISTENERS */
        // save to inventory listener
        proposeTradeButton = (Button) findViewById(R.id.proposeTradeButton);
        proposeTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.proposeTradeButtonClicked();
            }
        });

    }
}
