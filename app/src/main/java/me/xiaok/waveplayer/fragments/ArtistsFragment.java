package me.xiaok.waveplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.AlbumAdapter;
import me.xiaok.waveplayer.adapters.ArtistAdapter;

/**
 * Created by GeeKaven on 15/8/16.
 */
public class ArtistsFragment extends Fragment {

    private RecyclerView mList;
    private ArtistAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.com_list, container, false);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArtistAdapter(LibManager.getArtists());
        mLayoutManager = new GridLayoutManager(getActivity(),2);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mList = (RecyclerView) view.findViewById(R.id.list);
        mList.setLayoutManager(mLayoutManager);
        mList.setAdapter(mAdapter);
        super.onViewCreated(view, savedInstanceState);
    }
}
