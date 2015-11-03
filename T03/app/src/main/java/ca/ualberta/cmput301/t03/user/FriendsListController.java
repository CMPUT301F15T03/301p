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

    public void removeFriend(User friend) throws MalformedURLException {
        mFriendsList.removeFriend(friend);
        mFriendsList.commitChanges();
    }

    public void removeFriend(String friend) throws MalformedURLException {
        removeFriend(new User(friend, mContext));
    }
}
