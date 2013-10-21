package com.start;

import java.util.Random;

import android.content.Context;

import com.start.model.medmap.UserLocation;

public abstract class Util{
	
	private static Random random = new Random();
	
	public static void loadMapFiles(Context context){
//		try {
//			IOUtils.copy(context.getResources().openRawResource(R.raw.map1), new FileOutputStream(new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "1.map")));
//			IOUtils.copy(context.getResources().openRawResource(R.raw.map2), new FileOutputStream(new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "2.map")));
//			IOUtils.copy(context.getResources().openRawResource(R.raw.map0), new FileOutputStream(new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "all.map")));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static UserLocation locateUser(){
		UserLocation userLocation = new UserLocation();
		userLocation.setMapId(1);
		userLocation.setLatitude(0.0007107 - random.nextFloat() * 0.0001);
		userLocation.setLongitude(0.0015564 - random.nextFloat() * 0.0001);
		return userLocation;
	}
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
	    double earthRadius = 3958.75;
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    return earthRadius * c;
	}
}
