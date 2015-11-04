/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi
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

public class FriendsListListAdapter extends BaseAdapter {

    ArrayAdapter<User> adapter;
    private FriendsList mFriendsList;
    private Context ctx;

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
