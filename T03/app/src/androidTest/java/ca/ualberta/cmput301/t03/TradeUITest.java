package ca.ualberta.cmput301.t03;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;

import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.commontesting.SystemAnimations;
import ca.ualberta.cmput301.t03.user.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by rhanders on 2015-10-08.
 */
public class TradeUITest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    private final String testUsername2 = "TEST_JUNIT_2";

    private MainActivity mActivity;
    private SystemAnimations mSystemAnimations;

    public TradeUITest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mSystemAnimations = new SystemAnimations(getInstrumentation().getContext());
        mSystemAnimations.disableAll();
        mActivity = getActivity();

        PrimaryUserHelper.setup(this.getInstrumentation().getTargetContext());
    }

    @Override
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(this.getInstrumentation().getTargetContext());

//        mSystemAnimations.enableAll();
        super.tearDown();
    }

    public void testOfferTradeWithFriend(){
        //UC1.4.1 OfferTradeWithFriend

        // TODO create item in owner's inventory to trade for
        User currentUser = PrimaryUser.getInstance();
        try {
            currentUser.getFriends().addFriend(new User(testUsername2, mActivity.getBaseContext()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.BrowseListView))
                .perform(click());
        onView(withId(R.id.proposeTradeButton))
                .perform(click());
        onView(withId(R.id.tradeComposeConfirm))
                .perform(click());

        // TODO assert trade was created
        // TODO assert trade was offered

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
    }

    public void testOwnerIsNotifiedOfTradeOffer(){
        //UC1.4.2 OwnerIsNotifiedOfTradeOffer

        // TODO create trade for user to review

        // TODO open trades history

        // TODO assert trade exists in trades history

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
    }

    public void testOwnerAcceptsTrade(){
        //UC1.4.3 OwnerAcceptsOrDeclinesTrade main flow

        // TODO create trade for owner to accept

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
            .check(matches(isDisplayed()))
            .perform(click());
//        onView(withChild(allOf(withId(R.id.tradeTileTradeState), withText("offered by"))))
//                .perform(click());
//        onView(withId(R.id.tradeReviewAccept))
//                .perform(click());

        // TODO assert trade was accepted

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
    }

    public void testOwnerDeclinesTrade(){
        //UC1.4.3 OwnerAcceptsOrDeclinesTrade alternate flow

        // TODO create trade for the user to decline

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(this.getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withChild(allOf(withId(R.id.tradeTileTradeState), withText("offered by"))))
                .perform(click());
        onView(withId(R.id.tradeReviewDecline))
                .perform(click());

        // TODO assert trade was declined

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
    }

    public void testOwnerDeclinesTradeAndOffersCounterTrade(){
        //UC1.4.4 OwnerOffersCounterTrade

        // TODO create trade for the user to decline and counter offfer

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(this.getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withChild(allOf(withId(R.id.tradeTileTradeState), withText("offered by"))))
                .perform(click());
        onView(withId(R.id.tradeReviewDeclineAndCounterOffer))
                .perform(click());

        // TODO assert trade was declined

        // TODO compose counter offer

        // TODO assert counter offer was created

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
    }

    public void testBorrowerEditsTrade(){
        //UC1.4.5.1 BorrowerEditsTrade

        // TODO create item in owner's inventory to trade for

        onView(withId(R.id.BrowseListView))
                .perform(click());
        onView(withId(R.id.proposeTradeButton))
                .perform(click());

        // TODO assert trade in editable state
        // TODO change items offered to owner for owner's item
        // TODO assert trade view updated
        // TODO assert trade updated

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
    }

    public void testOwnerEditsCounterTrade(){
        //UC1.4.5.2 OwnerEditsCounterTrade

        // TODO create trade for the user to decline and counter offfer

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(this.getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withChild(allOf(withId(R.id.tradeTileTradeState), withText("offered by"))))
                .perform(click());
        onView(withId(R.id.tradeReviewDeclineAndCounterOffer))
                .perform(click());

        // TODO assert trade was declined

        // TODO compose counter offer

        // TODO assert counter offer was created

        // TODO assert counter offer is editable

        // TODO edit counter offer

        // TODO assert counter offer updated

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
    }

    public void testBorrowerDeletesTrade(){
        //UC1.4.6.1 BorrowerDeletesTrade

        // TODO create item in owner's inventory to trade for

        onView(withId(R.id.BrowseListView))
                .perform(click());
        onView(withId(R.id.proposeTradeButton))
                .perform(click());

        // TODO assert trade was created

        onView(withId(R.id.tradeComposeCancel))
                .perform(click());

        // TODO assert trade was cancelled

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
    }

    public void testOwnerDeletesCounterTrade(){
        //UC1.4.6.2 OwnerDeletesCounterTrade

        // TODO create trade for the user to decline and counter offfer

        onView(withContentDescription("Open navigation drawer"))
                .perform(click());
        onView(withText(this.getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withChild(allOf(withId(R.id.tradeTileTradeState), withText("offered by"))))
                .perform(click());
        onView(withId(R.id.tradeReviewDeclineAndCounterOffer))
                .perform(click());

        // TODO assert trade was declined

        // TODO compose counter offer

        // TODO assert counter offer was created

        onView(withId(R.id.tradeComposeCancel))
                .perform(click());

        // TODO assert counter offer cancelled

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
    }

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

    public void testUserBrowsesTradesInvolvingThem(){
        //UC1.4.8 UserBrowsesTradesInvolvingThem

        // TODO create trades in all possible states with the user as each role

        // TODO create trades which do not involve the user

        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(this.getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        // TODO assert each user trade is in the list

        // TODO assert each non-user trade is not in the list

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
    }

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



}
