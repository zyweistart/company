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

public class ForgetpwdActivity extends CoreActivity implements OnClickListener {

	private static final int SUCCESS = 0xAC1111;
	private static final int EXIT = 0xAC1112;

	public static final int STATUS_NUM_MODULE = 1;
	public static final int STATUS_CODE_MODULE = 2;
	public static final int STATUS_PASSWORD_MODULE = 3;
	public static final int STATUS_END_MODULE = 4;

	String leftTime = "秒后 可重新获取验证码";
	int leftSecond = 60;
	boolean isCountRun = false;
	/**
	 * 下一屏注册状态
	 */
	private int nextStatusModule = STATUS_CODE_MODULE;

	private int currentStatusModule = STATUS_NUM_MODULE;

	private Button btnRegisterNextStep = null;
	/**
	 * 第一屏输入手机号
	 */
	private LinearLayout linearLayoutMobileNumModule = null;
	/**
	 * 第一屏输入验证码
	 */
	private LinearLayout linearLayoutVerifyCodeModule = null;
	/**
	 * 第一屏设置密码
	 */
	private LinearLayout linearLayoutPasswordModule = null;

	private LinearLayout linearLayoutRegisterEndModule = null;

	private TextView tvVerifyCodeTitle = null;
	private TextView tvSetPasswordTitle = null;
	private TextView tvRegisterEndTitle = null;

	private ImageView ivVerifyCodeOK = null;
	private ImageView ivSetPasswordOK=null;

	private TextView tvTimeToReget = null;
	private Button btnReget = null;

	private Button btnTitleBack = null;
	// 手机号码
	private String g_Mobile = null;
	// 验证码
	private String g_VerifyCode = null;

	private CheckBox cbAgreement = null;
	private TextView tvAgreement = null;

	private TextView tvEndModuleTextView1 = null;
	private TextView tvEndModuleMyMobileNum = null;

