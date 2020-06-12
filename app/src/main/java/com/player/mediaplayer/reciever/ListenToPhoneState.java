package com.player.mediaplayer.reciever;

import com.player.mediaplayer.constant.AppConstant;
import com.player.mediaplayer.constant.AudioPlayer;
import com.player.mediaplayer.constant.Utils;
import com.player.mediaplayer.service.notificationService;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class ListenToPhoneState extends PhoneStateListener {

	private boolean pausedForPhoneCall = false;
	private boolean fromRinging = false;
	private boolean isFromThisListener = false;
	// private UIManager uiManager;
	private Context context = null;
	private MediaPlayer player = null;

	public ListenToPhoneState(Context cont) {
		// uiManager = manager;
		this.context = cont;
		player = AudioPlayer.getInstnace(cont).getMediaPlayer();
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			Log.d("DEBUG", "IDLE");
			if (Utils.list == null)
				return;
			if (!player.isPlaying()) {
				if ((pausedForPhoneCall || fromRinging) && (isFromThisListener || fromRinging)) {
					Intent inte1 = new Intent(context,
							notificationService.class);
					inte1.putExtra("work", "" + AppConstant.PLAY_STATE);
					context.startService(inte1);
					pausedForPhoneCall = false;
					fromRinging = false;
					isFromThisListener = false;
				}
			}

			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			Log.d("DEBUG", "OFFHOOK");
			if (Utils.list == null)
				return;
			isFromThisListener = false;
			//if (player.isPlaying()) {
			if(!fromRinging)
			{
				if (player.isPlaying()) {
				Intent inte2 = new Intent(context, notificationService.class);
				inte2.putExtra("work", "" + AppConstant.PLAY_STATE);
				context.startService(inte2);
				pausedForPhoneCall = true;
				fromRinging = false;
				isFromThisListener = true;
				}
			}
			//}

			break;
		case TelephonyManager.CALL_STATE_RINGING:
			Log.d("DEBUG", "RINGING");
			if (Utils.list == null)
				return;
			isFromThisListener = false;
			if (player.isPlaying()) {
				Intent inte3 = new Intent(context, notificationService.class);
				inte3.putExtra("work", "" + AppConstant.PLAY_STATE);
				context.startService(inte3);
				pausedForPhoneCall = true;
				fromRinging = true;
				isFromThisListener = true;
			}
			break;
		}
	}


}