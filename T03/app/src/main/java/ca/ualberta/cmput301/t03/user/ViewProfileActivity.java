package ca.ualberta.cmput301.t03.user;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import org.parceler.Parcels;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.BrowseInventoryFragment;

public class ViewProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        User u = Parcels.unwrap((Parcelable)extras.get("user"));



        getSupportFragmentManager().beginTransaction().add(android.R.id.content, ViewProfileFragment.newInstance(u)).commit();

    }


//    private void addFragment(){
//        Fragment fragment = null;
//        FragmentManager fragmentManager;
//        Class fragmentClass;
//
//        fragmentClass = ViewProfileFragment.class;
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().add(R.id., fragment).commit();
//        setTitle(getString(R.string.browseTitle));
//    }

}
