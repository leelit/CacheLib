package com.example.kenjxli.cachelib.memory.wrapper;

import android.graphics.Bitmap;

import com.example.kenjxli.cachelib.memory.MemoryCache;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by kenjxli on 2015/7/24.
 */
public class MaxAgeMemoryCache implements MemoryCache {

    private final MemoryCache cache;

    /**
     * 对象最长存活时间，单位s
     */
    private final long maxAge;

    private static final Map<String, Long> loadingTiming = Collections.synchronizedMap(new HashMap<String, Long>());

    public MaxAgeMemoryCache(MemoryCache cache, long maxAge) {
        this.cache = cache;
        this.maxAge = maxAge * 1000;
    }

    @Override
    public boolean put(String key, Bitmap value) {
        boolean result = cache.put(key, value);
        if (result) {
            loadingTiming.put(key, System.currentTimeMillis());
            synchronized (MaxAgeMemoryCache.class) {
                removeExpiredKey();
            }
        }
        return result;
    }

    @Override
    public Bitmap get(String key) {
        Bitmap result = cache.get(key);
        if (result != null) {
            loadingTiming.put(key, System.currentTimeMillis());
        }
        return result;
    }

    @Override
    public Bitmap remove(String key) {
        loadingTiming.remove(key);
        return cache.remove(key);
    }

    @Override
    public void clear() {
        loadingTiming.clear();
        cache.clear();
    }


    private void removeExpiredKey() {
        List<Map.Entry<String, Long>> list = getSortedKey();
        String firstKey = list.get(0).getKey();
        while (isExpired(firstKey)) {
            loadingTiming.remove(firstKey);
            cache.remove(firstKey);
            firstKey = list.get(0).getKey();
        }
    }

    private boolean isExpired(String key) {
        if (loadingTiming.containsKey(key)) {
            long lastTime = loadingTiming.get(key);
            long now = System.currentTimeMillis();
            if (now - lastTime > maxAge) {
                return true;
            }
        }
        return false;
    }


    /**
     * @return key时间排好序的List
     */
    private List<Map.Entry<String, Long>> getSortedKey() {
        List<Map.Entry<String, Long>> list = new LinkedList<>(loadingTiming.entrySet());
        Collections.sort(list, new LoadingTimingComparator());
        return list;
    }

    private class LoadingTimingComparator implements Comparator<Map.Entry<String, Long>> {

        @Override
        public int compare(Map.Entry<String, Long> lhs, Map.Entry<String, Long> rhs) {
            return (int) (lhs.getValue() - rhs.getValue());
        }
    }


}
