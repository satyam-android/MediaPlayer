package com.player.mediaplayer.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import com.player.mediaplayer.TopAlbumFragment;
import com.player.mediaplayer.category_albumList_activity;
import com.player.mediaplayer.constant.jsonPath;
import com.player.mediplayer.beans.albumBean;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class latestAlbumAsyncTask extends AsyncTask<String, Integer, String> {
	private Activity act = null;
	private ProgressDialog progress = null;
	private TopAlbumFragment frag = null;
	private ListView listview = null;
	private String url = "";
	private ArrayList<albumBean> list = null;
	private int footerpage = 0;
	private View loadmore = null;
	public latestAlbumAsyncTask(Activity act, TopAlbumFragment frag,ListView listview) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.frag = frag;
		this.listview = listview;
		this.url = jsonPath.LATEST_ALBUM;
	}
	
	
	public latestAlbumAsyncTask(Activity act,String url, ListView listview, ArrayList<albumBean> bean, int footerPageList, View loadmore)
	{
		this.act = act;
		this.listview = listview;
		this.url = url+"&p="+footerPageList;
		this.list = bean;
		this.loadmore = loadmore;

	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progress = new ProgressDialog(act);
		progress.setCanceledOnTouchOutside(false);
		progress.setMessage("Please wait...");
		progress.show();
		
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		//SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(act);
		StringBuffer str = new StringBuffer();
		//String id = pref.getString("userid", "");
		//String token = pref.getString("usertoken", "");
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse res = client.execute(get);
			InputStream in = res.getEntity().getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			String s = "";
			while ((s = br.readLine()) != null) {
				str.append(s);
				
			}
			br.close();
			in.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toString();
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//progress.cancel();
		try {
			ArrayList<albumBean> list = null;
			if(frag !=null)
			{
				 list = new ArrayList<>();
			}
			else
			{
				list = this.list;
			}
			
			JSONObject obj = new JSONObject(result);
			JSONArray arr = obj.getJSONArray("albums");
			for(int i = 0; i < arr.length(); i++)
			{
				albumBean bean = new albumBean();
				JSONObject jobj = arr.getJSONObject(i);
				String id = jobj.getString("id");
				bean.setId(id);
				String category = jobj.getString("category");
				bean.setCategory(category);
				String imagepath = jobj.getString("artwork");
				bean.setImagePath(imagepath);
				String title = jobj.getString("title");
				bean.setTitle(title);
				String no = jobj.getString("no_of_songs");
				bean.setNoOfSong(no);
				String songs_list = jobj.getString("songs_list");
				bean.setSongs_list(songs_list);
				list.add(bean);
				
			}
			if(frag !=null)
			frag.loadListView(list,progress);
			else
			{
				category_albumList_activity activity = (category_albumList_activity)act;
				activity.loadListView( progress);
			}
			/*Intent intent = new Intent(act, topAlbum.class);
			intent.putExtra("array", list);
			act.startActivity(intent);*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if(listview != null && loadmore != null)
			{
			listview.removeFooterView(loadmore);
			progress.cancel();
			}
		}
		
	}
	


}
