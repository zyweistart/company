package com.ancun.yulualiyun;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.utils.LogUtils;
import com.ancun.widget.GuideScrollLayout;
import com.ancun.widget.GuideScrollLayout.OnViewChangeListener;

/**
 * 欢迎引导
 * @author Start
 */
public class GuideActivity extends CoreActivity implements OnViewChangeListener,
		OnClickListener {

	private GuideScrollLayout mScrollLayout;
	private ImageView[] mImageViews;
	private int mViewCount;
	private int mCurSel;
	private ImageButton ibGuideMainToLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
			String channel=appInfo.metaData.getString("UMENG_CHANNEL");
			if("Ancun Market".equalsIgnoreCase(channel)){
				//官网版引导页
				setContentView(R.layout.activity_guide);
			}else{
				//市场版引导页
				setContentView(R.layout.activity_guidem);
			}
		} catch (NameNotFoundException e) {
			LogUtils.logError(e);
			return;
		}
		ibGuideMainToLogin = (ImageButton) findViewById(R.id.guid_main_btn_to_login);
		ibGuideMainToLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putBoolean(Constant.BUNLE_AUTOLOGINFLAG, true);
				Intent intent=new Intent(GuideActivity.this,LoginActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		mScrollLayout = (GuideScrollLayout) findViewById(R.id.guide_scrollLayout);
		// FrameLayout
		// flwelcome=(FrameLayout)mScrollLayout.findViewById(R.id.guide_welcome);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.guide_layout);
		mViewCount = mScrollLayout.getChildCount();
		mImageViews = new ImageView[mViewCount];
		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);
	}

	@Override
	public void OnViewChange(int view) {
		setCurPoint(view);
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mCurSel = index;
		// 显示和隐藏进入登录页面按钮
		if (mCurSel == mViewCount - 1) {
			ibGuideMainToLogin.setVisibility(View.VISIBLE);
		} else {
			ibGuideMainToLogin.setVisibility(View.GONE);
		}
	}

}