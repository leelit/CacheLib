package com.example.kenjxli.cachelib.utils.logger;

/**
 * Created by kenjxli on 2015/8/14.
 */
public class DownloadException extends Exception {
    private int responseCode;

    public DownloadException(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getResponseCode() {
        return responseCode;
    }
}
