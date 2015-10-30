package ca.ualberta.cmput301.t03.configuration;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.configuration.ConfigurationController;

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
    public void update(Observable observable) {
        offlineModeSwitch.setChecked(model.getOfflineModeEnabled());
        downloadImagesSwitch.setChecked(model.getDownloadImagesEnabled());
    }
}
