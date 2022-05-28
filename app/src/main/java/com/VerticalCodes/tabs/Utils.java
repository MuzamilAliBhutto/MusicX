package com.VerticalCodes.tabs;

import android.media.MediaMetadataRetriever;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.text.DecimalFormat;
import java.util.Locale;

public class Utils {
	public static String formatSize(long size) {
		String hrSize = null;

		double b = size;
		double k = size / 1024.0;
		double m = ((size / 1024.0) / 1024.0);
		double g = (((size / 1024.0) / 1024.0) / 1024.0);
		double t = ((((size / 1024.0) / 1024.0) / 1024.0) / 1024.0);

		DecimalFormat dec = new DecimalFormat("0.00");

		if (t > 1) {
			hrSize = dec.format(t).concat(" TB");
		} else if (g > 1) {
			hrSize = dec.format(g).concat(" GB");
		} else if (m > 1) {
			hrSize = dec.format(m).concat(" MB");
		} else if (k > 1) {
			hrSize = dec.format(k).concat(" KB");
		} else {
			hrSize = dec.format(b).concat(" Bytes");
		}
		return hrSize;
	}
	public static String formatDuration(final long duration) {
		return String.format(Locale.getDefault(), "%02d:%02d",
		TimeUnit.MILLISECONDS.toMinutes(duration),
		TimeUnit.MILLISECONDS.toSeconds(duration) -
		TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
	}
	public static byte[] getAlbumArt(String uri){
		MediaMetadataRetriever retriever=new MediaMetadataRetriever();
		try {
		retriever.setDataSource(uri);
		}
		catch (Exception e)
				{
					
				}
		byte[] art=retriever.getEmbeddedPicture();
		retriever.release();
		return art;
		
		}
		public static int getRandom(int i){
			Random random=new Random();
			return random.nextInt(i+1);
			}

}