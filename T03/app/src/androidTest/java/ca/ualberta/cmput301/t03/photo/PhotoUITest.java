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

package ca.ualberta.cmput301.t03.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.Drawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.TextView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.common.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.UserProfile;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.common.PauseForAnimation.pause;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class PhotoUITest {

    public static final String ITEM_NAME = "Test Item";
    public static final String ITEM_QUALITY = "Good";
    public static final String ITEM_CATEGORY = "Cameras";
    public static final String ITEM_DESCRIPTION = "Its a test camera";
    /**
     * A JUnit {@link Rule @Rule} to launch your activity under test. This is a replacement
     * for {@link ActivityInstrumentationTestCase2}.
     * <p>
     * Rules are interceptors which are executed for each test method and will run before
     * any of your setup code in the {@link Before @Before} method.
     * <p>
     * {@link ActivityTestRule} will create and launch of the activity for you and also expose
     * the activity under test. To get a reference to the activity you can use
     * the {@link ActivityTestRule#getActivity()} method.
     */


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(
            MainActivity.class, false, false);

    private MainActivity mActivity = null;


    @Before
    public void setActivity() throws Exception {
        PrimaryUserHelper.setup(InstrumentationRegistry.getTargetContext());
        pause();
        mActivity = mActivityRule.launchActivity(new Intent());

        pause();

        onView(withContentDescription(R.string.navigation_drawer_open))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText("Inventory"))
                .perform(click());
    }

    @After
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(InstrumentationRegistry.getTargetContext());
    }

    /**
     * UC06.01.01 AttachPhotographsToItem
     */
    @Test
    public void testAttachPhotographsToItems() throws IOException, ServiceNotAvailableException {
        Inventory inventory = PrimaryUser.getInstance().getInventory();
        Item item = new Item();
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemDescription(ITEM_DESCRIPTION);
        item.setItemIsPrivate(false);
        item.setItemName(ITEM_NAME);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemQuantity(1);

        for (Item item1 : inventory.getItems().values()) {
            inventory.removeItem(item1);
        }
        inventory.commitChanges();
        inventory.addItem(item);
        inventory.commitChanges();
        ItemPhotoController controller = new ItemPhotoController(item);
        controller.addPhotoToItem(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.photo_unavailable_test));
        controller.addPhotoToItem(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.photo_unavailable_test));
        controller.addPhotoToItem(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.photo_unavailable_test));


        assertEquals(3, item.getPhotoList().getPhotos().size());
    }

    /**
     * UC06.02.01 ViewItemPhotograph
     */
    @Test
    public void testViewItemPhotographs() throws IOException, ServiceNotAvailableException {
        Inventory inventory = PrimaryUser.getInstance().getInventory();
        Item item = new Item();
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemDescription(ITEM_DESCRIPTION);
        item.setItemIsPrivate(false);
        item.setItemName(ITEM_NAME);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemQuantity(1);

        for (Item item1 : inventory.getItems().values()) {
            inventory.removeItem(item1);
        }
        inventory.commitChanges();

        inventory.addItem(item);
        inventory.commitChanges();
        pause();
        Photo photo = new Photo(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.photo_unavailable_test));
        item.getPhotoList().addPhoto(photo);
        pause();
        onView(withText(ITEM_NAME)).check(matches(isDisplayed()));
        onView(withText(ITEM_NAME)).perform(click());
        pause();
        onView(withId(R.id.viewImagesbutton)).perform(click());
        onData(CoreMatchers.anything()).atPosition(0).check(matches(isDisplayed()));
    }

    /**
     * UC06.03.01 DeleteAttachedPhotograph
     */
    @Test
    public void testDeleteAttachedPhotographs() throws IOException, ServiceNotAvailableException {
        Inventory inventory = PrimaryUser.getInstance().getInventory();
        Item item = new Item();
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemDescription(ITEM_DESCRIPTION);
        item.setItemIsPrivate(false);
        item.setItemName(ITEM_NAME);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemQuantity(1);

        for (Item item1 : inventory.getItems().values()) {
            inventory.removeItem(item1);
        }
        inventory.commitChanges();

        inventory.addItem(item);
        inventory.commitChanges();
        pause();
        Photo photo = new Photo(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.photo_unavailable_test));
        item.getPhotoList().addPhoto(photo);
        pause();
        onView(withText(ITEM_NAME)).check(matches(isDisplayed()));
        onView(withText(ITEM_NAME)).perform(click());
        pause();
        onView(withId(R.id.viewImagesbutton)).perform(click());
        onData(CoreMatchers.anything()).atPosition(0).check(matches(isDisplayed()));
        onData(CoreMatchers.anything()).atPosition(0).perform(click());
        onView(withText("Delete")).perform(click());
        pause();
        assertEquals(0, item.getPhotoList().getPhotos().size());
    }

    @Test
    public void testPhotoIsUnder64k() throws IOException, ServiceNotAvailableException {
        Inventory inventory = PrimaryUser.getInstance().getInventory();
        Item item = new Item();
        item.setItemCategory(ITEM_CATEGORY);
        item.setItemDescription(ITEM_DESCRIPTION);
        item.setItemIsPrivate(false);
        item.setItemName(ITEM_NAME);
        item.setItemQuality(ITEM_QUALITY);
        item.setItemQuantity(1);

        for (Item item1 : inventory.getItems().values()) {
            inventory.removeItem(item1);
        }
        inventory.commitChanges();
        inventory.addItem(item);
        inventory.commitChanges();
        ItemPhotoController controller = new ItemPhotoController(item);
        controller.addPhotoToItem(BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.photo_unavailable_test));

        assertTrue(item.getPhotoList().getPhotos().get(0).getBase64Photo().getContents().length() < 65536);
    }

}
