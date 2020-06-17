package com.player.mediaplayer.reciever;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import com.player.SongsPod.R;
import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.service.notificationService;

import java.util.List;

public class notification_broadcast extends BroadcastReceiver {
	//private int playerState=AppConstant.PAUSE_STATE;
	//private Boolean isPlayerFirstPlay = true;
	private int NOTIFICATION_ID = 1;
	private ImageView play, next, pause, cross;
	private TextView title, singer, album;
	private RemoteViews contentView = null;
	private List<String> list;
	private Context context = null;
	//private int i ;

	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent == null)
		{
			return;
		}
		
		
		this.context  =context;
//		Log.i("##@@ inside broadcast", intent.getAction());
		//Toast.makeText(context.getApplicationContext(), "faf", 1000).show();
		//list = Utils.list;
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
		//AudioPlayer player = AudioPlayer.getInstnace();
		//initail(context);
		//String song = list.get(player.songIndex);
		//song = song.substring(song.lastIndexOf("/")+1, song.lastIndexOf("."));
		//if(song.length()>=17)
		//{
		//	song = song.substring(0, 17)+"...";
		//}
		//contentView.setTextViewText(R.id.notification_title,
		//		song);
		/*contentView.setTextViewText(R.id.notification_album,
				"My custom notification text");
		contentView.setTextViewText(R.id.notification_singer,
				"My custom notification text");*/
		
		
		
		int i = Integer.parseInt(intent.getStringExtra("work"));
/*		String action  = intent.getAction();
		if(action.equalsIgnoreCase("com.player.MediaPlayer.play"))
			i=3;
		else if(action.equalsIgnoreCase("com.player.MediaPlayer.next")) {
			i=5;
		}
		else if (action.equalsIgnoreCase("com.player.MediaPlayer.prev")) {
			i=6;
			
		}
		else if (action.equalsIgnoreCase("com.player.MediaPlayer.cross")) {
			i=7;
		}*/
//		i=AppConstant.CROSS;
		/*else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
	                TelephonyManager.EXTRA_STATE_RINGING)) {
			i=3;
	                // Phone number 
	                //String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

	                // Ringing state
	                // This code will execute when the phone has an incoming call
	        } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
	                TelephonyManager.EXTRA_STATE_IDLE)
	                || intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
	                        TelephonyManager.EXTRA_STATE_OFFHOOK)) {
	        	i=3;
	            // This code will execute when the call is answered or disconnected
	        }*/
		
		
		
		
		switch (i) {
		case AppConstant.PLAY_STATE:
			
			
			
			
			/*if(broadcastConst.playerState == AppConstant.PAUSE_STATE)
			{
				//broadcastConst.playerState = AppConstant.PLAY_STATE;
				contentView.setImageViewResource(R.id.notification_play, R.drawable.img_appwidget_pause);
				//home_play_pause.setImageResource(R.drawable.img_appwidget_pause);
				if(broadcastConst.isPlayerFirstPlay)
				{
					//broadcastConst.isPlayerFirstPlay = false;
					//home_play_pause.
				
				//player.playListIndex(player.songIndex);
				//mTimer.cancel();
				//runProgress(player.getMediaPlayer(), progressbar);
				
				}
				else
				{
					//player.start();
					//runProgress(player.getMediaPlayer(), progressbar);
				}
			}
			else if(broadcastConst.playerState == AppConstant.PLAY_STATE)
			{
				contentView.setImageViewResource(R.id.notification_play, R.drawable.img_appwidget_play);
				//home_play_pause.setImageResource(R.drawable.img_appwidget_play);
				//player.pause();
				//broadcastConst.playerState = AppConstant.PAUSE_STATE;
				//mTimer.cancel();
			}*/
			Intent inte = new Intent(context, notificationService.class);
			inte.putExtra("work", ""+AppConstant.PLAY_STATE);
			context.startService(inte);
			//creatNotication(context);

			break;
		case AppConstant.CROSS:
			mNotificationManager.cancel(NOTIFICATION_ID);
			context.stopService(new Intent(context, notificationService.class));

			break;
		case AppConstant.NEXT:
			/*if(player.getMediaPlayer().isPlaying())
			{
				//player.next();
				//mTimer.cancel();
				//progressbar.setProgress(0);
				//runProgress(player.getMediaPlayer(), progressbar);
			}
			else
			{
				//player.songIndex++;
				//home_player_title.setText(getSongName(Utils.list.get(player.songIndex)));
				//mTimer.cancel();
				//progressbar.setProgress(0);
				//isPlayerFirstPlay = true;
				//MediaPlayer pl = player.getMediaPlayer();
				//pl.stop();
				//pl.reset();
			}*/
			
			Intent inte1 = new Intent(context, notificationService.class);
			inte1.putExtra("work",""+ AppConstant.NEXT);
			context.startService(inte1);
			//creatNotication(context);
			break;
		case AppConstant.PREV:
			
			/*if(player.getMediaPlayer().isPlaying())
			{
				//player.previous();
				//mTimer.cancel();
				//progressbar.setProgress(0);
				//runProgress(player.getMediaPlayer(), progressbar);
			}
			else
			{
				//player.songIndex--;
				//home_player_title.setText(getSongName(Utils.list.get(player.songIndex)));
				//mTimer.cancel();
				//progressbar.setProgress(0);
				//isPlayerFirstPlay = true;
				//MediaPlayer pl = player.getMediaPlayer();
				//pl.stop();
				//pl.reset();
			}*/
			Intent inte2 = new Intent(context, notificationService.class);
			inte2.putExtra("work", ""+AppConstant.PREV);
			context.startService(inte2);
			//creatNotication(context);
			break;

		default:
			break;
		}
	}
	
	
	