	// 输入手机号的edittext
	private EditText etMobileNum;
	private EditText etVerifyCode;
	private EditText etPassword;
	private EditText etRePassword;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgetpwd);

		setNavigationTitle(R.string.forgetpwd_title);

		// 初始化注册界面
		btnRegisterNextStep = (Button) findViewById(R.id.btnRegisterNextStep);
		btnRegisterNextStep.setOnClickListener(this);

		linearLayoutRegisterEndModule = (LinearLayout) findViewById(R.id.appr_linearlayout_register_end_module);

		linearLayoutMobileNumModule = (LinearLayout) findViewById(R.id.appr_linearlayout_mobile_num_module);
		linearLayoutVerifyCodeModule = (LinearLayout) findViewById(R.id.appr_linearlayout_verify_code_module);
		linearLayoutPasswordModule = (LinearLayout) findViewById(R.id.appr_linearlayout_password_module);

		tvVerifyCodeTitle = (TextView) findViewById(R.id.appr_textview_verify_code_title);
		tvSetPasswordTitle = (TextView) findViewById(R.id.appr_textview_set_password_title);
		tvRegisterEndTitle = (TextView) findViewById(R.id.appr_textview_register_end_title);

		ivVerifyCodeOK = (ImageView) findViewById(R.id.appr_imageview_verify_code_ok);
		ivSetPasswordOK=(ImageView)findViewById(R.id.appr_imageview_set_password_ok);

		tvTimeToReget = (TextView) findViewById(R.id.appr_textview_time_to_reget);
		btnReget = (Button) findViewById(R.id.appr_btn_reget);
		btnReget.setOnClickListener(this);

		tvEndModuleTextView1 = (TextView) findViewById(R.id.appr_end_module_textview_msg);
		tvEndModuleTextView1.setText("新密码设置成功");

		tvEndModuleMyMobileNum = (TextView) findViewById(R.id.appr_my_mobile_num);
		tvEndModuleMyMobileNum.setText("");

		// 不显示同意协议
		cbAgreement = (CheckBox) findViewById(R.id.appr_checkbox_readProtocol);
		cbAgreement.setVisibility(View.GONE);
		tvAgreement = (TextView) findViewById(R.id.appr_textview_protocol);
		tvAgreement.setVisibility(View.GONE);

		btnTitleBack = (Button) findViewById(R.id.common_title_btn_left);
		btnTitleBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sendEmptyMessage(EXIT);
			}
		});
		setCurrRegisterView(STATUS_NUM_MODULE);

		// 添加edittext TextWatcher响应事件
		etMobileNum = (EditText) findViewById(R.id.appr_edittext_mobile_num);
		etMobileNum.addTextChangedListener(new CustomTextWatcher(etMobileNum));
		// 本机号 设为验证号码
		if (etMobileNum.getText().length() < 1) {
			String strAccount = CommonFn.getPhoneNumber(this);
			if (!Constant.EMPTYSTR.equals(strAccount)) {
				etMobileNum.setText(strAccount);
				etMobileNum.setTextColor(Color.BLACK);
				setSubmitBtnAble(true);
			}
		}

		etVerifyCode = (EditText) findViewById(R.id.appr_edittext_verify_code);
		etVerifyCode
				.addTextChangedListener(new CustomTextWatcher(etVerifyCode));

		etPassword = (EditText) findViewById(R.id.appr_edittext_password);
		etPassword.addTextChangedListener(new CustomTextWatcher(etPassword));
		etPassword.setHint("请输入新密码");
		// 失去焦点判断密码是否与账户相同
		etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (v.hasFocus() == false) {
					if (etMobileNum.getText().toString()
							.equals(etPassword.getText().toString())) {
						makeTextShort("为了您的账号安全，密码不能与账号相同");
						// etPassword.setSelection(etPassword.getText().toString().length());
					}
				}
			}
		});

		etRePassword = (EditText) findViewById(R.id.appr_edittext_rePassword);
		etRePassword
				.addTextChangedListener(new CustomTextWatcher(etRePassword));
		etRePassword.setHint("请再次输入密码");
	}

	/**
	 * 设置当前注册界面视图
	 */
	public void setCurrRegisterView(int nextStatusModule) {
		Resources rs = this.getResources();
		switch (nextStatusModule) {
		case STATUS_NUM_MODULE:
			this.currentStatusModule = STATUS_NUM_MODULE;

			tvVerifyCodeTitle.setTextColor(rs.getColor(R.color.black));
			tvSetPasswordTitle.setTextColor(rs
					.getColor(R.color.darkgray_regist));
			tvRegisterEndTitle.setTextColor(rs
					.getColor(R.color.darkgray_regist));

			setSubmitBtnAble(false);
			btnRegisterNextStep.setText(R.string.activity_getverifycode);
			break;
		case STATUS_CODE_MODULE:
			linearLayoutMobileNumModule.setVisibility(View.GONE);
			linearLayoutVerifyCodeModule.setVisibility(View.VISIBLE);
			linearLayoutPasswordModule.setVisibility(View.GONE);
			linearLayoutRegisterEndModule.setVisibility(View.GONE);
			this.currentStatusModule = STATUS_CODE_MODULE;
			this.nextStatusModule = STATUS_PASSWORD_MODULE;

			tvVerifyCodeTitle.setTextColor(rs.getColor(R.color.black));
			tvSetPasswordTitle.setTextColor(rs
					.getColor(R.color.darkgray_regist));
			tvRegisterEndTitle.setTextColor(rs
					.getColor(R.color.darkgray_regist));

			setSubmitBtnAble(false);

			btnRegisterNextStep.setText("提交验证码");

			getVerifityCode();
			break;
		case STATUS_PASSWORD_MODULE:
			linearLayoutMobileNumModule.setVisibility(View.GONE);
			linearLayoutVerifyCodeModule.setVisibility(View.GONE);
			linearLayoutPasswordModule.setVisibility(View.VISIBLE);
			linearLayoutRegisterEndModule.setVisibility(View.GONE);
			this.currentStatusModule = STATUS_PASSWORD_MODULE;
			this.nextStatusModule = STATUS_END_MODULE;

			ivVerifyCodeOK.setVisibility(View.VISIBLE);
			tvVerifyCodeTitle.setTextColor(rs.getColor(R.color.darkgray_regist));
			tvSetPasswordTitle.setTextColor(rs.getColor(R.color.black));
			tvRegisterEndTitle.setTextColor(rs.getColor(R.color.darkgray_regist));

			setSubmitBtnAble(false);

			btnRegisterNextStep.setText("提交密码");
			break;
		case STATUS_END_MODULE:
			linearLayoutMobileNumModule.setVisibility(View.GONE);
			linearLayoutVerifyCodeModule.setVisibility(View.GONE);
			linearLayoutPasswordModule.setVisibility(View.GONE);
			linearLayoutRegisterEndModule.setVisibility(View.VISIBLE);
			this.currentStatusModule = STATUS_END_MODULE;

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
		if (NetConnectManager.isNetWorkAvailable(this)) {
			if (v == btnRegisterNextStep) {
				switch (this.currentStatusModule) {
				case STATUS_NUM_MODULE:
					g_Mobile = etMobileNum.getText().toString();
					if ("".equals(g_Mobile)) {
						makeTextLong("手机号码不能为空");
						return;
					} else {
						setCurrRegisterView(nextStatusModule);
					}
					break;
				case STATUS_CODE_MODULE:
					g_VerifyCode = etVerifyCode.getText().toString();
					if ("".equals(g_VerifyCode)) {
						makeTextLong("验证码不能为空");
					} else {
						Map<String,String> requestParams=new HashMap<String,String>();
						requestParams.put("phone", g_Mobile);
						requestParams.put("authcode", g_VerifyCode);
						// 1:注册;2:密码找回
						requestParams.put("actype", "2");
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
						requestParams.put("phone", g_Mobile);
						requestParams.put("password", MD5.md5(password));
						requestParams.put("authcode", g_VerifyCode);
						// 8:
						// 400电话语录网站（www.4000951335.com）;9:400电话语录Android客户端
						requestParams.put("operatesource", "h");
						requestParams.put("ip", "");
						requestParams.put("mac", "");
						requestParams.put("loginflag", "1");
						Map<String,String> headerParams=new HashMap<String,String>();
						headerParams.put("sign", "");
						getAppContext().exeNetRequest(this,Constant.GlobalURL.v4pwdReset,requestParams,headerParams,new UIRunnable() {
							@Override
							public void run() {
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
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
					Intent loginIntent = new Intent(this,MainActivity.class);
					startActivity(loginIntent);
					finish();
					break;
				}
			} else if (v == btnReget) {// 重新获取验证码
				getVerifityCode();
			}
		} else {
			CommonFn.settingNetwork(this);
		}
	}

	/**
	 * 获取短信校验码
	 */
	public void getVerifityCode() {
		leftSecond = 60;
		isCountRun = true;
		btnReget.setVisibility(View.GONE);
		tvTimeToReget.setVisibility(View.VISIBLE);
		final ProgressDialog dialog = CommonFn.progressDialog(
				ForgetpwdActivity.this, "正在获取验证码...");
		dialog.show();
		new Thread() {
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("phone", g_Mobile);
					// 1:注册;2:密码找回
					params.put("actype", "2");
					String requestContent = XMLUtils.builderRequestXml(
							Constant.GlobalURL.v4scodeGet, params);
					// 解析XML内容
					String xmlContent=HttpUtils.requestServerByXmlContent(null, requestContent);
					final Map<String, Map<String, String>> mapXML=XMLUtils.xmlResolve(xmlContent);
					dialog.dismiss();
					Map<String, String> infoHead = mapXML
							.get(Constant.RequestXmLConstant.INFO);
					if (infoHead.get(Constant.RequestXmLConstant.CODE).equals(
							Constant.RequestXmLConstant.SUCCESSCODE)) {

					} else if (infoHead.get(Constant.RequestXmLConstant.CODE)
							.equals("120020")) {
						// 用户不存在
					} else {
						// 号码已经存在或有误
						makeTextLong(infoHead
								.get(Constant.RequestXmLConstant.MSG));
					}
				} catch (Exception e) {
					dialog.dismiss();
					makeTextLong("糟糕，好像出错了，请稍候再试！");
				}
				while (isCountRun) {
					try {
						Thread.sleep(1000);
						sendEmptyMessage(SUCCESS);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 提醒是否退出注册
			sendEmptyMessage(EXIT);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public void processMessage(final Message msg) {
		switch (msg.what) {
		case SUCCESS:
			tvTimeToReget.setText(leftSecond + leftTime);
			leftSecond--;
			if (leftSecond == 0) {
				isCountRun = false;
				tvTimeToReget.setVisibility(View.GONE);
				btnReget.setVisibility(View.VISIBLE);
			}
			break;
		case EXIT:
			// 提示是否退出注册
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					ForgetpwdActivity.this);
			dialog.setTitle("安存语录密码找回")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setMessage("你确定要退出本次密码找回吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 结束当前窗口
									finish();
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();// 取消弹出框
								}
							}).create().show();
			break;
		}
	}

	// edittext TextWatcher 监听事件
	private class CustomTextWatcher implements TextWatcher {
		private EditText mEditText;

		public CustomTextWatcher(EditText e) {
			mEditText = e;
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		public void afterTextChanged(Editable s) {
			if (s.length() > 0) {
				if (mEditText == etMobileNum) {// 注册手机号码
					if (s.length() != 11) {
						// makeTextShort("请输入正确的11位手机号码");
						setSubmitBtnAble(false);
					} else {
						// 设置提交按钮可用
						setSubmitBtnAble(true);
					}

				} else if (mEditText == etVerifyCode) {// 短信验证码
					if (s.length() != 6) {
						// makeTextShort("请输入正确的6位短信验证码");
						// 设置提交按钮不可用
						setSubmitBtnAble(false);
					} else {
						// 设置提交按钮可用
						setSubmitBtnAble(true);
					}
				} else if (mEditText == etRePassword) {// 确认密码
					if (s.length() > 0) {
						// 两次密码相同时提交按钮显示为可点击状态
						// Log.i(etPassword.getText().toString()+"bb"+s.toString(),
						// "bbb");
						if (etPassword.getText().toString()
								.equals(s.toString())) {
							// Log.i(etPassword.getText().toString()+"bb"+s.toString(),
							// "aaaaa");
							setSubmitBtnAble(true);
						} else {
							// 设置提交按钮不可用
							setSubmitBtnAble(false);
						}
						if (s.length() == etPassword.getText().length()) {
							if (!etPassword.getText().toString()
									.equals(s.toString())) {
								// 密码和确认密码长度相等时的判断
								makeTextShort("两次输入的密码不同");
							}
						} else if (s.length() > etPassword.getText().length()) {
							// 确认密码长度大于密码长度
							makeTextShort("输入确认密码错误");
						}
					}
				}

			} else {
				// 设置提交按钮不可用
				setSubmitBtnAble(false);
			}
		}
	}

	// 设置提交按钮是否可用
	public void setSubmitBtnAble(boolean isAble) {
		// 设置提交按钮的背景颜色 文字颜色和 可点击
		if (isAble) {
			btnRegisterNextStep
					.setBackgroundResource(R.drawable.register_button_selector);
			btnRegisterNextStep.setTextColor(getResources().getColor(
					R.color.register_btn_word_color));
			// btnRegisterNextStep.setEnabled(true);
		} else {
			btnRegisterNextStep
					.setBackgroundResource(R.drawable.register_button_not_available);
			btnRegisterNextStep.setTextColor(getResources().getColor(
					R.color.register_btn_disable_word_color));
			// btnRegisterNextStep.setEnabled(false);
		}

	}

}