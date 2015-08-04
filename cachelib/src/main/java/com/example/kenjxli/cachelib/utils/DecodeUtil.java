package com.example.kenjxli.cachelib.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by kenjxli on 2015/7/17.
 */
@Deprecated
public class DecodeUtil {


    /**
     * @param inputStream 图片输入流
     * @param reqWidth    实际宽度
     * @param reqHeigth   实际高度
     * @return
     */
    public static Bitmap compressBitmapToMemory(InputStream inputStream, int reqWidth, int reqHeigth) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        byte[] bytes = null;
        try {
            bytes = readStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeigth);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    private static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[10 * 1024 * 1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    /**
     * @return 图片压缩比例
     */
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * @param res
     * @param resId     图片资源ID
     * @param reqWidth  实际宽度
     * @param reqHeight 实际高度
     * @return
     */
    public static Bitmap compressBitmapToMemory(Resources res, int resId,
                                                int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


}
