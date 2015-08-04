package com.example.kenjxli.cachelib.memory.impl;

import android.graphics.Bitmap;

import com.example.kenjxli.cachelib.memory.MemoryCache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kenjxli on 2015/7/24.
 */
public class LruMemoryCache implements MemoryCache {

    private static final int LINKED_HASHMAP_CAPACITY = 0;
    private static final float LINKED_HASHMAP_LOADFACTOR = 0.75f;

    /**
     * 当前缓存大小，注意线程同步问题
     */
    private int currentSize;

    /**
     * 最大缓存大小
     */
    private int maxSize;


    private final Map<String, Bitmap> map;

    public LruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("cache size should > 0");
        }
        this.maxSize = maxSize;
        // access order设为true，以linked hashmap实现LRU算法。
        map = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(LINKED_HASHMAP_CAPACITY, LINKED_HASHMAP_LOADFACTOR, true));
    }

    @Override
    public boolean put(String key, Bitmap value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        synchronized (LruMemoryCache.class) {
            Bitmap pre = map.put(key, value);
            if (pre != null) {
                currentSize -= sizeOf(pre);
            }
            currentSize += sizeOf(value);
            trimToSize();
        }

        return true;
    }

    @Override
    public Bitmap get(String key) {
        return map.get(key);
    }

    @Override
    public Bitmap remove(String key) {
        Bitmap bitmap = map.remove(key);
        if (bitmap != null) {
            synchronized (LruMemoryCache.class) {
                currentSize -= sizeOf(bitmap);
            }
        }
        return bitmap;
    }

    @Override
    public void clear() {
        map.clear();
        synchronized (LruMemoryCache.class) {
            currentSize = 0;
        }
    }

    // 这个方法在put方法中被调用，所以方法内部无需同步
    private void trimToSize() {
        while (true) {
            if (currentSize <= maxSize) {
                break;
            }
            Map.Entry<String, Bitmap> toEvict = map.entrySet().iterator().next(); // 取第一个元素
            if (toEvict != null) {
                String key = toEvict.getKey();
                Bitmap value = map.remove(key);
                currentSize -= sizeOf(value);
            }
        }
    }

    private int sizeOf(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
