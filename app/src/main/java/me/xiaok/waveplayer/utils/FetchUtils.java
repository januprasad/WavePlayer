package me.xiaok.waveplayer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.Song;

/**
 * Created by GeeKaven on 15/8/18.
 */
public class FetchUtils {

  private static final String TAG = "FetchUtils";

  /**
   * 根据专辑ID,获取到Art的URI
   */
  public static Uri fetchArtByAlbumId(int albumId) {
    ArrayList<Album> albums = LibManager.getAlbums();
    Uri uri = null;
    for (Album a : albums) {
      if (a.getmAlbumId() == albumId) {
        uri = Uri.parse("file://" + a.getmAlbumArt());
      }
    }
    return uri;
  }

  /**
   * 根据专辑ID,获取到Art,转换为Bitmap;
   */
  public static Bitmap fetchAlbumArtLocal(int albumId) {
    ArrayList<Album> albums = LibManager.getAlbums();
    for (Album a : albums) {
      if (a.getmAlbumId() == albumId) {
        if (a.getmAlbumArt() != null) {
          return BitmapFactory.decodeFile(a.getmAlbumArt());
        } else {
          return null;
        }
      }
    }
    return null;
  }

  /**
   * 取得媒体文件信息，主要是得到专辑封面
   */
  public static Bitmap fetchFullArt(Song song) {
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    try {
      retriever.setDataSource(song.getmSongPath());
      byte[] stream = retriever.getEmbeddedPicture();
      if (stream != null) return BitmapFactory.decodeByteArray(stream, 0, stream.length);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
