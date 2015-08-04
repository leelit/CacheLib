package com.example.kenjxli.cachelib.memory.impl;

import android.graphics.Bitmap;

import com.example.kenjxli.cachelib.memory.MemoryCache;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base memory cache provides base function
 * Created by kenjxli on 2015/7/24.
 */
public class BaseMemoryCache implements MemoryCache {

    /**
     * 同步HashMap预防线程安全问题，软引用实现缓存
     */
    private final Map<String, SoftReference<Bitmap>> softMap = Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());

    @Override
    public boolean put(String key, Bitmap value) {
        softMap.put(key, new SoftReference<>(value));
        return true;
    }

    @Override
    public Bitmap get(String key) {
        SoftReference<Bitmap> temp = softMap.get(key);
        if (temp != null) {
            return temp.get();
        }
        return null;
    }

    @Override
    public Bitmap remove(String key) {
        SoftReference<Bitmap> pre = softMap.remove(key);
        if (pre != null) {
            return pre.get();
        }
        return null;
    }

    @Override
    public void clear() {
        softMap.clear();
    }

}
