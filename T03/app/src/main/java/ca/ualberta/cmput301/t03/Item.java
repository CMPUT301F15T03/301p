package ca.ualberta.cmput301.t03;

import java.util.Collection;

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public String getItemQuality() {
        return itemQuality;
    }

    public void setItemQuality(String itemQuality) {
        this.itemQuality = itemQuality;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public boolean isItemIsPrivate() {
        return itemIsPrivate;
    }

    public void setItemIsPrivate(boolean itemIsPrivate) {
        this.itemIsPrivate = itemIsPrivate;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
