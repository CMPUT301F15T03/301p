package ca.ualberta.cmput301.t03.inventory;

import android.content.Intent;
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
import ca.ualberta.cmput301.t03.common.PrimaryUserHelper;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.hasValue;

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
public class TestItemCloning {

    private final String TEST_OTHER_USER = PrimaryUserHelper.FRIEND_WITH_AN_INVENTORY2;
    private final String TEST_ITEM_NAME = PrimaryUserHelper.TEST_ITEM_1_F_2;

    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class, false, false);

    private MainActivity mActivity = null;


    public void navigateTo(String tab){
        onView(withContentDescription(R.string.navigation_drawer_open))
                .check(matches(isDisplayed()))
                .perform(click());
        pause();
        onView(withText(tab))
                .check(matches(isDisplayed()))
                .perform(click());
        pause();
    }

    @Before
    public void setActivity() throws Exception {

        PrimaryUserHelper.createUsers_NoFriendsAdded(InstrumentationRegistry.getTargetContext());

        mActivity = mActivityRule.launchActivity(new Intent());
        pause();

        navigateTo("Friends");

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

        navigateTo("Browse");
    }

    @After
    public void tearDown() throws Exception {
        PrimaryUserHelper.deleteUserWithFriend(InstrumentationRegistry.getTargetContext());
    }

    @Test
    public void testDoNothing() {
        onData(hasEntry(anything(), hasToString(TEST_ITEM_NAME)))
                .inAdapterView(withId(R.id.BrowseListView))
                .perform(click());
        onView(withId(R.id.cloneItemButton))
                .check(
                        matches(allOf(isDisplayed(), hasToString(containsString("clone")))))
                .perform(click());
                pause(1000);
        pressBack();
        navigateTo("Inventory");
        onData(hasEntry(anything(), hasToString(TEST_ITEM_NAME)))
                .inAdapterView(withId(R.id.InventoryListView))
                .check(matches(isDisplayed()));

        pause(1000);
    }


}
