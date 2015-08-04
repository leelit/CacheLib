package com.example.kenjxli.cachelib.disk.impl.limited;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.kenjxli.cachelib.disk.filename.FileNameGenerator;
import com.example.kenjxli.cachelib.disk.impl.LimitedDiskCache;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class LruDiskCache extends LimitedDiskCache {


    public LruDiskCache(long cacheSize, String cachePath, FileNameGenerator generator) {
        super(cacheSize, cachePath, generator);
    }

    public LruDiskCache(long cacheSize, String cachePath) {
        super(cacheSize, cachePath);
    }


    @Override
    public Bitmap get(String url) {
        Bitmap result = super.get(url);
        if (result != null) {
            File file = new File(getFilePath(url));
            file.setLastModified(System.currentTimeMillis());
        }
        return result;
    }


    @Override
    protected String nextRemoveFilePath() {
        File leastUsedFile = getSortedFile()[0];
        String filePath = leastUsedFile.getAbsolutePath();
        return filePath;
    }

}
