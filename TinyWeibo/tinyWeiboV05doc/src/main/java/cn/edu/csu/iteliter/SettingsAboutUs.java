/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * @filename SettingsAboutUs.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 关于我们
 * @author 孟丹
 * @team China ITElite Team
 * @email 495179346@qq.com
 * @updatetime 2012-12-21 下午2:48:13
 * @version 1.0
 * 
 */
public class SettingsAboutUs extends Activity {

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_aboutus);
	}

	/**
	 * 界面返回
	 * 
	 * @param v
	 *            View组件
	 */
	public void btn_back(View v) {
		finish();
	}
}