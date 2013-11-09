package com.ancun.yulualiyun;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
import com.ancun.utils.MD5;
import com.ancun.utils.PasswordVerityUtils;
import com.ancun.utils.StringUtils;

/**
 * 修改密码
 * 
 * @author Start
 */
public class ChangePasswordActivity extends CoreActivity implements
		OnClickListener {

	private EditText activity_modifypwd_original, activity_modifypwd_etnew,
			activity_modifypwd_etrenew;
	private Button activity_modifypwd_btnsubmit;

	private InputMethodManager imManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_modifypwd);
		setNavigationTitle(R.string.changepwd_title);

		imManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		activity_modifypwd_original = (EditText) findViewById(R.id.activity_modifypwd_original);
		activity_modifypwd_original.addTextChangedListener(new CustomTextWatcher());

		activity_modifypwd_etnew = (EditText) findViewById(R.id.activity_modifypwd_etnew);
		activity_modifypwd_etnew.addTextChangedListener(new CustomTextWatcher());

		activity_modifypwd_etrenew = (EditText) findViewById(R.id.activity_modifypwd_etrenew);
		activity_modifypwd_etrenew.addTextChangedListener(new CustomTextWatcher());

		activity_modifypwd_btnsubmit = (Button) findViewById(R.id.activity_modifypwd_btnsubmit);
		activity_modifypwd_btnsubmit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String tvOriginalStr = activity_modifypwd_original.getText().toString();
		String tvNewStr = activity_modifypwd_etnew.getText().toString();
		String tvNewreStr = activity_modifypwd_etrenew.getText().toString();
		
		if(StringUtils.isEmpty(tvOriginalStr)){
			makeTextShort("原密码不能为空！");
		} else if (!PasswordVerityUtils.verify(getAppContext(), tvNewStr,"新密码")) {
			
		} else if (!tvNewStr.equals(tvNewreStr)) {
			makeTextShort("两次密码输入不一致");
			activity_modifypwd_etnew.setText("");
			activity_modifypwd_etrenew.setText("");
		} else {
			Map<String, String> requestParams = new HashMap<String, String>();
			requestParams.put("accessid", Constant.ACCESSID);
			requestParams.put("passwordold", MD5.md5(tvOriginalStr.trim()));
			requestParams.put("passwordnew", MD5.md5(tvNewStr.trim()));
			getAppContext().exeNetRequest(this, Constant.GlobalURL.v4pwdModify,
					requestParams, null, new UIRunnable() {

						@Override
						public void run() {
							imManager.hideSoftInputFromWindow(
									activity_modifypwd_original.getWindowToken(), 0);
							imManager.hideSoftInputFromWindow(
									activity_modifypwd_etnew.getWindowToken(),0);
							imManager.hideSoftInputFromWindow(
									activity_modifypwd_etrenew.getWindowToken(),0);
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									new AlertDialog.Builder(ChangePasswordActivity.this)
									.setMessage("密码修改成功啦，亲如此有防范风险意识真的很值得大家学习哦~")
									.setCancelable(false)
									.setPositiveButton("确定", new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											finish();
										}
									}).show();
								}
							});
							
							
						}

					});
		}
	}

	private class CustomTextWatcher implements TextWatcher {

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void afterTextChanged(Editable s) {
			if (!StringUtils.isEmpty(activity_modifypwd_original.getText().toString())
					&&!StringUtils.isEmpty(activity_modifypwd_etnew.getText().toString())
					&&!StringUtils.isEmpty(activity_modifypwd_etrenew.getText().toString())) {
				activity_modifypwd_btnsubmit.setBackgroundResource(R.drawable.register_button_selector);
				activity_modifypwd_btnsubmit.setTextColor(Color.BLACK);
				activity_modifypwd_btnsubmit.setEnabled(true);
			} else {
				activity_modifypwd_btnsubmit.setBackgroundResource(R.drawable.register_button_not_available);
				activity_modifypwd_btnsubmit.setTextColor(Color.GRAY);
				activity_modifypwd_btnsubmit.setEnabled(false);
			}
			
		}

	}

}