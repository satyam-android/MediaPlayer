

package com.player.mediaplayer.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.player.mediaplayer.DownloadFragment;
import com.player.mediaplayer.searchActivity;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.constant.jsonPath;
import com.player.mediplayer.beans.albumBean;
import com.player.mediplayer.beans.topSongs;

public class getAlbumAsyncTask extends AsyncTask<String, Integer, String>{
	private searchActivity act = null;
	//private String value = "";
	private ProgressDialog progress;
	//private DownloadFragment frag = null;
	private String urlValue = "";
	public getAlbumAsyncTask(searchActivity act,String str  ) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.urlValue = str;
		//this.frag = frag;
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
		StringBuffer str = new StringBuffer();
		//String id = pref.getString("userid", "");
		//String token = pref.getString("usertoken", "");
		
		try {
			String url = urlValue;
			//url = url.trim();
			//if(url.contains(" "))
			//{
				//int i = url.indexOf(" ");
				//url = url.replaceAll("\\(\\)\\{\\}\\[\\]\\_\\-", "");
				//url = url.replace(" ", "+");
			//}
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
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
		List<topSongs> list = new ArrayList<>();
		try {
			JSONObject obj = new JSONObject(result);
			JSONArray arr = obj.getJSONArray("songs");
			for(int i =0; i < arr.length(); i++)
			{
				topSongs songBean = new topSongs();
				JSONObject jobj1 = arr.getJSONObject(i);
				songBean.setTitle(jobj1.getString("title"));
				songBean.setDownloads(jobj1.getString("downloads"));
				songBean.setMp3Url(jobj1.getString("hotlink"));
				albumBean bean = new albumBean();
				
				JSONObject jobj = jobj1.getJSONObject("album");
				
				
				
				String id = jobj.getString("id");
				bean.setId(id);
				String category = jobj.getString("category");
				bean.setCategory(category);
				String imagepath = jobj.getString("artwork");
				bean.setImagePath(imagepath);
				String title = jobj.getString("title");
				bean.setTitle(title);
				songBean.setAlbuminfo(bean);
				list.add(songBean);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Utils.toastshow(act, "No Result Found");
		}
		act.loadListView(list, progress);
		
	}

}

