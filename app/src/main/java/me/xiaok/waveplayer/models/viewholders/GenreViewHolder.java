package me.xiaok.waveplayer.models.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
import me.xiaok.waveplayer.PlayerController;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.activities.GenreActivity;
import me.xiaok.waveplayer.models.Genre;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.FetchUtils;
import me.xiaok.waveplayer.utils.Navigate;

/**
 * Created by GeeKaven on 15/8/24.
 */
public class GenreViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

  public static final String TAG = "GenreViewHolder";

  private View itemView;
  private FrameLayout mRoot;
  private SimpleDraweeView mGenreImg;
  private ImageView mClickMore;
  private TextView mGenreName;
  private TextView mGenreInfo;
  private Genre ref;
  private Context context;
  //在本类型下的所有歌曲
  private ArrayList<Song> mSongList;

  public GenreViewHolder(View itemView) {
    super(itemView);
    this.itemView = itemView;
    context = itemView.getContext();
    mRoot = (FrameLayout) itemView.findViewById(R.id.root);
    mGenreImg = (SimpleDraweeView) itemView.findViewById(R.id.back_img);
    mClickMore = (ImageView) itemView.findViewById(R.id.click_more);
    mGenreName = (TextView) itemView.findViewById(R.id.item_title);
    mGenreInfo = (TextView) itemView.findViewById(R.id.item_text);
    mGenreImg.setAspectRatio(1.0f);

    mRoot.setOnClickListener(this);
    mClickMore.setOnClickListener(this);
  }

  public void updateViewHolder(Genre genre) {
    ref = genre;

    if (mSongList == null) {
      mSongList = LibManager.getGenreSongs(genre);
    }
    if (mSongList.size() > 0) {
      mGenreImg.setImageURI(FetchUtils.fetchArtByAlbumId(mSongList.get(0).getmAlbumId()));
    }
    mGenreName.setText(genre.getmGenreName());
    mGenreInfo.setText(mSongList.size() + "首歌曲");
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.root:
        Navigate.to(itemView.getContext(), GenreActivity.class, GenreActivity.EXTRA_GENRE, ref);
        break;
      case R.id.click_more:
        PopupMenu popupMenu = new PopupMenu(itemView.getContext(), mClickMore, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_genre, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
        break;
    }
  }

  @Override public boolean onMenuItemClick(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.play_all:
        //                PlayerController.playAll(mSongList);
        return true;
      case R.id.add_queue:
        //                PlayerController.addQueue(mSongList);
        return true;
      case R.id.add_playlist:
        final ArrayList<PlayList> playLists = LibManager.getPlayLists();
        String[] names = new String[playLists.size()];
        for (int i = 0; i < playLists.size(); i++) {
          names[i] = playLists.get(i).getmPlayListName();
        }

        AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
            context.getString(R.string.add_to_playlist_title))
            .setItems(names, new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialogInterface, final int i) {
                new AsyncTask<Void, Void, Void>() {
                  @Override protected Void doInBackground(Void... voids) {
                    LibManager.addSongListToPlaylist(context, playLists.get(i),
                        LibManager.getGenreSongs(ref));
                    return null;
                  }

                  @Override protected void onPostExecute(Void aVoid) {
                    Toast.makeText(context, context.getString(R.string.message_add_to_playlist),
                        Toast.LENGTH_SHORT).show();
                  }
                }.execute();
              }
            })
            .setNegativeButton(context.getString(R.string.action_cancel),
                new DialogInterface.OnClickListener() {
                  @Override public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                  }
                })
            .show();
        return true;
    }
    return false;
  }
}
