package com.player.mediaplayer.database;

import java.util.ArrayList;
import java.util.List;

import com.player.mediplayer.beans.SongInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaylistTable extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "plalist.db";
	private static final String TABLE_NAME = "playlist";
	private static final String ID_COLOUMN = "ID";
	private static final String PATH_COLOUMN = "PATH";
	private static final String ARTIS = "ARTIST";
	private static final String FAVOURITE = "FAVOURITE";
	private static final String ALBUM = "ALBUM";
	private static final String DATE = "DATE";
	private static final String RECENT_PALYED = "RECENTPLAYED";
	private static final String DOWNLOAD = "DOWNLOAD";
	private static final String PLAYLIST_STATE = "PLAYLISTSTATE";
	private static final String PLAYLIST = "PLAYLIST";

	public PlaylistTable(Context context) {
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
				+ RECENT_PALYED + " text,"
				+ DOWNLOAD + " text,"
				+ PLAYLIST_STATE + " text,"
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
	
	public synchronized void updateAlbum(String path, String album) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(ALBUM, album);
	    
	 
	    // updating row
	     db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	    db.close();
	}
	
	public synchronized void insertSongInfo(SongInfo song,long time) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PATH_COLOUMN, song.getPath());
		values.put(ARTIS, song.getArtist());
		values.put(ALBUM, song.getAlbum());
		//values.put(PATH_COLOUMN, path);
		values.put(DATE, time);
		// insert row
		db.insert(TABLE_NAME, null, values);
		db.close();

	}
	
	public synchronized void insert(String path,String playlist, String playlistState) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PATH_COLOUMN, path);
		//values.put(PATH_COLOUMN, path);
		values.put(PLAYLIST, playlist);
		values.put(PLAYLIST_STATE, playlistState);
		// insert row
		long i = db.insert(TABLE_NAME, null, values);
		db.close();

	}
	
	public synchronized List<SongInfo> getAllPlaylistInfo(String playist)
	{
		List<SongInfo> data = new ArrayList<>();

	    String selectQuery = "SELECT * FROM " + TABLE_NAME+" WHERE " + PLAYLIST + " = \'"+playist+"\'";
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
	        	String playlistState = cursor.getString(8);
	        	boolean playState = false;
                playState = playlistState.equals("yes");
	        	bean.setPlaylistState(playState);
	          		data.add(bean);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized List<String> getAllPlaylist()
	{
		List<String> data = new ArrayList<>();

	    String selectQuery = "SELECT DISTINCT "+PLAYLIST+" FROM " + TABLE_NAME/*+" WHERE " + PLAYLIST + " = \'"+playist+"\'"*/;
	          SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	           // get  the  data into array,or class variable
	        	//SongInfo bean = new SongInfo();
	        	String path = cursor.getString(0);
	        	//bean.setPath(path);
	        	//String artist = cursor.getString(2);
	        	//bean.setArtist(artist);
	        	//String favourite = cursor.getString(3);
	        	//bean.setFavourite(favourite);
	        	//String album = cursor.getString(4);
	        	//bean.setAlbum(album);
	        	data.add(path);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized List<String> getAllPlaylistState()
	{
		List<String> data = new ArrayList<>();

	    String selectQuery = "SELECT  "+PLAYLIST_STATE+" FROM " + TABLE_NAME;//+" WHERE " + PLAYLIST_STATE + " = \'yes\'";
	          SQLiteDatabase db = this.getReadableDatabase();
	    Cursor cursor = db.rawQuery(selectQuery, null);
	             
	    if (cursor.moveToFirst()) {
	        do {
	           // get  the  data into array,or class variable
	        	//SongInfo bean = new SongInfo();
	        	String path = cursor.getString(0);
	        	//bean.setPath(path);
	        	//String artist = cursor.getString(2);
	        	//bean.setArtist(artist);
	        	//String favourite = cursor.getString(3);
	        	//bean.setFavourite(favourite);
	        	//String album = cursor.getString(4);
	        	//bean.setAlbum(album);
	        	data.add(path);
	        } while (cursor.moveToNext());
	    }
	    cursor.close();
	    db.close();
	    return data;
	}
	
	public synchronized void updateFav(String path, String fav) {
	    SQLiteDatabase db = this.getWritableDatabase();
	 
	    ContentValues values = new ContentValues();
	    values.put(FAVOURITE, fav);
	    
	 
	    // updating row
	    int i =  db.update(TABLE_NAME, values, PATH_COLOUMN + " = ?",
	            new String[] { String.valueOf(path) });
	    db.close();
	}
	
	public synchronized void delete(String playList)
	{
		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, PLAYLIST + " = ?",
	            new String[] { String.valueOf(playList) });
		database.close();
		
	}
	
	public synchronized void deleteAll() {
		SQLiteDatabase database = getWritableDatabase();
		database.delete(TABLE_NAME, null, null);
		database.close();
	}
	


}
