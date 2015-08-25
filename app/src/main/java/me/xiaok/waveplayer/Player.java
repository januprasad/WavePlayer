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

/**
 * Created by GeeKaven on 15/8/19.
 */
public class Player implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = "Player";
    //播放切换时发送播放歌曲的状态
    public static final String UPDATE_SONG_INFO = "me.xiaok.wavemusic.REFRESH_INFO";
    public static final String EXTRA_NAME = "extra_name";

    //播放列表
    private ArrayList<Song> queue;
    private int queuePosition;
    private Context context;
    private Bitmap art;

    //播放器
    private MediaPlayer mediaPlayer;

    /**
     * Player构造函数， 进行MediaPlayer初始化
     *
     * @param context
     */
    public Player(Context context) {
        this.context = context;

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);

        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);

    }

    /**
     * 播放前准备
     */
    public void begin() {
        mediaPlayer.stop();
        mediaPlayer.reset();

        art = FetchUtils.fetchFullArt(getNowPlaying());
        if (art == null) {
            LogUtils.v(TAG, "art is null");
        }

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
        mediaPlayer.pause();
    }

    /**
     * 设置播放队列以及起始位置
     *
     * @param list
     * @param positon
     */
    public void setQueue(ArrayList<Song> list, int positon) {
        this.queue = list;
        this.queuePosition = positon;

    }

    public void setSeek(int progress) {
        if (progress <= mediaPlayer.getDuration() && getNowPlaying() != null) {
            mediaPlayer.seekTo(progress);
        }
    }

    /**
     * 将一组歌添加到播放队列
     *
     * @param list
     */
    public void addQueue(ArrayList<Song> list) {
        this.queue.addAll(list);
    }

    /**
     * 准备完成
     *
     * @param mp
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
        updateNowPlaying();
    }

    /**
     * 播放完成
     *
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
        intent.setAction(UPDATE_SONG_INFO);
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_NAME, new Info(this));
        intent.putExtras(bundle);
        context.sendOrderedBroadcast(intent, null);
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
        public void writeToParcel(Parcel parcel, int flag) {
            parcel.writeBooleanArray(new boolean[]{isPlaying});
            parcel.writeLong(currentTime);
            parcel.writeLong(duration);
            parcel.writeLong(currentPosition);
            parcel.writeTypedArray(queue.toArray(new Parcelable[queue.size()]), flag);
            parcel.writeInt(queuePosition);
        }
    }

    /**
     * 判断是否正在播放
     *
     * @return
     */
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * 获得当前正在播放的歌曲
     *
     * @return
     */
    public Song getNowPlaying() {
        if (queue == null) {
            return null;
        } else {
            return queue.get(queuePosition);
        }
    }

    /**
     * 获得目前正在播放的时间
     *
     * @return
     */
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    /**
     * 获得当前播放队列位置
     *
     * @return
     */
    public int getQueuePosition() {
        return queuePosition;
    }

    /**
     * 获取当前播放队列
     *
     * @return
     */
    public ArrayList<Song> getQueue() {
        return new ArrayList<>(queue);
    }

    /**
     * 获取当前播放歌曲总时间
     *
     * @return
     */
    public long getDuration() {
        return getNowPlaying().getmDuration();
    }

    public Bitmap getArt() {
        return art;
    }
}
