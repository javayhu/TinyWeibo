/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter.customview;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.edu.csu.iteliter.R;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * @filename TimelineListView.java
 * @package cn.edu.csu.iteliter.customview
 * @project TinyWeibo 微微博
 * @description 自定义的微博列表的ListView控件
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午12:07:49
 * @version 1.0
 * 
 */
public class TimelineListView extends ListView implements OnScrollListener {

	/** 释放刷新 */
	private final static int RELEASE_To_REFRESH = 0;

	/** 下拉刷新 */
	private final static int PULL_To_REFRESH = 1;

	/** 正在刷新 */
	private final static int REFRESHING = 2;

	/** 刷新完成 */
	private final static int DONE = 3;

	/** 正在加载 */
	private final static int LOADING = 4;

	/** 下拉的距离与头部高度的比例值 */
	private final static int RATIO = 3;

	/** 布局渲染器 */
	private LayoutInflater inflater;

	/** ListView的头部 */
	private LinearLayout headView;

	/** 提示文本信息 */
	private TextView tipsTextview;

	/** 最近的更新时间 */
	private TextView lastUpdatedTextView;

	/** 箭头图片 */
	private ImageView arrowImageView;

	/** 加载的进度条 */
	private ProgressBar progressBar;

	/** 箭头的旋转动画 */
	private RotateAnimation animation;

	/** 箭头的反向旋转动画 */
	private RotateAnimation reverseAnimation;

	/** 是否已经记录了starty的值 */
	private boolean isRecored;

	/** 头部内容的宽度 */
	private int headContentWidth;

	/** 头部内容的高度 */
	private int headContentHeight;

	/** y的起始坐标 */
	private int startY;

	/** 当前显示的第一个item的索引 */
	private int firstItemIndex;

	/** 当前的状态 */
	private int state;

	/** 是否可以回退，如果可以那么就不用刷新 */
	private boolean isBack;

	/** 刷新事件的监听器 */
	private OnRefreshListener refreshListener;

	/** 是否可以刷新 */
	private boolean isRefreshable;

	/**
	 * 构造器
	 * 
	 * @param context
	 *            Context
	 */
	public TimelineListView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 构造器
	 * 
	 * @param context
	 *            Context
	 * @param attrs
	 *            属性值
	 */
	public TimelineListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 *            Context
	 */
	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.item_weibo_refresh, null);
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * 1000, 0, 0);// hjw

		headView.invalidate();
		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	/*
	 * (non-Javadoc)滚动时回调
	 * 
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	/*
	 * (non-Javadoc)滚动状态改变回调
	 * 
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	/*
	 * (non-Javadoc)触摸事件回调
	 * 
	 * @see android.widget.AbsListView#onTouchEvent(android.view.MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (state != REFRESHING && isRecored && state != LOADING) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {// hjw
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						if ((tempY - startY) / RATIO >= headContentHeight) {// hjw
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
						}
					}
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);// hjw
					}
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);// hjw
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 根据状态改变头部显示的内容
	 */
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText("释放刷新微博");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
			}
			break;
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新微博...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);// hjw
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow_down);
			tipsTextview.setText("微博刷新完毕");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 设置刷新事件的监听器
	 * 
	 * @param refreshListener
	 *            刷新事件的监听器
	 */
	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	/**
	 * The listener interface for receiving onRefresh events. The class that is interested in processing a onRefresh
	 * event implements this interface, and the object created with that class is registered with a component using the
	 * component's <code>addOnRefreshListener<code> method. When
	 * the onRefresh event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see OnRefreshEvent
	 */
	public interface OnRefreshListener {

		/**
		 * 正在刷新
		 */
		public void onRefresh();
	}

	/**
	 * 刷新完成
	 */
	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("刷新完成: " + WeiboUtil.formatWeiboDate(new Date()));
		changeHeaderViewByState();
	}

	/**
	 * On refresh.
	 */
	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();// call the listview on refresh method ,to load new datas
		}
	}

	/**
	 * 检测头部的大小
	 * 
	 * @param child
	 *            子控件
	 */
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {// this is important ! here lpHight <0
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/**
	 * 给ListView设置adapter
	 * 
	 * @param adapter
	 *            adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		lastUpdatedTextView.setText("上次刷新:" + WeiboUtil.formatWeiboDate(new Date()));
		super.setAdapter(adapter);
	}
}