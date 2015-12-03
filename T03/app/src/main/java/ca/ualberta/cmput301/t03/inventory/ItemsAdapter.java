package ca.ualberta.cmput301.t03.inventory;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;

/**
 * Copyright 2015 John Slevinsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ItemsAdapter<T extends Inventory> extends ArrayAdapter<Item> {

    private Inventory mInventory;
    private Context mContext;

    public List<Item> getItems(){
        return mInventory.getAdaptableItems();
    }

    /**
     * Item arraylist adapter, using Inventory as a model
     *
     * @param context application context
     * @param inventory reference to the Inventory model.
     */
    public ItemsAdapter(Context context, Inventory inventory) {
        super(context, 0);
        mInventory = inventory;
        mContext = context;


        addAll(getItems());
    }

    /**
     * Item arraylist adapter, using ArrayList of items as a model
     *
     * @param context application context
     * @param items reference to the ArrayList model.
     */
    public ItemsAdapter(Context context, ArrayList<Item> items){
        this(context, new Inventory());


        Inventory i = makeInventory(items);

        notifyUpdated(i);
    }

    /**
     * Transforms an arrayList of items into an Inventory, to be adapted.
     *
     * @param items ArrayList of items
     * @return an Inventory containing the same items
     */
    private Inventory makeInventory(ArrayList<Item> items){
        Inventory inventory = new Inventory();
        for (Item i: items){
            inventory.addItem(i);
        }
        return inventory;
    }
    /**
     * Gets called by Android when tiles need to be built.
     *
     * https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Item item = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_item_tile, parent, false);
        }
        // Lookup view for data population
        TextView categoryName = (TextView) convertView.findViewById(R.id.tileViewItemCategory);
        TextView itemName = (TextView) convertView.findViewById(R.id.tileViewItemName);
        ImageView image = (ImageView) convertView.findViewById(R.id.tileViewItemImage);
        // Populate the data into the template view using the data object
        categoryName.setText(item.getItemCategory());
        itemName.setText(item.getItemName());
        try {
            image.setImageBitmap(item.getPhotoList().getPhotos().get(0).getPhoto());
        } catch (IndexOutOfBoundsException e){
            image.setImageBitmap(((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.photo_unavailable)).getBitmap());
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {

        setNotifyOnChange(false);
        clear();
        addAll(getItems());
        setNotifyOnChange(true);


        super.notifyDataSetChanged();
    }


    /**
     * If the model reference changes, call this method to update the adapter data.
     *
     * @param model new reference to Inventory model.
     */
    public void notifyUpdated(Inventory model) {

        mInventory = model;
        clear();
        addAll(getItems());
    }

    /**
     * Like noftifyUpdated(Inventory), but if the model is in ArrayList form. (It must be an
     * ArrayList of items)
     *
     *
     * @param model
     */
    public void notifyUpdated(ArrayList<Item> model){
        Inventory i = makeInventory(model);
        notifyUpdated(i);
    }
}
