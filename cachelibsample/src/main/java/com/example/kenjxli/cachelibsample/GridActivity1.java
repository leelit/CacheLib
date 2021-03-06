package com.example.kenjxli.cachelibsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;


public class GridActivity1 extends Activity {

    private GridView gridView;
    private PhotoWallAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);
        gridView = (GridView) findViewById(R.id.photo_wall);
        adapter = new PhotoWallAdapter1(this, 0, ImageSoruce.images, gridView);
        gridView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter.cancelTasks();
    }


}
