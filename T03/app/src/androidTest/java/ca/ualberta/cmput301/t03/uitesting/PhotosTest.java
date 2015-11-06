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

package ca.ualberta.cmput301.t03.uitesting;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.photo.Photo;
import ca.ualberta.cmput301.t03.user.User;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

/**
 * Created by mmabuyo on 2015-11-05.
 */
public class PhotosTest extends ActivityInstrumentationTestCase2<MainActivity> {
    // TEST ITEM FIELDS
    String ITEM_NAME = "Camera";
    int ITEM_QUANTITY = 1;
    String ITEM_QUALITY = "A+";
    String ITEM_CATEGORY = "Cameras";
    boolean ITEM_PRIVATE = false;
    String ITEM_DESCRIPTION = "Pretty great camera";
    private MainActivity mActivity;
    private Context mContext;

    public PhotosTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = this.getInstrumentation().getTargetContext();

        PrimaryUserHelper.setup(mContext);
    }

    @Override
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(mContext);

        super.tearDown();
    }

    // for UC06.01.01 AttachPhotographsToItems
    public void testAttachPhotographsToItems() {
        // set up preconditions: owner is editing an item

        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
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
        }

        // TODO precondition: elastic search does have a record of the item

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());
        onView(withId(R.id.editItem)).perform(click());

        onView(withId(R.id.uploadPhotos)).perform(click());
        onView(withId(R.id.saveItem)).perform(click());

        try {
            // get item from inventory
            // it should still exist, inventory size should be same
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));

            // it should have photos
            Item testItem = user.getInventory().getItem(item.getUuid());
            assertEquals(1, testItem.getPhotoList().getPhotos().size());
        } catch (IOException e) {
            assertTrue("IOException in testEditAnItem", Boolean.FALSE);
        }

