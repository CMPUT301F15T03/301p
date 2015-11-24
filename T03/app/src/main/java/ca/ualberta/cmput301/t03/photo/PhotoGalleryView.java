package ca.ualberta.cmput301.t03.photo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.File;
import java.io.IOException;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

public class PhotoGalleryView extends AppCompatActivity {
    PhotoGallery model;
    PhotoGalleryController controller;
    Boolean isPrimaryUser;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery_view);
        final String user = getIntent().getStringExtra("USER");
        final String itemUUID = getIntent().getStringExtra("ITEM");
        final GridView photoGridView = (GridView) findViewById(R.id.photoGridView);
        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                User tempUser;
                isPrimaryUser = false;
                if (user.equals(PrimaryUser.getInstance().getUsername())) {
                    tempUser = PrimaryUser.getInstance();
                    isPrimaryUser = true;
                }
                else {
                    tempUser = new User(user, PhotoGalleryView.this.getApplicationContext());
                }
                try {
                    model = tempUser.getInventory().getItem(itemUUID).getPhotoList();
                    controller = new PhotoGalleryController(model);

                    for (Photo photo : model.getPhotos()) {
                        photo.downloadPhoto();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                adapter = new ImageAdapter(PhotoGalleryView.this, model);
                photoGridView.setAdapter(adapter);
                if (isPrimaryUser) {
                    photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            // if configuration.isDownloadImagesEnabled() == false, add an option to download the photo perhaps?
                            final CharSequence[] items = {"Delete", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(PhotoGalleryView.this);
                            builder.setTitle("Delete Photo?");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (item == 0) {
                                        AsyncTask worker = new AsyncTask() {
                                            @Override
                                            protected Object doInBackground(Object[] params) {
                                                controller.removePhoto(position);
                                                return null;
                                            }
                                            @Override
                                            protected void onPostExecute(Object o) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        };
                                        worker.execute();
                                    } else if (item == 1) {
                                        // close the alert dialog
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                }
            }
        };
        worker.execute();
    }
}
