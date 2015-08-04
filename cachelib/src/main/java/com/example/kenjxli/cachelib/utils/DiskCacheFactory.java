package com.example.kenjxli.cachelib.utils;

import com.example.kenjxli.cachelib.disk.DiskCache;
import com.example.kenjxli.cachelib.disk.filename.FileNameGenerator;
import com.example.kenjxli.cachelib.disk.impl.BaseDiskCache;
import com.example.kenjxli.cachelib.disk.impl.limited.FifoLimitedDiskCache;
import com.example.kenjxli.cachelib.disk.impl.limited.LifoLimitedDiskCache;
import com.example.kenjxli.cachelib.disk.impl.limited.LruDiskCache;

/**
 * Created by kenjxli on 2015/8/3.
 */
public class DiskCacheFactory {
    public static DiskCache createLruDiskCache(long cacheSize, String cachePath, FileNameGenerator generator) {
        return new LruDiskCache(cacheSize, cachePath, generator);
    }

    public static DiskCache createLruDiskCache(long cacheSize, String cachePath) {
        return new LruDiskCache(cacheSize, cachePath);
    }

    public static DiskCache createFifoLimitedDiskCache(long cacheSize, String cachePath, FileNameGenerator generator) {
        return new FifoLimitedDiskCache(cacheSize, cachePath, generator);
    }

    public static DiskCache createFifoLimitedDiskCache(long cacheSize, String cachePath) {
        return new FifoLimitedDiskCache(cacheSize, cachePath);
    }

    public static DiskCache createLifoLimitedDiskCache(long cacheSize, String cachePath, FileNameGenerator generator) {
        return new LifoLimitedDiskCache(cacheSize, cachePath, generator);
    }


    public static DiskCache createLifoLimitedDiskCache(long cacheSize, String cachePath) {
        return new LifoLimitedDiskCache(cacheSize, cachePath);
    }

    public static DiskCache createBaseDiskCache(String cachePath, FileNameGenerator generator) {
        return new BaseDiskCache(cachePath, generator);
    }
}
