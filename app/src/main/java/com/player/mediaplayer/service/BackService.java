package com.player.mediaplayer.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.player.mediaplayer.splash;
import com.player.mediaplayer.adapter.ExpandableListAdapterArtist;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;
import com.player.mediplayer.beans.adapterBean;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class BackService extends Service {
	private SongsTable db = null;
	private boolean isServiceRunning = false;
	public static boolean stopeservice = false;
	public static boolean isOneTimeRun = false;
	private HashMap<String, ArrayList<SongInfo>> listMapFolder = null;
	private List<String> listFolder = null;
	private adapterBean bean = null;
	private List<String> listAlbum = null;
	private HashMap<String, ArrayList<SongInfo>> listMapAlbum = null;
	private List<String> listArtist = null;
	private HashMap<String, ArrayList<SongInfo>> listMapArtist = null;
	private ExpandableListAdapterArtist adapFolder = null;
	private ExpandableListAdapterArtist adapAlbum = null;
	private ExpandableListAdapterArtist adapArtist = null;
	private Timer timer = null;
	private List<SongInfo> songList = null;
	
	
	//private Handler handle = null;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		db = new SongsTable(getApplicationContext());
		songList  = db.getAllList();
		listMapFolder = new HashMap<>();
		listFolder = new ArrayList<>();
		listMapAlbum = new HashMap<>();
		listAlbum = new ArrayList<>();
		listMapArtist = new HashMap<>();
		listArtist = new ArrayList<>();
		bean = adapterBean.getInstance();
		timer =  new Timer();
		isOneTimeRun = false;
		stopeservice = false;
		Log.i("backservice", "inside oncreate");
		//handle = new Handler();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		/*List<SongInfo> list = getPath();
		
		List<SongInfo> dbList = db.getAllList();
		//int dbSize = dbList.size();
		//int listSize = list.size();
		//if(listSize < dbSize)
		//{
			insertOrUpdateTable(list, dbList);
			deleteNotInSD(list, dbList);
			Utils.list = db.getAllList();*/
			//if(!isServiceRunning)
		Log.i("backservice", "inside onstartcommand");
		if(timer != null)
			timer.cancel();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if(isServiceRunning)
					return;
				// TODO Auto-generated method stub
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					new loadSongs().execute("");
					} else {
						new loadSongs().execute("");
					}
				
			}
		}, 10000*6	, 2*10000*60);
		
			
		//}
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("backservice", "inside ondestroy");
		if(timer != null)
			timer.cancel();
		super.onDestroy();
	}
	
	private synchronized void deleteNotInSD(List<SongInfo> systemList, List<SongInfo> dbList)
	{
		for(int i = 0; i < dbList.size(); i++)
		{
			boolean value = false;
			int index = -1;
			for(int j = 0; j < systemList.size(); j++)
			{
				if(!systemList.get(j).getPath().equals(dbList.get(i).getPath()))
				{
					value = false;
					index = i;
				}
				else
				{
					value = true;
					/*String artist = systemList.get(i).getArtist();
					String album = systemList.get(i).getAlbum();
					if(!artist.equals("Unknown"))
						db.updateArtist(systemList.get(i).getPath(), artist);
					if(!album.equals("Unknown"))
						db.updateArtist(systemList.get(i).getPath(), album);*/
				}
				if(value)
					break;
			}
			if(!value)
			{
				if(index != -1)
				{
					SongInfo song = dbList.get(index);
					db.delete(song.getPath());
					//db.insertSongInfo(song, System.currentTimeMillis());
				}
			}
			
		}
		Log.i("backservice", "deletenotinSD");
	}
	
	private synchronized void insertOrUpdateTable(List<SongInfo> systemList, List<SongInfo> dbList)
	{
		for(int i = 0; i < systemList.size(); i++)
		{
			boolean value = false;
			int index = -1;
			for(int j = 0; j < dbList.size(); j++)
			{
				if(!systemList.get(i).getPath().equals(dbList.get(j).getPath()))
				{
					value = false;
					index = i;
				}
				else
				{
					value = true;
					String artist = systemList.get(i).getArtist();
					String album = systemList.get(i).getAlbum();
					if(!artist.equals("Unknown"))
						db.updateArtist(systemList.get(i).getPath(), artist);
					if(!album.equals("Unknown"))
						db.updateAlbum(systemList.get(i).getPath(), album);
				}
				if(value)
					break;
			}
			if(!value)
			{
				if(index != -1)
				{
					SongInfo song = systemList.get(index);
				db.insertSongInfo(song, System.currentTimeMillis());
				Log.i("backservice", "insert song"+song.getPath());
				}
			}
			
		}
		Log.i("backservice", "inserORupdateDB");
	}
	
	
	private synchronized List<SongInfo> getPath()
	{
		String path = "/storage/";
		File file = new File(path);
		File[] s = file.listFiles();
		/*List<String> li = new ArrayList<>();
		SongsTable db = new SongsTable(getApplicationContext());*/
		
			Utils.songListService.clear();
		for(int i = 0; i<s.length; i++)
		{
			if( s[i].getAbsolutePath().contains("MediaPlayer")||s[i].getAbsolutePath().contains("Card") || s[i].getAbsolutePath().contains("card"))
			{
				String songPath = s[i].getAbsolutePath();
				Utils.getMusicService(songPath,getApplicationContext());
				
			}
		}
		
		return Utils.songListService;
	}
	
	private class loadSongs extends AsyncTask<String, Void, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isServiceRunning  = true;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			List<SongInfo> list = getPath();
			
			List<SongInfo> dbList = songList;
			//int dbSize = dbList.size();
			//int listSize = list.size();
			//if(listSize < dbSize)
			//{
			//int oldSize = dbList.size();
			deleteNotInSD(list, dbList);
			insertOrUpdateTable(list, dbList);
			//int newSize = db.getAllList().size();
			//if(oldSize != newSize)
			//{
				makedbListFolderreal();
				 adapFolder = bean.getExplistFolder();//new ExpandableListAdapterArtist(getApplicationContext(), listFolder, listMapFolder);
				if(adapFolder == null)
				{
					adapFolder = new ExpandableListAdapterArtist(getApplicationContext(), listFolder, listMapFolder);
					bean.setExplistFolder(adapFolder);
				}
				else
				{
					List<String> listFolder = adapFolder.getList();
					listFolder.clear();
					listFolder.addAll(BackService.this.listFolder);
					HashMap<String, ArrayList<SongInfo>> listmapFolder = adapFolder.getListMap();
					listmapFolder.clear();
					listmapFolder.putAll(BackService.this.listMapFolder);
					
							
					
					
				}
				//bean.setExplistFolder(adapFolder);
				
				//makedbListAlbum();
				 adapAlbum = bean.getExplistAlbum();//new ExpandableListAdapterArtist(getApplicationContext(), listAlbum, listMapAlbum);
				if(adapAlbum == null)
				{
					adapAlbum = new ExpandableListAdapterArtist(getApplicationContext(), listAlbum, listMapAlbum);
					bean.setExplistAlbum(adapAlbum);
				}
				else
				{
					List<String> listAlbum = adapAlbum.getList();
					listAlbum.clear();
					listAlbum.addAll(BackService.this.listAlbum);
					HashMap<String, ArrayList<SongInfo>> listmapAlbum = adapAlbum.getListMap();
					listmapAlbum.clear();
					listmapAlbum.putAll(BackService.this.listMapAlbum);
					
					
				}
				//bean.setExplistAlbum(adapAlbum);
				//makedbListArtist();
				adapArtist =  bean.getExpListArtist();//new ExpandableListAdapterArtist(getApplicationContext(), listAlbum, listMapAlbum);
				if(adapArtist == null)
				{
					adapArtist = new ExpandableListAdapterArtist(getApplicationContext(), listArtist, listMapArtist);
					bean.setExpListArtist(adapArtist);
				}
				else
				{
					List<String> listArtist = adapArtist.getList();
					listArtist.clear();
					listArtist.addAll(BackService.this.listArtist);
					HashMap<String, ArrayList<SongInfo>> listmapArtist = adapArtist.getListMap();
					listmapArtist.clear();
					listmapArtist.putAll(BackService.this.listMapArtist);
					
				}//new ExpandableListAdapterArtist(getApplicationContext(), listArtist, listMapArtist);
				//bean.setExpListArtist(adapArtist);
			//}
			
			//Utils.list = db.getAllList();
				Log.i("backservice", "everything updated");
			return "";
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//stopSelf();
			adapFolder.notifyDataSetChanged();
			adapAlbum.notifyDataSetChanged();
			adapArtist.notifyDataSetChanged();
			isServiceRunning  = false;
			isOneTimeRun = true;
			if(stopeservice)
				{
					Log.i("backService", "stop service by splash");
					stopSelf();
				}
			else
			{
				Log.i("backService", "DONOT stop service by splash");
			}
			//startService(new Intent(getApplicationContext(), BackService.class));
			
		}
		
	}
	private void makedbListFolderreal() {

		for (int i = 0; i < songList.size(); i++) {

			String path = songList.get(i).getPath();
			String album = songList.get(i).getAlbum();
			String folder = getFolerName(path);

			ArrayList<SongInfo> arr = listMapFolder.get(folder);
			if (arr == null) {
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
				listMapFolder.put(folder, arr);
				listFolder.add(folder);
			} else {
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
			}

			// album//

			// String path = songList.get(i).getPath();
			// String album = songList.get(i).getAlbum();

			ArrayList<SongInfo> arralb = listMapAlbum.get(album);
			if (arralb == null) {
				arralb = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arralb.add(bean);
				listMapAlbum.put(album, arralb);
				listAlbum.add(album);
			} else {
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arralb.add(bean);
			}
			// end album//

			// artist//

			// String path = songList.get(i).getPath();
			String artist = songList.get(i).getArtist();
			// String album = songList.get(i).getAlbum();
			ArrayList<SongInfo> arrArt = listMapArtist.get(artist);
			if (arrArt == null) {
				arrArt = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arrArt.add(bean);
				listMapArtist.put(artist, arrArt);
				listArtist.add(artist);
			} else {
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arrArt.add(bean);
			}

			// endartis//

		}

	}
	
	private void makedbListFolder()
	{
		listMapFolder.clear();
		listFolder.clear();
		//List<SongInfo> songList  = db.getAllList();
		for(int i = 0; i < songList.size(); i++)
		{
			
			String path = songList.get(i).getPath();
			String album = songList.get(i).getAlbum();
			String folder = getFolerName(path);
			
			ArrayList<SongInfo > arr = listMapFolder.get(folder);
			if(arr ==null)
			{
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
				listMapFolder.put(folder, arr);
				listFolder.add(folder);
			}
			else
			{
				SongInfo bean = new SongInfo();
			bean.setPath(path);
			bean.setAlbum(album);
			arr.add(bean);
			}
		}	
		
	}
	
	private String getFolerName(String path)
	{
		String[] split = path.split("/");
		return split[split.length-2];
	}
	
	
	private void makedbListAlbum()
	{
		listMapAlbum.clear();
		listAlbum.clear();
		//List<SongInfo> songList  = db.getAllList();
		for(int i = 0; i < songList.size(); i++)
		{
			
			String path = songList.get(i).getPath();
			String album = songList.get(i).getAlbum();
			
			ArrayList<SongInfo > arr = listMapAlbum.get(album);
			if(arr ==null)
			{
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
				listMapAlbum.put(album, arr);
				listAlbum.add(album);
			}
			else
			{
				SongInfo bean = new SongInfo();
			bean.setPath(path);
			bean.setAlbum(album);
			arr.add(bean);
			}
			
			/*
			runOnUiThread(new Runnable() {
				public void run() {
					if(adap != null)
					{
						List<String> li = adap.getList();
						li.addAll(tempList);
						tempList.clear();
						adap.notifyDataSetChanged();
					}
				}
			});*/
			
		}
		
	}
	
	
	
	private void makedbListArtist()
	{
		listMapArtist.clear();
		listArtist.clear();
		//List<SongInfo> songList  = db.getAllList();
		for(int i = 0; i < songList.size(); i++)
		{
			
			String path = songList.get(i).getPath();
			String artist = songList.get(i).getArtist();
			String album = songList.get(i).getAlbum();
			ArrayList<SongInfo > arr = listMapArtist.get(artist);
			if(arr ==null)
			{
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
				listMapArtist.put(artist, arr);
				listArtist.add(artist);
			}
			else
			{
				SongInfo bean = new SongInfo();
			bean.setPath(path);
			bean.setAlbum(album);
			arr.add(bean);
			}
			
			/*
			runOnUiThread(new Runnable() {
				public void run() {
					if(adap != null)
					{
						List<String> li = adap.getList();
						li.addAll(tempList);
						tempList.clear();
						adap.notifyDataSetChanged();
					}
				}
			});*/
			
		}
		
	}
	
	private synchronized List<SongInfo> filter(List<SongInfo > li)
	{
		List<SongInfo> songListService = new ArrayList<>();
		songListService.addAll(li);
		HashSet <SongInfo> h = new HashSet<SongInfo>(songListService);
		songListService.clear();
		songListService.addAll(h);
		return songListService;
		
	}


}
