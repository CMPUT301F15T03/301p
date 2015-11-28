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

import com.google.gson.annotations.Expose;

import org.parceler.Parcel;
import org.parceler.Transient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.photo.Photo;
import ca.ualberta.cmput301.t03.photo.PhotoGallery;

/**
 * Model for an Item, Is owned by an inventory which is owned by a user.
 */
@Parcel
public class Item implements Observer, Observable, Cloneable, Tileable {

    @Transient
    @Expose
    private PhotoGallery photoList;
    @Transient
    private Collection<Observer> observers;
    @Expose
    private String itemName;
    @Expose
    private int itemQuantity;
    @Expose
    private String itemQuality;
    @Expose
    private String itemCategory;
    @Expose
    private boolean itemIsPrivate;
    @Expose
    private String itemDescription;
    @Expose
    private UUID uuid;

    /**
     * Constructor for an item, where the required fields name and category are already known.
     * UUID is randomly chosen.
     * Mostly used for testing purposes.
     * @param name String name for item
     * @param category String category for item. This MUST be one of the pre-existing categories,
     *                 avoid using this constructor for this reason, might lead to errors unless category
     *                 is known beforehand.
     */
    public Item(String name, String category) {
        uuid = UUID.randomUUID();
        setItemName(name);
        setItemCategory(category);
        observers = new HashSet<>();
        photoList = new PhotoGallery();
        photoList.addObserver(this);

    }

    /**
     * Default constructor for an item, where fields are all empty.
     * UUID is randomly chosen.
     */
    public Item() {
        uuid = UUID.randomUUID();
        observers = new HashSet<>();
        photoList = new PhotoGallery();
        photoList.addObserver(this);
    }

    /**
     * Get the item's name.
     *
     * @return item name String
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Set the item's name.
     * Used by AddItem and EditItem controllers.
     * @param itemName item's new name
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Get the item quantity.
     *
     * @return new quantity
     */
    public int getItemQuantity() {
        return itemQuantity;
    }

    /**
     * Set the item quantity.
     * Used by AddItem and EditItem controllers.
     * @param itemQuantity new quantity
     */
    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    /**
     * Get the quality
     *
     * @return quality
     */
    public String getItemQuality() {
        return itemQuality;
    }

    /**
     * Set the quality.
     * Used by AddItem and EditItem controllers.
     * @param itemQuality quality
     */
    public void setItemQuality(String itemQuality) {
        this.itemQuality = itemQuality;
    }

    /**
     * Get category.
     *
     * @return category
     */
    public String getItemCategory() {
        return itemCategory;
    }

    /**
     * Set the category.
     * Used by AddItem and EditItem controllers.
     * @param itemCategory new category
     */
    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    /**
     * Check if item is private.
     * Used by AddItem and EditItem controllers.
     * @return true == private, false otherwise
     */
    public boolean isItemIsPrivate() {
        return itemIsPrivate;
    }

    /**
     * Set private status.
     * Used by AddItem and EditItem controllers.
     * @param itemIsPrivate new private status
     */
    public void setItemIsPrivate(boolean itemIsPrivate) {
        this.itemIsPrivate = itemIsPrivate;
    }

    /**
     * Get description.
     *
     * @return description
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Set the description
     * Used by AddItem and EditItem controllers.
     * @param itemDescription new description
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * Get the UUID.
     * Used when transitioning into AddItem and EditItem views.
     * @return uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Set the UUID.
     * Not usually used, item's uuid is initialized on item creation.
     * If used, ensure that uuid is indeed unique.
     * @param uuid new uuid
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public PhotoGallery getPhotoList() {
        photoList.removeObserver(this);
        photoList.addObserver(this);
        return photoList;
    }

    public void setPhotoList(PhotoGallery photoList) {
        this.photoList = photoList;
        photoList.addObserver(this);
    }

    public void clearPhotoList() {
        photoList.clear();
    }

    /**
     * Alias for notify observers, call this when a change has been made.
     * Used by EditItem controllers.
     */
    public void commitChanges() {
        notifyObservers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the Observer to add
     */
    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * {@inheritDoc}
     *
     * @param observer the Observer to remove
     */
    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void clearObservers() {
        observers.clear();
    }

    /**
     * {@inheritDoc}
     *
     * @param observable reference to the Observable that triggered the update()
     */
    @Override
    public void update(Observable observable) {
        notifyObservers();
        // todo this likely has something to do with one of the photos changing
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
//        Item item = (Item) super.clone();

        Item item = new Item(getItemName(), getItemCategory());
        item.setItemDescription(getItemDescription());
        item.setItemQuantity(getItemQuantity());
        item.setItemQuality(getItemQuality());

        item.photoList = (PhotoGallery) getPhotoList().clone();

        item.commitChanges();

        return item;

//        this(itemToClone.getItemName(), itemToClone.getItemCategory());
//        setItemDescription(itemToClone.getItemDescription());
//        setItemQuality(itemToClone.getItemQuality());
//        setItemQuantity(itemToClone.getItemQuantity());
//
//        PhotoGallery gallery = itemToClone.getPhotoList();
//        PhotoGallery myPhotoGallery = getPhotoList();
//        for (Photo p: gallery.getPhotos()){
//
//
//            myPhotoGallery.addPhoto(); Photo(p.getPhoto())
//        }
//
//        getPhotoList().addPhoto();

    }

    @Override
    public Map<String, ?> makeTile() {
        return null;
    }
}
