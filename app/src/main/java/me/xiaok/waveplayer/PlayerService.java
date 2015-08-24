package me.xiaok.waveplayer;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import me.xiaok.waveplayer.activities.HomeActivity;
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
    public static final String ACTION_ADD_QUEUE = "me.xiaok.waveplayer.ADD_QUEUE";
    public static final String ACTION_DELETE_SONG = "me.xiaok.waveplayer.DELETE_SONG";

    /**
     * 全局变量
     */
    private static PlayerService instance;
    private Player player;
    private NotificationManager notificationManager;
    private Context context;
    private boolean finished = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.i(TAG, "service onCreate() call");
        context = WaveApplication.getContext();
        if (instance == null) {
            instance = this;
        } else {
            LogUtils.i(TAG, "Attempt again create service");
            stopSelf();
            return;
        }

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (player == null) {
            player = new Player(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startForeground(NOTIFICATION_ID, getNotification());
        } else {
            startForeground(NOTIFICATION_ID, getNotificationCompat());
        }
    }

    /**
     * 更新Notification
     */
    public void notifyNowPlaying() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationManager.notify(NOTIFICATION_ID, getNotification());
        } else {
            notificationManager.notify(NOTIFICATION_ID, getNotificationCompat());
        }
    }

    /**
     * android 4.4下的Notification
     */
    @TargetApi(18)
    private Notification getNotificationCompat() {

        Intent intent = new Intent(this, Listener.class);

        RemoteViews notificationView = new RemoteViews(getPackageName(), R.layout.notification);
        RemoteViews notificationViewExpanded = new RemoteViews(getPackageName(), R.layout.notification_expanded);

        if (getArt() == null) {
            notificationView.setImageViewResource(R.id.notification_icon, R.mipmap.text_img);
            notificationViewExpanded.setImageViewResource(R.id.notification_icon, R.mipmap.text_img);
        } else {
            notificationView.setImageViewResource(R.id.notification_icon, R.mipmap.text_img);
            notificationViewExpanded.setImageViewResource(R.id.notification_icon, R.mipmap.text_img);
        }

        if (getNowPlaying() != null) {
            //更新notificationView内容
            notificationView.setTextViewText(R.id.notification_title, getNowPlaying().getmSongName());
            notificationView.setTextViewText(R.id.notification_subtitle, getNowPlaying().getmArtistName());
            //更新notificationViewExpanded内容
            notificationViewExpanded.setTextViewText(R.id.notification_title, getNowPlaying().getmSongName());
            notificationViewExpanded.setTextViewText(R.id.notification_subtitle, getNowPlaying().getmArtistName());
            notificationViewExpanded.setTextViewText(R.id.notification_text, getNowPlaying().getmAblumName());

            notificationView.setOnClickPendingIntent(R.id.notification_toggle_play, PendingIntent.getBroadcast(this, 1, intent.setAction(ACTION_TOGGLE_PLAY), 0));
            notificationView.setOnClickPendingIntent(R.id.notification_next, PendingIntent.getBroadcast(this, 1, intent.setAction(ACTION_NEXT), 0));
            notificationView.setOnClickPendingIntent(R.id.notification_previous, PendingIntent.getBroadcast(this, 1, intent.setAction(ACTION_PREVIOUS), 0));

            notificationViewExpanded.setOnClickPendingIntent(R.id.notification_toggle_play, PendingIntent.getBroadcast(this, 1, intent.setAction(ACTION_TOGGLE_PLAY), 0));
            notificationViewExpanded.setOnClickPendingIntent(R.id.notification_next, PendingIntent.getBroadcast(this, 1, intent.setAction(ACTION_NEXT), 0));
            notificationViewExpanded.setOnClickPendingIntent(R.id.notification_previous, PendingIntent.getBroadcast(this, 1, intent.setAction(ACTION_PREVIOUS), 0));

        } else {
            //更新notificationView内容
            notificationView.setTextViewText(R.id.notification_title, "Nothing is playing");
            notificationView.setTextViewText(R.id.notification_subtitle, "");
            //更新notificationViewExpanded内容
            notificationViewExpanded.setTextViewText(R.id.notification_title, "Nothing is playing");
            notificationViewExpanded.setTextViewText(R.id.notification_subtitle, "");
            notificationViewExpanded.setTextViewText(R.id.notification_text, "");
        }

        //更新TogglePlay button
        if (!(player.isPlaying() || player.isPreparing())) {
            notificationView.setImageViewResource(R.id.notification_toggle_play, R.mipmap.ic_play_arrow_white_48dp);
            notificationViewExpanded.setImageViewResource(R.id.notification_toggle_play, R.mipmap.ic_play_arrow_white_48dp);
        } else {
            notificationView.setImageViewResource(R.id.notification_toggle_play, R.mipmap.ic_pause_white_48dp);
            notificationViewExpanded.setImageViewResource(R.id.notification_toggle_play, R.mipmap.ic_pause_white_48dp);
        }

        // Build the notification
        Notification.Builder builder = new Notification.Builder(this)
                .setOngoing(true)
                .setSmallIcon(
                        (player.isPlaying() || player.isPreparing())
                                ? R.mipmap.ic_play_arrow_white_24dp
                                : R.mipmap.ic_pause_white_24dp
                )
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setContentIntent(PendingIntent.getActivity(
                        this,
                        0,
                        new Intent(context, HomeActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContent(notificationView);

        Notification notification;
        notification = builder.build();

        // Manually set the expanded and compact views
        notification.contentView = notificationView;
        notification.bigContentView = notificationViewExpanded;


        return notification;
    }

    /**
     * android 5.0下的Notification
     */
    @TargetApi(21)
    private Notification getNotification() {
        Notification.Builder notification = new Notification.Builder(context);

        Intent intent = new Intent(getInstance(), Listener.class);

        notification
                .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2))
                .setColor(context.getResources().getColor(R.color.grid_default_background))
                .setShowWhen(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setPriority(Notification.PRIORITY_LOW)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setContentIntent(PendingIntent.getActivity(
                        getInstance(),
                        0,
                        new Intent(context, HomeActivity.class),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        // Set the album artwork
        if (getArt() == null) {
            notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.text_img));
        } else {
            notification.setLargeIcon(getArt());
        }

        // 添加控制按钮
        //添加Previous按钮
        notification.addAction(R.mipmap.ic_skip_previous_white_48dp, "action_previous", PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_PREVIOUS), 0));
        // 添加Play/Pause切换按钮
        // Also set the notification's icon to reflect the player's status
        if (player.isPlaying() || player.isPreparing()) {
            notification
                    .addAction(R.mipmap.ic_pause_white_48dp, "action_pause", PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_TOGGLE_PLAY), 0))
                    .setSmallIcon(R.mipmap.ic_play_arrow_white_24dp);
        } else {
            notification
                    .setDeleteIntent(PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_STOP), 0))
                    .addAction(R.mipmap.ic_play_arrow_white_48dp, "action_play", PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_TOGGLE_PLAY), 0))
                    .setSmallIcon(R.mipmap.ic_pause_white_24dp);
        }
        // 添加Next按钮
        notification.addAction(R.mipmap.ic_skip_next_white_48dp, "action_next", PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_NEXT), 0));


        // 更新正在播放的信息
        if (getNowPlaying() != null) {
            notification
                    .setContentTitle(getNowPlaying().getmSongName())
                    .setContentText(getNowPlaying().getmArtistName())
                    .setSubText(getNowPlaying().getmAblumName());
        } else {
            notification
                    .setContentTitle("Nothing is playing")
                    .setContentText("")
                    .setSubText("");
        }
        return notification.build();
    }

    /**
     * 广播接收器，接收对Player的控制
     */
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
                    ArrayList<Song> songs = bundle.getParcelableArrayList(Player.QUEUE);
                    instance.player.setQueue(songs, bundle.getInt(Player.POSITION, 0));
                    instance.player.begin();
                    break;
                case ACTION_TOGGLE_PLAY:
                    instance.player.togglePlay();
                    break;
                case ACTION_PLAY:
                    instance.player.play();
                    break;
                case ACTION_NEXT:
                    instance.player.next();
                    break;
                case ACTION_PREVIOUS:
                    instance.player.previous();
                    break;
                case ACTION_PAUSE:
                    instance.player.pause();
                    break;
                case ACTION_STOP:
                    instance.stop();
                    break;
                case ACTION_ADD_QUEUE:
                    Bundle addBundle = intent.getExtras();
                    ArrayList<Song> songList = addBundle.getParcelableArrayList(PlayerService.ACTION_ADD_QUEUE);
                    instance.player.addQueue(songList);
                    break;
                case ACTION_DELETE_SONG:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        finish();
        super.onDestroy();
    }

    /**
     * 结束
     */
    public void stop() {
        LogUtils.i(TAG, "stop() called");
        // 如果UI线程正在工作，不要结束，只移除Notification
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for (int i = 0; i < procInfos.size(); i++) {
            if (procInfos.get(i).processName.equals(BuildConfig.APPLICATION_ID)) {
                player.pause();
                stopForeground(true);
                return;
            }
        }

        finish();
    }

    /**
     * 结束并清空资源
     */
    public void finish() {
        LogUtils.i(TAG, "finish() called");
        if (!finished) {
            notificationManager.cancel(NOTIFICATION_ID);
            player.finish();
            player = null;
            stopForeground(true);
            instance = null;
            stopSelf();
            finished = true;
        }
    }

    /**
     * 获得Service实例
     *
     * @return
     */
    public static PlayerService getInstance() {
        return instance;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * 获得当前正在播放的音乐
     *
     * @return
     */
    public Song getNowPlaying() {
        return player.getNowPlaying();
    }

    public Bitmap getArt() {
        return player.getArt();
    }

}
