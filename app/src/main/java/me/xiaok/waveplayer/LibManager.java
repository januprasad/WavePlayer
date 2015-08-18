package me.xiaok.waveplayer;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;

import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.Artist;
import me.xiaok.waveplayer.models.Song;

/**
 * 该类是实体管理类
 * 内部保存着歌曲列表，专辑列表，艺术家列表
 * <p/>
 * Created by GeeKaven on 15/8/16.
 */
public class LibManager {

    public static final ArrayList<Song> mSongLib = new ArrayList<>();
    public static final ArrayList<Artist> mArtistLib = new ArrayList<>();
    public static final ArrayList<Album> mAlbumLib = new ArrayList<>();

    /**
     * 歌曲查询
     */
    public static final String[] songsProjection = new String[]{
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST_ID
    };

    /**
     * 艺术家查询
     */
    public static final String[] artistProjection = new String[]{
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
    };

    /**
     * 专辑查询
     */
    public static final String[] albumProjection = new String[]{
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Media.ARTIST_ID,
            MediaStore.Audio.Albums.LAST_YEAR,
            MediaStore.Audio.Albums.ALBUM_ART,
    };

    /**
     * 扫描所有列表
     * @param context
     */
    public static void scanAll(Context context) {
        clearAll();
        setSongLib(scanSongs(context));
        setArtistLib(scanArtists(context));
        setAlbumLib(scanAlbums(context));
    }

    /**
     * 扫描歌曲
     * @param context
     * @return 歌曲列表
     */
    public static ArrayList<Song> scanSongs(Context context) {
        ArrayList<Song> songs = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songsProjection,
                MediaStore.Audio.Media.IS_MUSIC + "!=0",
                null,
                MediaStore.Audio.Media.TITLE + " ASC"
        );
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Song song = new Song(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))).equals(MediaStore.UNKNOWN_STRING)
                            ? context.getString(R.string.no_artist)
                            : cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)),
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID))
            );

            songs.add(song);
        }

        cursor.close();
        return songs;
    }

    /**
     * 扫描艺术家
     * @param context
     * @return 艺术家列表
     */
    public static ArrayList<Artist> scanArtists(Context context) {
        ArrayList<Artist> artists = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                artistProjection,
                null,
                null,
                MediaStore.Audio.Artists.ARTIST + " ASC"
        );

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Artist artist = new Artist(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)),
                    (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)).equals(MediaStore.UNKNOWN_STRING))
                            ? context.getString(R.string.no_artist)
                            : cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS))
            );
            artists.add(artist);
        }

        cursor.close();
        return artists;
    }

    /**
     * 扫描专辑
     * @param context
     * @return 专辑列表
     */
    public static ArrayList<Album> scanAlbums(Context context) {
        ArrayList<Album> albums = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                albumProjection,
                null,
                null,
                MediaStore.Audio.Albums.ALBUM + " ASC"
        );

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Album album = new Album(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)),
                    (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)).equals(MediaStore.UNKNOWN_STRING))
                            ? context.getString(R.string.no_artist)
                            : cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST_ID)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
            );
            albums.add(album);
        }

        cursor.close();
        return albums;
    }

    public static boolean isEmpty() {
        boolean flag = false;

        if (mSongLib.isEmpty() && mArtistLib.isEmpty() && mAlbumLib.isEmpty()){
            flag = true;
        }

        return flag;
    }

    /**
     * 设置歌曲列表
     *
     * @param songList
     */
    public static void setSongLib(ArrayList<Song> songList) {
        mSongLib.clear();
        mSongLib.addAll(songList);
    }

    /**
     * 设置艺术家列表
     *
     * @param artistList
     */
    public static void setArtistLib(ArrayList<Artist> artistList) {
        mArtistLib.clear();
        mArtistLib.addAll(artistList);
    }

    /**
     * 设置专辑列表
     *
     * @param albumList
     */
    public static void setAlbumLib(ArrayList<Album> albumList) {
        mAlbumLib.clear();
        mAlbumLib.addAll(albumList);
    }

    /**
     * 获取歌曲列表
     * @return
     */
    public static ArrayList<Song> getSongs() {
        return mSongLib;
    }

    /**
     * 获取艺术家列表
     * @return
     */
    public static ArrayList<Artist> getArtists() {
        return mArtistLib;
    }

    /**
     * 获取专辑列表
     * @return
     */
    public static ArrayList<Album> getAlbums() {
        return mAlbumLib;
    }

    /**
     * 清除所有内容
     */
    public static void clearAll() {
        mSongLib.clear();
        mArtistLib.clear();
        mAlbumLib.clear();
    }

    /**
     * 获取专辑下的所有歌曲
     * @param album
     * @return 同一专辑的所有歌
     */
    public static ArrayList<Song> getAlbumSongs(Album album) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song s : mSongLib) {
            if (s.getmAlbumId() == album.getmAlbumId()) {
                songs.add(s);
            }
        }

        return songs;
    }

    /**
     * 获取歌曲的专辑,每首歌只有一个专辑
     * @param song
     * @return
     */
    public static ArrayList<Album> getSongAlbum(Song song) {
        ArrayList<Album> albums = new ArrayList<>();
        for (Album a : mAlbumLib) {
            if (a.getmAlbumId() == song.getmAlbumId()) {
                albums.add(a);
                break;
            }
        }
        return albums;
    }

    /**
     * 获取歌手的所有专辑
     * @param artist
     * @return
     */
    public static ArrayList<Album> getArtistAlbums(Artist artist) {
        ArrayList<Album> albums = new ArrayList<>();
        for (Album a : mAlbumLib) {
            if (a.getmArtistId() == artist.getmArtistId()) {
                albums.add(a);
            }
        }
        return albums;
    }
}
