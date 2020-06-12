package com.player.mediaplayer.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.player.SongsPod.R;
import com.player.mediplayer.beans.SongInfo;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapterArtist extends BaseExpandableListAdapter {

	/*
	 *    // private Context _context;    // private List<String>
	 * _listDataHeader; // header titles    // // child data in format of header
	 * title, child title     //private HashMap<String, List<String>>
	 * _listDataChild;
	 */
	private Context _context;
	private List<String> _listDataHeader;
	private HashMap<String, ArrayList<SongInfo>> _listDataChild;

	public ExpandableListAdapterArtist(Context context, List<String> listDataHeader,HashMap<String, ArrayList<SongInfo>> listChildData) {
		this._context = context;
		this._listDataChild = listChildData;
		this._listDataHeader = listDataHeader;
	}

	/*
	 *       public ExpandableListAdapterArtist(Context context, List<String>
	 * listDataHeader,HashMap<String, List<String>> listChildData) {
	 *         this._context = context;         this._listDataHeader =
	 * listDataHeader;         this._listDataChild = listChildData;     }
	 */
	
	public List<String> getGroupList()
	{
		return _listDataHeader;
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this._listDataHeader.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		 return this._listDataChild.get(this._listDataHeader.get(groupPosition))
	                .size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
        
        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
 
        return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final SongInfo childText = (SongInfo) getChild(groupPosition, childPosition);
		 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }
 
        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);
 
        txtListChild.setText(getSongName(childText.getPath()));
        return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		 return true;
	}
	
	
	private String getSongName(String path) {
		return path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
	}
	/*
	 *       @Override     public Object getChild(int groupPosition, int
	 * childPosititon) {         return
	 * this._listDataChild.get(this._listDataHeader.get(groupPosition))
	 *                 .get(childPosititon);     }       @Override     public
	 * long getChildId(int groupPosition, int childPosition) {         return
	 * childPosition;     }       @Override     public View getChildView(int
	 * groupPosition, final int childPosition,             boolean isLastChild,
	 * View convertView, ViewGroup parent) {           final String childText =
	 * (String) getChild(groupPosition, childPosition);           if
	 * (convertView == null) {             LayoutInflater infalInflater =
	 * (LayoutInflater) this._context
	 *                     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 *             convertView = infalInflater.inflate(R.layout.list_item,
	 * null);         }           TextView txtListChild = (TextView) convertView
	 *                 .findViewById(R.id.lblListItem);  
	 *         txtListChild.setText(childText);         return convertView;
	 *     }       @Override     public int getChildrenCount(int groupPosition)
	 * {         return
	 * this._listDataChild.get(this._listDataHeader.get(groupPosition))
	 *                 .size();     }       @Override     public Object
	 * getGroup(int groupPosition) {         return
	 * this._listDataHeader.get(groupPosition);     }       @Override     public
	 * int getGroupCount() {         return this._listDataHeader.size();     }  
	 *     @Override     public long getGroupId(int groupPosition) {
	 *         return groupPosition;     }       @Override     public View
	 * getGroupView(int groupPosition, boolean isExpanded,             View
	 * convertView, ViewGroup parent) {         String headerTitle = (String)
	 * getGroup(groupPosition);         if (convertView == null) {
	 *             LayoutInflater infalInflater = (LayoutInflater) this._context
	 *                     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 *             convertView = infalInflater.inflate(R.layout.list_group,
	 * null);         }           TextView lblListHeader = (TextView)
	 * convertView                 .findViewById(R.id.lblListHeader);
	 *         lblListHeader.setTypeface(null, Typeface.BOLD);
	 *         lblListHeader.setText(headerTitle);           return convertView;
	 *     }       @Override     public boolean hasStableIds() {         return
	 * false;     }       @Override     public boolean isChildSelectable(int
	 * groupPosition, int childPosition) {         return true;     }
	 */

	public List<String> getList() {
		// TODO Auto-generated method stub
		return _listDataHeader;
	}
	
	public HashMap<String, ArrayList<SongInfo>> getListMap() {
		// TODO Auto-generated method stub
		return _listDataChild;
	}
	
}
