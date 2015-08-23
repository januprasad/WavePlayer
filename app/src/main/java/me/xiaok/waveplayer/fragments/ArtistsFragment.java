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
        mAdapter = new ArtistAdapter(new ArrayList<Artist>());
        mLayoutManager = new GridLayoutManager(getActivity(),2);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mList = (RecyclerView) view.findViewById(R.id.list);
        mList.setLayoutManager(mLayoutManager);
        mList.setAdapter(mAdapter);
        loadArtists();
        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 由于点击DrawerLayout的选项后，加载列表(数量有点多)，设置列表消耗一定时间
     * 会产生明显的卡顿现象，为了避免，就用了AsyncTask在后台加载，然后在显示
     * 但是，不sleep的话还是会有卡顿，因此就sleep一会儿，等侧边栏滑回后在显示
     */
    private void loadArtists() {
        new AsyncTask<Void, Void, ArrayList<Artist>>() {
            @Override
            protected ArrayList<Artist> doInBackground(Void... voids) {
                try {
                    Thread.sleep(280);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return LibManager.getArtists();
            }

            @Override
            protected void onPostExecute(ArrayList<Artist> artists) {
                super.onPostExecute(artists);
                mAdapter.setmArtistList(artists);
            }
        }.execute();
    }
}
