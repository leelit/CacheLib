package com.example.kenjxli.cachelib.utils.logger;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kenjxli on 2015/8/7.
 */
public class Logger {

    private static final String[] infos = new String[]{LoggerInfo.ADD_TO_DISK, LoggerInfo.ADD_TO_MEMORY, LoggerInfo.GET_FROM_DISK, LoggerInfo.GET_FROM_MEMORY, LoggerInfo.NET_DOWNLOAD};
    public static final String LOG_PATH = "/storage/emulated/0/alog.txt";

    private static boolean turnOnLog = false;

    public static void turnOffLog() {
        turnOnLog = true;
    }


    public static void log(Context context, String info, boolean successful) {
        if (turnOnLog) {
            log(context, Logger.LOG_PATH, info, successful);
        }
    }

    public static void log(Context context, String logPath, String info, boolean successful) {
        File file = new File(logPath);
        if (!file.exists()) {
            writeLog(context, logPath);
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("log", Context.MODE_PRIVATE);
        if (!isInfoValue(info)) {
            throw new IllegalArgumentException("info is wrong");
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int total = sharedPreferences.getInt(info, 0);
            editor.putInt(info, total + 1);
            int ok = sharedPreferences.getInt(info + "OK", 0);
            if (successful) {
                editor.putInt(info + "OK", ok + 1);
            }
            editor.commit();
            writeLog(context, logPath);
        }

    }


    private static void writeLog(Context context, String logPath) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("log", Context.MODE_PRIVATE);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < infos.length; i++) {
            int info_ok = sharedPreferences.getInt(infos[i] + "OK", 0);
            int info_total = sharedPreferences.getInt(infos[i], 0);
            String pro = info_ok + "/" + info_total;
            sb.append(infos[i]).append("\n").append(pro).append("\n");
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(logPath);
            out.write(sb.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private static boolean isInfoValue(String info) {
        boolean isValid = false;
        for (int i = 0; i < infos.length; i++) {
            if (info.equals(infos[i])) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }


}
