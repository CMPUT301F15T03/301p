package com.cmput301f15t03.dreamteamsupreme.cmput301f15t03;

import android.test.ActivityInstrumentationTestCase2;

import java.net.UnknownServiceException;
import java.util.List;

public class MockFriendsTest extends ActivityInstrumentationTestCase2{
    public MockFriendsTest(Class activityClass) {
        super(activityClass);
    }

    private static final String EXISTING_TEST_USER_NAME = "Test User";

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
    public void testCreateDuplicateProfile() {
        String username = "bobsmith";
        User user = new User(username);
        assertTrue(user.getName().equals(name));

        //grrr, junit3 does not have assertRaises...
        try {
            User user2 = new User(username);
            assertTrue("Exception should've been thrown", false);
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
        FriendsList friendsList = FriendsListManager.getFriendsList();

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
        FriendsList friendsList = FriendsListManager.getFriendsList();

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


    /**
     * UC02.05.01
     */
    public void testViewFriendProfile(){
        User user = new User("jane");
        User user2 = new User("steve");
        user2.setCity("Edmonton");
        user2.setEmail("abc@example.com");

        FriendsList friendsList = FriendsListManager.getFriendsList();

        friendsList.newFriendRequest(user, user2);

        FriendRequestList requests = friendsList.getRequests(user2);
        FriendRequest request = requests.get(0);
        request.accept();

        assertTrue(friendsList.areFriends(user, user2));
        

        List<User> friendsOf = friendsList.getFriendsOf(user);

        User otherUser = friendsOf.get(0);

        assertTrue(otherUser.getUsername().equals(user2.getUsername()));
        assertTrue(otherUser.getEmail().equals(user2.getEmail()));
        assertTrue(otherUser.getPhoneNumber().equals(user2.getPhoneNumber()));

    }

    /**
    * UC10.02.01
    */
    public void testEditName() {
        User user = UserFactory.getExistingUser(EXISTING_TEST_USER_NAME);
        String currentName = user.getName();
        // To ensure a different name
        String newName = new StringBuilder(currentName).append(" foo");
        user.setName(newName);

        assertTrue(user.getName().equals(newName));

        // Reset back to original value
        user.setName(currentName);
    }

    /**
     * UC10.02.01
    */
    public void testEditAddress() {
        User user = UserFactory.getExistingUser(EXISTING_TEST_USER_NAME);
        String currentAddress = user.getAddress();
        String newAddress = new StringBuilder(currentName).append(" foo");
        user.setAddress(newAddress);

        assertTrue(user.getAddress().equals(newAddress));

        user.setAddress(currentAddress);
    }

    /**
    * UC10.02.01
     */
    public void testEditPhoneNumberWithValidNumber() {
        User user = UserFactory.getExistingUser(EXISTING_TEST_USER_NAME);
        String currentPhoneNumber = user.getPhoneNumber();
        String newNumber = "403 113 1232";
        if (newNumber.equals(currentPhoneNumber))
        {
            newNumber = "404 113 1232";
        }
        user.setPhoneNumber(newNumber);

        assertTrue(user.getPhoneNumber().equals(newNumber));

        user.setPhoneNumber(currentPhoneNumber);
    }

    /**
    * UC10.02.01
    */
    public void testEditPhoneNumberWithInValidNumber() {
        User user = UserFactory.getExistingUser(EXISTING_TEST_USER_NAME);
        String currentPhoneNumber = user.getPhoneNumber();
        String newNumber = "hello";
        try {
            user.setPhoneNumber(newNumber);
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertTrue(user.getPhoneNumber().equals(currentPhoneNumber));
    }

    /**
    * UC10.02.01
    */
    public void testEditEmailWithValidEmail() {
        User user = UserFactory.getExistingUser(EXISTING_TEST_USER_NAME);
        String currentEmail = user.getEmail();
        String newEmail = "foo@bar.com";
        if (newEmail.equals(currentEmail))
        {
            newEmail = "bar@foo.com";
        }
        user.setEmail(newEmail);

        assertTrue(user.getEmail().equals(newEmail));

        user.setEmail(currentEmail);
    }

    /**
    * UC10.02.01
    */
    public void testEditEmailWithInvalidEmail() {
        User user = UserFactory.getExistingUser(EXISTING_TEST_USER_NAME);
        String currentEmail = user.getEmail();
        String newEmail = "foo@barcom";

        try {
            user.setEmail(newEmail);
            assertTrue(false);
        }
        catch (IllegalArgumentException e) {
            assertTrue(true);
        }

        assertTrue(user.getEmail().equals(currentEmail));
    }

    // TODO: Add more tests as more mutable fields are added
}
