package com.VerticalCodes.tabs;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> implements Filterable{
	Context context;
	ArrayList<Song> songArrayList=new ArrayList<>();
	ArrayList<Song> songListFull=new ArrayList<>();
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
		public SongAdapter(Context context,ArrayList<Song> songArrayList){
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
		myViewHolder.cardView.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
			//Toast.makeText(context,song.getSongTitle()+" "+position,Toast.LENGTH_SHORT).show();
			Intent i=new Intent(context,PlayerActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			i.putExtra("position",position);
			context.startActivity(i);
			
			
			
			
				
			}
			});
			byte[] bitmap=utils.getAlbumArt(song.getSongPath());
			if (bitmap != null){
			Glide.with(context).asBitmap().load(bitmap).into(myViewHolder.albumArt);
			}
			else {
				Glide.with(context).load(R.drawable.music).into(myViewHolder.albumArt);
				}
			
			
			
	}

	@Override
	public int getItemCount() {
	    return songArrayList.size();
	}
	@Override
	public Filter getFilter() {
		return MyFilter;
	}
	private Filter MyFilter=new Filter(){
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			ArrayList<Song> list=new ArrayList<>();
			if (constraint == null || constraint.length() == 0){
				list.addAll(songListFull);
			}
			else{
				String SearchPattern=constraint.toString().toLowerCase().trim();
				for(Song song : songListFull){
					
					String title=song.getSongTitle();
					
					if(title.toLowerCase().trim().contains(SearchPattern)){
						list.add(song);
					}
				}
			}
			FilterResults results=new FilterResults();
			results.values=list;
			results.count=list.size();
			return results;
		}
		
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			songArrayList.clear();
			songArrayList.addAll((List)results.values);
			notifyDataSetChanged();
			
		}
		
	};
	void updateList(ArrayList<Song> list){
		songArrayList.clear();
		songArrayList.addAll(list);
		notifyDataSetChanged();
		}
}