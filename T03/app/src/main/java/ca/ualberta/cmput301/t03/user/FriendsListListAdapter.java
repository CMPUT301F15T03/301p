package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import ca.ualberta.cmput301.t03.R;

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
public class FriendsListListAdapter extends BaseAdapter {

    private FriendsList mFriendsList;
    private Context ctx;

    ArrayAdapter<User> adapter;

    FriendsListListAdapter(Context context, FriendsList friendsList){
        ctx = context;
        mFriendsList = friendsList;

        adapter = new ArrayAdapter<User>(ctx, R.layout.friends_list_item, friendsList.getFriends());

    }


    @Override
    public int getCount() {
        return mFriendsList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriendsList.getFriends().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        inflater.inflate(R.layout.friends_list_item, parent, false);

        TextView view = (TextView)convertView.findViewById(R.id.friendsListRowItemText);
        view.setText(mFriendsList.getFriends().get(position).toString());

        return view;
    }
}
