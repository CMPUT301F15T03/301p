package ca.ualberta.cmput301.t03.photo;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import ca.ualberta.cmput301.t03.inventory.Item;

public class ItemPhotoController {

    static int TARGET_IMAGE_SIZE_BYTES = 65535;

    private Item itemModel;

    public ItemPhotoController(Item itemModel) {
        this.itemModel = itemModel;
    }

    /**
     * Add a bitmap image (resized to a target size of 64k) to the model item's photoGallery
     * This is called by the edit item view
     * @param image
     */
    public void addPhotoToItem(Bitmap image) {
        Photo photo = new Photo(resizeImage(image, TARGET_IMAGE_SIZE_BYTES));
        itemModel.getPhotoList().addPhoto(photo);
    }

    private Bitmap resizeImage(Bitmap image, long targetSize) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        long currentImageSize = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP).length();

        while (currentImageSize > targetSize) {
            image = Bitmap.createScaledBitmap(image, image.getWidth() / 2, image.getHeight() / 2, false);
            ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
            currentImageSize = Base64.encodeToString(stream2.toByteArray(), Base64.NO_WRAP).length();
        }

        return image;

    }

}
