package com.start.service;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.start.model.Searchable;
import com.start.navigation.R;

public class SearchResultAdapter extends BaseAdapter {

	private List<Searchable> items;
	private LayoutInflater mInflater;
	
	public SearchResultAdapter(LayoutInflater layoutInflater) {
		this.mInflater = layoutInflater;
	}

	public int getCount() {
		return items == null ? 0 : items.size();
	}

	public Searchable getItem(int position) {
		return items.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_result_item, parent, false);
			ViewHolder vh = new ViewHolder();
			vh.header = (TextView) convertView.findViewById(android.R.id.text1);
			vh.subHeader = (TextView) convertView.findViewById(android.R.id.text2);
			convertView.setTag(vh);
		}

		ViewHolder vh = (ViewHolder) convertView.getTag();
		vh.header.setText(getItem(position).getHeader());
		vh.subHeader.setText(getItem(position).getSubHeader());
		return convertView;
	}
	
	private class ViewHolder {
		TextView header;
		TextView subHeader;
	}

	public void setData(List<Searchable> searchables) {
		this.items = searchables;
		notifyDataSetChanged();
	}

}
