package com.player.mediaplayer;

import java.util.ArrayList;

import com.player.SongsPod.R;
import com.player.mediplayer.beans.albumBean;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class topAlbum extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.top_album);
		
		
		
	}
	
	private void loadWidget()
	{
		/*ImageView back = (ImageView) findViewById(R.id.top_album_back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});*/
		ListView listView = findViewById(R.id.top_album_listview);
		ArrayList<albumBean> bean = getArray();
		if(bean == null)
		{
			Toast.makeText(topAlbum.this, "Network Error", Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	private ArrayList<albumBean> getArray()
	{
		Intent intent = getIntent();
		if(intent !=null)
		{
			ArrayList<albumBean> bundle = (ArrayList<albumBean>) intent.getSerializableExtra("array");
            return bundle;
		}
		return null;
	}

}
