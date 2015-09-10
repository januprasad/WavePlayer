package me.xiaok.waveplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import java.util.ArrayList;
import me.xiaok.waveplayer.activities.NowPlayingMusic;
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
  public static final String ACTION_BEGIN = "me.xiaok.waveplayer.ACTION_BEGIN";
  public static final String ACTION_TOGGLE_PLAY = "me.xiaok.waveplayer.ACTION_TOGGLE_PLAY";
  public static final String ACTION_PLAY = "me.xiaok.waveplayer.ACTION_PLAY";
  public static final String ACTION_NEXT = "me.xiaok.waveplayer.ACTION_NEXT";
  public static final String ACTION_PREVIOUS = "me.xiaok.waveplayer.ACTION_PREVIOUS";
  public static final String ACTION_PAUSE = "me.xiaok.waveplayer.ACTION_PAUSE";
  public static final String ACTION_STOP = "me.xiaok.waveplayer.ACTION_STOP";
  public static final String ACTION_SEEK = "me.xiaok.waveplayer.ACTION_SEEK";
  public static final String ACTION_SET_QUEUE = "me.xiaok.waveplayer.SET_QUEUE";
  public static final String ACTION_DELETE_SONG = "me.xiaok.waveplayer.DELETE_SONG";
  public static final String ACTION_SET_PRES = "me.xiaok.waveplayer.SET_PRES";

  public static final String EXTRA_QUEUE = "extra_queue";
  public static final String EXTRA_POSITION = "extra_position";
  public static final String EXTRA_SEEK_POSITION = "extra_seek_position";
  public static final String EXTRA_STATE = "extra_state";

  /**
   * 全局变量
   */
  private static PlayerService instance;
  private Player player;
  private NotificationManager notificationManager;
  private Context context;
  private boolean finished = false;

  @Nullable @Override public IBinder onBind(Intent intent) {
    return null;
  }

  @Override public void onCreate() {
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

    startForeground(NOTIFICATION_ID, getNotification());
  }

  /**
   * 更新Notification
   */
  public void notifyNowPlaying() {
    notificationManager.notify(NOTIFICATION_ID, getNotification());
  }

  /**
   * 使用5.0的Notification管理，同时兼容5.0以前设备
   */
  private Notification getNotification() {
    NotificationCompat.Builder notification = new NotificationCompat.Builder(context);

    Intent intent = new Intent(getInstance(), Listener.class);

    Intent nowPlayingIntent = new Intent(context, NowPlayingMusic.class);

    notification.setStyle(new NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2))
        .setColor(context.getResources().getColor(R.color.grid_default_background))
        .setShowWhen(false)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setContentIntent(PendingIntent.getActivity(getInstance(), 0, nowPlayingIntent,
            PendingIntent.FLAG_UPDATE_CURRENT));

    // 这种专辑图标
    if (getArt() == null) {
      notification.setLargeIcon(
          BitmapFactory.decodeResource(context.getResources(), R.mipmap.text_img));
    } else {
      notification.setLargeIcon(getArt());
    }

    // 添加控制按钮
    //添加Previous按钮
    notification.addAction(R.mipmap.ic_skip_previous_white_48dp, "action_previous",
        PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_PREVIOUS), 0));
    // 添加Play/Pause切换按钮
    if (player.isPlaying()) {
      notification.addAction(R.mipmap.ic_pause_white_48dp, "action_pause",
          PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_TOGGLE_PLAY), 0))
          .setSmallIcon(R.mipmap.ic_play_arrow_white_24dp);
    } else {
      notification.setDeleteIntent(
          PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_STOP), 0))
          .addAction(R.mipmap.ic_play_arrow_white_48dp, "action_play",
              PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_TOGGLE_PLAY), 0))
          .setSmallIcon(R.mipmap.ic_pause_white_24dp);
    }
    // 添加Next按钮
    notification.addAction(R.mipmap.ic_skip_next_white_48dp, "action_next",
        PendingIntent.getBroadcast(context, 1, intent.setAction(ACTION_NEXT), 0));

    // 更新正在播放的信息
    if (getNowPlaying() != null) {
      notification.setContentTitle(getNowPlaying().getmSongName())
          .setContentText(getNowPlaying().getmArtistName())
          .setSubText(getNowPlaying().getmAblumName());
    } else {
      notification.setContentTitle("Nothing is playing").setContentText("").setSubText("");
    }
    return notification.build();
  }

  /**
   * 广播接收器，接收对Player的控制
   */
  public static class Listener extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
      if (intent.getAction() == null) {
        LogUtils.i(TAG, "not get action");
        return;
      }

      switch (intent.getAction()) {
        case ACTION_BEGIN:
          instance.player.begin();
          break;
        case ACTION_SET_QUEUE:
          int position = intent.getIntExtra(EXTRA_POSITION, 0);
          ArrayList<Song> songList = intent.getParcelableArrayListExtra(EXTRA_QUEUE);
          instance.player.setQueue(songList, position);
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
        case ACTION_SEEK:
          instance.player.setSeek(intent.getIntExtra(EXTRA_SEEK_POSITION, 0));
          break;
        case ACTION_DELETE_SONG:
          break;
        case ACTION_SET_PRES:
          instance.getPlayer().setPreferences(intent.getIntExtra(EXTRA_STATE, Player.REPEAT_ALL));
          break;
      }
    }
  }

  @Override public void onDestroy() {
    LogUtils.i(TAG, "onDestroy is called");
    finish();
    super.onDestroy();
  }

  /**
   * 结束
   */
  public void stop() {
    LogUtils.i(TAG, "stop() called");

    player.pause();
    stopForeground(true);

    finish();
  }

  /**
   * 结束并清空资源
   */
  public void finish() {
    LogUtils.i(TAG, "finish() called");
    LogUtils.i(TAG, "is finished : " + finished);
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
   */
  public static PlayerService getInstance() {
    return instance;
  }

  public Player getPlayer() {
    return player;
  }

  /**
   * 获得当前正在播放的音乐
   */
  public Song getNowPlaying() {
    return player.getNowPlaying();
  }

  public Bitmap getArt() {
    return player.getArt();
  }
}
