package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.artistListAdapter;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;
import com.splunk.mint.Mint;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class artistActivity extends Activity implements OnItemClickListener{
	private HashMap<String, ArrayList<SongInfo>> listMap = null;
	private List<String> list = null;
	private artistListAdapter adap = null;
	private loadSongInfo loads = null;
	private boolean isthreadRunning = true;
	private SongsTable db = null;
	private SharedPreferences pref = null;
	private List<String> tempList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(this, "e45fc306");
		setContentView(R.layout.local_song_list_dialog);
		db = new SongsTable(this);
		pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Boolean isFirst = pref.getBoolean("artist", true);
		listMap = new HashMap<>();
		list = new ArrayList<>();
		tempList = new ArrayList<>();
		loads = new loadSongInfo();
		if(isFirst)
		loads.execute("");
		else
			makedbList();
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//makeList();
		
		TextView text = findViewById(R.id.local_songTitle);
		text.setText("Artist");
		//text.setBackground(null);
		ImageView search = findViewById(R.id.local_music_search);
		search.setVisibility(View.INVISIBLE);
		ImageView back = findViewById(R.id.local_music_back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ListView listview = findViewById(R.id.local_music_list);
		adap = new artistListAdapter(artistActivity.this, listMap, list);
		listview.setAdapter(adap);
		listview.setOnItemClickListener(this);
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}
	
	private void makedbList()
	{
		List<SongInfo> songList  = db.getAllList();
		for(int i = 0; i < songList.size(); i++)
		{
			
			String path = songList.get(i).getPath();
			String artist = songList.get(i).getArtist();
			String album = songList.get(i).getAlbum();
			ArrayList<SongInfo > arr = listMap.get(artist);
			if(arr ==null)
			{
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
				listMap.put(artist, arr);
				list.add(artist);
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
	
	private void makeList()
	{
		List<SongInfo> songList  = db.getAllList();
		for(int i = 0; i < songList.size(); i++)
		{
			if(!isthreadRunning)
				return;
			String path = songList.get(i).getPath();
			String artist = Utils.getAudioArtist(path, artistActivity.this);
			db.updateArtist(path, artist);
			ArrayList<SongInfo > arr = listMap.get(artist);
			if(arr ==null)
			{
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				arr.add(bean);
				listMap.put(artist, arr);
				tempList.add(artist);
			}
			else
			{
				SongInfo bean = new SongInfo();
			bean.setPath(path);
			arr.add(bean);
			}
			
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
			});
			
		}
		Editor edit = pref.edit();
		edit.putBoolean("artist", false);
		edit.commit();
		
		
	}
	
	
	private class loadSongInfo extends AsyncTask<String, Integer, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			makeList();
			return null;
		}
		
		
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		isthreadRunning = false;
		loads.cancel(true);
		
		Intent intent = new Intent(artistActivity.this, MyDialogFragment.class);
		ArrayList<SongInfo> item = listMap.get(list.get(position));
		intent.putExtra("abcd",item);
		startActivity(intent);
		
	}
	
	
	

}
