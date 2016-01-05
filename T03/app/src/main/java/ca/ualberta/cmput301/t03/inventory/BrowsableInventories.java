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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import org.parceler.Transient;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import ca.ualberta.cmput301.t03.R;
import com.udeyrishi.androidelasticsearchdatamanager.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.filters.CollectionFilter;
import ca.ualberta.cmput301.t03.filters.Filter;
import ca.ualberta.cmput301.t03.filters.FilterCriteria;
import ca.ualberta.cmput301.t03.filters.Filterable;
import ca.ualberta.cmput301.t03.filters.item_criteria.*;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.user.FriendsList;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.UserAlreadyAddedException;
import ca.ualberta.cmput301.t03.user.UserNotFoundException;


/**
 * Model maintains a List of all of the user's friends and a list of all friends inventory items.
 * Will notify the observing user and any others registered as observers of any change.
 *
 * Used by browsable Views. Also available to anything requiring a list of all tradable items.
 */
public class BrowsableInventories extends Inventory {

}
