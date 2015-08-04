package com.example.kenjxli.cachelib_net_sample;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by kenjxli on 2015/8/3.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "come here");
        context = getApplicationContext();
    }

    public Context getContext() {
        return context;
    }
}
