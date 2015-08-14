package com.example.kenjxli.cachelib.utils;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.example.kenjxli.cachelib.disk.DiskCache;
import com.example.kenjxli.cachelib.disk.impl.limited.LruDiskCache;

import java.io.File;
import java.io.InputStream;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class DiskCacheUtil {
    private static final long CACHE_SIZE = 300 * 1024;

    private DiskCache diskCache;
    private static DiskCacheUtil util;


    private DiskCacheUtil(DiskCache diskCache) {
        this.diskCache = diskCache;
    }


    public static DiskCacheUtil getInstance(Context context, String subdir) {
        if (util == null) {
            synchronized (DiskCacheUtil.class) {
                if (util == null) {
                    DiskCache diskCache = DiskCacheFactory.createLruDiskCache(CACHE_SIZE, getDiskCacheDir(context, subdir));
                    util = new DiskCacheUtil(diskCache);
                }
            }
        }
        return util;
    }

    public void setDiskCache(DiskCache diskCache) {
        this.diskCache = diskCache;
    }


    public boolean addBitmapToDiskCache(String url, InputStream inputStream) {
        if (getBitmapFromDiskCache(url) == null) {
            return diskCache.put(url, inputStream);
        }
        return false;
    }

    public Bitmap getBitmapFromDiskCache(String url) {
        return diskCache.get(url);
    }

    public void removeBitmapFromDiskCache(String key) {
        diskCache.remove(key);
    }

    public void clearBitmapsInDiskCache() {
        diskCache.clear();
    }


    private static String getDiskCacheDir(Context context, String subdir) {
        String cacheDir;
        // 判断是否有SDK选择缓存路径
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            cacheDir = context.getExternalCacheDir().getPath();
        } else {
            cacheDir = context.getCacheDir().getPath();
        }
        return cacheDir + File.separator + subdir;
    }

    public void closeUtil() {
        util = null;
    }
}
