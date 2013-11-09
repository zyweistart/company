package com.ancun.yulualiyun;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.ancun.core.CoreActivity;
import com.ancun.yulualiyun.R;

/**
 * 申办公证
 * @author Start
 */
public class RecordedAppealNotarySuccess extends CoreActivity implements OnClickListener {

	private TextView btnNotaryNotify;
	private TextView btnNotaryList;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_recorded_appeal_notary_success);
		
		setNavigationTitle(R.string.recordedappealnotarysuccess_title);
		
		btnNotaryNotify=(TextView)findViewById(R.id.recorded_appeal_notary_notify);
		btnNotaryNotify.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		btnNotaryNotify.setOnClickListener(this);
		btnNotaryList=(TextView)findViewById(R.id.recorded_appeal_notary_list);
		btnNotaryList.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
		btnNotaryList.setOnClickListener(this);
	}
	
	 
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.recorded_appeal_notary_notify:
			Intent intentNotaryNotify=new Intent(RecordedAppealNotarySuccess.this,RecordedAppealNotaryNotify.class);
			startActivity(intentNotaryNotify);
			break;
		case R.id.recorded_appeal_notary_list:
			Intent intentNotaryList=new Intent(RecordedAppealNotarySuccess.this,RecordedAppealNotaryList.class);
			startActivity(intentNotaryList);
			break;
		}
	}
}