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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * This custom adapter aids in adding ImageViews to a ListView or GridView with bitmaps applied from
 * the PhotoGallery model.
 * <p/>
 * Modified from http://developer.android.com/guide/topics/ui/layout/gridview.html
 */
public class ImageAdapter extends BaseAdapter {
    private Activity activity;
    private Context context;
    private PhotoGallery model;

    /**
     * Set the context user by the BaseAdapter and set the model to be used in a custom fashion
     */
    public ImageAdapter(Activity activity, PhotoGallery model) {
        this.context = activity.getApplicationContext();
        this.model = model;
        this.activity = activity;
    }

    /**
     * helper to determine the number of items in our view, this will be the size of the photolist
     * in out photogallery
     *
     * @return size of list
     */
    public int getCount() {
        return model.getPhotos().size();
    }

    /**
     * helper to get the object applied to the view at a specific position, in this case a BitMap
     *
     * @param position the location in the model we should get the content from
     * @return content representing what should be set in the view
     */
    public Object getItem(int position) {
        return model.getPhotos().get(position).getPhoto();
    }

    /**
     * required getter that is never used
     *
     * @param position
     * @return
     */
    public long getItemId(int position) {
        return 0;
    }

    /**
     * create a new ImageView for each item referenced by the Adapter
     *
     * @param position    where in the list we need a new view
     * @param convertView either a new view or a recycled view that need to be filled in
     * @param parent      the parent view these are contained in, our gridview for instance
     * @return a new view with content applied.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            // we want the image to fill out the half the screen and be square
            int width = activity.getWindowManager().getDefaultDisplay().getWidth() / 2;
            imageView.setLayoutParams(new GridView.LayoutParams(width, width));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            // recycle old views
            imageView = (ImageView) convertView;
        }

        imageView.setImageBitmap((Bitmap) getItem(position));
        return imageView;
    }
}
