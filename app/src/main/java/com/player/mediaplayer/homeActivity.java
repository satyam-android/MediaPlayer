package com.player.mediaplayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.player.SongsPod.R;
import com.player.mediaplayer.adapter.LocalSongsAdapter;
import com.player.mediaplayer.adapter.TabsPagerAdapter;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.network.downloadApk;
import com.player.mediaplayer.reciever.ListenToPhoneState;

import com.player.mediaplayer.service.BackService;
import com.player.mediaplayer.service.notificationService;
import com.player.mediplayer.beans.SongInfo;
import com.splunk.mint.Mint;

import android.animation.Animator;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

public class homeActivity extends FragmentActivity implements OnClickListener,
		OnTouchListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private TextView my, download, feature,category;
	private View my_blue, feature_blue, download_blue,category_blue;
	public int playerState = AppConstant.PAUSE_STATE;
	private AudioPlayer player = null;
	public ImageView home_play_pause = null;
	private String[] tabs = { "My", "Feature", "Download" };
	public Boolean isPlayerFirstPlay = true;
	public TextView home_player_title = null;
	public ProgressBar progressbar = null;
	// public Timer mTimer = null;
	public int progressRate;
	private LinearLayout menu;
	private boolean isMenuVisible = false;
	private ImageView search;
	public int songIndex = 0;
	private Timer timerM = null;
	public TextView text = null;
	private LinearLayout home_player = null;
	private boolean isDownClicked;
	private Float firstX, firstY;
	private SongsTable db = null;
	private List<SongInfo> songlist = null;
	private SharedPreferences pref = null;
	private boolean showDownloadDialog = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD) {
				enableStrictMode(this);
				}
				else{
				//If the android version is less than 2.3 log a message to LogCat
				Log.v("VERSION < Android 2.3", "StrictMode not supported");
				}
				
			
		
		
		
		// TODO Auto-generated method stub
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Mint.initAndStartSession(this, "e45fc306");
		setContentView(R.layout.home);
		pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		db = new SongsTable(homeActivity.this);
		songlist = db.getAllList();
		loadWidget();
		loadPhoneState();
		time(progressbar);

		// startService(new Intent(this, notificationService.class));
		// mTimer = new Timer();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this);
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(4);

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				// actionBar.setSelectedNavigationItem(position);
				switch (position) {
				case 0:

					// viewPager.setCurrentItem(0);
					my_blue.setVisibility(View.VISIBLE);
					feature_blue.setVisibility(View.INVISIBLE);
					download_blue.setVisibility(View.INVISIBLE);
					category_blue.setVisibility(View.INVISIBLE);
					break;
				case 1:
					// viewPager.setCurrentItem(1);
					my_blue.setVisibility(View.INVISIBLE);
					feature_blue.setVisibility(View.VISIBLE);
					download_blue.setVisibility(View.INVISIBLE);
					category_blue.setVisibility(View.INVISIBLE);
					break;
				case 2:
					// viewPager.setCurrentItem(2);
					my_blue.setVisibility(View.INVISIBLE);
					feature_blue.setVisibility(View.INVISIBLE);
					download_blue.setVisibility(View.VISIBLE);
					category_blue.setVisibility(View.INVISIBLE);
					break;
				case 3:
					my_blue.setVisibility(View.INVISIBLE);
					feature_blue.setVisibility(View.INVISIBLE);
					download_blue.setVisibility(View.INVISIBLE);
					category_blue.setVisibility(View.VISIBLE);
					
					break;

				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		final String downloadapk = pref.getString("apkurl", "");
	/*	if(!downloadapk.equals("") && showDownloadDialog)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Install latest version");
			builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
					
					
				}
			});
			
			builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
					 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						 new downloadApk(homeActivity.this, downloadapk)
					 .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					 } else {
						 new downloadApk(homeActivity.this, downloadapk).execute();
					}
					//new downloadApk(homeActivity.this, downloadapk).execute("");
					
					
				}
			});
			builder.show();
			showDownloadDialog = false;
		}
		time(progressbar);*/
		/*
		 * MediaPlayer pl = player.getMediaPlayer(); if(pl.isPlaying()) {
		 * 
		 * int length = pl.getCurrentPosition();
		 * progressbar.setProgress(length);
		 * home_play_pause.setImageResource(R.drawable.img_appwidget_pause);
		 * mTimer.cancel(); runProgress(player.getMediaPlayer(), progressbar);
		 * 
		 * }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main_activity_action, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_search:

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/*
	 * private void setImageOnActionBar() {
	 * actionBar.setHomeButtonEnabled(false); actionBar.setIcon( new
	 * ColorDrawable(getResources().getColor(android.R.color.transparent)));
	 * actionBar.setBackgroundDrawable(new
	 * ColorDrawable(Color.parseColor("#95000000")));
	 * actionBar.setStackedBackgroundDrawable(new
	 * ColorDrawable(Color.parseColor("#95000000")));
	 * 
	 * actionBar.setDisplayOptions(actionBar.getDisplayOptions() |
	 * ActionBar.DISPLAY_SHOW_CUSTOM); ImageView imageView = new
	 * ImageView(actionBar.getThemedContext()); imageView.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // TODO Auto-generated method
	 * stub Toast.makeText(getApplicationContext(), "youclicked", 1000).show();
	 * 
	 * } }); imageView.setScaleType(ImageView.ScaleType.CENTER);
	 * imageView.setImageResource(R.drawable.img_search); ActionBar.LayoutParams
	 * layoutParams = new ActionBar.LayoutParams(
	 * ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
	 * Gravity.RIGHT | Gravity.CENTER_VERTICAL); layoutParams.rightMargin = 40;
	 * imageView.setLayoutParams(layoutParams);
	 * actionBar.setCustomView(imageView);
	 * 
	 * 
	 * }
	 */

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.i("homeactivity", "inside ondestroy");
		startService(new Intent(homeActivity.this, BackService.class));
		super.onDestroy();
		// stopService(new Intent(this, notificationService.class));
	}

	private void loadWidget() {
		viewPager = findViewById(R.id.pager);
		player = AudioPlayer.getInstnace(this);
		progressbar = findViewById(R.id.home_progress_bar);
		menu = findViewById(R.id.home_menu);
		search = findViewById(R.id.home_search_button_main);
		search.setOnClickListener(this);
		my = findViewById(R.id.home_my_tab);
		my.setOnClickListener(this);
		feature = findViewById(R.id.home_feature_tab);
		feature.setOnClickListener(this);
		download = findViewById(R.id.home_download_tab);
		download.setOnClickListener(this);
		category = findViewById(R.id.home_category_tab);
		category.setOnClickListener(this);

		TextView home_exit_button = findViewById(R.id.home_exit_button);
		home_exit_button.setOnClickListener(this);
		TextView aboutUS = findViewById(R.id.home_about_us);
		aboutUS.setOnClickListener(this);
		TextView feedback = findViewById(R.id.home_feedback);
		feedback.setOnClickListener(this);
		TextView download = findViewById(R.id.home_download);
		download.setOnClickListener(this);

		my_blue = findViewById(R.id.home_my_tab_blue);
		my_blue.setVisibility(View.VISIBLE);
		feature_blue = findViewById(R.id.home_feature_tab_blue);
		download_blue = findViewById(R.id.home_download_tab_blue);
		category_blue = findViewById(R.id.home_category_tab_blue);

		home_player = findViewById(R.id.home_player);
		home_player.setOnTouchListener(this);

		ImageView home_player_song_image = findViewById(R.id.home_bottom_music);
		home_player_song_image.setOnClickListener(this);
		home_player_title = findViewById(R.id.home_movable_text);
		// home_player_title.setOnTouchListener(this);
		if(Utils.list.size()>0)
		home_player_title.setText(getSongName(Utils.list.get(songIndex).getPath()));
		// home_player_title.setOnClickListener(this);
		home_play_pause = findViewById(R.id.home_play_pause);
		home_play_pause.setOnClickListener(this);
		ImageView home_play_next = findViewById(R.id.home_play_next);
		home_play_next.setOnClickListener(this);
		ImageView home_option_menu = findViewById(R.id.home_option_menu);
		home_option_menu.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.home_download:
			List<SongInfo> value = db.getAllListDownload();
			//Utils.list = db.getAllList();
			Intent intent = new Intent(homeActivity.this, MyDialogFragment.class);
			intent.putExtra("abcd", (ArrayList<SongInfo>)value);
			intent.putExtra("scr", "Download");
			startActivity(intent);
			break;
		case R.id.home_feedback:
			onButtonClickSend();
			break;
		case R.id.home_about_us:
			startActivity(new Intent(homeActivity.this, about_us_activity.class));
			break;
		case R.id.home_exit_button:
			finish();
			break;
		case R.id.home_option_menu:

			if (!isMenuVisible) {
				menu.setVisibility(View.VISIBLE);
				isMenuVisible = true;
				Animation anim = AnimationUtils.loadAnimation(
						homeActivity.this, R.anim.grow_from_bottom);
				menu.setAnimation(anim);

			}

			break;
		case R.id.home_play_next:
			/*
			 * if(player.getMediaPlayer().isPlaying()) { songIndex++;
			 * home_player_title
			 * .setText(getSongName(Utils.list.get(songIndex)));
			 * mTimer.cancel(); progressbar.setProgress(0);
			 * runProgress(player.getMediaPlayer(), progressbar); } else {
			 * songIndex++;
			 * home_player_title.setText(getSongName(Utils.list.get(
			 * songIndex)));
			 * 
			 * progressbar.setProgress(0); isPlayerFirstPlay = true;
			 * //MediaPlayer pl = player.getMediaPlayer(); //pl.stop();
			 * //pl.reset(); }
			 */

			Intent inte = new Intent(homeActivity.this,
					notificationService.class);
			inte.putExtra("work", "" + AppConstant.NEXT);
			startService(inte);

			break;
		case R.id.home_play_pause:
			/*
			 * if(playerState == AppConstant.PAUSE_STATE) { playerState =
			 * AppConstant.PLAY_STATE;
			 * home_play_pause.setImageResource(R.drawable.img_appwidget_pause);
			 * if(isPlayerFirstPlay) { isPlayerFirstPlay = false;
			 * //home_play_pause.
			 * 
			 * //player.playListIndex(player.songIndex,home_player_title,
			 * homeActivity.this);
			 * 
			 * 
			 * 
			 * } else { //player.start();
			 * 
			 * } } else if(playerState == AppConstant.PLAY_STATE) {
			 * home_play_pause.setImageResource(R.drawable.img_appwidget_play);
			 * //player.pause(); playerState = AppConstant.PAUSE_STATE;
			 * 
			 * }
			 */

			Intent inte1 = new Intent(homeActivity.this,
					notificationService.class);
			inte1.putExtra("song", player.songIndex);
			inte1.putExtra("work", "" + AppConstant.PLAY_STATE);
			startService(inte1);
			break;
		case R.id.home_movable_text:
			break;
		case R.id.home_bottom_music:
			break;
		case R.id.home_my_tab:

			viewPager.setCurrentItem(0);
			my_blue.setVisibility(View.VISIBLE);
			feature_blue.setVisibility(View.INVISIBLE);
			download_blue.setVisibility(View.INVISIBLE);
			category_blue.setVisibility(View.INVISIBLE);
			break;
		case R.id.home_feature_tab:
			viewPager.setCurrentItem(1);
			my_blue.setVisibility(View.INVISIBLE);
			feature_blue.setVisibility(View.VISIBLE);
			download_blue.setVisibility(View.INVISIBLE);
			category_blue.setVisibility(View.INVISIBLE);
			break;
		case R.id.home_download_tab:
			viewPager.setCurrentItem(2);
			my_blue.setVisibility(View.INVISIBLE);
			feature_blue.setVisibility(View.INVISIBLE);
			download_blue.setVisibility(View.VISIBLE);
			category_blue.setVisibility(View.INVISIBLE);
			break;
		case R.id.home_category_tab:
			viewPager.setCurrentItem(3);
			my_blue.setVisibility(View.INVISIBLE);
			feature_blue.setVisibility(View.INVISIBLE);
			download_blue.setVisibility(View.INVISIBLE);
			category_blue.setVisibility(View.VISIBLE);
			break;
			
		case R.id.home_search_button_main:
			startActivity(new Intent(homeActivity.this, searchActivity.class));
			break;

		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isMenuVisible) {
			menu.setVisibility(View.GONE);
			isMenuVisible = false;
			Animation anim = AnimationUtils.loadAnimation(homeActivity.this,
					R.anim.shrink_from_top);
			menu.setAnimation(anim);
		}

		else
			super.onBackPressed();
	}

	private String getSongName(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
	}

	public int[] getYCordinate() {
		int[] location = new int[2];
		search.getLocationOnScreen(location);

		int[] loc = new int[2];
		home_player_title.getLocationOnScreen(loc);
		// loc[0] = location[1];
		int[] locat = new int[2];
		locat[0] = location[1];
		locat[1] = loc[1];

		return locat;
	}

