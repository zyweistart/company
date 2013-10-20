package com.start.navigation;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.start.core.BoardActivity;
import com.start.model.Office;


public class OfficeActivity extends BoardActivity {

	private Office mOfficeInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_office);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.actionbar);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String room = extras.getString(Office.COLUMN_NAME_ROOM);
			startLoader(room);
		} else {
			finish();
		}
	}

	@Override
	protected void setupView() {
		if (mOfficeInfo == null) {
			return;
		}

		((TextView) findViewById(R.id.affair)).setText(mOfficeInfo.getAffair());
		((TextView) findViewById(R.id.staff)).setText(mOfficeInfo.getStaff());
	}

	@Override
	protected boolean doLoad(String... params) {
		boolean ret = false;

		Cursor cr = getContentResolver().query(Office.CONTENT_URI, null, Office.COLUMN_NAME_ROOM + "=?", new String[] {params[0]}, null);
		if (cr.moveToNext()) {
			Office o = new Office(cr.getString(cr.getColumnIndex(Office.COLUMN_NAME_ROOM)), 
					cr.getString(cr.getColumnIndex(Office.COLUMN_NAME_AFFAIR)),
							cr.getString(cr.getColumnIndex(Office.COLUMN_NAME_STAFF)));	
			mOfficeInfo = o;
			ret = true;
		}
		cr.close();
		return ret;	
	}
	
}
