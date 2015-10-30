/*
    Copyright (C) 2015 Kyle O'Shaughnessy
    Photography equipment trading application for CMPUT 301 at the University of Alberta.


    This file is part of {Application Name}.


    {Application Name} is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.ualberta.cmput301.t03.configuration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;

public class ConfigurationActivity extends AppCompatActivity implements Observer {

    private Configuration model;
    private ConfigurationController controller;
    private Switch offlineModeSwitch;
    private Switch downloadImagesSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = new Configuration(getApplicationContext());
        model.addObserver(this);
        controller = new ConfigurationController(model);

        offlineModeSwitch = (Switch) findViewById(R.id.offlineModeSwitch);
        downloadImagesSwitch = (Switch) findViewById(R.id.downloadImagesSwitch);

        offlineModeSwitch.setChecked(model.getOfflineModeEnabled());
        downloadImagesSwitch.setChecked(model.getDownloadImagesEnabled());

        offlineModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controller.onOfflineModeToggled(isChecked);
            }
        });
        downloadImagesSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controller.onDownloadImagesToggled(isChecked);
            }
        });
    }

    @Override
    protected void onDestroy() {
        model.removeObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable) {
        offlineModeSwitch.setChecked(model.getOfflineModeEnabled());
        downloadImagesSwitch.setChecked(model.getDownloadImagesEnabled());
    }

    public Configuration getModel() {
        return model;
    }

    public Switch getOfflineModeSwitch() {
        return offlineModeSwitch;
    }


    public Switch getDownloadImagesSwitch() {
        return downloadImagesSwitch;
    }
}