private void creatNotication(Context cont)
	
	{
	String ns = Context.NOTIFICATION_SERVICE;
	NotificationManager mNotificationManager = (NotificationManager)cont. getSystemService(ns);
	int icon = R.drawable.ic_launcher;
	long when = System.currentTimeMillis();
	
	NotificationCompat.Builder builder = new NotificationCompat.Builder(cont)
    .setSmallIcon(R.drawable.ic_launcher)
    .setContentTitle("My notification")
    .setContentText("Hello World!");
		
		//Notification notification = new Notification(icon, "", when);
		
		// contentView.setImageViewResource(R.id.notification_image,
		// R.drawable.notification_image);
		

	builder.setContent(contentView);
	builder.setSmallIcon(R.drawable.ic_launcher);
	builder.setAutoCancel(true);
	builder.setDefaults(0);
	builder.setSound(null);
	
	
	builder.setLights(Color.WHITE, 500, 500); //Do not clear the notification
	/*notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
	notification.defaults |= Notification.DEFAULT_VIBRATE; //Vibration
	notification.defaults |= Notification.DEFAULT_SOUND; // Sound
*/Notification not = builder.build();
not.flags |= Notification.FLAG_ONGOING_EVENT;
	mNotificationManager.notify(NOTIFICATION_ID, builder.build());
		
	}

private void initail(Context cont)
{
	Intent switchIntentNEXT = new Intent("com.player.MediaPlayer.next");
	switchIntentNEXT.putExtra("work",""+ AppConstant.NEXT);
	PendingIntent pendingSwitchIntentNEXT = PendingIntent.getBroadcast(cont, 100,
			switchIntentNEXT, PendingIntent.FLAG_UPDATE_CURRENT);

	Intent switchIntentCROSS = new Intent("com.player.MediaPlayer.cross");
	switchIntentCROSS.putExtra("work", ""+AppConstant.CROSS);
	PendingIntent pendingSwitchIntentCROSS = PendingIntent.getBroadcast(
			cont, 100, switchIntentCROSS, PendingIntent.FLAG_UPDATE_CURRENT);

	Intent switchIntentPLAY = new Intent("com.player.MediaPlayer.play");
	switchIntentPLAY.putExtra("work", ""+AppConstant.PLAY_STATE);
	PendingIntent pendingSwitchIntentPLAY = PendingIntent.getBroadcast(
			cont, 100, switchIntentPLAY, PendingIntent.FLAG_UPDATE_CURRENT);

	Intent switchIntentPREV = new Intent("com.player.MediaPlayer.prev");
	switchIntentPREV.putExtra("work", ""+AppConstant.PREV);
	PendingIntent pendingSwitchIntentPREV = PendingIntent.getBroadcast(cont, 100,
			switchIntentPREV, PendingIntent.FLAG_UPDATE_CURRENT);
	
	
	
	contentView = new RemoteViews(cont.getPackageName(),
			R.layout.notification_custom_view);
	
	contentView.setOnClickPendingIntent(R.id.notification_cross,
			pendingSwitchIntentCROSS);
	contentView.setOnClickPendingIntent(R.id.notification_prev,
			pendingSwitchIntentPREV);
	contentView.setOnClickPendingIntent(R.id.notification_play,
			pendingSwitchIntentPLAY);
	contentView.setOnClickPendingIntent(R.id.notification_next,
			pendingSwitchIntentNEXT);}

}
