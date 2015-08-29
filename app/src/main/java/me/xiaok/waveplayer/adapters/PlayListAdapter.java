package me.xiaok.waveplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.models.viewholders.PlayListViewHolder;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * Created by GeeKaven on 15/8/28.
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListViewHolder>{

    private static final String TAG = "PlayListAdapter";
    private ArrayList<PlayList> mPlayLists;

    public PlayListAdapter(ArrayList<PlayList> playLists) {
        this.mPlayLists = new ArrayList<>(playLists);
    }

    @Override
    public PlayListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PlayListViewHolder holder = new PlayListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false));
        holder.setPlayListRemoveListener(new PlayListViewHolder.PlayListRemoveListener() {
            @Override
            public void playListRemoved(PlayList playList) {
                notifyPlayListRemove(playList);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(PlayListViewHolder holder, int position) {
        holder.updateViewHolder(mPlayLists.get(position));
    }

    @Override
    public int getItemCount() {
        return mPlayLists.size();
    }


    public void notifyPlayListAdd(PlayList added) {
        mPlayLists = new ArrayList<>(LibManager.getPlayLists());
        if (mPlayLists.contains(added)) {
            notifyItemInserted(mPlayLists.indexOf(added));
        }
    }

    public void notifyPlayListRemove(PlayList removed) {
        if (mPlayLists.contains(removed))
            notifyItemRemoved(mPlayLists.indexOf(removed));

        mPlayLists = new ArrayList<>(LibManager.getPlayLists());
    }
}
