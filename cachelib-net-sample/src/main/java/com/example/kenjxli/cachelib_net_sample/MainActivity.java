package com.example.kenjxli.cachelib_net_sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.kenjxli.cachelib.manager.CachelibManager;
import com.example.kenjxli.cachelib.utils.logger.DownloadException;


public class MainActivity extends Activity {

    private ImageView main;
    private ImageView noneMain;
    private CachelibManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main = (ImageView) findViewById(R.id.mainThread);
        noneMain = (ImageView) findViewById(R.id.noneMainThread);

        mManager = CachelibManager.getInstance(MainActivity.this);

        String imageUrl = "http://img1.imgtn.bdimg.com/it/u=3321847692,4105747841&fm=21&gp=0.jpg";
        mManager.showImageViewWithoutCallback(imageUrl, main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String imageUrl = "http://img3.3lian.com/2013/v10/4/87.jpg";
                mManager.getAndCacheBitmap(imageUrl, new CachelibManager.OnBitmapDisposed() {
                    @Override
                    public void onSuccess(final Bitmap bitmap) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                noneMain.setImageBitmap(bitmap);
                            }
                        });
                    }

                    @Override
                    public void onError(DownloadException e) {
                        Log.e("TAG", e.getResponseCode() + "");
                    }


                });
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.closeUtil();
    }
}
