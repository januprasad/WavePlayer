package me.xiaok.waveplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.models.viewholders.PlayListViewHolder;

/**
 * Created by GeeKaven on 15/8/28.
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListViewHolder> {

    private ArrayList<PlayList> mPlayLists;

    public PlayListAdapter(ArrayList<PlayList> playLists) {
        this.mPlayLists = playLists;
    }

    @Override
    public PlayListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);
        return new PlayListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayListViewHolder holder, int position) {
        holder.updateViewHolder(mPlayLists.get(position));
    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }
}
