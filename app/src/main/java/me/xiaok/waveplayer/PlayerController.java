package me.xiaok.waveplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import java.util.ArrayList;

import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * 播放器控制类，用于控制Service中的播放器
 * Created by GeeKaven on 15/8/20.
 */
public class PlayerController {
    public static final String TAG = "PlayerController";

    public static Context application;


    /**
     * 启动播放器服务
     * @param context
     */
    public static void startService(Context context) {
        if (application == null) {
            application = context;

            Intent intent = new Intent(context, PlayerService.class);
            context.startService(intent);
        }
    }

    /**
     * 得到给Service发送广播的Intent
     * @param action
     * @return
     */
    public static Intent getBaseIntent(String action) {
        Intent intent = new Intent(application, PlayerService.Listener.class);
        if (action != null) intent.setAction(action);

        return intent;
    }

    public static void playAll(final ArrayList<Song> queue) {
        Intent intent = getBaseIntent(PlayerService.ACTION_PLAY_ALL);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PlayerService.ACTION_PLAY_ALL, queue);
        intent.putExtras(bundle);
        application.sendBroadcast(intent);
    }

    public static void playSong(final Song song) {
        Intent intent = getBaseIntent(PlayerService.ACTION_PLAY_SONG);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PlayerService.ACTION_PLAY_SONG, song);
        intent.putExtras(bundle);
        application.sendBroadcast(intent);
    }

    public static void addQueue(final ArrayList<Song> queue) {
        Intent intent = getBaseIntent(PlayerService.ACTION_ADD_QUEUE);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PlayerService.ACTION_ADD_QUEUE, queue);
        intent.putExtras(bundle);
        application.sendBroadcast(intent);
    }

    public static void togglePlay() {
        application.sendBroadcast(getBaseIntent(PlayerService.ACTION_TOGGLE_PLAY));
    }

    public static void play() {
        application.sendBroadcast(getBaseIntent(PlayerService.ACTION_PLAY));
    }

    public static void next() {
        application.sendBroadcast(getBaseIntent(PlayerService.ACTION_NEXT));
    }

    public static void previous() {
        application.sendBroadcast(getBaseIntent(PlayerService.ACTION_PREVIOUS));
    }

    public static void pause() {
        application.sendBroadcast(getBaseIntent(PlayerService.ACTION_PAUSE));
    }

    public static void stop() {
        application.sendBroadcast(getBaseIntent(PlayerService.ACTION_STOP));
    }

}
