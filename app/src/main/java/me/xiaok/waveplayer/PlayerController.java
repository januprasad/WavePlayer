package me.xiaok.waveplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.IBinder;

import java.util.ArrayList;

import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.PreferencesUtils;

/**
 * 播放器控制类，用于控制Service中的播放器
 * Created by GeeKaven on 15/8/20.
 */
public class PlayerController {
  public static final String TAG = "PlayerController";

  private static Context application;
  private static Player.Info info;
  private static Bitmap art;

  /**
   * 启动播放器服务
   */
  public static void startService(Context context) {
    if (application == null) {
      application = context;

      Intent serviceIntent = new Intent(context, PlayerService.class);
      context.bindService(serviceIntent, new ServiceConnection() {
        @Override public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override public void onServiceDisconnected(ComponentName name) {
        }
      }, Context.BIND_AUTO_CREATE);
    }
  }

  /**
   * 得到给Service发送广播的Intent
   */
  public static Intent getBaseIntent(String action) {
    Intent intent = new Intent(application, PlayerService.Listener.class);
    if (action != null) intent.setAction(action);

    return intent;
  }

  public static void togglePlay() {
    if (info != null) {
      info.currentPosition = getCurrentPosition();
      info.currentTime = System.currentTimeMillis();
      info.isPlaying = !info.isPlaying;
    }
    application.sendBroadcast(getBaseIntent(PlayerService.ACTION_TOGGLE_PLAY));
  }

  public static void setQueueAndPosition(final ArrayList<Song> songs, final int position) {
    if (info != null) {
      info.queue = songs;
      info.queuePosition = position;
    }
    LogUtils.v(TAG, songs.get(position).getmSongName() + PlayerService.EXTRA_POSITION);
    Intent intent = getBaseIntent(PlayerService.ACTION_SET_QUEUE);
    intent.putExtra(PlayerService.EXTRA_POSITION, position);
    intent.putParcelableArrayListExtra(PlayerService.EXTRA_QUEUE, songs);
    application.sendBroadcast(intent);
  }

  public static void begin() {
    if (info != null) {
      info.queuePosition = 0;
      info.currentTime = System.currentTimeMillis();
    }
    application.sendBroadcast(getBaseIntent(PlayerService.ACTION_BEGIN));
  }

  public static void next() {
    if (info != null && info.queuePosition < info.queue.size()) info.queuePosition++;
    application.sendBroadcast(getBaseIntent(PlayerService.ACTION_NEXT));
  }

  public static void previous() {
    if (info != null) {
      info.currentPosition = 0;
      info.currentTime = System.currentTimeMillis();

      if (info.queuePosition > 0) {
        info.queuePosition--;
      }
    }
    application.sendBroadcast(getBaseIntent(PlayerService.ACTION_PREVIOUS));
  }

  public static void pause() {
    application.sendBroadcast(getBaseIntent(PlayerService.ACTION_PAUSE));
  }

  public static void stop() {
    application.sendBroadcast(getBaseIntent(PlayerService.ACTION_STOP));
    application = null;
    info = null;
    art = null;
  }

  public static void seek(final int progress) {
    if (info != null) {
      info.currentPosition = progress;
      info.currentTime = System.currentTimeMillis();
    }
    Intent intent = getBaseIntent(PlayerService.ACTION_SEEK);
    intent.putExtra(PlayerService.EXTRA_SEEK_POSITION, progress);
    application.sendBroadcast(intent);
  }

  public static void togglePlayState(int state) {

    PreferencesUtils.putInt(application, Player.PREFERENCES_STATE, state);

    Intent intent = getBaseIntent(PlayerService.ACTION_SET_PRES);
    intent.putExtra(PlayerService.EXTRA_STATE, state);
    application.sendBroadcast(intent);
  }

  public static Player.Info getInfo() {
    return info;
  }

  public static long getCurrentPosition() {
    if (info == null) return 0;
    if (!isPlaying()) return info.currentPosition;

    long dT = System.currentTimeMillis() - info.currentTime;
    return info.currentPosition + dT;
  }

  public static long getDuration() {
    if (info != null) {
      return info.duration;
    }
    return Integer.MAX_VALUE;
  }

  public static boolean isPlaying() {
    return info != null && info.isPlaying;
  }

  public static Song getNowPlaying() {
    if (info != null && info.queuePosition < info.queue.size()) {
      return info.queue.get(info.queuePosition);
    }
    return null;
  }

  public static Bitmap getArt() {
    if (art == null) {
      art = FetchUtils.fetchFullArt(getNowPlaying());
    }
    return art;
  }

  public static class Receiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(Player.UPDATE_SONG_INFO)) {
        info = intent.getExtras().getParcelable(Player.EXTRA_NAME);
        art = null;
      }
    }
  }
}
