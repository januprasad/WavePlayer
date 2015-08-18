package me.xiaok.waveplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.models.viewholders.SongViewHolder;

/**
 * Created by GeeKaven on 15/8/18.
 */
public class SongAdapter extends RecyclerView.Adapter<SongViewHolder> {

    private ArrayList<Song> mSongList;
    private boolean showIcon = true;

    public SongAdapter(ArrayList<Song> songList) {
        this.mSongList = songList;
    }

    public SongAdapter(ArrayList<Song> songList, boolean flag) {
        this.mSongList = songList;
        this.showIcon = flag;
    }

    @Override
    public int getItemCount() {
        return mSongList.size();
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song s = mSongList.get(position);
        holder.updateViewHolder(s, showIcon);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SongViewHolder holder = new SongViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false));
        return holder;
    }
}
