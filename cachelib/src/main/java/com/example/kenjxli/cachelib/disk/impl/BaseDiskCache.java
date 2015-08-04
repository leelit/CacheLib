package com.example.kenjxli.cachelib.disk.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.kenjxli.cachelib.disk.DiskCache;
import com.example.kenjxli.cachelib.disk.filename.FileNameGenerator;
import com.example.kenjxli.cachelib.disk.filename.HashNameGenerator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class BaseDiskCache implements DiskCache {


    private String cachePath;
    private FileNameGenerator generator;


    public BaseDiskCache(String cachePath, FileNameGenerator generator) {
        this.cachePath = cachePath;
        this.generator = generator;
    }

    public BaseDiskCache(String cachePath) {
        this(cachePath, new HashNameGenerator());
    }

    @Override
    public boolean put(String url, InputStream inputStream) {
        BufferedInputStream in = new BufferedInputStream(inputStream);
        BufferedOutputStream out = null;
        try {
            String filePath = getFilePath(url);
            out = new BufferedOutputStream(new FileOutputStream(filePath));
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TAG", e.toString());

        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public Bitmap get(String url) {
        String filePath = getFilePath(url);
        File file = new File(filePath);
        if (file.exists()) {
            return BitmapFactory.decodeFile(filePath);
        }
        return null;
    }

    @Override
    public void remove(String url) {
        File file = new File(getFilePath(url));
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void clear() {
        File file = new File(cachePath);
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File eachFile : files) {
                eachFile.delete();
            }
        }
    }

    public String getFilePath(String url) {
        File file = new File(cachePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = generator.generate(url);
        return cachePath + File.separator + fileName;
    }

    public String getCachePath() {
        return cachePath;
    }


}
