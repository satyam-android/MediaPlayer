package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

import androidx.fragment.app.Fragment;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.ExpandableListAdapterArtist;
import com.player.mediaplayer.adapter.LocalSongsAdapter;
import com.player.mediaplayer.adapter.artistListAdapter;

import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.constant.localSongwidgetFromFragmentListner;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.service.notificationService;
import com.player.mediplayer.beans.SongInfo;
import com.player.mediplayer.beans.adapterBean;

public class albumSongFragment extends Fragment implements TextWatcher {
	Activity act;
	private HashMap<String, ArrayList<SongInfo>> listMap = null;
	private List<String> list = null;
	private ExpandableListAdapterArtist adap = null;
	private loadSongInfo loads = null;
	private boolean isthreadRunning = true;
	private SongsTable db = null;
	private SharedPreferences pref = null;
	private List<String> tempList = null;
	
	private HashMap<Integer, Boolean> hasmap = null;
	private EditText search = null;
	private List<String> tlist = null;
	private ExpandableListView expList = null;

	public albumSongFragment(Activity act) {
		this.act = act;
	}

	public albumSongFragment() {
		this.act = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (act == null) {
			act = getActivity();
		}
		
		View rootView = inflater.inflate(R.layout.expandable_list_view_artist,
				container, false);
		MyDialogFragment activity = (MyDialogFragment) act;
		search = rootView.findViewById(R.id.local_edit);
		activity.searchAlbum = search;
		hasmap = new HashMap<>();
		search.addTextChangedListener(this);

		if (!activity.isSearchopenAlbum) {
			search.setVisibility(View.GONE);
			// isSearchopenAlbum = !isSearchopenAlbum;
		} else {
			search.setVisibility(View.VISIBLE);
			// isSearchopenAlbum = !isSearchopenAlbum;
		}
		expList = rootView
				.findViewById(R.id.local_music_list);
		// expList = expList;
		db = new SongsTable(act);
		pref = PreferenceManager.getDefaultSharedPreferences(act
				.getApplicationContext());
		Boolean isFirst = pref.getBoolean("album", true);

		// isFirst = false;

		listMap = new HashMap<>();
		list = new ArrayList<>();
		tempList = new ArrayList<>();
		loads = new loadSongInfo();
		try {
			if (isFirst) {

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					loads.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					loads.execute();
				}
				adap = new ExpandableListAdapterArtist(act, list, listMap);
			} else {
				adap = adapterBean.getInstance().getExplistAlbum();
				if (adap == null) {
					makedbList();
					adap = new ExpandableListAdapterArtist(act, list, listMap);
					adapterBean.getInstance().setExplistAlbum(adap);
				} else {
					list = adap.getList();
					listMap = adap.getListMap();
				}

			}
		} catch (Exception e) {

		}
		// makedbList();
		tlist = new ArrayList<>();
		tlist.addAll(list);

		// localSongwidgetFromFragmentListner listner = (MyDialogFragment)act;
		// listner.onloadExpendableListViewAlbum(expList, search);
		// adap = new ExpandableListAdapterArtist(act, list, listMap);
		expList.setAdapter(adap);

		/*
		 * expList.setOnGroupClickListener(new OnGroupClickListener() {
		 * 
		 * @Override public boolean onGroupClick(ExpandableListView parent, View
		 * v, int groupPosition, long id) {
		 * 
		 * 
		 * return true; } });
		 */

