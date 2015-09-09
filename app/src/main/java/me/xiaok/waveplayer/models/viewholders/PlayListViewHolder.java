package me.xiaok.waveplayer.models.viewholders;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.activities.PlayListActivity;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * Created by GeeKaven on 15/8/28.
 */
public class PlayListViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
  public static final String TAG = "PlayListViewHolder";

  private View itemView;
  private FrameLayout mRoot;
  private SimpleDraweeView mPlayListImg;
  private ImageView mClickMore;
  private TextView mPlayListName;
  private TextView mPlayListInfo;
  private PlayList ref;
  private Context context;
  //在本类型下的所有歌曲
  private ArrayList<Song> mSongList;

  private PlayListRemoveListener listener;

  public PlayListViewHolder(View itemView) {
    super(itemView);
    this.itemView = itemView;
    context = itemView.getContext();

    mRoot = (FrameLayout) itemView.findViewById(R.id.root);
    mPlayListImg = (SimpleDraweeView) itemView.findViewById(R.id.back_img);
    mClickMore = (ImageView) itemView.findViewById(R.id.click_more);
    mPlayListName = (TextView) itemView.findViewById(R.id.item_title);
    mPlayListInfo = (TextView) itemView.findViewById(R.id.item_text);
    mPlayListImg.setAspectRatio(1.0f);

    mRoot.setOnClickListener(this);
    mClickMore.setOnClickListener(this);
  }

  public void updateViewHolder(PlayList playList) {
    ref = playList;

    if (mSongList == null) {
      mSongList = LibManager.getPlayListSongs(itemView.getContext(), playList);
    }

    if (mSongList.size() > 0) {
      mPlayListImg.setImageURI(FetchUtils.fetchArtByAlbumId(mSongList.get(0).getmAlbumId()));
    }

    mPlayListName.setText(playList.getmPlayListName());
    mPlayListInfo.setText(mSongList.size() + " songs");
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.root:
        Intent intent = new Intent(itemView.getContext(), PlayListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PlayListActivity.EXTRA_PLAYLIST, ref);
        bundle.putParcelableArrayList(PlayListActivity.EXTRA_SONG_LIST, mSongList);
        intent.putExtras(bundle);
        itemView.getContext().startActivity(intent);
        break;
      case R.id.click_more:
        LogUtils.v(TAG, "more click");
        PopupMenu popupMenu = new PopupMenu(itemView.getContext(), mClickMore, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_playlist, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
        break;
    }
  }

  @Override public boolean onMenuItemClick(MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.delete:
        if (listener != null) {
          new AsyncTask<Void, Void, PlayList>() {
            @Override protected PlayList doInBackground(Void... voids) {
              return LibManager.deletePlaylist(context, ref);
            }

            @Override protected void onPostExecute(PlayList playList) {
              if (playList != null) {
                listener.playListRemoved(playList);
                Toast.makeText(context,
                    String.format(context.getString(R.string.message_removed_playlist),
                        playList.getmPlayListName()), Toast.LENGTH_SHORT).show();
              }
            }
          }.execute();
        }
        return true;
    }
    return false;
  }

  public void setPlayListRemoveListener(PlayListRemoveListener listener) {
    this.listener = listener;
  }

  public interface PlayListRemoveListener {
    void playListRemoved(PlayList playList);
  }
}
