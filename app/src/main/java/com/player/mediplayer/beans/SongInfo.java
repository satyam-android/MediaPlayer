package com.player.mediplayer.beans;

import java.io.Serializable;

public class SongInfo implements Serializable{
	private String path = "";
	private String title = "";
	private String album = "";
	private String artist = "";
	private String favourite = "";
	private String download ="";
	private boolean showSpeaker = false;
	public String getDownload() {
		return download;
	}
	public void setDownload(String download) {
		this.download = download;
	}
	private boolean playlistState = false;
	public boolean isPlaylistState() {
		return playlistState;
	}
	public void setPlaylistState(boolean playlistState) {
		this.playlistState = playlistState;
	}
	public String getFavourite() {
		return favourite;
	}
	public void setFavourite(String favourite) {
		this.favourite = favourite;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public boolean isShowSpeaker() {
		return showSpeaker;
	}
	public void setShowSpeaker(boolean showSpeaker) {
		this.showSpeaker = showSpeaker;
	}
	

}
