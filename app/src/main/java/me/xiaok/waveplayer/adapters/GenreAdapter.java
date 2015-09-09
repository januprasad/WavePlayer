package me.xiaok.waveplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Genre;
import me.xiaok.waveplayer.models.viewholders.GenreViewHolder;

/**
 * Created by GeeKaven on 15/8/24.
 */
public class GenreAdapter extends RecyclerView.Adapter<GenreViewHolder> {

  private ArrayList<Genre> mGenreList;

  public GenreAdapter(ArrayList<Genre> genreList) {
    this.mGenreList = genreList;
  }

  @Override public GenreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    GenreViewHolder holder = new GenreViewHolder(
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false));
    return holder;
  }

  @Override public void onBindViewHolder(GenreViewHolder holder, int position) {
    holder.updateViewHolder(mGenreList.get(position));
  }

  @Override public int getItemCount() {
    return mGenreList.size();
  }
}
