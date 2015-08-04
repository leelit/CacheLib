package com.example.kenjxli.cachelib.disk;

import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * Created by kenjxli on 2015/7/28.
 */
public interface DiskCache {

    boolean put(String url, InputStream inputStream);

    Bitmap get(String url);

    void remove(String url);

    void clear();

    String getCachePath();

    String getFilePath(String url);

}
