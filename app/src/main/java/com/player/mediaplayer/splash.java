package com.player.mediaplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;

import com.google.android.material.snackbar.Snackbar;
import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.ExpandableListAdapterArtist;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.UserEmailFetcher;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.engine.appController;
import com.player.mediaplayer.network.emailAndImeidetailAsyncTask;
import com.player.mediaplayer.service.BackService;
import com.player.mediaplayer.service.notificationService;
import com.player.mediplayer.beans.SongInfo;
import com.player.mediplayer.beans.adapterBean;
import com.player.mediplayer.beans.objectClass;
import com.splunk.mint.Mint;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class splash extends Activity {
    private Handler handle = null;
    private long SCHE_TIME = 000;
    private ProgressDialog progress;
    private SharedPreferences pref = null;
    private SongsTable db = null;
    private HashMap<String, ArrayList<SongInfo>> listMapFolder = null;
    private List<String> listFolder = null;
    private adapterBean bean = null;
    private List<String> listAlbum = null;
    private HashMap<String, ArrayList<SongInfo>> listMapAlbum = null;
    private List<String> listArtist = null;
    private HashMap<String, ArrayList<SongInfo>> listMapArtist = null;
    private List<SongInfo> songList = null;
    private List<String> li = null;
    private final int PERMISSION_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Mint.initAndStartSession(splash.this, "e45fc306");
		setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {

                requestPermission();

            } else {

                startScan();

            }
        } else {
            startScan();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1= ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result2= ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE,READ_PHONE_STATE,GET_ACCOUNTS}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean phoneStateAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean accountAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted && phoneStateAccepted && accountAccepted)
                        startScan();
                        //   Toast.makeText(splash.this, "Permission Granted, Now you can access location data and camera.", Toast.LENGTH_LONG).show();
                    else {

                        //   Toast.makeText(splash.this, "Permission Denied, You cannot access location data and camera.", Toast.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE,READ_PHONE_STATE,GET_ACCOUNTS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                            }else if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE,READ_PHONE_STATE,GET_ACCOUNTS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                            }else if (shouldShowRequestPermissionRationale(GET_ACCOUNTS)) {
                                showMessageOKCancel("You need to allow access to the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_EXTERNAL_STORAGE,READ_PHONE_STATE,GET_ACCOUNTS},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                            }

                        }
                    }
                }
                return;
        }


