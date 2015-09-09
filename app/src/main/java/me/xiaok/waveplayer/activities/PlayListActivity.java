package me.xiaok.waveplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import me.xiaok.waveplayer.PlayerController;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.SongAdapter;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * Created by GeeKaven on 15/8/28.
 */
public class PlayListActivity extends BaseActivity implements View.OnClickListener{

    public static final String TAG = "PlayListActivity";
    public static final String EXTRA_PLAYLIST = "PlayList";
    public static final String EXTRA_SONG_LIST = "SongList";

    private RecyclerView mList;
    private FloatingActionButton mFabPlay;
    private ArrayList<Song> mSongList;
    private SongAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private PlayList mPlayList;
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPlayList = intent.getExtras().getParcelable(EXTRA_PLAYLIST);
        mSongList = intent.getExtras().getParcelableArrayList(EXTRA_SONG_LIST);

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

    private void setupInstance() {
        mList = (RecyclerView) findViewById(R.id.list);
        mFabPlay = (FloatingActionButton) findViewById(R.id.fab_play);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mPlayList.getmPlayListName());

        mList.setLayoutManager(mLayoutManager);
        mList.setAdapter(mAdapter);
        mFabPlay.setOnClickListener(this);

        loadBackDrop();
    }

    private void loadBackDrop() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_play:
                //将此专辑下的所有歌曲添加到播放队列，并且播放
                PlayerController.setQueueAndPosition(mSongList, 0);
                Navigate.to(this, NowPlayingMusic.class, NowPlayingMusic.EXTRA_NOW_PLAYING, mSongList.get(0));
                break;
        }
    }
}
