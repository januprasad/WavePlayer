package me.xiaok.waveplayer;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by GeeKaven on 15/8/17.
 */
public class WaveApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        PlayerController.startService(WaveApplication.getContext());
        Fresco.initialize(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }
}
