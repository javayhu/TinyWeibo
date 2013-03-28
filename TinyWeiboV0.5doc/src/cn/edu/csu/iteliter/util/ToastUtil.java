/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @filename ToastUtil.java
 * @package cn.edu.csu.iteliter.util
 * @project TinyWeibo 微微博
 * @description Toast提示信息的简易包装类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:29:03
 * @version 1.0
 * 
 */
public class ToastUtil {

	/**
	 * 显示长时间的Toast信息
	 * 
	 * @param context
	 *            Context
	 * @param message
	 *            信息
	 */
	public static void showLongToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示短时间的Toast信息
	 * 
	 * @param context
	 *            Context
	 * @param message
	 *            信息
	 */
	public static void showShortToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

}
