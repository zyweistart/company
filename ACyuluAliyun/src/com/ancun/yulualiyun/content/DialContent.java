package com.ancun.yulualiyun.content;

import java.util.HashMap;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ancun.core.CoreScrollContent;
import com.ancun.core.Constant.Auth;
import com.ancun.yulualiyun.MainActivity;
import com.ancun.yulualiyun.R;
/**
 * 拔号盘
 * @author Start
 */
public class DialContent extends CoreScrollContent implements OnClickListener , OnLongClickListener {

	/**
	 * 拨号按钮:0~9
	 */
	private ImageButton btn_0;
	private ImageButton btn_1;
	private ImageButton btn_2;
	private ImageButton btn_3;
	private ImageButton btn_4;
	private ImageButton btn_5;
	private ImageButton btn_6;
	private ImageButton btn_7;
	private ImageButton btn_8;
	private ImageButton btn_9;
	/**
	 * *键
	 */
	private ImageButton btnStar=null;
	/**
	 * #键
	 */
	private ImageButton btnPound=null;
	/**
	 * 删除键
	 */
	private ImageButton btnDelete=null;
	/**
	 * 拔打电话
	 */
	private ImageButton btnDial=null;
	/**
	 * 查找键 
	 */
	private ImageButton btnFind=null;
	/**
	 * 添加到联系人
	 */
	private ImageButton btnAddContact=null;	
	/**
	 * 显示号码的
	 */
	private EditText etDigits=null;
	/**
	 * 拨打的号码字符串
	 */
	private String phone="";
	/**
	 * 振动反馈
	 */
	private Vibrator dialVibrator;
	/**
	 * 拨号声音
	 */
	private static final int PLAY_TONE = 0x01;
	/**
	 * 声音的播放时间
	 */
	private static final int DTMF_DURATION_MS = 120; 
	/**
	 * 监视器对象锁
	 */
	private Object mToneGeneratorLock = new Object(); 
	/**
	 * 声音产生器
	 */
	private ToneGenerator mToneGenerator=null;
	
	private AudioManager audioManager=null;
	
	/**
	 * 存储DTMF Tones
	 */
	private static final HashMap<String,Integer> mToneMap = new HashMap<String,Integer>();
	
	static {
		mToneMap.put("1", ToneGenerator.TONE_DTMF_1);
		mToneMap.put("2", ToneGenerator.TONE_DTMF_2);
		mToneMap.put("3", ToneGenerator.TONE_DTMF_3);
		mToneMap.put("4", ToneGenerator.TONE_DTMF_4);
		mToneMap.put("5", ToneGenerator.TONE_DTMF_5);
		mToneMap.put("6", ToneGenerator.TONE_DTMF_6);
		mToneMap.put("7", ToneGenerator.TONE_DTMF_7);
		mToneMap.put("8", ToneGenerator.TONE_DTMF_8);
		mToneMap.put("9", ToneGenerator.TONE_DTMF_9);
		mToneMap.put("0", ToneGenerator.TONE_DTMF_0);
		mToneMap.put("#", ToneGenerator.TONE_DTMF_P);
		mToneMap.put("*", ToneGenerator.TONE_DTMF_S);
	}

