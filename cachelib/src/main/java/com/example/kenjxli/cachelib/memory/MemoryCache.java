package com.example.kenjxli.cachelib.memory;

import android.graphics.Bitmap;

/**
 * MemoryCache Interface
 * Created by kenjxli on 2015/7/24.
 */
public interface MemoryCache {

    boolean put(String key, Bitmap value);

    Bitmap get(String key);

    Bitmap remove(String key);

    void clear();
}
