package ca.ualberta.cmput301.t03;

import java.util.Collection;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class PhotoGallery {
    private Collection<Observer> observers;
    private Photo photoInFocus;

    public Photo getPhotoInFocus() {
        return photoInFocus;
    }

    public void setPhotoInFocus(Photo photoInFocus) {
        this.photoInFocus = photoInFocus;
    }

    public void load() { throw new UnsupportedOperationException(); }
    public void save() { throw new UnsupportedOperationException(); }

    public void removePhoto(Photo photo) { throw new UnsupportedOperationException(); }
    public void addPhoto(Photo photo) { throw new UnsupportedOperationException(); }

}
