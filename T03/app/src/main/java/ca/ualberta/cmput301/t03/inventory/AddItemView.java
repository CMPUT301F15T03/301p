package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ca.ualberta.cmput301.t03.R;

public class AddItemView extends AppCompatActivity {
    private Item itemModel;
    private AddItemController controller;
    private Button addToInventoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_add_item_view);

        controller = new AddItemController(this, findViewById(android.R.id.content));

        addToInventoryButton = (Button) findViewById(R.id.addItem);

        addToInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.addItemToInventoryButtonClicked();
            }
        });
    }
}
