package com.ancun.yulualiyun;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.Constant.Auth;
import com.ancun.core.CoreActivity;
import com.ancun.core.CoreScrollContent;
import com.ancun.model.UIRunnable;
import com.ancun.utils.LogUtils;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.PasswordVerityUtils;
import com.ancun.utils.TimeUtils;
import com.ancun.widget.ScrollLayout;
import com.ancun.yulualiyun.AppContext.LoadMode;
import com.ancun.yulualiyun.content.ContactContent;
import com.ancun.yulualiyun.content.DialContent;
import com.ancun.yulualiyun.content.RecentContent;
import com.ancun.yulualiyun.content.RecordedManagerContent;

/**
 * @author Start
 */
public class MainActivity extends CoreActivity implements ScrollLayout.LayoutChangeListener, OnClickListener {
	
	private InputMethodManager imManager; 
	
	private ImageButton activity_main_ibmore; 
	private Button activity_main_btnacount,activity_main_btnnetworktip;
	private TextView tab1,tab2,tab3,tab4;
	private ImageView top_bar_select;
	private ScrollLayout activity_main_scrolllayout;

	private DialContent dialContent;
	private RecentContent recentContent;
	private ContactContent contactContent;
	private RecordedManagerContent recordedManagerContent;
	
	private TextView main_tip_content;
	
	private long lastPressTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		main_tip_content=(TextView)findViewById(R.id.main_tip_content);
		
		//我的账户
		activity_main_btnacount=(Button)findViewById(R.id.activity_main_btnacount); 
		activity_main_btnacount.setOnClickListener(this);
		//如果不是主账户则隐藏
		if(!"1".equals(getAppContext().getUserInfo().get("masterflag"))){
			activity_main_btnacount.setVisibility(View.GONE);
		}
		//更多
		activity_main_ibmore=(ImageButton)findViewById(R.id.activity_main_ibmore); 
		activity_main_ibmore.setOnClickListener(this);
		//拨号盘标签
		tab1 = (TextView) findViewById(R.id.tab1);
		//最近通话标签
		tab2 = (TextView) findViewById(R.id.tab2);
		//联系人标签
		tab3 = (TextView) findViewById(R.id.tab3);
		//我的录音标签
		tab4 = (TextView) findViewById(R.id.tab4);
		//标签选择时
		top_bar_select = (ImageView) findViewById(R.id.top_bar_select);
		//网络连接
		activity_main_btnnetworktip=(Button)findViewById(R.id.activity_main_btnnetworktip);
		activity_main_btnnetworktip.setOnClickListener(this);
		//主体框架
		activity_main_scrolllayout = (ScrollLayout) findViewById(R.id.activity_main_scrolllayout);
		activity_main_scrolllayout.addChangeListener(this);
		//拨号盘
		dialContent = new DialContent(this, R.layout.content_dial);
		//最近通话
		recentContent = new RecentContent(this, R.layout.content_recent);
		recentContent.isOpenRefreshData=true;
		//联系人
		contactContent = new ContactContent(this, R.layout.content_contact);
		contactContent.isOpenRefreshData=true;
		//我的录音
		recordedManagerContent = new RecordedManagerContent(this,R.layout.content_recordedmanager);
		recordedManagerContent.isOpenRefreshData=true;
		
		activity_main_scrolllayout.addView(dialContent.getLayoutView());
		activity_main_scrolllayout.addView(recentContent.getLayoutView());
		activity_main_scrolllayout.addView(contactContent.getLayoutView());
		activity_main_scrolllayout.addView(recordedManagerContent.getLayoutView());
		//默认选择为第一屏取值为0，1，2  ，3 
		activity_main_scrolllayout.snapToScreen(0);
		
		imManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//电话监听
//		manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//		manager.listen(new MyPhoneStateListener(getAppContext()),PhoneStateListener.LISTEN_CALL_STATE);
		
