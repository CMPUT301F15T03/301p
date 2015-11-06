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
