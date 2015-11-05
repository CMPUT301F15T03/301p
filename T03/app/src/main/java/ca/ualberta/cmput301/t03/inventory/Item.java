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
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.photo.PhotoGallery;

/**
 * Created by mmabuyo on 2015-10-29.
 */
@Parcel
public class Item  implements Observer, Observable{
    @Transient
    private PhotoGallery photoList; // we will expose this later
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


    public Item(String name, String category) {
        uuid = UUID.randomUUID();
        setItemName(name);
        setItemCategory(category);
        observers = new HashSet<>();
    }

    public Item() {
        uuid = UUID.randomUUID();
        observers = new HashSet<>();
    }

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


    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void commitChanges() {
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void update(Observable observable) {
        notifyObservers();
        // todo this likely has something to do with one of the photos changing
    }
}
