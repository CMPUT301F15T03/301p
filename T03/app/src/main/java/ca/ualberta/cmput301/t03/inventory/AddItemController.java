/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of {ApplicationName}
 *
 * {ApplicationName} is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.inventory;
import ca.ualberta.cmput301.t03.R;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Controls adding an item to user's inventory.
 * Has view AddItemView and the user's Inventory as its model.
 */
public class AddItemController {
    Spinner categorySelect;
    private Item itemModel;
    private View v;
    private Activity activity;
    private Inventory inventory;

    /**
     * Default constructor for AddItemController.
     * Used to initialize an instance of the AddItemController from a relevant View
     * on an instance of the Inventory model.
     * @param v The AddItemView attached to this controller
     * @param activity Used to close the AddItemView (activity) when item is successfully added to inventory
     * @param inventory The model that will be updated once item is successfully added.
     */

    public AddItemController(View v, Activity activity, Inventory inventory, Item item) {
        this.v = v;
        this.activity = activity;
        this.inventory = inventory;
        this.itemModel = item;

        // Source, accessed Nov 3, 2015
        // http://developer.android.com/guide/topics/ui/controls/spinner.html#Populate
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

            }
        });
    }

    /**
     * On-click listener for the Add button.
     * This function grabs values from the AddItem fields and stores them in a private item model.
     * It successfully adds an item to inventory if the fields are valid, and closes the activity.
     */
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

    /**
     * Checks if item's fields are valid.
     * Required fields include the item name and category. Only the length of the name (>0) is checked.
     * The Spinner listener sets the item's category and that is also checked.
     * More validity checks can be added.
     * @return true if fields are valid; else false.
     */
    public boolean checkFieldsValid() {
        EditText itemNameText = (EditText) v.findViewById(R.id.itemName);
        String itemName = itemNameText.getText().toString();

        // the only things required are item name and category
        if (itemName.length() == 0) {
            return false;
        }
        if (itemModel.getItemCategory().length() == 0) { //todo npe here
            return false;
        }
        return true;
    }

}
