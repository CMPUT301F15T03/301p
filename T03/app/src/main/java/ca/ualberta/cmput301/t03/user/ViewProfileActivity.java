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

package ca.ualberta.cmput301.t03.user;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.parceler.Parcels;

import ca.ualberta.cmput301.t03.R;


/**
 * A skeleton activity to contain the view profile fragment.
 * <p>
 * Use intent.PutExtra with a Parceled user and key "user"
 * to specify which User's profile should be viewed.
 */
public class ViewProfileActivity extends AppCompatActivity {

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        User u = Parcels.unwrap((Parcelable) extras.get("user"));


        getSupportFragmentManager().beginTransaction().add(R.id.viewProfileFragmentContent, ViewProfileFragment.newInstance(u)).commit();

    }

}
