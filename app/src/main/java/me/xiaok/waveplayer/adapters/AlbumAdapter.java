package me.xiaok.waveplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.viewholders.AlbumViewHolder;

/**
 * 专辑适配器
 *
 * Created by GeeKaven on 15/8/16.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumViewHolder>{

    private ArrayList<Album> mAlbumList;

    public AlbumAdapter(ArrayList<Album> albumList) {
        mAlbumList = albumList;
    }

    public void setmAlbumList(ArrayList<Album> albumList) {
        mAlbumList = albumList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAlbumList.size();
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        holder.updateViewHolder(mAlbumList.get(position));
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AlbumViewHolder holder = new AlbumViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false));
        return holder;
    }


}
