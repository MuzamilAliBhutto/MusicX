package com.VerticalCodes.tabs;
import android.content.Intent;
import android.content.Context;

import android.content.BroadcastReceiver;

public class NotificationReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		String actionName=intent.getAction();
		if(actionName != null){
			switch (actionName){
					case App.ACTION_PLAY:
					Intent i=new Intent(context,MusicService.class);
					i.putExtra("actionName","playPause");
					context.startService(i);
					break ;
					case App.ACTION_PREVIOUS:
					Intent ii=new Intent(context,MusicService.class);
					ii.putExtra("actionName","previous");
					context.startService(ii);
					break ;
					case App.ACTION_NEXT:
					Intent iii=new Intent(context,MusicService.class);
					iii.putExtra("actionName","next");
					context.startService(iii);
					break ;
					case App.ACTION_CLOSE:
					Intent iiii=new Intent(context,MusicService.class);
					iiii.putExtra("actionName","close");
					context.startService(iiii);
					break ;
					}
			}
	}
	
}