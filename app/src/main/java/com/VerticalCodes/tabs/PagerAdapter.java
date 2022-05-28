package com.VerticalCodes.tabs;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class PagerAdapter  extends FragmentPagerAdapter{
	
	FragmentManager fragmentManager;
	public PagerAdapter(ArrayList<Fragment> list,ArrayList<String> arrayListTitle, FragmentManager fragmentManager){
		super(fragmentManager);
		this.list=list;
		this.fragmentManager=fragmentManager;
		this.arrayListTitle=arrayListTitle;
	}
	@Override
	public long getItemId(int arg0) {
		return list.get(arg0).getId();
	}
	ArrayList<String> arrayListTitle=new ArrayList<>();
	ArrayList<Fragment> list=new ArrayList<>();
	@Override
	public int getCount() {
	    return list.size();
	}

	@Override
	public Fragment getItem(int arg0) {
	    return list.get(arg0);
	}
	@Override
	public CharSequence getPageTitle(int arg0) {
		return arrayListTitle.get(arg0);
	}
}