package com.start.navigation;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.map.reader.header.FileOpenResult;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.start.core.AppConfig;
import com.start.utils.Utils;
import com.start.widget.ScrollLayout;

/**
 * 主界面
 * @author start
 *
 */
public class MainActivity extends MapActivity implements OnTouchListener {

	private MapView mMapView;
	
	private int mCurSel;
	private int mViewCount;
	private RadioButton[] mButtons;
	private ScrollLayout mScrollLayout;
	
	private RadioButton rboIntroduction;
	private RadioButton rboMap;
	private RadioButton rboProcess;
	private RadioButton rboFriend;
	private RadioButton rboMore;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		setUpUI();
	}
	
	@Override
    protected void onResume() {
	    	super.onResume();
	    	if(mViewCount == 0) mViewCount = mScrollLayout.getChildCount();
	    	if(mCurSel == 0 && !rboIntroduction.isChecked()) {
	    		rboIntroduction.setChecked(true);
	    		rboMap.setChecked(false);
	    		rboProcess.setChecked(false);
	    		rboFriend.setChecked(false);
	    		rboMore.setChecked(false);
	    	}
	    	//读取左右滑动配置
	    	mScrollLayout.setIsScroll(AppConfig.isScrollLayoutScrool());
    }

	private void init() {
		rboIntroduction=(RadioButton)findViewById(R.id.main_footbar_introduction);
		rboMap=(RadioButton)findViewById(R.id.main_footbar_map);
		rboProcess=(RadioButton)findViewById(R.id.main_footbar_process);
		rboFriend=(RadioButton)findViewById(R.id.main_footbar_friend);
		rboMore=(RadioButton)findViewById(R.id.main_footbar_more);
		
	    	mScrollLayout = (ScrollLayout) findViewById(R.id.main_scrolllayout);
	    	
	    	LinearLayout linearLayout = (LinearLayout) findViewById(R.id.module_main_footer_id);
	    	mViewCount = mScrollLayout.getChildCount();
	    	mButtons = new RadioButton[mViewCount];
	    	
	    	for(int i = 0; i < mViewCount; i++) 	{
	    		mButtons[i] = (RadioButton) linearLayout.getChildAt(i*2);
	    		mButtons[i].setTag(i);
	    		mButtons[i].setChecked(false);
	    		mButtons[i].setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						int pos = (Integer)(v.getTag());
						//点击当前项刷新
			    			if(mCurSel == pos) {
			    			}
						mScrollLayout.snapToScreen(pos);
					}
				});
	    	}
	    	
	    	//设置第一显示屏
	    	mCurSel = 0;
	    	mButtons[mCurSel].setChecked(true);
	    	
	    	mScrollLayout.SetOnViewChangeListener(new ScrollLayout.OnViewChangeListener() {
				public void OnViewChange(int viewIndex) {
					//切换列表视图-如果列表数据为空：加载数据
					setCurPoint(viewIndex);
				}
			});
    }
	
    /**
     * 设置底部栏当前焦点
     * @param index
     */
    private void setCurPoint(int index) {
	    	if (index < 0 || index > mViewCount - 1 || mCurSel == index)
	    		return;
	   	
	    	mButtons[mCurSel].setChecked(false);
	    	mButtons[index].setChecked(true);	
	    	mCurSel = index;
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}
	
	private void setUpUI() {
//		ViewGroup container = (ViewGroup) findViewById(R.id.module_main_frame_ll_content);
		mMapView = new MapView(this);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setClickable(true);
		mMapView.setOnTouchListener(this);
//		container.addView(mMapView, 0);
		setMapFile();
	}
	
	private void setMapFile() {
		String path = String.format("%1$s/%2$s.map", AppConfig.CONFIG_DATA_PATH_MEDMAP,"0202");
		String fullPath=Utils.getFile(this, path).getPath();
		Log.v("MainActivity",fullPath);
		FileOpenResult openResult = mMapView.setMapFile(Utils.getFile(this, path));
		if (!openResult.isSuccess()) {
			return;
		}
		updateOverlay();
	}
	
	private void updateOverlay() {
		
	}
	
}
