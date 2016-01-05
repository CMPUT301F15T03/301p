/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Controls editing an existing item in the user's inventory.
 * Has view EditItemView and the user's item as a model.
 */
public class EditItemController {
    private Item itemModel;
    private View v;
    private Activity activity;
    private Inventory inventory;
    private Spinner categorySelect;
    private User user;

    /**
     * Default constructor for EditItemController
     * Used to initialize an instance of the EditItemController from a relevant View
     * on an instance of the Inventory model.
     * @param v The EditItemView attached to this controller
     * @param activity Used to close the EditItemView (activity) when item is successfully saved to user's inventory.
     * @param inventory The model of the user's inventory to be updated.
     * @param item The item model to be updated.
     */
    public EditItemController(View v, Activity activity, Inventory inventory, Item item) {
        this.v = v;
        this.activity = activity;
        //this.inventory = inventory;
        itemModel = item;

        user = PrimaryUser.getInstance();

        categorySelect = (Spinner) v.findViewById(R.id.itemCategory);
        categorySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = (String) parent.getItemAtPosition(position);
                itemModel.setItemCategory(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Onclick listener of Save button.
     * Saves edited item to inventory by taking EditItemView fields and resaving them to item model.
     * Should not create a new item.
     */
    public void saveItemToInventory() {
        // get fields and resave
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

        itemModel.setItemName(itemName);
        itemModel.setItemQuantity(itemQuantity);
        itemModel.setItemQuality(itemQuality);
        itemModel.setItemIsPrivate(itemIsPrivate);
        itemModel.setItemDescription(itemDescription);

        // add to inventory
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                itemModel.commitChanges();
                activity.finish();
            }
        });
        thread.start();
    }

    /**
     * Onclick listener for Edit button.
     * Enables previously uneditable fields to be edited by the user.
     */
    public void editItemButtonClicked() {
        // show save button, upload photos button, and hide edit button
        v.findViewById(R.id.saveItem).setVisibility(View.VISIBLE);
        v.findViewById(R.id.uploadPhotos).setVisibility(View.VISIBLE);
        v.findViewById(R.id.editItem).setVisibility(View.GONE);

        // set EditText views to be editable
        v.findViewById(R.id.itemName).setFocusableInTouchMode(true);
        v.findViewById(R.id.itemQuality).setFocusableInTouchMode(true);
        v.findViewById(R.id.itemQuantity).setFocusableInTouchMode(true);
        v.findViewById(R.id.itemCategory).setFocusableInTouchMode(true);
        v.findViewById(R.id.itemCategory).setEnabled(true);
        v.findViewById(R.id.itemPrivateCheckBox).setFocusable(true);
        v.findViewById(R.id.itemPrivateCheckBox).setEnabled(true);
        v.findViewById(R.id.itemDescription).setFocusableInTouchMode(true);
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
        if (itemModel.getItemCategory().length() == 0) {
            return false;
        }
        return true;
    }

    /**
     * Onclick listener for Delete button.
     * Deletes item from the user's inventory, with no warnings.
     *
     * @param itemid the UUID of the item to be deleted.
     */
    public void deleteItemButtonClicked(UUID itemid) {
        final UUID itemUUID = itemid;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inventory = user.getInventory();
                    Item toBeRemoved = inventory.getItems().get(itemUUID);
                    inventory.removeItem(toBeRemoved);
                    activity.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
            }
        });
        thread.start();
    }

}
