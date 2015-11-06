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
import android.support.test.espresso.NoMatchingViewException;
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
import static junit.framework.Assert.fail;
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

import java.io.IOException;

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
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.UserProfile;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseInventoryTest {

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
        /**
         * Methods create and cleanup template user and friends.
         */
//        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(mActivity.getBaseContext());
//        PrimaryUserHelper.createAndLoadUserWithFriendThatHasInventory(mActivity.getBaseContext());

        Configuration configuration = new Configuration(InstrumentationRegistry.getInstrumentation().getTargetContext());
        configuration.setApplicationUserName("GENERALINVENTORYFRIEND1");
        User temp = new User(configuration.getApplicationUserName(), InstrumentationRegistry.getInstrumentation().getTargetContext());
        try{
            temp.getFriends();
            temp.getInventory();
            UserProfile prof = temp.getProfile();
            prof.commitChanges();

        } catch (IOException e){
            throw new RuntimeException();
        }

        /**
         * Go to browse fragment
         */
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
        onView(withText("Browse")).check(matches(isDisplayed())).perform(click());
        pause();

    }

    /**
     * Methods cleanup template user and friends.
     */
    @After
    public void cleanup(){
//        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(mActivity.getBaseContext());
    }

    /**
     * Testing the browse test.
     * @throws Exception
     */
    @Test
    public void testSimpleBrowseSearch() throws Exception{


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
     *UC3.1.1 BrowseAllFriendsGeneralSearch
     *UC3.2.1 SetPublicBrowseAndSearchInventory
     * Precondition User exists with friends that have inventories,
     * with both private and public items.
     * */
    @Test
    public void testBrowseAllFriendsGeneralSearch() throws Exception{

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
     *UC3.1.2 BrowseFriendGeneralSearch
     *Precondition User exists with friends that have inventories,
     * with both private and public items.
     * */
    @Test
    public void testBrowseFriendGeneralSearch() throws Exception{
        /**
         * Adding filter of a friend to view single friends inventory.
         */
        //TODO onView(withId(R.id.addFilterButton)).perform(click());
        onView(withClassName(new StringContains("AddFilterType"))).
                perform(typeText("friend"),
                        closeSoftKeyboard());
        pause();
        onView(withClassName(new StringContains("AddFilterText"))).
                perform(typeText("FRIENDWITHANINVENTORY"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();

        /**
         * Checks that there are two items from the filtered friend
         */
        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        try {
            onView(withText("testItem1f2")).check(matches(isDisplayed()));
            fail("View should not be displayed");
        } catch (NoMatchingViewException e) {
            //pass
        }
    }

    /**
     *UC3.1.2 BrowseFriendGeneralSearch
     *Precondition User exists with friends that have inventories,
     * with both private and public items.
     * */
    @Test
    public void testBrowseFriendFromFriends() throws Exception{
        /**
         * Navigate to FriendList and Find Friend
         */
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
        onView(withText("Friends")).check(matches(isDisplayed())).perform(click());
        pause();

        onData(hasToString("FRIENDWITHANINVENTORY"))
                .inAdapterView(withId(R.id.friendsListListView))
                .perform(click());
        pause();

        /**
         * Click show inventory button
         */
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        pause();

        /**
         * Checks that there are two items from the filtered friend
         */
        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        try {
            onView(withText("testItem3f1")).check(matches(isDisplayed()));
            fail("View should not be displayed");
        } catch (NoMatchingViewException e) {
            //pass
        }
    }

    /**
     *UC3.1.3 BrowseAllFriendsCategorySearch
     *Precondition User exists with friends that have inventories,
     * with items of two separate categories.
     * */
    @Test
    public void testBrowseAllFriendsCategorySearch() throws Exception{
        /**
         * Adding filter of a friend to view all friends inventories,
         * filtering by a given category.
         */
        //TODO onView(withId(R.id.addFilterButton)).perform(click());
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
     *UC3.1.4 BrowseFriendCategorySearch
     *Precondition User exists with friends that have inventories,
     * with items of two separate categories.
     * */
    @Test
    public void testBrowseFriendCategorySearch() throws Exception{
        /**
         * Adding filter of a friend and a category to view single friends
         * inventory of only items pertaining to a given category.
         */
        //TODO onView(withId(R.id.addFilterButton)).perform(click());
        onView(withClassName(new StringContains("AddFilterType"))).
                perform(typeText("friend"),
                        closeSoftKeyboard());
        pause();
        onView(withClassName(new StringContains("AddFilterText"))).
                perform(typeText("FRIENDWITHANINVENTORY"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();
        //TODO onView(withId(R.id.addFilterButton)).perform(click());
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
         * Checks that there is one item from the filtered friend and that is has category "stand".
         */
        onView(withText("testItem2f1")).check(matches(isDisplayed()));
        onView(withText("Stands")).check(matches(isDisplayed()));
        try {
            onView(withText("Cameras")).check(matches(isDisplayed()));
            fail("View should not be displayed");
        } catch (NoMatchingViewException e) {
            //pass
        }
    }

    /**
     *UC3.1.5 BrowseAllFriendsTextualQuerySearch
     *Precondition User exists with friends that have inventories,
     * with items with textual attributes that can be used to filter results.
     * */
    @Test
    public void testBrowseAllFriendsTextualQuerySearch() throws Exception{
        /**
         * Adding filter of textualQuery to view all friends inventories,
         * filtering by a given textualQuery.
         */
        //TODO onView(withId(R.id.addFilterButton)).perform(click());
        onView(withClassName(new StringContains("AddFilterType"))).
                perform(typeText("textual"),
                        closeSoftKeyboard());
        pause();
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
     *UC3.1.6 BrowseFriendTextualQuerySearch
     * Precondition User exists with friends that have inventories,
     * with items with textual attributes that can be used to filter results.
     * */
    @Test
    public void testBrowseFriendTextualQuerySearch() throws Exception{
        /**
         * Adding filter of a textualQuery and friend to view one friends inventory,
         * filtered by a given textualQuery.
         */
        //TODO onView(withId(R.id.addFilterButton)).perform(click());
        onView(withClassName(new StringContains("AddFilterType"))).
                perform(typeText("friend"),
                        closeSoftKeyboard());
        pause();
        onView(withClassName(new StringContains("AddFilterText"))).
                perform(typeText("FRIENDWITHANINVENTORY"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();
        //TODO onView(withId(R.id.addFilterButton)).perform(click());
        onView(withClassName(new StringContains("AddFilterType"))).
                perform(typeText("textual"),
                        closeSoftKeyboard());
        pause();
        onView(withClassName(new StringContains("AddFilterText"))).
                perform(typeText("FX10"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();

        /**
         * Checks that there is one items from the filtered friend.
         */
        onView(withText("testItem1f1")).check(matches(isDisplayed()));
    }


    /**
     *UC3.3.1 OfflineBrowsing
     *Precondition User exists with friends that have inventories,
     * with items with textual attributes that can be used to filter results.
     * User has enabled caching.
     * */
    @Test
    public void testOfflineBrowsing() throws Exception{
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