/*	private class MyDialogFragment extends DialogFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
		}

		@Override
		public void onStart() {
			super.onStart();
			Dialog d = getDialog();
			if (d != null) {
				homeActivity hom = homeActivity.this;
				int y[] = hom.getYCordinate();
				int width = ViewGroup.LayoutParams.MATCH_PARENT;
				int height = ViewGroup.LayoutParams.MATCH_PARENT;// y[1];//
				Window win = d.getWindow();
				win.setGravity(Gravity.TOP);
				d.getWindow().setLayout(width, height);
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.media_player_activity,
					container, false);
			ImageView back = (ImageView) root
					.findViewById(R.id.media_player_back);
			back.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});
			return root;
		}

	}*/

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		final int pointerIndex;
		final float x, y;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:

			pointerIndex = event.getActionIndex();
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);
			firstX = x;
			firstY = y;
			isDownClicked = true;
			break;
		case MotionEvent.ACTION_UP:
			isDownClicked = false;
			pointerIndex = event.getActionIndex();
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);
			if(y == firstY)
			{
				startActivity(new Intent(homeActivity.this, MediaPlayerActivity.class ));
				overridePendingTransition(R.anim.down_in, R.anim.up_out);
			}
			break;

		case MotionEvent.ACTION_MOVE:
			//if (!isDownClicked) {
				//return true;
			//}
			isDownClicked = false;
			pointerIndex = event.getActionIndex();
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);
			if(y<firstY)
			{
				startActivity(new Intent(homeActivity.this, MediaPlayerActivity.class ));
				overridePendingTransition(R.anim.down_in, R.anim.up_out);
			}
			break;
		default:
			break;

		}

		return true;

	}

	private void time(final ProgressBar progress) {
		/*if(timerM !=null)
		{
			return;
		}*/
		timerM = new Timer();
		timerM.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						// home_player_title.setText(getSongName(Utils.list.get(songIndex)));
					try {
						TextView text = getFragmentTextView();
						List<SongInfo> list = Utils.list;
						if (text != null) {
							int st = songlist.size();
							if(st == 0)
							{
								//st = db.getNumberOfColoumn();
							}
							text.setText("" + st + " songs");
						}
						//        if(player.songIndex < Utils.list.size())
							if(list.size()>0)
						home_player_title.setText(getSongName(list
								.get(player.songIndex).getPath()));
						
							if (player.getMediaPlayer() != null) {
								if (player.getMediaPlayer().isPlaying()) {
									home_play_pause
											.setImageResource(R.drawable.play_bar_pause);
									int duration = player.getMediaPlayer()
											.getDuration();
									int amoungToupdate = player
											.getMediaPlayer()
											.getCurrentPosition();
									int value = (amoungToupdate * 100)
											/ duration;
									progress.setProgress(value);
								} else
									home_play_pause
											.setImageResource(R.drawable.play_bar_play);

							} else {
								home_play_pause
										.setImageResource(R.drawable.play_bar_play);
							}
						} catch (Exception e) {
							home_play_pause
									.setImageResource(R.drawable.play_bar_play);
						}
					}
				});

			}
		}, 10, 100);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		timerM.cancel();
		//timerM = null;
	}

	public void setFragmentTextView(TextView text) {
		this.text = text;
	}

	private TextView getFragmentTextView() {
		return text;
	}
	
	private void loadPhoneState()
	{
		ListenToPhoneState phoneListener=new ListenToPhoneState(homeActivity.this);
	    TelephonyManager telephony = (TelephonyManager) 
	   getSystemService(Context.TELEPHONY_SERVICE);
	    telephony.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
	    
	}
	
	public void onButtonClickSend()
    {
        //get to, subject and content from the user input and store as string.
         // String emailTo         = editTextEailTo.getText().toString();
          String emailSubject     = "SongsPod feedback";//editTextEmailSubject.getText().toString();
          //String emailContent     = editTextEmailContent.getText().toString();
 
          Intent emailIntent = new Intent(Intent.ACTION_SEND);
          emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ "songspodapp@gmail.com"});
          emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
          //emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
          /// use below 2 commented lines if need to use BCC an CC feature in email
          //emailIntent.putExtra(Intent.EXTRA_CC, new String[]{ to});
          //emailIntent.putExtra(Intent.EXTRA_BCC, new String[]{to});
          ////use below 3 commented lines if need to send attachment
          //emailIntent .setType("image/jpeg");
         // emailIntent .putExtra(Intent.EXTRA_SUBJECT, "My Picture");
         // emailIntent .putExtra(Intent.EXTRA_STREAM, Uri.parse("file://sdcard/captureimage.png"));
 
          //need this to prompts email client only
          emailIntent.setType("message/rfc822");
 
          startActivityForResult(Intent.createChooser(emailIntent, "Select an Email Client:"),1000);
    }
	
	
	
	public static void enableStrictMode(Context context) {
		StrictMode.setThreadPolicy(
			new StrictMode.ThreadPolicy.Builder()
			.detectDiskReads()
			.detectDiskWrites()
			.detectNetwork()
			.penaltyLog()
			.build());
			StrictMode.setVmPolicy(
			new StrictMode.VmPolicy.Builder()
			.detectLeakedSqlLiteObjects()
			.penaltyLog()
			.build());
			}
		
	
	

}
