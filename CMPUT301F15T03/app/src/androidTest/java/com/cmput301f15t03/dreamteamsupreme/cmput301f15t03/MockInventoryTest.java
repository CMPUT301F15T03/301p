package com.cmput301f15t03.dreamteamsupreme.cmput301f15t03;

import android.test.ActivityInstrumentationTestCase2;

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
        assert.false(inv.contains(tempItem)); // inventory should not have the item yet

        assert.null(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens"));
        tempItem.setPublic();
        assert.equals(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens"), tempItem);

        inv.addItem(tempItem);

        assert.true(inv.contains(tempItem));
        assert.equals(server.getUser("UserName").getItem("50mm Cannon Lens"), tempItem); // server has the item

        tempItem = new Item();
        tempItem.setName("100mm Cannon Lens");
        tempItem.setQualilty("Invalid Condition Choice");
        tempItem.setCategory("Not one of 10 relevent categories");
        tempItem.setPrivate();
        tempItem.addComment("This is my cherished cannon lens!!");

        inv.addItem(tempItem);

        // the item had invalid attirbutes set and is therefore not in the inventory
        assert.false(inv.contains(tempItem)); 
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
        assert.equals(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens").getQuality(), "Bad");
         // server has the updated item
        assert.equals(server.getUser("UserName").getItem("50mm Cannon Lens").getQuality(), tempItem);

        tempItem.setCategory("Invalid category");
        assert.equals(tempItem.getCategory(), "lenses"); // invalid entry is not saved
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

        assert.true(inv.contains(tempItem));
        inv.remove(tempItem);
        assert.false(inv.contains(tempItem));
        assert.equals(server.getUser("UserName").getItem("50mm Cannon Lens").getStatus(), "inactive");// server lists item as inactive
        assert.null(friend.getFriend("UserName").getInventory().getItem("50mm Cannon Lens")); // friend cant see removed item
        assert.true(friend.getTrade(0).getStatus().equals("canceled"));
    }

    public void testViewInventory() {

    }

    public void testViewItem() {

    }
}
