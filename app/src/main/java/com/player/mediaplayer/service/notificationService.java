package com.player.mediaplayer.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.player.SongsPod.R;
import com.player.mediaplayer.homeActivity;
import com.player.mediaplayer.reciever.notification_broadcast;
import com.player.mediaplayer.splash;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.constant.nextSong;
import com.player.mediaplayer.reciever.broadcastConst;
import com.player.mediplayer.beans.SongInfo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import android.text.style.BulletSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

public class notificationService extends Service implements nextSong {
	public static final String CHANNEL_ID = "SongsPodForegroundServiceChannel";
	private List<SongInfo> list;
	private AudioPlayer player;
	public int songIndex=0;
	private RemoteViews contentView = null;
	private int playerState;
	private Boolean isPlayerFirstPlay = true;
	private int NOTIFICATION_ID = 1;
	private ImageView play, next, pause, cross;
	private TextView title, singer, album;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		player = AudioPlayer.getInstnace(this);
		playerState=AppConstant.PAUSE_STATE;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		if(Utils.list.size() <= 0)
		{
			return START_STICKY;
		}

		if (intent.getStringExtra("work") != null && player !=null)
			playSongs(getApplicationContext(), intent);

		creatNotication(getApplicationContext());
		return START_NOT_STICKY;
	}



	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MediaPlayer pl = player.getMediaPlayer();
		pl.stop();
		pl.reset();
		//pl.release();
		//pl = null;
	}

	private void playSongs(Context context, Intent intent) {

		if (intent == null) {
			return;
		}
		initail(context);
		//player.songIndex = 0;
		list = Utils.list;
		String ns = Context.NOTIFICATION_SERVICE;
		// NotificationManager mNotificationManager = (NotificationManager)
		// context
		// .getSystemService(ns);
		// AudioPlayer player = AudioPlayer.getInstnace();
		// initail(context);


		/*
		 * contentView.setTextViewText(R.id.notification_album,
		 * "My custom notification text");
		 * contentView.setTextViewText(R.id.notification_singer,
		 * "My custom notification text");
		 */

		int i = Integer.parseInt(intent.getStringExtra("work"));
		if(i==AppConstant.START_BIGINING)
		{
			i = AppConstant.PLAY_STATE;
			isPlayerFirstPlay = true;
			playerState = AppConstant.PAUSE_STATE;
			player.songIndex = intent.getIntExtra("song", 0);

		}


		switch (i) {
		case AppConstant.PLAY_STATE:

			if (playerState == AppConstant.PAUSE_STATE) {
				playerState = AppConstant.PLAY_STATE;
				contentView.setImageViewResource(R.id.notification_play,
				 R.drawable.img_appwidget_pause);
				// home_play_pause.setImageResource(R.drawable.img_appwidget_pause);
				if (isPlayerFirstPlay) {
					isPlayerFirstPlay = false;
					// home_play_pause.

					player.playListIndex(this,player.songIndex);
					// mTimer.cancel();
					// runProgress(player.getMediaPlayer(), progressbar);

				} else {
					player.start();
					// runProgress(player.getMediaPlayer(), progressbar);
				}
			} else if (playerState == AppConstant.PLAY_STATE) {
				 contentView.setImageViewResource(R.id.notification_play,
				 R.drawable.img_appwidget_play);
				// home_play_pause.setImageResource(R.drawable.img_appwidget_play);
				player.pause();
				playerState = AppConstant.PAUSE_STATE;
				// mTimer.cancel();
			}
			// creatNotication(context);

			break;
		case AppConstant.CROSS:
			// mNotificationManager.cancel(NOTIFICATION_ID);

			break;
		case AppConstant.NEXT:
			if (player.getMediaPlayer().isPlaying()) {
				Log.i("inside next", "");
				if(player.songIndex>=list.size()-1)
				{
//					Log.i("inside next IS PLAYING if	", player.songIndex+"");
				}
				else

					{
//					Log.i("inside next IS PLAYING else	", player.songIndex+"");
					//player.songIndex++;
					player.next(this);
					}
				// mTimer.cancel();
				// progressbar.setProgress(0);
				// runProgress(player.getMediaPlayer(), progressbar);
			} else {
				if(player.songIndex>=list.size()-1)
				{}
				else
				{
				//player.songIndex++;
				player.songIndex++;}
				// home_player_title.setText(getSongName(Utils.list.get(player.player.songIndex)));
				// mTimer.cancel();
				// progressbar.setProgress(0);
				// isPlayerFirstPlay = true;
				MediaPlayer pl = player.getMediaPlayer();
				pl.stop();
				pl.reset();
				isPlayerFirstPlay = true;
			}

			// creatNotication(context);
			break;
		case AppConstant.PREV:

			if (player.getMediaPlayer().isPlaying()) {
				if(player.songIndex<=0)
				{
					//return;
				}
				else
				{
					//player.songIndex--;
					player.previous(this);
				}
				// mTimer.cancel();
				// progressbar.setProgress(0);
				// runProgress(player.getMediaPlayer(), progressbar);
			} else {
				if(player.songIndex<=0)
				{
					//return;
				}
				else
				{
					//player.songIndex--;
					player.songIndex--;
				}
				// home_player_title.setText(getSongName(Utils.list.get(player.player.songIndex)));
				// mTimer.cancel();
				// progressbar.setProgress(0);
				// isPlayerFirstPlay = true;
				MediaPlayer pl = player.getMediaPlayer();
				pl.stop();
				pl.reset();
				isPlayerFirstPlay = true;
			}

			// creatNotication(context);
			break;

		default:
			break;
		}
		String song = list.get(player.songIndex).getPath() ;
		song = song.substring(song.lastIndexOf("/") + 1, song.lastIndexOf("."));
		if (song.length() >= 17) {
			song = song.substring(0, 17) + "...";
		}

		contentView.setTextViewText(R.id.notification_title,
				 song);
	}

	/*public void playListIndex(int index) {
		if(index<0)
		{
			index=0;
			return;
		}

		if(index>list.size()-1)
		{
			index = list.size()-1;
			return;
		}

		try {
			player.songIndex = index;
			if (player.isPlaying()) {
				player.stop();
				player.reset();
			}
			player.setDataSource(list.get(index));// Write your location
													// here
			player.prepare();
			player.start();
			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					try {
						player.songIndex++;
						if (player.songIndex < list.size()) {
							player.reset();
							 load the new source
							player.setDataSource(list.get(player.songIndex));
							 Prepare the mediaplayer
							player.prepare();
							 start
							player.start();
							String song = list.get(player.songIndex);
							song = song.substring(song.lastIndexOf("/") + 1, song.lastIndexOf("."));
							if (song.length() >= 17) {
								song = song.substring(0, 17) + "...";
							}

							contentView.setTextViewText(R.id.notification_title,
									 song);
							creatNotication(getApplicationContext());
						} else {
							 release mediaplayer
							// player.release();
						}
					}

					catch (Exception e) {

					}
				}

			});
		} catch (ArrayIndexOutOfBoundsException e) {
			// e.printStackTrace();
			if (player.songIndex < 0)
				player.songIndex = 0;
			else if (player.songIndex > list.size())
				player.songIndex = list.size();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void next() {
		player.songIndex++;
		// if (player.isPlaying()) {
		player.stop();
		player.reset();
		// }
		playListIndex(player.songIndex);
	}

	public void previous() {
		player.songIndex--;
		// if (player.isPlaying()) {
		player.stop();
		player.reset();
		// }
		playListIndex(player.songIndex);
	}
*/
	private void creatNotication(Context cont)

	{

/*if (entry.targetSdk >= Build.VERSION_CODES.LOLLIPOP) {
    entry.icon.setColorFilter(mContext.getResources().getColor(android.R.color.white));
} else {
    entry.icon.setColorFilter(null);
}*/


		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) cont
				.getSystemService(ns);
		int icon = R.drawable.app_icon;
		long when = System.currentTimeMillis();
		createNotificationChannel();
		Intent intent = new Intent(this, homeActivity.class);
		// Pass data to the new activity

		// Starts the activity on notification click
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				cont,CHANNEL_ID).setSmallIcon(R.drawable.app_icon)
//				.setContentTitle("My notification")
//				.setContentText("Hello World!")
				.setContentIntent(pIntent);

		// Notification notification = new Notification(icon, "", when);

		 //contentView.setImageViewResource(R.id.notification_image,
		 //R.drawable.notification_image);

		builder.setContent(contentView);
