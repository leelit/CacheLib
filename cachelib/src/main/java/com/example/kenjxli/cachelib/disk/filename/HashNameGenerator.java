package com.example.kenjxli.cachelib.disk.filename;

/**
 * Created by kenjxli on 2015/7/28.
 */
public class HashNameGenerator implements FileNameGenerator {


    @Override
    public String generate(String url) {
        return url.hashCode() + "";
    }
}