//		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(splash.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    public void startScan() {

        // stop();

        pref = PreferenceManager.getDefaultSharedPreferences(this);
		/*long time = 1427221800000l;// 1423506600000L * 60*60*24*8;
		long curr = System.currentTimeMillis();
		if (curr > time) {
			// finish();
			Utils.toastshow(splash.this, "Please check your device time");
			return;
		}*/
        if (Utils.list.size() > 0) {
            startActivity(new Intent(splash.this, homeActivity.class));
            return;
        }
        db = new SongsTable(splash.this);
        listMapFolder = new HashMap<>();
        listFolder = new ArrayList<>();
        listMapAlbum = new HashMap<>();
        listAlbum = new ArrayList<>();
        listMapArtist = new HashMap<>();
        listArtist = new ArrayList<>();
        bean = adapterBean.getInstance();

        // appController.getInstance().init();
        boolean isFirst = pref.getBoolean("first", true);

        // isFirst = true;

        if (isFirst) {
            String path = "/storage/";
            File file = new File(path);
            File[] s = file.listFiles();

            li = new ArrayList<>();
            // gettingSongUsingContentProvider();
            li.add(getFilesDir().getAbsolutePath());
			li.add(Environment.getExternalStorageDirectory().toString());

			/*for (int i = 0; i < s.length; i++) {
				if (s[i].getAbsolutePath().contains("MediaPlayer")
						|| s[i].getAbsolutePath().contains("Card")
						|| s[i].getAbsolutePath().contains("card")) {
					li.add(s[i].getAbsolutePath());
				}
			}*/

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    for (int i = 0; i < li.size(); i++) {
                        new loadSongs(li.get(i), db).execute();
                    }
                }
            }, 500);

            // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // new loads()
            // .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            // } else {
            // new loads().execute();
            // }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        new loads()
                                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }, 500);

            } else {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        new loads().execute();
                    }
                }, 500);
            }

        }
        // new loadSongs(s[]).execute("");

        /*
         * Utils.getMusic(li.get(0)); new Handler().postDelayed(new Runnable() {
         *
         * @Override public void run() { // TODO Auto-generated method stub
         *
         * } }, 3000);
         *
         * if(Utils.list.size()>0) { startActivity(new Intent(splash.this,
         * homeActivity.class)); }
         */
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


        boolean emailsent = false;// pref.getBoolean("emailfirst", false);

        if (!emailsent) {
            sendDetail();

        }

    }

    private Object getObjectToInsert(List<String> path) {
        objectClass obj = new objectClass();
        obj.setAct(splash.this);
        obj.setObj(path);
        return obj;
    }

    Handler handlloadsongs = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            // handle = new Handler();
            // handle.postDelayed(new Runnable() {

            // @Override
            // public void run() {
            // // TODO Auto-generated method stub
            if (Utils.list.size() > 0) {
                /*
                 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                 * new makeSongInfoList(splash.this,0)
                 * .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR); } else {
                 * new makeSongInfoList(splash.this,0).execute(); }
                 */
                startActivity(new Intent(splash.this, homeActivity.class));
                Editor edit = pref.edit();
                edit.putBoolean("first", false);
                edit.commit();
                finish();
            } else {

                startActivity(new Intent(splash.this, homeActivity.class));
                Editor edit = pref.edit();
                edit.putBoolean("first", true);
                edit.commit();
                finish();
            }
        }

    };

    private class loadSongs extends AsyncTask<String, Integer, String> {
        String path = null;
        SongsTable db = null;

        public loadSongs(String path, SongsTable db) {
            // TODO Auto-generated constructor stub
            this.path = path;
            this.db = db;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // progress = ProgressDialog.show(splash.this, "",
            // "App Initializing");
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            Utils.getMusic(path, db);
            handlloadsongs.sendEmptyMessage(0);
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            // progress.cancel();

            // }
            // }, SCHE_TIME);
        }

    }

    private void makedbListFolder() {

        for (int i = 0; i < songList.size(); i++) {

            String path = songList.get(i).getPath();
            String album = songList.get(i).getAlbum();
            String folder = getFolerName(path);

            ArrayList<SongInfo> arr = listMapFolder.get(folder);
            if (arr == null) {
                arr = new ArrayList<>();
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arr.add(bean);
                listMapFolder.put(folder, arr);
                listFolder.add(folder);
            } else {
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arr.add(bean);
            }

            // album//

            // String path = songList.get(i).getPath();
            // String album = songList.get(i).getAlbum();

            ArrayList<SongInfo> arralb = listMapAlbum.get(album);
            if (arralb == null) {
                arralb = new ArrayList<>();
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arralb.add(bean);
                listMapAlbum.put(album, arralb);
                listAlbum.add(album);
            } else {
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arralb.add(bean);
            }
            // end album//

            // artist//

            // String path = songList.get(i).getPath();
            String artist = songList.get(i).getArtist();
            // String album = songList.get(i).getAlbum();
            ArrayList<SongInfo> arrArt = listMapArtist.get(artist);
            if (arrArt == null) {
                arrArt = new ArrayList<>();
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arrArt.add(bean);
                listMapArtist.put(artist, arrArt);
                listArtist.add(artist);
            } else {
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arrArt.add(bean);
            }

            // endartis//

        }

    }

    private String getFolerName(String path) {
        String[] split = path.split("/");
        return split[split.length - 2];
    }

    private void makedbListAlbum() {
        // List<SongInfo> songList = db.getAllList();
        for (int i = 0; i < songList.size(); i++) {

            String path = songList.get(i).getPath();
            String album = songList.get(i).getAlbum();

            ArrayList<SongInfo> arr = listMapAlbum.get(album);
            if (arr == null) {
                arr = new ArrayList<>();
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arr.add(bean);
                listMapAlbum.put(album, arr);
                listAlbum.add(album);
            } else {
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arr.add(bean);
            }

            /*
             * runOnUiThread(new Runnable() { public void run() { if(adap !=
             * null) { List<String> li = adap.getList(); li.addAll(tempList);
             * tempList.clear(); adap.notifyDataSetChanged(); } } });
             */

        }

    }

    private void makedbListArtist() {
        // List<SongInfo> songList = db.getAllList();
        for (int i = 0; i < songList.size(); i++) {

            String path = songList.get(i).getPath();
            String artist = songList.get(i).getArtist();
            String album = songList.get(i).getAlbum();
            ArrayList<SongInfo> arr = listMapArtist.get(artist);
            if (arr == null) {
                arr = new ArrayList<>();
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arr.add(bean);
                listMapArtist.put(artist, arr);
                listArtist.add(artist);
            } else {
                SongInfo bean = new SongInfo();
                bean.setPath(path);
                bean.setAlbum(album);
                arr.add(bean);
            }

            /*
             * runOnUiThread(new Runnable() { public void run() { if(adap !=
             * null) { List<String> li = adap.getList(); li.addAll(tempList);
             * tempList.clear(); adap.notifyDataSetChanged(); } } });
             */

        }

    }

    Handler handlloads = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Utils.list = songList;// db.getAllList();

            startActivity(new Intent(splash.this, homeActivity.class));
            finish();
        }

    };

    private class loads extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            songList = db.getAllList();
            makedbListFolder();
            ExpandableListAdapterArtist adapFolder = new ExpandableListAdapterArtist(
                    splash.this, listFolder, listMapFolder);
            bean.setExplistFolder(adapFolder);
            // makedbListAlbum();
            ExpandableListAdapterArtist adapAlbum = new ExpandableListAdapterArtist(
                    splash.this, listAlbum, listMapAlbum);
            bean.setExplistAlbum(adapAlbum);
            // makedbListArtist();
            ExpandableListAdapterArtist adapArtist = new ExpandableListAdapterArtist(
                    splash.this, listArtist, listMapArtist);
            bean.setExpListArtist(adapArtist);

            /*
             * try { Thread.sleep(1000); } catch (InterruptedException e) { //
             * e.printStackTrace(); }
             */

            // startService(new Intent(splash.this, BackService.class));
            handlloads.sendEmptyMessage(0);
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

        }
    }


    /*********************************************************************************/
    /*********************************************************************************/
    /*************************** getting song using mediastore *************************/
    /*********************************************************************************/
    /*********************************************************************************/


    ArrayList<String> fullsongpath = null;

    String[] STAR = {"*"};
    int totalSongs;

    public ArrayList<String> ListAllSongs() {
        Uri allsongsuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        /* if (MusicUtils.isSdPresent()) { */
        Cursor cursor = managedQuery(allsongsuri, STAR, selection, null, null);

        totalSongs = cursor.getCount();

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String songname = cursor
                            .getString(cursor
                                    .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    int song_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media._ID));

                    String fullpath = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    fullsongpath.add(fullpath);

                    String albumname = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    int album_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                    String artistname = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int artist_id = cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST_ID));

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        // }
        return fullsongpath;
    }


    /***********************************************************************************************/
    /*******************************************************************************************/
    /************************************** end of getting song from mediastore *****************/
    /*****************************************************************************************/
    /*******************************************************************************************/






