package me.xiaok.waveplayer.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.activities.ArtistActivity;
import me.xiaok.waveplayer.models.Artist;
import me.xiaok.waveplayer.utils.LogUtils;
import me.xiaok.waveplayer.utils.MusicUtil;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * 艺术家适配器
 * <p/>
 * Created by GeeKaven on 15/8/16.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    private ArrayList<Artist> mArtistList;

    public ArtistAdapter(ArrayList<Artist> artistList) {
        this.mArtistList = artistList;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        holder.updateViewHolder(mArtistList.get(position));
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ArtistViewHolder holder = new ArtistViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artist, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final String TAG = "AlbumViewHolder";

        private View itemView;
        private FrameLayout mRoot;
        private SimpleDraweeView mArtistImg;
        private ImageView mClickImg;
        private TextView mArtistName;
        private TextView mAlbumNum;
        private Artist ref;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            mRoot = (FrameLayout) itemView.findViewById(R.id.root);
            mArtistImg = (SimpleDraweeView) itemView.findViewById(R.id.artist_img);
            mClickImg = (ImageView) itemView.findViewById(R.id.click_more);
            mArtistName = (TextView) itemView.findViewById(R.id.artist_name);
            mAlbumNum = (TextView) itemView.findViewById(R.id.album_num);
            mArtistImg.setAspectRatio(1.0f);

            mRoot.setOnClickListener(this);
            mClickImg.setOnClickListener(this);
        }

        /**
         * 更新ViewHolder
         *
         * @param artist
         */
        public void updateViewHolder(Artist artist) {
            ref = artist;
            mArtistName.setText(artist.getmArtistName());
            mAlbumNum.setText(artist.getmAlbumNum() + " album");
            mArtistImg.setImageURI(Uri.parse("file://" + LibManager.getArtistAlbums(artist).get(0).getmAlbumArt()));
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.click_more:
                    LogUtils.v(TAG, "more click");
                    break;
                case R.id.root:
                    LogUtils.v(TAG, ref.toString());
                    Navigate.to(itemView.getContext(), ArtistActivity.class, ArtistActivity.EXTRA_ALBUM, ref);
                    break;
            }
        }
    }
}
