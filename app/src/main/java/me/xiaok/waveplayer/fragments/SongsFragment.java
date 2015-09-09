package me.xiaok.waveplayer.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.adapters.SongAdapter;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * Created by GeeKaven on 15/8/16.
 */
public class SongsFragment extends Fragment {

  private static final String TAG = "SongsFragment";
  private RecyclerView mList;
  private SongAdapter mAdapter;

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    LogUtils.v(TAG, "SongsFragment onCreateView is called");

    View view = inflater.inflate(R.layout.com_list, container, false);
    mList = (RecyclerView) view.findViewById(R.id.list);

    mAdapter = new SongAdapter(LibManager.getSongs());
    LayoutManager layoutManager = new LinearLayoutManager(getActivity());

    mList.setLayoutManager(layoutManager);
    mList.setAdapter(mAdapter);
    return view;
  }
}
