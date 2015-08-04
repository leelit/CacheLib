package com.example.kenjxli.cachelibsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.kenjxli.cachelib.utils.MemoryCacheUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by kenjxli on 2015/7/29.
 */
public class PhotoWallAdapter2 extends ArrayAdapter<String> implements AbsListView.OnScrollListener {
    private GridView mPhotoWall;
    private MemoryCacheUtil memoryCacheUtil;
    private int mfirstVisibleItem;
    private int mvisibleItemCount;
    private Set<BitmapWorkerTask> tasks;

    private boolean isFirstEnter = true;

    public PhotoWallAdapter2(Context context, int resource, String[] objects, GridView PhotoWall) {
        super(context, resource, objects);
        mPhotoWall = PhotoWall;
        memoryCacheUtil = MemoryCacheUtil.getInstance();
        tasks = new HashSet<>();
        mPhotoWall.setOnScrollListener(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String url = getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.photo_layout, null);
        } else {
            view = convertView;
        }
        final ImageView photo = (ImageView) view.findViewById(R.id.photo);
        photo.setTag(url);
        setImageView(url, photo);
        return view;

    }


    private void setImageView(String imageUrl, ImageView imageView) {
        Bitmap bitmap = memoryCacheUtil.getBitmapFromMemoryCache(imageUrl);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.empty_photo);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
            loadBitmaps(mfirstVisibleItem, mvisibleItemCount);
        } else {
            cancelTasks();
        }
    }

    public void cancelTasks() {
        if (tasks != null) {
            for (BitmapWorkerTask task : tasks) {
                task.cancel(false);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mfirstVisibleItem = firstVisibleItem;
        mvisibleItemCount = visibleItemCount;
        if (isFirstEnter && visibleItemCount > 0) {
            isFirstEnter = false;
            loadBitmaps(firstVisibleItem, visibleItemCount);
        }
    }

    private void loadBitmaps(int firstVisibleItem, int visibleItemCount) {
        for (int i = firstVisibleItem; i < firstVisibleItem + visibleItemCount; i++) {
            String imageUrl = ImageSoruce.images[i];
            Bitmap bitmap = memoryCacheUtil.getBitmapFromMemoryCache(imageUrl);
            if (bitmap != null) {
                ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            } else {
                BitmapWorkerTask task = new BitmapWorkerTask();
                tasks.add(task);
                task.execute(imageUrl);
            }
        }
    }

    private class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private String imageUrl;
        private InputStream inputStream;


        @Override
        protected Bitmap doInBackground(String... params) {
            imageUrl = params[0];
            Bitmap bitmap = downloadBitmap();
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imageView = (ImageView) mPhotoWall.findViewWithTag(imageUrl);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            tasks.remove(this);
        }

        private Bitmap downloadBitmap() {
            HttpURLConnection connection = null;
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(5 * 1000);
                connection.setConnectTimeout(5 * 1000);
                inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    memoryCacheUtil.addBitmapToMemoryCache(imageUrl, bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }

            }
            return bitmap;
        }


    }

    public void removeMemoryCache() {
        memoryCacheUtil.removeCache();
    }

}
