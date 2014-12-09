/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:31
 */
package cn.edu.csu.iteliter.util;

/**
 * @filename DisplayUtil.java
 * @package cn.edu.csu.iteliter.util
 * @project TinyWeibo 微微博
 * @description 显示数据值的转换工具类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午00:53:07
 * @version 1.0
 * 
 */
public class DisplayUtil {

	/**
	 * 像素值转换成dip值
	 * 
	 * @param pxValue
	 *            像素值
	 * @param scale
	 *            缩放比例
	 * @return dip值
	 */
	public static int px2dip(float pxValue, float scale) {
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * dip值转换成像素值
	 * 
	 * @param dipValue
	 *            dip值
	 * @param scale
	 *            缩放比例
	 * @return 像素值
	 */
	public static int dip2px(float dipValue, float scale) {
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 像素值转换成sp值
	 * 
	 * @param pxValue
	 *            像素值
	 * @param fontScale
	 *            文字缩放比例
	 * @return sp值
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * sp值转换成像素值
	 * 
	 * @param spValue
	 *            sp值
	 * @param fontScale
	 *            文字缩放比例
	 * @return 像素值
	 */
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

}