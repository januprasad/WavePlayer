package me.xiaok.waveplayer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.GenreAdapter;

/**
 * Created by GeeKaven on 15/8/24.
 */
public class GenreFragment extends Fragment {

    private RecyclerView mList;
    private GenreAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.com_list, container, false);
        mList = (RecyclerView) view.findViewById(R.id.list);

        mAdapter = new GenreAdapter(LibManager.getGenres());
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);

        mList.setLayoutManager(layoutManager);
        mList.setAdapter(mAdapter);
        return view;
    }
}
