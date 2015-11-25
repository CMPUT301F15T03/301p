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
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import org.parceler.Parcels;

import java.io.IOException;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.photo.PhotoGalleryView;
import ca.ualberta.cmput301.t03.user.User;

/**
 * View which displays an interface to inspect an {@link Item} in a {@link User}'s {@link Inventory}.
 */
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
        setContentView(R.layout.activity_inspect_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        User userFromIntent = Parcels.unwrap(getIntent().getParcelableExtra("user"));


        if (userFromIntent == null || PrimaryUser.getInstance().equals(userFromIntent)) {
            user = PrimaryUser.getInstance();
        } else {
            user = new User(userFromIntent, getApplicationContext());
        }

        itemModel = Parcels.unwrap(getIntent().getParcelableExtra("inventory/inspect/item"));
        // populate with fields
        EditText itemNameText = (EditText) findViewById(R.id.itemName);
        EditText itemQuantityText = (EditText) findViewById(R.id.itemQuantity);
        EditText itemQualityText = (EditText) findViewById(R.id.itemQuality);
        EditText itemCategoryText = (EditText) findViewById(R.id.itemCategory);
        CheckBox itemIsPrivateCheckBox = (CheckBox) findViewById(R.id.itemPrivateCheckBox);
        EditText itemDescriptionText = (EditText) findViewById(R.id.itemDescription);

        // reference, accessed October 3, 2015
        // http://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
        itemNameText.setText(itemModel.getItemName());
        itemNameText.setFocusable(false);

        itemQuantityText.setText(String.valueOf(itemModel.getItemQuantity()));
        itemQuantityText.setFocusable(false);

        itemQualityText.setText(itemModel.getItemQuality());
        itemQualityText.setFocusable(false);

        itemCategoryText.setText(itemModel.getItemCategory());
        itemCategoryText.setFocusable(false);

        itemIsPrivateCheckBox.setChecked(itemModel.isItemIsPrivate());
        itemIsPrivateCheckBox.setFocusable(false);
        itemIsPrivateCheckBox.setEnabled(false);

        itemDescriptionText.setText(itemModel.getItemDescription());
        itemDescriptionText.setFocusable(false);

        controller = new InspectItemController(findViewById(R.id.edit_item_view), activity, user, inventoryModel, itemModel);

        /* BUTTON LISTENERS */
        // save to inventory listener
        proposeTradeButton = (Button) findViewById(R.id.proposeTradeButton);
        proposeTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.proposeTradeButtonClicked();
            }
        });

        Button viewImagesButton = (Button) findViewById(R.id.viewImagesbutton);
        viewImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InspectItemView.this, PhotoGalleryView.class);
                intent.putExtra("USER", user.getUsername());
                intent.putExtra("ITEM", itemModel.getUuid().toString());
                startActivity(intent);
            }
        });

        AsyncTask worker = new AsyncTask() {
            public Bitmap image = null;
            @Override
            protected Object doInBackground(Object[] params) {
                    try {
                        if (user.getInventory().getItem(itemModel.getUuid()).getPhotoList().getPhotos().size() > 0) {
                            image = user.getInventory().getItem(itemModel.getUuid()).getPhotoList().getPhotos().get(0).getPhoto();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                return null;
            }
            @Override
            protected void onPostExecute(Object o) {
                if (image != null) {
                    ImageView imageView = (ImageView) findViewById(R.id.itemMainPhoto);
                    imageView.setImageBitmap(image);
                }
            }
        };
        worker.execute();


    }
}
