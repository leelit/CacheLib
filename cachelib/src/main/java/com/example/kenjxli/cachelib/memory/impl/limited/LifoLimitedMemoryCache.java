package com.example.kenjxli.cachelib.memory.impl.limited;

/**
 * Created by kenjxli on 2015/7/24.
 */
public class LifoLimitedMemoryCache extends ListLimitedMemoryCache {

    public LifoLimitedMemoryCache(int maxSize) {
        super(maxSize);
    }


    @Override
    protected String nextRemovedKey() {
        return keys.get(keys.size() - 1);
    }

}