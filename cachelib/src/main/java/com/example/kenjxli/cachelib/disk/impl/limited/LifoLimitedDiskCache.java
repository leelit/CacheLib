package com.example.kenjxli.cachelib.disk.impl.limited;

import com.example.kenjxli.cachelib.disk.filename.FileNameGenerator;
import com.example.kenjxli.cachelib.disk.impl.LimitedDiskCache;

import java.io.File;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class LifoLimitedDiskCache extends LimitedDiskCache {
    public LifoLimitedDiskCache(long cacheSize, String cachePath, FileNameGenerator generator) {
        super(cacheSize, cachePath, generator);
    }

    public LifoLimitedDiskCache(long cacheSize, String cachePath) {
        super(cacheSize, cachePath);
    }

    @Override
    protected String nextRemoveFilePath() {
        File[] files = getSortedFile();
        String filePath = files[files.length - 1].getAbsolutePath();
        return filePath;
    }
}