		//网络检测
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(NetCheckReceiver.NetCheckFlag){
								activity_main_btnnetworktip.setVisibility(View.GONE);
							}else{
								activity_main_btnnetworktip.setVisibility(View.VISIBLE);
							}
						}
					});
					TimeUtils.sleep(1000);
				}
			}
		}).start();
		//版本检测
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(NetConnectManager.isNetWorkAvailable(MainActivity.this)){
					Map<String,String> requestParams=new HashMap<String,String>();
					requestParams.put("type","6");
					requestParams.put("termtype","7");
					Map<String,String> headerParams=new HashMap<String,String>();
					headerParams.put("sign", "");
					getAppContext().exeNetRequest(MainActivity.this,null,Constant.GlobalURL.versioninfoGet,requestParams,headerParams,new UIRunnable() {
						@Override
						public void run() {
							Integer maxVersion=Integer.parseInt(getInfoContent().get("maxverno"));
							Integer minVersion=Integer.parseInt(getInfoContent().get("minverno"));
							final String url=getInfoContent().get("url");
							int currentVersionCode=getAppContext().getSharedPreferencesUtils().getInteger(Constant.SharedPreferencesConstant.SP_CURRENTVERSIONCODE,0);
							if(minVersion>currentVersionCode){
								handler.post(new Runnable() {
									@Override
									public void run() {
										new AlertDialog.Builder(MainActivity.this)
										.setMessage("您使用的版本过低，请立即升级！否则可能无法正常使用。")
										.setCancelable(false)
										.setPositiveButton("确定", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												Intent fIntent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
												startActivity(fIntent);
												finish();
											}
										}).show();
									}
								});
							}else if(maxVersion>currentVersionCode){
								try{
									SimpleDateFormat sdf = new SimpleDateFormat(TimeUtils.yyyyMMdd_F);
									Date date= sdf.parse(TimeUtils.getSysdate());
									String datim=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_VERSION_DATA, "");
									if("".equals(datim)){
										datim=TimeUtils.getSysdate();
										getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_VERSION_DATA, datim);
									}
									Date mydate= sdf.parse(datim);
									long  day=(date.getTime()-mydate.getTime());//(24*60*60*1000);
									//每天检测一次
									if(day>30){
										getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_VERSION_DATA, TimeUtils.getSysdate());
										handler.post(new Runnable() {
											@Override
											public void run() {
												new AlertDialog.Builder(MainActivity.this)
												.setIcon(android.R.drawable.ic_dialog_info)
												.setMessage("检测到新版本，是否升级？")
												.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int whichButton) {
														new DownloadAppTask().execute(url);
													}
												}).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int whichButton) {
														dialog.dismiss();
													}
												}).show();
											}
										});
									}
								}catch(Exception e){
									LogUtils.logError(e);
								}
							}
						}
					},null);
				}
			}
			
		}).start();
		
		Bundle bundle=getIntent().getExtras();
		if(bundle!=null){
			String password=bundle.getString("password");
			//密码修改标记开
			if(password!=null&&!PasswordVerityUtils.verify(null, password)){
				new AlertDialog.Builder(MainActivity.this)
				.setCancelable(false)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setMessage("您的密码过于简单,存在严重的安全隐患,建议您及时修改 ,以免造成录音被误删等不必要的麻烦和损失。")
				.setPositiveButton("立即修改", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class)); 
					}
				}).setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				}).show();
			}
		}
		setWelcomeInfo();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false)){
			getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false);
		}
	}

	@Override
	public void doChange(int lastIndex, int currentIndex) {
		if (lastIndex != currentIndex) {
			//更改图标
			switch (currentIndex) {
			case 0:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				break;
			case 1:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				break;
			case 2:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				break;
			case 3:
				tab1.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab2.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab3.setTextColor(this.getResources().getColor(R.color.main_title_bar_normal));
				tab4.setTextColor(this.getResources().getColor(R.color.main_title_bar_selected));
				break;
			default:
				break;
			}
			TranslateAnimation animation = null;
			int tab1_x=0;
			int tab2_x=((LinearLayout) tab2.getParent()).getWidth();
			int tab3_x=((LinearLayout) tab2.getParent()).getWidth()+((LinearLayout) tab3.getParent()).getWidth();
			int tab4_x=((LinearLayout) tab2.getParent()).getWidth()+((LinearLayout) tab3.getParent()).getWidth()+((LinearLayout) tab4.getParent()).getWidth();
			switch (currentIndex) {
			case 0:
				if (lastIndex == 1) {
					animation = new TranslateAnimation(tab2_x,tab1_x,0, 0);
				} else if (lastIndex == 2) {
					animation = new TranslateAnimation(tab3_x,tab1_x , 0, 0);
				}else if (lastIndex == 3) {
					animation = new TranslateAnimation(tab4_x,tab1_x, 0, 0);
				}
				break;
			case 1:
				if (lastIndex ==0) {
					// 左到中
					animation = new TranslateAnimation(tab1_x,tab2_x,0, 0);
				} else if (lastIndex  ==2) {
					// 右到中
					animation = new TranslateAnimation(tab3_x,tab2_x, 0,0);
				}else if (lastIndex == 3) {
					animation = new TranslateAnimation(tab4_x,tab2_x, 0, 0);
				}
				break;
			case 2:
				if (lastIndex == 0) {
					animation = new TranslateAnimation( tab1_x,tab3_x, 0, 0);
				}else if (lastIndex == 1) {
					animation = new TranslateAnimation( tab2_x,tab3_x, 0,0);
				} else if (lastIndex == 3) {
					animation = new TranslateAnimation( tab4_x,tab3_x, 0, 0);
				}
				break;
			case 3:
				if (lastIndex == 0) {
					animation = new TranslateAnimation( tab1_x,tab4_x, 0, 0);
				}else if (lastIndex == 1) {
					animation = new TranslateAnimation( tab2_x,tab4_x, 0, 0);
				}else if (lastIndex == 2) {
					animation = new TranslateAnimation( tab3_x,tab4_x, 0,0);
				}
				break;
			}
			animation.setDuration(30);
			animation.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.overshoot_interpolator));
 			animation.setFillAfter(true);
 			animation.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.anim.decelerate_interpolator));
			top_bar_select.startAnimation(animation);
		}
		//初始切换面板时进行数据加载
		if(currentIndex==1){
			if(recentContent.isOpenRefreshData){
				//加载最近通话记录
				boolean SP_ALIYUN_RECRECENT_MESSAGE=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_RECRECENT_MESSAGE,true);
				if(SP_ALIYUN_RECRECENT_MESSAGE){
					final CheckBox cb=new CheckBox(this);
					cb.setText("不再提示");
					new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(false)
					.setMessage("应用将读取本地通话记录，以便您快速呼叫通话记录中的非通讯录联系人并对相关通话进行录音保全，我们不会保存和修改您通话记录中的任何信息。是否允许读取？")
					.setView(cb)
					.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(cb.isChecked()){
								getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_RECRECENT_MESSAGE,false);
							}
							recentContent.loadData(true);
							recentContent.isOpenRefreshData=false;
						}
					}).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							recentContent.isOpenRefreshData=false;
						}
					}).show();
				}else{
					recentContent.loadData(true);
					recentContent.isOpenRefreshData=false;
				}
			}
		}else if(currentIndex==2){
			if(contactContent.isOpenRefreshData){
				//加载通讯录
				boolean SP_ALIYUN_READCONTACT_MESSAGE=getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_READCONTACT_MESSAGE,true);
				if(SP_ALIYUN_READCONTACT_MESSAGE){
					final CheckBox cb=new CheckBox(this);
					cb.setText("不再提示");
					new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setCancelable(false)
					.setMessage("应用将读取手机本地通讯录，以便您通过客户端快速呼叫通讯录联系人并根据您的需要通话内容进行录音保全，应用不会保存和针对账号绑定通讯录的任何资料。是否允许读取？")
					.setView(cb)
					.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(cb.isChecked()){
								getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_READCONTACT_MESSAGE,false);
							}
							contactContent.loadData(true);
							contactContent.isOpenRefreshData=false;
						}
					}).setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							contactContent.isOpenRefreshData=false;
						}
					}).show();
				}else{
					contactContent.loadData(true);
					contactContent.isOpenRefreshData=false;
				}
			}
		}else if(currentIndex==3){
			if(recordedManagerContent.isOpenRefreshData){
				//加载录音数据
				recordedManagerContent.loadData(LoadMode.INIT);
//				recordedManagerContent.getRecordedManagerPullListviewData().getOnLoadDataListener().LoadData(LoadMode.INIT);
				recordedManagerContent.isOpenRefreshData=false;
			}
		}
	}

	@Override
	public void onClick(View v) {
		imManager.hideSoftInputFromWindow(contactContent.getEtSearch().getWindowToken(), 0);
		if (v == tab1.getParent()) {
			activity_main_scrolllayout.snapToScreen(0);
		} else if (v == tab2.getParent()) {
			activity_main_scrolllayout.snapToScreen(1);
		} else if (v == tab3.getParent()) {
			activity_main_scrolllayout.snapToScreen(2);
		} else if (v == tab4.getParent()) {
			if(getAppContext().isAuth(Auth.v4recqry1)){
				activity_main_scrolllayout.snapToScreen(3);
				recordedManagerContent.resetSearch();
			}else{
				makeTextShort("暂无权限");
			}
		}else if(v==activity_main_btnnetworktip){
			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		}else if(v==activity_main_ibmore){
			startActivityForResult(new Intent(this, SettingActivity.class), 0);
		}else if(v==activity_main_btnacount){
//			if(getAppContext().isOldUserFlag()){
//				//时长老用户
//				startActivity(new Intent(this,AccountMonthConsumeActivity.class));
//			}else{
//				//新套餐用户隐藏我的账户
//				startActivity(new Intent(this,MyAccountActivity.class));
//			}
			//新套餐用户隐藏我的账户
			startActivity(new Intent(this,AccountActivity.class));
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.onActivityResult(requestCode,resultCode,data);
		}
		if(resultCode== Constant.SETTING_RELOGIN){
			getAppContext().reLogin(this, null);
		}else if(resultCode==Constant.SETTING_QUIT){
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-lastPressTime) > 2000){
	        	makeTextShort("再按一次退出程序");                                
	        	lastPressTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onPause() {
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.onPause();
		}
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.onDestroy();
		}
		for(CoreScrollContent cscontent:CoreScrollContent.getContentcache().values()){
			cscontent.finish();
		}
		super.onDestroy();
	}
	
	public ScrollLayout getMainContentLayout() {
		return activity_main_scrolllayout;
	}

	private void setWelcomeInfo(){
		final String prodid=getAppContext().getUserInfo().get("prodid");
		if(!"".equals(prodid)){
			Map<String,String> requestParams=new HashMap<String,String>();
			requestParams.put("accessid", Constant.ACCESSID);
			requestParams.put("recordno", prodid);
			getAppContext().exeNetRequest(this,Constant.GlobalURL.v4versionDetail,requestParams,null,new UIRunnable(){
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							main_tip_content.setVisibility(View.VISIBLE);
							main_tip_content.setText(getAllInfoContent().get("versioninfo").get("content"));
							
						}
						
					});
				}
			});
		}
	}
	
}