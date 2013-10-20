package com.start.navigation;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.start.core.BoardActivity;
import com.start.model.Course;
import com.start.model.Teacher;

public class ClassActivity extends BoardActivity {
	
	private Course mCourseInfo;
	private Teacher mTeacherInfo;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_class);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String cName = extras.getString(Course.TABLE_NAME);
			String tName = extras.getString(Teacher.TABLE_NAME);
			startLoader(cName, tName);
		} else {
			finish();
		}
	}

	@Override
	protected void setupView() {
		if (mCourseInfo != null) {
			ViewGroup block = (ViewGroup) findViewById(R.id.class_block);
			block.setVisibility(View.VISIBLE);
			((TextView) block.findViewById(R.id.name)).setText(mCourseInfo.getName());
			((TextView) block.findViewById(R.id.desc)).setText(mCourseInfo.getDesc());
		}
		
		if (mTeacherInfo != null) {
			ViewGroup block = (ViewGroup) findViewById(R.id.teacher_block);
			block.setVisibility(View.VISIBLE);
			((TextView) block.findViewById(R.id.name)).setText(mTeacherInfo.getName());
			((TextView) block.findViewById(R.id.desc)).setText(mTeacherInfo.getDesc());
		}
	}

	@Override
	protected boolean doLoad(String... params) {
		boolean ret = false;

		ContentResolver resolver = getContentResolver();
		Cursor cr = resolver.query(Course.CONTENT_URI, null, Course.COLUMN_NAME_NAME + "=?", new String[] {params[0]}, null);
		if (cr.moveToNext()) {
			Course c = new Course(cr.getString(cr.getColumnIndex(Course.COLUMN_NAME_NAME)),
					cr.getString(cr.getColumnIndex(Course.COLUMN_NAME_DESC)));
			
			mCourseInfo = c;
			ret = true;
		}
		cr.close();

		cr = resolver.query(Teacher.CONTENT_URI, null, Teacher.COLUMN_NAME_NAME + "=?", new String[] {params[1]}, null);
		if (cr.moveToNext()) {
			Teacher t = new Teacher(cr.getString(cr.getColumnIndex(Teacher.COLUMN_NAME_NAME)),
					cr.getString(cr.getColumnIndex(Teacher.COLUMN_NAME_DESC)));
			
			mTeacherInfo = t;
			ret = true;
		}
		cr.close();
		
		return ret;			
	}
	
}
