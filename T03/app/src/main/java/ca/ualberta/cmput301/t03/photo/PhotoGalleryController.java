package ca.ualberta.cmput301.t03.photo;

/**
 * Created by mmabuyo on 2015-10-29.
 */
public class PhotoGalleryController {
    private PhotoGallery photoGalleryModel;

    public PhotoGalleryController(PhotoGallery photoGalleryModel) {
        this.photoGalleryModel = photoGalleryModel;
    }

    public void swipeLeft() { throw new UnsupportedOperationException(); }
    public void swipeRight() { throw new UnsupportedOperationException(); }
    public void longClick() { throw new UnsupportedOperationException(); }
    public void swipeDown() { throw new UnsupportedOperationException(); }
    public void click() { throw new UnsupportedOperationException(); }
}
