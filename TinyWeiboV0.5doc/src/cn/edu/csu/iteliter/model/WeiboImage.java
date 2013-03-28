/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * @filename WeiboImage.java
 * @package cn.edu.csu.iteliter.model
 * @project TinyWeibo 微微博
 * @description 微博图片
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:48:58
 * @version 1.0
 * 
 */
public class WeiboImage {

	/** ImageView 控件 */
	public ImageView imageView;// control

	/** 图片的URL地址 */
	public String imageurl;// url

	/** Bitmap信息 */
	public Bitmap bitmap;// bitmap

	/**
	 * 构造器
	 * 
	 * @param imageView
	 *            ImageView 控件
	 * @param imageurl
	 *            图片的URL地址
	 * @param bitmap
	 *            Bitmap信息
	 */
	public WeiboImage(ImageView imageView, String imageurl, Bitmap bitmap) {
		this.imageView = imageView;
		this.imageurl = imageurl;
		this.bitmap = bitmap;
	}

}
