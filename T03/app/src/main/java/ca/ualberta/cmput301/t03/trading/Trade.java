/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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

package ca.ualberta.cmput301.t03.trading;

import android.content.Context;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.TradeApp;

import com.udeyrishi.androidelasticsearchdatamanager.DataKey;
import com.udeyrishi.androidelasticsearchdatamanager.datamanagers.DataManager;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.inventory.Inventory;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeModificationException;
import ca.ualberta.cmput301.t03.trading.exceptions.IllegalTradeStateTransition;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Model which represents a single trade.
 * <p>
 * The Trade is immutable <strong>except for</strong> the following:
 * - state
 * - borrowersItems
 * - comments
 * <p>
 * In particular, {@link Trade#getTradeUUID()} will remain constant across modifications to these
 * fields and also across all time and space.
 * <p>
 * State is managed by the Trade's tradeState ({@link TradeState}) member.
 */
public class Trade implements Observable, Comparable<Trade>, Observer {
    public final static String type = "Trade";
    @Expose
    private TradeState state;
    @Expose
    private User borrower;
    @Expose
    private User owner;
    @Expose
    private Inventory borrowersItems;
    @Expose
    private Inventory ownersItems;
    @Expose
    private UUID tradeUUID;
    @Expose
    private String comments;

    private Context context;
    private DataManager dataManager;
    private Set<Observer> observers;

    @Expose
    private Boolean hasBeenSeen;

    @Expose
    private Boolean hasBeenNotified;

    /**
     * Builds a Trade object from an existing Trade's UUID.
     * <p>
     * This does <strong>not</strong> create a new Trade. Instead, it
     * builds an object which represents the existing trade with the given UUID.
     *
     * @param tradeUUID
     * @param context
     */
    public Trade(UUID tradeUUID, Context context) throws ServiceNotAvailableException {
        this.tradeUUID = tradeUUID;
        this.context = context;
        this.dataManager = TradeApp.getInstance().createDataManager(true);
        this.observers = new HashSet<>();
        this.load();
    }

    /**
     * Creates a new Trade, gives it a unique identifier ({@link UUID}), and persists it to local
     * and remote storage. This particular trade can be fetched later using the other Trade
     * constructor.
     *
     * @param borrower       User offering to trade for the owner's single item
     * @param owner          Owner of the single item
     * @param borrowersItems Items borrower is offering for the owner's single item
     * @param ownersItems    List of owner's single item
     * @param context        The application context. Used to fetch and save data using @{link DataManager}.
     */
    public Trade(User borrower, User owner,
                 Inventory borrowersItems, Inventory ownersItems,
                 Context context)  throws ServiceNotAvailableException {
        this.borrower = new User(borrower, context);
        this.owner = new User(owner, context);
        this.borrowersItems = new Inventory();
        for (Item item : borrowersItems) {
            this.borrowersItems.addItem(item);
        }

        this.ownersItems = new Inventory();
        for (Item item : ownersItems) {
            this.ownersItems.addItem(item);
        }

        this.context = context;
        this.dataManager = TradeApp.getInstance().createDataManager(true);
        this.observers = new HashSet<>();

        this.borrowersItems.addObserver(this);
        this.ownersItems.addObserver(this);

        this.state = new TradeStateComposing();
        this.tradeUUID = UUID.randomUUID();
        this.commitChanges();
    }

    /**
     * Fetches this {@link Trade} by {@link UUID} using {@link DataManager}
     * and updates this Trade object with any new data.
     * <p>
     * Fields updated, if changed:
     * <ul>
     *     <li>state</li>
     *     <li>borrowersItems</li>
     *     <li>comments</li>
     * </ul>
     */
    private void load() throws ServiceNotAvailableException {
        DataKey key = new DataKey(Trade.type, this.getTradeUUID().toString());
        try {
            if (dataManager.keyExists(key)) {
                try {
                    Trade t = dataManager.getData(key, Trade.class);
                    this.state = t.state;
                    this.borrowersItems = t.borrowersItems;
                    this.borrowersItems.addObserver(this);
                    this.comments = t.comments;
                    this.borrower = new User(t.borrower.getUsername(), context);
                    this.owner = new User(t.owner.getUsername(), context);
                    this.ownersItems = t.ownersItems;
                    this.hasBeenNotified = t.hasBeenNotified;
                    this.hasBeenSeen = t.hasBeenSeen;
                    this.ownersItems.addObserver(this);
                    this.commitChanges();
                } catch(NullPointerException e){
                    //Trade is corrupted; did a test crash?
                    this.state = new TradeStateCancelled();
                    this.borrowersItems = new Inventory();
                    this.borrowersItems.addObserver(this);
                    this.comments = "";
                    this.borrower = new User("CORRUPTED", context);
                    this.owner = new User("CORRUPTED", context);
                    this.ownersItems =  new Inventory();
                    this.hasBeenNotified = true;
                    this.hasBeenSeen = true;
                    this.ownersItems.addObserver(this);
                    commitChanges();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load trade with UUID " + this.getTradeUUID().toString());
        } catch (ServiceNotAvailableException e) {
            throw e;
        }
    }

    /**
     * Saves this {@link Trade} by its {@link UUID}
     */
    private void save() throws ServiceNotAvailableException {
        DataKey key = new DataKey(Trade.type, this.getTradeUUID().toString());
        try {
            dataManager.writeData(key, this, Trade.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save trade with UUID " + this.getTradeUUID().toString());
        } catch (ServiceNotAvailableException e) {
            throw e;
        }
    }

    /**
     * Saves this Trade, then notifies its observers of the changes
     */
    public void commitChanges() throws ServiceNotAvailableException {
        save();
        notifyObservers();
    }

    /**
     * @return True if this Trade is closed. Returns false otherwise.
     *
     * @see {@link TradeState#isClosed}
     */
    public Boolean isClosed() throws ServiceNotAvailableException {
        return getState().isClosed();
    }

    /**
     * @return True if this Trade is pending. Returns false otherwise.
     *
     * @see {@link TradeState#isPending}
     */
    public Boolean isPending() throws ServiceNotAvailableException {
        return getState().isPending();
    }

    /**
     * @return True if this Trade is editable. Returns false otherwise.
     *
     * @see {@link TradeState#isEditable}
     */
    public Boolean isEditable() throws ServiceNotAvailableException {
        return getState().isEditable();
    }

    /**
     * @return True if this Trade is public. Returns false otherwise.
     *
     * @see {@link TradeState#isPublic}
     */
    public Boolean isPublic() throws ServiceNotAvailableException {
        return getState().isPublic();
    }

    /**
     * Gets the current state of this Trade
     *
     * @return {@link TradeState} of this Trade
     */
    public TradeState getState() throws ServiceNotAvailableException {
        this.load();
        return state;
    }

    /**
     * Sets the current state of this trade.
     * <p>
     * This is intentionally package-private.
     * It is only to be used by {@link TradeState} implementations.
     *
     * @param state TradeState to set
     */
    void setState(TradeState state) throws ServiceNotAvailableException {
        this.state = state;
        this.commitChanges();
    }

    /**
     * Gets the 'borrower' user of this trade
     *
     * @return {@link User} who is the 'borrower' of this trade
     */
    public User getBorrower() {
        return this.borrower;
    }

    /**
     * Gets the 'owner' user of this trade
     *
     * @return {@link User} who is the 'owner' of this trade
     */
    public User getOwner() {
        return this.owner;
    }

    /**
     * Gets the items which the 'borrower' {@link User} is offering in this trade
     *
     * @return List of {@link Item}s which the borrower {@link User} is offering
     */
    public Inventory getBorrowersItems() throws ServiceNotAvailableException {
        this.load();
        return this.borrowersItems;
    }

    /**
     * Sets the {@link Item}s which the 'borrower' @{link User} is offering in this trade
     *
     * To be used while composing the trade. Will throw an exception if the trade is not in an
     * editable state.
     *
     * @param newBorrowersItems List of {@link Item}s to offer
     * @throws IllegalTradeModificationException if this trade is no longer in an editable {@link TradeState}
     */
    public void setBorrowersItems(Inventory newBorrowersItems) throws IllegalTradeModificationException, ServiceNotAvailableException {
        if (!state.isEditable()) {
            String msg = String.format("Trade %s in state %s is uneditable",
                    tradeUUID.toString(), state.toString());
            throw new IllegalTradeModificationException(msg);
        }
        this.borrowersItems = newBorrowersItems;
        // clear, addall...
        this.commitChanges();
    }

    /**
     * Gets the items which the 'owner' {@link User} is offering in this trade
     *
     * @return List of {@link Item}s which the 'owner' {@link User} is offering
     */
    public Inventory getOwnersItems() {
        return this.ownersItems;
    }

    /**
     * Gets the unique trade identifier for this trade
     *
     * @return Unique trade identifier ({@link UUID})
     */
    public UUID getTradeUUID() {
        return this.tradeUUID;
    }

    /**
     * Gets the owner comments for this trade. This becomes non-null once the trade is in state
     * {@link TradeStateAccepted}
     *
     * @return The owner's comments
     */
    public String getComments() throws ServiceNotAvailableException {
        this.load();
        return this.comments;
    }

    /**
     * Sets the owner comments for this trade
     *
     * To be used after accepting a trade.
     *
     * @param comments The owner's comments
     */
    public void setComments(String comments) throws ServiceNotAvailableException {
        this.comments = comments;
        this.commitChanges();
    }

    /**
     * Offers this trade. Only a trade in state {@link TradeStateComposing} can be offered.
     *
     * @throws IllegalTradeStateTransition if this trade cannot be offered
     */
    public void offer() throws IllegalTradeStateTransition, ServiceNotAvailableException {
        getState().offer(this);
    }

    /**
     * Cancels this trade. Only a trade in state {@link TradeStateComposing} can be cancelled.
     *
     * @throws IllegalTradeStateTransition if this trade cannot be cancelled
     */
    public void cancel() throws IllegalTradeStateTransition, ServiceNotAvailableException {
        getState().cancel(this);
    }

    /**
     * Accepts this trade. Only a trade in state {@link TradeStateOffered} can be accepted.
     *
     * @throws IllegalTradeStateTransition if this trade cannot be accepted
     */
    public void accept() throws IllegalTradeStateTransition, ServiceNotAvailableException {
        getState().accept(this);
    }

    /**
     * Completes this trade. Only a trade in state {@link TradeStateAccepted} can be completed.
     *
     * @throws IllegalTradeStateTransition if this trade cannot be accepted
     */
    public void complete() throws IllegalTradeStateTransition, ServiceNotAvailableException {
        getState().complete(this);
    }

    /**
     * Declines this trade. Only a trade in state {@link TradeStateOffered} can be declined.
     *
     * @throws IllegalTradeStateTransition if this trade cannot be declined
     */
    public void decline() throws IllegalTradeStateTransition, ServiceNotAvailableException {
        getState().decline(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the Observer to add
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the Observer to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void clearObservers() {
        observers.clear();
    }

    @Override
    public String toString() {
        return this.getTradeUUID().toString();
    }

    @Override
    public int compareTo(Trade another) {
        return getTradeUUID().compareTo(another.getTradeUUID());
    }

    /**
     * Generates an email subject after a trade has been accepted
     * @return subject of email
     */
    public String getEmailSubject() {
        return "Follow up details for our Photog Trade";
    }

    /**
     * Generates a email body containing all trade information after a trade has been accepted
     *
     * The user should be able to edit this either in the email intent or a provided text area.
     * @return body of trade accept email
     */
    public String getEmailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trade offer overview:\n\n");
        sb.append("Borrower ");
        sb.append(borrower.getUsername());
        if (borrowersItems.getItems().size() > 0) {
            sb.append(" would like to trade the following: \n");
            for (Item item : borrowersItems) {
                sb.append("\t");
                sb.append(item.getItemName());
                sb.append("\n");
            }
            sb.append("\n For these items of ");
            sb.append(owner.getUsername());
            sb.append(": \n");
        }
        else {
            sb.append(" would like to accept these items of yours: \n");
        }
        for (Item item : ownersItems) {
            sb.append("\t");
            sb.append(item.getItemName());
            sb.append("\n");
        }
        sb.append("\nEnter in the space below some details on how you (");
        sb.append(owner.getUsername());
        sb.append(") would like to proceed with the transfer of these items:\n\n");

        return sb.toString();
    }

    @Override
    public void update(Observable observable) {
        notifyObservers();

    }

    public void setHasBeenNotified(Boolean hasBeenNotified) {
        this.hasBeenNotified = hasBeenNotified;
    }

    public void setHasBeenSeen(Boolean hasBeenSeen) {
        this.hasBeenSeen = hasBeenSeen;
    }


    public Boolean getHasBeenNotified() {
        return hasBeenNotified == null ? false : hasBeenNotified;
    }

    public Boolean getHasBeenSeen() {
        return hasBeenSeen == null ? false : hasBeenSeen;
    }

}
