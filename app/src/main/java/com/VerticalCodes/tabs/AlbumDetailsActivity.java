package com.VerticalCodes.tabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class AlbumDetailsActivity extends AppCompatActivity {
	RecyclerView recyclerView;
	static ArrayList<Song> albumSongArrayList=new ArrayList<>();
	albumDetailsAdapter albumDetailsAdapter;
	String albumName;
	ImageView album_img,album_img2;
	TextView album_name;
	Song song;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_album_details);
		recyclerView=findViewById(R.id.recyclerview3);
		album_img=findViewById(R.id.activity_album_img);
		album_img2=findViewById(R.id.activity_album_img2);
		album_name=findViewById(R.id.activity_album_txt);
		song=getIntent().getParcelableExtra("song");
		albumName=song.getAlbumName();
		getMetadate(Uri.parse(song.getSongPath()));
		album_name.setText(albumName);
		int j=0;
		for(int i=0; i< layout1.albumSongArrayList.size();i++){
			if(albumName.equals(layout1.albumSongArrayList.get(i).getAlbumName())) {
				albumSongArrayList.add(j,layout1.albumSongArrayList.get(i));
				j++;
				
				}
		}
		albumDetailsAdapter=new albumDetailsAdapter(getApplicationContext(),albumSongArrayList);
		recyclerView.setAdapter(albumDetailsAdapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		
		
		
	}
	public void getMetadate(Uri uri){
		byte[] art=Utils.getAlbumArt(uri.toString());
		Bitmap bitmap=null;
		if (art != null) {
			bitmap=BitmapFactory.decodeByteArray(art,0,art.length);
			Palette.from(bitmap).generate(new Palette.PaletteAsyncListener(){
				@Override
				public void onGenerated(Palette arg0) {
					Palette.Swatch swatch=arg0.getDominantSwatch();
					if(swatch != null){
						album_img2.setBackgroundResource(R.drawable.gradient_bg);
						GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{swatch.getRgb(),0x00000000});
							album_img2.setBackground(gradientDrawable);
							
						}
						else{
							album_img2.setBackgroundResource(R.drawable.gradient_bg);
							GradientDrawable gradientDrawable=new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{0xff000000,0x00000000});
							album_img2.setBackground(gradientDrawable);
							
						}
				}
				
			});
			Glide.with(this)
					.load(art)
					.into(album_img);
			}
			else{
				album_img2.setBackgroundResource(R.drawable.gradient_bg);
				
				Glide.with(this)
					.load(R.drawable.gally)
					.into(album_img);
			}
		
		}
}