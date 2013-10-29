package com.start.service;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.start.model.MapData;
import com.start.navigation.R;

public class MapDataAdapter extends BaseAdapter {
	
	private List<MapData> mapDatas=new ArrayList<MapData>();

	private LayoutInflater inflater;

	public MapDataAdapter(LayoutInflater layoutInflater) {
		this.inflater = layoutInflater;
	}
	
	@Override
	public int getCount() {
		return mapDatas.size();
	}

	@Override
	public Object getItem(int position) {
		return mapDatas.get(position);
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
		((CheckedTextView) convertView).setText(mapDatas.get(position).getId().substring(2,4));
		return convertView;
	}
	
	public void setData(List<MapData> mapDatas){
		if(mapDatas!=null){
			this.mapDatas=mapDatas;
			this.notifyDataSetChanged();
		}
	}

}