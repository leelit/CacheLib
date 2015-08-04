package com.example.kenjxli.cachelib_net_sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.kenjxli.cachelib.manager.CachelibManager;


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
        int a = 1;
        a = 2;

        String imageUrl = "http://img1.imgtn.bdimg.com/it/u=3321847692,4105747841&fm=21&gp=0.jpg";
        mManager.showImageView(imageUrl, main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String imageUrl = "http://img3.3lian.com/2013/v10/4/87.jpg";
                mManager.showImageView(imageUrl, noneMain);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mManager.closeUtil();
    }
}
