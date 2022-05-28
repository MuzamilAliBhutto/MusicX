package com.VerticalCodes.tabs;

import android.content.Context;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class layout2 extends Fragment {
	Context context;
	ArrayList<Song> songArrayList;
	RecyclerView recyclerView;
	albumAdapter adapter;
	public layout2(Context context){
		this.context=context;
		}
	
	@Override
	public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
		View view=arg0.inflate(R.layout.layout_2,arg1,false);
		recyclerView=view.findViewById(R.id.recyclerview2);
		songArrayList=layout1.albumSongArrayList;
		adapter=new albumAdapter(context,songArrayList);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new GridLayoutManager(context,2));
		
		return view;
		
		
	}

}