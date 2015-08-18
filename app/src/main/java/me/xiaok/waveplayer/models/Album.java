package me.xiaok.waveplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 专辑实体类
 * Created by GeeKaven on 15/8/16.
 */
public class Album implements Parcelable {
    /**
     * 专辑Id
     */
    public int mAlbumId;

    /**
     * 专辑名称
     */
    public String mAlbumName;

    /**
     * 专辑的艺术家名称
     */
    public String mArtistName;

    /**
     * 专辑年份
     */
    public int mAlbumYear;

    /**
     * 专辑封面
     */
    public String mAlbumArt;

    public Album(final int albumId, final String albumName, final String artistName,
                 final int albumYear, final String albumArt) {
        this.mAlbumId = albumId;
        this.mAlbumName = albumName;
        this.mArtistName = artistName;
        this.mAlbumYear = albumYear;
        this.mAlbumArt = albumArt;
    }

    public Album(Parcel source) {
        mAlbumId = source.readInt();
        mAlbumName = source.readString();
        mArtistName = source.readString();
        mAlbumYear = source.readInt();
        mAlbumArt = source.readString();
    }

    public static final Parcelable.Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel parcel) {
            return new Album(parcel);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mAlbumId);
        parcel.writeString(mAlbumName);
        parcel.writeString(mArtistName);
        parcel.writeInt(mAlbumYear);
        parcel.writeString(mAlbumArt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return mAlbumName;
    }

    public int getmAlbumId() {
        return mAlbumId;
    }

    public String getmAlbumName() {
        return mAlbumName;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public int getmAlbumYear() {
        return mAlbumYear;
    }

    public String getmAlbumArt() {
        return mAlbumArt;
    }
}
