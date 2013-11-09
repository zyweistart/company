package com.ancun.yulualiyun;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ancun.core.Constant;
import com.ancun.core.CoreActivity;
import com.ancun.model.ContactModel;
import com.ancun.model.RecentModel;
import com.ancun.model.UIRunnable;
import com.ancun.utils.NetConnectManager;
import com.ancun.utils.TimeUtils;
/**
 * 外部电话拔打
 * @author Start
 */
public class DialActivity extends CoreActivity {

	private String oppo;
	private String phone;
	private ImageButton btnDialRecord;
	private ImageButton btnDialNormal;
	private TextView tvDialMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_dial);
		btnDialRecord=(ImageButton)findViewById(R.id.common_dial_record);
		btnDialNormal=(ImageButton)findViewById(R.id.common_dial_system);
		tvDialMessage=(TextView)findViewById(R.id.common_dial_message);

		Bundle extras=getIntent().getExtras();
		oppo=extras.getString("oppo");
		phone=extras.getString("phone");
		if(!Constant.EMPTYSTR.equals(oppo)&&!Constant.EMPTYSTR.equals(phone)){
			final ContactModel contactModel=getContactService().getContactModelByPhone(oppo);
			if(!getAppContext().getSharedPreferencesUtils().getBoolean(Constant.SharedPreferencesConstant.SP_UNFAMILIAR_EXTERNAL_DIAL, true)&&contactModel==null){
				call(oppo);
			}else if(contactModel!=null&&contactModel.getRecordFlag()==0){
				//自动录音
				recorded();
			}else if(contactModel==null||contactModel.getRecordFlag()==1){
				tvDialMessage.setText("您是否对   "+(contactModel==null?oppo:contactModel.getName())+"   进行通话录音？");
				btnDialRecord.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) { 
						recorded();
					}
				});
				btnDialNormal.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) { 
						call(oppo);
					}
				}); 
			}else{
				call(oppo);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK ){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void recorded(){
		if(NetConnectManager.isNetWorkAvailable(this)){
			Map<String,String> requestParams=new HashMap<String,String>();
			requestParams.put("accessid", Constant.ACCESSID_LOCAL);
			requestParams.put("calltype", "1");
			requestParams.put("username", phone);
			requestParams.put("oppno",oppo);
			Map<String,String> headerParams=new HashMap<String,String>();
			headerParams.put("sign",Constant.ACCESSKEY_LOCAL);
			getAppContext().exeNetRequest(this,Constant.GlobalURL.v4eCall,requestParams,headerParams,new UIRunnable() {
				
				@Override
				public void run() {
					//把拔打的电话加入最近通话记录中
					RecentModel recent=new RecentModel();
					recent.setPhone(oppo);
					recent.setStatus(CallLog.Calls.OUTGOING_TYPE);
					getRecentService().save(recent);
					getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_CALL_DIAL, oppo);
					getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false);
					String serverno=(Constant.SYSTEMTEST ?Constant.TESTSERVERNO:getInfoContent().get("serverno"));
					getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_SERVER_CALL, serverno);
					call(serverno);
				}
				
			});
		}else{
			String serverno=getAppContext().getSharedPreferencesUtils().getString(Constant.SharedPreferencesConstant.SP_SERVER_CALL, Constant.EMPTYSTR);
			if(Constant.EMPTYSTR.equals(serverno)){
				call(oppo);
			}else{
				getAppContext().getSharedPreferencesUtils().putString(Constant.SharedPreferencesConstant.SP_CALL_DIAL, oppo);
				getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_MAINACTIVITY_FLAG, false);
				call(serverno);
			}
		}
		
	}
	
	/**
	 * 拔打电话
	 */
	private void call(String callphone){
		TimeUtils.sleep(100);
		getAppContext().getSharedPreferencesUtils().putBoolean(Constant.SharedPreferencesConstant.SP_CALL_ISALLOW, false);
		Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+callphone));
		startActivity(intent);
		finish();
	}
	
}
