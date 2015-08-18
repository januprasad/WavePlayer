package me.xiaok.waveplayer.utils;

import android.util.Log;

import me.xiaok.waveplayer.BuildConfig;

/**
 * Log帮助类
 * Created by GeeKaven on 15/7/10.
 */
public class LogUtils {
    /**
     * Verbose
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }

    /**
     * Debug
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    /**
     * Info
     * @param tag
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    /**
     * Warning
     * @param tag
     * @param msg
     */
    public static void w(String tag, String msg) {
            Log.w(tag, msg);
    }

    /**
     * Error
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
            Log.e(tag, msg);
    }
}