/*	private void gettingSongUsingContentProvider() {
		String[] projection = { MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.DATE_ADDED, MediaStore.Audio.Media.DATA,
				MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.TITLE,

				MediaStore.Audio.Media.DISPLAY_NAME,
				MediaStore.Audio.Media.DURATION };
		ContentResolver cr = getContentResolver();

		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
		String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
		Cursor cursor = cr.query(uri, projection, selection, null, sortOrder);
		int count = 0;

		if (cursor != null) {
			count = cursor.getCount();

			if (count > 0) {
				while (cursor.moveToNext()) {

					String songname = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME));// (cursor
					// .getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
					int song_id = cursor.getInt(cursor
							.getColumnIndex(MediaStore.Audio.Media._ID));
					String time = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.DATE_ADDED));
					String fullpath = cursor
							.getString(cursor
									.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));// (cursor
					// .getColumnIndex(MediaStore.Audio.Media.DATA));
					db.insert(fullpath, Long.parseLong(time));

					String albumname = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ALBUM));
					String artistname = cursor.getString(cursor
							.getColumnIndex(MediaStore.Audio.Media.ARTIST));

					db.updateArtist(fullpath, artistname);
					db.updateAlbum(fullpath, albumname);


					 * String data =
					 * cur.getString(cur.getColumnIndex(MediaStore.
					 * Audio.Media.DATA)); if(data.contains("Card") ||
					 * data.contains("card")) { String str = data; String
					 * streing = str; }


					// Add code to get more column here

					// Save to your list here
				}

			}
		}

		cursor.close();
	}*/
    private void sendDetail() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String device_imei = "";
        try {
            device_imei = telephonyManager.getDeviceId();

            if (device_imei == null) {

                WifiManager wifi = (WifiManager) getSystemService(WIFI_SERVICE);
                WifiInfo wifiinfo = wifi.getConnectionInfo();
                device_imei = wifiinfo.getMacAddress();
                // device_imei=generateDeviceId();

            }
        } catch (Exception e) {

        }

        String email = UserEmailFetcher.getEmail(splash.this);
        String versionName = "";
        try {
            versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new emailAndImeidetailAsyncTask(splash.this, email, device_imei, versionName)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new emailAndImeidetailAsyncTask(splash.this, email, device_imei, versionName)
                    .execute("");
        }
    }

    private void stop() {
        long time = 1421736000000l;
        long curr = System.currentTimeMillis();
        if (curr > time) {
            finish();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (BackService.isOneTimeRun)
            stopService(new Intent(splash.this, BackService.class));
        else
            BackService.stopeservice = true;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private String generateDeviceId() {
        final String macAddr, androidId;

        WifiManager wifiMan = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        macAddr = wifiInf.getMacAddress();
        androidId = ""
                + android.provider.Settings.Secure.getString(
                getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), macAddr.hashCode());
        return deviceUuid.toString();
        // Maybe save this: deviceUuid.toString()); to the preferences.
    }

}