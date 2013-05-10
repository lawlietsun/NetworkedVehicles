package com.example.mediasync3;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class ImageUtils {
    public static String getCompressedImagePath(String orgImagePath,
            String storeImagePath) {
        if (orgImagePath == null) {
            return null;
        }
        Bitmap bitmap = decodeSampledBitmapFromResource(orgImagePath, 256, 256);
        String absolutePath = "";
        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(storeImagePath);
            bitmap.compress(CompressFormat.PNG, 70, fos);
            fos.flush();
            absolutePath = storeImagePath;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return absolutePath;
    }

    public static Bitmap decodeSampledBitmapFromResource(String orgImagePath,
            int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(orgImagePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(orgImagePath, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

  }