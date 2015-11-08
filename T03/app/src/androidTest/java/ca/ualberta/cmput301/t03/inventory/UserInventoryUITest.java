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

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.common.PrimaryUserHelper;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserInventoryUITest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    private MainActivity mActivity = null;

    /**
     * Method cleanup template user and friends.
     */

    @BeforeClass
    public static void setupTestUser() throws Exception {
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(InstrumentationRegistry.getTargetContext());
        PrimaryUserHelper.createAndLoadUserWithFriendThatHasInventory(InstrumentationRegistry.getTargetContext());
    }

    @AfterClass
    public static void restoreOriginalUser() throws Exception {
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(InstrumentationRegistry.getTargetContext());
    }

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
         * Go to inventory fragment
         */
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
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

        onView(withText("testItem1")).check(matches(isDisplayed()));
        onView(withText("testItem2")).check(matches(isDisplayed()));
        onView(withText("testItem3")).check(matches(isDisplayed()));

    }
}
