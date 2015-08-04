package com.example.kenjxli.cachelib.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.kenjxli.cachelib.memory.MemoryCache;
import com.example.kenjxli.cachelib.memory.impl.LruMemoryCache;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class MemoryCacheUtil {

    private MemoryCache cache;
    private static MemoryCacheUtil util;


    private MemoryCacheUtil(MemoryCache cache) {
        this.cache = cache;
    }

    public static MemoryCacheUtil getInstance() {
        if (util == null) {
            synchronized (MemoryCacheUtil.class) {
                if (util == null) {
                    // 默认缓存方式LRU
                    int maxMemory = (int) Runtime.getRuntime().maxMemory();
                    int cacheSize = maxMemory / 8;
                    util = new MemoryCacheUtil(MemoryCacheFactory.createLruMemoryCache(cacheSize));
                }
            }
        }
        return util;
    }

    /**
     * 用户自己设置缓存方式
     *
     * @param cache
     */
    public void setCache(MemoryCache cache) {
        this.cache = cache;
    }

    public boolean addBitmapToMemoryCache(String key, Bitmap value) {
        if (getBitmapFromMemoryCache(key) == null) {
            return cache.put(key, value);
        }
        return false;
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return cache.get(key);
    }

    public Bitmap removeBitmapFromMemoryCache(String key) {
        return cache.remove(key);
    }

    public void clearBitmapsInMemoryCache() {
        cache.clear();
    }

    public void removeCache() {
        util = null;
    }

}
