package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.player.SongsPod.R;
import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.artistListAdapter;

import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;
import com.splunk.mint.Mint;

public class folderActivity extends Activity implements OnItemClickListener{
	private HashMap<String, ArrayList<SongInfo>> listMap = null;
	private List<String> list = null;
	private artistListAdapter adap = null;
	//private loadSongInfo loads = null;
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
		Boolean isFirst = pref.getBoolean("album", true);
		listMap = new HashMap<>();
		list = new ArrayList<>();
		tempList = new ArrayList<>();
		makedbList();
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//makeList();
		
		TextView text = findViewById(R.id.local_songTitle);
		text.setText("Folder");
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
		adap = new artistListAdapter(folderActivity.this, listMap, list);
		listview.setAdapter(adap);
		listview.setOnItemClickListener(this);
		
	}
	private void makedbList()
	{
		List<SongInfo> songList  = db.getAllList();
		for(int i = 0; i < songList.size(); i++)
		{
			
			String path = songList.get(i).getPath();
			String album = songList.get(i).getAlbum();
			String folder = getFolerName(path);
			
			ArrayList<SongInfo > arr = listMap.get(folder);
			if(arr ==null)
			{
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
				listMap.put(folder, arr);
				list.add(folder);
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


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(folderActivity.this, MyDialogFragment.class);
		ArrayList<SongInfo> item = listMap.get(list.get(position));
		intent.putExtra("abcd",item);
		startActivity(intent);
		
	}
	}
