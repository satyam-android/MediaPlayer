package com.player.mediaplayer.adapter;

import java.util.HashMap;
import java.util.List;

import com.player.SongsPod.R;
import com.player.mediaplayer.constant.ImagLoader;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.network.downLoadSongAsyncTask;
import com.player.mediaplayer.network.loadAlbumPicAsyncTask;
import com.player.mediplayer.beans.topSongs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Shader.TileMode;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class downloadAdapter extends BaseAdapter {
	private List<topSongs> list = null;
	private HashMap<Integer, View> myMap = null;
	private LayoutInflater inflater = null;
	private Activity act = null;
	
	

	public downloadAdapter(Activity act, List<topSongs> list) {
		this.act = act;
		this.list = list;
		myMap = new HashMap<>();
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		View view = myMap.get(position);
		Holder holder = null;
		if (view == null) {
			holder = new Holder();
			view = inflater.inflate(R.layout.download_adapter_row, null);
			ImageView image = view
					.findViewById(R.id.download_image);
			TextView title = view.findViewById(R.id.download_title);
			holder.position = position;
			TextView album = view.findViewById(R.id.download_album);
			ProgressBar bar = view
					.findViewById(R.id.download_bar);
			holder.download = view.findViewById(R.id.download_btn);
			holder.bar = view.findViewById(R.id.download_bar);
			String url = list.get(position).getAlbuminfo().getImagePath();
			/*if (Utils.isNetworkAvailable(act)) {
				//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				//	new loadAlbumPicAsyncTask(act, image, url)
				//			.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			//	} else {
					new loadAlbumPicAsyncTask(act, image, url).execute("");
				//}
			} else
				Utils.toastshow(act, "Network Not Available");*/
			ImagLoader imageLoader = new ImagLoader(act);
			//String url = list.get(position).getImagePath();
			image.setTag( url);
			imageLoader.DisplayImage( url, act,image);
			String titlestr = list.get(position).getTitle();
			/*if(titlestr.length() >= 20)
			{
				titlestr = titlestr.substring(0, 20)+"...";
			}*/
			title.setText(titlestr);
			holder.title = list.get(position).getTitle();
			String albumstr = list.get(position).getAlbuminfo().getTitle();
            if(albumstr.length() >= 20)
			{
				albumstr = albumstr.substring(0, 20)+"...";
			}
			
			holder.album = list.get(position).getAlbuminfo().getTitle();
			album.setText(albumstr);
			holder.download.setTag(holder);
			//view.setTag(holder);
			
			
			holder.download.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Holder holder = (Holder) v.getTag();
					int pos = holder.position;
					ProgressBar bar = holder.bar;
					String title = holder.title;
					String album = holder.album;
					TextView download = holder.download;
					//Utils.toastshow(act, pos+"  clicked");
					if(Utils.isNetworkAvailable(act))
					{
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					{
						new downLoadSongAsyncTask(act,download, bar, list.get(pos).getMp3Url(), title,album).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
					else
					{
						new downLoadSongAsyncTask(act, download,bar, list.get(pos).getMp3Url(), title,album).execute("");
					}
					}
					else
						Utils.toastshow(act, "Network Not Available");
					
				}
			});
			myMap.put(position, view);
			

		}
		
		
		
		return view;
	}

	private  class Holder
	{
		public  TextView download;
		public  int position;
		public ProgressBar bar;
		public String title;
		public String album;
	}
}
