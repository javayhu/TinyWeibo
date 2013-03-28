/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

/**
 * @filename CacheUtil.java
 * @package cn.edu.csu.iteliter.util
 * @project TinyWeibo 微微博
 * @description 缓存数据处理工具类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-20 下午11:57:52
 * @version 1.0
 * 
 */
public class CacheUtil {

	/** 常量：微微博的缓存根目录 */
	public static final String TINYWEIBO_ROOT_PATH = android.os.Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "tinyweibo" + File.separator;

	/** 常量：微博用户的头像缓存路径 */
	public static final String PROFILE_CACHE_PATH = TINYWEIBO_ROOT_PATH + "profileimage" + File.separator;

	/** 常量：微博中的图片的缓存路径 */
	public static final String IMAGE_CACHE_PATH = TINYWEIBO_ROOT_PATH + "weiboimage" + File.separator;

	/** 常量：发送微博时拍照或者选择图片的缓存路径 */
	public static final String PICTURE_CACHE_PATH = TINYWEIBO_ROOT_PATH + "takenpicture" + File.separator;

	/*
	 * 静态代码块：初始化时创建目录
	 */
	static {
		File file = new File(TINYWEIBO_ROOT_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(PROFILE_CACHE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(IMAGE_CACHE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(PICTURE_CACHE_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 根据指定的文件夹路径和图片的URL地址来得到图片的Bitmap
	 * 
	 * @param parentpath
	 *            图片所在的文件夹
	 * @param imageurl
	 *            图片的URL地址
	 * @return 图片的Bitmap
	 */
	public static Bitmap restoreBitmap(String parentpath, String imageurl) {
		String path = parentpath + EncryptDecrypt.encrypt(imageurl);
		File file = new File(path);
		if (file.exists()) {
			try {
				return BitmapFactory.decodeStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 保存图片的bitmap信息到指定的路径
	 * 
	 * @param bitmap
	 *            图片的bitmap信息
	 * @param imagepath
	 *            要保存的路径
	 */
	public static void saveImageToPath(Bitmap bitmap, String imagepath) {
		File file = new File(imagepath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));// quality = 100
		} catch (Exception e) {
		}
	}

	/**
	 * 清空缓存
	 */
	public static void clearCache() {
		deleteDirectory(IMAGE_CACHE_PATH);
		deleteDirectory(PICTURE_CACHE_PATH);
		deleteDirectory(PROFILE_CACHE_PATH);
	}

	/**
	 * 删除指定的文件夹
	 * 
	 * @param cachePath
	 *            缓存文件夹路径
	 */
	private static void deleteDirectory(String cachePath) {
		File file = new File(cachePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
		}
	}

	/**
	 * 计算使用的空间大小
	 * 
	 * @return 空间大小
	 */
	public static double calculateSpaceUsed() {
		return getSize(new File(TINYWEIBO_ROOT_PATH));
	}

	/**
	 * 得到指定文件以及它的子文件的大小总和
	 * 
	 * @param file
	 *            文件路径
	 * @return 大小
	 */
	public static double getSize(File file) {
		if (file.exists()) {
			if (!file.isFile()) {
				File[] fl = file.listFiles();
				double ss = 0;
				for (File f : fl) {
					ss += getSize(f);
				}
				return ss;
			} else {
				double ss = (double) file.length() / 1024 / 1024;
				return ss;
			}
		} else {
			return 0.0;
		}
	}

}
