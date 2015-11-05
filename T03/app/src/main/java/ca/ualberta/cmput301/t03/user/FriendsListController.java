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

package ca.ualberta.cmput301.t03.user;

import android.content.Context;

import java.io.IOException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;

/**
 * Created by ross on 15-10-29.
 */
public class FriendsListController {

    FriendsList mFriendsList;
    Context mContext;
    Configuration mConfiguration;
    DataManager mDataManager;

    public FriendsListController(Context context, FriendsList friendsList){
        mFriendsList = friendsList;
        mContext = context;

        mConfiguration = new Configuration(context);
        mDataManager = new HttpDataManager(context);
    }

    public boolean isUserNameTaken(String username) throws IOException {
        return mDataManager.keyExists(new DataKey(UserProfile.type, username));
    }


    public void addFriend(User friend) throws UserNotFoundException, IOException, UserAlreadyAddedException {
        // Check if friend is already added...

        if (mFriendsList.containsFriend(friend)){
            throw new UserAlreadyAddedException("User %s is already in your friends list!");
        }

        // Check if friend exists
        boolean doesUserExist = isUserNameTaken(friend.getUsername());

        if (doesUserExist){
            //IF exists add it.
            User friendToAdd = friend;
            mFriendsList.addFriend(friendToAdd);
            mFriendsList.commitChanges();
        } else {
            throw new UserNotFoundException("Friend not found");
        }
    }

    public void addFriend(String friend) throws IOException, UserNotFoundException, UserAlreadyAddedException {
        addFriend(new User(friend, mContext));
    }

    public void removeFriend(User friend) {
        mFriendsList.removeFriend(friend);
        mFriendsList.commitChanges();
    }

    public void removeFriend(String friend) {
        removeFriend(new User(friend, mContext));
    }
}
