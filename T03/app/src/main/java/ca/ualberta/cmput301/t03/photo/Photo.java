/*
 * Copyright (C) 2015 Kyle O'Shaughnessy, Ross Anderson, Michelle Mabuyo, John Slevinsky, Udey Rishi, Quentin Lautischer
 * Photography equipment trading application for CMPUT 301 at the University of Alberta.
 *
 * This file is part of "Trading Post"
 *
 * "Trading Post" is free software: you can redistribute it and/or modify
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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;

import com.google.gson.annotations.Expose;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import ca.ualberta.cmput301.t03.Observable;
import ca.ualberta.cmput301.t03.Observer;
import ca.ualberta.cmput301.t03.R;
import ca.ualberta.cmput301.t03.TradeApp;
import ca.ualberta.cmput301.t03.common.exceptions.ServiceNotAvailableException;
import ca.ualberta.cmput301.t03.configuration.Configuration;
import ca.ualberta.cmput301.t03.datamanager.CachedDataManager;
import ca.ualberta.cmput301.t03.datamanager.DataKey;
import ca.ualberta.cmput301.t03.datamanager.DataManager;
import ca.ualberta.cmput301.t03.datamanager.LocalDataManager;

/**
 * Object for the item's photo. Belongs to an item's PhotoGallery.
 * <p/>
 * This object is lazy in the sense that it will not fetch its photo resource unless told to
 * by downloadPhoto() or getPhoto()
 * <p/>
 * When used in conjunction with the ItemPhotoController, images are capped at 64k bytes.
 */
public class Photo implements Observable, Cloneable {

    public static final String type = "Photo";

    @Expose
    private UUID photoUUID;
    private Collection<Observer> observers;
    private DataManager dataManager;
    private LocalDataManager localDataManager;

    private Base64Wrapper base64Photo;
    private Bitmap bitmap;

    public Photo() {
        observers = new ArrayList<>();
        dataManager = TradeApp.getInstance().createDataManager(false);
        localDataManager = new LocalDataManager(false);
        base64Photo = new Base64Wrapper();
    }

    public Photo(Bitmap photo) {
        observers = new ArrayList<>();
        dataManager = TradeApp.getInstance().createDataManager(false);
        localDataManager = new LocalDataManager(false);
        photoUUID = UUID.randomUUID();
        base64Photo = new Base64Wrapper();
        setPhoto(photo);
    }

    /**
     * Determines if the photo has been lazy downloaded yet.
     * <p/>
     * Called by views that are concerned with the manual download options.
     *
     * @return true if the photo has been downloaded, false if not
     */
    public boolean isDownloaded() {
        return localDataManager.keyExists((new DataKey(Photo.type, photoUUID.toString())));
    }

    /**
     * Downloads the base65 photo info and creates a bitmap from the information
     * both entities are then cached in the photo object for getting later
     * <p/>
     * For an ImageView, call setBitmap(Photo.getPhoto()); -- i think
     */
    public void downloadPhoto(Boolean force) {
        if (!isDownloaded()) {
            Configuration config = new Configuration(TradeApp.getContext());
            if (!force && !config.isDownloadImagesEnabled()) {
                bitmap = ((BitmapDrawable) TradeApp.getContext().getResources().getDrawable(R.drawable.photo_available_for_download)).getBitmap();
            }

            else {
                try {
                    base64Photo = dataManager.getData(new DataKey(Photo.type, photoUUID.toString()), Base64Wrapper.class);
                    byte[] bytes = Base64.decode(base64Photo.getContents(), Base64.NO_WRAP);
                    bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ServiceNotAvailableException e) {
                    throw new RuntimeException("App is offline.", e);
                }
            }
            notifyObservers();
        }

        else {
            try {
                base64Photo = localDataManager.getData(
                        new DataKey(Photo.type, photoUUID.toString()),
                        Base64Wrapper.class);
                byte[] bytes = Base64.decode(base64Photo.getContents(), Base64.NO_WRAP);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Downloads the base65 photo info and creates a bitmap from the information
     * both entities are then cached in the photo object for getting later
     * <p/>
     * For an ImageView, call setBitmap(Photo.getPhoto()); -- i think
     */
    public void downloadPhoto() {
        downloadPhoto(false);
    }

    /**
     * Get the rendered photo as a Bitmap, very useful for loading into views easily.
     * <p/>
     * If the photo has not been downloaded before a lazy load will be performed.
     *
     * @return bitmap rendered photo
     */
    public Bitmap getPhoto() {
        downloadPhoto();
        return bitmap;
    }

    /**
     * Provide a new bitmap to the image, this will become the new bitmap that is lazy loaded
     * <p/>
     * This setter will call the datamanager to make a persistent copy on the network and in cache
     *
     * @param photo
     */
    public void setPhoto(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        base64Photo.setContents(Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP));
        byte[] bytes = Base64.decode(base64Photo.getContents(), Base64.NO_WRAP);
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        try {
            dataManager.writeData(new DataKey(Photo.type, photoUUID.toString()), base64Photo, Base64Wrapper.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceNotAvailableException e) {
            throw new RuntimeException("App is offline.", e);
        }
        notifyObservers();
    }

    /**
     * This will call the datamanager to delete the datakey associated with the photo on both
     * the network and in the local cache
     * <p/>
     * this is meant to be called before removing an item from inventory to prevent dirty caches
     * that persist
     */
    public void deletePhoto() {
        try {
            dataManager.deleteIfExists(new DataKey(Photo.type, photoUUID.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServiceNotAvailableException e) {
            throw new RuntimeException("App is offline.", e);
        }
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
     * Get a copy of the Base64 encoded photo, this is useful when testing the image size
     *
     * @return base64 encoded photo
     */
    public Base64Wrapper getBase64Photo() {
        return base64Photo;
    }

    /**
     * Return an object that is an exact clone of the photo, this is used in the item clone
     * workflow
     * @return cloned photo
     * @throws CloneNotSupportedException
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
//        Photo p = (Photo) super.clone();
//        p.clearObservers();
        Bitmap bitCopy= Bitmap.createBitmap(getPhoto());
        Photo p = new Photo(bitCopy);

        return p;
    }

    public UUID getPhotoUUID() {
        return photoUUID;
    }
}