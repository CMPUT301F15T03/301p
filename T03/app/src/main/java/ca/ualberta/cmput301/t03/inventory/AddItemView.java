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

package ca.ualberta.cmput301.t03.inventory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;

import ca.ualberta.cmput301.t03.PrimaryUser;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.user.User;

/**
 * View which displays an interface to add an {@link Item} to a {@link User}'s {@link Inventory}.
 */
public class AddItemView extends AppCompatActivity {
    Spinner categoriesSpinner;
    private Item itemModel;
    private AddItemController controller;
    private Button addToInventoryButton;
    private Inventory inventoryModel;
    private User user;
    private Activity activity = this;

    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = PrimaryUser.getInstance();

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    inventoryModel = user.getInventory();
                    controller = new AddItemController(findViewById(R.id.add_item_view), activity, inventoryModel);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        worker.start();

        // Source, accessed Nov 3, 2015
        // http://developer.android.com/guide/topics/ui/controls/spinner.html#Populate
        categoriesSpinner = (Spinner) findViewById(R.id.itemCategory);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categoriesSpinner.setAdapter(adapter);

        addToInventoryButton = (Button) findViewById(R.id.addItem);
        addToInventoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.addItemToInventory();
            }
        });
    }

}
