package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.LocalSongsAdapter;
import com.player.mediaplayer.adapter.TabsPagerAdapter;
import com.player.mediaplayer.adapter.ViewPagerForLocalSongs;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.constant.localSongwidgetFromFragmentListner;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.service.notificationService;
import com.player.mediplayer.beans.SongInfo;
import com.splunk.mint.Mint;

public class MyDialogFragment extends FragmentActivity implements OnClickListener {

	List<SongInfo> tlist = null;
	SongsTable db = null;
	private ListView listview = null;
	private ExpandableListView expListArtist, expListFolder,expListAlbum;
	public EditText searchArtist, searchlocal,searchAlbum,searchFolder,search;
	public boolean isSearchopenFolder = false, isSearchopenArtist = false, isSearchopenAlbum = false, isSearchopenLocal = false;
	private ViewPager viewPager;
	private ViewPagerForLocalSongs mAdapter;
	private TextView local, artist, album,folder;
	public String str="";
	private View local_blue, artist_blue, album_blue, folder_blue;
	private boolean isLocal,isArtis,isAlbum,isFolder;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(this, "e45fc306");
		setContentView(R.layout.local_song_main_activity);
		loadWidget();
		db = new SongsTable(MyDialogFragment.this);
		
		//tlist = getIntentValue();
		mAdapter = new ViewPagerForLocalSongs(getSupportFragmentManager(), this);
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(4);
		
		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				// actionBar.setSelectedNavigationItem(position);
				switch (position) {
				case 0:

					// viewPager.setCurrentItem(0);
					local_blue.setVisibility(View.VISIBLE);
					artist_blue.setVisibility(View.INVISIBLE);
					album_blue.setVisibility(View.INVISIBLE);
					folder_blue.setVisibility(View.INVISIBLE);
					//search = searchlocal;
					//searchAlbum.setVisibility(View.g)
					isLocal = true;
					isArtis = false;
					isAlbum = false;
					isFolder = false;
					if(searchlocal != null)
					{
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchlocal.getWindowToken(), 0);
					}
					break;
				case 1:
					// viewPager.setCurrentItem(1);
					local_blue.setVisibility(View.INVISIBLE);
					artist_blue.setVisibility(View.VISIBLE);
					album_blue.setVisibility(View.INVISIBLE);
					folder_blue.setVisibility(View.INVISIBLE);
					//search = searchArtist;
					isLocal = false;
					isArtis = true;
					isAlbum = false;
					isFolder = false;
					if(searchArtist != null)
					{
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchArtist.getWindowToken(), 0);
					}
					break;
				case 2:
					// viewPager.setCurrentItem(2);
					local_blue.setVisibility(View.INVISIBLE);
					artist_blue.setVisibility(View.INVISIBLE);
					album_blue.setVisibility(View.VISIBLE);
					folder_blue.setVisibility(View.INVISIBLE);
					//search = searchAlbum;
					isLocal = false;
					isArtis = false;
					isAlbum = true;
					isFolder = false;
					if(searchAlbum != null)
					{
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchAlbum.getWindowToken(), 0);
					}
					
					break;
				case 3:
					// viewPager.setCurrentItem(2);
					local_blue.setVisibility(View.INVISIBLE);
					artist_blue.setVisibility(View.INVISIBLE);
					album_blue.setVisibility(View.INVISIBLE);
					folder_blue.setVisibility(View.VISIBLE);
					//search = searchFolder;
					isLocal = false;
					isArtis = false;
					isAlbum = false;
					isFolder = true;
					if(searchFolder != null)
					{
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(searchFolder.getWindowToken(), 0);
					}
					
					
					break;

				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		intentValueForScreen();
		changeLocalSongName();


		/*listview.setAdapter(new LocalSongsAdapter(MyDialogFragment.this, tlist));
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				AudioPlayer player = AudioPlayer
						.getInstnace(MyDialogFragment.this);

				if (!player.getMediaPlayer().isPlaying()) {
					player.getMediaPlayer().stop();
					player.getMediaPlayer().reset();
				}

				Utils.list = tlist;
				int index = position;

				Intent inte1 = new Intent(MyDialogFragment.this,
						notificationService.class);
				inte1.putExtra("song", index);
				inte1.putExtra("work", "" + AppConstant.START_BIGINING);
				MyDialogFragment.this.startService(inte1);
				finish();

			}
		});*/

	}
	private void changeLocalSongName()
	{
		//String str;
		Intent intent = getIntent();
		if (intent != null) {
			str = (String) intent.getSerializableExtra("scr");
			if (str == null)
				str = "All";
			
		}
		else
			str = "All";
		local.setText(str);
		
	}
	
	private void intentValueForScreen()
	{
		String str ;
		Intent intent = getIntent();
		if (intent != null) {
			str = (String) intent.getSerializableExtra("screen");
			if (str == null)
				str = "local";
			
		}
		else
			str = "local";
		int i = -1;
		
		
		 if(str.equals("local"))
			i=0;
		 else if(str.equals("Artist"))
			i=1;
		else if(str.equals("Album"))
			i=2;
		else if(str.equals("Folder"))
			i=3;
		switch (i) {
		
		
	case 0:

		viewPager.setCurrentItem(0);
		local_blue.setVisibility(View.VISIBLE);
		artist_blue.setVisibility(View.INVISIBLE);
		album_blue.setVisibility(View.INVISIBLE);
		folder_blue.setVisibility(View.INVISIBLE);
		//search = searchlocal;
		isLocal = true;
		isArtis = false;
		isAlbum = false;
		isFolder = false;
		break;
	case 1:
		viewPager.setCurrentItem(1);
		local_blue.setVisibility(View.INVISIBLE);
		artist_blue.setVisibility(View.VISIBLE);
		album_blue.setVisibility(View.INVISIBLE);
		folder_blue.setVisibility(View.INVISIBLE);
		//search = searchArtist;
		isLocal = false;
		isArtis = true;
		isAlbum = false;
		isFolder = false;
		break;
	case 2:
		viewPager.setCurrentItem(2);
		local_blue.setVisibility(View.INVISIBLE);
		artist_blue.setVisibility(View.INVISIBLE);
		album_blue.setVisibility(View.VISIBLE);
		folder_blue.setVisibility(View.INVISIBLE);
		//search = searchAlbum;
		isLocal = false;
		isArtis = false;
		isAlbum = true;
		isFolder = false;
		break;
		
	case 3:
		viewPager.setCurrentItem(3);
		local_blue.setVisibility(View.INVISIBLE);
		artist_blue.setVisibility(View.INVISIBLE);
		album_blue.setVisibility(View.INVISIBLE);
		folder_blue.setVisibility(View.VISIBLE);
		//search = searchFolder;
		isLocal = false;
		isArtis = false;
		isAlbum = false;
		isFolder = true;
		break;


	default:
		break;
		}

	}

	private void loadWidget() {
		viewPager = findViewById(R.id.pager);

		local = findViewById(R.id.home_my_tab);
		local.setOnClickListener(this);
		artist = findViewById(R.id.home_feature_tab);
		artist.setOnClickListener(this);
		album = findViewById(R.id.home_download_tab);
		album.setOnClickListener(this);
		folder = findViewById(R.id.home_folder);
		folder.setOnClickListener(this);

		
		local_blue = findViewById(R.id.home_my_tab_blue);
		local_blue.setVisibility(View.VISIBLE);
		artist_blue = findViewById(R.id.home_feature_tab_blue);
		album_blue = findViewById(R.id.home_download_tab_blue);
		folder_blue = findViewById(R.id.home_folder_blue);

		ImageView back = findViewById(R.id.local_music_back);
		back.setOnClickListener(this);
		ImageView musicSearch = findViewById(R.id.local_music_search);
		musicSearch.setOnClickListener(this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	/*private List<SongInfo> getIntentValue() {
		List<SongInfo> list = null;
		Intent intent = getIntent();
		if (intent != null) {
			list = (ArrayList<SongInfo>) intent.getSerializableExtra("abcd");
			if (list == null)
				list = db.getAllList();
		} else
			list = db.getAllList();
		return list;
	}*/

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//if (isSearchopen) {
		//	search.setVisibility(View.GONE);
		//	isSearchopen = !isSearchopen;
		//	setadapterOnlist(tlist);
		//}
		//else
					//super.onBackPressed();
		startActivity(new Intent(MyDialogFragment.this, homeActivity.class));

	}

	private int findIndex(String song) {
		for (int i = 0; i < Utils.list.size(); i++) {
			if (Utils.list.get(i).getPath().equals(song))
				return i;
		}
		return 0;
	}

/*	@Override
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
		tlist = searchClick(tlist);
		if (tlist == null)
			return;
		setadapterOnlist(tlist);

	}

	private void setadapterOnlist(List<SongInfo> li) {

		listview.setAdapter(new LocalSongsAdapter(MyDialogFragment.this, li));
	}

	private List<SongInfo> searchClick(List<SongInfo> filterAssignTask) {

		List<SongInfo> filtersearchlist = new ArrayList<SongInfo>();

		// if(filtercompletelist!=null)
		// filterAssignTask=filtercompletelist;
		if (filterAssignTask == null)
			return null;
		String str = search.getText().toString();
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
	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.local_music_back:
			//finish();
			startActivity(new Intent(MyDialogFragment.this, homeActivity.class));
			break;
		case R.id.local_music_search:
			
				
					if(isFolder)
					{
						if(isSearchopenFolder)
						{
							searchFolder.setVisibility(View.GONE);
							isSearchopenFolder = !isSearchopenFolder;
						}
						else
						{
							searchFolder.setVisibility(View.VISIBLE);
							isSearchopenFolder = !isSearchopenFolder;
						}
					}
						
					if(isAlbum)
						
					{
						if(isSearchopenAlbum)
						{
							searchAlbum.setVisibility(View.GONE);
							isSearchopenAlbum = !isSearchopenAlbum;
						}
						else
						{
							searchAlbum.setVisibility(View.VISIBLE);
							isSearchopenAlbum = !isSearchopenAlbum;
						}
					}
						
					if (isArtis) 
					{
						if(isSearchopenArtist)
						{
							searchArtist.setVisibility(View.GONE);
							isSearchopenArtist = !isSearchopenArtist;
						}
						else
						{
							searchArtist.setVisibility(View.VISIBLE);
							isSearchopenArtist = !isSearchopenArtist;
						}
					}
						
					if(isLocal)
					{
						if(isSearchopenLocal)
						{
							searchlocal.setVisibility(View.GONE);
							isSearchopenLocal = !isSearchopenLocal;
						}
						else
						{
							searchlocal.setVisibility(View.VISIBLE);
							isSearchopenLocal = !isSearchopenLocal;
						}
					}
			
			break;
		case R.id.home_my_tab:

			viewPager.setCurrentItem(0);
			local_blue.setVisibility(View.VISIBLE);
			artist_blue.setVisibility(View.INVISIBLE);
			album_blue.setVisibility(View.INVISIBLE);
			folder_blue.setVisibility(View.INVISIBLE);
			//search = searchlocal;
			isLocal = true;
			isArtis = false;
			isAlbum = false;
			isFolder = false;
			
			
				
			break;
		case R.id.home_feature_tab:
			viewPager.setCurrentItem(1);
			local_blue.setVisibility(View.INVISIBLE);
			artist_blue.setVisibility(View.VISIBLE);
			album_blue.setVisibility(View.INVISIBLE);
			folder_blue.setVisibility(View.INVISIBLE);
			//search = searchArtist;
			isLocal = false;
			isArtis = true;
			isAlbum = false;
			isFolder = false;
			break;
		case R.id.home_download_tab:
			viewPager.setCurrentItem(2);
			local_blue.setVisibility(View.INVISIBLE);
			artist_blue.setVisibility(View.INVISIBLE);
			album_blue.setVisibility(View.VISIBLE);
			folder_blue.setVisibility(View.INVISIBLE);
			//search = searchAlbum;
			isLocal = false;
			isArtis = false;
			isAlbum = true;
			isFolder =false;
			break;
			
		case R.id.home_folder:
			viewPager.setCurrentItem(3);
			local_blue.setVisibility(View.INVISIBLE);
			artist_blue.setVisibility(View.INVISIBLE);
			album_blue.setVisibility(View.INVISIBLE);
			folder_blue.setVisibility(View.VISIBLE);
			//search = searchFolder;
			isLocal = false;
			isArtis = false;
			isAlbum = false;
			isFolder = true;
			break;


		default:
			break;
		}
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	

	}

	/*@Override
	public void onloadListView(ListView listview, EditText search) {
		// TODO Auto-generated method stub
		searchlocal = search;
		
		this.listview = listview;
		//search.addTextChangedListener(this);
		listview.setAdapter(new LocalSongsAdapter(MyDialogFragment.this, tlist));
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				AudioPlayer player = AudioPlayer
						.getInstnace(MyDialogFragment.this);

				if (!player.getMediaPlayer().isPlaying()) {
					player.getMediaPlayer().stop();
					player.getMediaPlayer().reset();
				}

				Utils.list = tlist;
				int index = position;

				Intent inte1 = new Intent(MyDialogFragment.this,
						notificationService.class);
				inte1.putExtra("song", index);
				inte1.putExtra("work", "" + AppConstant.START_BIGINING);
				MyDialogFragment.this.startService(inte1);
				finish();

			}
		});
		
	}

	@Override
	public void onloadExpendableListViewArtist(ExpandableListView expList,
			EditText edit) {
		// TODO Auto-generated method stub
		expListArtist = expList;
		searchArtist = edit;
		
	}
	@Override
	public void onloadExpendableListViewAlbum(ExpandableListView expList,
			EditText edit) {
		// TODO Auto-generated method stub
		expListAlbum = expList;
		searchAlbum = edit;
		
	}
	@Override
	public void onloadExpendableListViewFolder(ExpandableListView expList,
			EditText edit) {
		// TODO Auto-generated method stub
		expListFolder = expList;
		searchFolder = edit;
		
	}

*/	

}
