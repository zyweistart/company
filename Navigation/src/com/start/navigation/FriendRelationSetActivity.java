package com.start.navigation;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_relation_set);
		setCurrentActivityTitle(R.string.activity_title_friend_relation_set);
		mFriendFelationSetEtContent=(EditText)findViewById(R.id.friend_relation_set_et_content);
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.friend_relation_set_btn_add){
			final String friendId=String.valueOf(mFriendFelationSetEtContent.getText());
			if(TextUtils.isEmpty(friendId)){
				makeTextLong("好友ID不能为空");
			}else{
				Map<String,String> requestParams=new HashMap<String,String>();
				requestParams.put("friendId", friendId);
				getHttpService().exeNetRequest(Constant.ServerAPI.nOpenLocation,requestParams,null,new UIRunnable() {
					
					@Override
					public void run() {
//						Map<String,String> user= getAppContext().getUserInfoByKey("v4info");
						String myId="";
						FriendHistory fh=getAppContext().getFriendHistoryService().findByMyIdAndFriendId(myId, friendId);
						ContentValues values=new ContentValues();
						values.put(FriendHistory.COLUMN_NAME_MYID, myId);
						values.put(FriendHistory.COLUMN_NAME_FRIENDID, friendId);
						values.put(FriendHistory.COLUMN_NAME_UPDATETIME, TimeUtils.getSysdate());
						if(fh!=null){
							getAppContext().getFriendHistoryService().insert(FriendHistory.TABLE_NAME, values);
						}else{
							getAppContext().getFriendHistoryService().update(FriendHistory.TABLE_NAME, values,
									FriendHistory.COLUMN_NAME_MYID+" = ? AND "+FriendHistory.COLUMN_NAME_FRIENDID+" = ?",
									new String[]{myId,friendId});
						}
						mFriendFelationSetEtContent.setText(getString(R.string.empty));
						setResult(FriendRelationListActivity.RESULT_REFRESH_CODE);
						finish();
					}
					
				});
			}
		}else if(v.getId()==R.id.friend_relation_set_btn_history_add){
			startActivity(new Intent(this,FriendHistoryListActivity.class));
		}
	}

}
