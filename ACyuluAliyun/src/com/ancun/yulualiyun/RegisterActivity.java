package com.ancun.yulualiyun;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.utils.CommonFn;
import com.ancun.utils.HttpUtils;
import com.ancun.utils.MD5;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.PasswordVerityUtils;
import com.ancun.utils.StringUtils;
import com.ancun.utils.XMLUtils;

public class RegisterActivity extends CoreActivity implements OnClickListener {

	private static final int SUCCESS=0xAC1111;
	private static final int EXIT=0xAC1112;

	private static final int STATUS_NUM_MODULE = 1;
	private static final int STATUS_CODE_MODULE = 2;
	private static final int STATUS_PASSWORD_MODULE = 3;
	private static final int STATUS_END_MODULE = 4;

	private String leftTime="秒后 可重新获取验证码";
	private int leftSecond=60;
	private boolean isCountRun=false;
	
	private int  nextStatusModule=STATUS_CODE_MODULE;

	private int currentStatusModule=STATUS_NUM_MODULE;

	private Button btnRegisterNextStep=null;
	
	private LinearLayout linearLayoutMobileNumModule=null;
	private LinearLayout linearLayoutVerifyCodeModule=null;
	private LinearLayout linearLayoutPasswordModule=null;
	private LinearLayout linearLayoutRegisterEndModule=null;

	private TextView appr_textview_protocol=null;
	
	private TextView tvVerifyCodeTitle=null;
	private TextView tvSetPasswordTitle=null;
	private TextView tvRegisterEndTitle=null;

	private ImageView ivVerifyCodeOK=null;
	private ImageView ivSetPasswordOK=null;

	private TextView tvTimeToReget=null;
	private Button btnReget=null;

	private Button btnTitleBack=null;
	//手机号码
	private String g_Mobile=null;
	//验证码
	private String g_VerifyCode=null;
	//输入手机号的edittext
	private EditText etMobileNum ;
	private EditText etVerifyCode ;
	private EditText etPassword ;
	private EditText etRePassword ;
	
	private TextView appr_end_module_tipmsg;
	
	public static final String ACTIVATION_FLAG="ACTIVATION_FLAG";

	private Bundle bundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		setNavigationTitle(R.string.register_title);
		
		bundle=getIntent().getExtras();
		if(bundle!=null){
			if(bundle.getBoolean(ACTIVATION_FLAG,false)){
				//设置返回值
				setResult(ActivationAccountActivity.RESULT_CODE_REGISTER);
			}
		}
		
		//初始化注册界面
		btnRegisterNextStep=(Button)findViewById(R.id.btnRegisterNextStep);
		btnRegisterNextStep.setOnClickListener(this);

		linearLayoutMobileNumModule=(LinearLayout)findViewById(R.id.appr_linearlayout_mobile_num_module);
		linearLayoutVerifyCodeModule=(LinearLayout)findViewById(R.id.appr_linearlayout_verify_code_module);
		linearLayoutPasswordModule=(LinearLayout)findViewById(R.id.appr_linearlayout_password_module);
		linearLayoutRegisterEndModule=(LinearLayout)findViewById(R.id.appr_linearlayout_register_end_module);

