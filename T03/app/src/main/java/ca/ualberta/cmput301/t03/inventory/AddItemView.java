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
import android.support.v4.app.SharedElementCallback;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.File;
import java.io.IOException;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.photo.ItemPhotoController;
import ca.ualberta.cmput301.t03.photo.PhotoGalleryView;
import ca.ualberta.cmput301.t03.user.User;

/**
 * View which displays an interface to add an {@link Item} to a {@link User}'s {@link Inventory}.
 */
public class AddItemView extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int SELECT_FILE = 2;

    Spinner categoriesSpinner;
    private Item itemModel;
    private ItemPhotoController itemPhotoController;
    private AddItemController controller;
    private Button addToInventoryButton;
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
        setContentView(R.layout.activity_add_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = PrimaryUser.getInstance();

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inventoryModel = user.getInventory();
                    itemModel = new Item();
                    controller = new AddItemController(findViewById(R.id.add_item_view), activity, inventoryModel, itemModel);
                    itemPhotoController = new ItemPhotoController(itemModel);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
            }
        });
        worker.start();

        // Source, accessed Nov 3, 2015
        // http://developer.android.com/guide/topics/ui/controls/spinner.html#Populate
        categoriesSpinner = (Spinner) findViewById(R.id.itemCategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categoriesSpinner.setAdapter(adapter);

        addToInventoryButton = (Button) findViewById(R.id.addItem);
        addToInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.addItemToInventory();
                }
        });

        Button uploadPhotosButton = (Button) findViewById(R.id.uploadPhotos);
        uploadPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUploadPhotosButtonClicked();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (itemModel != null) {
            itemModel.clearPhotoList();
        }
        super.onBackPressed();
    }

    /**
     * When the upload photos button is clicked an alert dialog will get created asking the
     * user if they want to upload a photo or take a photo, either option will result in a returned
     * bitmap which is handled in onActivityResult.
     *
     * Code used:
     * http://stackoverflow.com/questions/27874038/how-to-make-intent-chooser-for-camera-or-gallery-application-in-android-like-wha
     */
    private void onUploadPhotosButtonClicked() {
        final CharSequence[] items = { "Take A Photo", "Choose Photo from Gallery" };

        AlertDialog.Builder builder = new AlertDialog.Builder(AddItemView.this);
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

    /**
     * Upon return from the gallery or the camera, we get the bitmap returned and add it to the item's
     * photoGallery model via the ItemPhotoController; this will ensure the photo gets sized correctly
     *
     * Code used:
     * http://stackoverflow.com/questions/27874038/how-to-make-intent-chooser-for-camera-or-gallery-application-in-android-like-wha
     * @param requestCode We set this when launching the intent to determine how to handle its return
     * @param resultCode if the intent returned with what we want, this will be RESULT_OK
     * @param data optional data returned by the activity
     */
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

                String tempPath = getPath(selectedImageUri, AddItemView.this);
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

    /**
     * Used to get the path of the file selected in the gallery selector. This is called in the
     * onActivityResult handler.
     *
     * Code used:
     * http://stackoverflow.com/questions/27874038/how-to-make-intent-chooser-for-camera-or-gallery-application-in-android-like-wha
     * @param uri the uri returned by the gallery intent
     * @param activity this activity
     * @return
     */
    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
