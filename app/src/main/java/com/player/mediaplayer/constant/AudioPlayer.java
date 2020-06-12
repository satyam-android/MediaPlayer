package com.player.mediaplayer.constant;

import java.io.FileNotFoundException;
import java.util.List;

import com.player.mediaplayer.homeActivity;
import com.player.mediaplayer.database.SongsTable;
import com.player.mediaplayer.service.notificationService;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class AudioPlayer {
	private MediaPlayer player = null;
	private static AudioPlayer audioPlayer = null;
	// private List<String> Utils.list = null;
	public int songIndex=0;
	public String fromwhere="";
	public String song = "";
	private Activity activity = null;
	private SharedPreferences pref = null;
	public boolean isAllSongLoop = false;
	public boolean isOneSongLoop = false;
	private Context context = null;
	private SongsTable db = null;

	private AudioPlayer(Context cont) {
		player = new MediaPlayer();
		// Utils.list = Utils.list;
		pref = PreferenceManager.getDefaultSharedPreferences(cont);
		context = cont;
		db = new SongsTable(cont);
	}

	public static AudioPlayer getInstnace(Context con) {

		if (audioPlayer == null) {
			audioPlayer = new AudioPlayer(con);
		}
		return audioPlayer;
	}

	public void play(String path) {

		try {
			if (player.isPlaying()) {
				player.stop();
				player.reset();
			}
			songIndex = Utils.list.indexOf(path);
			player.setDataSource(path);// Write your location here
			player.prepare();
			player.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void playListIndex(final notificationService ser, int index) {

		try {
			songIndex = index;
			if (player.isPlaying()) {
				player.stop();
				player.reset();
			}
			player.setDataSource(Utils.list.get(songIndex).getPath());// Write
				song = Utils.list.get(songIndex).getPath();														// your
																		// location
			Log.i("inside playListIndex audio player	", songIndex + ""); // here
			player.prepare();
			player.start();
			db.updateRecentPlayed(Utils.list.get(songIndex).getPath(),
					System.currentTimeMillis());
			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					try {
						if (isOneSongLoop) {
							player.reset();
							/* load the new source */
							player.setDataSource(Utils.list.get(songIndex)
									.getPath());
							/* Prepare the mediaplayer */
							player.prepare();
							/* start */
							player.start();
							db.updateRecentPlayed(Utils.list.get(songIndex)
									.getPath(), System.currentTimeMillis());
							nextSong sa = ser;
							sa.onNextSong(songIndex);

						} else {
							songIndex++;
							if (songIndex < Utils.list.size()) {
								player.reset();
								/* load the new source */
								player.setDataSource(Utils.list.get(songIndex)
										.getPath());
								/* Prepare the mediaplayer */
								player.prepare();
								/* start */
								player.start();
								db.updateRecentPlayed(Utils.list.get(songIndex)
										.getPath(), System.currentTimeMillis());
								nextSong sa = ser;
								sa.onNextSong(songIndex);

							} else {
								/* release mediaplayer */
								// player.release();

								songIndex--;
								if (isAllSongLoop) {
									songIndex = 0;
									player.reset();
									/* load the new source */
									player.setDataSource(Utils.list.get(
											songIndex).getPath());
									/* Prepare the mediaplayer */
									player.prepare();
									/* start */
									player.start();
									db.updateRecentPlayed(
											Utils.list.get(songIndex).getPath(),
											System.currentTimeMillis());
									nextSong sa = ser;
									sa.onNextSong(songIndex);
								} else {
									String ns = Context.NOTIFICATION_SERVICE;
									NotificationManager mNotificationManager = (NotificationManager) context
											.getSystemService(ns);
									mNotificationManager.cancel(1);
									context.stopService(new Intent(context,
											notificationService.class));
									nextSong sa = ser;
									sa.afterSongComplete();
								}
							}
						}
						song = Utils.list.get(songIndex).getPath();
					} catch (FileNotFoundException e) {
						db.delete(Utils.list.get(songIndex).getPath());
						//Utils.toastshow(context, "Song not found");
					} catch (Exception e) {

					}
				}

			});
		} catch (ArrayIndexOutOfBoundsException e) {
			// e.printStackTrace();
			/*
			 * if(songIndex < 0) songIndex = 0; else if(songIndex
			 * >Utils.list.size()) songIndex = Utils.list.size();
			 */
		} catch (FileNotFoundException e) {
			db.delete(Utils.list.get(songIndex).getPath());
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void playListIndex(int index, final TextView text, Activity act) {
		activity = act;

		try {
			songIndex = index;
			if (player.isPlaying()) {
				player.stop();
				player.reset();
			}

			player.setDataSource(Utils.list.get(index).getPath());// Write your
																	// location
			// here
			player.prepare();
			player.start();
			text.setText(getSongName(Utils.list.get(songIndex).getPath()));
			player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					try {
						songIndex++;
						if (songIndex < Utils.list.size()) {
							player.reset();
							/* load the new source */
							player.setDataSource(Utils.list.get(songIndex)
									.getPath());
							/* Prepare the mediaplayer */
							player.prepare();
							/* start */
							player.start();

						} else {
							/* release mediaplayer */
							// player.release();
						}
					}

					catch (Exception e) {

					}
				}

			});
		} catch (ArrayIndexOutOfBoundsException e) {
			// e.printStackTrace();
			if (songIndex < 0)
				songIndex = 0;
			else if (songIndex > Utils.list.size())
				songIndex = Utils.list.size();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void pause() {
		player.pause();
	}

	public void start() {
		player.start();
	}

	public void stop() {
		player.stop();
	}

	public void next(notificationService ser) {
		Log.i("inside next audio player	", songIndex + "");
		songIndex++;
		// if (player.isPlaying()) {
		player.stop();
		player.reset();
		// }
		playListIndex(ser, songIndex);
	}

	public void next(TextView text, Activity activit) {
		songIndex++;
		// if (player.isPlaying()) {
		player.stop();
		player.reset();
		// }
		playListIndex(songIndex, text, activit);
	}

	public void previous(notificationService ser) {
		songIndex--;
		// if (player.isPlaying()) {
		player.stop();
		player.reset();
		// }
		playListIndex(ser, songIndex);
	}

	public void repeatOne() {
		if (player.isPlaying())
			player.setLooping(true);

	}

	public MediaPlayer getMediaPlayer() {
		return player;
	}

	private String getSongName(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
	}

}
