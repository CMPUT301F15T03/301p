package ca.ualberta.cmput301.t03.inventory;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.filters.item_criteria.CategoryFilterCriteria;
import ca.ualberta.cmput301.t03.filters.item_criteria.PrivateFilterCriteria;
import ca.ualberta.cmput301.t03.filters.item_criteria.StringQueryFilterCriteria;

import static junit.framework.Assert.assertEquals;

/**
 * Created by quentinlautischer on 2015-11-06.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilteringInventoryModelTest {
    BrowsableInventories modelBrowse;
    ArrayList<Item> list;
    ArrayList<FilterCriteria> filters;
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        modelBrowse = new BrowsableInventories();
        list = new ArrayList<>();
        filters = new ArrayList<>();

        Item item = new Item();
        item.setItemName("item1");
        item.setItemCategory("Cameras");
        item.setItemDescription("testDesc1");
        item.setItemIsPrivate(false);
        list.add(item);

        item = new Item();
        item.setItemName("item2");
        item.setItemCategory("Flash Photography");
        item.setItemDescription("testDesc2");
        item.setItemIsPrivate(false);
        list.add(item);

        item = new Item();
        item.setItemName("item3");
        item.setItemCategory("Cameras");
        item.setItemDescription("testDesc3 multi word");
        item.setItemIsPrivate(true);
        list.add(item);
    }

    @Test
    public void testBrowseViewableFilter() {
        filters.add( new PrivateFilterCriteria() );
        ArrayList<Item> filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(2, filteredList.size());
        assertEquals("item1", filteredList.get(0).getItemName());
        assertEquals("item2", filteredList.get(1).getItemName());
    }

    @Test
    public void testBrowseTextualSearchNameFilter() {
        filters.add(new StringQueryFilterCriteria("item1"));
        ArrayList<Item> filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(1, filteredList.size());
        assertEquals("item1", filteredList.get(0).getItemName());

        filters.clear();
        filters.add(new StringQueryFilterCriteria("testDesc2"));
        filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(1, filteredList.size());
        assertEquals("testDesc2", filteredList.get(0).getItemDescription());

        filters.clear();
        filters.add(new StringQueryFilterCriteria("multi"));
        filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(1, filteredList.size());
        assertEquals("item3", filteredList.get(0).getItemName());

        filters.clear();
        filters.add(new StringQueryFilterCriteria("multi word"));
        filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(1, filteredList.size());
        assertEquals("item3", filteredList.get(0).getItemName());

        filters.clear();
        filters.add(new StringQueryFilterCriteria("item"));
        filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(3, filteredList.size());
        assertEquals("item1", filteredList.get(0).getItemName());
        assertEquals("item2", filteredList.get(1).getItemName());
        assertEquals("item3", filteredList.get(2).getItemName());

        filters.clear();
        filters.add(new StringQueryFilterCriteria("test"));
        filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(3, filteredList.size());
        assertEquals("item1", filteredList.get(0).getItemName());
        assertEquals("item2", filteredList.get(1).getItemName());
        assertEquals("item3", filteredList.get(2).getItemName());
    }

    @Test
    public void testBrowseCategorySearchFilter(){
        filters.add(new CategoryFilterCriteria("Cameras"));
        ArrayList<Item> filteredList = modelBrowse.getFilteredItems(list, filters);
        assertEquals(2, filteredList.size());
        assertEquals("item1", filteredList.get(0).getItemName());
        assertEquals("item3", filteredList.get(1).getItemName());
    }


}
