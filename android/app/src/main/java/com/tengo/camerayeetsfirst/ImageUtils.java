package com.tengo.camerayeetsfirst;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

/**
 * Created by johnteng on 10/26/19.
 */

public class ImageUtils {

    public static byte[] getImageBytes(@Nullable final String imagePath) {
        if (imagePath == null)
            return null;

        byte[] byteArray = null;
            Bitmap bitmapImage = BitmapFactory.decodeFile(imagePath);
            int nh = (int) ( bitmapImage.getHeight() * (420.0 / bitmapImage.getWidth()) );
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 420, nh, true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byteArray = stream.toByteArray();
        return byteArray;
    }

    public static void displayImage(ImageView view, String path) {
        // Get the dimensions of the View
        int targetW = view.getWidth();
        int targetH = view.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        byte[] bytes = ImageUtils.getImageBytes(path);
        if (bytes == null)
            throw new RuntimeException("Cannot get bytes for image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        view.setImageBitmap(bitmap);
    }
}
