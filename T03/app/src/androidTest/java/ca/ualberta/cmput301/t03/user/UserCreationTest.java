/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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

import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.PrimaryUserHelper;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.CachedDataManager;
import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.DataManager;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;

public class UserCreationTest extends ActivityInstrumentationTestCase2 {

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
        DataManager dataManager = new CachedDataManager(context, new HttpDataManager(context, context.getString(R.string.elasticSearchRootUrl), true));
        Configuration configuration = new Configuration(context);
        configuration.setApplicationUserName("junittestuserxyz");
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
    public void testCreateProfile() throws ServiceNotAvailableException, IOException {

        Configuration configuration = new Configuration(getInstrumentation().getTargetContext());

        Context context = getInstrumentation().getTargetContext();
        DataManager dataManager = new CachedDataManager(context, new HttpDataManager(context, context.getString(R.string.elasticSearchRootUrl), true));
        configuration.setApplicationUserName("junittestuserxyz");
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
                userNameEditText.setText(PrimaryUserHelper.USER_ID);
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
                userNameEditText.setText("junittestuserxyz");
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

        assertEquals(pUser.getUsername(), "junittestuserxyz");
        String city  = pUser.getProfile().getCity();
        String email = pUser.getProfile().getEmail();
        android.os.SystemClock.sleep(500);
            assertEquals(city, "Edmonton");
            assertEquals(email, "a@b.ca");
        configuration.setApplicationUserName("JUNIT_TEST_USER_DO_NOT_USE_THIS_NAME");

        activity.finish();
    }
}
