package com.ancun.yulualiyun;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;

public class TipActivity extends CoreActivity {
	

	private static final String WEBURL="file:///android_asset/html/tip.html";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		setContentView(R.layout.activity_tip);
//		setNavigationTitle(R.string.tip_title);
		
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		
		LinearLayout layout=new LinearLayout(this);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout.setBackgroundColor(R.color.activity_gb);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		View navigation=layoutInflater.inflate(R.layout.common_title, null);
		layout.addView(navigation);
		((TextView)layout.findViewById(R.id.common_title_name)).setText(R.string.tip_title);
		
		WebView webView=new WebView(this);
		webView.getSettings().setDefaultTextEncodingName(Constant.ENCODE);
		webView.loadUrl(WEBURL);  
		layout.addView(webView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
		setContentView(layout);
	}
	
}