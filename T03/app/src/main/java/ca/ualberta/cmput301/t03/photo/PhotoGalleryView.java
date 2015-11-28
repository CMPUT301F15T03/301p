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

package ca.ualberta.cmput301.t03.photo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.io.IOException;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.user.User;

/**
 * In this activity the user can view all photos attached to an item, this can be accessed from
 * EditItemView and InspectItemView
 * <p/>
 * a USER and ITEM uuid are to be passed as extras into this activity
 */
public class PhotoGalleryView extends AppCompatActivity {
    PhotoGallery model;
    PhotoGalleryController controller;
    Boolean isPrimaryUser;
    ImageAdapter adapter;

    /**
     * The purpose of onCreate in this activity is to load the models that are referenced by Extras
     * In this case they ar ethe user and the item which has photos. once the models are loaded
     * the gridview is adapted to show the photo bitmaps.
     * <p/>
     * All operations of deleting photos are deferred to the controller
     *
     * @param savedInstanceState
     */
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
                } else {
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
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
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
                            final CharSequence[] items = {"Download", "Delete", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(PhotoGalleryView.this);
                            builder.setTitle("Modify Photo");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (item == 0) {
                                        AsyncTask worker = new AsyncTask() {
                                            @Override
                                            protected Object doInBackground(Object[] params) {
                                                controller.downloadPhoto(position);
                                                return null;
                                            }

                                            @Override
                                            protected void onPostExecute(Object o) {
                                                adapter.notifyDataSetChanged();
                                            }
                                        };
                                        worker.execute();
                                    }
                                    else if (item == 1) {
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
                                    } else if (item == 2) {
                                        // close the alert dialog
                                    }
                                }
                            });
                            builder.show();
                        }
                    });
                }

                else {
                    photoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            // if configuration.isDownloadImagesEnabled() == false, add an option to download the photo perhaps?
                            final CharSequence[] items = {"Download", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(PhotoGalleryView.this);
                            builder.setTitle("Modify Photo");
                            builder.setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (item == 0) {
                                        AsyncTask worker = new AsyncTask() {
                                            @Override
                                            protected Object doInBackground(Object[] params) {
                                                controller.downloadPhoto(position);
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
