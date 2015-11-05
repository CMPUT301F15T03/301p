package ca.ualberta.cmput301.t03.user;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static ca.ualberta.cmput301.t03.commontesting.Matchers.withAdaptedData;

import static android.support.test.espresso.matcher.ViewMatchers.withTagKey;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.commontesting.PauseForAnimation.pause;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;

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
public class FriendsListTest {

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

    private final String TEST_OTHER_USER = "TEST_USER_DO_NOT_USE_2";
    private final String TEST_CURRENT_USER = "TEST_USER_DO_NOT_USE_1";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private MainActivity mActivity = null;

    @Before
    public void setActivity() {

        mActivity = mActivityRule.getActivity();
        InstrumentationRegistry.getInstrumentation();

        Context ctx = InstrumentationRegistry.getTargetContext();
        Configuration c = new Configuration(ctx);
        c.setApplicationUserName(TEST_OTHER_USER);
        pause();
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
        onView(withText("Friends")).check(matches(isDisplayed())).perform(click());
        pause();
    }


    /**
     * US02.01.01, UC02.02.01
     */
    @Test
    public void testAddFriend() throws Exception{

        onView(withId(R.id.addFriendFab)).perform(click());
        pause();
        onView(withClassName(new StringContains("EditText"))).
                perform(typeText(TEST_OTHER_USER),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();
        onData(hasToString(TEST_OTHER_USER))
                .inAdapterView(withId(R.id.friendsListListView))
                .check(matches(isDisplayed()));
        pause();
    }

    /**
     * UC02.03.01
     */
    @Test
    public void testAddAndRemoveFriend() throws InterruptedException {

        onView(withId(R.id.addFriendFab)).perform(click());
        pause();
        onView(withClassName(new StringContains("EditText")))
                .perform(typeText(TEST_OTHER_USER),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add"))
                .perform(click());
        pause();
        onData(hasToString(TEST_OTHER_USER))
                .inAdapterView(withId(R.id.friendsListListView))
                .check(matches(isDisplayed()));
        pause();
        onView(withText(TEST_OTHER_USER)).perform(longClick());
        pause();
        onView(withId(R.id.friendsListListView))
                .check(matches(not(withAdaptedData(hasToString(TEST_OTHER_USER)))));
        pause();

    }



    /**
     * UC02.05.01
     */
    @Test
    public void testViewFriendProfile() {
        onView(withId(R.id.addFriendFab)).perform(click());
        pause();
        onView(withClassName(new StringContains("EditText")))
                .perform(typeText(TEST_OTHER_USER),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add"))
                .perform(click());
        pause();
        onData(hasToString(TEST_OTHER_USER))
                .inAdapterView(withId(R.id.friendsListListView))
                .check(matches(isDisplayed()));
        pause();
        onView(withText(TEST_OTHER_USER)).perform(click());
        pause();
        onView(withId(R.id.viewProfileUsername))
                .check(matches(allOf(isDisplayed(),
                        withText(TEST_OTHER_USER))));
        pause();
    }

}
