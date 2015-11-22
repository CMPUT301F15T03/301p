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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.photo.ItemPhotoController;
import ca.ualberta.cmput301.t03.photo.PhotoGalleryView;
import ca.ualberta.cmput301.t03.user.User;

/**
 * View which displays an interface to edit an {@link Item} in a {@link User}'s {@link Inventory}.
 */
public class EditItemView extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_FILE = 2;

    private Item itemModel;
    private EditItemController controller;
    private ItemPhotoController itemPhotoController;

    private Inventory inventoryModel;

    private User user;
    private Activity activity = this;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final UUID itemUUID = UUID.fromString(getIntent().getStringExtra("ITEM_UUID"));

        user = PrimaryUser.getInstance();

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inventoryModel = user.getInventory();

                    // get the actual item model clicked
                    itemModel = inventoryModel.getItems().get(itemUUID);
                    controller = new EditItemController(findViewById(R.id.edit_item_view), activity, inventoryModel, itemModel);
                    itemPhotoController = new ItemPhotoController(itemModel);

                    // populate with fields
                    final EditText itemNameText = (EditText) findViewById(R.id.itemName);
                    final EditText itemQuantityText = (EditText) findViewById(R.id.itemQuantity);
                    final EditText itemQualityText = (EditText) findViewById(R.id.itemQuality);
                    final Spinner itemCategoryText = (Spinner) findViewById(R.id.itemCategory);
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
                            itemCategoryText.setSelection(((ArrayAdapter) itemCategoryText.getAdapter()).getPosition(itemModel.getItemCategory()));
                            itemIsPrivateCheckBox.setChecked(itemModel.isItemIsPrivate());
                            itemDescriptionText.setText(itemModel.getItemDescription());
                        }
                    });
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
        this.findViewById(R.id.itemCategory).setFocusableInTouchMode(false);
        this.findViewById(R.id.itemCategory).setEnabled(false);
        this.findViewById(R.id.itemPrivateCheckBox).setFocusable(false);
        this.findViewById(R.id.itemPrivateCheckBox).setEnabled(false);
        this.findViewById(R.id.itemDescription).setFocusable(false);

        /* BUTTON LISTENERS */
        // save to inventory listener
        Button editItemButton = (Button) findViewById(R.id.editItem);
        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.editItemButtonClicked();
            }
        });

        // save to inventory listener
        Button saveToInventoryButton = (Button) findViewById(R.id.saveItem);
        saveToInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.saveItemToInventory();
            }
        });

        // delete from inventory listener
        Button deleteItemButton = (Button) findViewById(R.id.deleteItem);
        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.deleteItemButtonClicked(itemUUID);
            }
        });

        Button uploadPhotosButton = (Button) findViewById(R.id.uploadPhotos);
        uploadPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUploadPhotosButtonClicked();
            }
        });

        Button viewImagesButton = (Button) findViewById(R.id.viewImagesbutton);
        viewImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditItemView.this, PhotoGalleryView.class);
                intent.putExtra("USER", user.getUsername());
                intent.putExtra("ITEM", itemModel.getUuid().toString());
                startActivity(intent);
            }
        });


    }

    private void onUploadPhotosButtonClicked() {
        final CharSequence[] items = { "Take A Photo", "Choose Photo from Gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(EditItemView.this);
        builder.setTitle("Attach Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                } else if (item == 1) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap tempImage = null;
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                File f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    tempImage = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            new BitmapFactory.Options());
                    f.delete();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                String tempPath = getPath(selectedImageUri, EditItemView.this);
                tempImage = BitmapFactory.decodeFile(tempPath, new BitmapFactory.Options());
            }
        }
        final Bitmap image = tempImage;
        if (tempImage != null) {
            AsyncTask worker = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {
                    itemPhotoController.addPhotoToItem(image);
                    return null;
                }
            };
            worker.execute();
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


}
