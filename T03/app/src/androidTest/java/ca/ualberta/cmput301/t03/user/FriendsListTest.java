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

import org.hamcrest.core.StringContains;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.commontesting.Matchers.withAdaptedData;
import static ca.ualberta.cmput301.t03.commontesting.PauseForAnimation.pause;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;

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

    private final String TEST_OTHER_USER = PrimaryUserHelper.FRIEND_WITH_AN_INVENTORY2;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class, false, false);

    private MainActivity mActivity = null;


    @Before
    public void setActivity() throws Exception {

        PrimaryUserHelper.createUsers_NoFriendsAdded(InstrumentationRegistry.getTargetContext());

        mActivity = mActivityRule.launchActivity(new Intent());
        pause();
        onView(withContentDescription(R.string.navigation_drawer_open))
                .check(matches(isDisplayed()))
                .perform(click());
        pause();
        onView(withText("Friends"))
                .check(matches(isDisplayed()))
                .perform(click());
        pause();
    }

    @After
    public void tearDown() throws Exception {
        PrimaryUserHelper.deleteUserWithFriend(InstrumentationRegistry.getTargetContext());
    }

    /**
     * US02.01.01, UC02.02.01
     */
    @Test
    public void testAddFriend() throws Exception {

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
        onData(hasToString(TEST_OTHER_USER))
                .inAdapterView(withId(R.id.friendsListListView))
                .perform(longClick());
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
        onData(hasToString(TEST_OTHER_USER))
                .inAdapterView(withId(R.id.friendsListListView))
                .perform(click());
        pause();


        onView(withId(R.id.viewProfileUsername))
                .check(matches(allOf(isDisplayed(),
                        withText(TEST_OTHER_USER))));
        pause();
    }


    /**
     * US02.01.01, UC02.02.01
     */
    @Test
    public void testAddFriend_pressCancel() {
        onView(withId(R.id.addFriendFab)).perform(click());
        pause();
        onView(withClassName(new StringContains("EditText")))
                .perform(typeText(PrimaryUserHelper.FRIEND_WITH_AN_INVENTORY),
                        closeSoftKeyboard());
        pause();
        onView(withText("Cancel"))
                .perform(click());

        pause();
        //check friend was not added.
        onView(withId(R.id.friendsListListView))
                .check(matches(not(withAdaptedData(hasToString(PrimaryUserHelper.FRIEND_WITH_AN_INVENTORY)))));
        pause();

    }

    /**
     * US02.01.01, UC02.02.01
     */
    @Test
    public void testAddFriend_emptyInput() {
        onView(withId(R.id.addFriendFab)).perform(click());
        pause();
        onView(withClassName(new StringContains("EditText")))
                .perform(clearText(),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add"))
                .perform(click());

        //assert list does not contain friend
        onView(withId(R.id.friendsListListView))
                .check(matches(not(withAdaptedData(hasToString(PrimaryUserHelper.FRIEND_WITH_AN_INVENTORY)))));
        pause();


    }
}
