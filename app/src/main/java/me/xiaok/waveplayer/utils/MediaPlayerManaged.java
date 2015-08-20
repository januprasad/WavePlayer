package me.xiaok.waveplayer.utils;

import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by GeeKaven on 15/8/20.
 */
public class MediaPlayerManaged extends MediaPlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    public static enum status {
        IDLE, INITIALIZED, PREPARING, PREPARED, STARTED, PAUSED, STOPPED, COMPLETED
    }

    private static final String TAG = "MediaPlayerManaged";
    private status state;
    private OnPreparedListener onPreparedListener;
    private OnCompletionListener onCompletionListener;
    private OnErrorListener onErrorListener;

    public MediaPlayerManaged() {
        super();
        super.setOnCompletionListener(this);
        super.setOnPreparedListener(this);
        super.setOnErrorListener(this);
        state = status.IDLE;
    }

    @Override
    public void reset() {
        super.reset();
        state = status.IDLE;
    }

    @Override
    public void setDataSource(String path) throws IOException {
        if (state == status.IDLE) {
            super.setDataSource(path);
            state = status.INITIALIZED;
        } else {
            Log.i(TAG, "Attempted to set data source, but media player was in state " + state);
        }
    }

    @Override
    public void prepareAsync() {
        if (state == status.INITIALIZED) {
            super.prepareAsync();
            state = status.PREPARING;
        } else {
            Log.i(TAG, "Attempted to prepare async, but media player was in state " + state);
        }
    }

    @Override
    public void prepare() throws IOException {
        if (state == status.INITIALIZED) {
            super.prepare();
            state = status.PREPARING;
        } else {
            Log.i(TAG, "Attempted to prepare, but media player was in state " + state);
        }
    }

    @Override
    public void setOnPreparedListener(OnPreparedListener listener) {
        this.onPreparedListener = listener;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        state = status.PREPARED;
        if (onPreparedListener != null) {
            onPreparedListener.onPrepared(mp);
        }
    }

    @Override
    public void start() {
        if (state == status.PREPARED || state == status.STARTED || state == status.PAUSED || state == status.COMPLETED) {
            super.start();
            state = status.STARTED;
        } else {
            Log.i(TAG, "Attempted to start, but media player was in state " + state);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        state = status.COMPLETED;
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(mp);
        }
    }

    @Override
    public void setOnCompletionListener(OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }

    @Override
    public void setOnErrorListener(OnErrorListener listener) {
        onErrorListener = listener;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (onErrorListener != null) return onErrorListener.onError(mp, what, extra);

        Log.i(TAG, "An error occurred and the player was reset");
        reset();
        return true;
    }

    @Override
    public void seekTo(int mSec) {
        if (state == status.PREPARED || state == status.STARTED || state == status.PAUSED || state == status.COMPLETED) {
            super.seekTo(mSec);
        } else {
            Log.i(TAG, "Attempted to set seek, but media player was in state " + state);
        }
    }

    @Override
    public void stop() {
        if (state == status.STARTED || state == status.PAUSED || state == status.COMPLETED) {
            super.stop();
            state = status.STOPPED;
        } else {
            Log.i(TAG, "Attempted to stop, but media player was in state " + state);
        }
    }

    @Override
    public void pause() {
        if (state == status.STARTED || state == status.PAUSED) {
            super.pause();
            state = status.PAUSED;
        } else {
            Log.i(TAG, "Attempted to pause, but media player was in state " + state);
        }
    }

    public status getState() {
        return state;
    }
}
