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

package ca.ualberta.cmput301.t03.trading;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.Item;
import ca.ualberta.cmput301.t03.user.User;

public class TradeOfferComposeActivity extends AppCompatActivity {

    private Trade model;
    private TradeOfferComposeController controller;

    private TextView ownerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_offer_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ownerUsername = (TextView) findViewById(R.id.tradeComposeOtherUser);

        AsyncTask worker = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Context c = (Context) params[0];
                model = new Trade(Parcels.<User>unwrap(getIntent().getParcelableExtra("trade/compose/borrower")),
                        Parcels.<User>unwrap(getIntent().getParcelableExtra("trade/compose/owner")),
                        new ArrayList<Item>(),
                        new ArrayList<Item>() {{
                            add(Parcels.<Item>unwrap(getIntent().getParcelableExtra("trade/compose/item")));
                        }},
                        c);
                controller = new TradeOfferComposeController(c, model);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ownerUsername.setText(model.getOwner().getUsername());
                    }
                });
            }
        };
        worker.execute(getBaseContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trade_offer_compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
