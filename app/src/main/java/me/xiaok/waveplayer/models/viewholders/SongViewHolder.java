package me.xiaok.waveplayer.models.viewholders;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.Player;
import me.xiaok.waveplayer.PlayerController;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.activities.NowPlayingMusic;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.MusicUtils;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * Created by GeeKaven on 15/8/18.
 */
public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

    private static final String TAG = "SongViewHolder";
    private RelativeLayout mRoot;
    private SimpleDraweeView mSongImage;
    private TextView mSongTitle;
    private TextView mSongArtist;
    private TextView mSongDuration;
    private ImageView mClickMore;

    private Song ref;
    private int position;
    private ArrayList<Song> mSongList;

    public SongViewHolder(View itemView) {
        super(itemView);
        mRoot = (RelativeLayout) itemView.findViewById(R.id.root);
        mSongImage = (SimpleDraweeView)itemView.findViewById(R.id.song_image);
        mSongTitle = (TextView) itemView.findViewById(R.id.song_title);
        mSongArtist = (TextView) itemView.findViewById(R.id.song_artist);
        mSongDuration = (TextView) itemView.findViewById(R.id.song_duration);
        mClickMore = (ImageView) itemView.findViewById(R.id.click_more);

        mRoot.setOnClickListener(this);
        mClickMore.setOnClickListener(this);
    }

    public void updateViewHolder(Song s, boolean flag, int pos) {
        ref = s;
        position = pos;

        mSongTitle.setText(s.getmSongName());
        mSongArtist.setText(s.getmArtistName());
        mSongDuration.setText(MusicUtils.durationToString(s.getmDuration()));

        if (flag) {
            if (LibManager.getSongAlbum(s).get(0).getmAlbumArt() == null) {
                mSongImage.setImageURI(Uri.parse("res:///" + R.mipmap.default_artwork));
            } else {
                mSongImage.setImageURI(Uri.parse("file://" + LibManager.getSongAlbum(s).get(0).getmAlbumArt()));

            }
        } else {
            mSongImage.setVisibility(View.GONE);
            mRoot.setPadding(12,5,12,5);
        }
    }

    /**
     * 设置本歌曲所在的歌曲列表
     * @param songList
     */
    public void setSongList(ArrayList<Song> songList) {
        mSongList = songList;
    }

    /**
     * 处理点击Item点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.root:
                LogUtils.v(TAG, ref.getmSongName());
                PlayerController.setQueueAndPosition(mSongList, position);
                Navigate.to(itemView.getContext(), NowPlayingMusic.class, NowPlayingMusic.EXTRA_NOW_PLAYING, ref);
                break;
            case R.id.click_more:
                LogUtils.v(TAG, "more click");
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), mClickMore, Gravity.END);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu_song, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(this);
                popupMenu.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_queue:
                //添加到队列
                break;
            case R.id.add_playlist:
                //添加到播放列表
                break;
            case R.id.set_ring:
                //设置为铃声
                break;
            case R.id.delete:
                //删除
                break;
        }
        return true;
    }
}
