package me.xiaok.waveplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GeeKaven on 15/8/24.
 */
public class Genre implements Parcelable {

  /**
   * 类型Id
   */
  private int mGenreId;
  /**
   * 类型名称
   */
  private String mGenreName;

  public Genre(final int genreId, final String genreName) {
    this.mGenreId = genreId;
    this.mGenreName = genreName;
  }

  public Genre(Parcel parcel) {
    this.mGenreId = parcel.readInt();
    this.mGenreName = parcel.readString();
  }

  public static final Parcelable.Creator<Genre> CREATOR = new Creator<Genre>() {
    @Override public Genre createFromParcel(Parcel parcel) {
      return new Genre(parcel);
    }

    @Override public Genre[] newArray(int size) {
      return new Genre[size];
    }
  };

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel parcel, int i) {
    parcel.writeInt(mGenreId);
    parcel.writeString(mGenreName);
  }

  public int getmGenreId() {
    return mGenreId;
  }

  public String getmGenreName() {
    return mGenreName;
  }
}
