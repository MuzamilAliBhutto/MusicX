package com.VerticalCodes.tabs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class albumAdapter extends RecyclerView.Adapter<albumAdapter.MyHolder>{
	Context context;
	
	ArrayList<Song> songArrayList;
	@Override
	public MyHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v=LayoutInflater.from(context).inflate(R.layout.album_item,arg0,false);
	    return new MyHolder(v);
	}

	@Override
	public void onBindViewHolder(MyHolder myViewHolder, int position) {
		Song song=songArrayList.get(position);
		myViewHolder.album_name.setText(song.getAlbumName());
		myViewHolder.album_img.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
			Intent i=new Intent(context,AlbumDetailsActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("song",song);
			context.startActivity(i);
			
			}
			});
			byte[] bitmap=Utils.getAlbumArt(song.getSongPath());
			if (bitmap != null){
			Glide.with(context).asBitmap().load(bitmap).into(myViewHolder.album_img);
			}
			else {
				Glide.with(context).load(R.drawable.gally).into(myViewHolder.album_img);
				}
			
			
			
	}

	@Override
	public int getItemCount() {
	    return songArrayList.size();
	}
	public class MyHolder extends RecyclerView.ViewHolder{
		public TextView album_name;
		public ImageView album_img;
		public MyHolder(View v){
			super(v);
			album_name=v.findViewById(R.id.album_name);
			album_img=v.findViewById(R.id.album_img);
		}
	}
	public albumAdapter(Context context,ArrayList<Song> songArrayList){
		this.songArrayList=songArrayList;
		this.context=context;
		}
	
}