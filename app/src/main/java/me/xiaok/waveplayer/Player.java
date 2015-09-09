package me.xiaok.waveplayer;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import me.xiaok.waveplayer.activities.NowPlayingMusic;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.PreferencesUtils;

/**
 * Created by GeeKaven on 15/8/19.
 */
public class Player implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
    AudioManager.OnAudioFocusChangeListener {

  private static final String TAG = "Player";
  //播放切换时发送播放歌曲的状态
  public static final String UPDATE_SONG_INFO = "me.xiaok.wavemusic.REFRESH_INFO";
  public static final String EXTRA_NAME = "extra_name";
  public static final String PREFERENCES_STATE = "state";

  private Listener listener;
  private MediaSessionCompat mediaSession;
  private boolean focused = false;
  private boolean shouldPlay = false;

  //播放列表
  private ArrayList<Song> queue;
  private ArrayList<Song> shuffleQueue = new ArrayList<>();
  private int shuffleQueuePosition;
  private int queuePosition;
  private Context context;
  private Bitmap art;

  private int state;
  public static final int REPEAT_NONE = 0;
  public static final int REPEAT_ALL = 1;
  public static final int REPEAT_ONE = 2;
  public static final int SHUFFLE = 3;

  //播放器
  private MediaPlayer mediaPlayer;

  /**
   * Player构造函数， 进行MediaPlayer初始化
   */
  public Player(Context context) {
    this.context = context;

    mediaPlayer = new MediaPlayer();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);

    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnCompletionListener(this);

    // Initialize the queue
    queue = new ArrayList<>();
    queuePosition = 0;

    state = PreferencesUtils.getInt(context, PREFERENCES_STATE, REPEAT_NONE);

    initMediaSession();

    listener = new Listener(this);
    IntentFilter filter = new IntentFilter();
    filter.addAction(Intent.ACTION_MEDIA_BUTTON);
    filter.addAction(Intent.ACTION_HEADSET_PLUG);
    context.registerReceiver(listener, filter);
  }

  /**
   * 初始化MediaSession，MediaSession用于System用音乐进行交流
   */
  private void initMediaSession() {
    mediaSession = new MediaSessionCompat(context, TAG, null, null);
    mediaSession.setCallback(new MediaSessionCompat.Callback() {
      @Override public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
        LogUtils.i(TAG, "media button event :  " + mediaButtonEvent.getAction());
        return super.onMediaButtonEvent(mediaButtonEvent);
      }

      @Override public void onPlay() {
        play();
      }

      @Override public void onPause() {
        pause();
      }

      @Override public void onStop() {
        stop();
      }

      @Override public void onSeekTo(long pos) {
        setSeek((int) pos);
      }

      @Override public void onSkipToNext() {
        next();
      }

      @Override public void onSkipToQueueItem(long id) {
        super.onSkipToQueueItem(id);
      }
    });

    mediaSession.setSessionActivity(PendingIntent.getActivity(context, 0,
        new Intent(context, NowPlayingMusic.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
        PendingIntent.FLAG_CANCEL_CURRENT));
    mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

    PlaybackStateCompat.Builder state = new PlaybackStateCompat.Builder().setActions(
        PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
        .setState(PlaybackStateCompat.STATE_NONE, 0, 0f);
    mediaSession.setPlaybackState(state.build());
    mediaSession.setActive(true);
  }

  /**
   * 更新MediaSession，设置正在播放歌曲的信息
   */
  private void updateMediaSession() {
    if (getNowPlaying() != null) {
      MediaMetadataCompat.Builder metadataBuilder = new MediaMetadataCompat.Builder();
      metadataBuilder.putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE,
          getNowPlaying().getmSongName())
          .putString(MediaMetadataCompat.METADATA_KEY_TITLE, getNowPlaying().getmSongName())
          .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, getNowPlaying().getmAblumName())
          .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, getNowPlaying().getmArtistName())
          .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, getArt());
      mediaSession.setMetadata(metadataBuilder.build());

      PlaybackStateCompat.Builder state = new PlaybackStateCompat.Builder().setActions(
          PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE
              | PlaybackStateCompat.ACTION_PLAY_PAUSE
              | PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
              | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
              | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

      if (isPlaying()) {
        state.setState(PlaybackStateCompat.STATE_PLAYING, getQueuePosition(), 1f);
      } else {
        state.setState(PlaybackStateCompat.STATE_PAUSED, getQueuePosition(), 1f);
      }

      mediaSession.setPlaybackState(state.build());
    }
  }

  @Override public void onAudioFocusChange(int focusChange) {

    shouldPlay = isPlaying() || shouldPlay;

    switch (focusChange) {
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
        pause();
        break;
      case AudioManager.AUDIOFOCUS_LOSS:
        stop();
        break;
      case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
        mediaPlayer.setVolume(0.5f, 0.5f);
        break;
      case AudioManager.AUDIOFOCUS_GAIN:
        mediaPlayer.setVolume(1.0f, 1.0f);
        if (shouldPlay) play();
        shouldPlay = false;
        break;
    }
  }

  /**
   * 请求Audio焦点
   */
  public boolean getFocus() {
    if (!focused) {
      AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
      focused = (audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
          AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }
    return focused;
  }

  /**
   * 播放前准备
   */
  public void begin() {
    if (getFocus()) {
      mediaPlayer.stop();
      mediaPlayer.reset();

      art = FetchUtils.fetchFullArt(getNowPlaying());

      try {
        mediaPlayer.setDataSource(getNowPlaying().getmSongPath());
        mediaPlayer.prepareAsync();
        updateNowPlaying();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Play与Pause切换
   */
  public void togglePlay() {
    if (isPlaying()) {
      pause();
    } else {
      play();
    }
  }

  /**
   * 播放
   */
  public void play() {
    if (!isPlaying() && getFocus()) {
      mediaPlayer.start();
      updateNowPlaying();
    }
  }

  /**
   * 下一首
   */
  public void next() {
    if (state == SHUFFLE) {
      if (shuffleQueuePosition + 1 < shuffleQueue.size()) {
        shuffleQueuePosition++;
      } else {
        buildShuffleQueue();
      }
      begin();
    } else {
      if (queuePosition + 1 < queue.size()) {
        queuePosition++;
        begin();
      } else {
        if (state == REPEAT_ALL) {
          queuePosition = 0;
          begin();
        } else {
          mediaPlayer.pause();
          mediaPlayer.seekTo(mediaPlayer.getDuration());
          updateNowPlaying();
        }
      }
    }
  }

  /**
   * 上一首
   */
  public void previous() {
    if (queuePosition - 1 < 0) {
      queuePosition = queue.size() - 1;
    } else {
      queuePosition--;
    }
    begin();
  }

  /**
   * 暂停
   */
  public void pause() {
    if (isPlaying()) {
      mediaPlayer.pause();
      updateNowPlaying();
    }
  }

  /**
   * 停止
   */
  public void stop() {
    if (isPlaying()) {
      mediaPlayer.stop();
    }

    ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).abandonAudioFocus(this);
    focused = false;
    updateMediaSession();
  }

  /**
   * 设置播放队列以及起始位置
   */
  public void setQueue(ArrayList<Song> list, int positon) {
    this.queue = list;
    this.queuePosition = positon;
    if (state == SHUFFLE) {
      buildShuffleQueue();
    }
  }

  public void setSeek(int progress) {
    if (progress <= mediaPlayer.getDuration() && getNowPlaying() != null) {
      mediaPlayer.seekTo(progress);
    }
  }

  /**
   * 将一组歌添加到播放队列
   */
  public void addQueue(ArrayList<Song> list) {
    this.queue.addAll(list);
  }

  /**
   * 准备完成
   */
  @Override public void onPrepared(MediaPlayer mp) {
    mediaPlayer.start();
    updateNowPlaying();
  }

  /**
   * 播放完成
   */
  @Override public void onCompletion(MediaPlayer mediaPlayer) {
    if (state == REPEAT_ONE) {
      mediaPlayer.seekTo(0);
      play();
      updateNowPlaying();
    } else {
      next();
    }
  }

  /**
   * 结束时清除资源
   */
  public void finish() {
    ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).abandonAudioFocus(this);
    context.unregisterReceiver(listener);

    focused = false;
    mediaPlayer.stop();
    mediaPlayer.release();
    mediaPlayer = null;
    context = null;
  }

  /**
   * 设置播放状态，随机，列表循环，列表播放，单曲循环
   */
  public void setPreferences(int stateSetting) {
    state = stateSetting;
    if (state == SHUFFLE) {
      if (shuffleQueue.size() == 0) {
        buildShuffleQueue();
      }
    } else {
      if (shuffleQueue.size() > 0) {
        queuePosition = queue.indexOf(shuffleQueue.get(shuffleQueuePosition));
        shuffleQueue = new ArrayList<>();
      }
    }
  }

  /**
   * 建立随机播放列表
   */
  private void buildShuffleQueue() {
    shuffleQueue.clear();

    if (queue.size() > 0) {
      shuffleQueuePosition = 0;
      shuffleQueue.add(queue.get(queuePosition));

      ArrayList<Song> random = new ArrayList<>();

      for (int i = 0; i < queuePosition; i++) {
        random.add(queue.get(i));
      }

      for (int i = queuePosition + 1; i < queue.size(); i++) {
        random.add(queue.get(i));
      }

      Collections.shuffle(random, new Random(System.nanoTime()));

      shuffleQueue.addAll(random);
    }
  }

  /**
   * 更新正在播放的音乐
   */
  public void updateNowPlaying() {
    PlayerService.getInstance().notifyNowPlaying();

    Intent intent = new Intent();
    intent.setAction(UPDATE_SONG_INFO);
    Bundle bundle = new Bundle();
    bundle.putParcelable(EXTRA_NAME, new Info(this));
    intent.putExtras(bundle);
    context.sendOrderedBroadcast(intent, null);

    updateMediaSession();
  }

  /**
   * 携带播放信息类
   */
  public static class Info implements Parcelable {
    public boolean isPlaying;
    //存储当前系统时间
    public long currentTime;
    // 存储歌曲总时间
    public long duration;
    // 存储歌曲当前时间
    public long currentPosition;
    //当前播放队列
    public ArrayList<Song> queue;
    public int queuePosition;

    public Info(Player player) {
      isPlaying = player.isPlaying();
      currentTime = System.currentTimeMillis();
      duration = player.getDuration();
      currentPosition = player.getCurrentPosition();
      queue = player.getQueue();
      queuePosition = player.getQueuePosition();
    }

    public Info(Parcel parcel) {
      boolean[] booleans = new boolean[1];
      parcel.readBooleanArray(booleans);
      isPlaying = booleans[0];
      currentTime = parcel.readLong();
      duration = parcel.readLong();
      currentPosition = parcel.readLong();
      queue = parcel.createTypedArrayList(Song.CREATOR);
      queuePosition = parcel.readInt();
    }

    public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
      @Override public Info createFromParcel(Parcel parcel) {
        return new Info(parcel);
      }

      @Override public Info[] newArray(int size) {
        return new Info[size];
      }
    };

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int flag) {
      parcel.writeBooleanArray(new boolean[] { isPlaying });
      parcel.writeLong(currentTime);
      parcel.writeLong(duration);
      parcel.writeLong(currentPosition);
      parcel.writeTypedArray(queue.toArray(new Parcelable[queue.size()]), flag);
      parcel.writeInt(queuePosition);
    }
  }

  /**
   * 判断是否正在播放
   */
  public boolean isPlaying() {
    return mediaPlayer.isPlaying();
  }

  /**
   * 获得当前正在播放的歌曲
   */
  public Song getNowPlaying() {
    if (state == SHUFFLE) {
      if (shuffleQueue.size() == 0) {
        return null;
      } else {
        return shuffleQueue.get(shuffleQueuePosition);
      }
    } else {
      if (queue.size() == 0) {
        return null;
      } else {
        return queue.get(queuePosition);
      }
    }
  }

  /**
   * 获得目前正在播放的时间
   */
  public int getCurrentPosition() {
    return mediaPlayer.getCurrentPosition();
  }

  /**
   * 获得当前播放队列位置
   */
  public int getQueuePosition() {
    if (state == SHUFFLE) {
      return shuffleQueuePosition;
    }
    return queuePosition;
  }

  /**
   * 获取当前播放队列
   */
  public ArrayList<Song> getQueue() {
    if (state == SHUFFLE) {
      return new ArrayList<>(shuffleQueue);
    }
    return new ArrayList<>(queue);
  }

  /**
   * 获取当前播放歌曲总时间
   */
  public long getDuration() {
    if (getNowPlaying() != null) {
      return getNowPlaying().getmDuration();
    }
    return 0;
  }

  public Bitmap getArt() {
    return art;
  }

  /**
   * 主要用于监听系统media按键，耳机插拔
   */
  public static class Listener extends BroadcastReceiver {

    Player instence;

    public Listener(Player instence) {
      this.instence = instence;
    }

    @Override public void onReceive(Context context, Intent intent) {
      LogUtils.i(TAG, intent.getAction());
      if (Intent.ACTION_HEADSET_PLUG.equals(intent.getAction()) && instence.isPlaying()
          && intent.getIntExtra("state", -1) == 0) {
        instence.pause();
      }
    }
  }
}
