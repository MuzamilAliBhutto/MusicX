package com.VerticalCodes.tabs;

import android.app.PendingIntent;
import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.content.Intent;
import android.app.Service;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import androidx.core.app.NotificationManagerCompat;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat;
import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import java.util.ArrayList;

public class MusicService extends Service {
	public Binder binder = new MyBinder();
	MediaPlayer mediaPlayer;
	int position = -1;
	ArrayList<Song> songArrayList = new ArrayList<>();
	Uri uri;
	ActionPlaying actionPlaying;
	MediaSessionCompat mediaSessionCompat;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("MusicService", "onCreate");
		mediaSessionCompat = new MediaSessionCompat(this, "My Audio");
	}

	@Override
	public int onStartCommand(Intent arg0, int arg1, int arg2) {
		String actionName = arg0.getStringExtra("actionName");

		int myposi = arg0.getIntExtra("pos", -1);
		if (myposi != -1) {
			playMedia(myposi);
		}
		if (actionName != null) {
			switch (actionName) {
			case "playPause":
				if (actionPlaying != null) {
					actionPlaying.playPauseBtnClicked();
				}
				break;
			case "previous":
				if (actionPlaying != null) {
					actionPlaying.previousBtnClicked();
				}
				break;
			case "next":
				if (actionPlaying != null) {
					actionPlaying.nextBtnClicked();
				}
				break;
			case "close":
				stopSelf();
				mediaPlayer.stop();
				break;
			}
		}
		return START_STICKY;

	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return binder;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("MusicService", "onDestroy");
	}

	public class MyBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}

	boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	void pause() {
		mediaPlayer.pause();
	}

	void start() {
		mediaPlayer.start();
	}

	void stop() {
		mediaPlayer.stop();
	}

	void release() {
		mediaPlayer.release();
	}

	void seekTo(int position) {
		mediaPlayer.seekTo(position);
	}

	int getDuration() {
		return mediaPlayer.getDuration();
	}

	int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	void createMediaPlayer(int position) {
		uri = Uri.parse(songArrayList.get(position).getSongPath());
		mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
	}

	void playMedia(int startPosition) {
		position = startPosition;
		songArrayList = PlayerActivity.songArrayList;
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			if (songArrayList != null) {
				createMediaPlayer(position);
				mediaPlayer.start();
			}
		} else {
			createMediaPlayer(position);
			mediaPlayer.start();
		}
	}

	void onCompletation() {
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (actionPlaying != null) {
					actionPlaying.nextBtnClicked();
				}
			}
		});

	}

	void callbacks(ActionPlaying actionPlaying) {
		this.actionPlaying = actionPlaying;
	}

	void showNotification(int playPauseBtn, int p) {
		Bitmap thumb = null;
		byte[] art = Utils.getAlbumArt(songArrayList.get(p).getSongPath());
		if (art != null) {
			thumb = BitmapFactory.decodeByteArray(art, 0, art.length);

		} else {
			thumb = BitmapFactory.decodeResource(getResources(), R.drawable.gally);
		}

		Intent openIntent = new Intent(this, PlayerActivity.class);
		PendingIntent pendingOpen = PendingIntent.getActivity(this, 0, openIntent, 0);

		Intent previousIntent = new Intent(this, NotificationReceiver.class);
		previousIntent.setAction(App.ACTION_PREVIOUS);
		PendingIntent previousPending = PendingIntent.getBroadcast(this, 0, previousIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent pauseIntent = new Intent(this, NotificationReceiver.class);
		pauseIntent.setAction(App.ACTION_PLAY);
		PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent nextIntent = new Intent(this, NotificationReceiver.class);
		nextIntent.setAction(App.ACTION_NEXT);
		PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Intent closeIntent = new Intent(this, NotificationReceiver.class);
		closeIntent.setAction(App.ACTION_CLOSE);
		PendingIntent closePending = PendingIntent.getBroadcast(this, 0, closeIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(this, App.CHANNEL_2)
				.setSmallIcon(playPauseBtn).setLargeIcon(thumb).setContentTitle(songArrayList.get(p).getSongTitle())
				.setContentText(songArrayList.get(p).getArtistName())
				.addAction(R.drawable.previous, "previous", previousPending)
				.addAction(playPauseBtn, "pause", pausePending).addAction(R.drawable.next, "next", nextPending)
				.setPriority(NotificationCompat.PRIORITY_HIGH).addAction(R.drawable.close, "close", closePending)
				.setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1, 2))
				.setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setOnlyAlertOnce(true);
		byte[] arts = Utils.getAlbumArt(songArrayList.get(p).getSongPath());

		Bitmap bitmap = null;
		if (arts != null) {
			bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
			Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
				@Override
				public void onGenerated(Palette palette) {
					Palette.Swatch swatch = palette.getDominantSwatch();
					if (swatch != null) {
						notification.setColor(swatch.getTitleTextColor());

					}
				}
			});
		} else {
			bitmap = BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.gally);
			Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
				@Override
				public void onGenerated(Palette palette) {
					Palette.Swatch swatch = palette.getDominantSwatch();
					if (swatch != null) {
						notification.setColor(swatch.getTitleTextColor());

					}
				}
			});
		}
		//.setMediaSession(mSession.getSessionToken()));
		startForeground(3, notification.build());

	}
}