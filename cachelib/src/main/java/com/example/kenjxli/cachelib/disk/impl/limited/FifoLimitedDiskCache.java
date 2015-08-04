package com.example.kenjxli.cachelib.disk.impl.limited;

import com.example.kenjxli.cachelib.disk.filename.FileNameGenerator;
import com.example.kenjxli.cachelib.disk.impl.LimitedDiskCache;

import java.io.File;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class FifoLimitedDiskCache extends LimitedDiskCache {
    public FifoLimitedDiskCache(long cacheSize, String cachePath, FileNameGenerator generator) {
        super(cacheSize, cachePath, generator);
    }

    public FifoLimitedDiskCache(long cacheSize, String cachePath) {
        super(cacheSize, cachePath);
    }

    @Override
    protected String nextRemoveFilePath() {
        File firstFile = getSortedFile()[0];
        String filePath = firstFile.getAbsolutePath();
        return filePath;
    }
}
