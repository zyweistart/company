package com.ancun.yulualiyun;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.core.Constant.Auth;
import com.ancun.model.UIRunnable;
import com.ancun.utils.TimeUtils;

/**
 * 录音备注
 * @author Start
 */
public class RecordedRemark extends CoreActivity implements OnClickListener {

	private String fileno;

	private Boolean isEdit = false;

	private TextView tvrecorded_remark_calling;
	private TextView tvrecorded_remark_called;
	private TextView tvrecorded_remark_start_time;
	private TextView tvrecorded_remark_end_time;
	private TextView tvrecorded_remark_length;
	private EditText etrecorded_remark_edit;
	private ImageButton btnrecorded_remark_edit_submit;
	private ImageButton btnrecorded_taobao_appeal;
	private ImageButton btnrecorded_notarization_appeal;
	private Integer cerflag;
	private Integer accstatus;
	
	private static final int TAOBAOREQUESTCODE=0xAB001;
	public static final int REMARKRESULTCODE=0xAA001;
	public static final int REMARKMODIFYCODE=0xAA002;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_recorded_remark);
		
		setNavigationTitle(R.string.recordedremark_title);
		
		fileno=getIntent().getExtras().getString("fileno");
		if(fileno!=null){
			tvrecorded_remark_calling=(TextView)findViewById(R.id.recorded_remark_calling);
			tvrecorded_remark_called=(TextView)findViewById(R.id.recorded_remark_called);
			tvrecorded_remark_start_time=(TextView)findViewById(R.id.recorded_remark_start_time);
			tvrecorded_remark_end_time=(TextView)findViewById(R.id.recorded_remark_end_time);
			tvrecorded_remark_length=(TextView)findViewById(R.id.recorded_remark_length);
			etrecorded_remark_edit=(EditText)findViewById(R.id.recorded_remark_edit);
			btnrecorded_remark_edit_submit=(ImageButton)findViewById(R.id.recorded_remark_edit_submit);
			btnrecorded_remark_edit_submit.setOnClickListener(this);
			btnrecorded_taobao_appeal=(ImageButton)findViewById(R.id.recorded_taobao_appeal);
			btnrecorded_taobao_appeal.setOnClickListener(this);
			btnrecorded_notarization_appeal=(ImageButton)findViewById(R.id.recorded_notarization_appeal);
			btnrecorded_notarization_appeal.setOnClickListener(this);
			//获取录音数据
			Map<String,String> requestParams=new HashMap<String,String>();
			requestParams.put("accessid", Constant.ACCESSID);
			requestParams.put("status", "1");
			requestParams.put("fileno",fileno);
			getAppContext().exeNetRequest(this,Constant.GlobalURL.v4recGet,requestParams,null,new UIRunnable() {
				
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							tvrecorded_remark_calling.setText(getInfoContent().get("callerno"));
							tvrecorded_remark_called.setText(getInfoContent().get("calledno"));
							tvrecorded_remark_start_time.setText(getInfoContent().get("begintime"));
							tvrecorded_remark_end_time.setText(getInfoContent().get("endtime"));
							tvrecorded_remark_length.setText(TimeUtils.secondConvertTime(Integer.parseInt((getInfoContent().get("duration")))));
							etrecorded_remark_edit.setText(getInfoContent().get("remark"));
							//申请码状态
							accstatus=Integer.parseInt(getInfoContent().get("accstatus"));
							//公证标记
							cerflag=Integer.parseInt(getInfoContent().get("cerflag"));
							if(cerflag==1){
								btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.recorded_remark_notary_request_selector);
							}else{
								btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.recorded_remark_notary_cancel_selector);
							}
							if(accstatus==1){
								btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.recorded_remark_taobao_lookup_selector);
							}else{
								btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.recorded_remark_taobao_request_selector);
							}
						}
						
					});
					
				}
				
			});
			if (isEdit) {
				setRemarkEditStatus(true);
			}else {
				setRemarkEditStatus(false);
			}
		}
	}

	private void setRemarkEditStatus(Boolean isEditFlag) {
		if (isEditFlag) {//编辑状态
			etrecorded_remark_edit.setEnabled(true);
			etrecorded_remark_edit.setBackgroundResource(R.drawable.recorded_remark_edittext_bg_edit);
			btnrecorded_remark_edit_submit.setBackgroundResource(R.drawable.recorded_remark_submit_selector);
		}else {//查看状态
			etrecorded_remark_edit.setBackgroundResource(R.drawable.recorded_remark_edittext_bg_lookup);
			etrecorded_remark_edit.setEnabled(false);
			btnrecorded_remark_edit_submit.setBackgroundResource(R.drawable.recorded_remark_edit_selector);
		}
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.recorded_remark_edit_submit:
			if(!getAppContext().isAuth(Auth.v4recremark)){
				makeTextShort("暂无权限");
			}else{
				if (isEdit) {//编辑状态
					//关闭软键盘
	 				InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	 				inputMethodManager.hideSoftInputFromWindow(etrecorded_remark_edit.getWindowToken(), 0);

	 				final String remark=etrecorded_remark_edit.getText().toString();
	 				//提交修改备注
	 				Map<String,String> requestParams=new HashMap<String,String>();
	 				requestParams.put("accessid", Constant.ACCESSID);
	 				requestParams.put("fileno",fileno);
	 				requestParams.put("remark", remark);
	 				getAppContext().exeNetRequest(this,Constant.GlobalURL.v4recRemark,requestParams,null,new UIRunnable() {
	 					
	 					@Override
	 					public void run() {
	 						Intent intent=new Intent();
	 						Bundle bundle=new Bundle();
	 						bundle.putString("fileno", fileno);
	 						bundle.putInt("accstatus", accstatus);
	 						bundle.putInt("cerflag", cerflag);
	 						bundle.putString("remark", remark);
	 						intent.putExtras(bundle);
	 						setResult(REMARKMODIFYCODE,intent);
	 						makeTextLong("录音备注修改成功");
	 					}
	 					
	 				});
	 				//设置提交按钮未编辑按钮
					isEdit=false;
					setRemarkEditStatus(false);
				}else {
					//设置编辑按钮为提交按钮
					isEdit=true;
					setRemarkEditStatus(true);
					//打开软键盘
					InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
					if (inputMethodManager.isActive()) {
						inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			}
			break;
		case R.id.recorded_taobao_appeal:
			Bundle bundleTaobao=new Bundle();
			bundleTaobao.putString(RecordedManagerContentListActivity.RECORDED_FILENO, fileno);
			bundleTaobao.putInt(RecordedManagerContentListActivity.RECORDED_ACCSTATUS, accstatus);
			Intent intentTaobao;
			if(accstatus==1){
				//有效
				intentTaobao=new Intent(RecordedRemark.this,RecordedAppealTaobaoExtractionCode.class);
			}else{
				bundleTaobao.putInt("appeal_type", 1);
				intentTaobao=new Intent(RecordedRemark.this,RecordedAppealConfirmActivity.class);
			}
			intentTaobao.putExtras(bundleTaobao);
			startActivityForResult(intentTaobao,TAOBAOREQUESTCODE);
			break;
		case R.id.recorded_notarization_appeal:
			//申请公证
			Bundle bundleNotary=new Bundle();
			bundleNotary.putInt("appeal_type", 2);
			bundleNotary.putString("fileno", fileno);
			bundleNotary.putInt("cerflag", cerflag);
			Intent intentNotary=new Intent(RecordedRemark.this,RecordedAppealConfirmActivity.class);
			intentNotary.putExtras(bundleNotary);
			startActivityForResult(intentNotary,1);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data!=null){
			Bundle bundle=data.getExtras();
			if(bundle!=null){
				if(TAOBAOREQUESTCODE==requestCode){
					if(resultCode==RecordedAppealTaobaoExtractionCode.RecordedAppealTaobaoExtractionCodeResultCode){
						accstatus=bundle.getInt("accstatus");
						if(accstatus==1){
							btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.recorded_remark_taobao_lookup_selector);
						}else{
							btnrecorded_taobao_appeal.setBackgroundResource(R.drawable.recorded_remark_taobao_request_selector);
						}
						Intent intent=new Intent();
						Bundle taobaoBundle=new Bundle();
						taobaoBundle.putString("fileno", fileno);
						taobaoBundle.putInt("accstatus", accstatus);
						taobaoBundle.putInt("cerflag", cerflag);
						taobaoBundle.putString("remark", etrecorded_remark_edit.getText().toString());
						intent.putExtras(taobaoBundle);
						setResult(REMARKRESULTCODE,intent);
					}
				}else{
					if(resultCode==3){
						cerflag=bundle.getInt("cerflag");
						if(cerflag==1){
							btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.recorded_remark_notary_request_selector);
						}else{
							btnrecorded_notarization_appeal.setBackgroundResource(R.drawable.recorded_remark_notary_cancel_selector);
						}
						Intent intent=new Intent();
						Bundle notaryBundle=new Bundle();
						notaryBundle.putString("fileno", fileno);
						notaryBundle.putInt("cerflag", cerflag);
						notaryBundle.putInt("accstatus", accstatus);
						notaryBundle.putString("remark", etrecorded_remark_edit.getText().toString());
						intent.putExtras(notaryBundle);
						setResult(REMARKRESULTCODE,intent);
					}
				}
			}
		}
	}
}