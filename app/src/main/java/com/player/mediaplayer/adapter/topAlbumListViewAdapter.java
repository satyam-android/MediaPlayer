package com.player.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.player.SongsPod.R;
import com.player.mediaplayer.constant.ImagLoader;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.network.loadAlbumPicAsyncTask;
import com.player.mediplayer.beans.albumBean;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class topAlbumListViewAdapter extends BaseAdapter{
	private Activity act = null;
    private ArrayList<albumBean> list = null;
	private LayoutInflater inflater = null;
	private HashMap<Integer, View> map = null;
	public topAlbumListViewAdapter(Activity act, ArrayList<albumBean> list) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.list = list;
		this.map = new HashMap<>();
		this.inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = map.get(position);
		if(view == null)
		{
			view = inflater.inflate(R.layout.top_album_listview_row, null);
			ImageView imageView = view.findViewById(R.id.topalbum_image);
			String url = list.get(position).getImagePath();
			TextView textView = view.findViewById(R.id.topalbum_text);
			TextView album = view.findViewById(R.id.topalbum_album_text);
			album.setText(list.get(position).getNoOfSong()+" songs");
			/*if(Utils.isNetworkAvailable(act))
			{
			//if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			//{
			//	new loadAlbumPicAsyncTask(act, imageView, url).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			//}
			//else
			//{
				new loadAlbumPicAsyncTask(act, imageView, url).execute("");
			//}
			}
			else
				Utils.toastshow(act, "Network Not Available");*/
			ImagLoader imageLoader = new ImagLoader(act);
			//String url = list.get(position).getImagePath();
			imageView.setTag( url);
			imageLoader.DisplayImage( url, act,imageView);
			textView.setText(list.get(position).getTitle());
			map.put(position, view);
		}
		return view;
	}

}
