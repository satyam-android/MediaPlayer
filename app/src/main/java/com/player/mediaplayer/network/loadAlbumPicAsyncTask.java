package com.player.mediaplayer.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;

public class loadAlbumPicAsyncTask extends AsyncTask<String, Integer, Void> {
	private Activity act = null;
	private ImageView imageview = null;
	private String path = null;

	public loadAlbumPicAsyncTask(Activity act, ImageView imageview, String path) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.imageview = imageview;
		this.path = path;
	}

	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub

		Bitmap bit = null;
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.connect();
			InputStream in = connection.getInputStream();
			bit = BitmapFactory.decodeStream(in);
			imageview.setImageBitmap(bit);

		} catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

}
