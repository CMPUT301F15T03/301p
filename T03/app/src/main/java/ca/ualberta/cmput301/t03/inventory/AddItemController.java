package ca.ualberta.cmput301.t03.inventory;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.ualberta.cmput301.t03.R;

/**
 * Created by mmabuyo on 2015-11-03.
 */
public class AddItemController {
    private Item itemModel;
    private View v;
    private Context context;

    public AddItemController(Context context, View v) {
        this.v = v;
        this.context = context;
        itemModel = new Item();
    }

    public void addItemToInventory() {
        // get fields
        EditText itemNameText = (EditText) v.findViewById(R.id.itemName);
        String itemName = itemNameText.getText().toString();

        EditText itemQuantityText = (EditText) v.findViewById(R.id.itemQuantity);
        int itemQuantity = Integer.parseInt(itemQuantityText.getText().toString());

        EditText itemQualityText = (EditText) v.findViewById(R.id.itemQuality);
        String itemQuality = itemQualityText.getText().toString();

        EditText itemCategoryText = (EditText) v.findViewById(R.id.itemCategory);
        String itemCategory = itemCategoryText.getText().toString();

        CheckBox itemIsPrivateCheckBox = (CheckBox) v.findViewById(R.id.itemPrivateCheckBox);
        boolean itemIsPrivate = itemIsPrivateCheckBox.isChecked();

        EditText itemDescriptionText = (EditText) v.findViewById(R.id.itemDescription);
        String itemDescription = itemDescriptionText.getText().toString();

        // check if fields are filled out
        // checkFieldsValid()

        // setters for itemModel
        itemModel.setItemName(itemName);
        itemModel.setItemQuantity(itemQuantity);
        itemModel.setItemQuality(itemQuality);
        itemModel.setItemCategory(itemCategory);
        itemModel.setItemIsPrivate(itemIsPrivate);
        itemModel.setItemDescription(itemDescription);

        // log to see if it worked
        Log.d("ITEM", itemModel.getItemName());
        Log.d("ITEM", itemModel.getItemQuality());
        Log.d("ITEM", itemModel.getItemCategory());
        Log.d("ITEM", itemModel.getItemDescription());

        // add to inventory
    }

    public void checkFieldsValid() {
        // get fields from view

        // check for validity
    }

    public void addItemToInventoryButtonClicked() {
        addItemToInventory();
    }
}
