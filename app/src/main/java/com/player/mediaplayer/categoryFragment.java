package com.player.mediaplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.Fragment;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.categoryAdapter;
import com.player.mediaplayer.adapter.topAlbumListViewAdapter;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.network.categoriesAsyncTask;
import com.player.mediplayer.beans.albumBean;
import com.player.mediplayer.beans.categoryBean;
import com.player.mediplayer.beans.topSongs;

public class categoryFragment extends Fragment {
	private Activity act = null;
	private ListView listView = null;
	private boolean isDataDown = false;
	public categoryFragment(Activity act) {
		this.act = act;
	}

	public categoryFragment() {
		this.act = getActivity();
	}

	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(menuVisible && !isDataDown)
		{
			if (Utils.isNetworkAvailable(getActivity())) {
				// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
				// {
				// new
				// categoriesAsyncTask(getActivity(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				// }
				// else
				// {
				new categoriesAsyncTask(categoryFragment.this.getActivity(), categoryFragment.this).execute("");
			} else
				Utils.toastshow(getActivity(), "Network Not Available");
			
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.top_album, container, false);

		loadWidget(rootView);
		

		return rootView;
	}

	private void loadWidget(View v) {
		/*
		 * ImageView back = (ImageView) findViewById(R.id.top_album_back);
		 * back.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub finish(); } });
		 */
		listView = v.findViewById(R.id.top_album_listview);

	}

	public void loadListView(final ArrayList<categoryBean> bean, ProgressDialog progress) {
		if(act == null)
		{
			act = getActivity();
		}
		listView.setAdapter(new categoryAdapter(act, bean));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), category_albumList_activity.class);
				intent.putExtra("value", bean.get(position).getAlbums_list());
				startActivity(intent);
				
			}
		});
		
		
		progress.cancel();
		isDataDown = true;
	}

}