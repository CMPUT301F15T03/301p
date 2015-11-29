package ca.ualberta.cmput301.t03.inventory;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.mocks.TestDto;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;

import static junit.framework.Assert.assertEquals;

/**
 * Created by quentinlautischer on 2015-11-06.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BrowseInventoryModelTest {
    BrowsableInventories modelBrowse;
    ArrayList<Item> list;
    ArrayList<FilterCriteria> filters;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    public void setUp() {
        modelBrowse = new BrowsableInventories();
        list = new ArrayList<>();
        filters = new ArrayList<>();
    }

    @Test
    public void testBrowseViewableFilter() {
        ArrayList<Item> filteredList = modelBrowse.getFilteredItems(list, filters);

    }

}
