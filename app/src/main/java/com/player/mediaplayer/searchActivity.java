package com.player.mediaplayer;

import java.util.List;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.downloadAdapter;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.network.getAlbumAsyncTask;
import com.player.mediaplayer.network.searchAsyncTask;
import com.player.mediplayer.beans.topSongs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class searchActivity extends Activity implements OnClickListener {

	private ListView listview = null;
	private EditText search = null;
	private LinearLayout layout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search);
		loadWidget();
		getIntentValue();
	}

	private void loadWidget() {
		ImageView back = findViewById(R.id.search_back);
		back.setOnClickListener(this);
		search = findViewById(R.id.search_song);
		layout = findViewById(R.id.search_layout);
		TextView searchBtn = findViewById(R.id.search_btn);
		searchBtn.setOnClickListener(this);
		listview = findViewById(R.id.search_list);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search_back:
			startActivity(new Intent(searchActivity.this, homeActivity.class));
			break;
		case R.id.search_btn:
			String str = search.getText().toString();
			searchBtnClick(str);

			break;

		default:
			break;
		}

	}
	
	private void searchBtnClick(String str)
	{
		
		if(str.length() == 0)
		{
			Utils.toastshow(searchActivity.this, "Enter something");
			return;
		}
		if(Utils.isNetworkAvailable(searchActivity.this))
		{
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				new searchAsyncTask(searchActivity.this, str).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			else
			{
				new searchAsyncTask(searchActivity.this, str).execute("");
			}
			
		}
		else
			Utils.toastshow(searchActivity.this, "Network Not Available");
		
		
	}
	
	public void loadListView(List<topSongs> list)
	{
		if(list.size() == 0)
		{
			Utils.toastshow(searchActivity.this, "No result found");
			return;
			
		}
		listview.setAdapter(new downloadAdapter(searchActivity.this, list));
		
		
	}
	
	private void getIntentValue()
	{
		String value = getIntent().getStringExtra("value");
		if(value != null)
		{
			layout.setVisibility(View.GONE);
			//searchBtnClick(value);
			getAlbum(value);
		}
		
	}
	
	private void getAlbum(String str)
	{
		if(str.length() == 0)
		{
			Utils.toastshow(searchActivity.this, "Enter something");
			return;
		}
		if(Utils.isNetworkAvailable(searchActivity.this))
		{
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			{
				new getAlbumAsyncTask(searchActivity.this, str).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			else
			{
				new getAlbumAsyncTask(searchActivity.this, str).execute("");
			}
			
		}
		else
			Utils.toastshow(searchActivity.this, "Network Not Available");
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		startActivity(new Intent(searchActivity.this, homeActivity.class));
	}
	
	public void loadListView(List<topSongs> list, ProgressDialog bar)
	{
		if(list.size() == 0)
		{
			bar.cancel();
			Utils.toastshow(searchActivity.this, "No result found");
			return;
			
		}
		//Utils.toastshow(getActivity(), list.size()+"  songs");
		downloadAdapter d = new downloadAdapter(searchActivity.this, list);
		listview.setAdapter(d);
		d.notifyDataSetChanged();
		bar.cancel();
		//isDataDown = true;
		
		
	}

}
