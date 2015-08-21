package me.xiaok.waveplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PowerManager;

import java.io.IOException;
import java.util.ArrayList;

import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.MediaPlayerManaged;

/**
 * Created by GeeKaven on 15/8/19.
 */
public class Player implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener{

    private static final String TAG = "Player";
    public static final String QUEUQ = "Queue";
    public static final String POSITION = "Position";
    //播放切换时发送播放歌曲的状态
    public static final String SONG_CHANGE = "me.xiaok.wavemusic.SONG_CHANGE";
    public static final String INFO = "info";

    //播放列表
    private ArrayList<Song> queue;
    private int queuePosition;
    private Context context;
    private Bitmap art;

    //播放器
    private MediaPlayerManaged mediaPlayer;

    /**
     * Player构造函数， 进行MediaPlayer初始化
     * @param context
     */
    public Player(Context context) {
        this.context = context;

        mediaPlayer = new MediaPlayerManaged();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

        queue = new ArrayList<>();
    }

    /**
     * 播放前准备
     */
    public void begin() {
        mediaPlayer.stop();
        mediaPlayer.reset();

        art = FetchUtils.fetchAlbumArtLocal(getNowPlaying().getmAlbumId());

        try {
            mediaPlayer.setDataSource(queue.get(queuePosition).getmSongPath());
            mediaPlayer.prepareAsync();
            updateNowPlaying();
        } catch (IOException e) {
            e.printStackTrace();
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
        updateNowPlaying();
    }

    /**
     * 播放
     */
    public void play() {
        if (!isPlaying()) {
            mediaPlayer.start();
        }
    }

    /**
     * 下一首
     */
    public void next() {
        if (queuePosition + 1 == queue.size()) {
            queuePosition = 0;
        } else {
            queuePosition++;
        }
        begin();
    }

    /**
     * 上一首
     */
    public void previous() {
        if (queuePosition - 1 < 0){
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
        mediaPlayer.pause();
    }

    /**
     * 设置播放队列以及起始位置
     * @param list
     * @param positon
     */
    public void setQueue(ArrayList<Song> list, int positon) {
        queue.clear();
        this.queue.addAll(list);
        this.queuePosition = positon;
    }

    /**
     * 准备完成
     * @param mediaPlayer
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        updateNowPlaying();
    }

    /**
     * 播放完成
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }

    /**
     * 结束时清除资源
     */
    public void finish() {
        if (isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            context = null;
        }
    }

    /**
     * 更新正在播放的音乐
     */
    public void updateNowPlaying() {
        PlayerService.getInstance().notifyNowPlaying();

        Intent intent = new Intent();
        intent.setAction(SONG_CHANGE);
        Bundle bundle = new Bundle();
        bundle.putParcelable(INFO, new Info(this));
        intent.putExtras(bundle);
        context.sendOrderedBroadcast(intent, null);
    }

    public static class Info implements Parcelable {

        public boolean isPlaying;
        public boolean isPause;
        public boolean isPrepared;
        public Song song;

        public Info(Player player) {
            isPlaying = player.isPlaying();
            isPause = player.isPasue();
            isPrepared = player.isPrepared();
            song = player.getNowPlaying();
        }

        public Info(Parcel parcel) {
            boolean[] booleans = new boolean[3];
            parcel.readBooleanArray(booleans);
            isPlaying = booleans[0];
            isPause = booleans[1];
            isPrepared = booleans[2];
            song = parcel.readParcelable(Song.class.getClassLoader());
        }

        public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
            @Override
            public Info createFromParcel(Parcel parcel) {
                return new Info(parcel);
            }

            @Override
            public Info[] newArray(int size) {
                return new Info[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeBooleanArray(new boolean[]{isPlaying, isPause, isPrepared});
            parcel.writeParcelable(song, PARCELABLE_WRITE_RETURN_VALUE);
        }
    }

    /**
     * 判断是否正在播放
     * @return
     */
    public boolean isPlaying() {
        return mediaPlayer.getState() == MediaPlayerManaged.status.STARTED;
    }

    /**
     * 判断时否暂停
     * @return
     */
    public boolean isPasue() {
        return mediaPlayer.getState() == MediaPlayerManaged.status.PAUSED;
    }

    public boolean isPrepared() {
        return mediaPlayer.getState() == MediaPlayerManaged.status.PREPARED;
    }

    public boolean isPreparing() {
        return mediaPlayer.getState() == MediaPlayerManaged.status.PREPARING;
    }

    /**
     * 获得当前正在播放的歌曲
     * @return
     */
    public Song getNowPlaying() {
        LogUtils.v(TAG, "position: " + queuePosition + "size : " + queue.size());
        if (queue.size() == 0) {
            return null;
        } else {
            return queue.get(queuePosition);
        }
    }

    /**
     * 获得目前正在播放的时间
     * @return
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 获得当前播放队列位置
     * @return
     */
    public int getQueuePosition() {
        return queuePosition;
    }

    public Bitmap getArt() {
        return art;
    }
}
