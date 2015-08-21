package me.xiaok.waveplayer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Song;

/**
 * 正在播放音乐界面
 * Created by GeeKaven on 15/8/20.
 */
public class NowPlayingMusic extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_ALBUM = "NowPlayingMusic";

    private Song song;
    private SimpleDraweeView mSongImg;
    private TextView mSongTitle;
    private TextView mSongInfo;

    private ImageView mTogglePlay;
    private ImageView mNext;
    private ImageView mPrevious;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_now_playing;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        song = intent.getExtras().getParcelable(EXTRA_ALBUM);

        setupInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化View
     */
    public void setupInstance() {
        mToolBar.setTitle("");
        mSongImg = (SimpleDraweeView) findViewById(R.id.song_image);
        mSongTitle = (TextView) findViewById(R.id.song_title);
        mSongInfo = (TextView) findViewById(R.id.song_info);
        mTogglePlay = (ImageView) findViewById(R.id.control_toggle_play);
        mNext = (ImageView) findViewById(R.id.control_next);
        mPrevious = (ImageView) findViewById(R.id.control_previous);

        mSongImg.setAspectRatio(1.0f);
        mSongImg.setImageURI(Uri.parse("res:///" + R.mipmap.text_img));
        mSongTitle.setText(song.getmSongName());
        mSongInfo.setText(song.getmArtistName() + "|" + song.getmAblumName());

        mTogglePlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.control_toggle_play:
                break;
            case R.id.control_next:
                break;
            case R.id.control_previous:
                break;
        }
    }

    /**
     * 更新UI
     */
    @Override
    public void update() {

    }
}