package com.VerticalCodes.tabs;

import android.Manifest;
import android.content.Intent;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;
import com.VerticalCodes.tabs.PagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.Tab;
import com.VerticalCodes.tabs.layout1;
import com.VerticalCodes.tabs.layout2;
import com.VerticalCodes.tabs.layout3;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	Toolbar toolbar;
	ViewPager pager;
	TabLayout tabLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabLayout = findViewById(R.id.tabLayout);
		pager = findViewById(R.id.viewPager);
		Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
			@Override
			public void onPermissionGranted(PermissionGrantedResponse response) {
				/* ... */
				ArrayList<String> title = new ArrayList<>();
				ArrayList<Fragment> list = new ArrayList<>();
				list.add(new layout1(getApplicationContext()));
				list.add(new layout2(getApplicationContext()));
				title.add("Songs");
				title.add("Albums");
				PagerAdapter adapter = new PagerAdapter(list, title, getSupportFragmentManager());
				pager.setAdapter(adapter);
				tabLayout.setupWithViewPager(pager);
				}

			@Override
			public void onPermissionDenied(PermissionDeniedResponse response) {
				/* ... */
				Toast.makeText(getApplicationContext(),"permission needed",Toast.LENGTH_SHORT).show();
				}

			@Override
			public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
				/* ... */
				token.continuePermissionRequest();
				
				}
		}).check();
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.song_menu, menu);
		MenuItem item = menu.findItem(R.id.search);
		SearchView searchView = (SearchView) item.getActionView();
		searchView.setQueryHint("Search here.....");
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String a) {
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				/*String t = newText.toLowerCase();
				ArrayList<Song> list = new ArrayList<>();
				for (Song song : layout1.songArrayList) {
					if (song.getSongTitle().toLowerCase().contains(t)) {
						list.add(song);
					}
				}
				layout1.songAdapter.updateList(list);
				//Toast.makeText(getApplicationContext(),newText,Toast.LENGTH_SHORT).show();
				*/
				layout1.songAdapter.getFilter().filter(newText);

				return true;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		int id = menuItem.getItemId();
		if (id == R.id.about) {
			Intent i = new Intent(getApplicationContext(), ActivityAbout.class);
			startActivity(i);
		}

		return true;
	}
}