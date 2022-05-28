package com.VerticalCodes.tabs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.palette.graphics.Palette;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.lang.Thread;
import java.lang.Runnable;

public class PlayerActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {
	RelativeLayout relativeLayout;
	static ArrayList<Song> songArrayList = new ArrayList<>();
	int position = -1;
	//static MediaPlayer mediaPlayer;
	TextView title, artist, current_duration, total_duration;
	ImageView shuffle, previous, next, repeat, cover_art, gradient_img,back,menu;
	SeekBar seekBar;
	FloatingActionButton play_pause;
	static Uri uri;
	Handler handler = new Handler();
	Thread btnPlayThread, btnNextThread, btnPreviousThread, complitationThread;
	boolean isShuffleBtnClicked = false, isRepeatBtnClicked = false;
	MusicService musicService;
	MediaSessionCompat mSession;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_player);
		init();
		Intent i = new Intent(PlayerActivity.this, MusicService.class);
		bindService(i, this, Context.BIND_AUTO_CREATE);
		mSession = new MediaSessionCompat(this, "MyAudio");
		getIntentMethord();

		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
				// TODO: Implement this method
				if (musicService != null && p3) {
					musicService.seekTo(p2 * 1000);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar p1) {
				// TODO: Implement this method
			}

			@Override
			public void onStopTrackingTouch(SeekBar p1) {
				// TODO: Implement this method
			}
		});
		PlayerActivity.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (musicService != null) {
					current_duration.setText(Utils.formatDuration(musicService.getCurrentPosition()));
					seekBar.setProgress(musicService.getCurrentPosition() / 1000);
				}
				handler.postDelayed(this, 1000);
			}
		});
		shuffle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isShuffleBtnClicked) {
					isShuffleBtnClicked = false;
					shuffle.setImageResource(R.drawable.shuffle_off);

				} else {
					isShuffleBtnClicked = true;
					shuffle.setImageResource(R.drawable.shuffle_on);
				}
			}

		});
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
		menu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
		repeat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isRepeatBtnClicked) {
					isRepeatBtnClicked = false;
					repeat.setImageResource(R.drawable.repeat_off);

				} else {
					isRepeatBtnClicked = true;
					repeat.setImageResource(R.drawable.repeat_on);
				}
			}

		});

		playBtnListener();
		previousBtnListener();
		nextBtnListener();

	}

	@Override
	protected void onPause() {
		super.onPause();
		unbindService(this);

	}

	private void init() {
		menu=findViewById(R.id.btn_menu);
		back=findViewById(R.id.btn_back);
		title = findViewById(R.id.song_name);
		artist = findViewById(R.id.song_artist);
		current_duration = findViewById(R.id.current_duration);
		total_duration = findViewById(R.id.total_duration);
		seekBar = findViewById(R.id.seekbar);
		shuffle = findViewById(R.id.shuffle);
		previous = findViewById(R.id.previous);
		play_pause = findViewById(R.id.play);
		next = findViewById(R.id.next);
		repeat = findViewById(R.id.repeat);
		cover_art = findViewById(R.id.player_album_art);
		relativeLayout = findViewById(R.id.mContainar);
		gradient_img = findViewById(R.id.imageView_for_color);
	}

	private void getIntentMethord() {
		position = getIntent().getIntExtra("position", -1);
		String sender = getIntent().getStringExtra("sender");
		if (sender != null && sender.equals("albumDetails")) {
			songArrayList = AlbumDetailsActivity.albumSongArrayList;
		} else {
			songArrayList = layout1.songArrayList;
		}
		if (songArrayList != null) {
			play_pause.setImageResource(R.drawable.pause);
			uri = Uri.parse(songArrayList.get(position).getSongPath());
		}

		title.setText(songArrayList.get(position).getSongTitle());
		artist.setText(songArrayList.get(position).getArtistName());

		getMatadata(uri);
		Intent i = new Intent(PlayerActivity.this, MusicService.class);
		i.putExtra("pos", position);
		startService(i);

	}

	private void getMatadata(Uri urii) {
		byte[] art = Utils.getAlbumArt(urii.toString());
		Bitmap bitmap;
		if (art != null) {
			bitmap = BitmapFactory.decodeByteArray(art, 0, art.length);
			Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
				@Override
				public void onGenerated(Palette palette) {
					Palette.Swatch swatch = palette.getDominantSwatch();
					if (swatch != null) {
						gradient_img.setBackgroundResource(R.drawable.gradient_bg);
						relativeLayout.setBackgroundResource(R.drawable.player_bg);
						GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
								new int[] { swatch.getRgb(), 0x00000000 });
						gradient_img.setBackground(gradient);
						GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
								new int[] { swatch.getRgb(), swatch.getRgb() });
						relativeLayout.setBackground(gradient1);
						title.setTextColor(swatch.getTitleTextColor());
						artist.setTextColor(swatch.getBodyTextColor());
					} else {

						gradient_img.setBackgroundResource(R.drawable.gradient_bg);
						relativeLayout.setBackgroundResource(R.drawable.player_bg);
						GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
								new int[] { 0xff000000, 0x00000000 });
						gradient_img.setBackground(gradient);
						GradientDrawable gradient1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
								new int[] { 0xff000000, 0xff000000 });
						relativeLayout.setBackground(gradient1);
						title.setTextColor(Color.WHITE);
						artist.setTextColor(Color.DKGRAY);
					}
				}
			});
			Glide.with(getApplicationContext()).asBitmap().load(art).into(cover_art);
		} else {
			gradient_img.setBackgroundResource(R.drawable.gradient_bg);
			relativeLayout.setBackgroundResource(R.drawable.player_bg);
			title.setTextColor(Color.WHITE);
			artist.setTextColor(Color.DKGRAY);
			Glide.with(getApplicationContext()).asBitmap().load(R.drawable.gally).into(cover_art);
		}
	}

	public void playPauseBtnClicked() {
		if (musicService.isPlaying()) {
			
			play_pause.setImageResource(R.drawable.play);
			musicService.pause();
			seekBar.setMax(musicService.getDuration() / 1000);
			PlayerActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (musicService != null) {
						seekBar.setProgress(musicService.getCurrentPosition() / 1000);
					}
					handler.postDelayed(this, 1000);
				}
			});
			musicService.showNotification(R.drawable.play,position);
		} else {
			play_pause.setImageResource(R.drawable.pause);
			
			musicService.start();
			seekBar.setMax(musicService.getDuration() / 1000);
			PlayerActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (musicService != null) {
						seekBar.setProgress(musicService.getCurrentPosition() / 1000);
					}
					handler.postDelayed(this, 1000);
				}
			});
