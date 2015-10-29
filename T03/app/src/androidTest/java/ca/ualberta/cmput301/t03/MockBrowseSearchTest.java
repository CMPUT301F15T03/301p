package ca.ualberta.cmput301.t03;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quentinlautischer on 2015-10-07.
 */
public class MockBrowseSearchTest extends ActivityInstrumentationTestCase2{

    public MockBrowseSearchTest() {
        super(com.cmput301f15t03.dreamteamsupreme.cmput301f15t03.MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = getActivity();
        BrowseSearch browseSearch = new BrowseSearch();

        User friend1 = new User("Sally");
        User friend2 = new User("Sammy");

        friend1.addFriend(friend2);

        Inventory<Items> mockFriendInventory1 = new Inventory<Items>();
        mockFriendInventory1.add(new Items("item1", "BigCategory"));
        mockFriendInventory1.add(new Items("item2", "SmallCategory"));

        Inventory<Items> mockFriendInventory2 = new Inventory<Items>();
        mockFriendInventory1.add(new Items("item1", "BigCategory"));
        mockFriendInventory1.add(new Items("item2", "SmallCategory"));

        friend1.setInventory = mockFriendInventory1;
        friend2.setInventory = mockFriendInventory2;

    }

    public void testBrowseAllFriendsGeneralSearch() {
        //UC3.1.1 BrowseAllFriendsGeneralSearch
        List<Items> expectedItems = new ArrayList<Items>();
        expectedItems.add(new Items("item1", "BigCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));
        expectedItems.add(new Items("item1", "BigCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));

        List<Items> getItems = browser.getAllFriendsPublicInventories();

        int n= 0;
        for(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(n)));
            n++;
        }
    }

    public void testBrowseFriendGeneralSearch() {
        //UC3.1.2 BrowseFriendGeneralSearch
        List<Items> expectedItems = new ArrayList<Items>();
        expectedItems.add(new Items("item1", "BigCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));

        List<Items> getItems = browser.getFriendPublicInventory("Sally");

        int n= 0;
        for(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(n)));
            n++;
        }
    }

    public void testBrowseAllFriendsCategorySearch() {
        //UC3.1.3 BrowseAllFriendsCategorySearch
        List<Items> expectedItems = new ArrayList<Items>();
        expectedItems.add(new Items("item2", "SmallCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));

        List<Items> getItems = browser.getAllFriendsPublicInventories("SmallCategory");

        int n= 0;
        for(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(n)));
            n++;
        }
    }

    public void testBrowseFriendCategorySearch() {
        //UC3.1.4 BrowseFriendCategorySearch
        List<Items> expectedItems = new ArrayList<Items>();
        expectedItems.add(new Items("item2", "SmallCategory"));

        List<Items> getItems = browser.getFriendPublicInventory("Sally", "SmallCategory");

        int i= 0;
        for(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }
    }

    public void testBrowseAllFriendsTextualQuerySearch() {
        //UC3.1.5 BrowseAllFriendsTextualQuerySearch
        List<Items> expectedItems = new ArrayList<Items>();
        expectedItems.add(new Items("item2", "SmallCategory");
        expectedItems.add(new Items("item2", "SmallCategory");

        List<Items> getItems = browser.getAllFriendsPublicInventories("item2");

        int i= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }
    }

    public void testBrowseFriendTextualQuerySearch(){
        //UC3.1.6 BrowseFriendTextualQuerySearch
        List<Items> expectedItems = new ArrayList<Items>();
        expectedItems.add(new Items("item2", "SmallCategory");

        List<Items> getItems = browser.getFriendPublicInventory("item2");

        int j= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(j)));
            j++;
        }
    }

    public void testOfflineBrowsing() {
        //UC3.3.1 OfflineBrowsing
        NetworkManager.setDeviceOnline();
        asssertTrue(NetworkManager.deviceIsOnline());

        List<Items> expectedItems = new ArrayList<Items>();
        expectedItems.add(new Items("item1", "BigCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));
        expectedItems.add(new Items("item1", "BigCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));

        List<Items> getItems = browser.getAllFriendsPublicInventories();

        int n= 0;
        for(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(n)));
            n++;
        }

        NetworkManager.setDeviceOffline(); // set device offline
        assertTrue(NetworkManager.deviceIsOffline()); // make sure the device is offline
        
        List<Items> getItems = browser.getAllFriendsPublicInventories();

        int n= 0;
        for(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            n++;
        }
    }

}
