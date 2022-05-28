package com.VerticalCodes.tabs;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;

public class Song implements Parcelable {
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Song createFromParcel(Parcel in) {
			return new Song(in);
		}

		public Song[] newArray(int size) {
			return new Song[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO: Implement this method
		return 0;
	}

	public Song(Parcel in) {
		this.mTitle = in.readString();
		this.mPath = in.readString();
		this.mAlbumName = in.readString();
		this.mArtistName = in.readString();
		this.mTrackNumber = in.readInt();
		this.mDuration = in.readLong();
		this.mArtistId = in.readInt();
		this.mYear = in.readInt();
		this.mSize = in.readLong();

	}

	public Song() {
	}

	@Override
	public void writeToParcel(Parcel p1, int p2) {
		// TODO: Implement this method
		p1.writeString(this.mTitle);
		p1.writeString(this.mPath);
		p1.writeString(this.mAlbumName);
		p1.writeString(this.mArtistName);
		p1.writeInt(this.mTrackNumber);
		p1.writeLong(this.mDuration);
		p1.writeInt(this.mArtistId);
		p1.writeInt(this.mYear);
		p1.writeLong(this.mSize);

	}

	static final Song EMPTY_SONG = new Song();

	private String mTitle;
	private int mTrackNumber;
	private long mDuration;
	private String mPath;
	private String mAlbumName;
	private int mArtistId;
	private String mArtistName;
	private int mYear;
	private long mSize;

	@NonNull
	public static String formatDuration(final long duration) {
		return String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(duration),
				TimeUnit.MILLISECONDS.toSeconds(duration)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
	}

	public static int formatTrack(final int trackNumber) {
		int formatted = trackNumber;
		if (trackNumber >= 1000) {
			formatted = trackNumber % 1000;
		}
		return formatted;
	}

	@NonNull
	public final String getSongTitle() {
		return mTitle;
	}

	@NonNull
	public void setSongTitle(String title) {
		mTitle = title;
	}

	public final int getTrackNumber() {
		return mTrackNumber;
	}

	@NonNull
	public void setTrackNumber(int number) {
		mTrackNumber = number;
	}

	public final long getSongDuration() {
		return mDuration;
	}

	@NonNull
	public void setSongDuration(long duration) {
		mDuration = duration;
	}

	@NonNull
	public final String getSongPath() {
		return mPath;
	}

	@NonNull
	public void setSongPath(String path) {
		mPath = path;
	}

	@NonNull
	public final String getAlbumName() {
		return mAlbumName;
	}

	@NonNull
	public void setAlbumName(String name) {
		mAlbumName = name;
	}

	public final int getArtistId() {
		return mArtistId;
	}

	@NonNull
	public void setArtistId(int number) {
		mArtistId = number;
	}

	@NonNull
	public final String getArtistName() {
		return mArtistName;
	}

	@NonNull
	public void setArtistName(String name) {
		mArtistName = name;
	}

	public final int getYear() {
		return mYear;
	}

	@NonNull
	public void setYear(int number) {
		mYear = number;
	}

	@NonNull
	public void setSongSize(long number) {
		mSize = number;
	}

	public final long getSongSize() {
		return mSize;
	}
}