package com.ancun.widget;

import java.util.Vector;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ScrollLayout extends ViewGroup {

	private Scroller mScroller;//生成滑动条对象
	private VelocityTracker mVelocityTracker;
	private int mCurScreen;
	private int mDefaultScreen = 0;//默认screen
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;
	private static final int SNAP_VELOCITY = 500;
	private int mTouchState = TOUCH_STATE_REST;
	private int mTouchSlop;
	private float mLastMotionX;
	private int sensitivity = 30;
	private boolean spring;
	private Vector<LayoutChangeListener> listeners;
	
	//构造函数
	public ScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
		mCurScreen = mDefaultScreen;
		//计算子视图能滑动的距离
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		listeners = new Vector<LayoutChangeListener>();
	}

	public void addChangeListener(LayoutChangeListener listener) {
		listeners.add(listener);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		//记录滚动视图组有多少个子视图 
		final int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);//获取子视图
			if (childView.getVisibility() != View.GONE) {//判断当前子视图是否可视
				final int childWidth = childView.getMeasuredWidth();//获取母体宽度
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight()); //设置子体位置
				childLeft += childWidth;//记录子体左边边界位置 
			}
		}
	}

	
	   //view.View的子函数,需要重写
    //执行初始的判断测量，滑到初始位置，其他时候不产生作用
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//MeasureSpec是全局静态变量View的成员变量，封装传递从父到子的布局要求。 
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(//如果父母没有为子窗体提供合适的宽度，则抛出异常
					"ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {//如果父母没有为子窗体提供合适的高度，则抛出异常
			throw new IllegalStateException(
					"ScrollLayout only can run at EXACTLY mode!");
		}

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();//记录子视图个数 
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}
	//判断当前滚动坐标是否超过屏幕一半
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		//Log.i("0进入","aa1");
		int lastIndex = mCurScreen;
		//计算当前应该显示的是第几个子窗体（0、1、2、3）
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		//Log.i("1进入 getChildCount "+(getChildCount()-1),"aa1");
		//判断当前子窗体是否停在准确位置，不准确则运行 
		if (getScrollX() != (whichScreen * getWidth())) {

			//记录当前距离标准位置坐标的距离
			final int delta = whichScreen * getWidth() - getScrollX();
			//设置子窗体滑动的横向起点、纵向起点、横向位移、纵向位移、移动时间（微秒）
			mScroller.startScroll(getScrollX(), 0, delta, 0,
					 Math.abs(delta)/3);
			mCurScreen = whichScreen;
			 
			invalidate(); // Redraw the layout
			 
		} //Log.i("3进入 curindex"+whichScreen,"aa1");
		for (LayoutChangeListener listener : listeners)
			listener.doChange(lastIndex, whichScreen);
		
		//Log.i("4进入 lastIndex"+lastIndex,"aa1");
	}

	public void setToScreen(int whichScreen) {
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		mCurScreen = whichScreen;
		scrollTo(whichScreen * getWidth(), 0);
	}

	public int getCurScreen() {
		return mCurScreen;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	public boolean isSpring() {
		return spring;
	}

	public void setSpring(boolean spring) {
		this.spring = spring;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mVelocityTracker == null)
			mVelocityTracker = VelocityTracker.obtain();
		mVelocityTracker.addMovement(event);
		final int action = event.getAction();
		final float x = event.getX();
		switch (action) {
		case MotionEvent.ACTION_DOWN://如果中途结束了进程。则中断动作
			if (!mScroller.isFinished())
				mScroller.abortAnimation();
			mLastMotionX = x;//获取第当前横坐标
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);//记录移动的距离
			if (Math.abs(deltaX) > sensitivity) {
				// 左滑动为正数、右为负数
				if (spring) {
					scrollBy(deltaX, 0);
					mLastMotionX = x;
				} else {
					final int childCount = getChildCount();
					boolean max = mCurScreen < childCount - 1;
					boolean min = mCurScreen > 0;
					boolean canMove = deltaX > 0 ? (max ? true : false)
							: (min ? true : false);
					if (canMove) {
						scrollBy(deltaX, 0);
						mLastMotionX = x;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST))
			return true;
		final float x = ev.getX();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop)
				mTouchState = TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}
	
	public interface LayoutChangeListener {
		public void doChange(int lastIndex, int currentIndex);
	}
	
}