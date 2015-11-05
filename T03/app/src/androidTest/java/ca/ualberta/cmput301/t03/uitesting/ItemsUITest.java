package ca.ualberta.cmput301.t03.uitesting;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Ignore;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
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
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Created by mmabuyo on 2015-11-04.
 */
public class ItemsUITest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    private HttpDataManager dataManager;

    // TEST ITEM FIELDS
    String ITEM_NAME = "Camera";
    int ITEM_QUANTITY = 1;
    String ITEM_QUALITY = "A+";
    String ITEM_CATEGORY = "Cameras";
    boolean ITEM_PRIVATE = false;
    String ITEM_DESCRIPTION = "Pretty great camera";

    public ItemsUITest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();

        PrimaryUserHelper.setup(this.getInstrumentation().getTargetContext());

    }

    @Override
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(this.getInstrumentation().getTargetContext());

        super.tearDown();

    }

    public void testAddAnItem() {
        // UC1.01.01 AddAnItem
        final User user = PrimaryUser.getInstance();

        // initialize test item
        final Item item = new Item();
        item.setItemName(ITEM_NAME);
        item.setItemQuantity(ITEM_QUANTITY);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemIsPrivate(ITEM_PRIVATE);
        item.setItemDescription(ITEM_DESCRIPTION);

        // precondition: user does not have the item!
        final int[] inventory_size = new int[1];
        Thread checkInventoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    assertFalse(user.getInventory().getItems().containsKey(item.getUuid()));
                    inventory_size[0] = user.getInventory().getItems().size();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkInventoryThread.start();

        // TODO precondition: elastic search does not have a record of the item
        //dataManager.keyExists(item.getUuid());

        // UI: navigate to inventory and click add an item
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.addItemInventoryFab))
                .perform(click());

        // UI: fill in all fields
        onView(withId(R.id.itemName)).perform(typeText(ITEM_NAME), closeSoftKeyboard());
        onView(withId(R.id.itemQuality)).perform(typeText(ITEM_QUALITY), closeSoftKeyboard());
        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(ITEM_CATEGORY))).perform(click());
        onView(withId(R.id.itemCategory)).check(matches(withSpinnerText(containsString(ITEM_CATEGORY))));
        //Source: https://github.com/jordanterry/Espresso-Examples/blob/master/app/src/androidTest/java/test/nice/testproject/MainActivityTests.java
        // Accessed November 5, 2015
        if (ITEM_PRIVATE) { onView(withId(R.id.itemPrivateCheckBox)).perform(click()); }
        onView(withId(R.id.itemDescription)).perform(typeText(ITEM_DESCRIPTION), closeSoftKeyboard());

        // add item through UI
        onView(withId(R.id.addItem)).perform(click());

        // check that it actually got added to inventory
        Thread checkAddedItemThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // inventory size should have increased by one!
                    assertEquals(inventory_size[0] + 1, user.getInventory().getItems().size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkAddedItemThread.start();
    }

    public void testEditAnItem() {
        //UC1.02.01 - Edit an Item
        final User user = PrimaryUser.getInstance();

        // initialize test item
        final Item item = new Item();
        item.setItemName(ITEM_NAME);
        item.setItemQuantity(ITEM_QUANTITY);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemIsPrivate(ITEM_PRIVATE);
        item.setItemDescription(ITEM_DESCRIPTION);

        // precondition: user already has the item! so add it.
        final int[] inventory_size = new int[1];
        UUID test_item_uuid = item.getUuid();
        Thread checkInventoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // add item to inventory
                    user.getInventory().addItem(item);
                    assertTrue(user.getInventory().getItems().containsKey(item.getUuid()) );
                    inventory_size[0] = user.getInventory().getItems().size();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkInventoryThread.start();

        // TODO precondition: elastic search does have a record of the item


        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        // Source: http://stackoverflow.com/questions/22965839/espresso-click-by-text-in-list-view
        // Accessed November 5, 2015
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.editItem)).perform(click());

        // change item name
        final String NEW_ITEM_NAME = "Polaroid Camera";
        final String NEW_ITEM_QUALITY = "A little dented";
        onView(withId(R.id.itemName)).perform(typeText(NEW_ITEM_NAME), closeSoftKeyboard());
        onView(withId(R.id.itemQuality)).perform(typeText(NEW_ITEM_QUALITY), closeSoftKeyboard());
        onView(withId(R.id.saveItem)).perform(click());

        // test if item was edited in inventory, should still be the same UUID but different name and quality
        Thread checkEditedItem = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // get item from inventory
                    // it should still exist, inventory size should be same
                    assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
                    assertEquals(inventory_size[0], user.getInventory().getItems().size());

                    // it should have a new item name and new item quality
                    Item testItem = user.getInventory().getItem(item.getUuid());
                    assertEquals(NEW_ITEM_NAME, testItem.getItemName());
                    assertEquals(NEW_ITEM_QUALITY, testItem.getItemQuality());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkEditedItem.start();
    }

    public void testRemoveAnItem() {
        //UC1.03.01 - Remove an Item
        final User user = PrimaryUser.getInstance();

        // initialize test item
        final Item item = new Item();
        item.setItemName(ITEM_NAME);
        item.setItemQuantity(ITEM_QUANTITY);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemIsPrivate(ITEM_PRIVATE);
        item.setItemDescription(ITEM_DESCRIPTION);

        // precondition: user already has the item! so add it.
        final int[] inventory_size = new int[1];
        UUID test_item_uuid = item.getUuid();

        Thread checkInventoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // add item to inventory
                    user.getInventory().addItem(item);
                    assertTrue(user.getInventory().getItems().containsKey(item.getUuid()) );
                    inventory_size[0] = user.getInventory().getItems().size();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkInventoryThread.start();

        // TODO precondition: elastic search does have a record of the item


        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        // Source: http://stackoverflow.com/questions/22965839/espresso-click-by-text-in-list-view
        // Accessed November 5, 2015
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.deleteItem)).perform(click());

        Thread checkDeleteThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // item should not exist anymore! Inventory size decrease by 1
                    assertFalse(user.getInventory().getItems().containsKey(item.getUuid()));
                    assertEquals(inventory_size[0]-1, user.getInventory().getItems().size());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkDeleteThread.start();

    }

    public void testViewOwnInventory() {
        //UC1.04.01 - View Own Inventory

        final User user = PrimaryUser.getInstance();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // does user inventory have anything??
        final int[] inventory_size = new int[1];
        Thread checkInventoryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inventory_size[0] = user.getInventory().getItems().size();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        checkInventoryThread.start();

        // if user has items, they should be able to see it
        if (inventory_size[0] > 0) {

        } else {

        }
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
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
    }

    @Ignore
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
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
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
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.editItem)).perform(click());

        // change item check box
        onView(withId(R.id.itemPrivateCheckBox))
                .perform(click());

        onView(withId(R.id.saveItem)).perform(click());
    }

}