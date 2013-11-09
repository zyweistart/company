package com.ancun.yulualiyun;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.core.Constant.Auth;
import com.ancun.model.UIRunnable;
import com.ancun.utils.CommonFn;
import com.ancun.utils.NetConnectManager;

/**
 * 提取申请码
 * @author Start
 */
@SuppressWarnings("deprecation")
public class RecordedAppealTaobaoExtractionCode extends CoreActivity implements OnClickListener {
	
	public static final int RecordedAppealTaobaoExtractionCodeResultCode=0xAC0012;

	private ClipboardManager clipboard;
	
	private TextView tv_recorded_appeal_taobao_code_url;
	private TextView tv_recorded_appeal_taobao_code;
	private TextView tv_recorded_appeal_taobao_limit_time;
	private Button btn_recorded_appeal_taobao_btn_copy;
	private Button btn_recorded_appeal_taobao_btn_send_to_mobile;
	private Button btn_recorded_appeal_taobao_btn_cancel;
	
	private String fileno;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_recorded_appeal_extraction_code);
		clipboard=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		tv_recorded_appeal_taobao_code_url=(TextView)findViewById(R.id.recorded_appeal_taobao_code_url);
		tv_recorded_appeal_taobao_code=(TextView)findViewById(R.id.recorded_appeal_taobao_code);
		tv_recorded_appeal_taobao_limit_time=(TextView)findViewById(R.id.recorded_appeal_taobao_limit_time);
		btn_recorded_appeal_taobao_btn_copy=(Button)findViewById(R.id.recorded_appeal_taobao_btn_copy);
		btn_recorded_appeal_taobao_btn_copy.setOnClickListener(this);
		btn_recorded_appeal_taobao_btn_send_to_mobile=(Button)findViewById(R.id.recorded_appeal_taobao_btn_send_to_mobile);
		btn_recorded_appeal_taobao_btn_send_to_mobile.setOnClickListener(this);
		btn_recorded_appeal_taobao_btn_cancel=(Button)findViewById(R.id.recorded_appeal_taobao_btn_cancel);
		btn_recorded_appeal_taobao_btn_cancel.setOnClickListener(this);
		fileno=getIntent().getExtras().getString("fileno");
		if(fileno!=null){
			Integer accstatus=getIntent().getExtras().getInt(RecordedManagerContentListActivity.RECORDED_ACCSTATUS);
			if(accstatus==1){
				setNavigationTitle(R.string.viewextractioncode_title);
				//有效
				if(NetConnectManager.isNetWorkAvailable(RecordedAppealTaobaoExtractionCode.this)){
					if(!getAppContext().isAuth(Auth.v4recqry3)){
						makeTextShort("暂无权限");
						finish();
					}else{
						getDataTask(2);
					}
				}else{
					CommonFn.settingNetwork(RecordedAppealTaobaoExtractionCode.this);
				}
			}else{
				setNavigationTitle(R.string.regextractioncode_title);
				//生成
				if(NetConnectManager.isNetWorkAvailable(RecordedAppealTaobaoExtractionCode.this)){
					getDataTask(1);
				}else{
					CommonFn.settingNetwork(RecordedAppealTaobaoExtractionCode.this);
				}
			}
		}
	}
	
	public void getDataTask(final Integer status){
		Map<String,String> requestParams=new HashMap<String,String>();
		requestParams.put("accessid", Constant.ACCESSID);
		requestParams.put("fileno",fileno);
		requestParams.put("acccodeact", String.valueOf(status));
		requestParams.put("vtime","10");
		getAppContext().exeNetRequest(this,Constant.GlobalURL.v4recAcccode,requestParams,null,new UIRunnable() {
			
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Bundle resultBundle=new Bundle();
						resultBundle.putString(RecordedManagerContentListActivity.RECORDED_FILENO, fileno);
						if(status==1||status==2){
							tv_recorded_appeal_taobao_code_url.setText(getInfoContent().get("url"));
							tv_recorded_appeal_taobao_code.setText(getInfoContent().get("acccode"));
							tv_recorded_appeal_taobao_limit_time.setText(getInfoContent().get("endtime"));
							resultBundle.putInt(RecordedManagerContentListActivity.RECORDED_ACCSTATUS,1);
							Intent resultIntent=new Intent();
							resultIntent.putExtras(resultBundle);
							setResult(RecordedAppealTaobaoExtractionCodeResultCode,resultIntent);
						}else if(status==3){
							resultBundle.putInt(RecordedManagerContentListActivity.RECORDED_ACCSTATUS,2);
							Intent resultIntent=new Intent();
							resultIntent.putExtras(resultBundle);
							setResult(RecordedAppealTaobaoExtractionCodeResultCode,resultIntent);
							makeTextLong("撤销成功！");
							finish();
						}
					}
				});
				
			}
			
		});
	}
	
	@Override
	public void onClick(View v) {
		String strContent=tv_recorded_appeal_taobao_code_url.getText().toString()+tv_recorded_appeal_taobao_code.getText().toString();
		switch(v.getId()){
		case R.id.recorded_appeal_taobao_btn_copy:
			//复制内容
			clipboard.setText(strContent);
			btn_recorded_appeal_taobao_btn_copy.setEnabled(false);
			makeTextLong("复制成功");
			break;
		case R.id.recorded_appeal_taobao_btn_send_to_mobile:
             // 联系人地址 
             Uri smsToUri = Uri.parse("smsto:");
             Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, 
             smsToUri); 
             
             strContent="您申请的录音提取码为："+strContent+" ，凭该提取码可在官网公开查询、下载本条通话录音，请妥善保管。客服电话:95105856【安存科技】";
             
             // 短信内容 
             mIntent.putExtra("sms_body",strContent);
             startActivity(mIntent); 
			break;
		case R.id.recorded_appeal_taobao_btn_cancel:
			if(!getAppContext().isAuth(Auth.v4recalter8)){
				getAppContext().makeTextShort("暂无权限");
			}else{
				AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
				aDialog.
				setIcon(android.R.drawable.ic_dialog_info).
				setTitle("提示！").
				setMessage("是否撤销提取？").
				setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(NetConnectManager.isNetWorkAvailable(RecordedAppealTaobaoExtractionCode.this)){
							getDataTask(3);
						}else{
							CommonFn.settingNetwork(RecordedAppealTaobaoExtractionCode.this);
						}
					}
				}).setNeutralButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
			}
			break;
		}
	}
}