musicService.showNotification(R.drawable.pause,position);
		}

	}

	public void previousBtnClicked() {
		if (musicService.isPlaying()) {
			musicService.stop();
			musicService.release();
			if (isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = Utils.getRandom(songArrayList.size() - 1);
			} else if (!isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = ((position - 1) < 0 ? (songArrayList.size() - 1) : (position - 1));

			}

			uri = Uri.parse(songArrayList.get(position).getSongPath());
			musicService.createMediaPlayer(position);
			musicService.start();
			
			play_pause.setImageResource(R.drawable.pause);
			seekBar.setMax(musicService.getDuration() / 1000);
			total_duration.setText(Utils.formatDuration(musicService.getDuration()));
			title.setText(songArrayList.get(position).getSongTitle());
			artist.setText(songArrayList.get(position).getArtistName());
			getMatadata(uri);
			PlayerActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (musicService != null) {
						current_duration.setText(Utils.formatDuration(musicService.getCurrentPosition()));
						seekBar.setProgress(musicService.getCurrentPosition() / 1000);
					}
					handler.postDelayed(this, 1000);
				}

			});
			musicService.onCompletation();
musicService.showNotification(R.drawable.pause,position);
		} else {

			if (isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = Utils.getRandom(songArrayList.size() - 1);
			} else if (!isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = ((position - 1) < 0 ? (songArrayList.size() - 1) : (position - 1));

			}
			uri = Uri.parse(songArrayList.get(position).getSongPath());
			musicService.createMediaPlayer(position);
			
			play_pause.setImageResource(R.drawable.pause);
			musicService.start();
			seekBar.setMax(musicService.getDuration() / 1000);
			total_duration.setText(Utils.formatDuration(musicService.getDuration()));
			title.setText(songArrayList.get(position).getSongTitle());
			artist.setText(songArrayList.get(position).getArtistName());
			getMatadata(uri);
			PlayerActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (musicService != null) {
						current_duration.setText(Utils.formatDuration(musicService.getCurrentPosition()));
						seekBar.setProgress(musicService.getCurrentPosition() / 1000);
					}
					handler.postDelayed(this, 1000);
				}
			});
			musicService.onCompletation();
			musicService.showNotification(R.drawable.pause,position);
		}
	}

	public void nextBtnClicked() {

		if (musicService.isPlaying()) {
			musicService.stop();
			musicService.release();
			if (isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = Utils.getRandom(songArrayList.size() - 1);
			} else if (!isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = ((position + 1) % songArrayList.size());
			}
			uri = Uri.parse(songArrayList.get(position).getSongPath());
			musicService.createMediaPlayer(position);
			musicService.start();
			
			play_pause.setImageResource(R.drawable.pause);
			seekBar.setMax(musicService.getDuration() / 1000);
			total_duration.setText(Utils.formatDuration(musicService.getDuration()));
			title.setText(songArrayList.get(position).getSongTitle());
			artist.setText(songArrayList.get(position).getArtistName());
			getMatadata(uri);
			PlayerActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (musicService != null) {
						current_duration.setText(Utils.formatDuration(musicService.getCurrentPosition()));
						seekBar.setProgress(musicService.getCurrentPosition() / 1000);
					}
					handler.postDelayed(this, 1000);
				}

			});
			musicService.onCompletation();
