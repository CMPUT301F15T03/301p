package ca.ualberta.cmput301.t03.configuration;

import ca.ualberta.cmput301.t03.configuration.Configuration;

/**
 * Created by kyleoshaughnessy on 2015-10-29.
 */
public class ConfigurationController {
    private Configuration model;

    public ConfigurationController(Configuration model) {
        this.model = model;
    }

    public void onOfflineModeToggled(Boolean switchState) {
        model.setDownloadImagesEnabled(switchState);
    }

    public void onDownloadImagesToggled(Boolean switchState) {
        model.setDownloadImagesEnabled(switchState);
    }
}
