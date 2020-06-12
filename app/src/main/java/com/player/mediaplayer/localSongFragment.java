package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.LocalSongsAdapter;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.service.notificationService;
import com.player.mediplayer.beans.SongInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.Fragment;

public class localSongFragment extends Fragment implements TextWatcher {
	Activity act;
	List<SongInfo> tlist = null;
	List<SongInfo> orList = null;
	EditText search = null;
	 ListView listview = null;
	 SongsTable db = null;
	 LocalSongsAdapter adap = null;
	 AudioPlayer player = null;
	 boolean isplayed = false;
	 Timer time = null;
	 boolean firsttime ;
	public localSongFragment(Activity act)
	{
		this.act = act;
	}
 
	public localSongFragment()
	{
		this.act = getActivity();
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	if(act == null)
    	{
    		act = getActivity();
    	}
 
        View rootView = inflater.inflate(R.layout.local_song_listview_fragment, container, false);
        MyDialogFragment activity = (MyDialogFragment)act;
        search = rootView.findViewById(R.id.local_edit);
        search.addTextChangedListener(this);
        activity.searchlocal = search;
        
        if(!activity.isSearchopenLocal)
		{
			search.setVisibility(View.GONE);
			//activity.isSearchopenFolder = !activity.isSearchopenFolder;
		}
		else
		{
			search.setVisibility(View.VISIBLE);
			//activity.isSearchopenFolder = !activity.isSearchopenFolder;
		}
        
        
        db = new SongsTable(act);
		player = AudioPlayer.getInstnace(getActivity());
		tlist = getIntentValue();
		orList = new ArrayList<>();
		orList.addAll(tlist);
		firsttime = true;
		adap = new LocalSongsAdapter(act, orList, orList.size());
        listview = rootView.findViewById(R.id.local_music_list);
        onloadListView(listview);
         
        return rootView;
    }
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	timer();
    	if(tlist.size() == Utils.list.size())
    	{
    		
    		
    		listview.setSelection(AudioPlayer.getInstnace(act).songIndex);
    	}
    }
    
    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	time.cancel();
    }
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
		tlist = searchClick(orList);
		if (tlist == null)
			return;
		setadapterOnlist(tlist);

	}
	private List<SongInfo> getIntentValue() {
		List<SongInfo> list = null;
		Intent intent = act.getIntent();
		if (intent != null) {
			list = (ArrayList<SongInfo>) intent.getSerializableExtra("abcd");
			if (list == null)
				list = db.getAllList();
		} else
			list = db.getAllList();
		return list;
	}
	private void setadapterOnlist(List<SongInfo> li) {
		adap =new LocalSongsAdapter(act, li,li.size());
		listview.setAdapter(adap);
	}

	private List<SongInfo> searchClick(List<SongInfo> filterAssignTask) {

		List<SongInfo> filtersearchlist = new ArrayList<SongInfo>();

		// if(filtercompletelist!=null)
		// filterAssignTask=filtercompletelist;
		if (filterAssignTask == null)
			return null;
		String str = search.getText().toString();
		if(str == null)
		{
				return null;
		}
		for (int i = 0; i < filterAssignTask.size(); i++) {
			String s = str.toLowerCase().trim();
			String songName = getSongName(filterAssignTask.get(i).getPath()
					.toLowerCase().trim());

			if (songName.contains(s)) {
				// if(user[value].equals(filterAssignTask.get(i).getAssign_username()))
				filtersearchlist.add(filterAssignTask.get(i));

			}
		}

		return filtersearchlist;
	}

	private String getSongName(String path) {
		String str = path.substring(path.lastIndexOf("/") + 1,
				path.lastIndexOf("."));

		return str;
	}
	
	public void onloadListView(ListView listview) {
		// TODO Auto-generated method stub
		
		
		
		//search.addTextChangedListener(this);
		//listview.setAdapter(setadapterOnlist(tlist));
		setadapterOnlist(tlist);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				AudioPlayer player = AudioPlayer
						.getInstnace(act);

				if (!player.getMediaPlayer().isPlaying()) {
					player.getMediaPlayer().stop();
					player.getMediaPlayer().reset();
				}
				player.fromwhere = ((MyDialogFragment)getActivity()).str;
				Utils.list = tlist;
				int index = position;

				Intent inte1 = new Intent(act,
						notificationService.class);
				inte1.putExtra("song", index);
				inte1.putExtra("work", "" + AppConstant.START_BIGINING);
				act.startService(inte1);
				//act.finish();
				act.startActivity(new Intent(act, homeActivity.class));

			}
		});
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	private void timer()
	{
		
		time = new Timer();
		time.schedule(new TimerTask() {
			
			@Override
			public void run() {
				act.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if(player.getMediaPlayer().isPlaying())
							isplayed = true;
						if(isplayed)
						if(adap.getlist().size()==orList.size() && Utils.list.size() == orList.size() && player.fromwhere.equals("All"/*((MyDialogFragment)getActivity()).str)*/))
						{
							if(player.songIndex>0)
							{
								
								View view =	adap.getView(player.songIndex-1,null,null);
								ImageView img = view.findViewById(R.id.localsongsound);
								img.setVisibility(View.INVISIBLE);
							}
							int playerindex = player.songIndex;
							int listsize = orList.size()-1;
							if(player.songIndex < orList.size()-1)
							{
								int ind =player.songIndex+1;
								View view =	adap.getView(player.songIndex+1,null,null);
								ImageView img = view.findViewById(R.id.localsongsound);
								img.setVisibility(View.INVISIBLE);
							}
							View view = null;
							/*if(player.fromwhere.equals("Recent Played") && firsttime)
							{
								view =	adap.getView(0,null,null);
								
								for(int i=0 ; i< Utils.list.size();i++)
								{
									if(Utils.list.get(i).getPath().equals(player.song))
									{
										player.songIndex = i;
									}
								}
								//Utils.list.indexOf()
								firsttime = false;
								
							}
							else
							{
								view =	adap.getView(player.songIndex,null,null);
							}*/
							view =	adap.getView(player.songIndex,null,null);
						ImageView img = view.findViewById(R.id.localsongsound);
						img.setVisibility(View.VISIBLE);
						}
					}
				});
				// TODO Auto-generated method stub
				
				/*if(map !=null)
				{
					
				}*/
			}
		}, 10, 3000);
	}

}