	public DialContent(Activity activity, int resourceID) {
		super(activity, resourceID);
		dialVibrator = ( Vibrator ) activity.getApplication().getSystemService(Service.VIBRATOR_SERVICE); 
		//按键声音播放设置及初始化,设置声音的大小   STREAM_DTMF
		mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 70);
		audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
		initDialWidget();
	}

	/**
	 * 初始化拨号按钮并设置监听器
	 */
	public void initDialWidget(){
		btn_0=(ImageButton)findViewById(R.id.dial_zero);
		btn_0.setOnClickListener(this);
		btn_1=(ImageButton)findViewById(R.id.dial_one);
		btn_1.setOnClickListener(this);
		btn_2=(ImageButton)findViewById(R.id.dial_two);
		btn_2.setOnClickListener(this);
		btn_3=(ImageButton)findViewById(R.id.dial_three);
		btn_3.setOnClickListener(this);
		btn_4=(ImageButton)findViewById(R.id.dial_four);
		btn_4.setOnClickListener(this);
		btn_5=(ImageButton)findViewById(R.id.dial_five);
		btn_5.setOnClickListener(this);
		btn_6=(ImageButton)findViewById(R.id.dial_six);
		btn_6.setOnClickListener(this);
		btn_7=(ImageButton)findViewById(R.id.dial_seven);
		btn_7.setOnClickListener(this);
		btn_8=(ImageButton)findViewById(R.id.dial_eight);
		btn_8.setOnClickListener(this);
		btn_9=(ImageButton)findViewById(R.id.dial_nine);
		btn_9.setOnClickListener(this);
		btnStar=(ImageButton)findViewById(R.id.dial_star);
		btnStar.setOnClickListener(this);
		btnPound=(ImageButton)findViewById(R.id.dial_pound);
		btnPound.setOnClickListener(this);
		btnDial=(ImageButton)findViewById(R.id.dial_dialButton);
		btnDial.setOnClickListener(this);
		btnDelete=(ImageButton)findViewById(R.id.dial_delete);
		btnDelete.setOnClickListener(this);
		btnDelete.setOnLongClickListener(this);
		btnFind=(ImageButton)findViewById(R.id.dial_find);
		btnFind.setOnClickListener(this);
		btnAddContact=(ImageButton)findViewById(R.id.dial_add_contact);	
		btnAddContact.setOnClickListener(this);
		etDigits=(EditText)findViewById(R.id.dial_digits);
		etDigits.setText("");
	}
	/**
	 * 显示拨的号码
	 */
	public void showDigits() {
		etDigits.setText(phone);
		if (phone.length()<12) {
			etDigits.setText(phone);
		}else {
			etDigits.setText(phone.subSequence(phone.length()-11, phone.length()));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v==btn_0) {
			phone+=0;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("0"));
		}else if(v==btn_1){
			phone+=1;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("1"));
		}else if(v==btn_2){
			phone+=2;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("2"));
		}else if(v==btn_3){
			phone+=3;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("3"));
		}else if(v==btn_4){
			phone+=4;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("4"));
		}else if(v==btn_5){
			phone+=5;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("5"));
		}else if(v==btn_6){
			phone+=6;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("6"));
		}else if(v==btn_7){
			phone+=7;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("7"));
		}else if(v==btn_8){
			phone+=8;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("8"));
		}else if(v==btn_9){
			phone+=9;
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("9"));
		}else if(v==btnStar){//*
			phone+="*";
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("*"));
		}else if(v==btnPound){
			phone+="#";
			showDigits();
			sendMessage(PLAY_TONE,mToneMap.get("#"));
		}else if(v==btnDial){
			//拨号
			if (phone.length()>0) {
				getMainActivity().inAppDial(phone);
				phone="";
				etDigits.setText(phone);
			}else {
				getAppContext().makeTextShort("请输入号码后再点击拨号");
			}
		}else if(v==btnDelete){
			//删除
			if (phone.length()>0) {
				phone=phone.substring(0, phone.length()-1);
				showDigits();
			}
		}else if(v==btnFind){
			//界面弹转直我的录音
			if(getAppContext().isAuth(Auth.v4recqry1)){
				((MainActivity)getActivity()).getMainContentLayout().snapToScreen(3);
			}else{
				getAppContext().makeTextShort("暂无权限");
			}
		}else if(v==btnAddContact){
			//增加联系人
			if (phone.length()>0) {
				Intent intent = new Intent(Intent.ACTION_INSERT);
				intent.setType("vnd.android.cursor.dir/person");
				intent.setType("vnd.android.cursor.dir/contact");
				intent.setType("vnd.android.cursor.dir/raw_contact");
				intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, phone);
				intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE,android.provider.Contacts.PhonesColumns.TYPE_MOBILE);
				startActivity(intent);
			}else {
				getAppContext().makeTextShort("请输入号码后，再点击添加联系人");
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v==btnDelete) {
			phone="";
			etDigits.setText(phone);
		}
		return false;
	}
	
	@Override
	public void processMessage(Message msg) {
		switch (msg.what) {
		case PLAY_TONE:
			//振动
			dialVibrator.vibrate(new long[]{0,10,20,30},-1); 
			Integer tone_id = (Integer) msg.obj;
			if (tone_id != null&&tone_id!=-1) {
				//-1是只振动不发声
				Integer tone=tone_id.intValue();
				int ringerMode = audioManager.getRingerMode();
				if (ringerMode == AudioManager.RINGER_MODE_SILENT
						|| ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
					// 静音或者震动时不发出声音
					return;
				}
				synchronized (mToneGeneratorLock) {
					if (mToneGenerator == null) {
						Log.w(TAG, "playTone: mToneGenerator == null, tone: " + tone);
						return;
					}
					mToneGenerator.startTone(tone, DTMF_DURATION_MS);
				}
			}
			break;
		}
	}
	
}