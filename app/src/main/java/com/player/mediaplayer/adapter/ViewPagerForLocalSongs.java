package com.player.mediaplayer.adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.player.mediaplayer.DownloadFragment;
import com.player.mediaplayer.TopAlbumFragment;
import com.player.mediaplayer.MyFragment;
import com.player.mediaplayer.albumSongFragment;
import com.player.mediaplayer.artistSongFragment;
import com.player.mediaplayer.folderSongFragment;
import com.player.mediaplayer.localSongFragment;
import com.player.mediaplayer.constant.Utils;

public class ViewPagerForLocalSongs  extends FragmentPagerAdapter {
	private Activity act;
	//private String tabtitles[] = new String[] { "Tab1", "Tab2", "Tab3" };
    public ViewPagerForLocalSongs(FragmentManager fm, Activity act) {
        super(fm);
        this.act = act;
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            // Top Rated fragment activity
            return new localSongFragment(act);
        case 1:
            // Games fragment activity
            return new artistSongFragment(act);
        case 2:
            // Movies fragment activity
            return new albumSongFragment(act);
        case 3:
        	return new folderSongFragment(act);
        }
        
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 4;
    }
}
