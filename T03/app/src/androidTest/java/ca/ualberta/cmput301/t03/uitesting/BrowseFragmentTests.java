package ca.ualberta.cmput301.t03.uitesting;

import android.test.ActivityInstrumentationTestCase2;
import android.view.MenuItem;
import android.widget.ListView;

import junit.framework.TestCase;

import ca.ualberta.cmput301.t03.MainActivity;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.BrowseInventoryFragment;

/**
 * Created by quentinlautischer on 2015-11-03.
 * http://roisagiv.github.io/blog/2014/01/04/testing-a-fragment-using-instrumentation/
 */
public class BrowseFragmentTests extends ActivityInstrumentationTestCase2<FragmentContainerActivity> {

    private BrowseInventoryFragment browseInventoryFragment;

    public BrowseFragmentTests() {
        super(FragmentContainerActivity.class);
    }

    @Override protected void setUp() throws Exception {
        super.setUp();

//        browseInventoryFragment = new BrowseInventoryFragment();
//        getActivity().addFragment(browseInventoryFragment, BrowseInventoryFragment.class.getSimpleName());
//        getInstrumentation().waitForIdleSync();
    }

    public void test_Should_Set_Title_TextView_Text() {
//        assertNotNull(browseInventoryFragment.getView().findViewById(R.id.BrowseListView));
//        ListView lv = (ListView) browseInventoryFragment.getView().findViewById(R.id.BrowseListView);
//        assertEquals(0, lv.getCount());
    }
}
