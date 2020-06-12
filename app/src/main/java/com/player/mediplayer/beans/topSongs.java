package com.player.mediplayer.beans;

public class topSongs {
	private String title;
	private String downloads;
	private String mp3Url;
	private albumBean albuminfo;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDownloads() {
		return downloads;
	}
	public void setDownloads(String downloads) {
		this.downloads = downloads;
	}
	public String getMp3Url() {
		return mp3Url;
	}
	public void setMp3Url(String mp3Url) {
		this.mp3Url = mp3Url;
	}
	public albumBean getAlbuminfo() {
		return albuminfo;
	}
	public void setAlbuminfo(albumBean albuminfo) {
		this.albuminfo = albuminfo;
	}
	

}
