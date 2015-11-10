package com.mycj.beasun.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Music implements Parcelable {
	private long id;
	private String title;
	private String album;
	private int duration;
	private String artist;
	private String url;
	private long  size;
	private long albumId;
	
	 public Music(){  
     } 
	
	public Music(long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		 dest.writeLong(id);  
         dest.writeString(title);  
         dest.writeString(album);  
         dest.writeString(artist);  
         dest.writeString(url);  
         dest.writeInt(duration);  
         dest.writeLong(size);  
         dest.writeLong(albumId);  
	}
	
	/**
	 * @return the albumId
	 */
	public long getAlbumId() {
		return albumId;
	}

	/**
	 * @param albumId the albumId to set
	 */
	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public static final Parcelable.Creator<Music> createor = new Creator<Music>() {

		@Override
		public Music createFromParcel(Parcel source) {
			  Music music = new Music();  
			  music.setId(source.readLong());  
			  music.setTitle(source.readString());  
			  music.setAlbum(source.readString());  
			  music.setArtist(source.readString());  
			  music.setUrl(source.readString());  
			  music.setDuration(source.readInt());  
			  music.setSize(source.readLong());  
			  music.setAlbumId(source.readLong());  
              return music;  
		}

		@Override
		public Music[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Music[size];
		}
	};
	
	
}
