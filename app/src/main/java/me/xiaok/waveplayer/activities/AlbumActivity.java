package me.xiaok.waveplayer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.SongAdapter;
import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * 专辑详情Activity
 *
 * 用于显示专题的信息，内部的歌曲列表等
 *
 * Created by GeeKaven on 15/8/16.
 */
public class AlbumActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_ALBUM = "album_detail";
    private Album mAlbum;

    private RecyclerView mList;
    private FloatingActionButton mFabPlay;
    private ArrayList<Song> mSongList;
    private SongAdapter mAdapter;
    private LayoutManager mLayoutManager;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mAlbum = intent.getExtras().getParcelable(EXTRA_ALBUM);
        mSongList = LibManager.getAlbumSongs(mAlbum);

        setupAdapter();
        setupInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    private void setupAdapter() {
        mAdapter = new SongAdapter(mSongList, false);
        mLayoutManager = new LinearLayoutManager(this);
    }

    /**
     * 初始化View
     */
    private void setupInstance() {
        mList = (RecyclerView) findViewById(R.id.list);
        mFabPlay = (FloatingActionButton) findViewById(R.id.fab_play);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mAlbum.getmAlbumName());

        mList.setLayoutManager(mLayoutManager);
        mList.setAdapter(mAdapter);
        mFabPlay.setOnClickListener(this);

        loadBackDrop();
    }

    /**
     * 加载专辑图片
     */
    private void loadBackDrop() {
        SimpleDraweeView backDrop = (SimpleDraweeView) findViewById(R.id.backdrop);
        backDrop.setImageURI(Uri.parse("file://" + mAlbum.getmAlbumArt()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_play:
                //将此专辑下的所有歌曲添加到播放队列，并且播放
//                PlayerController.playAll(mSongList);
                Navigate.to(this, NowPlayingMusic.class, NowPlayingMusic.EXTRA_NOW_PLAYING, mSongList.get(0));
                break;
        }
    }
}