musicService.showNotification(R.drawable.pause,position);
		} else {

			if (isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = Utils.getRandom(songArrayList.size() - 1);
			} else if (!isShuffleBtnClicked && !isRepeatBtnClicked) {
				position = ((position + 1) % songArrayList.size());
			}
			uri = Uri.parse(songArrayList.get(position).getSongPath());
			musicService.createMediaPlayer(position);
	
			play_pause.setImageResource(R.drawable.pause);
			musicService.start();
			seekBar.setMax(musicService.getDuration() / 1000);
			total_duration.setText(Utils.formatDuration(musicService.getDuration()));
			title.setText(songArrayList.get(position).getSongTitle());
			artist.setText(songArrayList.get(position).getArtistName());
			getMatadata(uri);
			PlayerActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (musicService != null) {
						current_duration.setText(Utils.formatDuration(musicService.getCurrentPosition()));
						seekBar.setProgress(musicService.getCurrentPosition() / 1000);
					}
					handler.postDelayed(this, 1000);
				}

			});
			musicService.showNotification(R.drawable.pause,position);
			musicService.onCompletation();
		}
	}

	private void playBtnListener() {
		btnPlayThread = new Thread() {
			@Override
			public void run() {
				play_pause.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						playPauseBtnClicked();
					}

				});
			}
		};
		btnPlayThread.start();
	}

	private void previousBtnListener() {
		btnPreviousThread = new Thread() {
			@Override
			public void run() {
				previous.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						previousBtnClicked();
					}

				});
			}
		};
		btnPreviousThread.start();
	}

	private void nextBtnListener() {
		btnNextThread = new Thread() {
			@Override
			public void run() {
				next.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						nextBtnClicked();
					}

				});
			}
		};
		btnNextThread.start();
	}

	private void complitationListener() {
		/**complitationThread =new Thread(){
			@Override
			public void run(){
				musicService.onCompletation();
				
			}
			};
			complitationThread.start();*/
	}

	@Override
	public void onServiceConnected(ComponentName p1, IBinder binderr) {
		// TODO: Implement this method
		MusicService.MyBinder binder = (MusicService.MyBinder) binderr;
		musicService = binder.getService();
		musicService.callbacks(this);
		Toast.makeText(getApplicationContext(), "On Service Connected", Toast.LENGTH_SHORT).show();
		seekBar.setMax(musicService.getDuration() / 1000);
		total_duration.setText(Utils.formatDuration(musicService.getDuration()));
		musicService.showNotification(R.drawable.pause,position);
		if (musicService != null) {
			musicService.stop();
			musicService.release();
			musicService.createMediaPlayer(position);
			musicService.start();
		} else {
			musicService.createMediaPlayer(position);
			musicService.start();

		}

		musicService.onCompletation();

	}

	@Override
	public void onServiceDisconnected(ComponentName p1) {
		// TODO: Implement this method
		musicService = null;
	}
@Override
public void onBackPressed()
{
	// TODO: Implement this method
	
	super.onBackPressed();
}
}