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

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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
import ca.ualberta.cmput301.t03.common.PrimaryUserHelper;

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
import static ca.ualberta.cmput301.t03.common.PauseForAnimation.pause;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.hasToString;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseInventoryUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class, false, false);

    private MainActivity mActivity = null;


    @Before
    public void setActivity() {
        PrimaryUserHelper.createAndLoadUserWithFriendThatHasInventory(InstrumentationRegistry.getTargetContext());

        mActivity = mActivityRule.launchActivity(new Intent());
        InstrumentationRegistry.getInstrumentation();
        pause();

        /*
         * Go to browse fragment
         */
//        onView(withContentDescription(R.string.navigation_drawer_open))
//                .check(matches(isDisplayed()))
//                .perform(click());
//        pause();
//        onView(withText("Browse"))
//                .check(matches(isDisplayed()))
//                .perform(click());
//        pause();
    }

    @After
    public void tearDown() throws Exception {
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(InstrumentationRegistry.getTargetContext());
    }

    /**
     * Testing the browse test.
     *
     * @throws Exception
     */
    @Test
    public void testSimpleBrowseSearch() throws Exception {


        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        try {
            onView(withText("testItem2f4")).check(matches(isDisplayed()));
            fail("View should not be displayed");
        } catch (NoMatchingViewException e) {
            //pass
        }
//        onData(hasToString("testItem1f1"))
//                .inAdapterView(withId(R.id.BrowseListView)).atPosition(0).onChildView(withId(R.id.tileViewItemName))
//                .check(matches(isDisplayed()));

    }

    /**
     * UC3.1.1 BrowseAllFriendsGeneralSearch
     * UC3.2.1 SetPublicBrowseAndSearchInventory
     * Precondition User exists with friends that have inventories,
     * with both private and public items.
     */
    @Test
    public void testBrowseAllFriendsGeneralSearch() throws Exception {

        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        onView(withText("testItem1f2")).check(matches(isDisplayed()));
        onView(withText("testItem2f2")).check(matches(isDisplayed()));
        try {
            onView(withText("testItem3f1")).check(matches(isDisplayed()));
            fail("View should not be displayed");
        } catch (NoMatchingViewException e) {
            //pass
        }
        try {
            onView(withText("testItem3f2")).check(matches(isDisplayed()));
            fail("View should not be displayed");
        } catch (NoMatchingViewException e) {
            //pass
        }
    }

    /**
     * UC3.1.3 BrowseAllFriendsCategorySearch
     * Precondition User exists with friends that have inventories,
     * with items of two separate categories.
     */
    @Test
    public void testBrowseAllFriendsCategorySearch() throws Exception {
        /**
         * Adding filter of a friend to view all friends inventories,
         * filtering by a given category.
         */
        onView(withContentDescription(R.id.filter_inventory_button))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withClassName(new StringContains("AddFilterType"))).
                perform(typeText("category"),
                        closeSoftKeyboard());
        pause();
        onView(withClassName(new StringContains("AddFilterText"))).
                perform(typeText("Stands"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();

        /**
         * Checks that there are two items from the filtered friends and that they
         * are of category "stands".
         */

        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f2")).check(matches(isDisplayed()));
        onView(withText("Stands")).check(matches(isDisplayed()));
        try {
            onView(withText("Cameras")).check(matches(isDisplayed()));
            fail("View should not be displayed");
        } catch (NoMatchingViewException e) {
            //pass
        }
    }



    /**
     * UC3.1.5 BrowseAllFriendsTextualQuerySearch
     * Precondition User exists with friends that have inventories,
     * with items with textual attributes that can be used to filter results.
     */
    @Test
    public void testBrowseAllFriendsTextualQuerySearch() throws Exception {
        /**
         * Adding filter of textualQuery to view all friends inventories,
         * filtering by a given textualQuery.
         */
        onView(withContentDescription(R.id.search_inventory_button))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withClassName(new StringContains("AddFilterType"))).
                perform(typeText("category"),
                        closeSoftKeyboard());
        onView(withClassName(new StringContains("AddFilterText"))).
                perform(typeText("FX10"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();

        /**
         * Checks that there are two items from the filtered friends and that have the
         * textual string "FX10"
         */
        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem1f2")).check(matches(isDisplayed()));

    }




    /**
     * UC3.3.1 OfflineBrowsing
     * Precondition User exists with friends that have inventories,
     * with items with textual attributes that can be used to filter results.
     * User has enabled caching.
     */
    @Test
    public void testOfflineBrowsing() throws Exception {
        /**
         * Browse Inventories while online
         */
        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        onView(withText("testItem1f2")).check(matches(isDisplayed()));
        onView(withText("testItem2f2")).check(matches(isDisplayed()));

        /**
         * Leave the browse Fragment
         */
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        pause();

        /**
         * Go Offline
         */
        //TODO NetworkManager.setDeviceOffline();
        fail();
        //TODO assertTrue(NetworkManager.deviceIsOffline());

        /**
         * Return to Browse
         */
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
        onView(withText("Browse")).check(matches(isDisplayed())).perform(click());
        pause();

        /**
         * Verify Can still see inventory items while offline.
         */
        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        onView(withText("testItem1f2")).check(matches(isDisplayed()));
        onView(withText("testItem2f2")).check(matches(isDisplayed()));
    }


}
