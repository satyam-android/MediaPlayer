package com.player.mediaplayer.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class downLoadSongAsyncTask extends AsyncTask<String, Integer, String> {

	// private ProgressDialog progress = null;;
	private Activity act = null;
	private ProgressBar bar = null;
	private String path = "";
	private String title = "";
	private SongsTable db = null;
	private String album = "";
	private TextView download = null;
	public downLoadSongAsyncTask(Activity act,TextView downlaod, ProgressBar bar, String url,
			String title, String album) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.bar = bar;
		this.path = url;
		this.title = title;
		this.db = new SongsTable(act);
		this.album = album;
		this.download = downlaod;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		bar.setVisibility(View.VISIBLE);
		download.setEnabled(false);
		/*
		 * progress = new ProgressDialog(act);
		 * progress.setCanceledOnTouchOutside(false);
		 * progress.setMessage("Please wait..."); progress.show();
		 */
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
			OutputStream output = new FileOutputStream(AudioPath(title));

			byte[] data = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				// After this onProgressUpdate will be called
				int value = (int) ((total * 100) / lenghtOfFile);
				publishProgress(value);

				// writing data to file
				output.write(data, 0, count);
			}
			output.flush();
			if (total == lenghtOfFile) {
				String path = AudioPath(title);
				db.delete(path);
				db.insert(path, System.currentTimeMillis());
				db.updateAlbum(path, album);
				db.updateArtist(path, Utils.getAudioArtist(path	, act));
				db.updateDownload(path, "yes");
				return "true";
				//Utils.list = db.getAllList();
			} else {
				File file = new File(AudioPath(title));
				file.delete();
			}
			// flushing output
			

			// closing streams
			output.close();
			input.close();

		} catch (Exception e) {
			Log.e("Error: ", e.getMessage());
		}
		return "false";
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result.equals("true"))
		{
			Utils.toastshow(act, "Download Complete");
		}
		else
		{
			Utils.toastshow(act, "Network Error");		}
		bar.setVisibility(View.INVISIBLE);
		download.setEnabled(true);

	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		bar.setProgress(values[0]);
	}

	private String AudioPath(String str) {

		/*
		 * File file= new File(HelperFile.RECORD_PATH); File fil = null;
		 * file.mkdir(); fil = new File(file, str+".3gp");
		 * //fil.createNewFile(); mFileName = fil.getAbsolutePath();
		 */// +str+".3gp";
		
		String extr = Environment.getExternalStorageDirectory().toString();
		//if(!(extr.contains("Card") || extr.contains("card")))
		//{
			//extr = getPath();
		//}
		File m = new File(extr);
		if(m.exists())
		{
			//m.mkdir();
			Log.i("exist","");
		}
		else
		Log.i("notexist","");
		File mFolder = new File(extr + "/SongsPod/");

		if (!mFolder.exists()) {
			boolean br = mFolder.mkdir();
		}

		File mDir = new File(mFolder.getAbsoluteFile() + "/Downloaded/");
		if (!mDir.exists()) {
			boolean b = mDir.mkdir();
		}

		String s = str + ".mp3";

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

	private boolean deviceContainFile(String str) {
		try {
			
		
		String extr = Environment.getExternalStorageDirectory().toString();
		File mFolder = new File(extr + "/MediaPlayer//Downloaded/");

		

		String s = str + ".mp3";

		File f = new File(mFolder.getAbsolutePath(), str);
		File[] files = f.listFiles();
		if (files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				return files[i].getAbsolutePath().contains(str);
			}
		} 
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
			return false;
			

	}
}
