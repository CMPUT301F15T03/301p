package ca.ualberta.cmput301.t03.inventory;

import java.util.Collection;

import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.photo.PhotoGallery;
import ca.ualberta.cmput301.t03.datamanager.DataManager;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class Item {
    private PhotoGallery photoList;
    private DataManager dataManager;
    private Collection<Observer> observers;
    private String itemName;
    private int itemQuantity;
    private String itemQuality;
    private String itemCategory;
    private boolean itemIsPrivate;
    private String itemDescription;

}
