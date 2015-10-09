package com.cmput301f15t03.dreamteamsupreme.cmput301f15t03;

import android.test.ActivityInstrumentationTestCase2;

import java.net.UnknownServiceException;

public class MockFriendsTest extends ActivityInstrumentationTestCase2{
    public MockFriendsTest(Class activityClass) {
        super(activityClass);
    }

    /**
     * UC02.04.01
     */
    public void testCreateProfile() {
        String username = "bobsmith";
        User user = new User(username);
        assertTrue(user.getName().equals(name));

        String address = "123 sesame street";
        user.setAddress(address);
        assertTrue(user.getAddress().equals(address));

        String email = "abc@example.com";
        user.setEmail(email);
        assertTrue(user.getEmail().equals(email));

        String cellPhone = "780 123 4567";
        user.setPhoneNumber(cellPhone);
        assertTrue(user.getPhoneNumber().equals(cellPhone));
    }

    /**
     * UC02.04.01
     */
    public void testCreateDuplicateProile() {
        String username = "bobsmith";
        User user = new User(username);
        assertTrue(user.getName().equals(name));

        //grrr, junit3 does not have assertRaises...
        try {
            User user2 = new User(username);
        } catch(DuplicateUsernameException e){
            assertTrue("Duplicate username exception", true);
        }

    }

    /**
     * UC02.02.01
     */
    public void testAddFriend(){
        User user = new User("jane");
        User user2 = new User("steve");
        FriendsList friendsList = getFriendsList();

        friendsList.newFriendRequest(user, user2);

        FriendRequestList requests = friendsList.getRequests(user2);
        FriendRequest request = requests.get(0);
        request.accept();

        assertTrue(friendsList.areFriends(user, user2));
    }

    /**
     * UC02.03.01
     */
    public void testRemoveFriend(){
        User user = new User("jane");
        User user2 = new User("steve");
        FriendsList friendsList = getFriendsList();

        friendsList.newFriendRequest(user, user2);

        FriendRequestList requests = friendsList.getRequests(user2);
        FriendRequest request = requests.get(0);
        request.accept();

        assertTrue(friendsList.areFriends(user, user2));

        friendsList.removeFriendship(user, user2);

        assertFalse(friendsList.areFriends(user, user2));
    }

    /**
     * UC02.01.01
     */
    public void testAddFavourite(){
        User user = new User("jane");
        User user2 = new User("steve");

        user.addFavourite(user2);

        List<Favourites> favouritesList = user.getFavourites();

        assertTrue(favouritesList.contains(user2));
    }

    /**
     * UC02.01.01
     */
    public void testRemoveFavourite(){
        User user = new User("jane");
        User user2 = new User("steve");

        user.addFavourite(user2);

        List<Favourites> favouritesList = user.getFavourites();

        assertTrue(favouritesList.contains(user2));

        user.removeFavourite(user2);

        favouritesList = user.getFavourites();

        assertFalse(favouritesList.contains(user));
    }

}
