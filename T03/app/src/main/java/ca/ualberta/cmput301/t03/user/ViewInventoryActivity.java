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

package ca.ualberta.cmput301.t03.user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.UserInventoryFragment;

/**
 * A skeleton activity to contain the view inventory fragment.
 * <p>
 * Use intent.PutExtra with a Parceled user and key "user"
 * to specify which User's inventory should be viewed.
 */
public class ViewInventoryActivity extends AppCompatActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        User u = Parcels.unwrap(getIntent().getExtras().getParcelable("user"));
        getSupportFragmentManager().beginTransaction().add(R.id.viewInventoryFragmentContent, UserInventoryFragment.newInstance(u)).commit();

    }
}
