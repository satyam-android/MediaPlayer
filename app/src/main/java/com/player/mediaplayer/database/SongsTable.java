package com.player.mediaplayer.database;

import java.util.ArrayList;
import java.util.List;

import com.player.mediplayer.beans.SongInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SongsTable extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "media.db";
	private static final String TABLE_NAME = "songs";
	private static final String ID_COLOUMN = "ID";
	private static final String PATH_COLOUMN = "PATH";
	private static final String ARTIS = "ARTIST";
	private static final String FAVOURITE = "FAVOURITE";
	private static final String ALBUM = "ALBUM";
	private static final String DATE = "DATE";
	private static final String RECENT_PALYED = "RECENTPLAYED";
	private static final String DOWNLOAD = "DOWNLOAD";
	private static final String PLAYLIST = "PLAYLIST";
	public int songs = 0;

	public SongsTable(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String restro_table = "create table if not exists " + TABLE_NAME
				+ " (" 
				+ ID_COLOUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," + PATH_COLOUMN + " text,"
				+ ARTIS + " text,"
				+ FAVOURITE + " text,"
				+ ALBUM + " text,"
				+ DATE + " INTEGER,"
				+ RECENT_PALYED + " INTEGER,"
				+ DOWNLOAD + " text,"
				+ PLAYLIST + " text);" ;
		db.execSQL(restro_table);
		
		/*db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + ID_COLOUMN
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + PATH_COLOUMN + "TEXT,"
				+ ARTIS + "TEXT,"
				+ FAVOURITE + "TEXT,"
				+ ALBUM + "TEXT,"
				+ DATE + "INTEGER,"
				+ RECENT_PALYED + "TEXT,"
				+ DOWNLOAD + "TEXT,"
				+ PLAYLIST + "TEXT"
				+ " );");
*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

	public synchronized void insert(String path,long time) {
		SQLiteDatabase db = getWrite();

		ContentValues values = new ContentValues();
		values.put(PATH_COLOUMN, path);
		//values.put(PATH_COLOUMN, path);
		values.put(DATE, time);
		songs++;
		// insert row
		long i = db.insert(TABLE_NAME, null, values);
		db.close();

	}
	public synchronized void delete(String path)
	{
		SQLiteDatabase database = getWritableDatabase();
		int i = database.delete(TABLE_NAME, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
		if(i>0)
		{
			Log.d("song deleted :-  ", path);
		}
		database.close();
		
	}
	public synchronized void insertSongInfo(SongInfo song,long time) {
		SQLiteDatabase db = getWrite();

		ContentValues values = new ContentValues();
		values.put(PATH_COLOUMN, song.getPath());
		values.put(ARTIS, song.getArtist());
		values.put(ALBUM, song.getAlbum());
		//values.put(PATH_COLOUMN, path);
		values.put(DATE, time);
		songs++;
		// insert row
		db.insert(TABLE_NAME, null, values);
		db.close();

	}

	public synchronized void insertArray(List<String> path) {
		SQLiteDatabase db = getWrite();
		for (String pa : path) {
			ContentValues values = new ContentValues();
			values.put(PATH_COLOUMN, pa);
			// insert row
			db.insert(TABLE_NAME, null, values);
		}
		db.close();
	}

	public synchronized void deleteAll() {
		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, null, null);
		database.close();
	}
	
	public synchronized List<SongInfo> getAllList()
	{
		List<SongInfo> data = new ArrayList<>();

	    String selectQuery = "SELECT  DISTINCT * FROM " + TABLE_NAME;
	          SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	           // get  the  data into array,or class variable
	        	SongInfo bean = new SongInfo();
	        	String path = cursor.getString(1);
	        	bean.setPath(path);
	        	String artist = cursor.getString(2);
	        	bean.setArtist(artist);
	        	String favourite = cursor.getString(3);
	        	bean.setFavourite(favourite);
	        	String album = cursor.getString(4);
	        	bean.setAlbum(album);
	        	String download = cursor.getString(7);
	        	bean.setDownload(download);
	        	bean.setShowSpeaker(true);
	        	data.add(bean);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized List<SongInfo> getAllListFav()
	{
		List<SongInfo> data = new ArrayList<>();

	    String selectQuery = "SELECT DISTINCT * FROM " + TABLE_NAME+" WHERE " + FAVOURITE + " = \'yes\'";
	          SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	           // get  the  data into array,or class variable
	        	SongInfo bean = new SongInfo();
	        	String path = cursor.getString(1);
	        	bean.setPath(path);
	        	String artist = cursor.getString(2);
	        	bean.setArtist(artist);
	        	String favourite = cursor.getString(3);
	        	bean.setFavourite(favourite);
	        	String album = cursor.getString(4);
	        	bean.setAlbum(album);
	        	String download = cursor.getString(7);
	        	bean.setDownload(download);
	        	bean.setShowSpeaker(false);
	        	data.add(bean);
	        	
	        	
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized List<SongInfo> getAllListDownload()
	{
		List<SongInfo> data = new ArrayList<>();

	    String selectQuery = "SELECT DISTINCT * FROM " + TABLE_NAME+" WHERE " + DOWNLOAD + " = \'yes\'";
	          SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	           // get  the  data into array,or class variable
	        	SongInfo bean = new SongInfo();
	        	String path = cursor.getString(1);
	        	bean.setPath(path);
	        	String artist = cursor.getString(2);
	        	bean.setArtist(artist);
	        	String favourite = cursor.getString(3);
	        	bean.setFavourite(favourite);
	        	String album = cursor.getString(4);
	        	bean.setAlbum(album);
	        	String download = cursor.getString(7);
	        	bean.setDownload(download);
	        	bean.setShowSpeaker(false);
	        	data.add(bean);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized List<SongInfo> getAllListRecentAdded(long i)
	{
		List<SongInfo> data = new ArrayList<>();

	    String selectQuery = "SELECT DISTINCT * FROM " + TABLE_NAME+" WHERE " + DATE + " > "+ i + " order by "+DATE+" desc";
	          SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	           // get  the  data into array,or class variable
	        	SongInfo bean = new SongInfo();
	        	String path = cursor.getString(1);
	        	bean.setPath(path);
	        	String artist = cursor.getString(2);
	        	bean.setArtist(artist);
	        	String favourite = cursor.getString(3);
	        	bean.setFavourite(favourite);
	        	String album = cursor.getString(4);
	        	bean.setAlbum(album);
	        	String download = cursor.getString(7);
	        	bean.setDownload(download);
	        	bean.setShowSpeaker(false);
	        	data.add(bean);
	        	
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized List<SongInfo> getAllListRecentPlayed()
	{
		List<SongInfo> data = new ArrayList<>();

	    String selectQuery = "SELECT DISTINCT * FROM " + TABLE_NAME+" ORDER BY " +RECENT_PALYED+" DESC LIMIT 10" ;//" + DATE + " > "+ i;
	          SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	           // get  the  data into array,or class variable
	        	SongInfo bean = new SongInfo();
	        	String path = cursor.getString(1);
	        	bean.setPath(path);
	        	String artist = cursor.getString(2);
	        	bean.setArtist(artist);
	        	String favourite = cursor.getString(3);
	        	bean.setFavourite(favourite);
	        	String album = cursor.getString(4);
	        	bean.setAlbum(album);
	        	String download = cursor.getString(7);
	        	bean.setDownload(download);
	        	bean.setShowSpeaker(false);
	        	data.add(bean);
	        	
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized int getNumberOfColoumn()
	{
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
	    cursor.close();
	    db.close();
	    return cnt;
	}
	
	public synchronized void updateFav(String path, String fav) {
	    SQLiteDatabase db = getWrite();
	 
	    ContentValues values = new ContentValues();
	    values.put(FAVOURITE, fav);
	    
	 
	    // updating row
	   db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	    db.close();
	}
	
	public synchronized void updateDate(String path, Long date) {
	    SQLiteDatabase db = getWrite();
	 
	    ContentValues values = new ContentValues();
	    values.put(DATE, date);
	    
	 
	    // updating row
	     db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	    db.close();
	}
	public synchronized void updateRecentPlayed(String path, Long time) {
	    SQLiteDatabase db = getWrite();
	 
	    ContentValues values = new ContentValues();
	    values.put(RECENT_PALYED, time);
	    
	 
	    // updating row
	     db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	    db.close();
	}
	
	public synchronized void updateAlbum(String path, String album) {
	    SQLiteDatabase db = getWrite();
	 
	    ContentValues values = new ContentValues();
	    values.put(ALBUM, album);
	    
	 
	    // updating row
	     db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	    db.close();
	}
	
	public synchronized void updateArtist(String path, String artist) {
	    SQLiteDatabase db = getWrite();
	 
	    ContentValues values = new ContentValues();
	    values.put(ARTIS, artist);
	    
	 
	    // updating row
	  db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	   db.close();
	}
	public synchronized void updateDownload(String path, String download) {
	    SQLiteDatabase db = getWrite();
	 
	    ContentValues values = new ContentValues();
	    values.put(DOWNLOAD, download);
	    
	 
	    // updating row
	  int i = db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	   db.close();
	}
	
	private synchronized SQLiteDatabase getWrite()
	{
		return this.getWritableDatabase();
	}
	
	public synchronized int getNumberOfColoumn(String str)
	{
		String countQuery = "SELECT  * FROM " + TABLE_NAME+" WHERE "+PATH_COLOUMN+"= "+str;
	    SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(countQuery, null);
	    int cnt = cursor.getCount();
	    cursor.close();
	    db.close();
	    return cnt;
	}
}
