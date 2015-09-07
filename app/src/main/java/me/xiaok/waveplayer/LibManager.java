package me.xiaok.waveplayer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import me.xiaok.waveplayer.models.Album;
import me.xiaok.waveplayer.models.Artist;
import me.xiaok.waveplayer.models.Genre;
import me.xiaok.waveplayer.models.PlayList;
import me.xiaok.waveplayer.models.Song;
import me.xiaok.waveplayer.utils.LogUtils;

/**
 * 该类是实体管理类
 * 内部保存着歌曲列表，专辑列表，艺术家列表
 * <p/>
 * Created by GeeKaven on 15/8/16.
 */
public class LibManager {

    public static final String TAG = "LibManager";

    public static final ArrayList<Song> mSongLib = new ArrayList<>();
    public static final ArrayList<Artist> mArtistLib = new ArrayList<>();
    public static final ArrayList<Album> mAlbumLib = new ArrayList<>();
    public static final ArrayList<PlayList> mPlayListLib = new ArrayList<>();
    public static final ArrayList<Genre> mGenreLib = new ArrayList<>();

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

    public static final String[] playListProjection = new String[]{
            MediaStore.Audio.Playlists._ID,
            MediaStore.Audio.Playlists.NAME
    };

    public static final String[] genreProjection = new String[]{
            MediaStore.Audio.Genres._ID,
            MediaStore.Audio.Genres.NAME
    };

    public static final String[] playListSongProjection = new String[] {
            MediaStore.Audio.Playlists.Members.TITLE,
            MediaStore.Audio.Playlists.Members.AUDIO_ID,
            MediaStore.Audio.Playlists.Members.ARTIST,
            MediaStore.Audio.Playlists.Members.ALBUM,
            MediaStore.Audio.Playlists.Members.DURATION,
            MediaStore.Audio.Playlists.Members.DATA,
            MediaStore.Audio.Playlists.Members.ALBUM_ID,
            MediaStore.Audio.Playlists.Members.ARTIST_ID
    };

    /**
     * 扫描所有列表
     *
     * @param context
     */
    public static void scanAll(Context context) {
        clearAll();
        setSongLib(scanSongs(context));
        setArtistLib(scanArtists(context));
        setAlbumLib(scanAlbums(context));
        setPlayListLib(scanPlayList(context));
        setGenreLib(scanGenres(context));
    }

