package com.ancun.yulualiyun;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.UIRunnable;
/**
 * 意见反馈
 * @author Start
 */
public class FeedbackActivity extends CoreActivity  {

	private Button activity_feedback_submit;
	private EditText activity_feedback_content,activity_feedback_contact;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
		setNavigationTitle(R.string.feedback_title);
        //反馈内容
		activity_feedback_content =(EditText)findViewById(R.id.activity_feedback_content);
		activity_feedback_content.addTextChangedListener(new CustomTextWatcher());
        //联系方式
		activity_feedback_contact =(EditText)findViewById(R.id.activity_feedback_contact);
		activity_feedback_contact.addTextChangedListener(new CustomTextWatcher());
		//内容提交
		activity_feedback_submit =(Button)findViewById(R.id.activity_feedback_submit);
		activity_feedback_submit.setOnClickListener(new View.OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("accessid", Constant.ACCESSID);
				requestParams.put("feedcontent",String.valueOf(activity_feedback_content.getText()));
				requestParams.put("email", String.valueOf(activity_feedback_contact.getText()));
				getAppContext().exeNetRequest(FeedbackActivity.this,Constant.GlobalURL.v4Feedback,requestParams,null,new UIRunnable() {
					
					@Override
					public void run() {
						makeTextLong("反馈成功");
						finish();
					}
					
				});
			}
		});
		
    }
    
	private class CustomTextWatcher implements TextWatcher {

		public void beforeTextChanged(CharSequence s, int start, int count,int after) {
			
		}

		public void afterTextChanged(Editable s) {
			
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (start + count > 0) {
				activity_feedback_submit.setBackgroundResource(R.drawable.register_button_selector);
				activity_feedback_submit.setTextColor(Color.BLACK);
				activity_feedback_submit.setEnabled(true);
			} else {
				activity_feedback_submit.setBackgroundResource(R.drawable.register_button_not_available);
				activity_feedback_submit.setTextColor(Color.GRAY);
				activity_feedback_submit.setEnabled(false);
			}
		}

	}
    
}