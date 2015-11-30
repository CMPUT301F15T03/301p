package ca.ualberta.cmput301.t03.inventory;

import android.content.Intent;
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
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.hasToString;

/**
 * Created by quentinlautischer on 2015-11-29.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseFriendInventoryUITest {

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
         * Go to inventory friends inventory
         */
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
        onView(withText("Friends")).check(matches(isDisplayed())).perform(click());
        pause();
        pause();

        onData(hasToString("friendwithaninventory"))
                .inAdapterView(withId(R.id.friendsListListView))
                .perform(click());
        pause();
        pause();

        /**
         * Click show inventory button
         */
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        pause();
    }

    @After
    public void tearDown() throws Exception {
        PrimaryUserHelper.deleteAndUnloadUserWithFriendThatHasInventory(InstrumentationRegistry.getTargetContext());
    }

    /**
     * UC3.1.2 BrowseFriendGeneralSearch
     * Precondition User exists with friends that have inventories,
     * with both private and public items.
     */
    @Test
    public void testBrowseFriendGeneral() throws Exception {
        /**
         * Navigate to FriendList and Find Friend
         */


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
     * UC3.1.6 BrowseFriendTextualQuerySearch
     * Precondition User exists with friends that have inventories,
     * with items with textual attributes that can be used to filter results.
     */
    @Test
    public void testBrowseFriendTextualQuerySearch() throws Exception {
        /**
         * Adding filter of a textualQuery and friend to view one friends inventory,
         * filtered by a given textualQuery.
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
        pause();
        pause();

        /**
         * Checks that there are two items from the filtered friends and that have the
         * textual string "FX10"
         */
        onView(withText("testItem1f1")).check(matches(isDisplayed()));
        onView(withText("testItem2f1")).check(matches(isDisplayed()));

        onView(withContentDescription("Search"))
                .check(matches(isDisplayed()))
                .perform(click());

        onView(withId(R.id.addSearchFilterText)).
                perform(typeText("2"),
                        closeSoftKeyboard());
        pause();
        onView(withText("Add")).
                perform(click());
        pause();

        onView(withText("testItem2f1")).check(matches(isDisplayed()));
    }

    /**
     * UC3.1.4 BrowseFriendCategorySearch
     * Precondition User exists with friends that have inventories,
     * with items of two separate categories.
     */
    @Test
    public void testBrowseFriendCategorySearch() throws Exception {
            /**
             * Adding filter of a friend and a category to view single friends
             * inventory of only items pertaining to a given category.
             */

            /**
             * Checks that there is one item from the filtered friend and that is has category "stand".
             */
    }

}

