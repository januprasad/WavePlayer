package me.xiaok.waveplayer.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import de.umass.lastfm.ImageSize;
import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.PlayerController;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.ArtistDetailAdapter;
import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.Artist;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * 歌手详情Activity
 *
 * 显示歌手信息，本地专辑，所有歌曲
 *
 * Created by GeeKaven on 15/8/16.
 */
public class ArtistActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_ALBUM = "artist_detail";
    public static final String TAG = "ArtistActivity";
    private Artist mArtist;

    private RecyclerView mList;
    private FloatingActionButton mFabPlay;
    private ArtistDetailAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private ArrayList<Song> mSongList;
    private ArrayList<Album> mAlbumList;
    private de.umass.lastfm.Artist artist;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail_artist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mArtist = intent.getExtras().getParcelable(EXTRA_ALBUM);

        mAlbumList = LibManager.getArtistAlbums(mArtist);
        mSongList = LibManager.getArtistSong(mArtist);

        setupAdapter();
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

    private void setupAdapter() {
        mAdapter = new ArtistDetailAdapter(this, mAlbumList, mSongList);
        mLayoutManager = new GridLayoutManager(this, 2);
        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == ArtistDetailAdapter.HEAD_VIEW)
                    return 2;
                else if (mAdapter.getItemViewType(position) == ArtistDetailAdapter.SONG_VIEW)
                    return 2;
                else
                    return 1;

            }
        };
        spanSizeLookup.setSpanIndexCacheEnabled(true);
        mLayoutManager.setSpanSizeLookup(spanSizeLookup);
    }

    /**
     * 初始化View
     */
    private void setupInstance() {
        mList = (RecyclerView) findViewById(R.id.list);
        mFabPlay = (FloatingActionButton) findViewById(R.id.fab_play);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mArtist.getmArtistName());

        mList.setLayoutManager(mLayoutManager);
        mList.setAdapter(mAdapter);

        mFabPlay.setOnClickListener(this);
        loadBackDrop();
    }

    /**
     * 加载专辑图片
     */
    private void loadBackDrop() {
        final SimpleDraweeView backDrop = (SimpleDraweeView) findViewById(R.id.backdrop);
        backDrop.setImageURI(Uri.parse("res:///" + R.mipmap.text_img));

//        new AsyncTask<Artist, Void, Void>() {
//            @Override
//            protected Void doInBackground(Artist... artists) {
//                artist = FetchUtils.fetchArtistInfo(ArtistActivity.this, artists[0].getmArtistName());
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                if (artist != null) {
//                    backDrop.setImageURI(Uri.parse(artist.getImageURL(ImageSize.MEGA)));
//                }
//            }
//        }.execute(mArtist);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_play:
                PlayerController.setQueueAndPosition(mSongList, 0);
                Navigate.to(this, NowPlayingMusic.class, NowPlayingMusic.EXTRA_ALBUM, mSongList.get(0));
                break;
        }
    }
}
