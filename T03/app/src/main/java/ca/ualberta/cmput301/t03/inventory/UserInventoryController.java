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

package ca.ualberta.cmput301.t03.inventory;

import android.content.Context;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.HttpDataManager;

/**
 * Controller for the Inventory model.
 * <p>
 * Automatically commits changes when items are added.
 */
public class UserInventoryController {
    public final static String ITEM_NAME = null;

    Inventory inventory;
    Context context;
    Configuration configuration;
    DataManager dataManager;

    /**
     * @param inventory The model Object containing the users Items
     * @param context   Activity Context
     */
    public UserInventoryController(Context context, Inventory inventory) {
        this.inventory = inventory;
        this.context = context;

        this.configuration = new Configuration(context);
        this.dataManager = new HttpDataManager(context);
    }

    /**
     * Add an Item to Inventory
     *
     * @param item User new item
     */
    public void addingItemToInventory(Item item) {
        inventory.addItem(item);
    }

}
