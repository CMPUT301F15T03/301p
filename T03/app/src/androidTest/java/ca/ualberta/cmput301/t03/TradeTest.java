//package ca.ualberta.cmput301.t03;
//
//import ca.ualberta.cmput301.t03.inventory.Item;
//import ca.ualberta.cmput301.t03.trading.Trade;
//import ca.ualberta.cmput301.t03.user.User;
//
///**
// * Created by rhanders on 2015-10-08.
// */
//public class MockTradeTest {
//
//    public MockTradeTest() {
//        super(MainActivity.class);
//    }
//
//    public void testOfferTradeWithFriend(){
//        //UC1.4.1 OfferTradeWithFriend
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//
//        assertTrue(tradeManager.exists(trade));
//        assertTrue(tradeManager.isOpen(trade));
//    }
//
//    public void testOwnerIsNotifiedOfTradeOffer(){
//        //UC1.4.2 OwnerIsNotifiedOfTradeOffer
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//
//        assertTrue(owner.wasNotifedOfTrade(trade));
//    }
//
//    public void testOwnerAcceptsTrade(){
//        //UC1.4.3 OwnerAcceptsOrDeclinesTrade main flow
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//
//        tradeManager.accept(trade);
//
//        assertFalse(owner.hasItemInInventory(itemBorrowerWants));
//        assertTrue(borrower.hasItemInInventory(itemBorrowerWants));
//        assertTrue(tradeManager.isAccepted(trade));
//        assertTrue(tradeManager.isClosed(trade));
//    }
//
//    public void testOwnerDeclinesTrade(){
//        //UC1.4.3 OwnerAcceptsOrDeclinesTrade alternate flow
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//
//        tradeManager.decline(trade);
//
//        assertTrue(owner.hasItemInInventory(itemBorrowerWants));
//        assertFalse(borrower.hasItemInInventory(itemBorrowerWants));
//        assertFalse(tradeManager.isAccepted(trade));
//        assertFalse(tradeManager.isClosed(trade)); // not closed, owner has chance to Counter Offer
//    }
//
//    public void testOwnerDeclinesTradeAndOffersCounterTrade(){
//        //UC1.4.4 OwnerOffersCounterTrade
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//
//        tradeManager.decline(trade);
//        Trade counterTrade = new CounterTrade(trade);
//
//        List<Item> counterItemsInReturn = new List<Item>();
//        if (borrower.hasInventoryItems()) {
//            counterItemsInReturn.add(borrower.getInventoryItems().get(0));
//        }
//        counterTrade.addItemsInReturn(counterItemsInReturn);
//        tradeManager.offer(trade);
//
//        assertTrue(owner.hasItemInInventory(itemBorrowerWants));
//        assertFalse(borrower.hasItemInInventory(itemBorrowerWants));
//        assertFalse(tradeManager.isAccepted(trade));
//        assertFalse(tradeManager.isClosed(trade));
//    }
//
//    public void testBorrowerEditsTrade(){
//        //UC1.4.5.1 BorrowerEditsTrade
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//
//        assertTrue(trade.itemsBorrowerWants().size() == 1);
//        assertTrue(trade.itemsBorrowerWants().get(0) == itemBorrowerWants);
//        assertTrue(trade.itemsInReturn().size() == 0);
//
//        // Editing
//        Item newItemBorrowerWants = owner.getInventoryItems().get(0);
//        trade.addItemBorrowerWants(newItemBorrowerWants);
//        trade.removeItemBorrowerWants(itemBorrowerWants);
//        trade.addItemsInReturn(borrower.getInventoryItems());
//        trade.removeItemsInReturn(borrower.getInventoryItems());
//
//        assertTrue(trade.itemsBorrowerWants().size() == 1);
//        assertTrue(trade.itemsBorrowerWants().get(0) == newItemBorrowerWants);
//        assertTrue(trade.itemsInReturn().size() == 0);
//    }
//
//    public void testOwnerEditsCounterTrade(){
//        //UC1.4.5.2 OwnerEditsCounterTrade
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//
//        assertTrue(trade.itemsBorrowerWants().size() == 1);
//        assertTrue(trade.itemsBorrowerWants().get(0) == itemBorrowerWants);
//        assertTrue(trade.itemsInReturn().size() == 0);
//
//        Trade counterTrade = new CounterTrade(trade);
//
//        // Editing
//        Item newItemBorrowerWants = owner.getInventoryItems().get(0);
//        counterTrade.addItemBorrowerWants(newItemBorrowerWants);
//        counterTrade.removeItemBorrowerWants(itemBorrowerWants);
//        counterTrade.addItemsInReturn(borrower.getInventoryItems());
//        counterTrade.removeItemsInReturn(borrower.getInventoryItems());
//
//        assertTrue(counterTrade.itemsBorrowerWants().size() == 1);
//        assertTrue(counterTrade.itemsBorrowerWants().get(0) == newItemBorrowerWants);
//        assertTrue(counterTrade.itemsInReturn().size() == 0);
//    }
//
//    public void testBorrowerDeletesTrade(){
//        //UC1.4.6.1 BorrowerDeletesTrade
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//
//        assertTrue(trade.isOfferable());
//        trade.delete();
//        assertFalse(trade.isOfferable());
//    }
//
//    public void testOwnerDeletesCounterTrade(){
//        //UC1.4.6.2 OwnerDeletesCounterTrade
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//
//        assert(trade.itemsBorrowerWants().size() == 1);
//        assert(trade.itemsBorrowerWants().get(0) == itemBorrowerWants);
//        assert(trade.itemsInReturn().size() == 0);
//
//        Trade counterTrade = new CounterTrade(trade);
//
//        assertTrue(counterTrade.isOfferable());
//        counterTrade.delete();
//        assertFalse(counterTrade.isOfferable());
//    }
//
//    public void testOwnerWritesTradeAcceptComments(){
//        //UC1.4.7.1 OwnerWritesTradeAcceptComments
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//
//        tradeManager.accept(trade);
//        trade = tradeManager.promptForTradeAcceptComments(trade, owner);
//
//        assertTrue(trade.getAcceptComments().length() > 0);
//    }
//
//    public void testOwnerAndBorrowerReceiveTradeDetailsEmail(){
//        //UC1.4.7.2 OwnerAndBorrowerReceiveTradeDetailsEmail
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        assertTrue(owner.getEmail() != null);
//        assertTrue(borrower.getEmail() != null);
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//
//        tradeManager.accept(trade);
//        trade = tradeManager.promptForTradeAcceptComments(trade, owner);
//
//        Boolean success = tradeManager.sendTradeDetailsEmail(trade);
//        assertTrue(success);
//    }
//
//    public void testUserBrowsesTradesInvolvingThem(){
//        //UC1.4.8 UserBrowsesTradesInvolvingThem
//        TradeManager tradeManager = new TradeManager();
//        List<Trade> expectedTrades = new List<Trade>();
//        List<Trade> unexpectedTrades = new List<Trade>();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//
//        tradeManager.offer(trade);
//        tradeManager.accept(trade);
//        expectedTrades.add(trade);
//
//        Trade unofferedTrade = trade.clone();
//        unexpectedTrades.add(unofferedTrade);
//
//        Trade tradeNotInvolvingBorrower = new Trade(new User("User C"), owner, itemBorrowerWants, itemsInReturn);
//        tradeManager.offer(trade);
//        tradeManager.accept(trade);
//        unexpectedTrades.add(tradeNotInvolvingBorrower);
//
//        User userC = new User("User C");
//        Trade tradeInvolvingBorrowerAsOwner = new Trade(new User("User C"), borrower, userC.getItemsInInventory().get(0), itemsInReturn);
//        tradeManager.offer(tradeInvolvingBorrowerAsOwner);
//        tradeManager.accept(tradeInvolvingBorrowerAsOwner);
//        expectedTrades.add(tradeInvolvingBorrowerAsOwner);
//
//        List<Trade> tradesInvolvingBorrower = tradeManager.getTradesForUser(borrower);
//        for (Trade t : expectedTrades) {
//            assertTrue(tradesInvolvingBorrower.contains(t));
//        }
//        for (Trade t : unexpectedTrades) {
//            assertFalse(tradesInvolvingBorrower.contains(t));
//        }
//    }
//
//    public void testOfferTradeWithFriendWhileOffline(){
//        //UC1.4.1 OfferTradeWithFriend
//        TradeManager tradeManager = new TradeManager();
//
//        User owner = new User("User A");
//        User borrower = new User("User B");
//
//        Item itemBorrowerWants = new Item("Item Borrower Wants");
//        owner.addItemToInventory(itemBorrowerWants);
//
//        List<Item> itemsInReturn = new List<Item>();
//        Trade trade = new Trade(borrower, owner, itemBorrowerWants, itemsInReturn);
//
//        assertTrue(NetworkManager.deviceIsOffline());
//        tradeManager.offer(trade);
//
//        while (NetworkManager.deviceIsOffline());
//        assertTrue(tradeManager.exists(trade));
//        assertTrue(tradeManager.isOpen(trade));
//    }
//
//
//
//}
