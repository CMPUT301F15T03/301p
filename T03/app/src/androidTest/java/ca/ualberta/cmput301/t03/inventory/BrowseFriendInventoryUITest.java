package ca.ualberta.cmput301.t03.inventory;

import android.support.test.espresso.NoMatchingViewException;

import org.hamcrest.core.StringContains;
import org.junit.Test;

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
import static ca.ualberta.cmput301.t03.common.PauseForAnimation.pause;
import static junit.framework.Assert.fail;
import static org.hamcrest.Matchers.hasToString;

/**
 * Created by quentinlautischer on 2015-11-29.
 */
public class BrowseFriendInventoryUITest {


    /**
     * UC3.1.2 BrowseFriendGeneralSearch
     * Precondition User exists with friends that have inventories,
     * with both private and public items.
     */
    @Test
    public void testBrowseFriendFromFriends() throws Exception {
        /**
         * Navigate to FriendList and Find Friend
         */
        onView(withContentDescription("Open navigation drawer")).check(matches(isDisplayed())).perform(click());
        pause();
        onView(withText("Friends")).check(matches(isDisplayed())).perform(click());
        pause();

        onData(hasToString("friendwithaninventory"))
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

