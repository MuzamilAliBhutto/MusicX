package com.VerticalCodes.tabs;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;

public class FragmentMiniPlayer extends Fragment{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
		View view=inflater.inflate(R.layout.fragment_mini_player,viewGroup,false);
		return view;
	}
}