//        // create some photos
//        Photo photo1 = new Photo("img/1");
//        Photo photo2 = new Photo("img/2");
//        Photo photo3 = new Photo("img/3");
//        Photo photo4 = new Photo("img/4");
//        Photo photo5 = new Photo("img/5");
//        Photo photo6 = new Photo("img/6");
//        tempItem.addPhoto(photo1);
//
//        // photo should have be added to item's photos
//        assertTrue(item.getPhotos().contains(photo1));
//
//        // adding more photos to the item
//        tempItem.addPhoto(photo2);
//        tempItem.addPhoto(photo3);
//
//        assertEquals(tempItem.getPhotos().size, 3);
//
//        // photo size > file size requirement should not get added
//        try {
//            Photo largePhoto = new Photo("img/7");
//            tempItem.addPhoto(largePhoto);
//        } catch (OversizedPhotoException e) {
//            assertTrue(true);
//        }
//
//        // cannot go over maximum number of photos to an item
//        // right now, assuming max number is 5 for each item
//        tempItem.addPhoto(photo4);
//        tempItem.addPhoto(photo5);
//
//        // so adding a 6th picture should cause an error
//        try {
//            tempItem.addPhoto(photo6);
//        } catch (MaxPhotosLimitException e) {
//            assertTrue(true);
//        }
    }

    // for UC06.02.01 ViewItemPhotographs
    public void testViewItemPhotographs() {
        // set up preconditions: item exists with photographs attached
        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        }

        // initialize test item
        final Item item = new Item();
        item.setItemName(ITEM_NAME);
        item.setItemQuantity(ITEM_QUANTITY);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemIsPrivate(ITEM_PRIVATE);
        item.setItemDescription(ITEM_DESCRIPTION);

        Photo p1 = new Photo();
        ArrayList<Photo> photos = new ArrayList<Photo>();
        photos.add(p1);

        item.getPhotoList().setPhotos(photos);

        final int[] inventory_size = new int[1];
        try {
            // add item to inventory
            user.getInventory().addItem(item);
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testEditAnItem", Boolean.FALSE);
        }

        // TODO precondition: elastic search does have a record of the item

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());

        onView(withId(R.id.itemMainPhoto)).perform(click());

    }

    // for UC06.03.01 DeleteAttachedPhotographs
    public void testDeleteAttachedPhotographs() {
        // set up preconditions: item exists with photographs attached
// set up preconditions: item exists with photographs attached
        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
        }

        // initialize test item
        final Item item = new Item();
        item.setItemName(ITEM_NAME);
        item.setItemQuantity(ITEM_QUANTITY);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemIsPrivate(ITEM_PRIVATE);
        item.setItemDescription(ITEM_DESCRIPTION);

        Photo p1 = new Photo();
        ArrayList<Photo> photos = new ArrayList<Photo>();
        photos.add(p1);

        item.getPhotoList().setPhotos(photos);

        final int[] inventory_size = new int[1];
        try {
            // add item to inventory
            user.getInventory().addItem(item);
            assertTrue(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testEditAnItem", Boolean.FALSE);
        }

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText("Inventory")).check(matches(isDisplayed())).perform(click());

        // should be back in inventory view so click on item tile that was just added
        onData(anything()).inAdapterView(withId(R.id.InventoryListView)).atPosition(0).perform(click());

        onView(withId(R.id.editItem)).perform(click());
        onView(withId(R.id.itemPhoto1)).perform(longClick());

        // delete the photographs
        item.getPhotoList().removePhoto(p1);

        // check to make sure photo is not in the item's photographs anymore
        assertFalse(item.getPhotoList().getPhotos().contains(p1));

        // all photos should have been removed
        assertEquals(0, item.getPhotoList().getPhotos().size());
    }

    // for UC09.01.01 CreateItemsOffline
    public void testCreateItemsOffline() {
        // make connectivity offline
        // future reference: http://stackoverflow.com/questions/12535101/how-can-i-turn-off-3g-data-programmatically-on-android/12535246#12535246

        // BLOCKED BY US11.01.05 DataManager
//        NetworkManager.setDeviceOffline(); // set device offline
//        assertTrue(NetworkManager.deviceIsOffline()); // make sure the device is offline

        final User user = PrimaryUser.getInstance();

        // re-initialize inventory to be empty
        try {
            user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        } catch (IOException e) {
            assertTrue("IOException in testChangePublicStatus", false);
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
            // add item to inventory, but it won't be on the server
            user.getInventory().addItem(item);
            assertFalse(user.getInventory().getItems().containsKey(item.getUuid()));
            inventory_size[0] = user.getInventory().getItems().size();
        } catch (IOException e) {
            assertTrue("IOException in testEditAnItem", Boolean.FALSE);
        }

//		NetworkManager.setDeviceOnline();
//		asssertTrue(NetworkManager.deviceIsOnline());

        try {
            // now should be on the server
            assertFalse(user.getInventory().getItems().containsKey(item.getUuid()));
        } catch (IOException e) {
            assertTrue("IOException in testEditAnItem", Boolean.FALSE);
        }
        // assertEquals(server.getUser("UserName").getItem("50mm Cannon Lens"), tempItem); // server has the item
    }

    /**
     * UC06.05.01
     */
    public void testManualDownloadItemPhotoWhenAutoDownloadDisabled() {
//        AppSettings settings = AppSettings.getInstance();
//        Boolean originalValue = settings.getAutoDownloadModeValue();
//        settings.setAutoDownloadModeValue(Boolean.FALSE);

        User user = PrimaryUser.getInstance();
        //user.clearCache();
        try {
            HashMap<UUID, Item> items = user.getInventory().getItems();
            for (Item item : items.values()) {
                if (item.getPhotoList().getPhotos().size() > 0) {
                    Photo photo = item.getPhotoList().getPhotos().get(0);
                    assertFalse(photo.isDownloaded());

                    photo.downloadPhoto();
                    assertTrue(photo.isDownloaded());

                    //settings.setAutoDownloadModeValue(originalValue);
                    return;
                }
            }
        } catch (IOException e) {
            assertFalse(true);
        }


        // settings.setAutoDownloadModeValue(originalValue);
        // Dev note: test user should've items with photos. Fix it and
        // re-run the test if this fails
        assertTrue(false);
    }
}

