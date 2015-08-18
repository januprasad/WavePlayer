package me.xiaok.waveplayer;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

/**
 * Created by GeeKaven on 15/8/17.
 */
public class WaveApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        Fresco.initialize(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }
}
