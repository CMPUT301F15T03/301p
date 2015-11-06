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

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.StringContains;
import org.junit.After;
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
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.commontesting.Matchers.withAdaptedData;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static ca.ualberta.cmput301.t03.commontesting.PauseForAnimation.pause;




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
public class UserInventoryTest {

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
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(mActivity.getBaseContext());
        PrimaryUserHelper.createAndLoadUserWithFriendThatHasInventory(mActivity.getBaseContext());

        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
    }

    @After
    public void cleanup(){
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(mActivity.getBaseContext());
    }

    /**
     *UC1.04.01 - View Own Inventory
     *
     * */
    @Test
    public void testViewOwnInventory() throws Exception{
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        // UI: fill in all fields
        onView(withId(R.id.itemName)).perform(typeText("testItem1"), closeSoftKeyboard());
        onView(withId(R.id.itemQuality)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.itemDescription)).perform(typeText(""), closeSoftKeyboard());

        // add item through UI
        onView(withId(R.id.addItem)).perform(click());
        pause();

        onData(hasToString("testItem1"))
                .inAdapterView(withId(R.id.InventoryListView))
                .atPosition(0)
                .check(matches(isDisplayed()));
//        onData(hasToString("testItem2"))
//                .inAdapterView(withId(R.id.InventoryListView))
//                .atPosition(1)
//                .check(matches(isDisplayed()));
//        onData(hasToString("testItem3"))
//                .inAdapterView(withId(R.id.InventoryListView))
//                .atPosition(0)
//                .check(matches(isDisplayed()));
    }
}
