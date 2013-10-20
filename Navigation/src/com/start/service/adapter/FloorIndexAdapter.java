package com.start.service.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.start.model.Building;
import com.start.navigation.R;

public class FloorIndexAdapter extends BaseAdapter {

	private Building building;
	private LayoutInflater inflater;

	public FloorIndexAdapter(LayoutInflater layoutInflater) {
		super();
		this.inflater = layoutInflater;
	}

	public int getCount() {
		return building == null ? 0 : building.getFloorCount();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_item_floor_index, parent, false);
		}
		int floor = building.getMaxFloor() - position;
		((CheckedTextView) convertView).setText(floor > 0 ? ("" + floor) : "B" + (1-floor));
		return convertView;
	}

	public void setData(Building mBuilding) {
		this.building = mBuilding;
		notifyDataSetChanged();
	}

}
