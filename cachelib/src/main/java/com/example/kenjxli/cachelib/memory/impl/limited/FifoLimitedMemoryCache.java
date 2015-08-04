package com.example.kenjxli.cachelib.memory.impl.limited;

/**
 * Created by kenjxli on 2015/7/24.
 */
public class FifoLimitedMemoryCache extends ListLimitedMemoryCache {


    public FifoLimitedMemoryCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected String nextRemovedKey() {
        return keys.get(0);
    }


}
