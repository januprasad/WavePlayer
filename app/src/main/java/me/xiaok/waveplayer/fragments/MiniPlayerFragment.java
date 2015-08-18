package me.xiaok.waveplayer.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Song;

/**
 * 下方音乐控制器
 *
 * Created by GeeKaven on 15/8/17.
 */
public class MiniPlayerFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout mRoot;
    private ImageView mPlay;
    private ImageView mNext;

    private SimpleDraweeView mSongImg;
    private TextView mSongTitle;
    private TextView mSongArtist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mini_player, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mSongImg = (SimpleDraweeView)view.findViewById(R.id.song_image);
        mSongTitle = (TextView)view.findViewById(R.id.song_title);
        mSongArtist = (TextView)view.findViewById(R.id.song_artist);

        mRoot = (RelativeLayout) view.findViewById(R.id.root);
        mPlay = (ImageView)view.findViewById(R.id.mini_play);
        mNext = (ImageView)view.findViewById(R.id.mini_next);

        mRoot.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mNext.setOnClickListener(this);
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateFragment(Song s) {
        mSongTitle.setText(s.getmSongName());
        mSongArtist.setText(s.getmArtistName());
    }

    @Override
    public void onClick(View view) {

    }
}
