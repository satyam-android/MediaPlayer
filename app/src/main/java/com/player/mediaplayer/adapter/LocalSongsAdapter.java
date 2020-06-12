package com.player.mediaplayer.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.player.SongsPod.R;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.PlaylistTable;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LocalSongsAdapter extends BaseAdapter{
	private Activity act = null;
	private List<SongInfo> list = null;
	private LayoutInflater inflater = null;
	private HashMap<Integer, View> map = null;
	//private boolean isfav = false;
	private SongsTable db =null;
	private AudioPlayer player = null;
	private PlaylistTable playdb = null;
	private int size ;
	public LocalSongsAdapter(Activity act, List<SongInfo> list,int dbSize)
	{
		this.act = act;
		this.list = list;
		this.size = dbSize;
		inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		map =new HashMap<>();
		db = new SongsTable(act);
		playdb = new PlaylistTable(act);
		player = AudioPlayer.getInstnace(act);
	}
	public HashMap<Integer, View> getviewmap()
	{
		return map;
	}
	public List<SongInfo> getlist()
	{
		return list;
	}
	
	public void setlist(List<SongInfo> li)
	{
		list = li;
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
		//return null;
		View view = map.get(position);
		Holder holder = null;
		if(view == null)
		{
			view = inflater.inflate(R.layout.local_song_row, null);
			holder = new Holder();
			TextView te = view.findViewById(R.id.local_song_row_text);
			TextView alb = view.findViewById(R.id.local_song_row_text_album);
			
			holder.fav = view.findViewById(R.id.media_player_fav);
			String favValue = list.get(position).getFavourite();
			if(favValue != null && favValue.equals("yes"))
			{
				holder.isfav = true;
				holder.fav.setImageResource(R.drawable.dislike);
			}
			else
			{
				holder.isfav = false;
				holder.fav.setImageResource(R.drawable.like);
			}
			holder.sound = view.findViewById(R.id.localsongsound);
			view.setTag(holder);
			holder.fav.setTag(holder);
			holder.position = position;
			SongInfo bean = list.get(position);
			String path = bean.getPath();
			String albu =bean.getAlbum();
			if(albu !=null)
			/*if(albu.length() >30)
				albu = albu.substring(0, 30)+"...";*/
			alb.setText(albu);
			String textn = getSongName(path);
			/*if(textn.length()>28)
				textn = textn.substring(0, 28)+"...";*/
			te.setText(textn);
			map.put(position, view);
		}
		else
			holder = (Holder) view.getTag();
		holder.fav.setOnClickListener(new View.OnClickListener() {
			
			//private boolean isfav = false;
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Holder hold = (Holder) v.getTag();
				int pos = hold.position;
				//isfav = hold.isfav;
				
				if(!hold.isfav)
				{
					hold.isfav = true;
					//isfav = false;
					hold.fav.setImageResource(R.drawable.dislike);
					String path = list.get(pos).getPath();
				 db.updateFav(path, "yes");
				 playdb.updateFav(path, "yes");
				 Utils.toastshow(act, "Song added to Favourite");
				}
				else
				{
					hold.isfav = false;
					hold.isfav = false;
					hold.fav.setImageResource(R.drawable.like);
					String path = list.get(pos).getPath();
					db.updateFav(path, "no");
					playdb.updateFav(path, "no");
					Utils.toastshow(act, "Song removed from Favourite");
				}
				Utils.list = db.getAllList();
				
			}
		});
		/*if(Utils.list.size() == size)
		if(player.songIndex == position)
		{
			holder.sound.setVisibility(View.VISIBLE);
		}*/
		
		
		return view;
	}
	
	private void timer()
	{
		Timer time = new Timer();
		time.schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(Utils.list.size() == size)
				if(map !=null)
				{
					
				}
			}
		}, 10, 3000);
	}
	
	
	
	private class Holder
	{
		ImageView sound;
		ImageView fav;
		int position;
		boolean isfav = false;
	}
	
	private String getSongName(String path)
	{
		  String str =path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
		  if(str.length()>=30)
		  {
			  str = str.substring(0, 30);
			  
		  }
		  return str;
	}

}
