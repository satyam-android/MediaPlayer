package com.player.mediaplayer.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.player.mediaplayer.constant.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

public class downloadApk extends AsyncTask<String, Integer, String>{
	String path = "";
	ProgressDialog progress = null;
	Activity act = null;
	
	public downloadApk(Activity act,String path) {
		// TODO Auto-generated constructor stub
		this.path = path;
		progress = new ProgressDialog(act);
		this.act = act;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progress.setMessage("Updating SongsPod ");
	      progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	      progress.setIndeterminate(false);
	      progress.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		int count;
		try {
			URL url = new URL(path);
			URLConnection conection = url.openConnection();
			conection.connect();
			// getting file length
			int lenghtOfFile = conection.getContentLength();

			// input stream to read file - with 8k buffer
			InputStream input = new BufferedInputStream(url.openStream(), 8192);
			//if(deviceContainFile(title))
			//{
			//	return new String("false");
			//}
			// Output stream to write file
			OutputStream output = new FileOutputStream(AudioPath("SongsPod"));

			byte[] data = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				// After this onProgressUpdate will be called
				float av =((total * 100) / lenghtOfFile); 
				int value = (int) av;
				publishProgress(value);

				// writing data to file
				output.write(data, 0, count);
			}
			output.flush();
			
			// flushing output
			

			// closing streams
			output.close();
			input.close();

		} catch (Exception e) {
			Log.e("Error: ", e.getMessage());
			return "false";
		}
		return "True";
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progress.cancel();
		if(result.equals("True"))
		{
			Intent promptInstall = new Intent(Intent.ACTION_VIEW)
		    .setDataAndType(Uri.fromFile(new File(AudioPath("SongsPod" ))), 
		                    "application/vnd.android.package-archive");
		act.startActivity(promptInstall); 
SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(act.getApplicationContext());
		
		Editor edit = pref.edit();
		edit.putString("apkurl", "");
		edit.commit();
		}
		else
		{
			Toast.makeText(act, "Network error", 500).show();
		}
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		//super.onProgressUpdate(values);
		progress.setProgress(values[0]);
	}
	
	private String AudioPath(String str) {

		/*
		 * File file= new File(HelperFile.RECORD_PATH); File fil = null;
		 * file.mkdir(); fil = new File(file, str+".3gp");
		 * //fil.createNewFile(); mFileName = fil.getAbsolutePath();
		 */// +str+".3gp";
		
		String extr ;//= Environment.getExternalStorageDirectory().toString();
		//if(!(extr.contains("Card") || extr.contains("card")))
		//{
			extr = getPath();
		//}
		File mFolder = new File(extr + "/SongsPod/");

		if (!mFolder.exists()) {
			boolean br = mFolder.mkdir();
		}

		File mDir = new File(mFolder.getAbsoluteFile() + "/Apk/");
		if (!mDir.exists()) {
			boolean b = mDir.mkdir();
		}

		String s = str + ".apk";

		File f = new File(mDir.getAbsolutePath(), s);
		return f.getAbsolutePath();
	}
	
	private synchronized String getPath()
	{
		String path = "/storage/";
		File file = new File(path);
		File[] s = file.listFiles();
		String songPath = "";
		for(int i = 0; i<s.length; i++)
		{
			if(s[i].getAbsolutePath().contains("Card") || s[i].getAbsolutePath().contains("card"))
			{
				songPath = s[i].getAbsolutePath();
				
				
			}
		}
		
		return songPath;
	}


}