		appr_textview_protocol=(TextView)findViewById(R.id.appr_textview_protocol);
		appr_textview_protocol.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(RegisterActivity.this,ServerClauseActivity.class);
				startActivity(intent);
			}
		});
		
		tvVerifyCodeTitle=(TextView)findViewById(R.id.appr_textview_verify_code_title);
		tvSetPasswordTitle=(TextView)findViewById(R.id.appr_textview_set_password_title);
		tvRegisterEndTitle=(TextView)findViewById(R.id.appr_textview_register_end_title);

		ivVerifyCodeOK=(ImageView)findViewById(R.id.appr_imageview_verify_code_ok);
		ivSetPasswordOK=(ImageView)findViewById(R.id.appr_imageview_set_password_ok);

		tvTimeToReget=(TextView)findViewById(R.id.appr_textview_time_to_reget);
		btnReget=(Button)findViewById(R.id.appr_btn_reget);
		btnReget.setOnClickListener(this);

		btnTitleBack=(Button)findViewById(R.id.common_title_btn_left);
		btnTitleBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendEmptyMessage(EXIT);
			}
		});
		
		setCurrRegisterView(currentStatusModule);

		//添加edittext  TextWatcher响应事件
		etMobileNum =(EditText)findViewById(R.id.appr_edittext_mobile_num);
		etMobileNum.addTextChangedListener(new CustomTextWatcher(etMobileNum));
		//本机号 设为验证号码
		if (etMobileNum.getText().length()<1) {
			String strAccount=CommonFn.getPhoneNumber(this);
			if(!Constant.EMPTYSTR.equals(strAccount)){
				etMobileNum.setText(strAccount);
				etMobileNum.setTextColor(Color.BLACK );
				//设置提交按钮可用
				setSubmitBtnAble(true);
			}
		}

		etVerifyCode=(EditText)findViewById(R.id.appr_edittext_verify_code);
		etVerifyCode.addTextChangedListener(new CustomTextWatcher(etVerifyCode));

		etPassword=(EditText)findViewById(R.id.appr_edittext_password);
		etPassword.addTextChangedListener(new CustomTextWatcher(etPassword));
		//失去焦点判断密码是否与账户相同
		etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) { 
				if(v.hasFocus()==false){  
					if(etMobileNum.getText().toString().equals(etPassword.getText().toString())){	 
						makeTextShort("为了您的账号安全，密码不能与账号相同");
						//etPassword.setSelection(etPassword.getText().toString().length());
					}
				}
			}
		});
		etRePassword=(EditText)findViewById(R.id.appr_edittext_rePassword);
		etRePassword.addTextChangedListener(new CustomTextWatcher(etRePassword));
		appr_end_module_tipmsg=(TextView)findViewById(R.id.appr_end_module_tipmsg);
	}
	/**
	 * 设置当前注册界面视图
	 */
	public void setCurrRegisterView(int nextStatusModule){
		Resources rs = this.getResources(); 
		switch (nextStatusModule) {
		case STATUS_NUM_MODULE:
			this.currentStatusModule=STATUS_NUM_MODULE;

			tvVerifyCodeTitle.setTextColor(rs.getColor(R.color.black));
			tvSetPasswordTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			tvRegisterEndTitle.setTextColor(rs.getColor(R.color.darkgray_regist));

			//设置提交按钮不可用
			setSubmitBtnAble(false);
			btnRegisterNextStep.setText(R.string.activity_getverifycode);
			break;
		case STATUS_CODE_MODULE:
			linearLayoutMobileNumModule.setVisibility(View.GONE);
			linearLayoutVerifyCodeModule.setVisibility(View.VISIBLE);
			linearLayoutPasswordModule.setVisibility(View.GONE);
			linearLayoutRegisterEndModule.setVisibility(View.GONE);
			this.currentStatusModule=STATUS_CODE_MODULE;
			this.nextStatusModule=STATUS_PASSWORD_MODULE;

			tvVerifyCodeTitle.setTextColor(rs.getColor(R.color.black));
			tvSetPasswordTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			tvRegisterEndTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			//设置提交按钮不可用
			setSubmitBtnAble(false);
			btnRegisterNextStep.setText("提交验证码");

			getVerifityCode();
			break;
		case STATUS_PASSWORD_MODULE:
			linearLayoutMobileNumModule.setVisibility(View.GONE);
			linearLayoutVerifyCodeModule.setVisibility(View.GONE);
			linearLayoutPasswordModule.setVisibility(View.VISIBLE);
			linearLayoutRegisterEndModule.setVisibility(View.GONE);
			this.currentStatusModule=STATUS_PASSWORD_MODULE;
			this.nextStatusModule=STATUS_END_MODULE;

			ivVerifyCodeOK.setVisibility(View.VISIBLE);
			tvVerifyCodeTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			tvSetPasswordTitle.setTextColor(rs.getColor(R.color.black));
			tvRegisterEndTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			//设置提交按钮不可用
			setSubmitBtnAble(false);
			btnRegisterNextStep.setText("提交密码");
			break;
		case STATUS_END_MODULE:
//			TextView tvMobile=(TextView)findViewById(R.id.appr_my_mobile_num);
//			tvMobile.setText(g_Mobile);
			linearLayoutMobileNumModule.setVisibility(View.GONE);
			linearLayoutVerifyCodeModule.setVisibility(View.GONE);
			linearLayoutPasswordModule.setVisibility(View.GONE);
			linearLayoutRegisterEndModule.setVisibility(View.VISIBLE);
			this.currentStatusModule=STATUS_END_MODULE;

			ivVerifyCodeOK.setVisibility(View.VISIBLE);
			ivSetPasswordOK.setVisibility(View.VISIBLE);
			tvVerifyCodeTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			tvSetPasswordTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			tvRegisterEndTitle.setTextColor(rs.getColor(R.color.black));
			btnRegisterNextStep.setText("进入体验");
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if(NetConnectManager.isNetWorkAvailable(this)){
			if(v==btnRegisterNextStep){
				switch (this.currentStatusModule) {
				case STATUS_NUM_MODULE:
					g_Mobile=etMobileNum.getText().toString();
					if("".equals(g_Mobile)){
						makeTextShort("手机号不能为空！");
					}else if(g_Mobile.length()!=11){
						makeTextShort("请输入正确的 11 位手机号码");
					}else if(!((CheckBox)findViewById(R.id.appr_checkbox_readProtocol)).isChecked()){
						makeTextShort("请先阅读并同意《安存语录服务条款》");
					}else{
						setCurrRegisterView(nextStatusModule);
					}
					break;
				case STATUS_CODE_MODULE:
					g_VerifyCode=etVerifyCode.getText().toString();
					if("".equals(g_VerifyCode)){
						makeTextShort("验证码不能为空");
					}else{
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("phone", g_Mobile);
						requestParams.put("authcode", g_VerifyCode);
						requestParams.put("actype", "1");
						Map<String,String> headerParams=new HashMap<String,String>();
						headerParams.put("sign", "");
						getAppContext().exeNetRequest(this,Constant.GlobalURL.v4scodeVer,requestParams,headerParams,new UIRunnable() {
							
							@Override
							public void run() {
								
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										setCurrRegisterView(nextStatusModule);
									}
									
								});
								
							}
							
						});
					}
					break;
				case STATUS_PASSWORD_MODULE:
					final String password=etPassword.getText().toString();
					final String rePassword=etRePassword.getText().toString();
					
					if (!PasswordVerityUtils.verify(getAppContext(), password,"密码")) {
						
					} else if (!password.equals(rePassword)) {
						makeTextShort("两次密码输入不一致");
						etPassword.setText("");
						etRePassword.setText("");
					} else {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("phone",g_Mobile);
						requestParams.put("password", MD5.md5(password));
						requestParams.put("authcode", g_VerifyCode);
						requestParams.put("signupsource","h");
						requestParams.put("ip", "");
						requestParams.put("mac", "");
						requestParams.put("idcode", getAppContext().getYunOSAPI().getUUID());
						requestParams.put("loginflag", "1");
						
						//用户自主注册开通流程
						requestParams.put("prodid", "yunos02");
						if(bundle!=null){
							if(bundle.getBoolean(ACTIVATION_FLAG,false)){
								//阿里云OS激活赠送用户
								requestParams.put("prodid", "yunos01");
							}
						}
						
						Map<String,String> headerParams=new HashMap<String,String>();
						headerParams.put("sign", "");
						getAppContext().exeNetRequest(this,Constant.GlobalURL.v4Signup,requestParams,headerParams,new UIRunnable() {
							
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										
										if(bundle!=null){
											if(bundle.getBoolean(ACTIVATION_FLAG,false)){
												//注册成功后如果为赠送注册则：1.通知OS服务端2.初始设置标记设为TRUE表示下次启动不再检测3.显示注册成功获赠文案
												getAppContext().getYunOSAPI().notifyOSService();
												//赠送激活成功初始设置标记
												getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_ALIYUN_INIT_SET,true);
												appr_end_module_tipmsg.setVisibility(View.VISIBLE);
											}
										}
										
										getAppContext().buildAuth(getResponseContent());
										getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_ACCOUNT,g_Mobile);
										try {
											getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_PASSWORD,StringUtils.doKeyEncrypt(password,getAssets().open(Constant.DESKEYKEY)));
										} catch (IOException e) {
											e.printStackTrace();
											getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_PASSWORD,Constant.EMPTYSTR);
										}
										getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_AUTOLOGIN,true);
										getAppContext().setUserInfoAll(getAllInfoContent());
										getAppContext().setUserInfo(getAllInfoContent().get("v4info"));
										Constant.ACCESSID=getAppContext().getUserInfo().get("accessid");
										Constant.ACCESSKEY=getAppContext().getUserInfo().get("accesskey");
										//注册成功
										setCurrRegisterView(nextStatusModule);
									}
								});
								
							}
							
						});

					}
					break;
				case STATUS_END_MODULE:
					if(bundle!=null){
						if(bundle.getBoolean(ACTIVATION_FLAG,false)){
							finish();
							return;
						}
					}
					Intent loginIntent=new Intent(RegisterActivity.this,MainActivity.class);
					startActivity(loginIntent);
					finish();
					break;
				}
			}else if (v==btnReget) {
				//重新获取验证码
				getVerifityCode();
			}
		}else{
			CommonFn.settingNetwork(this);
		}
	}
	/**
	 * 获取短信校验码
	 */
	public void getVerifityCode(){
		leftSecond=60;
		isCountRun=true;
		btnReget.setVisibility(View.GONE);
		tvTimeToReget.setVisibility(View.VISIBLE);
		final ProgressDialog dialog =CommonFn.progressDialog(RegisterActivity.this, "获取验证码...");
		dialog.show();
		new Thread(){
			public void run() {
				try{
					Map<String,String> params=new HashMap<String,String>();
					params.put("phone", g_Mobile);
					params.put("actype", "1");
					String requestContent = XMLUtils.builderRequestXml(Constant.GlobalURL.v4scodeGet, params);
					String xmlContent=HttpUtils.requestServerByXmlContent(null, requestContent);
					final Map<String, Map<String, String>> mapXML=XMLUtils.xmlResolve(xmlContent);
					dialog.dismiss();
					Map<String,String> infoHead=mapXML.get(Constant.RequestXmLConstant.INFO);
					if(infoHead.get(Constant.RequestXmLConstant.CODE).equals(Constant.RequestXmLConstant.SUCCESSCODE)){
						
					}else if(infoHead.get(Constant.RequestXmLConstant.CODE).equals("120169")){
						//号码已存在则暂时不提示
					}else{
						//号码已经存在或有误
						makeTextLong(infoHead.get(Constant.RequestXmLConstant.MSG));
					}
				}catch(Exception e){
					dialog.dismiss();
					makeTextLong("糟糕，好像出错了，请稍候再试！");
				}
				while(isCountRun){
					try{
						Thread.sleep(1000);
						sendEmptyMessage(SUCCESS);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			//提醒是否退出注册
			sendEmptyMessage(EXIT);
			return true;
		}else{		
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void processMessage(final Message msg) {
		switch (msg.what) {
		case SUCCESS: 
			tvTimeToReget.setText(leftSecond+leftTime); 
			leftSecond--;  
			if (leftSecond==0) {
				isCountRun=false;
				tvTimeToReget.setVisibility(View.GONE);
				btnReget.setVisibility(View.VISIBLE);
			}
			break;
		case EXIT:
			//提示是否退出注册
			AlertDialog.Builder dialog=new AlertDialog.Builder(RegisterActivity.this);  
			dialog.
			setTitle("安存语录注册").
			setIcon(android.R.drawable.ic_dialog_info).
			setMessage("你确定要退出本次注册吗？").
			setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				@Override  
				public void onClick(DialogInterface dialog, int which) {  
					//结束当前窗口
					finish();
				}  
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {  
				@Override  
				public void onClick(DialogInterface dialog, int which) {  
					dialog.cancel();//取消弹出框  
				}  
			}).create().show();
			break;
		}
	} 
	//edittext  TextWatcher 监听事件
	private class CustomTextWatcher implements TextWatcher {
		
		private EditText mEditText;
		
		public CustomTextWatcher(EditText e) {
			mEditText = e;
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

		public void afterTextChanged(Editable s) {
			if (s.length() >0) {
				if (mEditText==etMobileNum) {//注册手机号码  
					if (s.length()>11) {
						//设置提交按钮不可用
						setSubmitBtnAble(false);
					}else {
						//设置提交按钮可用
						setSubmitBtnAble(true);
					}
				}else if (mEditText==etVerifyCode) {//短信验证码 
					if (s.length()!=6) {
						//设置提交按钮不可用
						setSubmitBtnAble(false);
					}else {
						//设置提交按钮可用
						setSubmitBtnAble(true);
					}
				}else if (mEditText==etRePassword) {//确认密码
					if (s.length() >0) {
						//两次密码相同时提交按钮显示为可点击状态
						if (etPassword.getText().toString().equals(s.toString())) {
							setSubmitBtnAble(true);
						}else {
							//设置提交按钮不可用
							setSubmitBtnAble(false);
						}
						if (s.length() ==etPassword.getText().length()) {
							if (!etPassword.getText().toString().equals(s.toString())) {
								//密码和确认密码长度相等时的判断
								makeTextShort("两次输入的密码不同");
							}
						}else if (s.length() > etPassword.getText().length()) {
							// 确认密码长度大于密码长度
							makeTextShort("输入确认密码错误");
						}
					}
				}
			}else {
				//设置提交按钮不可用
				setSubmitBtnAble(false);
			}
		}
	}
	
	//设置提交按钮是否可用
	public void setSubmitBtnAble(boolean isAble) {
		//设置提交按钮的背景颜色 文字颜色和 可点击
		if (isAble) {
			btnRegisterNextStep.setBackgroundResource(R.drawable.register_button_selector);
			btnRegisterNextStep.setTextColor(getResources().getColor(R.color.register_btn_word_color));
			//btnRegisterNextStep.setEnabled(true);
		}else {
			btnRegisterNextStep.setBackgroundResource(R.drawable.register_button_not_available);
			btnRegisterNextStep.setTextColor(getResources().getColor(R.color.register_btn_disable_word_color));
			//btnRegisterNextStep.setEnabled(false);
		}
	}

}