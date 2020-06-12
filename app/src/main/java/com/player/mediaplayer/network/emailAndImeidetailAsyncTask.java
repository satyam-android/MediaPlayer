package com.player.mediaplayer.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.player.mediaplayer.constant.jsonPath;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class emailAndImeidetailAsyncTask extends AsyncTask<String, Integer, String>{
	
	private Activity act = null;
	private String email = "";
	private String imei = "";
	private String versionName = "";
	
	public emailAndImeidetailAsyncTask(Activity act, String email, String imei, String versionName) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.email = email;
		this.imei = imei;
		this.versionName = versionName;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		StringBuffer str = new StringBuffer();
		//String id = pref.getString("userid", "");
		//String token = pref.getString("usertoken", "");
		String url = jsonPath.ACCOUNT_DETAIL+"imei="+imei+"&"+"email="+email+"&channel_id=apk7&version="+versionName;//imei=542136556032123&email=abc@gmail.com"
		//url = URLEncoder.encode(url);
		//http://mp3jugaad.in/json-feeds/record.php?imei=542136556032123&email=abc@gmail.com&channel_id=apk1
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
		if(result.equals("Saved"))
		{
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(act);
			Editor edit = pref.edit();
			edit.putBoolean("emailfirst",true);
			edit.commit();
			
			
			SharedPreferences pref1 = PreferenceManager.getDefaultSharedPreferences(act.getApplicationContext());
			Editor edit1 = pref1.edit();
			edit1.putString("apkurl","");
			edit1.commit();
			
		}
		else
		{
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(act.getApplicationContext());
			Editor edit = pref.edit();
			edit.putString("apkurl",result);
			edit.commit();
			
		}
		
	}

}
