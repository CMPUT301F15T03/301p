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

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;
import ca.ualberta.cmput301.t03.user.User;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

/**
 * Created by mmabuyo on 2015-11-04.
 * <p>
 * CODE REUSE
 * Source: http://stackoverflow.com/questions/22965839/espresso-click-by-text-in-list-view
 * Accessed November 5, 2015. Used to select the first item in inventory
 * onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
 */
public class ItemsUITest extends ActivityInstrumentationTestCase2<MainActivity> {
    // TEST ITEM FIELDS
    String ITEM_NAME = "Camera";
    int ITEM_QUANTITY = 1;
    String ITEM_QUALITY = "A+";
    String ITEM_CATEGORY = "Cameras";
    boolean ITEM_PRIVATE = false;
    String ITEM_DESCRIPTION = "Pretty great camera";
    private MainActivity mActivity;
    private Context mContext;
    private HttpDataManager dataManager;

    public ItemsUITest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = this.getInstrumentation().getTargetContext();
        mActivity = getActivity();

        PrimaryUserHelper.setup(mContext);
    }

    @Override
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(mContext);

        super.tearDown();
    }

    public void testAddAnItem() {
        // UC1.01.01 AddAnItem
        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", false);
        }

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
        try {
            assertFalse(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testAddAnItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testAddAnItem", Boolean.FALSE);
        }

        // TODO precondition: elastic search does not have a record of the item


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
        if (ITEM_PRIVATE) {
            onView(withId(R.id.itemPrivateCheckBox)).perform(click());
        }
        onView(withId(R.id.itemDescription)).perform(typeText(ITEM_DESCRIPTION), closeSoftKeyboard());

        // add item through UI
        onView(withId(R.id.addItem)).perform(click());

        // check that it actually got added to inventory
        try {
            // check inventory, and inventory size should have increased by one!
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            assertEquals(inventory_size[0] + 1, user.getInventory().getItems().size());
        } catch (IOException e) {
            assertTrue("IOException in testAddAnItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testAddAnItem", Boolean.FALSE);
        }
    }

    public void testEditAnItem() {
        //UC1.02.01 - Edit an Item
        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", false);
        }

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

        try {
            // add item to inventory
            user.getInventory().addItem(item);
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testEditAnItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testEditAnItem", Boolean.FALSE);
        }

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

        onView(withId(R.id.itemName)).perform(clearText(), typeText(NEW_ITEM_NAME), closeSoftKeyboard());
        onView(withId(R.id.itemQuality)).perform(clearText(), typeText(NEW_ITEM_QUALITY), closeSoftKeyboard());
        onView(withId(R.id.saveItem)).perform(click());

        // test if item was edited in inventory, should still be the same UUID but different name and quality
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
            assertTrue("IOException in testEditAnItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testEditAnItem", Boolean.FALSE);
        }

    }

    public void testRemoveAnItem() {
        //UC1.03.01 - Remove an Item
        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", Boolean.FALSE);
        }

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

        try {
            // add item to inventory
            user.getInventory().addItem(item);
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testRemoveAnItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testRemoveAnItem", Boolean.FALSE);
        }

        // TODO precondition: elastic search does have a record of the item

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.deleteItem)).perform(click());

        try {
            // item should not exist anymore! Inventory size decrease by 1
            assertFalse(user.getInventory().getItems().containsKey(item.getUuid()));
            assertEquals(inventory_size[0] - 1, user.getInventory().getItems().size());
        } catch (IOException e) {
            assertTrue("IOException in testRemoveAnItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testRemoveAnItem", Boolean.FALSE);
        }

        // TODO: assert view that there is nothing in inventory any more.
    }

    public void testViewOwnInventory() {
        //UC1.04.01 - View Own Inventory

        final User user = PrimaryUser.getInstance();

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // does user inventory have anything??
        final int[] inventory_size = new int[1];
        try {
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testViewOwnInventory", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testViewOwnInventory", Boolean.FALSE);
        }

        // TODO: user should be able to see all their items
        // if user has items, they should be able to see it
        if (inventory_size[0] > 0) {
            onData(anything())
                    .inAdapterView(withId(R.id.InventoryListView))
                    .check(matches(isDisplayed()));
        } else {
            onData(anything())
                    .inAdapterView(withId(R.id.InventoryListView))
                    .check(doesNotExist());
        }

        // TODO: assert views
    }

    public void testViewItem() {
        //UC1.05.01 - View Item
        final User user = PrimaryUser.getInstance();

        // initialize test item
        final Item item = new Item();
        item.setItemName(ITEM_NAME);
        item.setItemQuantity(ITEM_QUANTITY);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemIsPrivate(ITEM_PRIVATE);
        item.setItemDescription(ITEM_DESCRIPTION);

        // precondition: item should exist in user inventory, so let's add the item
        final int[] inventory_size = new int[1];
        try {
            // add item to inventory
            user.getInventory().addItem(item);
//                    assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
//                    inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testRemoveAnItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testRemoveAnItem", Boolean.FALSE);
        }


        // TODO precondition: elastic search does have a record of the item

        // navigate to inventory
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // click to view details of the only item in inventory
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());

        // TODO: assert view
    }

    public void testChooseACategory() {
        //UC1.06.01 - Choose a Category
        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", false);
        }

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
        try {
            // add item to inventory
            user.getInventory().addItem(item);
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testChooseACategory", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChooseACategory", Boolean.FALSE);
        }

        // TODO precondition: elastic search does have a record of the item

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.editItem)).perform(click());

        // change item category
        final String NEW_ITEM_CATEGORY = "Instant Photography";
        onView(withId(R.id.itemCategory)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(NEW_ITEM_CATEGORY))).perform(click());

        // test if item was edited in inventory, should still be the same UUID but different category
        try {
            // get item from inventory
            // it should still exist, inventory size should be same
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            assertEquals(inventory_size[0], user.getInventory().getItems().size());

            // it should have the new item name and new item quality
            Item testItem = user.getInventory().getItem(item.getUuid());
            assertEquals(NEW_ITEM_CATEGORY, testItem.getItemCategory());
        } catch (IOException e) {
            assertTrue("IOException in testChooseACategory", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChooseACategory", Boolean.FALSE);
        }

    }


    public void testNavigateToItem() {
        //UC1.07.01 - Navigate to Entry Item
        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", false);
        }

        // initialize test item
        final Item item = new Item();
        item.setItemName(ITEM_NAME);
        item.setItemQuantity(ITEM_QUANTITY);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemIsPrivate(ITEM_PRIVATE);
        item.setItemDescription(ITEM_DESCRIPTION);

        // precondition: item should exist in user inventory, so let's add the item
        final int[] inventory_size = new int[1];
        try {
            // add item to inventory
            user.getInventory().addItem(item);
//                    assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
//                    inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testNavigateToItem", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testNavigateToItem", Boolean.FALSE);
        }


        // TODO precondition: elastic search does have a record of the item

        // navigate to inventory
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // click to view details of the only item in inventory
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());

        // TODO: assert view
    }

    public void testChangePublicStatus() {
        //UC1.08.01 - Change Public Status

        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", false);
        }

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

        try {
            // add item to inventory
            user.getInventory().addItem(item);
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", Boolean.FALSE);
        }


        // TODO precondition: elastic search does have a record of the item


        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.editItem)).perform(click());

        // item was public, now make it private
        onView(withId(R.id.itemPrivateCheckBox)).perform(click());
        onView(withId(R.id.saveItem)).perform(click());

        try {
            // get item from inventory
            // it should still exist, inventory size should be same
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            assertEquals(inventory_size[0], user.getInventory().getItems().size());

            // it should have private status
            Item testItem = user.getInventory().getItem(item.getUuid());
            assertTrue(testItem.isItemIsPrivate());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", Boolean.FALSE);
        } catch (ServiceNotAvailableException e) {
            assertTrue("ServiceNotAvailableException in testChangePublicStatus", Boolean.FALSE);
        }

    }
}