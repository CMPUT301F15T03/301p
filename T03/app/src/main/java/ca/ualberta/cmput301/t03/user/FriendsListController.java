package ca.ualberta.cmput301.t03.user;

import android.content.Context;

import java.io.IOException;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.httpdatamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.photo.Photo;

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
        try {
            mDataManager = new HttpDataManager(context);
        } catch (MalformedURLException e) {
            throw new RuntimeException("There has been a issue contacting the application server.");
        }
    }

    public boolean isUserNameTaken(String username) throws IOException {
        return mDataManager.keyExists(new DataKey(UserProfile.type, username));
    }


    public void addFriend(User friend) {
        throw new UnsupportedOperationException();
    }

    public void addFriend(String friend) throws IOException {
        // Check if friend exists

        boolean doesUserExist = isUserNameTaken(friend);

        if (doesUserExist){
            //IF exists add it.
            User friendToAdd = new User(friend, mContext);
            mFriendsList.addFriend(friendToAdd);
            mFriendsList.commitChanges();
        } else {
            throw new UnsupportedOperationException("Friend not found");
        }

    }

    public void removeFriend(User friend) {
        throw new UnsupportedOperationException();
    }
}
