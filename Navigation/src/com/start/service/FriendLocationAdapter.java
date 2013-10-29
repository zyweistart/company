package com.start.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.start.navigation.R;

public class FriendLocationAdapter extends BaseAdapter {
	
	private List<Map<String,String>> datas=new ArrayList<Map<String,String>>();

	private LayoutInflater inflater;

	public FriendLocationAdapter(LayoutInflater layoutInflater) {
		this.inflater = layoutInflater;
	}
	
	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.lvitem_mapdata_index, parent, false);
		}
		((CheckedTextView) convertView).setText(datas.get(position).toString());
		return convertView;
	}
	
	public void setData(List<Map<String,String>> datas){
		if(datas!=null){
			this.datas=datas;
			this.notifyDataSetChanged();
		}
	}

}