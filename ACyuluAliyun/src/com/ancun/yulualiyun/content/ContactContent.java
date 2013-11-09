package com.ancun.yulualiyun.content;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ancun.core.CoreScrollContent;
import com.ancun.model.ContactModel;
import com.ancun.utils.CommonFn;
import com.ancun.yulualiyun.R;
/**
 * 联系人
 * @author Start
 */
public class ContactContent extends CoreScrollContent implements AdapterView.OnItemClickListener,OnClickListener, Filterable {

	private FilterContact mFilter; 
	private PopupWindow contactPopupWindow=null;
	private ImageButton btnAutoRecorded=null;
	private ImageButton btnRemindRecorded=null;
	private ImageButton btnCloseRecorded=null;
	private View popupWindow_view;
	private ListView contactListView;
	private ContactAdapter adapter;
	//状态提示文字
//	private final String[] itemStrings = {"自动录音", "手动选择","从不录音"};

	private List<ContactModel> mListDataItemsFilter=null;
	private List<ContactModel> mListDataItems=null;
	private EditText etSearch;
	private ImageButton ibSearchClean;
	private ImageButton ibSearchBegin;

	private boolean isShowOverLay=false;
	/**
	 * 上一次浮动的字
	 */
	private String mPrevLetter="";
	/**
	 * 联系人滚动时浮动的字
	 */
	private TextView overlay;
	/**
	 * 窗口管理服务
	 */
	private WindowManager mWindowManager;
	private boolean mShowing; //是否显示mDialogText
	private boolean mReady; //mDialogText是否已准备好
	private RemoveWindow mRemoveWindow = new RemoveWindow();
	Handler mHandler = new Handler();
	//删除滚动时浮动的字
	private final class RemoveWindow implements Runnable {
		public void run() {
			removeWindow();
		}
	}