//		builder.setCustomContentView(contentView);
		builder.setSmallIcon(R.drawable.app_icon);
		builder.setAutoCancel(true);
		builder.setDefaults(0);
		builder.setSound(null);

		builder.setLights(Color.WHITE, 500, 500); // Do not clear the
													// notification
		/*
		 * notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
		 * notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
		 * notification.defaults |= Notification.DEFAULT_SOUND; // Sound
		 */Notification not = builder.build();
		not.flags |= Notification.FLAG_ONGOING_EVENT|Notification.FLAG_NO_CLEAR;
		mNotificationManager.notify(NOTIFICATION_ID, not);
		startForeground(NOTIFICATION_ID, not);

	}

	private void initail(Context cont) {
		Intent switchIntentNEXT = new Intent(cont, notification_broadcast.class);
		switchIntentNEXT.putExtra("work", "" + AppConstant.NEXT);
		PendingIntent pendingSwitchIntentNEXT = PendingIntent.getBroadcast(
				cont, 100, switchIntentNEXT, PendingIntent.FLAG_UPDATE_CURRENT);
//		PendingIntent pendingSwitchIntentNEXT=PendingIntent.getService(cont,AppConstant.NEXT,switchIntentNEXT,0);

		Intent switchIntentCROSS = new Intent(cont,notification_broadcast.class);
		switchIntentCROSS.putExtra("work", "" + AppConstant.CROSS);
		PendingIntent pendingSwitchIntentCROSS = PendingIntent
				.getBroadcast(cont, 101, switchIntentCROSS,
						PendingIntent.FLAG_UPDATE_CURRENT);
//		PendingIntent pendingSwitchIntentCROSS=PendingIntent.getService(cont,AppConstant.CROSS,switchIntentCROSS,0);




		Intent switchIntentPREV = new Intent(cont,notification_broadcast.class);
		switchIntentPREV.putExtra("work", "" + AppConstant.PREV);
		PendingIntent pendingSwitchIntentPREV = PendingIntent.getBroadcast(
				cont, 102, switchIntentPREV, PendingIntent.FLAG_UPDATE_CURRENT);
//		PendingIntent pendingSwitchIntentPREV=PendingIntent.getService(cont,AppConstant.PREV,switchIntentPREV,0);

		Intent switchIntentPLAY = new Intent(cont,notification_broadcast.class);
		switchIntentPLAY.putExtra("work", "" + AppConstant.PLAY_STATE);
		PendingIntent pendingSwitchIntentPLAY = PendingIntent.getBroadcast(
				cont, 103, switchIntentPLAY, PendingIntent.FLAG_UPDATE_CURRENT);
//		PendingIntent pendingSwitchIntentPLAY=PendingIntent.getService(cont,AppConstant.PLAY_STATE,switchIntentPLAY,0);


		contentView = new RemoteViews(cont.getPackageName(),
				R.layout.notification_custom_view);

		contentView.setOnClickPendingIntent(R.id.notification_cross,
				pendingSwitchIntentCROSS);
		contentView.setOnClickPendingIntent(R.id.notification_prev,
				pendingSwitchIntentPREV);
		contentView.setOnClickPendingIntent(R.id.notification_play,
				pendingSwitchIntentPLAY);
		contentView.setOnClickPendingIntent(R.id.notification_next,
				pendingSwitchIntentNEXT);

	}


	@Override
	public void onNextSong(int index) {
		// TODO Auto-generated method stub
		String song = list.get(index).getPath();
		song = song.substring(song.lastIndexOf("/") + 1, song.lastIndexOf("."));
		if (song.length() >= 17) {
			song = song.substring(0, 17) + "...";
		}

		contentView.setTextViewText(R.id.notification_title,
				 song);
		contentView.setImageViewResource(R.id.notification_play,
				 R.drawable.img_appwidget_pause);
		creatNotication(getApplicationContext());

	}


	@Override
	public void afterSongComplete() {
		// TODO Auto-generated method stub
		isPlayerFirstPlay = true;

	}

	private void createNotificationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel serviceChannel = new NotificationChannel(
					CHANNEL_ID,
					"Foreground Service Channel",
					NotificationManager.IMPORTANCE_DEFAULT
			);
			NotificationManager manager = getSystemService(NotificationManager.class);
			serviceChannel.setSound(null,null);
			manager.createNotificationChannel(serviceChannel);
		}
	}

}
