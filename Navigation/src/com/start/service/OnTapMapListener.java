package com.start.service;

import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class OnTapMapListener extends SimpleOnGestureListener {

	private OnClickListener mListener;

	public OnTapMapListener(OnClickListener listener) {
		this.mListener = listener;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		mListener.onClickAt(e.getX(), e.getY());
		return true;
	}
	
	public interface OnClickListener {
		void onClickAt(float x, float y);
	}

}
