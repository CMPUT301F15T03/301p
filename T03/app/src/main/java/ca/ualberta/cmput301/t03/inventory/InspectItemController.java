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

package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.parceler.Parcels;

import java.io.IOException;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.trading.TradeOfferComposeActivity;
import ca.ualberta.cmput301.t03.user.User;

/**
 * Controller for inspecting an item from another user's inventory.
 */
public class InspectItemController {
    private Item itemModel;
    private View v;
    private Activity activity;
    private Inventory inventory;
    private User owner;

    /**
     * Default constructor for InspectItemController.
     * Used to initialize an instance of the InspectItemController from a relevant View
     * on an instance of the Inventory model.
     *
     * @param v The EditItemView attached to this controller
     * @param activity Used to switch to another activity (Trade)
     * @param owner The user that owns the inventory the item belongs to.
     * @param inventory The inventory model the item belongs to.
     * @param item The item being inspected.
     */
    public InspectItemController(View v, Activity activity, User owner, Inventory inventory, Item item) {
        this.v = v;
        this.activity = activity;
        this.owner = owner;
        this.inventory = inventory;
        itemModel = item;
    }

    /**
     * Onclick listener for Propose button.
     * Starts a new TradeOfferComposeActivity and
     * passes along the borrower, owner and item details to the new activity.
     */
    public void proposeTradeButtonClicked() {
        Intent i = new Intent(activity.getBaseContext(), TradeOfferComposeActivity.class);
        // TODO ditch Parcel, pass the borrower username, owner username, and item uuid as strings
        i.putExtra("trade/compose/borrower", Parcels.wrap(PrimaryUser.getInstance()));
        i.putExtra("trade/compose/owner", Parcels.wrap(owner));
        i.putExtra("trade/compose/item", Parcels.wrap(itemModel));
        activity.startActivity(i);
    }

    /**
     * Listens for an item click.
     *
     * Called by the "clone item" view widget.
     *
     * Results in a full copy of the item being added
     * to the User's Inventory. As well, the photos will
     * be cloned
     */
    public Item cloneItem() throws IOException, ServiceNotAvailableException {

        Item newItem = null;
        try {
            newItem = (Item) itemModel.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        Inventory inv = PrimaryUser.getInstance().getInventory();
        inv.addItem(newItem);
        inv.commitChanges();

        return newItem;
    }

}
