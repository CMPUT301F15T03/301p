package ca.ualberta.cmput301.t03;

import android.test.ActivityInstrumentationTestCase2;

import java.net.UnknownServiceException;

public class AppSettingsTest extends ActivityInstrumentationTestCase2{
    public AppSettingsTest(Class activityClass) {
        super(activityClass);
    }

    private static final String EXISTING_TEST_USER_NAME = "Test User";

    /**
    * UC10.01.01
    */
    public void testSettingAutoDownloadModeOff() {
        AppSettings settings = AppSettings.getInstance();
        Boolean originalValue = settings.getAutoDownloadModeValue();
        settings.setAutoDownloadModeValue(Boolean.FALSE);

        User user = User.getExistingUser(EXISTING_TEST_USER_NAME);
        user.clearCache();
        List<Item> items = user.getInventory().getItems();
        for (Item item : items)
        {
            if (item.getPhotos().size() > 0)
            {
                Photo photo = item.getPhotos().getElementAt(0);
                assertFalse(photo.isDownloaded());
                settings.setAutoDownloadModeValue(originalValue);
                return;
            }
        }

        settings.setAutoDownloadModeValue(originalValue);
        // Dev note: test user should've items with photos. Fix it and
        // re-run the test if this fails
        assertTrue(false);
    }

    /**
    * UC10.01.01
    */
    public void testSettingAutoDownloadModeOn() {
        AppSettings settings = AppSettings.getInstance();
        Boolean originalValue = settings.getAutoDownloadModeValue();
        settings.setAutoDownloadModeValue(Boolean.TRUE);

        User user = User.getExistingUser(EXISTING_TEST_USER_NAME);
        user.clearCache();
        List<Item> items = user.getInventory().getItems();
        for (Item item : items)
        {
            if (item.getPhotos().size() > 0)
            {
                Photo photo = item.getPhotos().getElementAt(0);
                assertTrue(photo.isDownloaded());
                settings.setAutoDownloadModeValue(originalValue);
                return;
            }
        }

        settings.setAutoDownloadModeValue(originalValue);
        // Dev note: test user should've items with photos. Fix it and
        // re-run the test if this fails
        assertTrue(false);
    }
}
