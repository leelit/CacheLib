package com.example.kenjxli.cachelib.utils;

import com.example.kenjxli.cachelib.memory.MemoryCache;
import com.example.kenjxli.cachelib.memory.impl.BaseMemoryCache;
import com.example.kenjxli.cachelib.memory.impl.LruMemoryCache;
import com.example.kenjxli.cachelib.memory.impl.limited.FifoLimitedMemoryCache;
import com.example.kenjxli.cachelib.memory.impl.limited.LifoLimitedMemoryCache;
import com.example.kenjxli.cachelib.memory.impl.limited.LruNoneLinkedHashMap;

/**
 * Created by kenjxli on 2015/8/3.
 */
public class MemoryCacheFactory {

    public static MemoryCache createLruMemoryCache(int maxSize) {
        return new LruMemoryCache(maxSize);
    }

    public static MemoryCache createFifoLimitedMemoryCache(int maxSize) {
        return new FifoLimitedMemoryCache(maxSize);
    }

    public static MemoryCache createLifoLimitedMemoryCache(int maxSize) {
        return new LifoLimitedMemoryCache(maxSize);
    }

    public static MemoryCache createLruNoneLinkedHashMap(int maxSize) {
        return new LruNoneLinkedHashMap(maxSize);
    }

    public static MemoryCache createBaseMemoryCache() {
        return new BaseMemoryCache();
    }
}
