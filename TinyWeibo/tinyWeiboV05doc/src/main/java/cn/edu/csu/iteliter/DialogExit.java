/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import cn.edu.csu.iteliter.util.ToastUtil;

/**
 * @filename DialogExit.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 退出的对话框
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午12:37:13
 * @version 1.0
 * 
 */
public class DialogExit extends Activity {

	/** 退出界面的布局 */
	private LinearLayout ll_exit_layout;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_exit);
		ll_exit_layout = (LinearLayout) findViewById(R.id.ll_exit_layout);
		ll_exit_layout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ToastUtil.showShortToast(getApplicationContext(), "点击窗口外部关闭窗口！");
			}
		});
	}

	/*
	 * (non-Javadoc)触摸事件的回调方法
	 * 
	 * @see android.app.Activity#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	/**
	 * 取消退出
	 * 
	 * @param v
	 *            按钮组件
	 */
	public void btn_exit_cancle(View v) {
		finish();
	}

	/**
	 * 确定退出
	 * 
	 * @param v
	 *            按钮组件
	 */
	public void btn_exit_ok(View v) {
		finish();
		MainWeibo.instance.finish();
	}

}
