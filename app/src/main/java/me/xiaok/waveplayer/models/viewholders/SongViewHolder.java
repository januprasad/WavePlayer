package me.xiaok.waveplayer.models.viewholders;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

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
            mSongImage.setImageURI(Uri.parse("file://" + LibManager.getSongAlbum(s).get(0).getmAlbumArt()));
        } else {
            mSongImage.setVisibility(View.GONE);
            mRoot.setPadding(12,5,12,5);
        }
    }

    /**
     * 设置本歌曲所在的歌曲列表
     * @param songList
     */
    public void setSongList(ArrayList songList) {
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
                //PlayerController.setQueueAndPosition(mSongList, position);
                Navigate.to(itemView.getContext(), NowPlayingMusic.class, NowPlayingMusic.EXTRA_ALBUM, ref);
                break;
            case R.id.click_more:
                LogUtils.v(TAG, "more click");
                break;
        }
    }
}
