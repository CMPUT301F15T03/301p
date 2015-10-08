package com.cmput301f15t03.dreamteamsupreme.cmput301f15t03;

/**
 * Created by rhanders on 2015-10-08.
 */
public class MockTradeTest {

    public MockTradeTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        root = getActivity();

        TradeManager tradeManager = new BrowseSearch();

        User owner = new User("User A");
        User borrower = new User("User B");
        User user = new User("User C");
    }

    public void testOfferTradeWithFriend(){
        //UC1.4.1 BrowseAllFriendsGeneralSearch
        TradeManager tradeManager = new BrowseSearch();

        User owner = new User("User A");
        User borrower = new User("User B");

        Item itemBorrowerWants = new Item("Item Borrower Wants");
        owner.addItemToInventory(itemBorrowerWants);

        List<Item> itemsInReturn = new List<Item>();
        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
        tradeManager.initiate(trade);

        assert(tradeManager.exists(trade));
    }

    public void testBrowseFriendGeneralSearch(){
        //UC1.3.1.2 BrowseFriendGeneralSearch
        List<Items> expectedItems = new List();
        expectedItems.add(new Items("item1", "BigCategory");
        expectedItems.add(new Items("item2", "SmallCategory");

        List<Items> getItems = browser.getFriendPublicInventory("Sally");

        int i= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }
    }

    public void testBrowseAllFriendsCategorySearch(){
        //UC1.3.1.3 BrowseAllFriendsCategorySearch
        List<Items> expectedItems = new List();
        expectedItems.add(new Items("item2", "SmallCategory");
        expectedItems.add(new Items("item2", "SmallCategory");

        List<Items> getItems = browser.getAllFriendsPublicInventories("SmallCategory");

        int i= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }
    }

    public void testBrowseFriendCategorySearch(){
        //UC1.3.1.4 BrowseFriendCategorySearch
        List<Items> expectedItems = new List();
        expectedItems.add(new Items("item2", "SmallCategory");

        List<Items> getItems = browser.getFriendPublicInventory("Sally", "SmallCategory");

        int i= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }
    }

    public void testBrowseAllFriendsTextualQuerySearch(){
        //UC1.3.1.5 BrowseAllFriendsTextualQuerySearch
        List<Items> expectedItems = new List();
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
        //UC1.3.1.6 BrowseFriendTextualQuerySearch
        List<Items> expectedItems = new List();
        expectedItems.add(new Items("item2", "SmallCategory");

        List<Items> getItems = browser.getFriendPublicInventory("item2");

        int i= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }
    }

    public void testOfflineBrowsing(){
        //UC1.3.3.1 OfflineBrowsing
        List<Items> expectedItems = new List();
        expectedItems.add(new Items("item1", "BigCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));
        expectedItems.add(new Items("item1", "BigCategory"));
        expectedItems.add(new Items("item2", "SmallCategory"));

        List<Items> getItems = browser.getAllFriendsPublicInventories();

        int i= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }

        //GO OFFLINE
        List<Items> getItems = browser.getAllFriendsPublicInventories();

        int i= 0;
        For(Items i: getItems){
            assertTrue(i.equals(expectedItems.get(i)));
            i++;
        }
    }

}
