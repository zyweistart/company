package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.start.core.Constant;
import com.start.core.CoreActivity;
import com.start.model.FriendHistory;
import com.start.model.UIRunnable;
import com.start.utils.TimeUtils;

/**
 * 好友关系设置
 * @author start
 *
 */
public class FriendRelationSetActivity extends CoreActivity implements OnClickListener {

	private EditText mFriendFelationSetEtContent;
	private Button mModuleMainHeaderContentAdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_relation_set);
		setCurrentActivityTitle(R.string.activity_title_friend_relation_set);
		
		mModuleMainHeaderContentAdd = (Button) findViewById(R.id.module_main_header_content_click);
		mModuleMainHeaderContentAdd.setVisibility(View.VISIBLE);
		
		mFriendFelationSetEtContent=(EditText)findViewById(R.id.friend_relation_set_et_content);
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.module_main_header_content_click){
			startActivity(new Intent(this,FriendHistoryListActivity.class));
		} else if(v.getId()==R.id.friend_relation_set_btn_add){
			final String friendId=String.valueOf(mFriendFelationSetEtContent.getText());
			if(TextUtils.isEmpty(friendId)){
				makeTextLong("好友ID不能为空");
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				Map<String,String> headerParams=new HashMap<String,String>();
				headerParams.put("sign", "");
				getHttpService().exeNetRequest(Constant.ServerAPI.nOpenLocation,requestParams,headerParams,new UIRunnable() {
					
					@Override
					public void run() {
						
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								setResult(FriendRelationListActivity.RESULT_CODE_SET_REFRESH);
								makeTextLong("开放好友位置成功");
								mFriendFelationSetEtContent.setText(getString(R.string.empty));
								String myId=getAppContext().getMyID();
								String fileno=getAppContext().getCurrentDataNo();
								FriendHistory fh=getAppContext().getFriendHistoryService().findByMyIdAndFriendId(myId, friendId);
								ContentValues values=new ContentValues();
								values.put(FriendHistory.COLUMN_NAME_FILENO, fileno);
								values.put(FriendHistory.COLUMN_NAME_MYID, myId);
								values.put(FriendHistory.COLUMN_NAME_FRIENDID, friendId);
								values.put(FriendHistory.COLUMN_NAME_UPDATETIME, TimeUtils.getSysdate());
								if(fh==null){
									getAppContext().getFriendHistoryService().insert(FriendHistory.TABLE_NAME, values);
								}else{
									getAppContext().getFriendHistoryService().update(FriendHistory.TABLE_NAME, values,
											FriendHistory.COLUMN_NAME_MYID+" = ? AND "+
													FriendHistory.COLUMN_NAME_FRIENDID+" = ? AND "+
														FriendHistory.COLUMN_NAME_FILENO+" = ?",
											new String[]{myId,friendId,fileno});
								}
							}
							
						});
						
					}
					
				});
			}
		}
	}

}
