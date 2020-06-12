package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.List;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.downloadAdapter;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.network.latestAlbumAsyncTask;
import com.player.mediaplayer.network.topSongsAsyncTask;
import com.player.mediplayer.beans.albumBean;
import com.player.mediplayer.beans.topSongs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

public class DownloadFragment  extends Fragment implements OnClickListener {
	private Activity act = null;
	private ListView listView = null;
	private boolean isDataDown = false;
	private View loadmore = null;
	private int footerPageList = 0;
	private ArrayList<topSongs> bean = null;
	private downloadAdapter d;
	private int listsi = 0;
	public DownloadFragment(Activity act)
	{
		this.act = act;
	}
	public DownloadFragment()
	{
		this.act = getActivity();
	}
	
	@Override
	public void setMenuVisibility(boolean menuVisible) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(menuVisible);
		if(menuVisible && !isDataDown)
		{
			 if(Utils.isNetworkAvailable(getActivity())){
		         //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		         //{
		         //	new topSongsAsyncTask(getActivity(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		         //}
		         //else
		        // {
		         	new topSongsAsyncTask(DownloadFragment.this.getActivity(), DownloadFragment.this, footerPageList, listView, loadmore,bean).execute("");
		         }
		         else
		         	Utils.toastshow(getActivity(), "Network Not Available");
		}
	}
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	if(act == null)
    	{
    		act = getActivity();
    	}
 
    	 View rootView = inflater.inflate(R.layout.top_album, container, false);
         bean = new ArrayList<>();
         d = new downloadAdapter(getActivity(), bean);
         //loadWidget(rootView);
    	 listView = rootView.findViewById(R.id.top_album_listview);
    	 loadmore = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footerview_listview, null, false);
 		loadmore.setId(1004);
 		loadmore.setOnClickListener(this);
 		listView.addFooterView(loadmore);
          
         return rootView;
    }
    
    /*private void loadWidget(View v)
   	{
   		ImageView back = (ImageView) findViewById(R.id.top_album_back);
   		back.setOnClickListener(new View.OnClickListener() {
   			
   			@Override
   			public void onClick(View v) {
   				// TODO Auto-generated method stub
   				finish();
   			}
   		});
   		listView = (ListView) v.findViewById(R.id.top_album_listview);
   		
   	}*/
    
    public void loadListView(List<topSongs> list, ProgressDialog bar)
	{
		if(list.size() == 0)
		{
			bar.cancel();
			Utils.toastshow(getActivity(), "No result found");
			return;
			
		}
		//Utils.toastshow(getActivity(), list.size()+"  songs");
		d.notifyDataSetChanged();
		listView.setAdapter(d);
		listView.setSelection(listsi);
		listsi = listsi+20;
		d.notifyDataSetChanged();
		bar.cancel();
		isDataDown = true;
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case 1004:
			footerPageList++;
			if(Utils.isNetworkAvailable(getActivity())){
		         //if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		         //{
		         //	new topSongsAsyncTask(getActivity(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		         //}
		         //else
		        // {
		         	new topSongsAsyncTask(DownloadFragment.this.getActivity(), DownloadFragment.this,footerPageList,listView,loadmore,bean).execute("");
		         }
		         else
		         	Utils.toastshow(getActivity(), "Network Not Available");
			break;

		default:
			break;
		}
		
	}
 
}
