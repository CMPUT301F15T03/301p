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
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.common.PauseForAnimation.pause;
import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserInventoryUITest {

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

        /**
         * Go to inventory fragment
         */
        onView(withContentDescription(R.string.navigation_drawer_open))
                .check(matches(isDisplayed()))
                .perform(click());
        pause();
        onView(withText("Inventory"))
                .check(matches(isDisplayed()))
                .perform(click());
        pause();
    }

    @After
    public void tearDown() throws Exception {
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(InstrumentationRegistry.getTargetContext());
    }

    /**
     * UC1.04.01 - View Own Inventory
     * Preconditions: The Current user already has an inventory containing added items.
     */
    @Test
    public void testViewOwnInventory() throws Exception {
        /**
         * Verify that three items are present as configured in the users template.
         */
        pause();
        pause();
        onView(withText("testItem1")).check(matches(isDisplayed()));
        onView(withText("testItem2")).check(matches(isDisplayed()));
        onView(withText("testItem3")).check(matches(isDisplayed()));

    }

    @Test
    public void testAddTextualQuerySearchOnInventory() throws Exception {
        /**
         * Adding filter of textualQuery to view all friends inventories,
         * filtering by a given textualQuery.
         */
        onView(withContentDescription("Search"))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.addSearchFilterText)).
                perform(typeText("item"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();

        /**
         * Checks that there are two items from the filtered friends and that have the
         * textual string "FX10"
         */
        onView(withText("testItem1")).check(matches(isDisplayed()));
        onView(withText("testItem2")).check(matches(isDisplayed()));
        onView(withText("testItem3")).check(matches(isDisplayed()));

        onView(withContentDescription("Search"))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.addSearchFilterText)).
                perform(typeText("3"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();

        onView(withText("testItem3")).check(matches(isDisplayed()));
    }
    @Test
    public void testAddCategorySearchOnInventory(){

    }
}
