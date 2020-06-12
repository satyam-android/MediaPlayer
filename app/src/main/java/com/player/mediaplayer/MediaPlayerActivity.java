package com.player.mediaplayer;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.player.SongsPod.R;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.database.PlaylistTable;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.service.notificationService;
import com.player.mediplayer.beans.SongInfo;
import com.splunk.mint.Mint;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

public class MediaPlayerActivity extends FragmentActivity implements
		OnClickListener, OnSeekBarChangeListener, OnTouchListener {
	private TextView startTime, endTime, title;
	private SeekBar progress = null;
	private AudioPlayer player = null;
	private ImageView playPause = null;
	private Timer timerM = null;
	private ImageView Image = null;
	private int repeatValue = 1;
	private ImageView repeat = null, fav = null ;
	private boolean isfav = false;
	private SongsTable db = null;
	private PlaylistTable playdb = null;
	private float firstX;
	private float firstY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		Mint.initAndStartSession(this, "e45fc306");
		setContentView(R.layout.media_player_activity);
		player = AudioPlayer.getInstnace(this);
		db = new SongsTable(this);
		playdb = new PlaylistTable(this);
		loadWidget();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		time(progress);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		timerM.cancel();
	}

	private void loadWidget() {
		LinearLayout layout = findViewById(R.id.player_title);
		layout.setOnTouchListener(this);
		//LinearLayout main = (LinearLayout)findViewById(R.id.player_root);
		//main.setOnTouchListener(this);
		
		ImageView back = findViewById(R.id.media_player_back);
		back.setOnClickListener(this);
		ImageView option = findViewById(R.id.media_player_option);
		option.setOnClickListener(this);
		Image = findViewById(R.id.media_player_Image);
		Image.setOnTouchListener(this);
		playPause = findViewById(R.id.media_player_play);
		playPause.setOnClickListener(this);
		repeat = findViewById(R.id.media_player_repeat);
		repeat.setOnClickListener(this);
		ImageView prev = findViewById(R.id.media_player_prev);
		prev.setOnClickListener(this);
		ImageView next = findViewById(R.id.media_player_next);
		next.setOnClickListener(this);
		
		fav = findViewById(R.id.media_player_fav);
		fav.setOnClickListener(this);

		title = findViewById(R.id.media_player_title);
		endTime = findViewById(R.id.media_player_endTime);
		startTime = findViewById(R.id.media_player_startTime);

		progress = findViewById(R.id.media_player_progress_bar);
		progress.setOnSeekBarChangeListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.media_player_back:
			finish();

			break;
		case R.id.media_player_option:
			startActivity(new Intent(MediaPlayerActivity.this, MyDialogFragment.class));
			break;
		case R.id.media_player_Image:

			break;
		case R.id.media_player_play:

			Intent inte1 = new Intent(MediaPlayerActivity.this,
					notificationService.class);
			inte1.putExtra("song", player.songIndex);
			inte1.putExtra("work", "" + AppConstant.PLAY_STATE);
			startService(inte1);

			break;
		case R.id.media_player_repeat:
			repeatValue++;
			switch (repeatValue) {
			case 1:
				//repeatValue++;
				repeat.setImageResource(R.drawable.img_appwidget_playmode_sequence);
				player.isAllSongLoop = false;
				player.isOneSongLoop = false;
				break;
			case 2:
				//repeatValue++;
				repeat.setImageResource(R.drawable.img_appwidget_playmode_repeat);
				player.isAllSongLoop = true;
				player.isOneSongLoop = false;
				break;
			case 3:
				repeatValue = 0;
				repeat.setImageResource(R.drawable.img_appwidget_playmode_repeatone);
				player.isAllSongLoop = false;
				player.isOneSongLoop = true;
				break;

			default:
				break;
			}

			break;
		case R.id.media_player_prev:

			Intent inte2 = new Intent(MediaPlayerActivity.this,
					notificationService.class);
			inte2.putExtra("work", "" + AppConstant.PREV);
			startService(inte2);

			break;
		case R.id.media_player_next:
			Intent inte = new Intent(MediaPlayerActivity.this,
					notificationService.class);
			inte.putExtra("work", "" + AppConstant.NEXT);
			startService(inte);
			break;
		case R.id.media_player_fav:
			
			if(!isfav)
			{
				isfav = true;
			 fav.setImageResource(R.drawable.dislike);
			 db.updateFav(Utils.list.get(player.songIndex).getPath(), "yes");
			 playdb.updateFav(Utils.list.get(player.songIndex).getPath(), "yes");
			 Utils.list.get(player.songIndex).setFavourite("yes");
			}
			else
			{
				isfav = false;
				fav.setImageResource(R.drawable.like);
				db.updateFav(Utils.list.get(player.songIndex).getPath(), "no");
				playdb.updateFav(Utils.list.get(player.songIndex).getPath(), "no");
				Utils.list.get(player.songIndex).setFavourite("no");
			}
			
			break;

		default:
			break;
		}

	}

	private void time(final SeekBar progress) {
		timerM = new Timer();
		timerM.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					@SuppressWarnings("deprecation")
					public void run() {
						// home_player_title.setText(getSongName(Utils.list.get(songIndex)));
						// TextView text = getFragmentTextView();
						try {
							List<SongInfo> list = Utils.list;
							// if(text != null)
							// text.setText("" + list.size() + " songs");
							
							if (player.songIndex < Utils.list.size())
							{
								int size = getSongName(list
										.get(player.songIndex).getPath()).length();
								String str = getSongName(list
										.get(player.songIndex).getPath());
								if(size > 25)
								{
									str = str.substring(0,25)+"...";
								}
								title.setText(str);
								String favValue = list.get(player.songIndex).getFavourite();
								if(favValue != null && favValue.equals("yes"))
								{
									isfav = true;
									fav.setImageResource(R.drawable.dislike);
								}
								else
								{
									isfav = false;
									fav.setImageResource(R.drawable.like);
								}
							}
							if (player.getMediaPlayer() != null) {
								if (player.getMediaPlayer().isPlaying()) {
									playPause
											.setImageResource(R.drawable.pause);
									int duration = player.getMediaPlayer()
											.getDuration();
									int amoungToupdate = player
											.getMediaPlayer()
											.getCurrentPosition();
									int value = (amoungToupdate * 100)
											/ duration;
									progress.setProgress(value);
									startTime
											.setText(getmiliTotime(amoungToupdate));
									endTime.setText(getmiliTotime(duration));
									Bitmap bit = null;
									if (player.songIndex < Utils.list.size())
										bit = getImage(list
												.get(player.songIndex).getPath());
									if (bit != null) {
										BitmapDrawable ob = new BitmapDrawable(
												getResources(), bit);
										int sdk = android.os.Build.VERSION.SDK_INT;
										if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
											Image.setBackgroundDrawable(ob);
										} else {
											Image.setBackground(ob);
										}
									} else {
										// Bitmap bit =
										BitmapDrawable drawable = (BitmapDrawable) getResources()
												.getDrawable(
														R.drawable.img_imageview_soundsearch_result_default_cover);
										bit = drawable.getBitmap();

										BitmapDrawable ob = new BitmapDrawable(
												getResources(), bit);
										int sdk = android.os.Build.VERSION.SDK_INT;
										if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
											Image.setBackgroundDrawable(ob);
										} else {
											//player.isAllSongLoop = true;
											//player.isOneSongLoop = false;					
											Image.setBackground(ob);
										}

									}
									// Image.seti

								} else
									playPause
											.setImageResource(R.drawable.play);

							} else {
								playPause
										.setImageResource(R.drawable.play);
							}
						} catch (Exception e) {
							playPause
									.setImageResource(R.drawable.play);
						}
					}
				});

			}
		}, 10, 900);
	}

	private String getSongName(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
	}

	private String getmiliTotime(int value) {
		int sec = value / 1000;
		int min = 0;
		while (sec >= 60) {
			min++;
			sec = sec - 60;

		}
		String m = "";
		String s = "";
		if(min<=9)
		{
			m = "0"+min;
		}
		else
			m = min+"";
		if(sec<=9)
			s = "0"+sec;
		else
			s = sec+"";

		String time = m + ":" + s;
		return time;
	}

	private void getAudioInfo(String mp3File) {
		File file = new File(mp3File);
		MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
		Uri uri = Uri.fromFile(file);
		mediaMetadataRetriever.setDataSource(MediaPlayerActivity.this, uri);
		String title = mediaMetadataRetriever
				.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

	}

	private Bitmap getImage(String path) {
		MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
		metaRetriver.setDataSource(path);
		byte[] art = metaRetriver.getEmbeddedPicture();
		if (art != null) {
			Bitmap songImage = BitmapFactory
					.decodeByteArray(art, 0, art.length);
			return songImage;
		} else
			return null;

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if(player.getMediaPlayer() != null && fromUser){
			int duration = player.getMediaPlayer()
					.getDuration();
			long di = progress*duration;
			 int prog = (int)di/100;
			player.getMediaPlayer().seekTo(prog);
        }
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
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
			
			break;

		case MotionEvent.ACTION_MOVE:
			//if (!isDownClicked) {
				//return true;
			//}
			
			pointerIndex = event.getActionIndex();
			x = event.getX(pointerIndex);
			y = event.getY(pointerIndex);
			if(y>firstY)
			{
				//startActivity(new Intent(MediaPlayerActivity.this, homeActivity.class ));
				finish();
				overridePendingTransition(R.anim.down_in, R.anim.up_out);
			}
			break;
		default:
			break;

		}

		return true;

	}

}
