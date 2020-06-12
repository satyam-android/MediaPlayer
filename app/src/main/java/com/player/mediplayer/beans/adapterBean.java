package com.player.mediplayer.beans;

import com.player.mediaplayer.adapter.ExpandableListAdapterArtist;

public class adapterBean {
	private static adapterBean bean = null;
	private adapterBean()
	{
		
	}
	public static adapterBean getInstance()
	{
		if(bean == null)
			bean = new adapterBean();
		return bean;
	}
	
	private ExpandableListAdapterArtist expListArtist = null;
	private ExpandableListAdapterArtist explistFolder = null;
	private ExpandableListAdapterArtist explistAlbum = null;
	public ExpandableListAdapterArtist getExpListArtist() {
		return expListArtist;
	}
	public void setExpListArtist(ExpandableListAdapterArtist expListArtist) {
		this.expListArtist = expListArtist;
	}
	public ExpandableListAdapterArtist getExplistFolder() {
		return explistFolder;
	}
	public void setExplistFolder(ExpandableListAdapterArtist explistFolder) {
		this.explistFolder = explistFolder;
	}
	public ExpandableListAdapterArtist getExplistAlbum() {
		return explistAlbum;
	}
	public void setExplistAlbum(ExpandableListAdapterArtist explistAlbum) {
		this.explistAlbum = explistAlbum;
	}
	
	

}
