package com.example.kenjxli.cachelib.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.kenjxli.cachelib.disk.DiskCache;
import com.example.kenjxli.cachelib.memory.MemoryCache;
import com.example.kenjxli.cachelib.utils.DiskCacheUtil;
import com.example.kenjxli.cachelib.utils.MemoryCacheUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kenjxli on 2015/7/31.
 */
public class CachelibManager {

    private ImageView imageView;
    private DiskCacheUtil mDiskCacheUtil;
    private MemoryCacheUtil mMemoryCacheUtil;
    private static CachelibManager manager;

    private CachelibManager(Context context) {
        mDiskCacheUtil = DiskCacheUtil.getInstance(context, "bitmap");
        mMemoryCacheUtil = MemoryCacheUtil.getInstance();
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

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = msg.getData().getParcelable("bitmap");
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    };

    public void setDiskCache(DiskCache diskCache) {
        mDiskCacheUtil.setDiskCache(diskCache);
    }

    public void setMemoryCache(MemoryCache memoryCache) {
        mMemoryCacheUtil.setCache(memoryCache);
    }

    public synchronized void showImageView(String imageUrl, ImageView imageView) {
        this.imageView = imageView;
        Bitmap bitmap = mMemoryCacheUtil.getBitmapFromMemoryCache(imageUrl);
        if (bitmap != null) {
            dispatchMessage(imageView, bitmap);
        } else {
            bitmap = mDiskCacheUtil.getBitmapFromDiskCache(imageUrl);
            if (bitmap != null) {
                mMemoryCacheUtil.addBitmapToMemoryCache(imageUrl, bitmap);
                dispatchMessage(imageView, bitmap);
            } else {
                new DownloadBitmapTask(imageView).execute(imageUrl);
            }
        }
    }


    private void dispatchMessage(ImageView imageView, Bitmap bitmap) {
        if (isMainThread()) {
            Log.e("TAG", "MainThread");
            imageView.setImageBitmap(bitmap);
        } else {
            Log.e("TAG", "noneMainThread");
            sendMessage(bitmap);
        }
    }

    private void sendMessage(Bitmap bitmap) {
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", bitmap);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    private class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView imageView;

        public DownloadBitmapTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            InputStream inputStream = getBitmapInputStream(imageUrl);
            mDiskCacheUtil.addBitmapToDiskCache(imageUrl, inputStream);
            Bitmap bitmap = mDiskCacheUtil.getBitmapFromDiskCache(imageUrl);
            if (bitmap != null) {
                mMemoryCacheUtil.addBitmapToMemoryCache(imageUrl, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
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
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isMainThread() {
        return Thread.currentThread().getId() == 1;
    }

    public void closeUtil() {
        mMemoryCacheUtil.removeCache();
        mDiskCacheUtil.closeUtil();
        manager = null;
    }

}
