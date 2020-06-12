package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.List;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.LocalSongsAdapter;
import com.player.mediaplayer.adapter.SpinnerAdapter;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.NDSpinner;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.service.notificationService;
import com.player.mediplayer.beans.SongInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class MyFragment extends Fragment implements OnClickListener {
	private TextView sizeText = null;
	LinearLayout main_song_tab, favourite, download, playlist, recent_played,
			recent_added, artist, album, folder, layout1, layout2, layout3,
			layout4,actual_layout_1,actual_layout_2,actual_layout_3,actual_layout_4;
	Activity act;
	private SharedPreferences pref = null;
	private boolean favouriteValue, recentValue, downloadValue, playlistValue,
			addedRecentValue, artistValue, albumValue, folderValue;
	List<String> notInUI, inUI;
	private String[] dialogValue;
	private boolean[] dialogBoolean;
	private NDSpinner spinner_person;
	private TextView text1, text2, text3, text4, text5, text6, text7, text8;
	private List<TextView> textviewList = null;
	private List<LinearLayout> linearLayoutList = null;
	private boolean isSpinnerFirstTime;
	private SongsTable db = null;
	private View rootView = null;
	public MyFragment(Activity act) {
		this.act = act;
	}
	public MyFragment() {
		this.act = getActivity();
	}
	
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(act == null)
		{
			act = getActivity();
		}
		notInUI = new ArrayList<>();
		inUI = new ArrayList<>();
		textviewList = new ArrayList<>();
		linearLayoutList = new ArrayList<>();
		dialogValue = new String[8];
		dialogBoolean = new boolean[8];
		
		db = new SongsTable(getActivity());
		rootView = inflater
				.inflate(R.layout.my_fragment, container, false);
		

		return rootView;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(act == null)
		{
			act = getActivity();
		}
		isSpinnerFirstTime = true;
		uipreference();
		loadwidget(rootView);
		changeLayout();
	}

	private void loadwidget(View view) {
		sizeText = view.findViewById(R.id.my_main_tab_song_size);
		sizeText.setText("" + Utils.list.size() + " songs");
		homeActivity hom = (homeActivity) act;
		hom.setFragmentTextView(sizeText);
		main_song_tab = view.findViewById(R.id.my_main_tab);
		main_song_tab.setOnClickListener(this);
		main_song_tab.setEnabled(true);
		favourite = view.findViewById(R.id.my_my_favorite);
		//favourite.setOnClickListener(this);
		linearLayoutList.add(favourite);
		download = view.findViewById(R.id.my_my_download);
		//download.setOnClickListener(this);
		linearLayoutList.add(download);
		playlist = view.findViewById(R.id.my_my_playlist);
		//playlist.setOnClickListener(this);
		linearLayoutList.add(playlist);
		recent_played = view
				.findViewById(R.id.my_my_recent_played);
		///recent_played.setOnClickListener(this);
		linearLayoutList.add(recent_played);
		recent_added = view
				.findViewById(R.id.my_my_Recent_Added);
		//recent_added.setOnClickListener(this);
		linearLayoutList.add(recent_added);
		artist = view.findViewById(R.id.my_artist);
		//artist.setOnClickListener(this);
		linearLayoutList.add(artist);
		album = view.findViewById(R.id.my_album);
		//album.setOnClickListener(this);
		linearLayoutList.add(album);
		folder = view.findViewById(R.id.my_folder);
		//folder.setOnClickListener(this);
		linearLayoutList.add(folder);
		layout1 = view.findViewById(R.id.my_layout1);
		layout2 = view.findViewById(R.id.my_layout2);
		layout3 = view.findViewById(R.id.my_layout3);
		layout4 = view.findViewById(R.id.my_layout4);
		
		actual_layout_1 = view.findViewById(R.id.actual_layout_1);
		actual_layout_2 = view.findViewById(R.id.actual_layout_2);
		actual_layout_3 = view.findViewById(R.id.actual_layout_3);
		actual_layout_4 = view.findViewById(R.id.actual_layout_4);

		text1 = view.findViewById(R.id.text_1);
		text2 = view.findViewById(R.id.text_2);
		text3 = view.findViewById(R.id.text_3);
		text4 = view.findViewById(R.id.text_4);
		text5 = view.findViewById(R.id.text_5);
		text6 = view.findViewById(R.id.text_6);
		text7 = view.findViewById(R.id.text_7);
		text8 = view.findViewById(R.id.text_8);
		
		text1.setOnClickListener(this);
		text2.setOnClickListener(this);
		text3.setOnClickListener(this);
		text4.setOnClickListener(this);
		text5.setOnClickListener(this);
		text6.setOnClickListener(this);
		text7.setOnClickListener(this);
		text8.setOnClickListener(this);
		

		textviewList.add(text1);
		textviewList.add(text2);
		textviewList.add(text3);
		textviewList.add(text4);
		textviewList.add(text5);
		textviewList.add(text6);
		textviewList.add(text7);
		textviewList.add(text8);

		loadSpinner(view);
		// ImageView main_tab_option = (ImageView) view
		// .findViewById(R.id.my_main_tab_option);
		// main_tab_option.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.my_main_tab:
			
			//main_song_tab.setEnabled(false);
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("screen", "local");
			intent.putExtra("scr", "All");
			startActivity(intent);
			
			break;
		case R.id.text_1:
			onTextClick(text1.getText().toString());
			break;
		case R.id.text_2:
			onTextClick(text2.getText().toString());
			break;
		case R.id.text_3:
			onTextClick(text3.getText().toString());
			break;
		case R.id.text_4:
			onTextClick(text4.getText().toString());
			break;
		case R.id.text_5:
			onTextClick(text5.getText().toString());
			break;
		case R.id.text_6:
			onTextClick(text6.getText().toString());
			break;
		case R.id.text_7:
			onTextClick(text7.getText().toString());
			break;
		case R.id.text_8:
			onTextClick(text8.getText().toString());
			break;

		default:
			break;
		}

	}

	private void loadSpinner(View v) {
		spinner_person = v.findViewById(R.id.my_main_tab_option);
		List<String> list = new ArrayList<String>();
		/*if(notInUI.size() == 0)
			list.add("All Songs");*/
		list.addAll(notInUI);
		list.add("Customize UI");

		/*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.spinner_item, list);
		dataAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
		final ArrayAdapter<String> dataAdapter = new SpinnerAdapter(
	            getActivity(),MyFragment.this, R.layout.spinner_item,
	            list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_person.setAdapter(dataAdapter);

		spinner_person.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String selectedString = (String) arg0.getItemAtPosition(arg2);
				if(!isSpinnerFirstTime)
				{
					
				customizeValue(selectedString);
				}
				isSpinnerFirstTime = false;

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				//String num_person = "1-20";
			}
			
			
		});
	}

	private void uipreference() {
		notInUI.clear();
		inUI.clear();
		pref = PreferenceManager.getDefaultSharedPreferences(act);
		favouriteValue = pref.getBoolean("favouriteValue", true);
		if (favouriteValue)
			inUI.add("My Favourite");
		else
			notInUI.add("My Favourite");
		dialogValue[0] = "My Favourite";
		dialogBoolean[0] = favouriteValue;

		recentValue = pref.getBoolean("recentValue", true);
		if (recentValue)
			inUI.add("Recent Played");
		else
			notInUI.add("Recent Played");
		dialogValue[1] = "Recent Played";
		dialogBoolean[1] = recentValue;

		downloadValue = pref.getBoolean("downloadValue", true);
		if (downloadValue)
			inUI.add("My Download");
		else
			notInUI.add("My Download");
		dialogValue[2] = "My Download";
		dialogBoolean[2] = downloadValue;

		playlistValue = pref.getBoolean("playlistValue", true);
		if (playlistValue)
			inUI.add("My Playlist");
		else
			notInUI.add("My Playlist");
		dialogValue[3] = "My Playlist";
		dialogBoolean[3] = playlistValue;

		addedRecentValue = pref.getBoolean("addedRecentValue", false);
		if (addedRecentValue)
			inUI.add("Recently Added");
		else
			notInUI.add("Recently Added");
		dialogValue[4] = "Recently Added";
		dialogBoolean[4] = addedRecentValue;

		artistValue = pref.getBoolean("artistValue", false);
		if (artistValue)
			inUI.add("Artist");
		else
			notInUI.add("Artist");
		dialogValue[5] = "Artist";
		dialogBoolean[5] = artistValue;

		albumValue = pref.getBoolean("albumValue", false);
		if (albumValue)
			inUI.add("Album");
		else
			notInUI.add("Album");
		dialogValue[6] = "Album";
		dialogBoolean[6] = albumValue;

		folderValue = pref.getBoolean("folderValue", false);
		if (folderValue)
			inUI.add("Folder");
		else
			notInUI.add("Folder");
		dialogValue[7] = "Folder";
		dialogBoolean[7] = folderValue;

	}

	private void customizeValue(String selectedString) {
		if (selectedString.equals("Customize UI" )) {
			// Boolean dialogBoolean[] = ;
			AlertDialog.Builder build = new AlertDialog.Builder(act);
			/*build.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					List<String> list = new ArrayList<String>();
					if(notInUI.size() == 0)
						list.add("All Songs");
					list.addAll(notInUI);
					list.add("Customize UI");
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
							getActivity(), R.layout.spinner_item, list);
					dataAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					final ArrayAdapter<String> dataAdapter = new SpinnerAdapter(
				            getActivity(),MyFragment.this, R.layout.spinner_item,
				            list);
					dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinner_person.setAdapter(dataAdapter);
					isSpinnerFirstTime = true;
				}
			});
			*/
			build.setMultiChoiceItems(dialogValue, dialogBoolean,
					new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							// TODO Auto-generated method stub
							// if(!inUI.contains(""))
							// {
							// inUI.add(dialogValue[which]);
							// }
							// if(!notInUI.contains(""))
							// {
							// notInUI.add(dialogValue[which]);
							// }
							dialogBoolean[which] = isChecked;

						}
					});

			build.setPositiveButton("Okay",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							changePrefValue();
							changeLayout();
							/* change adapter of spinner */
							List<String> list = new ArrayList<String>();
							/*if(notInUI.size() == 0)
								list.add("All Songs");*/
							list.addAll(notInUI);
							list.add("Customize UI");
							/*ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
									getActivity(), R.layout.spinner_item, list);
							dataAdapter
									.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
							final ArrayAdapter<String> dataAdapter = new SpinnerAdapter(
						            getActivity(),MyFragment.this, R.layout.spinner_item,
						            list);
							dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
							spinner_person.setAdapter(dataAdapter);
							isSpinnerFirstTime = true;

						}
					});
			
			AlertDialog di =build.show();
			
			

		}else
			onTextClick(selectedString);
	}
	

	private void changePrefValue() {
		notInUI.clear();
		inUI.clear();
		// pref = PreferenceManager.getDefaultSharedPreferences(act);
		Editor edit = pref.edit();
		edit.putBoolean("favouriteValue", dialogBoolean[0]);
		if (dialogBoolean[0])
			inUI.add("My Favourite");
		else
			notInUI.add("My Favourite");

		edit.putBoolean("recentValue", dialogBoolean[1]);
		if (dialogBoolean[1])
			inUI.add("Recent Played");
		else
			notInUI.add("Recent Played");

		edit.putBoolean("downloadValue", dialogBoolean[2]);
		if (dialogBoolean[2])
			inUI.add("My Download");
		else
			notInUI.add("My Download");

		edit.putBoolean("playlistValue", dialogBoolean[3]);
		if (dialogBoolean[3])
			inUI.add("My Playlist");
		else
			notInUI.add("My Playlist");

		edit.putBoolean("addedRecentValue", dialogBoolean[4]);
		if (dialogBoolean[4])
			inUI.add("Recently Added");
		else
			notInUI.add("Recently Added");

		edit.putBoolean("artistValue", dialogBoolean[5]);
		if (dialogBoolean[5])
			inUI.add("Artist");
		else
			notInUI.add("Artist");

		edit.putBoolean("albumValue", dialogBoolean[6]);
		if (dialogBoolean[6])
			inUI.add("Album");
		else
			notInUI.add("Album");

		edit.putBoolean("folderValue", dialogBoolean[7]);
		if (dialogBoolean[7])
			inUI.add("Folder");
		else
			notInUI.add("Folder");

		edit.commit();

	}

	private void changeLayout() {
		for(int i =0; i < linearLayoutList.size(); i++)
		{
			linearLayoutList.get(i).setVisibility(View.INVISIBLE);
		}
		for (int i = 0; i < inUI.size(); i++) {
			String value = inUI.get(i);
			textviewList.get(i).setText(value);
			if(value.equals("My Favourite"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.dislike, 0, 0, 0);
			}
			else if(value.equals("Recent Played"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.recent_played, 0, 0, 0);
			}
			else if(value.equals("My Download"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.download, 0, 0, 0);
			}
			else if(value.equals("My Playlist"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.playlist, 0, 0, 0);
			}
			else if(value.equals("Recently Added"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.recent_add, 0, 0, 0);
			}
			else if(value.equals("Artist"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.artist, 0, 0, 0);
			}
			else if(value.equals("Album"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.album, 0, 0, 0);
			}
			else if(value.equals("Folder"))
			{
				TextView text = textviewList.get(i);
				text.setCompoundDrawablesWithIntrinsicBounds( R.drawable.folder, 0, 0, 0);
			}
			linearLayoutList.get(i).setVisibility(View.VISIBLE);
		}
		

		switch (inUI.size()) {
		case 0:
			actual_layout_1.setVisibility(View.GONE);
			actual_layout_2.setVisibility(View.GONE);
			actual_layout_3.setVisibility(View.GONE);
			actual_layout_4.setVisibility(View.GONE);
			
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.VISIBLE);
			
			
			
			
			break;
		case 1:
		case 2:
			actual_layout_1.setVisibility(View.VISIBLE);
			actual_layout_2.setVisibility(View.GONE);
			actual_layout_3.setVisibility(View.GONE);
			actual_layout_4.setVisibility(View.GONE);
			
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.VISIBLE);

			break;
		case 3:
		case 4:
			actual_layout_1.setVisibility(View.VISIBLE);
			actual_layout_2.setVisibility(View.VISIBLE);
			actual_layout_3.setVisibility(View.GONE);
			actual_layout_4.setVisibility(View.GONE);
			
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.VISIBLE);
			layout3.setVisibility(View.VISIBLE);

		
		

			break;
		case 5:
		case 6:
			

			
			actual_layout_1.setVisibility(View.VISIBLE);
			actual_layout_2.setVisibility(View.VISIBLE);
			actual_layout_3.setVisibility(View.VISIBLE);
			actual_layout_4.setVisibility(View.GONE);
			
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.GONE);
			layout3.setVisibility(View.VISIBLE);

			break;
		case 7:
		case 8:

			
			actual_layout_1.setVisibility(View.VISIBLE);
			actual_layout_2.setVisibility(View.VISIBLE);
			actual_layout_3.setVisibility(View.VISIBLE);
			actual_layout_4.setVisibility(View.VISIBLE);
			
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.GONE);
			layout3.setVisibility(View.GONE);

			break;

		default:
			break;
		}

	}
	
	
	public void onTextClick(String str)
	{
		if(str.equals("My Favourite"))
		{
			//Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
			List<SongInfo> value = db.getAllListFav();
			//Utils.list = db.getAllList();
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("abcd", (ArrayList<SongInfo>)value);
			intent.putExtra("scr", "Favourite");
			startActivity(intent);
		}
		else if(str.equals("Recent Played"))
		{
			//Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
			List<SongInfo> value = db.getAllListRecentPlayed();
			//Utils.list = db.getAllList();
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("abcd", (ArrayList<SongInfo>)value);
			intent.putExtra("scr", "Recent Played");
			startActivity(intent);
		}
		else if(str.equals("My Download"))
		{
			//Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
			
			List<SongInfo> value = db.getAllListDownload();
			//Utils.list = db.getAllList();
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("abcd", (ArrayList<SongInfo>)value);
			intent.putExtra("scr", "Download");
			startActivity(intent);
		}
		else if(str.equals("My Playlist"))
		{
			//Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
			startActivity(new Intent(getActivity(),PlalistActivity.class));
		}
		else if(str.equals("Recently Added"))
		{
			//Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
			long p = System.currentTimeMillis();
			long i = p-(10*24*60*60*1000);
			List<SongInfo> value = db.getAllListRecentAdded(i);
			
			//Utils.list = db.getAllList();
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("abcd", (ArrayList<SongInfo>)value);
			intent.putExtra("scr", "Recently Added");
			startActivity(intent);
		}
		else if(str.equals("Artist"))
		{
			//startActivity(new Intent(getActivity(),artistActivity.class));
			
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("screen", "Artist");
			startActivity(intent);
			
			
			
		}
		else if(str.equals("Album"))
		{
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("screen", "Album");
			startActivity(intent);
			//startActivity(new Intent(getActivity(),albumActivity.class));
		}
		else if(str.equals("Folder"))
		{
			Intent intent = new Intent(getActivity(), MyDialogFragment.class);
			intent.putExtra("screen", "Folder");
			startActivity(intent);
			//startActivity(new Intent(getActivity(),folderActivity.class));
		}
		/*else if(str.equals("Customize UI"))
		{
			customizeValue(str);
		}
		else if(str.equals("All Songs"))
		{
			FragmentTransaction ft = getActivity().getFragmentManager()
					.beginTransaction();
			MyDialogFragment frag = new MyDialogFragment();
			// frag.show(ft, "txn_tag");
			frag.show(ft, "txn_tag");
		}*/
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	isSpinnerFirstTime = true;
	}

}
