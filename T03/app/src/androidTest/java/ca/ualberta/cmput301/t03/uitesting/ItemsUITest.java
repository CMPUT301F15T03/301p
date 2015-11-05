package ca.ualberta.cmput301.t03.uitesting;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Matcher;

import java.util.HashMap;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by mmabuyo on 2015-11-04.
 */
public class ItemsUITest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;

    public ItemsUITest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();

        PrimaryUserHelper.setup(this.getInstrumentation().getTargetContext());

        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(this.getInstrumentation().getTargetContext());
        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.tearDown();

    }

//    public void testAddAnItem() {
//        // UC1.01.01 AddAnItem
//        onView(withContentDescription("Open navigation drawer")).perform(click());
//        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
//        onView(withId(R.id.addItemInventoryFab))
//                .perform(click());
//
//        String ITEM_NAME = "Camera";
//        onView(withId(R.id.itemName))
//                .perform(typeText(ITEM_NAME), closeSoftKeyboard());
//
//        String ITEM_CATEGORY = "Cameras";
//
//        onView(withId(R.id.itemCategory)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
//        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));
//
//        onView(withId(R.id.addItem)).perform(click());
//    }

    public void testRemoveAnItem() {
        //UC1.03.01 - Remove an Item

        // add item first
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        String ITEM_NAME = "Lens";
        onView(withId(R.id.itemName))
                .perform(typeText(ITEM_NAME), closeSoftKeyboard());

        String ITEM_CATEGORY = "Lens mounts";

        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));

        onView(withId(R.id.addItem)).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(hasToString(ITEM_NAME))
                .inAdapterView(withId(R.id.InventoryListView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.deleteItem)).perform(click());
    }

    public void testEditAnItem() {
        //UC1.02.01 - Edit an Item

        // add item first
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        String ITEM_NAME = "Lens";
        onView(withId(R.id.itemName))
                .perform(typeText(ITEM_NAME), closeSoftKeyboard());

        String ITEM_CATEGORY = "Lens mounts";

        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));

        onView(withId(R.id.addItem)).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(hasToString(ITEM_NAME))
                .inAdapterView(withId(R.id.InventoryListView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.editItem)).perform(click());

        // change item name
        String NEW_ITEM_NAME = "50mm Lens";
        onView(withId(R.id.itemName))
                .perform(typeText(ITEM_NAME), closeSoftKeyboard());

        onView(withId(R.id.saveItem)).perform(click());
    }

    public void testViewOwnInventory() {
        //UC1.04.01 - View Own Inventory
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
    }

    public void testViewItem() {
        //UC1.05.01 - View Item

        // navigate to inventory
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // add item to make sure it exists
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        String ITEM_NAME = "Lens";
        onView(withId(R.id.itemName))
                .perform(typeText(ITEM_NAME), closeSoftKeyboard());

        String ITEM_CATEGORY = "Lens mounts";

        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));

        onView(withId(R.id.addItem)).perform(click());

        // should be back in inventory view so click on item tile that was just added
        // click to view details
        onData(hasToString(ITEM_NAME))
                .inAdapterView(withId(R.id.InventoryListView))
                .check(matches(isDisplayed()));
    }

    public void testChooseACategory() {
        //UC1.06.01 - Choose a Category
        // navigate to inventory
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        String ITEM_NAME = "Lens";
        onView(withId(R.id.itemName))
                .perform(typeText(ITEM_NAME), closeSoftKeyboard());

        String ITEM_CATEGORY = "Lens mounts";

        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));

        onView(withId(R.id.addItem)).perform(click());
    }

    public void testNavigateToItem() {
        //UC1.07.01 - Navigate to Entry Item
        // navigate to inventory
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // add item to make sure it exists
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        String ITEM_NAME = "Lens";
        onView(withId(R.id.itemName))
                .perform(typeText(ITEM_NAME), closeSoftKeyboard());

        String ITEM_CATEGORY = "Lens mounts";

        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));

        onView(withId(R.id.addItem)).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(hasToString(ITEM_NAME))
                .inAdapterView(withId(R.id.InventoryListView))
                .check(matches(isDisplayed()));
    }

    public void testChangePublicStatus() {
        //UC1.08.01 - Change Public Status

        // add item first
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        String ITEM_NAME = "Lens";
        onView(withId(R.id.itemName))
                .perform(typeText(ITEM_NAME), closeSoftKeyboard());

        String ITEM_CATEGORY = "Lens mounts";

        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));

        onView(withId(R.id.addItem)).perform(click());

        // should be back in inventory view so click on item tile that was just added
        // onData(allOf(is(instanceOf(Book.class)), withBookTitle(BOOK_TITLE))).perform(click());
        onData(withText(ITEM_NAME))
                .inAdapterView(withId(R.id.InventoryListView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.editItem)).perform(click());

        // change item check box
        onView(withId(R.id.itemPrivateCheckBox))
                .perform(click());

        onView(withId(R.id.saveItem)).perform(click());
    }

}