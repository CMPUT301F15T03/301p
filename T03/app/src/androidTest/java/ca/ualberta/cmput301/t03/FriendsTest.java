package ca.ualberta.cmput301.t03;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import java.util.List;

import ca.ualberta.cmput301.t03.user.FriendsList;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.InitializeUserController;

public class MockFriendsTest extends ActivityInstrumentationTestCase2{
    public MockFriendsTest(Class activityClass) {
        super(activityClass);
    }

    public static void TIMEOUT_IN_MS = 1000;
    /**
     * UC02.01.01, UC02.02.01
     */
    public void testUserCanSeeFriends(){
        MainActivity activity = (MainActivity) getActivity();

        //Precondition: user joesmith exists
        User user = new User("joesmith");

        InitializeUserController userController = InitializeUserController.getInstance();
        userController.addUser(user);

        // code from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Date: 2015-10-16
        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(FriendsListActivity.class.getName(),
                        null, false);



        activity.runOnUiThread(new Runnable() {
            public void run() {
                View v = sidebar.getChildAt(0); // Click on "friends" item to navigate to friends list
                sidebar.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // Validate that ReceiverActivity is started
        FriendsListActivity receiverActivity = (FriendsListActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                FriendsListActivity.class, receiverActivity.getClass());

        FriendsList friendsList = receiverActivity.getFriendsList();
        String friendName = "joesmith";


        //Precondition for adding a friend
        for (Friend f: friendsList){
            assertFalse(f.getUsername.equals(friendName));
        }

        //Add the new friend through the UI
        FloatingActionButton fab = receiverActivity.getAddButton();
        fab.performClick();

        EditText inputBox = receiverActivity.getInputBox();
        inputBox.setText(friendName);
        Button okButton = receiverActivity.getOKButton();
        okButton.performClick();

        //Ensure the new friend is visible in the list.
        Boolean containsFriend = Boolean.FALSE;
        for (Friend f: friendsList){
            if (f.getUsername().equals(friendName)) {
                containsFriend = Boolean.TRUE;
            }
        }
        assertTrue(containsFriend);
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
        FriendsList friendsList = user.getFriendsList();

        friendsList.addFriend(user2);
        assertTrue(friendsList.contains(user2));

    }


    /**
     * UC02.03.01
     */
    public void testRemoveFriend(){
        User user = new User("jane");
        User user2 = new User("steve");
        FriendsList friendsList = user.getFriendsList();

        friendsList.addFriend(user2);
        assertTrue(friendsList.contains(user2));

        friendsList.removeFriend(user2);
        assertFalse(friendsList.contains(user2));
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

        User user = new User("jane");
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        user.setCity("Edmonton");
        user.setPhone("780 123 4567");



        String currentName = user.getName();
        // To ensure a different name


        StringBuilder newName = new StringBuilder(currentName).append(" foo");
        user.setName(newName.toString());

        assertTrue(user.getName().equals(newName));

        // Reset back to original value
        user.setName(currentName);
    }

    /**
     * UC10.02.01
    */
    public void testEditCity() {
        User user = new User("jane");
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        user.setCity("Edmonton");
        user.setPhone("780 123 4567");

        String currentCity = user.getCity();
        String newCity = "Calgary";
        user.setCity(newCity);

        assertTrue(user.getCity().equals(newCity));

    }

    /**
    * UC10.02.01
     */
    public void testEditPhoneNumberWithValidNumber() {
        User user = new User("jane");
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        user.setCity("Edmonton");
        user.setPhone("780 123 4567");


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
        User user = new User("jane");
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        user.setCity("Edmonton");
        user.setPhone("780 123 4567");

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
        User user = new User("jane");
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        user.setCity("Edmonton");
        user.setPhone("780 123 4567");

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
        User user = new User("jane");
        user.setName("Jane Smith");
        user.setEmail("jane@example.com");
        user.setCity("Edmonton");
        user.setPhone("780 123 4567");

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
}
