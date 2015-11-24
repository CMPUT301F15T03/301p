package ca.ualberta.cmput301.t03.photo;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import ca.ualberta.cmput301.t03.R;

/**
 * Modified from http://developer.android.com/guide/topics/ui/layout/gridview.html
 */
public class ImageAdapter extends BaseAdapter {
    private Activity activity;
    private Context context;
    private PhotoGallery model;

    public ImageAdapter(Activity activity, PhotoGallery model) {
        this.context = activity.getApplicationContext();
        this.model = model;
        this.activity = activity;
    }

    public int getCount() {
        return model.getPhotos().size();
    }

    public Object getItem(int position) {
        return model.getPhotos().get(position).getPhoto();
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);

            int width = activity.getWindowManager().getDefaultDisplay().getWidth() / 2;
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap((Bitmap) getItem(position));
        return imageView;
    }
}
