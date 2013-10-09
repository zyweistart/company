package com.start.model;

import java.util.HashMap;


public class RemoteClassInfo implements AbsClass {
 
	private static final HashMap<String, Integer> WeekdayMap;
	
	static {
		WeekdayMap = new HashMap<String, Integer>();

		WeekdayMap.put("一", 1);
		WeekdayMap.put("二", 2);
		WeekdayMap.put("三", 3);
		WeekdayMap.put("四", 4);
		WeekdayMap.put("五", 5);
		WeekdayMap.put("六", 6);
		WeekdayMap.put("日", 7);
		
	}
	private String name;
	private String teacher;
	private String time;
	private int startWeek;
	private int endWeek;
	private int weekday;
	
	public RemoteClassInfo(String name, String teacher, String time) {
		super();
		this.name = name;
		this.teacher = teacher;
		this.time = time;
		this.startWeek = 1;
		this.endWeek = 20;
		this.weekday = WeekdayMap.get(time.substring(1, 2));
	}

	@Override
	public String getCourse() {
		return name;
	}

	@Override
	public String getTeacher() {
		return teacher;
	}

	public String getTime() {
		return time;
	}

	public int getStartWeek() {
		return startWeek;
	}

	public int getEndWeek() {
		return endWeek;
	}

	public void setStartWeek(int startWeek) {
		this.startWeek = startWeek;
	}

	public void setEndWeek(int endWeek) {
		this.endWeek = endWeek;
	}

	@Override
	public String getDuration() {
		return time.substring(2);
	}

	@Override
	public int compareTo(AbsClass another) {
		RemoteClassInfo anotherClass = (RemoteClassInfo) another;
		
		int result = this.weekday - anotherClass.weekday;
		if (result == 0) {
			String first = time.substring(3, 4);
			String last = anotherClass.time.substring(3, 4);
			result = first.compareTo(last);
		}
		return result;
	}

	@Override
	public int getWeekday() {
		return weekday;
	}
}