		expList.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				boolean isdropopen ;
				if(hasmap.get(groupPosition) == null)
					isdropopen = false;
				else
					isdropopen = hasmap.get(groupPosition);
				ImageView img = v.findViewById(R.id.dropimg);
				if (!isdropopen) {
					img.setImageDrawable(getResources().getDrawable(
							R.drawable.drop_up_icon));
					isdropopen = !isdropopen;
				} else {
					img.setImageDrawable(getResources().getDrawable(
							R.drawable.drop_dwon));
					isdropopen = !isdropopen;
				}
				hasmap.put(groupPosition, isdropopen);
				return false;
			}
		});
		expList.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				String str = adap.getGroupList().get(groupPosition);
				ArrayList<SongInfo> tlist = listMap.get(str);

				AudioPlayer player = AudioPlayer.getInstnace(act);

				if (!player.getMediaPlayer().isPlaying()) {
					player.getMediaPlayer().stop();
					player.getMediaPlayer().reset();
				}

				Utils.list = tlist;
				int index = childPosition;

				Intent inte1 = new Intent(act, notificationService.class);
				inte1.putExtra("song", index);
				inte1.putExtra("work", "" + AppConstant.START_BIGINING);
				act.startService(inte1);
				// act.finish();
				act.startActivity(new Intent(act, homeActivity.class));

				return true;
			}
		});
		//

		return rootView;
	}

	private class loadSongInfo extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {

				makeList();
				adapterBean.getInstance().setExplistAlbum(
						new ExpandableListAdapterArtist(act, list, listMap));
			} catch (Exception e) {
				// TODO: handle exception
			}
			return "";
		}

	}

	private void makedbList() {
		List<SongInfo> songList = db.getAllList();
		for (int i = 0; i < songList.size(); i++) {

			String path = songList.get(i).getPath();
			String album = songList.get(i).getAlbum();

			ArrayList<SongInfo> arr = listMap.get(album);
			if (arr == null) {
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
				listMap.put(album, arr);
				list.add(album);
			} else {
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				bean.setAlbum(album);
				arr.add(bean);
			}

			/*
			 * runOnUiThread(new Runnable() { public void run() { if(adap !=
			 * null) { List<String> li = adap.getList(); li.addAll(tempList);
			 * tempList.clear(); adap.notifyDataSetChanged(); } } });
			 */

		}

	}

	private void makeList() {
		List<SongInfo> songList = db.getAllList();
		for (int i = 0; i < songList.size(); i++) {

			String path = songList.get(i).getPath();
			String album = Utils.getAudioAlbum(path, act);
			db.updateAlbum(path, album);
			// /playlistDB.updateAlbum(path, album);
			ArrayList<SongInfo> arr = listMap.get(album);
			if (arr == null) {
				arr = new ArrayList<>();
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				arr.add(bean);
				// arr.add(path);
				listMap.put(album, arr);
				tempList.add(album);
			} else {
				SongInfo bean = new SongInfo();
				bean.setPath(path);
				arr.add(bean);
			}

			act.runOnUiThread(new Runnable() {
				public void run() {
					if (adap != null) {
						List<String> li = adap.getList();
						li.addAll(tempList);
						tempList.clear();
						adap.notifyDataSetChanged();
					}
				}
			});

		}
		Editor edit = pref.edit();
		edit.putBoolean("album", false);
		edit.commit();

	}

	/*
	 * private String getSongName(String path) { return
	 * path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf(".")); }
	 */

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		tlist = searchClick(list);
		if (tlist == null)
			return;
		setadapterOnlist(tlist);

	}

	private void setadapterOnlist(List<String> li) {
		adap = new ExpandableListAdapterArtist(act, li, listMap);

		expList.setAdapter(adap);
	}

	private List<String> searchClick(List<String> filterAssignTask) {

		List<String> filtersearchlist = new ArrayList<String>();

		// if(filtercompletelist!=null)
		// filterAssignTask=filtercompletelist;
		if (filterAssignTask == null)
			return null;
		String str = search.getText().toString();
		if (str == null) {
			return null;
		}
		for (int i = 0; i < filterAssignTask.size(); i++) {

			String s1 = str.toLowerCase();
			if (s1 == null)
				return null;
			String s = s1.trim();
			if (s == null)
				return null;
			String songN = filterAssignTask.get(i);
			if (songN == null)
				return null;
			String songNam = songN.toLowerCase();
			if (songNam == null)
				return null;
			String songName = songNam.trim();
			if (songName == null)
				return null;
			// .toLowerCase().trim();

			if (songName.contains(s)) {
				// if(user[value].equals(filterAssignTask.get(i).getAssign_username()))
				filtersearchlist.add(filterAssignTask.get(i));

			}
		}

		return filtersearchlist;
	}

}