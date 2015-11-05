package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.configuration.Configuration;

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
import static java.lang.Thread.sleep;

/**
 * Copyright 2015 John Slevinsky
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserProfileTest {

    public static final String TEST_EXAMPLE_COM = "test@example.com";
    public static final String STRING_TO_BE_TYPED = "780 123 4567";
    public static final String CALGARY = "Calgary";
    public static final String VANCOUVER = "Vancouver";
    public static final String STRING_TO_BE_TYPED1 = "7781234567";
    public static final String TEST2_EXAMPLE_COM = "test2@example.com";
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

    private final String TEST_USER_NAME = "TEST_USER_DO_NOT_USE_1";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private MainActivity mActivity = null;

    @Before
    public void setActivity() {

        mActivity = mActivityRule.getActivity();
        Context ctx = InstrumentationRegistry.getTargetContext();
        Configuration c = new Configuration(ctx);
        c.setApplicationUserName(TEST_USER_NAME);
        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        onView(withText("My Profile")).check(matches(isDisplayed())).perform(click());
    }

    @Test
    public void testViewUserProfile(){
        onView(withId(R.id.viewProfileUsername)).check(matches(withText(TEST_USER_NAME)));
    }


    /**
     * UC10.02.01
     * @throws InterruptedException
     */
    @Test
    public void testEditUserProfile() throws InterruptedException {
        onView(withId(R.id.edit_profile_button))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.profileEmailEditText))
                .perform(clearText(),
                        typeText(TEST_EXAMPLE_COM),
                        closeSoftKeyboard());
        onView(withId(R.id.profilePhoneEditText))
                .perform(clearText(),
                        typeText(STRING_TO_BE_TYPED),
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
                .check(matches(withText(STRING_TO_BE_TYPED)));

        onView(withId(R.id.edit_profile_button))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.profileEmailEditText))
                .perform(clearText(),
                        typeText(TEST2_EXAMPLE_COM),
                        closeSoftKeyboard());
        onView(withId(R.id.profilePhoneEditText))
                .perform(clearText(),
                        typeText(STRING_TO_BE_TYPED1),
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
                .check(matches(withText(STRING_TO_BE_TYPED1)));
    }
}
