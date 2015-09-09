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
   */
  public static void v(String tag, String msg) {
    if (BuildConfig.DEBUG) {
      Log.v(tag, msg);
    }
  }

  /**
   * Debug
   */
  public static void d(String tag, String msg) {
    if (BuildConfig.DEBUG) {
      Log.d(tag, msg);
    }
  }

  /**
   * Info
   */
  public static void i(String tag, String msg) {
    Log.i(tag, msg);
  }

  /**
   * Warning
   */
  public static void w(String tag, String msg) {
    Log.w(tag, msg);
  }

  /**
   * Error
   */
  public static void e(String tag, String msg) {
    Log.e(tag, msg);
  }
}