	public ContactContent(final Activity activity, int resourceID) {
		super(activity, resourceID);
		contactListView = (ListView) findViewById(R.id.contacts_listview);
		contactListView.setOnItemClickListener(this);
		
		View ContactsSearchBarView = View.inflate(activity, R.layout.module_contact_search_bar, null);  
		contactListView.addHeaderView(ContactsSearchBarView); //把view对象添加到listView对象的头部，可以随listView一起滑动；
 
		etSearch=(EditText)ContactsSearchBarView.findViewById(R.id.contact_search_bar_content);
		etSearch.addTextChangedListener(new CustomTextWatcher());
		ibSearchClean=(ImageButton)ContactsSearchBarView.findViewById(R.id.contact_search_bar_clean);
		ibSearchClean.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				etSearch.setText("");
			}
		});
		ibSearchBegin=(ImageButton)ContactsSearchBarView.findViewById(R.id.contact_search_bar_icon);
		ibSearchBegin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//打开软键盘
				InputMethodManager inputMethodManager=(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);  
				if (inputMethodManager.isActive()) {
					inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		});

		//获取Window窗口管理服务
		mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		overlay = (TextView) View.inflate(activity, R.layout.module_contact_overlay, null);  
		overlay.setVisibility(View.INVISIBLE);

		//将Runnable添加到消息队列中
		mHandler.post(new Runnable() {
			public void run() {
				mReady = true;
				//设置mDialogText的WindowManager.LayoutParams参数
				WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
						WindowManager.LayoutParams.TYPE_APPLICATION,
						WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
						PixelFormat.TRANSLUCENT);
				mWindowManager.addView(overlay, lp);
			}

		});
		contactListView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isShowOverLay=true;
				//关闭输入框弹窗的键盘
				InputMethodManager inputMethodManager=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);  
				inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
				return false;
			}
		});
		contactListView.setOnScrollListener(new OnScrollListener() {  
			@Override  
			public void onScroll(AbsListView view, int firstVisibleItem,  
					int visibleItemCount, int totalItemCount) {  
				//显示姓名的第一个字
				 
				if (isShowOverLay&&mReady&&mListDataItemsFilter!=null&&mListDataItemsFilter.size()>0&&overlay!=null) {
					String name=mListDataItemsFilter.get(firstVisibleItem).getName();
					if(name!=null){
						String firstLetter=name.substring(0, 1);
						//当浮动字 不可见时设置其可见
						if (!mShowing && !firstLetter.equals(mPrevLetter)  ) {
							mShowing = true;
							overlay.setVisibility(View.VISIBLE);
						}
						overlay.setText(firstLetter);  
						//将消息队列中还在等待post的mRemoveWindow清除
						mHandler.removeCallbacks(mRemoveWindow);
						//将Runnable mRemoveWindow添加到消息队列中，并延迟1.5s后运行
						// 1.5s后 设置为不可见
						mHandler.postDelayed(mRemoveWindow, 1000);
						mPrevLetter = firstLetter;
					}
				}
			}  
			@Override  
			public void onScrollStateChanged(AbsListView view, int scrollState) {  
				
			}  

		});
	}
	
	@Override
	public void finish() {
		if(mWindowManager!=null){
			if(overlay!=null){
				mWindowManager.removeView(overlay);
				overlay=null;
			}
		}
	}

	//将overlay设置为不可见
	private void removeWindow() {
		if (mShowing) {
			mShowing = false;
			overlay.setVisibility(View.INVISIBLE);
//			mPrevLetter="";
		}
	}
	
	public void loadData(final Boolean flag){
		//系统通讯录加载
		final ProgressDialog dialog =CommonFn.progressDialog(getContext(),"本地通讯录同步中，请稍等");
		dialog.setCancelable(false);
		dialog.show();
		new Thread() {
			public void run() {
				try{
					if(mListDataItems==null){
						mListDataItems=new ArrayList<ContactModel>();
					}else{
						mListDataItems.clear();
					}
					//对联系人进行排序，排序规则先按分类进行排序然后在分类中再按联系人姓名升顺排
					List<ContactModel> group1=new ArrayList<ContactModel>();
					List<ContactModel> group2=new ArrayList<ContactModel>();
					List<ContactModel> group3=new ArrayList<ContactModel>();
					for(ContactModel cm:getMainActivity().getContactService().loadAllContact()){
						if(cm.getRecordFlag()==0){
							//自动录音
							group1.add(cm);
						}else if(cm.getRecordFlag()==1){
							//手动选择
							group2.add(cm);
						}else if(cm.getRecordFlag()==2){
							//关闭录音
							group3.add(cm);
						}
					}
					mListDataItems.addAll(group1);
					mListDataItems.addAll(group2);
					mListDataItems.addAll(group3);
					//filter listview赋初值
					if(mListDataItemsFilter==null){
						mListDataItemsFilter=new ArrayList<ContactModel>();
					}else{
						mListDataItemsFilter.clear();
					}
					mListDataItemsFilter.addAll(mListDataItems);
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							if(flag){
								adapter=new ContactAdapter();
								contactListView.setAdapter(adapter);
							}else{
								//每次更新listview  按照搜索框的内容从新过滤 
								getFilter().filter(etSearch.getText());
							}
						}
					});
				}finally{
					dialog.dismiss();
				}
			};
		}.start();
	}
	/**
	 * 联系人适配器
	 */
	public class ContactAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListDataItemsFilter.size();
		}

		@Override
		public Object getItem(int position) {
			return mListDataItemsFilter.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,ViewGroup parent) {
			ContactViewHolder holder;
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.lvitem_content_contact,null);
				holder = new ContactViewHolder();
				holder.alpha = (TextView) convertView.findViewById(R.id.contact_alpha);
				holder.alphaView = (View) convertView.findViewById(R.id.contact_alpha_view);
				holder.name = (TextView) convertView.findViewById(R.id.contact_name_text);
				holder.phone = (TextView) convertView.findViewById(R.id.contact_phone_text);
				holder.photo = (ImageView) convertView.findViewById(R.id.contact_image_view);
				holder.changeFlag = (ImageButton) convertView.findViewById(R.id.contact_changestatus_button);
				convertView.setTag(holder);
			} else {
				holder = (ContactViewHolder) convertView.getTag();
			}
			ContactModel info = mListDataItemsFilter.get(position);
			holder.key=info.getLookupKey();
			holder.name.setText(info.getName());
			holder.phone.setText("");
			if (info.getPhotoID() > 0) {
				holder.photo.setImageBitmap(getMainActivity().getContactService().loadContactPhoto(info.getId()));
			}else{
				holder.photo.setImageResource(R.drawable.contact_head);
			}
			holder.changeFlag.setTag(info);
			Integer recordFlag= info.getRecordFlag();
			if (recordFlag==2) {//取消录音
				holder.changeFlag.setBackgroundResource(R.drawable.contact_close_recorded_selector);
			}else if (recordFlag==1){//提示录音
				holder.changeFlag.setBackgroundResource(R.drawable.contact_remind_recorded_selector);
			}else if (recordFlag==0){//自动录音
				holder.changeFlag.setBackgroundResource(R.drawable.contact_auto_recorded_selector);
			}
			holder.changeFlag.setOnClickListener(ContactContent.this);
			if (position == 0) {
				showAlphaText(holder, position);
			} else if (position > 0) {
				if (info.getRecordFlag() != mListDataItemsFilter.get(position - 1).getRecordFlag()) {
					showAlphaText(holder, position);
				} else {
					holder.alpha.setVisibility(View.GONE);
					holder.alphaView.setVisibility(View.GONE);
				}
			}
			return convertView;
		}
		
	}
	/**
	 * 视图辅助类
	 * @author Start
	 */
	public final class ContactViewHolder {
		/**
		 * 主键
		 */
		private String key; 
		/**
		 * 状态提示标题
		 */
		private TextView alpha;
		/**
		 * 状态提示标题下的横向线
		 */
		private View alphaView;
		/**
		 * 姓名
		 */
		private TextView name;
		/**
		 * 电话 
		 */
		private TextView phone;
		/**
		 * 照片
		 */
		private ImageView photo;
		/**
		 * 更改状态标记按钮
		 */
		private ImageButton changeFlag;
	}
	/**
	 * 设置AlphaText文字内容或者隐藏
	 */
	public void showAlphaText(ContactViewHolder holder, int position) {
//		Integer recordflag=mListDataItemsFilter.get(position).getRecordFlag();
//		if (recordflag == 0) {
//			holder.alpha.setVisibility(View.VISIBLE);
//			holder.alphaView.setVisibility(View.VISIBLE);
//		} else if (recordflag == 1) {
//			holder.alpha.setVisibility(View.VISIBLE);
//			holder.alphaView.setVisibility(View.VISIBLE);
//		} else if (recordflag == 2) {
//			holder.alpha.setVisibility(View.VISIBLE);
//			holder.alphaView.setVisibility(View.VISIBLE);
//		}
//		holder.alpha.setText(itemStrings[recordflag]);
	}
	////////////////////////////////////////////////Event
	@Override
	public void onItemClick(AdapterView<?> arg0,final View arg1, int arg2, long arg3) {
		final ContactViewHolder holder=(ContactViewHolder)arg1.getTag();
		List<String>phones=getMainActivity().getContactService().getContactAllPhone(holder.key);
		if(phones.size()>1){
			final String[] phonearr=phones.toArray(new String[phones.size()]);
			AlertDialog.Builder changeStatusBuilder = new AlertDialog.Builder(getContext());
			changeStatusBuilder
			.setTitle("通话号码选择")
			.setIcon(android.R.drawable.ic_dialog_info) 
			.setSingleChoiceItems(phonearr,1,
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,final int which) {
					dialog.dismiss();
					getMainActivity().inAppDial(phonearr[which]);
//					getMainActivity().getDialService().dial(phonearr[which]);
				}
			}).create().show();
		}else if(phones.size()==1){
			//如果电话为一个则直接拔号
			getMainActivity().inAppDial(phones.get(0));
//			getMainActivity().getDialService().dial(phones.get(0));
		}else{
			getAppContext().makeTextLong("无任何联系号码!");
		}
	}
	@Override
	public void onClick(View view) {
		if(null != contactPopupWindow && contactPopupWindow.isShowing()) {
			contactPopupWindow.dismiss();
		}else {   
			initPopuptWindow(view);   
			contactPopupWindow.showAsDropDown(view,0,-210);
		}   
	}
	/**
	 * 创建PopupWindow
	 */
	private void initPopuptWindow(View view) {
		final ContactModel cm=(ContactModel)view.getTag();
		//获取自定义布局文件popup.xml的视图             
		popupWindow_view =  getLayoutInflater().inflate(R.layout.module_contact_popupwindow, null,false); 
		contactPopupWindow = new PopupWindow(popupWindow_view,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);   
		btnAutoRecorded = (ImageButton)popupWindow_view.findViewById(R.id.contact_popup_btn_auto_recorded);
		btnAutoRecorded.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setContactStatus(cm,0);

			}
		});
		btnRemindRecorded = (ImageButton)popupWindow_view.findViewById(R.id.contact_popup_btn_remind_recorded);
		btnRemindRecorded.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setContactStatus(cm,1);
			}
		});
		btnCloseRecorded = (ImageButton)popupWindow_view.findViewById(R.id.contact_popup_btn_close_recorded);
		btnCloseRecorded.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setContactStatus(cm,2);
			}
		});
		contactPopupWindow.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.contact_popup_drawable));
		contactPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog); 
		//设置PopupWindow外部区域是否可触摸
		contactPopupWindow.setOutsideTouchable(false);
		// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
		contactPopupWindow.setFocusable(true);
		contactPopupWindow.update();
		//changeFlagBtn 高度
		int[] changeFlagBtnLocation = new int[2];
		view.getLocationOnScreen(changeFlagBtnLocation);
		Rect changeFlagBtnRect = new Rect(changeFlagBtnLocation[0], changeFlagBtnLocation[1], changeFlagBtnLocation[0] + view.getWidth(),
				changeFlagBtnLocation[1] + view.getHeight());
