package ca.ualberta.cmput301.t03;

import android.test.ActivityInstrumentationTestCase2;

import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

public class MockInventoryTest extends ActivityInstrumentationTestCase2{
    public MockInventoryTest(Class activityClass) {
        super(activityClass);
    }

    public void testAddItem() {
        /**
        * US01.01.01 As an owner, I want to remove, edit, or add an item to my inventory that I want to share. It will have a name, quantity, quality, category, if I want to share it with others, and comments.
        * 
        * US01.04.01 As an owner, not every item in my inventory will be shared or listed. Items that are not to publicly shared will not be. As an owner, I might use them for trades.
        *
        * US01.07.01 As an owner, I want the category for an item to be one of 10 relevant categories for THINGS.
        **/
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        Item tempItem = new Item();
        tempItem.setName("50mm Cannon Lens");
        tempItem.setQualilty("Good Condition");
        tempItem.setCategory("lenses");
        tempItem.setPrivate();
        tempItem.addComment("This is my cherished cannon lens!!");
        assertFalse(inv.contains(tempItem)); // inventory should not have the item yet

        assertNull(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens"));
        tempItem.setPublic();
        assertEquals(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens"), tempItem);

        inv.addItem(tempItem);

        assertTrue(inv.contains(tempItem));
        assertEquals(server.getUser("UserName").getItem("50mm Cannon Lens"), tempItem); // server has the item

        tempItem = new Item();
        tempItem.setName("100mm Cannon Lens");
        tempItem.setQualilty("Invalid Condition Choice");
        tempItem.setCategory("Not one of 10 relevent categories");
        tempItem.setPrivate();
        tempItem.addComment("This is my cherished cannon lens!!");

        inv.addItem(tempItem);

        // the item had invalid attirbutes set and is therefore not in the inventory
        assertFalse(inv.contains(tempItem)); 
    }

    public void testEditAnItem() {
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        tempItem.setName("50mm Cannon Lens");
        tempItem.setQualilty("Bad Condition"); // changed this
        tempItem.setCategory("lenses");
        tempItem.setPublic();
        tempItem.addComment("This is my cherished cannon lens!!");
        inv.addItem(tempItem);

        tempItem.setQualilty("Bad");
        // friend sees the updated quality
        assertEquals(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens").getQuality(), "Bad");
         // server has the updated item
        assertEquals(server.getUser("UserName").getItem("50mm Cannon Lens").getQuality(), tempItem);

        tempItem.setCategory("Invalid category");
        assertEquals(tempItem.getCategory(), "lenses"); // invalid entry is not saved
    }

    public void testRemoveAnItem() {
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        tempItem.setName("50mm Cannon Lens");
        tempItem.setQualilty("Bad Condition"); // changed this
        tempItem.setCategory("lenses");
        tempItem.setPublic();
        tempItem.addComment("This is my cherished cannon lens!!");
        inv.addItem(tempItem);

        friend.initiateTrade(User, tempItem);

        assertTrue(inv.contains(tempItem));
        inv.remove(tempItem);
        assertFalse(inv.contains(tempItem));
        assertEquals(server.getUser("UserName").getItem("50mm Cannon Lens").getStatus(), "inactive");// server lists item as inactive
        assertNull(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens")); // friend cant see removed item
        assertTrue(friend.getTrade(0).getStatus().equals("canceled"));
    }

    public void testViewInventory() {
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        tempItem.setName("50mm Cannon Lens");
        tempItem.setQualilty("Bad Condition"); // changed this
        tempItem.setCategory("lenses");
        tempItem.setPublic();
        tempItem.addComment("This is my cherished cannon lens!!");
        inv.addItem(tempItem);

        assertEquals(InventoryView.getView().getItems(), inventory.getItems());
    }

    public void testViewItem() {
        // i dont really know how to test UI
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        tempItem.setName("50mm Cannon Lens");
        tempItem.setQualilty("Bad Condition");
        tempItem.setCategory("lenses");
        tempItem.setPublic();
        tempItem.addComment("This is my cherished cannon lens!!");
        inv.addItem(tempItem);

        assertEquals(InventoryView.getView().getItems().getItem("50mm Cannon Lens"), inventory.getItem("50mm Cannon Lens"));
    }

    public void testChooseCategory() {
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        tempItem.setName("50mm Cannon Lens");
        String category = Items.Categories[0];
        tempItem.setQualilty("Bad Condition");
        tempItem.setCategory(category); // valid selection for category
        tempItem.setPublic();
        tempItem.addComment("This is my cherished cannon lens!!");
        inv.addItem(tempItem);

        assertEquals(inventory.getItem("50mm Cannon Lens").getCategory(), category); // add item and check category
        category = Items.Categories[4];
        inventory.getItem("50mm Cannon Lens").setCategory(category);
        assertEquals(inventory.getItem("50mm Cannon Lens").getCategory(), category); // edit item and check category
    }

    public void testNavigateToEntryItem() {
    	ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        tempItem.setName("50mm Cannon Lens");
        String category = Items.Categories[0];
        tempItem.setQualilty("Bad Condition");
        tempItem.setCategory(category);
        tempItem.setPublic();
        tempItem.addComment("This is my cherished cannon lens!!");
        inv.addItem(tempItem);


        MainActivity activity = (MainActivity) getActivity();

        // code from https://developer.android.com/training/activity-testing/activity-functional-testing.html
        // Date: 2015-10-16
        // Set up an ActivityMonitor
        Instrumentation.ActivityMonitor receiverActivityMonitor =
                getInstrumentation().addMonitor(InventoryActivity.class.getName(),
                        null, false);



        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.inventorybutton.performItemClick(v, 0, v.getId());
            }
        });
        getInstrumentation().waitForIdleSync();

        // Validate that ReceiverActivity is started
        InventoryActivity receiverActivity = (InventoryActivity)
                receiverActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_MS);
        assertNotNull("ReceiverActivity is null", receiverActivity);
        assertEquals("Monitor for ReceiverActivity has not been called",
                1, receiverActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                InventoryActivity.class, receiverActivity.getClass());

        ItemList itemList = receiverActivity.getItemList();

        assertTrue(itemList.contains(tempItem));
    }

    public void testChangePublicStatus() {
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        User friend = new User("friend");
        owner.addFriend(friend);
        sever.addUser(owner);
        Inventory inv = new Inventory();
        owner.setInventory(inv);

        tempItem.setName("50mm Cannon Lens");
        String category = Items.Categories[0];
        tempItem.setQualilty("Bad Condition");
        tempItem.setCategory(category); // valid selection for category
        tempItem.setPublic();
        tempItem.addComment("This is my cherished cannon lens!!");
        inv.addItem(tempItem);

        assertEquals(inventory.getItem("50mm Cannon Lens").getStatus(), "Public"); // add item and check category
        assertEquals(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens"), tempItem);

        inventory.getItem("50mm Cannon Lens").setPrivate();
        assertEquals(inventory.getItem("50mm Cannon Lens").getStatus(), "private"); // edit item and check category
        assertNull(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens"));
    }
}
