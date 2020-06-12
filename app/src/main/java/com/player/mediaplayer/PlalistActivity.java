package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.List;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.PlaylistActivityListAdapter;
import com.player.mediaplayer.database.PlaylistTable;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;
import com.splunk.mint.Mint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Playlists;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PlalistActivity extends Activity implements OnClickListener {
	private ListView listPlalist = null;
	private EditText edit = null;
	private SongsTable db = null;
	private PlaylistTable playDb = null;
	private List<String> list = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		Mint.initAndStartSession(this, "e45fc306");
		setContentView(R.layout.playlist_activity);
		loadWidget();
		list = new ArrayList<>();
		db = new SongsTable(PlalistActivity.this);
		playDb = new PlaylistTable(PlalistActivity.this);
		list = playDb.getAllPlaylist();
		PlaylistActivityListAdapter adap = new PlaylistActivityListAdapter(PlalistActivity.this, list,listPlalist);
		listPlalist.setAdapter(adap);
	}

	private void loadWidget() {
		TextView addPlaylist = findViewById(R.id.plalist_add);
		addPlaylist.setOnClickListener(this);
		listPlalist = findViewById(R.id.playlist_list);
		listPlalist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				List<SongInfo> info = playDb.getAllPlaylistInfo(list.get(position));
				Intent intent = new Intent(PlalistActivity.this, MyDialogFragment.class);
				//ArrayList<SongInfo> item = listMap.get(list.get(position));
				intent.putExtra("abcd",(ArrayList<SongInfo>)info);
				intent.putExtra("scr", "Playlist");
				startActivity(intent);
				
			}
		});
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.plalist_add:
			alertDialogForPlalist();
			break;

		default:
			break;
		}

	}

	private void alertDialogForPlalist() {
		AlertDialog.Builder build = new AlertDialog.Builder(
				PlalistActivity.this);
		build.setTitle("Add a Playlist");
		edit = new EditText(PlalistActivity.this);
		edit.setHint("Add a Playlist");
		build.setView(edit);
		build.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		build.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				chooseSongsForPlaylist();

			}
		});
		build.show();
	}

	private void chooseSongsForPlaylist() {
		final List<SongInfo> listTemp = new ArrayList<>();
		AlertDialog.Builder build = new AlertDialog.Builder(
				PlalistActivity.this);
		build.setTitle("Add Songs");
		final List<SongInfo> info = db.getAllList();
		build.setMultiChoiceItems(convertListToString(info), getBoolean(),
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						// TODO Auto-generated method stub
						try {

							if (isChecked) {
								if(!listTemp.contains(info.get(which)))
								listTemp.add(info.get(which));
							} else {
								listTemp.remove(info.get(which));

							}
						} catch (Exception e) {
							// TODO: handle exception
						}

					}
				});
		build.setPositiveButton("Okay", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				for (SongInfo songInfo : listTemp) {
					playDb.insert(songInfo.getPath(),
							edit.getText().toString(), "yes");
					playDb.updateFav(songInfo.getPath(), songInfo.getFavourite());
					playDb.updateAlbum(songInfo.getPath(), songInfo.getAlbum());
				}
				list = playDb.getAllPlaylist();
				PlaylistActivityListAdapter adap = new PlaylistActivityListAdapter(PlalistActivity.this, list,listPlalist);
				listPlalist.setAdapter(adap);
			}
		});
		build.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
		build.show();
	}

	private String[] convertListToString(List<SongInfo> list) {
		String[] path = new String[list.size() ];
		for (int i = 0; i < list.size(); i++) {
			path[i] = getSongName(list.get(i).getPath());
		}
		return path;
	}

	private String getSongName(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
	}

	private boolean[] getBoolean() {
		boolean[] bool = null;
		return bool;
	}
}
