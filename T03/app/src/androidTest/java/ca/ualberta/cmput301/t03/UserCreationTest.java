//package ca.ualberta.cmput301.t03;
//
//import android.app.Activity;
//import android.app.Instrumentation;
//import android.test.ActivityInstrumentationTestCase2;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.widget.Button;
//import android.widget.EditText;
//
//import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
//import ca.ualberta.cmput301.t03.configuration.Configuration;
//import ca.ualberta.cmput301.t03.user.InitializeUserActivity;
//import ca.ualberta.cmput301.t03.user.User;
//import ca.ualberta.cmput301.t03.user.UserProfile;
//
//public class UserCreationTest extends ActivityInstrumentationTestCase2{
//
//    public UserCreationTest() {
//        super(ca.ualberta.cmput301.t03.MainActivity.class);
//    }
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        PrimaryUserHelper.setup(this.getInstrumentation().getTargetContext());
//    }
//
//    @Override
//    public void tearDown() throws Exception {
//        PrimaryUserHelper.tearDown(this.getInstrumentation().getTargetContext());
//        super.tearDown();
//    }
//
//    /**
//    * UC02.04.01
//    */
////    public void testCreateDuplicateProfile() {
////        String username = "bobsmith";
////        User user = new User(username);
////        assertTrue(user.getName().equals(name));
////
////        //grrr, junit3 does not have assertRaises...
////        try {
////            User user2 = new User(username);
////            assertTrue("Exception should've been thrown", false);
////        } catch(DuplicateUsernameException e){
////            assertTrue("Duplicate username exception", true);
////        }
////
////    }
//
//    /**
//    * UC02.04.01
//    */
//    public void testCreateProfile() {
//        Configuration configuration = new Configuration(getInstrumentation().getTargetContext());
//        configuration.clearApplicaitonUserName();
//
//
//        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(InitializeUserActivity.class.getName(), null, false);
//        MainActivity activity = (MainActivity) getActivity();
//
//        InitializeUserActivity initActivity = (InitializeUserActivity) getInstrumentation().waitForMonitorWithTimeout(am, 1000);
//        assertNotNull(initActivity);
//        assertEquals(InitializeUserActivity.class, initActivity.getClass());
//        assertEquals(true, getInstrumentation().checkMonitorHit(am, 1));
//        getInstrumentation().removeMonitor(am);
//
//        EditText userNameEditText = initActivity.getUserNameEditText();
//        EditText emailEditText = initActivity.getEmailEditText();
//        EditText cityEditText = initActivity.getCityEditText();
//        EditText phoneNumberEditText = initActivity.getPhoneNumberEditText();
//        Button doneButton = initActivity.getDoneButton();
//
//
//
//
//        String username = "bobsmith";
//        User user = new User(username);
//        assertTrue(user.getName().equals(name));
//
//        String address = "123 sesame street";
//        user.setAddress(address);
//        assertTrue(user.getAddress().equals(address));
//
//        String email = "abc@example.com";
//        user.setEmail(email);
//        assertTrue(user.getEmail().equals(email));
//
//        String cellPhone = "780 123 4567";
//        user.setPhoneNumber(cellPhone);
//        assertTrue(user.getPhoneNumber().equals(cellPhone));
//    }
//}
