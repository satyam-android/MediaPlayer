package com.player.mediaplayer.adapter;

import java.util.List;

import com.player.mediaplayer.MyFragment;
import com.player.SongsPod.R;

import android.app.Activity;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class SpinnerAdapter extends ArrayAdapter<String> {

	 private Activity context;
	 private List<String> data = null;
	 private Fragment frag = null;
	 public SpinnerAdapter(Activity context, Fragment frag, int resource, List<String> data)
	 {
	     super(context, resource, data);
	     this.context = context;
	     this.data = data;
	     this.frag = frag;
	 }
	  

	 @Override
	 public View getDropDownView(int position, View convertView, ViewGroup parent)
	 {   
	     View row = convertView;
	     if(row == null)
	     {
	         //inflate your customlayout for the textview
	    	 
	         LayoutInflater inflater = context.getLayoutInflater();
	         row = inflater.inflate(R.layout.spinner_row, parent, false);
	         
	     }
	     //put the data in it
	     String item =data.get(position);
	     if(item != null)
	     {   
	        TextView text1 = row.findViewById(R.id.Spinner_textView);
	        text1.setTextColor(Color.WHITE);
	        text1.setText(item);
	       // if(position == 0)
	        	//text1.setVisibility(View.GONE);
	        /*text1.setTag(position);
	        text1.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
					int pos = (int) v.getTag();
					MyFragment fr = (MyFragment) frag;
					fr.onTextClick(data.get(pos));
					}
					return true;
				}
			});*/
	        /*text1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pos = (int) v.getTag();
					MyFragment fr = (MyFragment) frag;
					fr.onTextClick(data.get(pos));
					
					
				}
			});*/
	     }

	     return row;
	 }
	 
	 
	
	}