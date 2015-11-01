package ca.ualberta.cmput301.t03;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.inventory.BrowseInventoryFragment;
import ca.ualberta.cmput301.t03.configuration.ConfigurationActivity;
import ca.ualberta.cmput301.t03.trading.TradeOfferHistoryFragment;
import ca.ualberta.cmput301.t03.user.EditProfileFragment;
import ca.ualberta.cmput301.t03.user.FriendsListFragment;
import ca.ualberta.cmput301.t03.user.InitializeUserActivity;
import ca.ualberta.cmput301.t03.user.UserInventoryFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public FragmentManager mfragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addInitialFragment();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // todo : uncomment the below when ready :D
        Configuration config = new Configuration(getApplicationContext());
        if (!config.isApplicationUserNameSet()) {
            Intent intent = new Intent(this, InitializeUserActivity.class);
            this.startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(this, ConfigurationActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        String title;
        Class fragmentClass;

        switch(item.getItemId()){
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
                fragmentClass = EditProfileFragment.class;
                title = getString(R.string.editProfileTitle);
                break;
            default:
                fragmentClass = BrowseInventoryFragment.class;
                title = getString(R.string.browseTitle);
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            mfragmentManager.beginTransaction().replace(R.id.fragmentContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addInitialFragment(){
        Fragment fragment = null;
        Class fragmentClass;

        fragmentClass = BrowseInventoryFragment.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mfragmentManager = getSupportFragmentManager();
        mfragmentManager.beginTransaction().add(R.id.fragmentContent, fragment).commit();
    }
}
