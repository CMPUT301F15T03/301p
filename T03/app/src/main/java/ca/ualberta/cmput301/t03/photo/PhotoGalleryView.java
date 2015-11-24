package ca.ualberta.cmput301.t03.photo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;

import java.io.IOException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

public class PhotoGalleryView extends AppCompatActivity {
    PhotoGallery model;

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
                User tempUser = new User(user, PhotoGalleryView.this.getApplicationContext());
                try {
                    model = tempUser.getInventory().getItem(itemUUID).getPhotoList();
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
                photoGridView.setAdapter(new ImageAdapter(PhotoGalleryView.this, model));
            }
        };
        worker.execute();
    }
}
