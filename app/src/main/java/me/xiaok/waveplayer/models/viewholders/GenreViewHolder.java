package me.xiaok.waveplayer.models.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.activities.GenreActivity;
import me.xiaok.waveplayer.models.Genre;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * Created by GeeKaven on 15/8/24.
 */
public class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public static final String TAG = "GenreViewHolder";

    private View itemView;
    private FrameLayout mRoot;
    private SimpleDraweeView mGenreImg;
    private ImageView mClickImg;
    private TextView mGenreName;
    private TextView mGenreInfo;
    private Genre ref;
    //在本类型下的所有歌曲
    private ArrayList<Song> mSongList;

    public GenreViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;

        mRoot = (FrameLayout) itemView.findViewById(R.id.root);
        mGenreImg = (SimpleDraweeView) itemView.findViewById(R.id.genre_img);
        mClickImg = (ImageView) itemView.findViewById(R.id.click_more);
        mGenreName = (TextView) itemView.findViewById(R.id.genre_name);
        mGenreInfo = (TextView) itemView.findViewById(R.id.genre_info);
        mGenreImg.setAspectRatio(1.0f);

        mRoot.setOnClickListener(this);
        mClickImg.setOnClickListener(this);
    }

    public void updateViewHolder(Genre genre) {
        ref = genre;

        if (mSongList == null) {
            mSongList = LibManager.getGenreSongs(genre);
        }
        if (mSongList.size() > 0) {
            mGenreImg.setImageURI(FetchUtils.fetchArtByAlbumId(mSongList.get(0).getmAlbumId()));
        }
        mGenreName.setText(genre.getmGenreName());
        mGenreInfo.setText(mSongList.size() + "首歌曲");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.root:
                Navigate.to(itemView.getContext(), GenreActivity.class, GenreActivity.EXTRA_GENRE, ref);
                break;
            case R.id.click_more:
                break;
        }
    }
}
