package me.xiaok.waveplayer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.util.ArrayList;

import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.models.Album;

/**
 * Created by GeeKaven on 15/8/18.
 */
public class FetchUtils {

    /**
     * 根据专辑ID,获取到Art的URI
     * @param albumId
     * @return
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
     * @param albumId
     * @return
     */
    public static Bitmap fetchAlbumArtLocal(int albumId) {
        ArrayList<Album> albums = LibManager.getAlbums();
        for (Album a : albums) {
            if (a.getmAlbumId() == albumId) {
                return BitmapFactory.decodeFile(a.getmAlbumArt());
            }
        }
        return null;
    }
}
