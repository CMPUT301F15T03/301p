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

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.commontesting.PauseForAnimation.pause;
import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserProfileTest {

    public static final String TEST_EXAMPLE_COM = "test@example.com";
    public static final String PHONE_1 = "780 123 4567";
    public static final String CALGARY = "Calgary";
    public static final String VANCOUVER = "Vancouver";
    public static final String PHONE_2 = "7781234567";
    public static final String TEST2_EXAMPLE_COM = "test2@example.com";
    public static final String INVALID_EMAIL = "asdfadfadfsdd";
    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test. This is a replacement
     * for {@link ActivityInstrumentationTestCase2}.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class, false, false);

    private MainActivity mActivity = null;


    @Before
    public void setActivity() throws Exception {
        PrimaryUserHelper.setup(InstrumentationRegistry.getTargetContext());
        pause();
        mActivity = mActivityRule.launchActivity(new Intent());

        pause();

        onView(withContentDescription(R.string.navigation_drawer_open))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("My Profile"))
                .perform(click());
    }

    @After
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(InstrumentationRegistry.getTargetContext());
    }

    /**
     * UC02.04.01
     */
    @Test
    public void testViewUserProfile() throws IOException {

        User user = PrimaryUser.getInstance();
        UserProfile profile = PrimaryUser.getInstance().getProfile();

        assertEquals(PrimaryUserHelper.USER_ID, user.getUsername());
        assertEquals(PrimaryUserHelper.CITY, profile.getCity());
        assertEquals(PrimaryUserHelper.EMAIL, profile.getEmail());
        assertEquals(PrimaryUserHelper.PHONE, profile.getPhone());

        onView(withId(R.id.viewProfileUsername)).check(matches(withText(PrimaryUserHelper.USER_ID)));
        onView(withId(R.id.viewProfileCity)).check(matches(withText(PrimaryUserHelper.CITY)));
        onView(withId(R.id.viewProfilePhone)).check(matches(withText(PrimaryUserHelper.PHONE)));
        onView(withId(R.id.viewProfileEmail)).check(matches(withText(PrimaryUserHelper.EMAIL)));
    }


    /**
     * UC10.02.01
     *
     * @throws InterruptedException
     */
    @Test
    public void testEditUserProfile_validInputs() throws InterruptedException {
        onView(withId(R.id.edit_profile_button))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.profileEmailEditText))
                .perform(clearText(),
                        typeText(TEST_EXAMPLE_COM),
                        closeSoftKeyboard());
        onView(withId(R.id.profilePhoneEditText))
                .perform(clearText(),
                        typeText(PHONE_1),
                        closeSoftKeyboard());
        onView(withId(R.id.profileCityEditText)).
                perform(clearText(),
                        typeText(CALGARY),
                        closeSoftKeyboard());
        pressBack();

        onView(withId(R.id.viewProfileEmail))
                .check(matches(withText(TEST_EXAMPLE_COM)));
        onView(withId(R.id.viewProfileCity))
                .check(matches(withText(CALGARY)));
        onView(withId(R.id.viewProfilePhone))
                .check(matches(withText(PHONE_1)));

        onView(withId(R.id.edit_profile_button))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.profileEmailEditText))
                .perform(clearText(),
                        typeText(TEST2_EXAMPLE_COM),
                        closeSoftKeyboard());
        onView(withId(R.id.profilePhoneEditText))
                .perform(clearText(),
                        typeText(PHONE_2),
                        closeSoftKeyboard());
        onView(withId(R.id.profileCityEditText)).
                perform(clearText(),
                        typeText(VANCOUVER),
                        closeSoftKeyboard());

        pressBack();

        onView(withId(R.id.viewProfileEmail))
                .check(matches(withText(TEST2_EXAMPLE_COM)));
        onView(withId(R.id.viewProfileCity))
                .check(matches(withText(VANCOUVER)));
        onView(withId(R.id.viewProfilePhone))
                .check(matches(withText(PHONE_2)));
    }

    @Test
    public void testEditUserProfile_invalidEmail() {

        TextView t = (TextView) mActivity.findViewById(R.id.viewProfileEmail);
        String email_before = t.getText().toString();

        onView(withId(R.id.edit_profile_button))
                .check(matches(isDisplayed()))
                .perform(click());
        pause();
        onView(withId(R.id.profileEmailEditText))
                .perform(clearText(),
                        typeText(INVALID_EMAIL),
                        closeSoftKeyboard());
        pause();
        onView(withId(R.id.profilePhoneEditText))
                .perform(clearText(),
                        typeText(PHONE_1),
                        closeSoftKeyboard());
        pause();
        onView(withId(R.id.profileCityEditText)).
                perform(clearText(),
                        typeText(CALGARY),
                        closeSoftKeyboard());
        pause();
        pressBack();

        pause();
        onView(withId(R.id.viewProfileEmail))
                .check(matches(withText(email_before)));
        pause();
        onView(withId(R.id.viewProfileCity))
                .check(matches(withText(CALGARY)));
        pause();
        onView(withId(R.id.viewProfilePhone))
                .check(matches(withText(PHONE_1)));
        pause();

    }
}
