package ca.ualberta.cmput301.t03.configuration;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = new Configuration();
        model.addObserver(this);
        controller = new ConfigurationController(model);
        // TODO : get the view elements for switches and assign them the the Switch members
    }

    @Override
    public void update(Observable observable) {
        throw new UnsupportedOperationException();
    }
}
