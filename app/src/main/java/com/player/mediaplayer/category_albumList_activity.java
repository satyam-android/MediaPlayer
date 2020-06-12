package com.player.mediaplayer;

import java.util.ArrayList;

import org.w3c.dom.ls.LSInput;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.topAlbumListViewAdapter;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.network.latestAlbumAsyncTask;
import com.player.mediplayer.beans.albumBean;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class category_albumList_activity extends Activity implements
		OnClickListener {

	private ListView listView = null;
	private boolean isDataDown = false;
	private ArrayList<albumBean> bean = null;
	private int footerPageList = 0;
	private String url = "";
	private topAlbumListViewAdapter adap = null;
	private View loadmore = null;
	private int listsi = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.top_album);
		bean = new ArrayList<>();
		loadWidget();
		getIntentValue();
		adap = new topAlbumListViewAdapter(
				category_albumList_activity.this, bean);

	}

	private void getIntentValue() {
		url = getIntent().getStringExtra("value");
		if (url != null) {

			// searchBtnClick(value);

			if (Utils.isNetworkAvailable(this)) {
				// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
				// {
				// new
				// latestAlbumAsyncTask(getActivity(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				// }
				// else
				// {
				new latestAlbumAsyncTask(category_albumList_activity.this,
						url, listView, bean,footerPageList,loadmore).execute("");
			} else
				Utils.toastshow(this, "Network Not Available");
		}

	}

	private void loadWidget() {
		LinearLayout layout = findViewById(R.id.album_top);
		layout.setVisibility(View.VISIBLE);
		ImageView back = findViewById(R.id.top_album_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		listView = findViewById(R.id.top_album_listview);
		loadmore = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview_listview, null, false);
		loadmore.setId(5001);
		loadmore.setOnClickListener(this);
		listView.addFooterView(loadmore);

	}

	public void loadListView(ProgressDialog progress) {
		adap.notifyDataSetChanged();
		listView.setAdapter(adap);
		listView.setSelection(listsi);
		listsi = listsi+20;
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(category_albumList_activity.this,
						searchActivity.class);
				intent.putExtra("value", bean.get(position).getSongs_list());
				startActivity(intent);

			}
		});

		progress.cancel();
		isDataDown = true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case 5001:
			footerPageList++;
			if (Utils.isNetworkAvailable(this)) {
				// if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
				// {
				// new
				// latestAlbumAsyncTask(getActivity(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				// }
				// else
				// {
				new latestAlbumAsyncTask(category_albumList_activity.this,
						url, listView, bean,footerPageList,loadmore).execute("");
			} else
				Utils.toastshow(this, "Network Not Available");
			
			break;

		default:
			break;
		}

	}

}
