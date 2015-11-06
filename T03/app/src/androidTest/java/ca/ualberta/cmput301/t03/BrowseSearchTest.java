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

package ca.ualberta.cmput301.t03;

/**
 * Created by quentinlautischer on 2015-10-07.
 */
//public class BrowseSearchTest extends ActivityInstrumentationTestCase2{
//
//    public BrowseSearchTest() {
//        super(com.cmput301f15t03.dreamteamsupreme.cmput301f15t03.MainActivity.class);
//    }
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        root = getActivity();
//        BrowseSearch browseSearch = new BrowseSearch();
//
//        User friend1 = new User("Sally");
//        User friend2 = new User("Sammy");
//
//        friend1.addFriend(friend2);
//
//        Inventory<Items> mockFriendInventory1 = new Inventory<Items>();
//        mockFriendInventory1.add(new Items("item1", "BigCategory"));
//        mockFriendInventory1.add(new Items("item2", "SmallCategory"));
//
//        Inventory<Items> mockFriendInventory2 = new Inventory<Items>();
//        mockFriendInventory1.add(new Items("item1", "BigCategory"));
//        mockFriendInventory1.add(new Items("item2", "SmallCategory"));
//
//        friend1.setInventory = mockFriendInventory1;
//        friend2.setInventory = mockFriendInventory2;
//
//    }
//
//    public void testBrowseAllFriendsGeneralSearch() {
//        //UC3.1.1 BrowseAllFriendsGeneralSearch
//        List<Items> expectedItems = new ArrayList<Items>();
//        expectedItems.add(new Items("item1", "BigCategory"));
//        expectedItems.add(new Items("item2", "SmallCategory"));
//        expectedItems.add(new Items("item1", "BigCategory"));
//        expectedItems.add(new Items("item2", "SmallCategory"));
//
//        List<Items> getItems = browser.getAllFriendsPublicInventories();
//
//        int n= 0;
//        for(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(n)));
//            n++;
//        }
//    }
//
//    public void testBrowseFriendGeneralSearch() {
//        //UC3.1.2 BrowseFriendGeneralSearch
//        List<Items> expectedItems = new ArrayList<Items>();
//        expectedItems.add(new Items("item1", "BigCategory"));
//        expectedItems.add(new Items("item2", "SmallCategory"));
//
//        List<Items> getItems = browser.getFriendPublicInventory("Sally");
//
//        int n= 0;
//        for(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(n)));
//            n++;
//        }
//    }
//
//    public void testBrowseAllFriendsCategorySearch() {
//        //UC3.1.3 BrowseAllFriendsCategorySearch
//        List<Items> expectedItems = new ArrayList<Items>();
//        expectedItems.add(new Items("item2", "SmallCategory"));
//        expectedItems.add(new Items("item2", "SmallCategory"));
//
//        List<Items> getItems = browser.getAllFriendsPublicInventories("SmallCategory");
//
//        int n= 0;
//        for(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(n)));
//            n++;
//        }
//    }
//
//    public void testBrowseFriendCategorySearch() {
//        //UC3.1.4 BrowseFriendCategorySearch
//        List<Items> expectedItems = new ArrayList<Items>();
//        expectedItems.add(new Items("item2", "SmallCategory"));
//
//        List<Items> getItems = browser.getFriendPublicInventory("Sally", "SmallCategory");
//
//        int i= 0;
//        for(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(i)));
//            i++;
//        }
//    }
//
//    public void testBrowseAllFriendsTextualQuerySearch() {
//        //UC3.1.5 BrowseAllFriendsTextualQuerySearch
//        List<Items> expectedItems = new ArrayList<Items>();
//        expectedItems.add(new Items("item2", "SmallCategory");
//        expectedItems.add(new Items("item2", "SmallCategory");
//
//        List<Items> getItems = browser.getAllFriendsPublicInventories("item2");
//
//        int i= 0;
//        For(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(i)));
//            i++;
//        }
//    }
//
//    public void testBrowseFriendTextualQuerySearch(){
//        //UC3.1.6 BrowseFriendTextualQuerySearch
//        List<Items> expectedItems = new ArrayList<Items>();
//        expectedItems.add(new Items("item2", "SmallCategory");
//
//        List<Items> getItems = browser.getFriendPublicInventory("item2");
//
//        int j= 0;
//        For(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(j)));
//            j++;
//        }
//    }
//
//    public void testOfflineBrowsing() {
//        //UC3.3.1 OfflineBrowsing
//        NetworkManager.setDeviceOnline();
//        asssertTrue(NetworkManager.deviceIsOnline());
//
//        List<Items> expectedItems = new ArrayList<Items>();
//        expectedItems.add(new Items("item1", "BigCategory"));
//        expectedItems.add(new Items("item2", "SmallCategory"));
//        expectedItems.add(new Items("item1", "BigCategory"));
//        expectedItems.add(new Items("item2", "SmallCategory"));
//
//        List<Items> getItems = browser.getAllFriendsPublicInventories();
//
//        int n= 0;
//        for(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(n)));
//            n++;
//        }
//
//        NetworkManager.setDeviceOffline(); // set device offline
//        assertTrue(NetworkManager.deviceIsOffline()); // make sure the device is offline
//
//        List<Items> getItems = browser.getAllFriendsPublicInventories();
//
//        int n= 0;
//        for(Items i: getItems){
//            assertTrue(i.equals(expectedItems.get(i)));
//            n++;
//        }
//    }
//
//}
