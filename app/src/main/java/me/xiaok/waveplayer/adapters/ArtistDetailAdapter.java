package me.xiaok.waveplayer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.models.viewholders.AlbumViewHolder;
import me.xiaok.waveplayer.models.viewholders.SongViewHolder;

/**
 * 艺术家详细RecyclerView适配器, 有艺术家图片（从Last.fm获取）,本地艺术家专辑，艺术家歌曲
 * Created by GeeKaven on 15/8/18.
 */
public class ArtistDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  //保存艺术家歌曲，专辑引用
  private ArrayList<Song> mSongList;
  private ArrayList<Album> mAlbumList;
  private Context mContext;

  public static final int HEAD_VIEW = 0;
  public static final int ALBUM_VIEW = 1;
  public static final int SONG_VIEW = 2;

  public ArtistDetailAdapter(Context context, ArrayList<Album> albumList,
      ArrayList<Song> songList) {
    this.mContext = context;
    this.mAlbumList = albumList;
    this.mSongList = songList;
  }

  @Override public int getItemCount() {
    return mSongList.size() + mAlbumList.size() + 2;
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    switch (getItemViewType(position)) {
      case HEAD_VIEW:
        ((HeadViewHolder) holder).update(
            position == 0 ? mContext.getResources().getString(R.string.album)
                : mContext.getResources().getString(R.string.song));
        break;
      case ALBUM_VIEW:
        ((AlbumViewHolder) holder).updateViewHolder(mAlbumList.get(position - 1));
        break;
      case SONG_VIEW:
        ((SongViewHolder) holder).updateViewHolder(mSongList.get(position - mAlbumList.size() - 2),
            false, position - mAlbumList.size() - 2);
        break;
    }
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    switch (viewType) {
      case HEAD_VIEW:
        return new HeadViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_head, parent, false));
      case ALBUM_VIEW:
        return new AlbumViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false));
      case SONG_VIEW:
        SongViewHolder holder = new SongViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false));
        holder.setSongList(mSongList);
        return holder;
    }
    return null;
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return HEAD_VIEW;
    } else if (position <= mAlbumList.size()) {
      return ALBUM_VIEW;
    } else if (position == mAlbumList.size() + 1) {
      return HEAD_VIEW;
    } else {
      return SONG_VIEW;
    }
  }

  class HeadViewHolder extends RecyclerView.ViewHolder {
    private TextView mHead;

    public HeadViewHolder(View itemView) {
      super(itemView);
      mHead = (TextView) itemView.findViewById(R.id.head_text);
    }

    public void update(String title) {
      mHead.setText(title);
    }
  }
}
