package com.start.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.start.core.Constant;
import com.start.core.CoreActivity;

/**
 * 开始页
 * @author start
 *
 */
public class StartActivity extends CoreActivity implements AnimationListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_start, null);
		setContentView(view);
		AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(this);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}
	
	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		String CURRENTDATAFILENO=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferences.CURRENTDATAFILENO, Constant.EMPTYSTR);
		if(Constant.EMPTYSTR.equals(CURRENTDATAFILENO)){
			startActivity(new Intent(StartActivity.this, FirstSetMapDataActivity.class));
		}else{
			startActivity(new Intent(StartActivity.this, MainActivity.class));
		}
		finish();
	}
	
}