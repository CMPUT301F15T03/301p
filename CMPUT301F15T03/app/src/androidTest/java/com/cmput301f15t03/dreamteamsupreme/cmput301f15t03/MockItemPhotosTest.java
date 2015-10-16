package com.cmput301f15t03.dreamteamsupreme.cmput301f15t03;

/**
 * Created by mabuyo on 2015-10-08.
 */

public class MockItemPhotosTest extends ActivityInstrumentationTestCase2 {

    private static final String EXISTING_TEST_USER_NAME = "Test User";

    public MockItemPhotosTest() {
        super(MainActivity.class);
    }

    // for UC06.01.01 AttachPhotographsToItems
    public void testAttachPhotographsToItems() {
        Item item = new Item("Test Item");
        Photo photo1 = new Photo("img/1");
        Photo photo2 = new Photo("img/2");
        Photo photo3 = new Photo("img/3");
        Photo photo4 = new Photo("img/4");
        Photo photo5 = new Photo("img/5");
        Photo photo6 = new Photo("img/6");
        item.addPhoto(photo1);

        // photo should have be added to item's photos
        assertTrue(item.getPhotos().contains(photo1));

        // adding more photos to the item
        item.addPhoto(photo2);
        item.addPhoto(photo3);

        assertEquals(item.getPhotos().size, 3);

        // photo size > file size requirement should not get added
        try {
            Photo largePhoto = new Photo("img/7");
            item.addPhoto(largePhoto);
        } catch (OversizedPhotoException e) {
            assertTrue(true);
        }

        // cannot go over maximum number of photos to an item
        item.addPhoto(photo4);
        item.addPhoto(photo5);

        // so adding a 6th picture should cause an error
        try {
            item.addPhoto(photo6);
        } catch (MaxPhotosLimitException e) {
            assertTrue(true);
        }
    }

    // for UC06.02.01 ViewItemPhotographs
    public void testViewItemPhotographs() {
        // add photograph
        Item item = new Item("Test Item");
        Photo photo1 = new Photo("img/1");
        Photo photo2 = new Photo("img/2");
        Photo photo3 = new Photo("img/3");
        item.add(photo1);

        // someone wants to view the photos of the item, they clicked photo1
        // (the only existing photo of the item right now), so not really in gallery view yet
        item.viewPhotos(photo1);

        // isInFocus assumes that the first photo in the list is in focus / full screen
        assertTrue(photo1.isInFocus());

        // now add more photos
        item.addPhoto(photo2);
        item.addPhoto(photo3);

        // nextPhoto and previousPhoto methods are equivalent to "swiping" through the gallery
        item.viewPhotos(photo1);
        item.nextPhoto();
        assertTrue(photo2.isInFocus());
        item.nextPhoto();
        assertTrue(photo3.isInFocus());
        item.nextPhoto();
        assertTrue(photo1.isInFocus());
    }

    // for UC06.03.01 DeleteAttachedPhotographs
    public void testDeleteAttachedPhotographs() {
        Item item = new Item("Test Item");
        Photo photo1 = new Photo("img/1");
        Photo photo2 = new Photo("img/2");
        Photo photo3 = new Photo("img/3");

        // add photographs
        item.addPhoto(photo1);
        item.addPhoto(photo2);
        item.addPhoto(photo3);

        // delete the photographs
        item.removePhoto(photo1);

        // check to make sure photo is not in the item's photographs anymore
        assertFalse(item.getPhotos().contains(photo1));

        item.removePhoto(photo2);
        item.removePhoto(photo3);

        // all photos should have been removed
        assertEquals(0, item.getPhotos().size());
    }

    // for UC09.01.01 CreateItemsOffline
    public void testCreateItemsOffline() {
        // make connectivity offline
        // future reference: http://stackoverflow.com/questions/12535101/how-can-i-turn-off-3g-data-programmatically-on-android/12535246#12535246
        ElasticSearchServer server = new ElasticSearchServer();
        User owner = new User("UserName");
        sever.addUser(owner);
        Inventory inv = new Inventory();

        NetworkManager.setDeviceOffline(); // set device offline
        assertTrue(NetworkManager.deviceIsOffline()); // make sure the device is offline

        owner.setInventory(inv);

        Item tempItem = new Item();
        tempItem.setName("50mm Cannon Lens");
        tempItem.setQualilty("Good Condition");
        tempItem.setCategory("lenses");
        tempItem.setPrivate();
        tempItem.addComment("This is my cherished cannon lens!!");
        assertFalse(inv.contains(tempItem)); // inventory should not have the item yet

        inv.addItem(tempItem);

        assertTrue(inv.contains(tempItem));
        assertNull(server.getUser("UserName").getItem("50mm Cannon Lens"), tempItem); // server does not have the item

		NetworkManager.setDeviceOnline();
		asssertTrue(NetworkManager.deviceIsOnline());

        assertEquals(server.getUser("UserName").getItem("50mm Cannon Lens"), tempItem); // server has the item

    }

    /**
    * UC06.05.01
    */
    public void testManualDownloadItemPhotoWhenAutoDownloadDisabled() {
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

                photo.downloadPhoto();
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