    /**
     * 扫描歌曲
     *
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
     *
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
     *
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

    /**
     * 扫描播放清单
     *
     * @param context
     * @return
     */
    public static ArrayList<PlayList> scanPlayList(Context context) {
        ArrayList<PlayList> playLists = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                playListProjection,
                null,
                null,
                MediaStore.Audio.Playlists.NAME + " ASC"
        );

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            PlayList playList = new PlayList(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.NAME))
            );
            playLists.add(playList);
        }

        cursor.close();
        return playLists;
    }

    /**
     * 扫描类型， 并将Song与Genre关联
     *
     * @param context
     * @return
     */
    public static ArrayList<Genre> scanGenres(Context context) {
        LogUtils.v(TAG, "scanGenres() called");
        ArrayList<Genre> genres = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI,
                genreProjection,
                null,
                null,
                MediaStore.Audio.Genres.NAME + " ASC");

        LogUtils.v(TAG, cursor.getCount() + "");
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            int genreId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Genres._ID));
            String genreName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Genres.NAME));

            //LogUtils.v(TAG, "get genres : id = " + genreId + "  name = " + genreName);
            Genre genre = new Genre(genreId, genreName);
            genres.add(genre);

            //将歌曲与类型关联
            Cursor genreCursor = context.getContentResolver().query(
                    MediaStore.Audio.Genres.Members.getContentUri("external", genreId),
                    new String[]{MediaStore.Audio.Media._ID},
                    MediaStore.Audio.Media.IS_MUSIC + " != 0 ",
                    null,
                    null
            );
            genreCursor.moveToFirst();
            for (int j = 0; j < genreCursor.getCount(); j++) {
                genreCursor.moveToPosition(j);
                int index = genreCursor.getColumnIndex(MediaStore.Audio.Media._ID);
                final Song song = findSongById(genreCursor.getInt(index));
                if (song != null) {
                    song.setmGenreId(genreId);
                }
            }
            genreCursor.close();
        }

        cursor.close();
        return genres;
    }

    public static boolean isEmpty() {
        boolean flag = false;

        if (mSongLib.isEmpty() && mArtistLib.isEmpty() && mAlbumLib.isEmpty()
                && mPlayListLib.isEmpty() && mGenreLib.isEmpty()) {
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
     * 设置清单列表
     *
     * @param playList
     */
    public static void setPlayListLib(ArrayList<PlayList> playList) {
        mPlayListLib.clear();
        mPlayListLib.addAll(playList);
    }

    /**
     * 设置类型列表
     *
     * @param genreList
     */
    public static void setGenreLib(ArrayList<Genre> genreList) {
        mGenreLib.clear();
        mGenreLib.addAll(genreList);
    }

    /**
     * 获取歌曲列表
     *
     * @return
     */
    public static ArrayList<Song> getSongs() {
        return mSongLib;
    }

    /**
     * 获取艺术家列表
     *
     * @return
     */
    public static ArrayList<Artist> getArtists() {
        return mArtistLib;
    }

    /**
     * 获取专辑列表
     *
     * @return
     */
    public static ArrayList<Album> getAlbums() {
        return mAlbumLib;
    }

    /**
     * 获得播放清单
     *
     * @return
     */
    public static ArrayList<PlayList> getPlayLists() {
        return mPlayListLib;
    }

    /**
     * 获取类型列表
     *
     * @return
     */
    public static ArrayList<Genre> getGenres() {
        return mGenreLib;
    }

    /**
     * 清除所有内容
     */
    public static void clearAll() {
        mSongLib.clear();
        mArtistLib.clear();
        mAlbumLib.clear();
        mGenreLib.clear();
        mPlayListLib.clear();
    }

    /**
     * 获取专辑下的所有歌曲
     *
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
     *
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
     *
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

    /**
     * 获取该歌手的所有歌曲
     *
     * @param artist
     * @return
     */
    public static ArrayList<Song> getArtistSong(Artist artist) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song s : mSongLib) {
            if (s.getmArtistId() == artist.getmArtistId()) {
                songs.add(s);
            }
        }
        return songs;
    }

    /**
     * 获得播放清单里的所有歌曲
     *
     * @param playList
     * @return
     */
    public static ArrayList<Song> getPlayListSongs(Context context, PlayList playList) {
        ArrayList<Song> songs = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", playList.getmPlayListId()),
                playListSongProjection,
                MediaStore.Audio.Media.IS_MUSIC + " != 0",
                null,
                null
        );

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            Song song = new Song(
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.AUDIO_ID)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.TITLE)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM)),
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.DURATION)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.DATA)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ALBUM_ID)),
                    cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists.Members.ARTIST_ID))
            );
            songs.add(song);
        }

        cursor.close();
        return songs;
    }

    /**
     * 获取一个类型下的所有歌曲
     *
     * @param genre
     * @return
     */
    public static ArrayList<Song> getGenreSongs(Genre genre) {
        ArrayList<Song> songs = new ArrayList<>();
        for (Song s : mSongLib) {
            if (s.getmGenreId() == genre.getmGenreId()) {
                songs.add(s);
            }
        }
        return songs;
    }

    /**
     * 根据Id寻找歌曲
     *
     * @param songId
     * @return
     */
    public static Song findSongById(int songId) {
        for (Song s : mSongLib) {
            if (s.getmSongId() == songId) {
                return s;
            }
        }
        return null;
    }

    public static PlayList createPlaylist(final View view, final String playlistName){
        final Context context = view.getContext();
        String trimmedName = playlistName.trim();

        setPlayListLib(scanPlayList(context));

        String error = checkPlaylistName(context, trimmedName);
        if (error != null){
            return null;
        }

        final PlayList created = makePlaylist(context, trimmedName);

        return created;
    }

    /**
     * 创建歌单
     * @param context
     * @param playlistName
     * @return
     */
    private static PlayList makePlaylist(final Context context, final String playlistName){
        String trimmedName = playlistName.trim();

        // 添加歌单到MediaStore
        ContentValues mInserts = new ContentValues();
        mInserts.put(MediaStore.Audio.Playlists.NAME, trimmedName);
        mInserts.put(MediaStore.Audio.Playlists.DATE_ADDED, System.currentTimeMillis());
        mInserts.put(MediaStore.Audio.Playlists.DATE_MODIFIED, System.currentTimeMillis());

        Uri newPlaylistUri = context.getContentResolver().insert(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI, mInserts);

        // 更新存储的歌单
        setPlayListLib(scanPlayList(context));

        // 获取新歌单ID
        Cursor cursor = context.getContentResolver().query(
                newPlaylistUri,
                new String[]{MediaStore.Audio.Playlists._ID},
                null, null, null);

        cursor.moveToFirst();
        final PlayList playlist = new PlayList(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Playlists._ID)), playlistName);
        cursor.close();

        return playlist;
    }

    /**
     * 删除歌单
     * @param context
     * @param playlist
     */
    public static PlayList deletePlaylist(final Context context, final PlayList playlist){
        //从MediaStore移除歌单
        context.getContentResolver().delete(
                MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
                MediaStore.Audio.Playlists._ID + "=?",
                new String[]{playlist.getmPlayListId() + ""});

        //更新歌单
        mPlayListLib.clear();
        setPlayListLib(scanPlayList(context));

        return playlist;
    }

    /**
     * 向歌单添加音乐
     * @param context
     * @param playlist
     * @param song
     */
    public static void addSongToPlaylist (final Context context, final PlayList playlist, final Song song){
        Cursor cur = context.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.getmPlayListId()),
                null, null, null,
                MediaStore.Audio.Playlists.Members.TRACK + " ASC");

        long count = 0;
        if (cur.moveToLast()) count = cur.getLong(cur.getColumnIndex(MediaStore.Audio.Playlists.Members.TRACK));
        cur.close();

        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, count + 1);
        values.put(MediaStore.Audio.Playlists.Members.AUDIO_ID, song.getmSongId());

        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.getmPlayListId());
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(uri, values);
        resolver.notifyChange(Uri.parse("content://media"), null);
    }

    /**
     * 批量向歌单添加音乐
     * @param context
     * @param playlist
     * @param songs
     */
    public static void addSongListToPlaylist(final Context context, final PlayList playlist, final ArrayList<Song> songs){
        Cursor cur = context.getContentResolver().query(
                MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.getmPlayListId()),
                null, null, null,
                MediaStore.Audio.Playlists.Members.TRACK + " ASC");

        long count = 0;
        if (cur.moveToLast()) count = cur.getLong(cur.getColumnIndex(MediaStore.Audio.Playlists.Members.TRACK));
        cur.close();

        ContentValues[] values = new ContentValues[songs.size()];
        for (int i = 0; i < songs.size(); i++) {
            values[i] = new ContentValues();
            values[i].put(MediaStore.Audio.Playlists.Members.PLAY_ORDER, count + 1);
            values[i].put(MediaStore.Audio.Playlists.Members.AUDIO_ID, songs.get(i).getmSongId());
        }

        Uri uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlist.getmPlayListId());
        ContentResolver resolver = context.getContentResolver();
        resolver.bulkInsert(uri, values);
        resolver.notifyChange(Uri.parse("content://media"), null);
    }

    /**
     * 检查输入歌单名称， 为空，或已经存在
     * @param context
     * @param trimmedName
     * @return
     */
    public static String checkPlaylistName(Context context, String trimmedName) {

        if (trimmedName.length() == 0) {
            return context.getResources().getString(R.string.input_empty);
        }

        if (trimmedName.trim().length() == 0) {
            return context.getResources().getString(R.string.input_empty);
        }

        for (PlayList playList : mPlayListLib) {
            if (playList.getmPlayListName().equals(trimmedName)) {
                return context.getResources().getString(R.string.input_exist);
            }
        }

        return null;
    }

    /**
     * 音乐添加，删除后，数据库中的音乐信息并没有更新，原来的音乐还会显示在那里
     * 那么就需要重新扫描音乐文件，更新数据库，然后在刷新信息
     * @param context
     */
    public static void scanMusic(Context context) {
        Intent intent = new Intent();
    }
}
