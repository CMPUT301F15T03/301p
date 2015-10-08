package com.cmput301f15t03.dreamteamsupreme.cmput301f15t03;

import android.test.ActivityInstrumentationTestCase2;

public class MockFriendsTest extends ActivityInstrumentationTestCase2{
    public MockFriendsTest(Class activityClass) {
        super(activityClass);
    }

    public void testCreateProfile() {
        //every user has a profile
        User user = new User("bobsmith", "1234 98st", "")
    }

    public void testAddFriend(){
        User user = new User();
        User user2 = new User();

        FriendsList friendsList = user.getFriendsList();
        FriendsList friendsList2 = user2.getFriendsList();

        friendsList.add(user2);
        assertTrue(friendsList.contains(user2));
        assertTrue(friendsList2.contains(user));
    }

    public void testRemoveFriend(){
        User user = new user();
        User user2 = new user();

        FriendsList friendsList = user.getFriendsList();
        FriendsList friendsList2 = user2.getFriendsList();

        friendsList.add(user2);

        assertTrue(friendsList.contains(user2));
        assertTrue(friendsList2.contains(user));

        friendsList.remove(user2);

        assertFalse(friendsList.contains(user2));
        assertFalse(friendsList2.contains(user));
    }


}
