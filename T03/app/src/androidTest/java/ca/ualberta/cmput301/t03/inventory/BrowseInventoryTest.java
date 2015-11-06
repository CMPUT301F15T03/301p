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

package ca.ualberta.cmput301.t03.inventory;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.hamcrest.core.StringContains;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.commontesting.PauseForAnimation.pause;
import static java.lang.Thread.sleep;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;

        import android.support.test.InstrumentationRegistry;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.test.suitebuilder.annotation.LargeTest;

        import org.hamcrest.core.StringContains;
        import org.junit.Before;
        import org.junit.Rule;
        import org.junit.Test;
        import org.junit.runner.RunWith;

        import ca.ualberta.cmput301.t03.MainActivity;
        import ca.ualberta.cmput301.t03.R;

        import static android.support.test.espresso.Espresso.onData;
        import static android.support.test.espresso.Espresso.onView;
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
        import static java.lang.Thread.sleep;
        import static org.hamcrest.CoreMatchers.not;
        import static org.hamcrest.Matchers.allOf;
        import static org.hamcrest.Matchers.hasToString;



        import android.support.test.InstrumentationRegistry;
        import android.support.test.rule.ActivityTestRule;
        import android.support.test.runner.AndroidJUnit4;
        import android.test.ActivityInstrumentationTestCase2;
        import android.test.suitebuilder.annotation.LargeTest;
        import android.view.View;
        import android.widget.Adapter;
        import android.widget.AdapterView;

        import org.hamcrest.Description;
        import org.hamcrest.Matcher;
        import org.hamcrest.TypeSafeMatcher;
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
        import static java.lang.Thread.sleep;
        import static org.hamcrest.CoreMatchers.not;
        import static org.hamcrest.Matchers.allOf;
        import static org.hamcrest.Matchers.anything;
        import static org.hamcrest.Matchers.contains;
        import static org.hamcrest.Matchers.hasToString;
        import static org.hamcrest.Matchers.is;

        import ca.ualberta.cmput301.t03.MainActivity;
        import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseInventoryTest {

    private final String TEST_USER_NAME = "quentin";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private MainActivity mActivity = null;

    @Before
    public void setActivity() {

        mActivity = mActivityRule.getActivity();
        InstrumentationRegistry.getInstrumentation();
        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PrimaryUserHelper.createAndLoadUserWithFriendThatHasInventory(mActivity.getBaseContext());
    }

    @After
    public void cleanup(){
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(mActivity.getBaseContext());
    }

    /**
     *UC3.1.1 BrowseAllFriendsGeneralSearch
     *
     * */
    @Test
    public void testBrowseAllFriendsGeneralSearch() throws Exception{
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
//        pause();
        onView(withText("Friends")).check(matches(isDisplayed())).perform(click());
//        pause();
        onView(withId(R.id.addFriendFab)).perform(click());
//        pause();
        onView(withClassName(new StringContains("EditText"))).
                perform(typeText("FRIEND_WITH_AN_INVENTORY"),
                        closeSoftKeyboard());
//        pause();
        onView(withText("Add")).
                perform(click());
//        pause();
        onData(hasToString("FRIEND_WITH_AN_INVENTORY"))
                .inAdapterView(withId(R.id.friendsListListView))
                .check(matches(isDisplayed()));
//        pause();
        onView(withId(R.id.addFriendFab)).perform(click());
//        pause();
        onView(withClassName(new StringContains("EditText"))).
                perform(typeText("FRIEND_WITH_AN_INVENTORY2"),
                        closeSoftKeyboard());
//        pause();
        onView(withText("Add")).
                perform(click());
//        pause();
        onData(hasToString("FRIEND_WITH_AN_INVENTORY2"))
                .inAdapterView(withId(R.id.friendsListListView))
                .check(matches(isDisplayed()));
//        pause();
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
//        pause();
        onView(withText("Browse")).check(matches(isDisplayed())).perform(click());
        pause();
        onData(hasToString("tileViewItemName=testItem1f1"))
                .inAdapterView(withId(R.id.BrowseListView))
                .check(matches(isDisplayed()));
    }

    /**
     *UC3.1.2 BrowseFriendGeneralSearch
     *
     * */
//    @Test
//    public void testBrowseFriendGeneralSearch() throws Exception{
//
//    }

    /**
     *UC3.1.3 BrowseAllFriendsCategorySearch
     *
     * */
//    @Test
//    public void testBrowseAllFriendsCategorySearch() throws Exception{
//
//    }

    /**
     *UC3.1.4 BrowseFriendCategorySearch
     *
     * */
//    @Test
//    public void testBrowseFriendCategorySearch() throws Exception{
//
//    }

    /**
     *UC3.1.5 BrowseAllFriendsTextualQuerySearch
     *
     * */
//    @Test
//    public void testBrowseAllFriendsTextualQuerySearch() throws Exception{
//
//    }

    /**
     *UC3.1.6 BrowseFriendTextualQuerySearch
     *
     * */
//    @Test
//    public void testBrowseFriendTextualQuerySearch() throws Exception{
//
//    }

    /**
     *UC3.2.1 SetPublicBrowseAndSearchInventory
     *
     * */
//    @Test
//    public void testSetPublicBrowseAndSearchInventory() throws Exception{
//
//    }

    /**
     *UC3.3.1 OfflineBrowsing
     *
     * */
//    @Test
//    public void testOfflineBrowsing() throws Exception{
//
//    }


}
