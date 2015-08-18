package me.xiaok.waveplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created by GeeKaven on 15/8/18.
 */
public class Navigate {
    /**
     * 开启一个Activity
     * @param from 原Activity
     * @param to 目的Activity
     */
    public static void to(Context from, Class<? extends Activity> to) {
        to(from, to, null, null);
    }

    /**
     * 开启一个Activity,带参数
     * @param from 原Activity
     * @param to 目的Activity
     * @param extra 参数KEY
     * @param p 参数
     */
    public static void to(Context from, Class<? extends Activity> to, String extra, Parcelable p) {
        Intent intent = new Intent(from, to);
        if (extra != null && p != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(extra, p);
            intent.putExtras(bundle);
        }
        from.startActivity(intent);
    }

    public static void up(Activity activity) {
        activity.finish();
    }

    public static void back(Activity activity) {
        activity.finish();
    }

}
