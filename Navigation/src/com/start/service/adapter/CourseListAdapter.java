package com.start.service.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.start.model.AbsClass;
import com.start.model.RemoteClassInfo;
import com.start.navigation.R;

public class CourseListAdapter extends BaseAdapter {
	public static final int DAY_OF_WEEK = 1;
	public static final int CLASS = 0;

	private List<AbsClass> courses = new ArrayList<AbsClass>();
	private LayoutInflater inflater;
	private String[] weekdaysArray;
	private String weeks;

	public CourseListAdapter(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		weekdaysArray = context.getResources().getStringArray(R.array.weekdays);
		weeks = context.getResources().getString(R.string.week_duration);
	}

	@Override
	public int getCount() {
		return courses.size();
	}

	@Override
	public AbsClass getItem(int position) {
		return courses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int viewType = getItemViewType(position);
		AbsClass c = getItem(position);

		if (viewType == DAY_OF_WEEK) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_section, parent, false);
			}
			((TextView) convertView).setText(weekdaysArray[getItem(position).getWeekday() - 1]);
		} else {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_item_class, parent, false);
			}
			((TextView) convertView.findViewById(R.id.time)).setText(c.getDuration());
			((TextView) convertView.findViewById(R.id.course)).setText(c.getCourse());
			((TextView) convertView.findViewById(R.id.teacher)).setText(c.getTeacher());
			if (c instanceof RemoteClassInfo) {
				((TextView) convertView.findViewById(R.id.weeks)).setText(String.format(weeks, ((RemoteClassInfo) c).getStartWeek(), ((RemoteClassInfo) c).getEndWeek()));
			}
		}
		
		return convertView;
	}

	@Override
	public int getItemViewType(int position) {
		String course = getItem(position).getCourse();
		if (course == null) {
			return DAY_OF_WEEK;
		} else {
			return CLASS;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public void setData(List<AbsClass> data) {
		courses.clear();
		courses.addAll(data);
	}

	@Override
	public boolean isEnabled(int position) {
		return getItemViewType(position) == CLASS;
	}
	
	public static void sortCourse(List<AbsClass> classList) {
		classList.add(Monday);
		classList.add(Tuesday);
		classList.add(Wednesday);
		classList.add(Thursday);
		classList.add(Friday);
		classList.add(Saturday);
		classList.add(Sunday);
		
		Collections.sort(classList);
	}

	private static final MockClass Monday = new MockClass(1);
	private static final MockClass Tuesday = new MockClass(2);
	private static final MockClass Wednesday = new MockClass(3);
	private static final MockClass Thursday = new MockClass(4);
	private static final MockClass Friday = new MockClass(5);
	private static final MockClass Saturday = new MockClass(6);
	private static final MockClass Sunday = new MockClass(7);
	
	private static class MockClass implements AbsClass {

		private int day;

		public MockClass(int weekday) {
			this.day = weekday;
		}
		
		@Override
		public int compareTo(AbsClass another) {
			if (another instanceof MockClass) {
				return day - ((MockClass) another).day;
			} else {
				if (day == another.getWeekday()) {
					return -1;
				} else {
					return day - another.getWeekday();
				}
			}
		}

		@Override
		public String getCourse() {
			return null;
		}

		@Override
		public String getTeacher() {
			return null;
		}

		@Override
		public String getDuration() {
			return null;
		}

		@Override
		public int getWeekday() {
			return day;
		}
		
	}
}
