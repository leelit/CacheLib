package com.example.kenjxli.cachelib.disk.wrapper;

import android.graphics.Bitmap;

import com.example.kenjxli.cachelib.disk.DiskCache;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class MaxAgeDiskCache implements DiskCache {

    private boolean isFirstTime;
    private final DiskCache diskCache;

    /**
     * 最大存活时间，单位d
     */
    private final long MAX_AGE;

    public MaxAgeDiskCache(DiskCache diskCache, int maxAge) {
        if (maxAge <= 0) {
            throw new IllegalArgumentException("maxAge shoud > 0");
        }
        this.diskCache = diskCache;
        this.MAX_AGE = maxAge * 1000 * 3600 * 24;
    }

    @Override
    public boolean put(String url, InputStream inputStream) {
        if (isFirstTime) {
            removeOldFile();
            isFirstTime = false;
        }
        return diskCache.put(url, inputStream);
    }

    @Override
    public Bitmap get(String url) {
        return diskCache.get(url);
    }

    @Override
    public void remove(String url) {
        diskCache.remove(url);
    }

    @Override
    public void clear() {
        diskCache.clear();
    }

    @Override
    public String getCachePath() {
        return diskCache.getCachePath();
    }

    @Override
    public String getFilePath(String url) {
        return diskCache.getFilePath(url);
    }

    private void removeOldFile() {
        File[] files = getSortedFile();
        if (files != null) {
            int length = files.length;
            for (int i = 0; i < length; i++) {
                File file = files[i];
                if ((System.currentTimeMillis() - file.lastModified()) > MAX_AGE) {
                    file.delete();
                } else {
                    break;
                }
            }
        }
    }

    private File[] getSortedFile() {
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
