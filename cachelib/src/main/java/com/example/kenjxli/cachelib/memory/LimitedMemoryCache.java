package com.example.kenjxli.cachelib.memory;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by kenjxli on 2015/7/24.
 */
public abstract class LimitedMemoryCache implements MemoryCache {

    private final int maxSize;
    private AtomicInteger currentSize = new AtomicInteger(0);

    private static final Map<String, Bitmap> strongMap = Collections.synchronizedMap(new HashMap<String, Bitmap>());

    public LimitedMemoryCache(int maxSize) {
        this.maxSize = maxSize;
    }


    @Override
    public boolean put(String key, Bitmap value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException();
        }
        Bitmap pre = strongMap.put(key, value);
        if (pre != null) {
            currentSize.getAndAdd(-sizeOf(pre));
        }
        currentSize.getAndAdd(sizeOf(value));
        synchronized (LimitedMemoryCache.class) {
            while (currentSize.get() > maxSize) {
                String nextRemoveKey = nextRemovedKey();
                remove(nextRemoveKey);
            }
        }

        return true;
    }

    @Override
    public Bitmap get(String key) {
        return strongMap.get(key);
    }

    @Override
    public Bitmap remove(String key) {
        Bitmap bitmap = strongMap.remove(key);
        if (bitmap != null) {
            currentSize.getAndAdd(-sizeOf(bitmap));
        }
        return bitmap;
    }

    @Override
    public void clear() {
        strongMap.clear();
        currentSize.set(0);
    }

    protected int sizeOf(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

    // 模板方法模式，下一个删除的key
    protected abstract String nextRemovedKey();


}
