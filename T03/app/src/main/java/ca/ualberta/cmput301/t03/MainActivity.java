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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.configuration.ConfigurationActivity;
import ca.ualberta.cmput301.t03.inventory.BrowseInventoryFragment;
import ca.ualberta.cmput301.t03.inventory.UserInventoryFragment;
import ca.ualberta.cmput301.t03.trading.TradeOfferHistoryFragment;
import ca.ualberta.cmput301.t03.user.FriendsListFragment;
import ca.ualberta.cmput301.t03.user.InitializeUserActivity;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.ViewProfileFragment;

/**
 * The main view of our application.
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer {

    public FragmentManager fragmentManager;
    private TextView sidebarUsernameTextView;
    private TextView sidebarEmailTextView;

    /**
     * called when the activity is requested, used to initialize most of the view elements and
     * start the main application flow. The applications main user is asserted and set after which
     * the primary user is setup.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);
        sidebarUsernameTextView = (TextView) header.findViewById(R.id.SidebarUsernameTextView);
        sidebarEmailTextView = (TextView) header.findViewById(R.id.sidebarEmailTextView);

        final Configuration config = new Configuration(getApplicationContext());
        if (!config.isApplicationUserNameSet()) {
            Intent intent = new Intent(this, InitializeUserActivity.class);
            this.startActivityForResult(intent, 1);
        } else {
            afterUserSetup();
        }


    }

    /**
     * Callback for startActivityForResult, this will help delay the call of afterUserSetup until
     * we have setup an application username in configuration. This will get called upon exit of
     * the InitializeUserActivity intent.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            afterUserSetup();
        }
    }

    /**
     * Close the navigation menu if it is open, else, delegate back button press to base class.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * handler called to inflate the options menu.
     *
     * @param menu menu to inflate a layout into
     * @return true == success, false == failure
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Handler for options menu item clicks, User primarily to enter the configuration view of the
     * application.
     *
     * @param item the menu item that was selected
     * @return true == success, false == failure
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, ConfigurationActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Toggle between the different fragments in the navigation menu, this handler will swap out
     * the requested fragment and replace the nav bar title.
     *
     * @param item the navigation menu item that was selected
     * @return success == true, failure == false.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;
        String title;
        Class fragmentClass;

        switch (item.getItemId()) {
            case R.id.nav_inventory:
                fragmentClass = UserInventoryFragment.class;
                title = getString(R.string.inventoryTitle);
                break;
            case R.id.nav_browse:
                fragmentClass = BrowseInventoryFragment.class;
                title = getString(R.string.browseTitle);
                break;
            case R.id.nav_trades:
                fragmentClass = TradeOfferHistoryFragment.class;
                title = getString(R.string.tradeTitle);
                break;
            case R.id.nav_friends:
                fragmentClass = FriendsListFragment.class;
                title = getString(R.string.friendsTitle);
                break;
            case R.id.nav_edit_profile:
                fragmentClass = ViewProfileFragment.class;
                title = getString(R.string.myProfileLabel);
                break;
            default:
                fragmentClass = BrowseInventoryFragment.class;
                title = getString(R.string.browseTitle);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            fragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * After loading the other UI pieces load the default ui fragment
     */
    private void addInitialFragment() {
        Fragment fragment = null;
        Class fragmentClass;

        fragmentClass = BrowseInventoryFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentContent, fragment).commit();
//        setTitle(getString(R.string.browseTitle));
    }

    /**
     * After confirmation of Configuration holding a username, this method will setup and load
     * the application's User singleton.
     */
    private void afterUserSetup() {
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                PrimaryUser.setup(getApplicationContext());
                User mainUser = PrimaryUser.getInstance();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishOnCreate();
                    }
                });
                return null;
            }
        };
        task.execute();
    }

    /**
     * Load the first fragment and load the username and email fields of the nav bar header.
     */
    public void finishOnCreate() {
        addInitialFragment();
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                final String username = PrimaryUser.getInstance().getUsername();
                String emailTemp = "";
                try {
                    emailTemp = PrimaryUser.getInstance().getProfile().getEmail();
                } catch (IOException e) {
                    emailTemp = "";
                }

                final String email = emailTemp;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sidebarUsernameTextView.setText(username);
                        sidebarEmailTextView.setText(email);
                    }
                });
                try {
                    PrimaryUser.getInstance().getProfile().addObserver(MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        task.execute();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable observable) {
        try {
            final String email = PrimaryUser.getInstance().getProfile().getEmail();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sidebarEmailTextView.setText(email);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
