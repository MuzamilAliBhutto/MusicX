package com.VerticalCodes.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import com.VerticalCodes.tabs.SongAdapter.MyViewHolder;
import com.bumptech.glide.Glide;
import java.util.List;
import java.util.ArrayList;

public class albumDetailsAdapter extends RecyclerView.Adapter<albumDetailsAdapter.MyViewHolder>{
	Context context;
	ArrayList<Song> songArrayList;
	ArrayList<Song> songListFull;
	public class MyViewHolder extends RecyclerView.ViewHolder{
		public TextView title,duration,size;
		public ImageView albumArt;
		CardView cardView;
		public MyViewHolder(View v){
			super(v);
			title=v.findViewById(R.id.songtitle);
			size=v.findViewById(R.id.songsize);
			duration=v.findViewById(R.id.songduration);
			albumArt=v.findViewById(R.id.imageview1);
			cardView=v.findViewById(R.id.cardview1);
		
			
			
			}
		
		
		}
		public albumDetailsAdapter(Context context,ArrayList<Song> songArrayList){
			this.context=context;
			this.songArrayList=songArrayList;
			songListFull=new ArrayList<>(songArrayList);
			}
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v=LayoutInflater.from(context).inflate(R.layout.song_item,arg0,false);
	    return new MyViewHolder(v);
	}

	@Override
	public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
		Song song=songArrayList.get(position);
		myViewHolder.title.setText(song.getSongTitle());
		Utils utils=new Utils();
		myViewHolder.size.setText(Utils.formatSize(song.getSongSize()));
		myViewHolder.duration.setText(Utils.formatDuration(song.getSongDuration()));
			byte[] art=Utils.getAlbumArt(song.getSongPath());
			
			Bitmap bitmap=null;
			if (art != null) {
				bitmap=BitmapFactory.decodeByteArray(art,0,art.length);
				Palette.from(bitmap).generate(new Palette.PaletteAsyncListener(){
					@Override
					public void onGenerated(Palette arg0) {
						Palette.Swatch swatch=arg0.getDominantSwatch();
						if(swatch != null){
							myViewHolder.cardView.setBackgroundResource(R.drawable.player_bg);
							GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),swatch.getRgb()});
							myViewHolder.cardView.setBackground(gradientDrawable);
							myViewHolder.title.setTextColor(swatch.getTitleTextColor());
							myViewHolder.size.setTextColor(swatch.getBodyTextColor());
							myViewHolder.duration.setTextColor(swatch.getBodyTextColor());
						}
						else{
							myViewHolder.cardView.setBackgroundResource(R.drawable.player_bg);
							GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0xff000000});
							myViewHolder.cardView.setBackground(gradientDrawable);
							myViewHolder.title.setTextColor(Color.WHITE);
							myViewHolder.size.setTextColor(Color.DKGRAY);
							myViewHolder.duration.setTextColor(Color.DKGRAY);
						}
						}
					});
					
				
				
			}
			else{
				myViewHolder.cardView.setBackgroundResource(R.drawable.player_bg);
		myViewHolder.title.setTextColor(Color.WHITE);
							myViewHolder.size.setTextColor(Color.DKGRAY);
							myViewHolder.duration.setTextColor(Color.DKGRAY);
			}
		myViewHolder.cardView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
			//Toast.makeText(context,song.getSongTitle()+" "+position,Toast.LENGTH_SHORT).show();
			Intent i=new Intent(context,PlayerActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("sender","albumDetails");
			i.putExtra("position",position);
			context.startActivity(i);
			
			
			
				
			}
			});
			byte[] bitmapa=utils.getAlbumArt(song.getSongPath());
			if (bitmap != null){
			Glide.with(context).asBitmap().load(bitmapa).into(myViewHolder.albumArt);
			}
			else {
				Glide.with(context).load(R.drawable.music).into(myViewHolder.albumArt);
				}
			
			
			
	}
	@Override
	public int getItemCount() {
	    return songArrayList.size();
	}
	public void getMetadate(Uri uri){
		
		
	}
	}