package com.player.mediplayer.beans;

public class albumBean {
	private String id;
	private String category;
	private String noOfSong;
	private String imagePath;
	private String title;
	private String songs_list;
	public String getSongs_list() {
		return songs_list;
	}
	public void setSongs_list(String songs_list) {
		this.songs_list = songs_list;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getNoOfSong() {
		return noOfSong;
	}
	public void setNoOfSong(String noOfSong) {
		this.noOfSong = noOfSong;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

}
