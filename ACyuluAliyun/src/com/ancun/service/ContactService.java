package com.ancun.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import com.ancun.core.CoreServiceModel;
import com.ancun.model.ContactModel;
import com.ancun.utils.StringUtils;

public class ContactService extends CoreServiceModel {

	public ContactService(Context mContext){
		super(mContext);
	}
	
	public List<ContactModel> loadAllContact() {
//		Cursor allContactCursor=null;
//		Map<String,ContactModel> cmMap=new HashMap<String,ContactModel>();
//		try{
//			allContactCursor = getSQLiteDatabase().query(ContactModel.TABLENAME, new String[]{"key","flag","usersetflag"},
//					null,null,null, null, null);
//			if (allContactCursor.moveToFirst()) {
//				do {
//					ContactModel cm=new ContactModel();
//					cm.setPhone(allContactCursor.getString(allContactCursor.getColumnIndex("key")));
//					cm.setRecordFlag(allContactCursor.getInt(allContactCursor.getColumnIndex("flag")));
//					cm.setUserSetFlag(allContactCursor.getInt(allContactCursor.getColumnIndex("usersetflag")));
//					cmMap.put(cm.getPhone(), cm);
//				} while (allContactCursor.moveToNext());
//			}
//		}finally{
//			allContactCursor.close();
//		}
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts.LOOKUP_KEY
        };
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(
        		ContactsContract.Contacts.CONTENT_URI, projection, null, null, sortOrder);
        List<ContactModel> contactListData=new ArrayList<ContactModel>();
		try{
			if (cursor.moveToFirst()) {
				do {
					ContactModel mContactInfo = new ContactModel();
					mContactInfo.setId(cursor.getLong(0));
					mContactInfo.setName(cursor.getString(1));
					mContactInfo.setPhotoID(cursor.getLong(2));
					mContactInfo.setLookupKey(cursor.getString(3));
//					if(cmMap.isEmpty()){
//						ContactModel contactModel=new ContactModel();
//						contactModel.setLookupKey(mContactInfo.getLookupKey());
//						save(contactModel);
//					}else{
//						ContactModel contactModel=cmMap.get(mContactInfo.getLookupKey());
//						if(contactModel==null){
//							contactModel=new ContactModel();
//							contactModel.setLookupKey(mContactInfo.getLookupKey());
////							save(contactModel);
//						}else{
//							mContactInfo.setRecordFlag(contactModel.getRecordFlag());
//							mContactInfo.setUserSetFlag(contactModel.getUserSetFlag());
//						}
//					}
					contactListData.add(mContactInfo);
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return contactListData;
	}
	
	/**
	 * 根据主键获取,记录标识
	 */
	private ContactModel findByKey(String key){
		ContactModel contactModel=null;
//		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = getSQLiteDatabase().query(ContactModel.TABLENAME, new String[]{"key","flag","usersetflag"},
				"key=?", new String[]{key}, null, null, null);
		try{
			if(cursor.moveToFirst()){
				contactModel=new ContactModel();
				contactModel.setPhone(cursor.getString(cursor.getColumnIndex("key")));
				contactModel.setRecordFlag(cursor.getInt(cursor.getColumnIndex("flag")));
				contactModel.setUserSetFlag(cursor.getInt(cursor.getColumnIndex("usersetflag")));
			}
		}finally{
			cursor.close();
		}
		return contactModel;
	}
	/**
	 * 根据唯一键获取当前用户的手机号码列表
	 */
	public List<String> getContactAllPhone(String key){
		List<String> phones=new ArrayList<String>();
        String[] projection = new String[] {
        		ContactsContract.Contacts.Data.DATA1
        };
        String sortOrder = ContactsContract.Contacts.Data.DATA1+ " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(
        		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
        		projection,
        		ContactsContract.Contacts.LOOKUP_KEY+"=?",new String[]{key}, sortOrder);
		try{
			if (cursor.moveToFirst()) {
				do {
					phones.add(cursor.getString(0));
				} while (cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return phones;
	}
	/**
	 * 保存
	 */
	private void save(ContactModel contactModel){
		ContentValues values = new ContentValues();
		values.put("key",contactModel.getLookupKey());
		values.put("flag",contactModel.getRecordFlag());
		values.put("usersetflag", contactModel.getUserSetFlag());
		getSQLiteDatabase().insert(ContactModel.TABLENAME, null, values);
	}
	/**
	 * 修改录音标记 
	 */
	public void modifyFlag(String key,Integer flag){
		ContactModel contactModel=findByKey(key);
		if(contactModel==null){
			contactModel=new ContactModel();
			contactModel.setLookupKey(key);
			contactModel.setRecordFlag(flag);
			contactModel.setUserSetFlag(2);
			save(contactModel);
		}else{
			ContentValues values = new ContentValues();
			values.put("flag", String.valueOf(flag));
			values.put("usersetflag", "2");
			getSQLiteDatabase().update(ContactModel.TABLENAME, values, "key=?", new String[]{key});
		}
	}
	/**
	 * 根据号码获取联系人信息
	 */
	public ContactModel getContactModelByPhone(String phone){
		String[] projection = {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.Contacts.DISPLAY_NAME,
				ContactsContract.Contacts.Data.DATA1, 
				ContactsContract.Contacts.LOOKUP_KEY,
				ContactsContract.Contacts.PHOTO_ID };
		// 获得所有的联系人
		Cursor cursor = getContext().getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				projection, ContactsContract.Contacts.Data.DATA1+" like ?",
				new String[]{StringUtils.convertPhonelike(phone)},null);
		ContactModel mContactInfo=null;
		try{
			if(cursor.moveToFirst()) {
				do{
					String tmpPhone=cursor.getString(2);
					if(StringUtils.convertPhone(tmpPhone).equals(phone)){
						mContactInfo = new ContactModel();
						mContactInfo.setId(cursor.getLong(0));
						mContactInfo.setName(cursor.getString(1));
						mContactInfo.setPhone(tmpPhone);
						mContactInfo.setLookupKey(cursor.getString(3));
						mContactInfo.setPhotoID(cursor.getLong(4));
						ContactModel contactModel=findByKey(mContactInfo.getLookupKey());
						if(contactModel==null){
							contactModel=new ContactModel();
							contactModel.setLookupKey(mContactInfo.getLookupKey());
							save(contactModel);
						}else{
							mContactInfo.setRecordFlag(contactModel.getRecordFlag());
							mContactInfo.setUserSetFlag(contactModel.getUserSetFlag());
						}
						break;
					}
				}while(cursor.moveToNext());
			}
		}finally{
			cursor.close();
		}
		return mContactInfo;
	}
	
	/**
	 * 加载当前联系人头像
	 */
	public Bitmap loadContactPhoto(Long id) {
		InputStream localInputStream = null;
		try {
			Uri localUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
			localInputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(), localUri);
			return BitmapFactory.decodeStream(localInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(localInputStream!=null){
				try {
					localInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					localInputStream=null;
				}
			}
		}
		return null;
	}
	
}