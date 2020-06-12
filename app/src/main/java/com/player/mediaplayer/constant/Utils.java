package com.player.mediaplayer.constant;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.player.mediaplayer.database.SongsTable;
import com.player.mediplayer.beans.SongInfo;

import android.app.Activity;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utils {
	public static List<SongInfo> list= new ArrayList<>();
	public static List<SongInfo> songListService = new ArrayList<>();
	private static MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
	
	public static List<SongInfo> getMusic(String path, SongsTable db) {
		//list.clear();
		List<SongInfo> mFiles = new ArrayList<>();
		List<String> songList = new ArrayList<>();
		File music = new File(path);
		List<SongInfo> temp =walkdir(music, mFiles,songList,db);
		if(temp!=null && temp.size()>0)
		insertList(temp);
		
		return list;

	}
	
	public static List<SongInfo> getMusicService(String path,Context cont) {
		//songListService.clear();
		List<SongInfo> mFiles = new ArrayList<>();
		List<String> songList = new ArrayList<>();
		File music = new File(path);
		List<SongInfo> temp =walkdirService(music, mFiles,songList,cont);
		if(temp!=null && temp.size()>0)
		insertsongInfoList(temp);
		
		return songListService;

	}
	
	private static synchronized void insertList(List<SongInfo > li)
	{
		list.addAll(li);
		HashSet <SongInfo> h = new HashSet<SongInfo>(list);
	    list.clear();
	    list.addAll(h);
	}
	private static synchronized void insertsongInfoList(List<SongInfo > li)
	{
		
		songListService.addAll(li);
		HashSet <SongInfo> h = new HashSet<SongInfo>(songListService);
		songListService.clear();
		songListService.addAll(h);
		
	}

	private static List<SongInfo> walkdir(File dir, List<SongInfo> mFiles,List<String> songlist, SongsTable db) {
		String mp3 = ".mp3";
		String wav = ".wav";

		File[] listFile = dir.listFiles();

		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					walkdir(listFile[i], mFiles,songlist,db);
				} else {
					if (listFile[i].getName().endsWith(mp3)) {
						String songpath = listFile[i].getAbsolutePath();
						String songName = getSongName(songpath);
						Log.i("##path of the song is", songpath);
						if(!songlist.contains(songName))
							{
								songlist.add(songName);
								SongInfo bean = new SongInfo();
								bean.setPath(songpath);
								mFiles.add(bean);
								db.insert(songpath,System.currentTimeMillis());
								
							}

					}
				}
			}
		}
		return mFiles;
	}
	
	private static List<SongInfo> walkdirService(File dir, List<SongInfo> mFiles,List<String> songlist,Context cont) {
		String mp3 = ".mp3";
		String wav = ".wav";

		File[] listFile = dir.listFiles();

		if (listFile != null) {
			for (int i = 0; i < listFile.length; i++) {

				if (listFile[i].isDirectory()) {
					walkdirService(listFile[i], mFiles,songlist,cont);
				} else {
					if (listFile[i].getName().endsWith(mp3)) {
						String songpath = listFile[i].getAbsolutePath();
						String songName = getSongName(songpath);
						//Log.i("##path of the song is", songpath);
						if(!songlist.contains(songName))
							{
								songlist.add(songName);
								SongInfo bean = new SongInfo();
								bean.setPath(songpath);
								bean.setAlbum(getAudioAlbum(songpath,cont));
								bean.setArtist(getAudioArtist(songpath, cont));
								mFiles.add(bean);
								//db.insert(songpath,System.currentTimeMillis());
								
							}

					}
				}
			}
		}
		return mFiles;
	}
	
	private static String getSongName(String path)
	{
		 return path.substring(path.lastIndexOf("/")+1, path.lastIndexOf("."));
	}
	
	public static synchronized String getAudioTitle(String mp3File, Context activ) {
		//SongInfo bean = new SongInfo();
		File file = new File(mp3File);
		
		Uri uri = Uri.fromFile(file);
		mediaMetadataRetriever.setDataSource(activ, uri);
		String title = mediaMetadataRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		//String album = (String) mediaMetadataRetriever
		//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		////String artist = (String) mediaMetadataRetriever
		//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		//bean.setAlbum(album);
		//bean.setArtist(artist);
		//bean.setPath(mp3File);
		//bean.setTitle(title);
		title = title.trim();
		return title;

	}
	
	public static synchronized String getAudioAlbum(String mp3File, Context activ) {
		//SongInfo bean = new SongInfo();
		try{
		File file = new File(mp3File);
		
		Uri uri = Uri.fromFile(file);
		mediaMetadataRetriever.setDataSource(activ, uri);
		//String title = (String) mediaMetadataRetriever
		//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		String album = mediaMetadataRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		////String artist = (String) mediaMetadataRetriever
		//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		//bean.setAlbum(album);
		//bean.setArtist(artist);
		//bean.setPath(mp3File);
		//bean.setTitle(title);
		album = album.trim();
		if(album.equals("") || album == null || album.equals(" ")|| album == "null")
			return "Unknown";
		return album;
		}
		catch(Exception e)
		{
			return "Unknown";
		}

		

	}
	
	public static synchronized String getAudioArtist(String mp3File, Context activ) {
		//SongInfo bean = new SongInfo();
		try{
		File file = new File(mp3File);
		
		Uri uri = Uri.fromFile(file);
		
		mediaMetadataRetriever.setDataSource(activ, uri);
		//String title = (String) mediaMetadataRetriever
		//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
		//String album = (String) mediaMetadataRetriever
		//		.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
		String artist = mediaMetadataRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
		//bean.setAlbum(album);
		//bean.setArtist(artist);
		//bean.setPath(mp3File);
		//bean.setTitle(title);
		artist = artist.trim();
		if(artist.equals("") || artist == null || artist.equals(" ") || artist == "null")
			return "Unknown";
		return artist;
		}
		catch(Exception e)
		{
			return "Unknown";
		}

	}
	
	public static void toastshow(Context ctx , String msg )
	{
		Toast.makeText(ctx, msg	, Toast.LENGTH_SHORT).show();
	}
	public static boolean isNetworkAvailable(Context ctx) {
		ConnectivityManager connectivityManager = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
	
	public static void hideSoftKeyboard(Activity activity, View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
	}
	
	public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}
