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

    /**
     * A JUnit {@link org.junit.Rule @Rule} to launch your activity under test. This is a replacement
     * for {@link android.test.ActivityInstrumentationTestCase2}.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link org.junit.Before @Before} method.
     * <p>
     * {@link android.support.test.rule.ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link android.support.test.rule.ActivityTestRule#getActivity()} method.
     */

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
    }


    /**
     * US02.01.01, UC02.02.01
     */
    @Test
    public void testAddFilterToBrowse() throws Exception{

        onView(withId(R.id.addFilterBrowseFab)).perform(click());
        fail();
    }


    @Test
    public void testTileViewBrowsableInventory() throws Exception{
        PrimaryUserHelper.createAndLoadUserWithFriendThatHasInventory(mActivity.getBaseContext());

        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
//        onView(withText("Friends")).check(matches(isDisplayed())).perform(click());
//        pause();
        onView(withText("Browse")).check(matches(isDisplayed())).perform(click());
        pause();

        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(mActivity.getBaseContext());

    }
    /**
     * UC02.03.01
     */
//    @Test
//    public void testRemoveItemFromInventory() throws InterruptedException {
//
//        onView(withId(R.id.addItemInventoryFab)).perform(click());
//        //TODO Ensure Can click delete Intent created and shown.
//
//    }

    /**
     * UC02.05.01
     */
//    @Test
//    public void testViewFriendProfile() {
//        onView(withId(R.id.addFriendFab)).perform(click());
//        onView(withClassName(new StringContains("EditText")))
//                .perform(typeText(TEST_USER_NAME),
//                        closeSoftKeyboard());
//        onView(withText("Add"))
//                .perform(click());
//        onData(hasToString(TEST_USER_NAME))
//                .inAdapterView(withId(R.id.friendsListListView))
//                .check(matches(isDisplayed()));
//
//        onView(withText(TEST_USER_NAME)).perform(click());
//        onView(withId(R.id.viewProfileUsername))
//                .check(matches(allOf(isDisplayed(),
//                        withText(TEST_USER_NAME))));
//    }

}
