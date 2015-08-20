package me.xiaok.waveplayer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * 后台播放服务
 * Created by GeeKaven on 15/8/19.
 */
public class PlayerService extends Service {

    private static final String TAG = "PlayerService";

    /**
     * 常量
     */
    private static final int NOTIFICATION_ID = 1;
    public static final String ACTION_TOGGLE_PLAY = "me.xiaok.waveplayer.ACTION_TOGGLE_PLAY";
    public static final String ACTION_PLAY = "me.xiaok.waveplayer.ACTION_PLAY";
    public static final String ACTION_NEXT = "me.xiaok.waveplayer.ACTION_NEXT";
    public static final String ACTION_PREVIOUS = "me.xiaok.waveplayer.ACTION_PREVIOUS";
    public static final String ACTION_PAUSE = "me.xiaok.waveplayer.ACTION_PAUSE";
    public static final String ACTION_STOP = "me.xiaok.waveplayer.ACTION_STOP";
    public static final String ACTION_SET_QUEUE = "me.xiaok.waveplayer.SET_QUEUE";

    /**
     * 全局变量
     */
    private static PlayerService instance;
    private Player player;
    private NotificationManager notificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "service onCreate() call");
        if (instance == null) {
            instance = this;
        } else {
            LogUtils.i(TAG, "Attempt again create service");
            stopSelf();
            return;
        }

        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (player == null) {
            player = new Player(this);
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            startForeground(NOTIFICATION_ID, getNotification());
//        } else {
//            startForeground(NOTIFICATION_ID, getNotificationCompat());
//        }
    }

    /**
     * android 4.4下的Notification
     */
    @TargetApi(18)
    private Notification getNotificationCompat() {
        return null;
    }

    /**
     * android 5.0下的Notification
     */
    @TargetApi(21)
    private Notification getNotification() {
        return null;
    }

    public static class Listener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == null) {
                LogUtils.i(TAG, "not get action");
                return;
            }

            switch (intent.getAction()) {
                case ACTION_SET_QUEUE:
                    Bundle bundle = intent.getExtras();
                    ArrayList<Song> songs = bundle.getParcelableArrayList(Player.QUEUQ);
                    getService().player.setQueue(songs, bundle.getInt(Player.POSITION, 0));
                    getService().player.begin();
                    break;
                case ACTION_TOGGLE_PLAY:
                    getService().player.togglePlay();
                    break;
                case ACTION_PLAY:
                    getService().player.play();
                    break;
                case ACTION_NEXT:
                    getService().player.next();
                    break;
                case ACTION_PREVIOUS:
                    getService().player.previous();
                    break;
                case ACTION_PAUSE:
                    getService().player.pause();
                    break;
                case ACTION_STOP:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        instance = null;
        notificationManager.cancel(NOTIFICATION_ID);
        player.finish();
    }

    public static PlayerService getService() {
        return instance;
    }

    public Player getPlayer() {
        return player;
    }

}
