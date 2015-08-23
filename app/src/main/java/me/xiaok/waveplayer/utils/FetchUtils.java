package me.xiaok.waveplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

import de.umass.lastfm.Artist;
import de.umass.lastfm.Caller;
import de.umass.lastfm.cache.FileSystemCache;
import me.xiaok.waveplayer.BuildConfig;
import me.xiaok.waveplayer.LibManager;
import me.xiaok.waveplayer.R;
import me.xiaok.waveplayer.models.Album;

/**
 * Created by GeeKaven on 15/8/18.
 */
public class FetchUtils {


    private static final String TAG = "FetchUtils";
    // API key for Last.fm. Please use your own.
    private static final String API_KEY = "a9fc65293034b84b83d20c6e2ecda4b5";
    private static boolean lastFmInitialized = false;

    public static void initLastFm(Context context) {
        Caller.getInstance().setCache(new FileSystemCache(new File(context.getExternalCacheDir() + "/lastfm/")));
        Caller.getInstance().setUserAgent("Wave/" + BuildConfig.VERSION_NAME);

        lastFmInitialized = true;
    }

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

    /**
     * 从Last.fm获取艺术家信息
     * @param context
     * @param artistName
     * @return
     */
    public static Artist fetchArtistInfo(Context context, String artistName) {
        if (artistName.equalsIgnoreCase(context.getString(R.string.no_artist)))
            return null;
        if (!lastFmInitialized)
            initLastFm(context);
        ConnectivityManager network = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(network.getActiveNetworkInfo() != null &&
                network.getActiveNetworkInfo().isAvailable() && !network.getActiveNetworkInfo().isRoaming()) {

            return  Artist.getInfo(artistName, API_KEY);
        }
        return null;
    }
}
