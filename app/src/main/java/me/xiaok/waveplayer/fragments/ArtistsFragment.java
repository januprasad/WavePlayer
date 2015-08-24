package me.xiaok.waveplayer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.AlbumAdapter;
import me.xiaok.waveplayer.adapters.ArtistAdapter;
import me.xiaok.waveplayer.models.Artist;

/**
 * Created by GeeKaven on 15/8/16.
 */
public class ArtistsFragment extends Fragment {

    private RecyclerView mList;
    private ArtistAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.com_list, container, false);
        mList = (RecyclerView) view.findViewById(R.id.list);

        mAdapter = new ArtistAdapter(LibManager.getArtists());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        mList.setLayoutManager(layoutManager);
        mList.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
