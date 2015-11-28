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

package ca.ualberta.cmput301.t03.photo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collection;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;

/**
 * class PhotoGallery encapsulates a collection of {@link Photo}s
 * <p/>
 * This model is used often in the application in such place as EditItemView, AddItemView,
 * InspectItemView, as well as the inventory browse fragments.
 */
public class PhotoGallery implements Observable, Observer, Cloneable {

    private Collection<Observer> observers;
    @Expose
    private ArrayList<Photo> photos;

    public PhotoGallery() {
        observers = new ArrayList<>();
        photos = new ArrayList<>();
    }

    /**
     * Retrieves the list of photos for the item.
     *
     * @return Array list of item's photos.
     */
    public ArrayList<Photo> getPhotos() {
        for (Photo photo : photos) {
            photo.removeObserver(this);
            photo.addObserver(this);
        }
        return photos;
    }

    /**
     * Sets the given array list of photos to be the item's photos.
     *
     * @param photos
     */
    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
        for (Photo photo : photos) {
            photo.addObserver(this);
        }
    }

    /**
     * Removes the photo from the item's photo gallery.
     * <p/>
     * This should be called to ensure that all copies of the photos in local cache and on the
     * network are removed
     *
     * @param photo
     */
    public void removePhoto(Photo photo) {
        photos.remove(photo);
        photo.deletePhoto();
        notifyObservers();
    }

    /**
     * Adds the photo to the item's photo gallery.
     * <p/>
     * This should be called by the ItemPhotoController only.
     *
     * @param photo
     */
    public void addPhoto(Photo photo) {
        photos.add(photo);
        photo.addObserver(this);
        notifyObservers();
    }

    /**
     * Remove all photos from the photoList and delete there data on the network and local cache
     * <p/>
     * Useful in test setups and teardowns.
     */
    public void clear() {
        for (Photo photo : photos) {
            photo.deletePhoto();
        }
        photos.clear();
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

    /**
     * {@inheritDoc}
     */
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
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
//        PhotoGallery newGallery = (PhotoGallery) super.clone();
        PhotoGallery newGallery = new PhotoGallery();

        for (Photo p: getPhotos()){
            newGallery.addPhoto((Photo) p.clone());
        }
        return newGallery;
    }
}
