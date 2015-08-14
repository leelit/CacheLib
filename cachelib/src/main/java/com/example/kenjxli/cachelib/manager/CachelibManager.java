package com.example.kenjxli.cachelib.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.example.kenjxli.cachelib.disk.DiskCache;
import com.example.kenjxli.cachelib.memory.MemoryCache;
import com.example.kenjxli.cachelib.utils.DiskCacheUtil;
import com.example.kenjxli.cachelib.utils.MemoryCacheUtil;
import com.example.kenjxli.cachelib.utils.logger.DownloadException;
import com.example.kenjxli.cachelib.utils.logger.Logger;
import com.example.kenjxli.cachelib.utils.logger.LoggerInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by kenjxli on 2015/7/31.
 * updated on 2015/8/5
 * 把传参改为回调
 */
public class CachelibManager {

    private int responseCode = -1;

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE;
    private static final int KEEP_ALIVE = 1;

    private static CachelibManager manager;

    private DiskCacheUtil mDiskCacheUtil;
    private MemoryCacheUtil mMemoryCacheUtil;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private BlockingQueue<Runnable> sPoolWorkQueue;
    private ExecutorService threadPool;

    private Map<String, Runnable> tasks = new HashMap<>();
    private Context mContext;

    private CachelibManager(Context context) {
        mContext = context;
        mDiskCacheUtil = DiskCacheUtil.getInstance(context, "bitmap");
        mMemoryCacheUtil = MemoryCacheUtil.getInstance();
        sPoolWorkQueue =
                new LinkedBlockingQueue<>();
        threadPool
                = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue);
    }

    public static CachelibManager getInstance(Context context) {
        if (manager == null) {
            synchronized (CachelibManager.class) {
                if (manager == null) {
                    manager = new CachelibManager(context);
                }
            }
        }
        return manager;
    }


    public void setDiskCache(DiskCache diskCache) {
        mDiskCacheUtil.setDiskCache(diskCache);
    }

    public void setMemoryCache(MemoryCache memoryCache) {
        mMemoryCacheUtil.setCache(memoryCache);
    }


    public void showImageViewWithoutCallback(String imageUrl, ImageView imageView) {
        showImageView(imageUrl, imageView);
    }

    @Deprecated
    private void showImageView(String imageUrl, ImageView imageView) {
        Bitmap bitmap = mMemoryCacheUtil.getBitmapFromMemoryCache(imageUrl);
        if (bitmap != null) {
            dispatchMessage(imageView, bitmap);
        } else {
            bitmap = mDiskCacheUtil.getBitmapFromDiskCache(imageUrl);
            if (bitmap != null) {
                mMemoryCacheUtil.addBitmapToMemoryCache(imageUrl, bitmap);
                dispatchMessage(imageView, bitmap);
            } else {
                threadPool.execute(new DownloadBitmapTask(imageUrl, imageView));
            }
        }
    }


    private void dispatchMessage(final ImageView imageView, final Bitmap bitmap) {
        if (isMainThread()) {
            Log.e("TAG", "main thread");
            imageView.setImageBitmap(bitmap);
        } else {
            Log.e("TAG", "none-main thread");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    }


    private class DownloadBitmapTask implements Runnable {
        private String imageUrl;
        private ImageView imageView;

        public DownloadBitmapTask(String imageUrl, ImageView imageView) {
            this.imageUrl = imageUrl;
            this.imageView = imageView;
            tasks.put(imageUrl, this);
        }

        @Override
        public void run() {
            InputStream inputStream = getBitmapInputStream(imageUrl);
            mDiskCacheUtil.addBitmapToDiskCache(imageUrl, inputStream);
            final Bitmap bitmap = mDiskCacheUtil.getBitmapFromDiskCache(imageUrl);
            if (bitmap != null) {
                mMemoryCacheUtil.addBitmapToMemoryCache(imageUrl, bitmap);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (imageView != null) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });
            }
        }
    }


    private InputStream getBitmapInputStream(String url) {
        URL imageUrl;
        try {
            imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            responseCode = connection.getResponseCode();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            responseCode = -1;
        }
        return null;
    }

    private boolean isMainThread() {
        return Thread.currentThread().getId() == 1;
    }

    /**
     * 当释放manager时，所有相关的资源都会被释放
     */
    public void closeUtil() {
        mMemoryCacheUtil.removeCache();
        mDiskCacheUtil.closeUtil();
        threadPool.shutdown();
        threadPool = null;
        clearTask();
        sPoolWorkQueue = null;
        manager = null;
    }

    /**
     * 增加回调方式
     */

    public interface OnBitmapDisposed {
        void onSuccess(Bitmap bitmap);

        void onError(DownloadException e);
    }

    public void getAndCacheBitmap(String imageUrl, OnBitmapDisposed callBack) {
        showImageView(imageUrl, callBack);
    }

    @Deprecated
    private void showImageView(String imageUrl, OnBitmapDisposed callBack) {
        Bitmap bitmap = mMemoryCacheUtil.getBitmapFromMemoryCache(imageUrl);
        if (bitmap != null) {
            Logger.log(mContext, LoggerInfo.GET_FROM_MEMORY, true);
            callBack.onSuccess(bitmap);
        } else {
            Logger.log(mContext, LoggerInfo.GET_FROM_MEMORY, false);
            bitmap = mDiskCacheUtil.getBitmapFromDiskCache(imageUrl);
            if (bitmap != null) {
                Logger.log(mContext, LoggerInfo.GET_FROM_DISK, true);
                mMemoryCacheUtil.addBitmapToMemoryCache(imageUrl, bitmap);
                callBack.onSuccess(bitmap);
            } else {
                Logger.log(mContext, LoggerInfo.GET_FROM_DISK, false);
                threadPool.execute(new DisposingBitmapTask(imageUrl, callBack));
            }
        }

    }

    private class DisposingBitmapTask implements Runnable {
        private String imageUrl;
        private OnBitmapDisposed callBack;

        DisposingBitmapTask(String imageUrl, OnBitmapDisposed callBack) {
            this.imageUrl = imageUrl;
            this.callBack = callBack;
            tasks.put(imageUrl, this);
        }

        @Override
        public void run() {
            InputStream inputStream = getBitmapInputStream(imageUrl);
            if (inputStream != null) {
                Logger.log(mContext, LoggerInfo.NET_DOWNLOAD, true);
            } else {
                Logger.log(mContext, LoggerInfo.NET_DOWNLOAD, false);
            }
            mDiskCacheUtil.addBitmapToDiskCache(imageUrl, inputStream);
            Bitmap bitmap = mDiskCacheUtil.getBitmapFromDiskCache(imageUrl);
            if (bitmap != null) {
                Logger.log(mContext, LoggerInfo.ADD_TO_DISK, true);
                boolean result = mMemoryCacheUtil.addBitmapToMemoryCache(imageUrl, bitmap);
                if (result) {
                    Logger.log(mContext, LoggerInfo.ADD_TO_MEMORY, true);
                } else {
                    Logger.log(mContext, LoggerInfo.ADD_TO_MEMORY, false);
                }
                callBack.onSuccess(bitmap);
            } else {
                Logger.log(mContext, LoggerInfo.ADD_TO_DISK, false);
                callBack.onError(new DownloadException(responseCode));
            }
        }
    }

    public void removeTask(String imageUrl) {
        Runnable task = tasks.remove(imageUrl);
        if (task != null) {
            sPoolWorkQueue.remove(task);
        }
    }

    public void clearTask() {
        sPoolWorkQueue.clear();
    }


}
