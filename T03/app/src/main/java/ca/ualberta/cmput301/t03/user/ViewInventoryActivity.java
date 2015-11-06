package ca.ualberta.cmput301.t03.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.parceler.Parcels;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.inventory.BrowseInventoryFragment;
import ca.ualberta.cmput301.t03.inventory.UserInventoryFragment;

public class ViewInventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);
        User u = Parcels.unwrap(getIntent().getExtras().getParcelable("user"));
        getSupportFragmentManager().beginTransaction().add(R.id.viewInventoryFragmentContent, UserInventoryFragment.newInstance(u)).commit();

    }
}
