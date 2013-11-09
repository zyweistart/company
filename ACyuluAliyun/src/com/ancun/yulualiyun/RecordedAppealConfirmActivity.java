package com.ancun.yulualiyun;


import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.core.Constant.Auth;
import com.ancun.model.UIRunnable;

public class RecordedAppealConfirmActivity extends CoreActivity {
	
	private ImageView ivTitle=null;
	private TextView tvMessage=null;
	private ImageButton ibYes=null;
	private ImageButton ibNo=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);   
		setContentView(R.layout.activity_recorded_appeal_confirm);
		ivTitle=(ImageView)findViewById(R.id.recorded_appeal_confirm_title);
		tvMessage=(TextView)findViewById(R.id.recorded_appeal_confirm_message);
		final int appealType=getIntent().getExtras().getInt("appeal_type");
		final String fileno=getIntent().getExtras().getString("fileno");
		final int cerflag=getIntent().getExtras().getInt("cerflag");
		if (1==appealType) {//淘申述
			if(!getAppContext().isAuth(Auth.v4recalter8)){
				makeTextShort("暂无权限");
				finish();
			}
			ivTitle.setBackgroundResource(R.drawable.recorded_appeal_title_taobao);
			tvMessage.setText("凭提取码可在官网公开查询、验证本条通话录音，确定申请？");
		}else if (2==appealType) {//申请公证
			//只有主账号才可以申请公证
			if(!"1".equals(getAppContext().getUserInfo().get("masterflag"))){
				makeTextShort("暂无权限");
				finish();
			}
			ivTitle.setBackgroundResource(R.drawable.recorded_appeal_title_notary);
			if(cerflag==1){
				tvMessage.setText("您确定将该录音提交至公证机构申办公证吗？");
			}else if(cerflag==2){
				tvMessage.setText("您确定要取消该录音申办公证吗？");
			}
		}
		ibYes=(ImageButton)findViewById(R.id.recorded_appeal_confirm_yes);
		ibYes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=getIntent().getExtras();
				if (1==appealType) {
					Intent intentTaobao=new Intent(RecordedAppealConfirmActivity.this,RecordedAppealTaobaoExtractionCode.class);
					intentTaobao.putExtras(bundle);
					startActivityForResult(intentTaobao,RecordedManagerContentListActivity.TAOBAOREQUESTCODE);
				}else if (2==appealType) {
					Map<String,String> requestParams=new HashMap<String,String>();
					requestParams.put("accessid", Constant.ACCESSID);
					requestParams.put(RecordedManagerContentListActivity.RECORDED_FILENO,fileno);
					//1:取消出证;2:申请出证
					if(cerflag==1){
						requestParams.put(RecordedManagerContentListActivity.RECORDED_CEFFLAG,"2");
					}else{
						requestParams.put(RecordedManagerContentListActivity.RECORDED_CEFFLAG,"1");
					}
					getAppContext().exeNetRequest(RecordedAppealConfirmActivity.this,Constant.GlobalURL.v4recCer,requestParams,null,new UIRunnable() {
						@Override
						public void run() {
							Bundle resultBundle=new Bundle();
							resultBundle.putString(RecordedManagerContentListActivity.RECORDED_FILENO, fileno);
							if(cerflag==1){
								Intent intentNotaryNotify=new Intent(RecordedAppealConfirmActivity.this,RecordedAppealNotarySuccess.class);
								intentNotaryNotify.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intentNotaryNotify);
								resultBundle.putInt(RecordedManagerContentListActivity.RECORDED_CEFFLAG, 2);
							}else{
								resultBundle.putInt(RecordedManagerContentListActivity.RECORDED_CEFFLAG, 1);
							}
							Intent resultIntent=new Intent();
							resultIntent.putExtras(resultBundle);
							setResult(3,resultIntent);
							finish();
						}
					});
				}
			}
		});
		ibNo=(ImageButton)findViewById(R.id.recorded_appeal_confirm_no);
		ibNo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==RecordedManagerContentListActivity.TAOBAOREQUESTCODE){
			setResult(resultCode,data);
		}
		finish();
	}
	
}