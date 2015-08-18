package me.xiaok.waveplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 歌曲实体类
 * Created by GeeKaven on 15/8/16.
 */
public class Song implements Parcelable {

    /**
     * 歌曲Id
     */
    private int mSongId;

    /**
     * 歌曲名称
     */
    private String mSongName;

    /**
     * 艺术家
     */
    private String mArtistName;

    /**
     * 专辑
     */
    private String mAblumName;

    /**
     * 时长
     */
    private long mDuration;

    /**
     * 路径
     */
    private String mSongPath;

    /**
     * 歌曲的专辑Id
     */
    private int mAlbumId;

    /**
     * 歌曲的艺术家Id
     */
    private int mArtistId;

    public Song(final int songId, final String songName, final String artistName,
                final String ablumName, final long duration, final String songPath,
                final int albumId, final int artistId) {
        this.mSongId = songId;
        this.mSongName = songName;
        this.mArtistName = artistName;
        this.mAblumName = ablumName;
        this.mDuration = duration;
        this.mSongPath = songPath;
        this.mAlbumId = albumId;
        this.mArtistId = artistId;
    }

    public Song(Parcel source) {
        mSongId = source.readInt();
        mSongName = source.readString();
        mArtistName = source.readString();
        mAblumName = source.readString();
        mDuration = source.readLong();
        mSongPath = source.readString();
        mAlbumId = source.readInt();
        mArtistId = source.readInt();
    }

    @Override
    public String toString() {
        return mSongName;
    }

    public static final Parcelable.Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel parcel) {
            return new Song(parcel);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mSongId);
        parcel.writeString(mSongName);
        parcel.writeString(mArtistName);
        parcel.writeString(mAblumName);
        parcel.writeLong(mDuration);
        parcel.writeString(mSongPath);
        parcel.writeInt(mAlbumId);
        parcel.writeInt(mArtistId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getmSongPath() {
        return mSongPath;
    }

    public long getmDuration() {
        return mDuration;
    }

    public String getmAblumName() {
        return mAblumName;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public String getmSongName() {
        return mSongName;
    }

    public int getmSongId() {
        return mSongId;
    }

    public int getmAlbumId() {
        return mAlbumId;
    }

    public int getmArtistId() {
        return mArtistId;
    }
}
