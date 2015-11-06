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

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.parceler.Parcels;

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


        User userFromIntent = Parcels.unwrap(getIntent().getParcelableExtra("user"));


        if (userFromIntent==null || PrimaryUser.getInstance().equals(userFromIntent)){
            user = PrimaryUser.getInstance();
        } else {
            user = new User(userFromIntent, getApplicationContext());
        }


        user = PrimaryUser.getInstance();
        itemModel = Parcels.unwrap(getIntent().getParcelableExtra("inventory/inspect/item"));

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
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

                controller = new InspectItemController(findViewById(R.id.edit_item_view), activity, user, inventoryModel, itemModel);
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
