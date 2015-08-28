package me.xiaok.waveplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Artist;
import me.xiaok.waveplayer.models.viewholders.ArtistViewHolder;

/**
 * 艺术家适配器
 * <p/>
 * Created by GeeKaven on 15/8/16.
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistViewHolder> {

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
                .inflate(R.layout.item_grid, parent, false));
        return holder;
    }

    @Override
    public int getItemCount() {
        return mArtistList.size();
    }
}
