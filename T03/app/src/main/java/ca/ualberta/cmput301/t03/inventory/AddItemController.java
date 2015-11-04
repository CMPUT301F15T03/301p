package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ca.ualberta.cmput301.t03.R;

/**
 * Created by mmabuyo on 2015-11-03.
 */
public class AddItemController {
    private Item itemModel;
    private View v;
    private Activity activity;
    private Inventory inventory;
    Spinner categorySelect;

    public AddItemController(View v, Activity activity, Inventory inventory) {
        this.v = v;
        this.activity = activity;
        this.inventory = inventory;
        itemModel = new Item();

        // listeners
        categorySelect = (Spinner) v.findViewById(R.id.itemCategory);
        categorySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getItemAtPosition(position);
                itemModel.setItemCategory(category);

                Log.d("ITEM", itemModel.getItemCategory());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(v, "Please fill in the required fields.", Toast.LENGTH_LONG).show();
//                return;
            }
        });
    }


    public void addItemToInventory() {
        // get fields
        EditText itemNameText = (EditText) v.findViewById(R.id.itemName);
        String itemName = itemNameText.getText().toString();

        int itemQuantity = 1;
        EditText itemQuantityText = (EditText) v.findViewById(R.id.itemQuantity);
        if (itemQuantityText.getText().toString().length() > 0) {
            itemQuantity = Integer.parseInt(itemQuantityText.getText().toString());
        }
        EditText itemQualityText = (EditText) v.findViewById(R.id.itemQuality);
        String itemQuality = itemQualityText.getText().toString();

        CheckBox itemIsPrivateCheckBox = (CheckBox) v.findViewById(R.id.itemPrivateCheckBox);
        boolean itemIsPrivate = itemIsPrivateCheckBox.isChecked();

        EditText itemDescriptionText = (EditText) v.findViewById(R.id.itemDescription);
        String itemDescription = itemDescriptionText.getText().toString();

        if (!checkFieldsValid()) {
            Toast.makeText(activity, "Please fill in the required fields.", Toast.LENGTH_LONG).show();
            return;
        }

        // setters for itemModel
        itemModel.setItemName(itemName);
        itemModel.setItemQuantity(itemQuantity);
        itemModel.setItemQuality(itemQuality);
        itemModel.setItemIsPrivate(itemIsPrivate);
        itemModel.setItemDescription(itemDescription);

        // add to inventory
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                inventory.addItem(itemModel);
                // close view, should go back to inventory view, which should have been updated with the new item!
                activity.finish();
            }
        });
        thread.start();

    }

    public boolean checkFieldsValid() {
        EditText itemNameText = (EditText) v.findViewById(R.id.itemName);
        String itemName = itemNameText.getText().toString();

        // the only things required are item name and category
        if (itemName.length() == 0) {
            return false;
        }

        return true;
    }


    public void addItemToInventoryButtonClicked() {
        addItemToInventory();
    }
}
