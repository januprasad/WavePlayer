package me.xiaok.waveplayer.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.ArtistDetailAdapter;
import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.Artist;
import me.xiaok.waveplayer.models.FmArtist;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.request.GsonRequest;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * 歌手详情Activity
 * <p/>
 * 显示歌手信息，本地专辑，所有歌曲
 * <p/>
 * Created by GeeKaven on 15/8/16.
 */
public class ArtistActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_ARTIST = "artist_detail";
    public static final String TAG = "ArtistActivity";
    private Artist mArtist;

    private RecyclerView mList;
    private FloatingActionButton mFabPlay;
    private ArtistDetailAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private ArrayList<Song> mSongList;
    private ArrayList<Album> mAlbumList;

    private static final String API_KEY = "692515bb0a1d5a21a327cf0901674370";
    private static final String url = "http://ws.audioscrobbler.com/2.0/";
    private static final String parmar = "?method=artist.getInfo&lang=zh&artist=";

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mArtist = intent.getExtras().getParcelable(EXTRA_ARTIST);

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
        try {
            loadBackDrop();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载专辑图片
     */
    private void loadBackDrop() throws UnsupportedEncodingException {
        final SimpleDraweeView backDrop = (SimpleDraweeView) findViewById(R.id.backdrop);
        if (mArtist.getmArtistName().equalsIgnoreCase(this.getString(R.string.no_artist)))
            return;

        String artistName = URLEncoder.encode(mArtist.getmArtistName(), "UTF-8");
        LogUtils.v(TAG, artistName);
        String urlFormat = String.format("%s?method=%s&lang=%s&artist=%s&format=json&api_key=%s"
                ,url,"artist.getInfo", "zh", artistName,API_KEY);
        ConnectivityManager network = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (network.getActiveNetworkInfo() != null &&
                network.getActiveNetworkInfo().isAvailable() && !network.getActiveNetworkInfo().isRoaming()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            GsonRequest<FmArtist> request = new GsonRequest<>(urlFormat, FmArtist.class,
                    new Response.Listener<FmArtist>() {
                        @Override
                        public void onResponse(FmArtist response) {
                            backDrop.setImageURI(Uri.parse(response.getArtist().getImage().get(4).getImageUrl()));
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
            queue.add(request);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_play:
//                PlayerController.playAll(mSongList);
                Navigate.to(this, NowPlayingMusic.class, NowPlayingMusic.EXTRA_NOW_PLAYING, mSongList.get(0));
                break;
        }
    }
}
