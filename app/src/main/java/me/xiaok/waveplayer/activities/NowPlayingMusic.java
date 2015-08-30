package me.xiaok.waveplayer.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import me.xiaok.waveplayer.Player;
import me.xiaok.waveplayer.PlayerController;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.WaveApplication;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.models.viewholders.AlbumViewHolder;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.MusicUtils;
import me.xiaok.waveplayer.utils.PreferencesUtils;

/**
 * 正在播放音乐界面
 * Created by GeeKaven on 15/8/20.
 */
public class NowPlayingMusic extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "NowPlayingMusic";

    public static final String EXTRA_NOW_PLAYING = "extra_NowPlayingMusic";

    private Song song;
    private SeekBar mSeekBar;
    private SimpleDraweeView mSongImg;
    private TextView mSongTitle;
    private TextView mSongInfo;

    private ImageView mTogglePlay;
    private ImageView mNext;
    private ImageView mPrevious;
    private ImageView mReflectedImage;

    private SeekObserver observer = null;
    private Song currentRef = null;

    private int iconIndex;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_now_playing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        song = intent.getExtras().getParcelable(EXTRA_NOW_PLAYING);

        setupInstance();

        observer = new SeekObserver();

        iconIndex = PreferencesUtils.getInt(this, Player.PREFERENCES_STATE, Player.REPEAT_NONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_now_playing_music, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(0);
        switch (iconIndex) {
            case 0:
                item.setIcon(R.mipmap.ic_queue_music_white_48dp);
                toastShow(getResources().getString(R.string.message_repeat_none));
                break;
            case 1:
                item.setIcon(R.mipmap.ic_repeat_white_48dp);
                toastShow(getResources().getString(R.string.message_repeat_all));
                break;
            case 2:
                item.setIcon(R.mipmap.ic_repeat_one_white_48dp);
                toastShow(getResources().getString(R.string.message_repeat_one));
                break;
            case 3:
                item.setIcon(R.mipmap.ic_shuffle_white_48dp);
                toastShow(getResources().getString(R.string.message_shuffle));
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_play_controller:
                iconIndex = (iconIndex + 1) % 4;
                invalidateOptionsMenu();
                PlayerController.togglePlayState(iconIndex);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        observer.stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(observer).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 初始化View
     */
    public void setupInstance() {
        mToolBar.setTitle("");
        mSeekBar = (SeekBar) findViewById(R.id.song_seekbar);
        mSongImg = (SimpleDraweeView) findViewById(R.id.song_image);
        mSongTitle = (TextView) findViewById(R.id.song_title);
        mSongInfo = (TextView) findViewById(R.id.song_info);
        mTogglePlay = (ImageView) findViewById(R.id.control_toggle_play);
        mNext = (ImageView) findViewById(R.id.control_next);
        mPrevious = (ImageView) findViewById(R.id.control_previous);
        mReflectedImage = (ImageView) findViewById(R.id.reflected_image);

        mSongTitle.setText(song.getmSongName());
        mSongInfo.setText(song.getmArtistName() + "|" + song.getmAblumName());
        Bitmap reflectedImage;
        if (FetchUtils.fetchAlbumArtLocal(song.getmAlbumId()) == null) {
            reflectedImage = MusicUtils.createReflectedImage(BitmapFactory.decodeResource(getResources(), R.mipmap.default_artwork));
        } else {
            mSongImg.setImageURI(FetchUtils.fetchArtByAlbumId(song.getmAlbumId()));
            reflectedImage = MusicUtils.createReflectedImage(FetchUtils.fetchAlbumArtLocal(song.getmAlbumId()));
        }
        mReflectedImage.setImageBitmap(reflectedImage);
        mTogglePlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(new SeekBarChangeListener());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.control_toggle_play:
                PlayerController.togglePlay();
                break;
            case R.id.control_next:
                PlayerController.next();
                observer.stop();
                mSeekBar.setMax(Integer.MAX_VALUE);
                mSeekBar.setProgress(Integer.MAX_VALUE);
                break;
            case R.id.control_previous:
                PlayerController.previous();
                mSeekBar.setMax(Integer.MAX_VALUE);
                mSeekBar.setProgress(0);
                break;
        }
    }

    /**
     * 更新UI
     */
    @Override
    public void update() {
        Player.Info info = PlayerController.getInfo();
        if (info != null) {
            Song song = PlayerController.getNowPlaying();
            if (song != null) {
                if (!song.equals(currentRef)) {
                    //当begin()的时候，此时的isPlaying为false
                    //歌曲处于正在播放时，并且seekbar没有启动，那么就启动它
                    //因为这里时第一次启动seekbar
                    LogUtils.v(TAG, info.isPlaying + "");
                    if (!info.isPlaying && !observer.isRunning()) {
                        new Thread(observer).start();
                    }
                    Bitmap reflectedImage;
                    if (FetchUtils.fetchAlbumArtLocal(song.getmAlbumId()) == null) {
                        reflectedImage = MusicUtils.createReflectedImage(BitmapFactory.decodeResource(getResources(), R.mipmap.default_artwork));
                    } else {
                        mSongImg.setImageURI(FetchUtils.fetchArtByAlbumId(song.getmAlbumId()));
                        reflectedImage = MusicUtils.createReflectedImage(FetchUtils.fetchAlbumArtLocal(song.getmAlbumId()));
                    }
                    mReflectedImage.setImageBitmap(reflectedImage);
                    mSongTitle.setText(song.getmSongName());
                    mSongInfo.setText(song.getmArtistName() + " | " + song.getmAblumName());
                    currentRef = song;
                }

                if (info.isPlaying) {
                    //正在播放时将togglebutton设置为暂停图片
                    mTogglePlay.setImageResource(R.mipmap.ic_pause_white_48dp);
                } else {
                    //在begin()的时候此时歌曲没有播放，为正在准备中，
                    //在此时将seekbar的最大值设置为歌曲的总长度, 进度设置为当前进度，由于准备中，当前进度为0
                    mSeekBar.setMax((int) PlayerController.getDuration());
                    mSeekBar.setProgress((int) PlayerController.getCurrentPosition());
                    mTogglePlay.setImageResource(R.mipmap.ic_play_arrow_white_48dp);
                }
            }
        }
    }

    class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        boolean touchingProgressBar = false;

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b && !touchingProgressBar) {
                onStartTrackingTouch(seekBar);
                onStopTrackingTouch(seekBar);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            observer.stop();
            touchingProgressBar = true;
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            PlayerController.seek(mSeekBar.getProgress());
            new Thread(observer).start();
            touchingProgressBar = false;
        }
    }

    //
    class SeekObserver implements Runnable {
        private boolean stop = false;

        @Override
        public void run() {
            stop = false;
            while (!stop) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSeekBar.setProgress((int) PlayerController.getCurrentPosition());
                    }
                });
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop() {
            LogUtils.v(TAG, "runnable stop ");
            stop = true;
        }

        public boolean isRunning() {
            return !stop;
        }
    }

    private void toastShow(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
