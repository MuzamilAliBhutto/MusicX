package com.VerticalCodes.tabs;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application{
	public static final String CHANNEL_1="Channel1";
	public static final String CHANNEL_2="Channel2";
	public static final String ACTION_PLAY="action_play";
	public static final String ACTION_PREVIOUS="action_previous";
	public static final String ACTION_NEXT="action_next";
	public static final String ACTION_CLOSE="action_close";
	@Override
	public void onCreate() {
		super.onCreate();
		create();
	}
	private void create(){
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
			NotificationChannel channel1=new NotificationChannel(CHANNEL_1,"Channel1",NotificationManager.IMPORTANCE_HIGH);
			channel1.setDescription("This channel is for showing music status.");
			NotificationChannel channel2=new NotificationChannel(CHANNEL_2,"Channel2",NotificationManager.IMPORTANCE_HIGH);
			channel1.setDescription("This is channel2.");
			NotificationManager manager=getSystemService(NotificationManager.class);
			manager.createNotificationChannel(channel1);
			manager.createNotificationChannel(channel2);
			
			}
		}
}