//		int popupWindowWidth = popupWindow_view.getMeasuredWidth();
		popupWindow_view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		popupWindow_view.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		int popupWindowHeight = popupWindow_view.getMeasuredHeight();
//		Log.i("rect top: "+changeFlagBtnRect.top , "ab3");
//		Log.i("popupWindowHeight: "+popupWindowHeight , "ab3");
		int popupWindow_yPos = changeFlagBtnRect.top-popupWindowHeight;
		contactPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, popupWindow_yPos+2);
	}
	/**
	 * 设置联系人状态
	 */
	private void setContactStatus(ContactModel cm , int status) {
		if(cm.getRecordFlag()!=status){		
			getMainActivity().getContactService().modifyFlag(cm.getLookupKey(), status);
			loadData(false);
		}
		//关闭popupwindow
		if(null != contactPopupWindow && contactPopupWindow.isShowing()) {
			contactPopupWindow.dismiss();
		}
	}
	/**
	 * 实例化filter
	 */
	@Override  
	public Filter getFilter() {   
		if (mFilter == null) {  
			mFilter = new FilterContact();  
		}  
		return mFilter;  
	}  
	/**
	 * 按照姓名查找 的filter类
	 */
	private class FilterContact extends Filter {  

		@Override  
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			mListDataItemsFilter.clear();
			if (prefix == null || prefix.length() == 0) {
				//输入为空
				mListDataItemsFilter.addAll(mListDataItems);
			} else {
				for(ContactModel userInfo:mListDataItems){
					String name=userInfo.getName().toLowerCase();
					if(name==null||"".equals(name)){
						continue;
					}
					String pre=prefix.toString().toLowerCase();
					//匹配
					if(name.contains(pre)){
						mListDataItemsFilter.add(userInfo);
					}else if(name.equals(pre)){
						mListDataItemsFilter.add(userInfo);
					}else if(userInfo.getPinyinName().contains(pre)){
						mListDataItemsFilter.add(userInfo);
					}
				}
			}
			results.values = mListDataItemsFilter;
			results.count = mListDataItemsFilter.size();
			return results;  
		}  

		@Override  
		protected void publishResults(CharSequence constraint, FilterResults results) {
			adapter.notifyDataSetChanged();
		}
	}
	
	private class CustomTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}
		@Override
		public void afterTextChanged(Editable s) {
			getFilter().filter(s);
			//每次输入框文字变化    需要滚动屏幕才显示浮动姓
			isShowOverLay=false;
		}
	}

	public EditText getEtSearch() {
		return etSearch;
	}

}