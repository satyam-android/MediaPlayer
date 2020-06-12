package com.player.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.player.mediaplayer.MyDialogFragment;
import com.player.mediaplayer.MyFragment;
import com.player.mediaplayer.PlalistActivity;
import com.player.SongsPod.R;
import com.player.mediaplayer.constant.NDSpinner;
import com.player.mediaplayer.database.PlaylistTable;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class PlaylistActivityListAdapter extends BaseAdapter{
	private Activity act = null;
	private List<String> list = null;
	private LayoutInflater inflater = null;
	private HashMap<Integer, View> mapView = null;
	private String playlistName = null;
	private PlaylistTable playDb = null;
	private ListView viewList = null;
	private SongsTable db = null;
	private List<SongInfo> editList  = null;
	public PlaylistActivityListAdapter(Activity act, List<String> list, ListView viewList)
	{
		this.act = act;
		this.list = list;
		this.inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mapView = new HashMap<>();
		this.playDb = new PlaylistTable(act);
		this.viewList = viewList;
		this.db = new SongsTable(act);
		this.editList = new ArrayList<>();
		//this.playlistName = playlistName;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mapView.get(position);
		Holder holder = null;
		if(view == null)
		{
			view = inflater.inflate(R.layout.playlist_activity_row, null);
			holder = new Holder();
			holder.playlist = view.findViewById(R.id.plalist_textView);
			holder.layout  = view.findViewById(R.id.playlist_item_layout);
			holder.playlist.setText(list.get(position));
			TextView songs = view.findViewById(R.id.plalist_textView_song);
			songs.setText(playDb.getAllPlaylistInfo(list.get(position)).size() + " Songs");
			holder.playlist.setTag(holder);
			holder.layout.setTag(holder);
			holder.pos = position;
			holder.spinner = view.findViewById(R.id.playlist_option);
			loadSpinner(holder.spinner);
			holder.spinner.setTag(holder);
			view.setTag(holder);
			mapView.put(position, view);
			
		}
		else
		{
			holder = (Holder) view.getTag();
		}
		holder.layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Holder hold = (Holder) v.getTag();
				int position = hold.pos;
				List<SongInfo> info = playDb.getAllPlaylistInfo(list.get(position));
				Intent intent = new Intent(act, MyDialogFragment.class);
				//ArrayList<SongInfo> item = listMap.get(list.get(position));
				intent.putExtra("abcd",(ArrayList<SongInfo>)info);
				intent.putExtra("scr", "Playlist");
				act.startActivity(intent);
				
			}
		});
		
		holder.spinner.setOnItemSelectedListener(new onitemClick(holder.spinner));/*new OnItemSelectedListener() {
			private boolean isSpinnerFirstTime = true;

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Holder hold = (Holder) arg1.getTag();
				int position = hold.pos;
				String selectedString = (String) arg0.getItemAtPosition(arg2);
				if(!isSpinnerFirstTime)
				{
					
				//customizeValue(selectedString);
					onSpinnerItemClick(selectedString,position);
				}
				isSpinnerFirstTime = false;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				//String num_person = "1-20";
			}
			
			
		});*/
		return view;
	}
	
	private class onitemClick implements OnItemSelectedListener
	{
		private boolean isSpinnerFirstTime = true;
		private View v = null;
		public onitemClick(View v)
		{
			this.v = v;
		}

		
		
		private void itemClick(AdapterView<?> parent,int positio)
		{

			// TODO Auto-generated method stub
			Holder hold = (Holder) v.getTag();
			int position = hold.pos;
			String selectedString = (String) parent.getItemAtPosition(positio);
			if(!isSpinnerFirstTime)
			{
				
			//customizeValue(selectedString);
				onSpinnerItemClick(selectedString,position);
			}
			isSpinnerFirstTime = false;

		
		}



		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			itemClick(parent, position);
			
		}



		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class Holder
	{
		public NDSpinner spinner;
		public int pos;
		public TextView playlist;
		public LinearLayout layout;
	}
	
	private void loadSpinner(NDSpinner spinner) {
		
		List<String> list = new ArrayList<String>();
		/*if(notInUI.size() == 0)
			list.add("All Songs");*/
		//list.addAll(notInUI);
		list.add("Edit");
		list.add("Delete");

		/*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
		final ArrayAdapter<String> dataAdapter = new SpinnerAdapter(
	            act,null, R.layout.spinner_item,
	            list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);

		
	}
	
	private void onSpinnerItemClick(String str, Integer pos)
	{
		if(str.equals("Edit"))
		{
			chooseSongsForPlaylist(list.get(pos));
			
			
		}
		else if(str.equals("Delete"))
		{
			playDb.delete(list.get(pos));
			List<String> list = playDb.getAllPlaylist();
			PlaylistActivityListAdapter adap = new PlaylistActivityListAdapter(act, list,viewList);
			viewList.setAdapter(adap);
		}
	}
	
	private void chooseSongsForPlaylist(final String playlist) {
		final List<SongInfo> listTemp = new ArrayList<>();
		AlertDialog.Builder build = new AlertDialog.Builder(
				act);
		build.setTitle("Add Songs");
		final List<SongInfo> info = db.getAllList();
		List<SongInfo> playlistList = getplaylistList(playlist);
		editList.clear();
		editList.addAll(playlistList);
		final List<String> listString = createPathList(editList);
		boolean[] bool = creatBooleanList(playlistList, info);
		
		build.setMultiChoiceItems(convertListToString(info), bool,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						// TODO Auto-generated method stub
						try {

							if (isChecked) {
								if(!listString.contains(info.get(which).getPath()))
									{
									listString.add(info.get(which).getPath());
									editList.add(info.get(which));
									}
							} else {
								if(listString.contains(info.get(which).getPath()))
									{
									listString.remove(info.get(which).getPath());
									removeObj(editList,info.get(which).getPath());
									//editList.remove(info.get(which));
									}

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
				playDb.delete(playlist);
				for (SongInfo songInfo : editList) {
					playDb.insert(songInfo.getPath(),playlist, "yes");
					playDb.updateFav(songInfo.getPath(), songInfo.getFavourite());
				}
				list = playDb.getAllPlaylist();
				PlaylistActivityListAdapter adap = new PlaylistActivityListAdapter(act, list,viewList);
				viewList.setAdapter(adap);
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
	
	private List<SongInfo> getplaylistList(String playlist) {
		List<SongInfo> info = playDb.getAllPlaylistInfo(playlist);
		/*boolean[] bool = new boolean[info.size()];
		for (int i = 0; i < info.size(); i++) {
			bool[i] = info.get(i).isPlaylistState();
			editList.add(info.get(i).getPath());
		}*/
		return info;
	}
	
	private boolean[] creatBooleanList(List<SongInfo> info, List<SongInfo> allSongList)
	{
		boolean[] bool = new boolean[allSongList.size()];
		for(int t =0; t < allSongList.size(); t++)
		{
			bool[t] = false;
		}
		for(int i = 0; i < info.size(); i++)
		{
			for(int j = 0; j < allSongList.size(); j++ )
			{
				if(allSongList.get(j).getPath().equals(info.get(i).getPath()))
				{
					bool[j] = true;
				
				}
			}
		}
		
		return bool;
	}
	
	private List<String> createPathList(List<SongInfo> list)
	{
		List<String> listString = new ArrayList<>();
		for (SongInfo bean : list) {
			listString.add(bean.getPath());
		}
		return listString;
	}
	
	private void removeObj(List<SongInfo> list, String str)
	{
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getPath().equals(str))
			{
				list.remove(i);
			}
		}
	}

}
