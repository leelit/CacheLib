package com.example.kenjxli.cachelib.memory.impl.limited;

import android.graphics.Bitmap;

import com.example.kenjxli.cachelib.memory.LimitedMemoryCache;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kenjxli on 2015/7/27.
 */
public class LruNoneLinkedHashMap extends LimitedMemoryCache {

    private static final List<String> keys = Collections.synchronizedList(new LinkedList<String>());

    public LruNoneLinkedHashMap(int maxSize) {
        super(maxSize);
    }


    @Override
    public boolean put(String key, Bitmap value) {
        if (keys.contains(key)) {
            keys.remove(key);
        }
        keys.add(key); // 不包含时生成新的
        return super.put(key, value);
    }

    @Override
    public Bitmap get(String key) {
        if (keys.contains(key)) {
            keys.remove(key);
            keys.add(key); // 包含时才生成新的
        }
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
    protected String nextRemovedKey() {
        return keys.get(0); // first key
    }


}
