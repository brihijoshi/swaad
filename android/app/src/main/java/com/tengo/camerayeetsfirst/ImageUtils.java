package com.tengo.camerayeetsfirst;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
}
