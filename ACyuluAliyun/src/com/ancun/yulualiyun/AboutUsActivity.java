package com.ancun.yulualiyun;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.ancun.core.CoreActivity;
import com.ancun.utils.LogUtils;
/**
 * 关于我们
 * @author Start
 */
public class AboutUsActivity extends CoreActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
		setNavigationTitle(R.string.aboutus_title);
		TextView setting_about_us_version=(TextView)findViewById(R.id.setting_about_us_version);
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
//			int versionCode=packInfo.versionCode;
//			ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(),PackageManager.GET_META_DATA);
			setting_about_us_version.setText("当前版本："+packInfo.versionName);
//			setting_about_us_version.setText("v:"+versionCode+"\nc:"+appInfo.metaData.getString("BaiduMobAd_CHANNEL"));
//			LogUtils.logInfo("当前版本:"+versionCode+"\t友盟渠道:"+appInfo.metaData.getString("UMENG_CHANNEL")+"\t百度渠道:"+appInfo.metaData.getString("BaiduMobAd_CHANNEL"));
		} catch (NameNotFoundException e) {
			LogUtils.logError(e);
		}
    }
  
}