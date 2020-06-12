package com.player.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.player.SongsPod.R;
import com.player.mediplayer.beans.SongInfo;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class artistListAdapter extends BaseAdapter {
	private Activity act = null;
	private HashMap<String, ArrayList<SongInfo>> hashList = null;
	private List<String> list = null;
	private LayoutInflater inflater = null;
	private HashMap< Integer, View> viewMap = null;
	public artistListAdapter(Activity act, HashMap<String , ArrayList<SongInfo>> hashList, List<String> list)
	{
		this.act = act;
		this.hashList = hashList;
		this.list = list;
		this.inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.viewMap = new HashMap<>();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	public List<String> getList()
	{
		return list;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = viewMap.get(position);
		if(view == null)
		{
		view = inflater.inflate(R.layout.artist_listview_row	, null);
			TextView text = view.findViewById(R.id.Spinner_textView);
			TextView numb = view.findViewById(R.id.Spinner_textView_songs);
			String textValue =list.get(position);
			/*if(textValue.length()>50)
			{
				textValue = textValue.substring(0,50)+"...";
			}*/
			text.setText(textValue);
			
			String artist = list.get(position);
			int i = hashList.get(artist).size();
			numb.setText(i+" Songs");
			
			viewMap.put(position, view);
		}
		return view;
	}

}
