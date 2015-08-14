package com.example.kenjxli.cachelib.disk.impl;

import android.graphics.Bitmap;

import com.example.kenjxli.cachelib.disk.filename.FileNameGenerator;
import com.example.kenjxli.cachelib.memory.LimitedMemoryCache;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;


/**
 * Created by kenjxli on 2015/7/28.
 */
public abstract class LimitedDiskCache extends BaseDiskCache {

    /**
     * 缓存大小
     */
    private final long CACHE_SIZE;


    public LimitedDiskCache(long cacheSize, String cachePath, FileNameGenerator generator) {
        super(cachePath, generator);
        this.CACHE_SIZE = cacheSize;
    }

    public LimitedDiskCache(long cacheSize, String cachePath) {
        super(cachePath);
        this.CACHE_SIZE = cacheSize;
    }


    @Override
    public boolean put(String url, InputStream inputStream) {
        boolean result = super.put(url, inputStream);
        if (result) {
            synchronized (LimitedMemoryCache.class) {
                while (getCurrentSize() > CACHE_SIZE) {
                    String removeFilePath = nextRemoveFilePath();
                    File deleteFile = new File(removeFilePath);
                    deleteFile.delete();
                }

            }
        }
        return result;
    }

    @Override
    public Bitmap get(String url) {
        return super.get(url);
    }

    @Override
    public void remove(String url) {
        super.remove(url);
    }

    @Override
    public void clear() {
        super.clear();
    }

    protected abstract String nextRemoveFilePath();

    private long getCurrentSize() {
        long result = 0;
        File file = new File(getCachePath());
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File eachFile : files) {
                    result += eachFile.length();
                }
            }
        }
        return result;
    }

    protected File[] getSortedFile() {
        File file = new File(getCachePath());
        if (file.exists()) {
            File[] files = file.listFiles();
            Arrays.sort(files, new SortedFileComparator());
            return files;
        }
        return null;
    }

    private class SortedFileComparator implements Comparator<File> {

        @Override
        public int compare(File lhs, File rhs) {
            return (int) (lhs.lastModified() - rhs.lastModified());
        }
    }
}
