package ca.ualberta.cmput301.t03.inventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.user.User;

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
public class InventoryAdapter extends ArrayAdapter<Item> implements Observer {

    private Inventory mInventory;
    private Context mContext;

    public ArrayList<Item> getItems(){
        ArrayList<Item> items = new ArrayList<>();
        for (Item i: mInventory.getItems().values()){
            items.add(i);
        }
        return items;
    }

    public InventoryAdapter(Context context, Inventory inventory) {
        super(context, 0);
        mInventory = inventory;
        mContext = context;

        addAll(getItems());
    }

    /**
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
        } catch (Exception e){
            e.printStackTrace(); //fixme do da rigt things
        }
        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void update(Observable observable) {
        addAll(getItems());
    }
}
