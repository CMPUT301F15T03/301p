package ca.ualberta.cmput301.t03;

import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.user.FriendsList;
import ca.ualberta.cmput301.t03.user.InitializeUserActivity;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.UserProfile;

public class UserCreationTest extends ActivityInstrumentationTestCase2{

    public UserCreationTest() {
        super(ca.ualberta.cmput301.t03.MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        PrimaryUserHelper.setup(this.getInstrumentation().getTargetContext());
    }

    @Override
    public void tearDown() throws Exception {

        Context context = getInstrumentation().getTargetContext();
        DataManager dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        Configuration configuration = new Configuration(context);
        configuration.setApplicationUserName("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME2");
        try {
            dataManager.deleteIfExists(new DataKey(UserProfile.type, configuration.getApplicationUserName()));
            dataManager.deleteIfExists(new DataKey(Inventory.type, configuration.getApplicationUserName()));
            dataManager.deleteIfExists(new DataKey(FriendsList.type, configuration.getApplicationUserName()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrimaryUserHelper.tearDown(this.getInstrumentation().getTargetContext());

        super.tearDown();
    }

    /**
    * UC02.04.01
     * testCreateDuplicateProfile() now covered in here
    */
    public void testCreateProfile() {

        Configuration configuration = new Configuration(getInstrumentation().getTargetContext());

        Context context = getInstrumentation().getTargetContext();
        DataManager dataManager = new CachedDataManager(new HttpDataManager(context, true), context, true);
        configuration.setApplicationUserName("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME2");
        try {
            dataManager.deleteIfExists(new DataKey(UserProfile.type, configuration.getApplicationUserName()));
            dataManager.deleteIfExists(new DataKey(Inventory.type, configuration.getApplicationUserName()));
            dataManager.deleteIfExists(new DataKey(FriendsList.type, configuration.getApplicationUserName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.clearApplicationUserName();


        Instrumentation.ActivityMonitor am = getInstrumentation().addMonitor(InitializeUserActivity.class.getName(), null, false);
        MainActivity activity = (MainActivity) getActivity();

        InitializeUserActivity initActivity = (InitializeUserActivity) getInstrumentation().waitForMonitorWithTimeout(am, 1000);
        assertNotNull(initActivity);
        assertEquals(InitializeUserActivity.class, initActivity.getClass());
        assertEquals(true, getInstrumentation().checkMonitorHit(am, 1));
        getInstrumentation().removeMonitor(am);

        final EditText userNameEditText = initActivity.getUserNameEditText();
        final EditText emailEditText = initActivity.getEmailEditText();
        final EditText cityEditText = initActivity.getCityEditText();
        EditText phoneNumberEditText = initActivity.getPhoneNumberEditText();
        final Button doneButton = initActivity.getDoneButton();

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userNameEditText.setText("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME");
                doneButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        android.os.SystemClock.sleep(2500);
        String expectedToast = getInstrumentation().getTargetContext().getString(R.string.userNameTakenToast);
        assertEquals(initActivity.getToast(), expectedToast);


        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                userNameEditText.setText("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME2");
                doneButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        android.os.SystemClock.sleep(2500);
        expectedToast = getInstrumentation().getTargetContext().getString(R.string.noCityToast);
        assertEquals(initActivity.getToast(), expectedToast);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cityEditText.setText("Edmonton");
                doneButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        android.os.SystemClock.sleep(250);
        expectedToast = getInstrumentation().getTargetContext().getString(R.string.invalidEmailToast);

        assertEquals(initActivity.getToast(), expectedToast);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                emailEditText.setText("a@b.ca");
                doneButton.performClick();
            }
        });

        getInstrumentation().waitForIdleSync();
        android.os.SystemClock.sleep(250);

        PrimaryUser.setup(getInstrumentation().getTargetContext());
        User pUser = PrimaryUser.getInstance();

        assertEquals(pUser.getUsername(), "JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME2");
        try {
            assertEquals(pUser.getProfile().getCity(), "Edmonton");
            assertEquals(pUser.getProfile().getEmail(), "a@b.ca");

        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.setApplicationUserName("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME");

        activity.finish();
    }
}
