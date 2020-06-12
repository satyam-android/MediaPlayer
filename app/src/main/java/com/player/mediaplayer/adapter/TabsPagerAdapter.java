package com.player.mediaplayer.adapter;

import com.player.mediaplayer.DownloadFragment;
import com.player.mediaplayer.TopAlbumFragment;
import com.player.mediaplayer.MyFragment;
import com.player.mediaplayer.categoryFragment;
import com.player.mediaplayer.constant.Utils;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class TabsPagerAdapter extends FragmentPagerAdapter {
	private Activity act;

	// private String tabtitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
	public TabsPagerAdapter(FragmentManager fm, Activity act) {
		super(fm);
		this.act = act;
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new MyFragment(act);
		case 1:
			// Games fragment activity
			return new TopAlbumFragment(act);
		case 2:
			// Movies fragment activity
			return new DownloadFragment(act);

		case 3:
			// Movies fragment activity
			return new categoryFragment(act);
		}
		Utils.toastshow(act, "Error");
		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}
	/*
	 * @Override public CharSequence getPageTitle(int position) { return
	 * tabtitles[position]; }
	 */

}