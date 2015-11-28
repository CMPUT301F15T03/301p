package ca.ualberta.cmput301.t03.inventory;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.ualberta.cmput301.t03.MainActivity;

import static junit.framework.Assert.assertEquals;

/**
 * Created by quentinlautischer on 2015-11-06.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserInventoryModelTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void testInventoryInit() {
        Inventory inventory = new Inventory();
        assertEquals(inventory.getItems().size(), 0);
    }



}

