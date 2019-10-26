package com.tengo.camerayeetsfirst;

import android.support.annotation.Nullable;

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
        byte[] buf = null;
        try {
            final InputStream in = new FileInputStream(new File(imagePath));
            buf = new byte[in.available()];
            while (in.read(buf) != -1) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }
}
