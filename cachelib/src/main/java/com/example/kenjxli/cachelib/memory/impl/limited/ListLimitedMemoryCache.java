package com.example.kenjxli.cachelib.memory.impl.limited;

import android.graphics.Bitmap;

import com.example.kenjxli.cachelib.memory.LimitedMemoryCache;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kenjxli on 2015/7/25.
 */
public abstract class ListLimitedMemoryCache extends LimitedMemoryCache {
    protected static final List<String> keys = Collections.synchronizedList(new LinkedList<String>());

    public ListLimitedMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    public boolean put(String key, Bitmap value) {
        if (!keys.contains(key)) {
            keys.add(key);
        }
        return super.put(key, value);
    }

    @Override
    public Bitmap get(String key) {
        return super.get(key);
    }

    @Override
    public Bitmap remove(String key) {
        keys.remove(key);
        return super.remove(key);
    }

    @Override
    public void clear() {
        keys.clear();
        super.clear();
    }

    @Override
    protected abstract String nextRemovedKey();
}
