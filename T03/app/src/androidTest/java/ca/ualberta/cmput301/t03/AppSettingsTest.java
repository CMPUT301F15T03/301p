package ca.ualberta.cmput301.t03;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CompoundButton;

import ca.ualberta.cmput301.t03.configuration.ConfigurationActivity;

//
public class AppSettingsTest extends ActivityInstrumentationTestCase2 {

    private Boolean switchToggledState = false;

    public AppSettingsTest() {
        super(ca.ualberta.cmput301.t03.configuration.ConfigurationActivity.class);
    }

    /**
     * UC10.01.01
     */
    public void testSettingAutoDownloadModeOff() {
        final ConfigurationActivity activity = (ConfigurationActivity) getActivity();

        // check that the view will trigger a change in the model
        activity.getModel().setDownloadImages(true);

        Boolean originalValue = activity.getModel().isDownloadImagesEnabled();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.getDownloadImagesSwitch().setChecked(false);
            }
        });
        getInstrumentation().waitForIdleSync();

        Boolean newValue = activity.getModel().isDownloadImagesEnabled();
        assertFalse(newValue);
        assertTrue(originalValue);

        //check that the model will trigger an update in the view
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.getDownloadImagesSwitch().setChecked(true);
                activity.getDownloadImagesSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        switchToggledState = isChecked;
                    }
                });

                activity.getModel().setDownloadImages(false);
            }
        });
        getInstrumentation().waitForIdleSync();

        assertFalse(switchToggledState);
    }

    /**
     * UC10.01.01
     */
    public void testSettingAutoDownloadModeOn() {
        final ConfigurationActivity activity = (ConfigurationActivity) getActivity();

        // check that the view will trigger a change in the model
        activity.getModel().setDownloadImages(false);

        Boolean originalValue = activity.getModel().isDownloadImagesEnabled();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.getDownloadImagesSwitch().setChecked(true);
            }
        });
        getInstrumentation().waitForIdleSync();

        Boolean newValue = activity.getModel().isDownloadImagesEnabled();
        assertTrue(newValue);
        assertFalse(originalValue);

        //check that the model will trigger an update in the view
        activity.runOnUiThread(new Runnable() {
            public void run() {
                activity.getDownloadImagesSwitch().setChecked(false);
                activity.getDownloadImagesSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        switchToggledState = isChecked;
                    }
                });

                activity.getModel().setDownloadImages(true);
            }
        });
        getInstrumentation().waitForIdleSync();

        assertTrue(switchToggledState);

        // this will be useful testing for the photos stories as a precondition for manual downloads
//        User user = User.getInstance();
//        user.clearCache();
//        List<Item> items = user.getInventory().getItems();
//        for (Item item : items) {
//            if (item.getPhotos().size() > 0) {
//                Photo photo = item.getPhotos().getElementAt(0);
//                assertTrue(photo.isDownloaded());
//                settings.setAutoDownloadModeValue(originalValue);
//                return;
//            }
//        }
//
//        settings.setAutoDownloadModeValue(originalValue);
//        // Dev note: test user should've items with photos. Fix it and
//        // re-run the test if this fails
//        assertTrue(false);
    }
}
