package com.VerticalCodes.tabs;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class layout1 extends Fragment {
	
	public layout1(Context context){
		this.context=context;
		}
		Context context;
	public RecyclerView recyclerview;
	public static SongAdapter songAdapter;
	public static ArrayList<Song> songArrayList=new ArrayList<>();
	static ArrayList<Song> albumSongArrayList=new ArrayList<>();
	Cursor cursor;
	ContentResolver contentResolver;
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View view=arg0.inflate(R.layout.layout_1,arg1,false);
		recyclerview=view.findViewById(R.id.recyclerview1);
		ArrayList<String> duplicate=new ArrayList<>();
		contentResolver=context.getContentResolver();
		Uri uri=MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		String[] projection={MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ARTIST_ID,
			MediaStore.Audio.Media.DURATION,
			MediaStore.MediaColumns.SIZE,
			MediaStore.Audio.Media.YEAR,
			MediaStore.Audio.Media.ALBUM,
		MediaStore.Audio.Media.TRACK};
		String order=MediaStore.Audio.Media.DISPLAY_NAME;
		Cursor cursor=contentResolver.query(uri,projection,null,null,order);
		if (cursor != null){
			if(cursor.moveToFirst()){
				do{
					
					String title=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
					String path=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
					String artist=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
					int artistId=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
					long duration=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
					long size=cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE));
					int year=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.YEAR));
					String album=cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
					
					int track=cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
					Song song=new Song();
					song.setSongTitle(title);
					song.setSongPath(path);
					song.setSongSize(size);
					song.setSongDuration(duration);
					song.setAlbumName(album);
					song.setArtistId(artistId);
					song.setArtistName(artist);
					song.setTrackNumber(track);
					
					song.setYear(year);
					songArrayList.add(song);
					if (!duplicate.contains(album)){
						albumSongArrayList.add(song);
						duplicate.add(album);
						}
				}
				while (cursor.moveToNext());
				
			}
		}
		cursor.close();
		songAdapter=new SongAdapter(context,songArrayList);
		recyclerview.setAdapter(songAdapter);
		recyclerview.setLayoutManager(new LinearLayoutManager(context));
		
		
		
		return view;
		
		
	}
}