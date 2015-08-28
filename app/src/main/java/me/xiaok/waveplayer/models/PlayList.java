package me.xiaok.waveplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 播放清单
 *
 * Created by GeeKaven on 15/8/24.
 */
public class PlayList implements Parcelable{
    private int mPlayListId;
    private String mPlayListName;

    public PlayList(final int playListId, final String playListName) {
        this.mPlayListId = playListId;
        this.mPlayListName = playListName;
    }

    public PlayList(Parcel parcel) {
        this.mPlayListId = parcel.readInt();
        this.mPlayListName = parcel.readString();
    }

    public static final Parcelable.Creator<PlayList> CREATOR = new Creator<PlayList>() {
        @Override
        public PlayList createFromParcel(Parcel parcel) {
            return new PlayList(parcel);
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mPlayListId);
        parcel.writeString(mPlayListName);
    }

    public int getmPlayListId() {
        return mPlayListId;
    }

    public String getmPlayListName() {
        return mPlayListName;
    }
}
