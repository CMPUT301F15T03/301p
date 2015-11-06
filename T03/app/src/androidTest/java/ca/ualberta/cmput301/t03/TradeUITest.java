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

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import ca.ualberta.cmput301.t03.commontesting.PrimaryUserHelper;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.Trade;
import ca.ualberta.cmput301.t03.trading.TradeStateAccepted;
import ca.ualberta.cmput301.t03.trading.TradeStateCancelled;
import ca.ualberta.cmput301.t03.trading.TradeStateDeclined;
import ca.ualberta.cmput301.t03.trading.TradeStateOffered;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;
import ca.ualberta.cmput301.t03.user.User;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static ca.ualberta.cmput301.t03.commontesting.PauseForAnimation.pause;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;

/**
 * Created by rhanders on 2015-10-08.
 */
public class TradeUITest
        extends ActivityInstrumentationTestCase2<MainActivity> {

    private static final String TEST_USER_FRIEND_1 = "TEST_USER_FRIEND_1";
    private static final String TEST_ITEM_1_CATEGORY = "TEST_ITEM_1_CATEGORY";
    private static final String TEST_ITEM_1_NAME = "TEST_ITEM_1_NAME";
    private MainActivity mActivity;
    private Context mContext;

    public TradeUITest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        mContext = this.getInstrumentation().getTargetContext();

        /**
         * Setup users.
         * user
         *  - clear friends
         *  - clear items
         *  - clear trades
         * userFriend1
         *  - clear friends
         *  - clear items
         */
        PrimaryUserHelper.setup(mContext);
        User user = PrimaryUser.getInstance();
        user.getFriends().setFriends(new ArrayList<User>());
        user.getInventory().setItems(new LinkedHashMap<UUID, Item>());
        user.getTradeList().setTrades(new LinkedHashMap<UUID, Trade>());

        User userFriend1 = new User(TEST_USER_FRIEND_1, mContext);
        userFriend1.getFriends().setFriends(new ArrayList<User>());
        userFriend1.getInventory().setItems(new LinkedHashMap<UUID, Item>());
    }

    @Override
    public void tearDown() throws Exception {
        PrimaryUserHelper.tearDown(mContext);

        super.tearDown();
    }

    /**
     * UC1.4.1 OfferTradeWithFriend
     */
    public void testOfferTradeWithFriend() {
        /**
         * Create an owner with an item, and add them as a friend
         * The user can then offer to trade for the item
         */
        User user = PrimaryUser.getInstance();
        User owner = new User(TEST_USER_FRIEND_1, mContext);
        try {
            owner.getTradeList().clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Item ownerItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            owner.getInventory().addItem(ownerItem);
            user.getFriends().addFriend(owner);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(1, user.getFriends().size());
            assertTrue(user.getFriends().containsFriend(owner));
            assertNotNull(owner.getInventory().getItem(ownerItem.getUuid()));
        } catch (IOException e) {
            assertTrue("IOException in testOfferTradeWithFriend", Boolean.FALSE);
        }

        /**
         * Open 'browse friends inventories'
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.browseTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Create a trade using the UI
         */
        pause();
        onData(hasEntry(anything(), hasToString(ownerItem.getItemName())))
                .inAdapterView(withId(R.id.BrowseListView))
                .atPosition(0)
                .perform(click());
        pause();
        onView(withId(R.id.proposeTradeButton))
                .perform(click());
        pause();
        onView(withId(R.id.tradeComposeOffer))
                .perform(click());
        pause();

        try {
            assertEquals(1, user.getTradeList().getTrades().size());
            assertEquals(1, owner.getTradeList().getTrades().size());
            Trade trade = user.getTradeList().getTradesAsList().get(0);

            assertEquals(user.getUsername(), trade.getBorrower().getUsername());
            assertEquals(owner.getUsername(), trade.getOwner().getUsername());

            assertEquals(TradeStateOffered.class, trade.getState().getClass());

            assertEquals(1, trade.getOwnersItems().size());
            assertNotNull(trade.getOwnersItems().get(0));
            assertEquals(ownerItem.getItemName(), trade.getOwnersItems().get(0).getItemName());
        } catch (IOException e) {
            fail("IOException in testOfferTradeWithFriend");
        }
    }

    /**
     * UC1.4.2 OwnerIsNotifiedOfTradeOffer
     */
    public void testOwnerIsNotifiedOfTradeOffer() {
        /**
         * Create a borrower with an item and who also has the user as a friend
         * Then, have the borrower offer a trade with the user
         */
        User user = PrimaryUser.getInstance();
        User borrower = new User(TEST_USER_FRIEND_1, mContext);
        final Item userItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            user.getInventory().addItem(userItem);
            borrower.getFriends().addFriend(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Item> borrowerItems = new ArrayList<Item>() {{
            add(userItem);
        }};
        Trade trade = new Trade(borrower, user, borrowerItems, new ArrayList<Item>(), mContext);
        try {
            trade.offer();
        } catch (IllegalTradeStateTransition e) {
            e.printStackTrace();
        }

        /**
         * Open trade history
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Assert trade exists in trade history view
         */
        onData(hasToString(userItem.getItemName()))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()));
        onData(hasToString(user.getUsername()))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()));
        onData(hasToString(borrower.getUsername()))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()));
        onData(hasToString("offered"))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()));
    }

    /**
     * UC1.4.3 OwnerAcceptsOrDeclinesTrade main flow
     */
    public void testOwnerAcceptsTrade() {
        /**
         * Create a borrower with an item and who also has the user as a friend
         * Then, have the borrower offer a trade with the user
         */
        User user = PrimaryUser.getInstance();
        User borrower = new User(TEST_USER_FRIEND_1, mContext);
        final Item userItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            user.getInventory().addItem(userItem);
            borrower.getFriends().addFriend(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Item> borrowerItems = new ArrayList<Item>() {{
            add(userItem);
        }};
        Trade trade = new Trade(borrower, user, borrowerItems, new ArrayList<Item>(), mContext);
        try {
            trade.offer();
        } catch (IllegalTradeStateTransition e) {
            e.printStackTrace();
        }

        /**
         * Open trade history
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
            .check(matches(isDisplayed()))
            .perform(click());

        /**
         * Open trade review
         */
        onData(hasToString("offered by"))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Accept the trade
         */
        onView(withId(R.id.tradeReviewAccept))
                .perform(click());

        /**
         * Assert trade was accepted
         */
        assertEquals(TradeStateAccepted.class, trade.getState().getClass());
    }

    /**
     * UC1.4.3 OwnerAcceptsOrDeclinesTrade alternate flow
     */
    public void testOwnerDeclinesTrade() {
        /**
         * Create a borrower with an item and who also has the user as a friend
         * Then, have the borrower offer a trade with the user
         */
        User user = PrimaryUser.getInstance();
        User borrower = new User(TEST_USER_FRIEND_1, mContext);
        final Item userItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            user.getInventory().addItem(userItem);
            borrower.getFriends().addFriend(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Item> borrowerItems = new ArrayList<Item>() {{
            add(userItem);
        }};
        Trade trade = new Trade(borrower, user, borrowerItems, new ArrayList<Item>(), mContext);
        try {
            trade.offer();
        } catch (IllegalTradeStateTransition e) {
            e.printStackTrace();
        }

        /**
         * Open trade history
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Open trade review
         */
        onData(hasToString("offered by"))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Decline the trade
         */
        onView(withId(R.id.tradeReviewDecline))
                .perform(click());

        /**
         * Assert trade was declined
         */
        assertEquals(TradeStateDeclined.class, trade.getState().getClass());
    }

    /**
     * UC1.4.4 OwnerOffersCounterTrade
     */
    public void testOwnerDeclinesTradeAndOffersCounterTrade() {
        /**
         * Create a borrower with an item and who also has the user as a friend
         * Then, have the borrower offer a trade with the user
         */
        User user = PrimaryUser.getInstance();
        User borrower = new User(TEST_USER_FRIEND_1, mContext);
        final Item userItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            user.getInventory().addItem(userItem);
            borrower.getFriends().addFriend(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Item> borrowerItems = new ArrayList<Item>() {{
            add(userItem);
        }};
        Trade trade = new Trade(borrower, user, borrowerItems, new ArrayList<Item>(), mContext);
        try {
            trade.offer();
        } catch (IllegalTradeStateTransition e) {
            e.printStackTrace();
        }

        /**
         * Open trade history
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Open trade review
         */
        onData(hasToString("offered by"))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Decline the trade
         */
        onView(withId(R.id.tradeReviewDeclineAndCounterOffer))
                .perform(click());

        /**
         * Assert trade was declined
         */
        assertEquals(TradeStateDeclined.class, trade.getState().getClass());

        // TODO compose counter offer

        // TODO assert counter offer was created

        // TODO remove fail
        fail("test functionality not yet implemented");
    }

    /**
     * UC1.4.5.1 BorrowerEditsTrade
     */
    public void testBorrowerEditsTrade() {
        /**
         * Create an owner with an item, and add them as a friend
         * The user can then offer to trade for the item
         */
        User user = PrimaryUser.getInstance();
        User owner = new User(TEST_USER_FRIEND_1, mContext);
        Item ownerItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            owner.getInventory().addItem(ownerItem);
            user.getFriends().addFriend(owner);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(1, user.getFriends().size());
            assertTrue(user.getFriends().containsFriend(owner));
            assertNotNull(owner.getInventory().getItem(ownerItem.getUuid()));
        } catch (IOException e) {
            assertTrue("IOException in testOfferTradeWithFriend", Boolean.FALSE);
        }

        /**
         * Create a trade using the UI
         */
        pause();
        onData(anything())
                .inAdapterView(withId(R.id.BrowseListView))
                .atPosition(0)
                .perform(click());
        pause();
        onView(withId(R.id.proposeTradeButton))
                .perform(click());
        pause();

        /**
         * Assert trade is in editable state
         */
        try {
            assertEquals(1, user.getTradeList().getTrades().size());
            Trade trade = null;
            for (Trade t : user.getTradeList().getTrades().values()) {
                trade = t;
            }
            assertTrue(trade.isEditable());
        } catch (IOException e) {
            assertTrue("IOException in testBorrowerEditsTrade", Boolean.FALSE);
        }

        /**
         * TODO Add an item to the trade, assert it was added
         * Note: the Add Item button exists but the functionality does not work yet
         */


        /**
         * TODO Remove an item from the trade, assert it was removed
         * Note: this view/controller functionality does not exist yet
         */

        // TODO remove fail
        fail("test functionality not yet implemented");
    }

    /**
     * UC1.4.5.2 OwnerEditsCounterTrade
     */
    public void testOwnerEditsCounterTrade() {
        /**
         * Create a borrower with an item and who also has the user as a friend
         * Then, have the borrower offer a trade with the user
         */
        User user = PrimaryUser.getInstance();
        User borrower = new User(TEST_USER_FRIEND_1, mContext);
        final Item userItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            user.getInventory().addItem(userItem);
            borrower.getFriends().addFriend(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Item> borrowerItems = new ArrayList<Item>() {{
            add(userItem);
        }};
        Trade trade = new Trade(borrower, user, borrowerItems, new ArrayList<Item>(), mContext);
        try {
            trade.offer();
        } catch (IllegalTradeStateTransition e) {
            e.printStackTrace();
        }

        /**
         * Open trade history
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Open trade review
         */
        onData(hasToString("offered by"))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Decline the trade
         */
        onView(withId(R.id.tradeReviewDeclineAndCounterOffer))
                .perform(click());

        /**
         * Assert trade was declined
         */
        assertEquals(TradeStateDeclined.class, trade.getState().getClass());

        // TODO compose counter offer

        // TODO assert counter offer was created

        // TODO assert counter offer is editable

        // TODO edit counter offer

        // TODO assert counter offer updated

        // TODO remove fail
        fail("test functionality not yet implemented");
    }

    /**
     * UC1.4.6.1 BorrowerDeletesTrade
     */
    public void testBorrowerDeletesTrade() {
        /**
         * Create an owner with an item, and add them as a friend
         * The user can then offer to trade for the item
         */
        User user = PrimaryUser.getInstance();
        User owner = new User(TEST_USER_FRIEND_1, mContext);
        Item ownerItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            owner.getInventory().addItem(ownerItem);
            user.getFriends().addFriend(owner);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assertEquals(1, user.getFriends().size());
            assertTrue(user.getFriends().containsFriend(owner));
            assertNotNull(owner.getInventory().getItem(ownerItem.getUuid()));
        } catch (IOException e) {
            assertTrue("IOException in testOfferTradeWithFriend", Boolean.FALSE);
        }

        /**
         * Create a trade using the UI
         */
        pause();
        onData(anything())
                .inAdapterView(withId(R.id.BrowseListView))
                .atPosition(0)
                .perform(click());
        pause();
        onView(withId(R.id.proposeTradeButton))
                .perform(click());
        pause();
        onView(withId(R.id.tradeComposeCancel))
                .perform(click());
        pause();

        try {
            assertEquals(1, user.getTradeList().getTrades().size());
            Trade trade = null;
            for (Trade t : user.getTradeList().getTrades().values()) {
                trade = t;
            }
            assertNotNull(trade);
            assertEquals(user.getUsername(), trade.getBorrower().getUsername());
            assertEquals(owner.getUsername(), trade.getOwner().getUsername());
            assertEquals(TradeStateCancelled.class, trade.getState().getClass());
            assertEquals(1, trade.getOwnersItems().size());
            assertNotNull(trade.getOwnersItems().get(0));
            assertEquals(ownerItem.getItemName(), trade.getOwnersItems().get(0));
        } catch (IOException e) {
            assertTrue("IOException in testOfferTradeWithFriend", Boolean.FALSE);
        }
    }

    /**
     * UC1.4.6.2 OwnerDeletesCounterTrade
     */
    public void testOwnerDeletesCounterTrade() {
        /**
         * Create a borrower with an item and who also has the user as a friend
         * Then, have the borrower offer a trade with the user
         */
        User user = PrimaryUser.getInstance();
        User borrower = new User(TEST_USER_FRIEND_1, mContext);
        final Item userItem = new Item(TEST_ITEM_1_NAME, TEST_ITEM_1_CATEGORY);
        try {
            user.getInventory().addItem(userItem);
            borrower.getFriends().addFriend(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Item> borrowerItems = new ArrayList<Item>() {{
            add(userItem);
        }};
        Trade trade = new Trade(borrower, user, borrowerItems, new ArrayList<Item>(), mContext);
        try {
            trade.offer();
        } catch (IllegalTradeStateTransition e) {
            e.printStackTrace();
        }

        /**
         * Open trade history
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Open trade review
         */
        onData(hasToString("offered by"))
                .inAdapterView(withId(R.id.tradeHistoryListView))
                .check(matches(isDisplayed()))
                .perform(click());

        /**
         * Decline the trade
         */
        onView(withId(R.id.tradeReviewDeclineAndCounterOffer))
                .perform(click());

        /**
         * Assert trade was declined
         */
        assertEquals(TradeStateDeclined.class, trade.getState().getClass());

        // TODO compose counter offer

        // TODO assert counter offer was created

        // TODO cancel counter offer

        // TODO assert counter offer cancelled

        // TODO remove fail
        fail("test functionality not yet implemented");
    }

    /**
     * UC1.4.7.1 OwnerWritesTradeAcceptComments
     */
    public void testOwnerWritesTradeAcceptComments() {
        fail("test functionality not yet implemented");
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
    }

    /**
     * UC1.4.7.2 OwnerAndBorrowerReceiveTradeDetailsEmail
     */
    public void testOwnerAndBorrowerReceiveTradeDetailsEmail() {
        fail("test functionality not yet implemented");
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
    }

    /**
     * UC1.4.8 UserBrowsesTradesInvolvingThem
     */
    public void testUserBrowsesTradesInvolvingThem() {
        // TODO create trades in all possible states with the user as each role

        // TODO create trades which do not involve the user

        /**
         * Open trade history
         */
        onView(withContentDescription("Open navigation drawer")).perform(click());
        onView(withText(getActivity().getString(R.string.tradeTitle)))
                .check(matches(isDisplayed()))
                .perform(click());

        // TODO assert each user trade is in the list

        // TODO assert each non-user trade is not in the list

        // TODO remove fail
        fail("test functionality not yet implemented");
    }

    /**
     * UC1.4.1 OfferTradeWithFriend
     */
    public void testOfferTradeWithFriendWhileOffline() {
        fail("test functionality not yet implemented");
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
    }
}
