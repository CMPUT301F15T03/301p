/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi
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

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import ca.ualberta.cmput301.t03.R;

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
        // get views and resave
        EditText itemNameText = (EditText) v.findViewById(R.id.itemName);
        String itemName = itemNameText.getText().toString();

        int itemQuantity = 1;
        EditText itemQuantityText = (EditText) v.findViewById(R.id.itemQuantity);
        if (itemQuantityText.getText().toString().length() > 0) {
            itemQuantity = Integer.parseInt(itemQuantityText.getText().toString());
        }
        EditText itemQualityText = (EditText) v.findViewById(R.id.itemQuality);
        String itemQuality = itemQualityText.getText().toString();

        EditText itemCategoryText = (EditText) v.findViewById(R.id.itemCategory);
        String itemCategory = itemCategoryText.getText().toString();

        CheckBox itemIsPrivateCheckBox = (CheckBox) v.findViewById(R.id.itemPrivateCheckBox);
        boolean itemIsPrivate = itemIsPrivateCheckBox.isChecked();

        EditText itemDescriptionText = (EditText) v.findViewById(R.id.itemDescription);
        String itemDescription = itemDescriptionText.getText().toString();

        itemModel.setItemName(itemName);
        itemModel.setItemQuantity(itemQuantity);
        itemModel.setItemQuality(itemQuality);
        itemModel.setItemCategory(itemCategory);
        itemModel.setItemIsPrivate(itemIsPrivate);
        itemModel.setItemDescription(itemDescription);

        // add to inventory
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                inventory.addItem(itemModel);
                activity.finish();
            }
        });
        thread.start();
    }

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
        v.findViewById(R.id.itemPrivateCheckBox).setFocusableInTouchMode(true);
        v.findViewById(R.id.itemDescription).setFocusableInTouchMode(true);
    }

    public void deleteItemButtonClicked() {

    }

}
