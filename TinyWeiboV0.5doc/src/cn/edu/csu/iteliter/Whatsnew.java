/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:31
 */
package cn.edu.csu.iteliter;

import java.util.ArrayList;

import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.util.UserDataUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

/**
 * @filename Whatsnew.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 欢迎界面
 * @author 孟丹
 * @team China ITElite Team
 * @email 495179346@qq.com
 * @updatetime 2012-12-21 下午3:11:59
 * @version 1.0
 * 
 */
public class Whatsnew extends Activity {

	/** viewpager组件 */
	private ViewPager mViewPager;

	/** 第1个page */
	private ImageView mPage0;

	/** 第2个page */
	private ImageView mPage1;

	/** 第3个page */
	private ImageView mPage2;

	/** 第4个page */
	private ImageView mPage3;

	/** 第5个page. */
	private ImageView mPage4;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whatsnew_viewpager);
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		mPage0 = (ImageView) findViewById(R.id.page0);// round dot
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);
		mPage4 = (ImageView) findViewById(R.id.page4);

		LayoutInflater layoutInflater = LayoutInflater.from(this);// views
		View view1 = layoutInflater.inflate(R.layout.whatsnew1, null);
		View view2 = layoutInflater.inflate(R.layout.whatsnew2, null);
		View view3 = layoutInflater.inflate(R.layout.whatsnew3, null);
		View view4 = layoutInflater.inflate(R.layout.whatsnew4, null);
		View view5 = layoutInflater.inflate(R.layout.whatsnew5, null);

		final ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view5);

		// viewpager的内容adapter
		PagerAdapter mPagerAdapter = new PagerAdapter() {

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mViewPager.setAdapter(mPagerAdapter);
	}

	/**
	 * The listener interface for receiving myOnPageChange events. The class that is interested in processing a myOnPageChange event
	 * implements this interface, and the object created with that class is registered with a component using the component's
	 * <code>addMyOnPageChangeListener<code> method. When
	 * the myOnPageChange event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see MyOnPageChangeEvent
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		/*
		 * (non-Javadoc)选中某一个page的回调方法
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
		 */
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				unselectall();
				mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				break;
			case 1:
				unselectall();
				mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				break;
			case 2:
				unselectall();
				mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				break;
			case 3:
				unselectall();
				mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				break;
			case 4:
				unselectall();
				mPage4.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
				break;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/**
	 * 点击 开始体验
	 * 
	 * @param v
	 *            View组件
	 */
	public void btn_start(View v) {
		UserData userData = UserDataUtil.readUserData(getApplicationContext());
		userData.setFirstrun(false);
		UserDataUtil.updateUserData(getApplicationContext(), userData);
		Intent intent = new Intent();
		intent.setClass(Whatsnew.this, WhatsnewDoor.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 全不选
	 */
	private void unselectall() {
		mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page));
		mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
		mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
		mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));
		mPage4.setImageDrawable(getResources().getDrawable(R.drawable.page));
	}

}
