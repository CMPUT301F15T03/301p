package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;

import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.user.User;
import ca.ualberta.cmput301.t03.user.UserInventoryController;

public class AddItemView extends AppCompatActivity {
    private Item itemModel;
    private AddItemController controller;
    private Button addToInventoryButton;
    private Inventory inventoryModel;

    private FloatingActionButton fab;
    private User user;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_item_view);

        final Configuration c = new Configuration(this.getBaseContext());
        c.getApplicationUserName();


        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    user = c.getFullApplicationUser();
                    inventoryModel = user.getInventory();
                    controller = new AddItemController(findViewById(R.id.add_item_view), activity, inventoryModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        worker.start();

        addToInventoryButton = (Button) findViewById(R.id.addItem);

        addToInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.addItemToInventoryButtonClicked();
            }
        });
    }
}
