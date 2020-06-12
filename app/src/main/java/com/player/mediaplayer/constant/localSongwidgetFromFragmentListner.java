package com.player.mediaplayer.constant;

import android.app.ExpandableListActivity;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;

public interface localSongwidgetFromFragmentListner {
	void onloadListView(ListView listview, EditText edit);
	void onloadExpendableListViewArtist(ExpandableListView expList, EditText edit);
	void onloadExpendableListViewAlbum(ExpandableListView expList, EditText edit);
	void onloadExpendableListViewFolder(ExpandableListView expList, EditText edit);

}
