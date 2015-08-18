package me.xiaok.waveplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 艺术家实体类
 * Created by GeeKaven on 15/8/16.
 */
public class Artist implements Parcelable {
    /**
     * 艺术家Id
     */
    private int mArtistId;

    /**
     * 艺术家名字
     */
    private String mArtistName;

    /**
     * 专辑数量
     */
    private int mAlbumNum;

    public Artist(final int artistId, final String artistName, final int albumNum) {
        this.mArtistId = artistId;
        this.mArtistName = artistName;
        this.mAlbumNum = albumNum;
    }

    public Artist(Parcel source) {
        mArtistId = source.readInt();
        mArtistName = source.readString();
        mAlbumNum = source.readInt();
    }

    public static final Parcelable.Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel parcel) {
            return new Artist(parcel);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mArtistId);
        parcel.writeString(mArtistName);
        parcel.writeInt(mAlbumNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return mArtistName;
    }

    public int getmArtistId() {
        return mArtistId;
    }

    public String getmArtistName() {
        return mArtistName;
    }

    public int getmAlbumNum() {
        return mAlbumNum;
    }
}
