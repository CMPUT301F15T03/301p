package ca.ualberta.cmput301.t03.trading;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.Adaptable;
import ca.ualberta.cmput301.t03.inventory.Item;
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
public class TradesAdapter<T extends TradeList> extends ArrayAdapter<Trade> {
    private T mTradeList;
    private Context mContext;
    private HashMap<UUID, String> tradeStates;
    private Boolean currentUserIsOwner;

    public List<Trade> getTrades(){
        try {
            return mTradeList.getAdaptableItems();
        } catch (IOException e) {
            return new ArrayList<>();
        } catch (ServiceNotAvailableException e) {
            return new ArrayList<>(); //fixme this does not belong
        }
    }

    public TradesAdapter(Context context, T tradelist) {
        super(context, 0);
        mTradeList = tradelist;
        mContext = context;
        tradeStates = new HashMap<>();
        currentUserIsOwner = false;


        notifyUpdated(mTradeList);

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

        Boolean currentUserIsOwner = false;
        Trade trade = getItem(position);
        Item mainItem = trade.getOwnersItems().getItems().get(0);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_trade_tile, parent, false);
        }
        // Lookup view for data population
        TextView categoryName = (TextView) convertView.findViewById(R.id.tradeTileMainItemCategory);
        TextView itemName = (TextView) convertView.findViewById(R.id.tradeTileMainItemName);
        ImageView image = (ImageView) convertView.findViewById(R.id.tradeTileMainItemImage);
        TextView status = (TextView) convertView.findViewById(R.id.tradeTileTradeState);
        TextView otherUser = (TextView) convertView.findViewById(R.id.tradeTileOtherUser);



        // Populate the data into the template view using the data object
        categoryName.setText(mainItem.getItemCategory());
        itemName.setText(mainItem.getItemName());
        try {
            image.setImageBitmap(mainItem.getPhotoList().getPhotos().get(0).getPhoto());
        } catch (IndexOutOfBoundsException e){
            image.setImageBitmap(((BitmapDrawable) getContext().getResources().getDrawable(R.drawable.photo_unavailable)).getBitmap());
        }
        status.setText(tradeStates.get(trade.getTradeUUID()));
        otherUser.setText(trade.getBorrower().getUsername());

        // Return the completed view to render on screen
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();


    }



    public void notifyUpdated(T model) {
        mTradeList = model;
        clear();

        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                getTrades();
                for (Trade t:mTradeList){
                    tradeStates.put(t.getTradeUUID(), t.getState().getInterfaceString(currentUserIsOwner));
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                notifyDataSetChanged();
            }
        };
        task.execute();


        addAll(getTrades());
    }

}
