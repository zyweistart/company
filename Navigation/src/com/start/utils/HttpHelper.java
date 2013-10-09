package com.start.utils;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

import android.util.Log;

public class HttpHelper {

	public static final String TAG = "HttpClient";
	
	public static final String HOST = "http://124.160.64.236/";
	public static final String CHECKCODE_URL = HOST + "CheckCode.aspx";
	public static final String LOGON_URL = HOST + "default2.aspx";
	public static final String CHARSET_NAME = "gb2312";
	
	public static final String VIEWSTATE_START = "__VIEWSTATE\" value=\"";
	public static final String VIEWSTATE_END = "\" />";
	

	private static final DefaultHttpClient instance;
	private static URI referrer;
	private static boolean sessionExpired = true;
	private static String queryUrl = null;
	
	static {
		instance = new DefaultHttpClient();
		instance.setRedirectHandler(new DefaultRedirectHandler() {
			
			@Override
			public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
				return super.isRedirectRequested(response, context);
			}
			
			@Override
			public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
				referrer = super.getLocationURI(response, context);
				return referrer;
			}
		});
	}

	public static DefaultHttpClient getHttpClient() {
		return instance; 
	}
	
	public static URI getReferrer() {
		return referrer;
	}
	
	public static HttpResponse post(HttpPost request) {
		HttpResponse response = null;
		try {
			response = instance.execute(request);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return response;
	}

	public static HttpResponse get(HttpGet request) {
		HttpResponse response = null;
		try {
			response = instance.execute(request);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return response;
	}

	public static void updateSession(boolean expired, String url) {
		sessionExpired = expired;
		if (expired) {
			queryUrl = null;
		} else {
			queryUrl = url;
		}
	}

	public static boolean isSessionExpired() {
		return sessionExpired;
	}

	public static String getQueryUrl() {
		return queryUrl;
	}
}
