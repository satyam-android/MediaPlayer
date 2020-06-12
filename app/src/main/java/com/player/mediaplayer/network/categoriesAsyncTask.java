package com.player.mediaplayer.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.player.mediaplayer.categoryFragment;
import com.player.mediaplayer.constant.jsonPath;
import com.player.mediplayer.beans.categoryBean;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class categoriesAsyncTask extends AsyncTask<String , Integer, String>{

	private ProgressDialog progress = null;
	private Activity act = null;
	private categoryFragment frag = null;
	public categoriesAsyncTask(Activity act, categoryFragment frag) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.frag = frag;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progress = new ProgressDialog(act );
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
				String url = jsonPath.CATEGORIES;
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
		ArrayList<categoryBean> list = new ArrayList<>();
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray arr = obj.getJSONArray("categories");
			for(int i =0; i < arr.length(); i++)
			{
				categoryBean songBean = new categoryBean();
				JSONObject jobj1 = arr.getJSONObject(i);
				songBean.setTitle(jobj1.getString("title"));
				songBean.setTotal_albums(jobj1.getString("total_albums"));
				songBean.setImage_path(jobj1.getString("album_art"));
				songBean.setAlbums_list(jobj1.getString("albums_list"));
				
				list.add(songBean);
				
			}
			frag.loadListView(list,progress);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
