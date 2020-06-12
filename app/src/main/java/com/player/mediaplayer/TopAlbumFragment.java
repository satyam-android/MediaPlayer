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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class TopAlbumFragment  extends Fragment {
	private Activity act = null;
	private ListView listView = null;
	private boolean isDataDown = false;
	public TopAlbumFragment(Activity act)
	{
		this.act = act;
	}
	
	public TopAlbumFragment()
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
		        //	new latestAlbumAsyncTask(getActivity(),this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		        //}
		        //else
		       // {
		        	new latestAlbumAsyncTask(TopAlbumFragment.this.getActivity(), TopAlbumFragment.this,listView).execute("");
		        }
		        else
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
    
    private void loadWidget(View v)
	{
		/*ImageView back = (ImageView) findViewById(R.id.top_album_back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		listView = v.findViewById(R.id.top_album_listview);
		
	}
	
	public void loadListView(final ArrayList<albumBean> bean, ProgressDialog progress)
	{
		if(act == null)
		{
			act = getActivity();
		}
		listView.setAdapter(new topAlbumListViewAdapter(act, bean));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), searchActivity.class);
				intent.putExtra("value", bean.get(position).getSongs_list());
				startActivity(intent);
				
			}
		});
		
		progress.cancel();
		isDataDown = true;
	}

 
}