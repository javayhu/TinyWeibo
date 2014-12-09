/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import cn.edu.csu.iteliter.MainWeibo;
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.model.WeiboImage;

/**
 * @filename WeiboUtil.java
 * @package cn.edu.csu.iteliter.util
 * @project TinyWeibo 微微博
 * @description 微博处理工具类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:36:41
 * @version 1.0
 * 
 */
public class WeiboUtil {

	/** 日期时间的格式 */
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm MM-dd");

	/**
	 * 格式化微博的日期
	 * 
	 * @param date
	 *            微博日期
	 * @return 格式化之后的字符串
	 */
	public static String formatWeiboDate(Date date) {
		return simpleDateFormat.format(date);
	}

	/**
	 * 格式化占用的空间大小
	 * 
	 * @param spaceUsed
	 *            使用的空间大小
	 * @return 空间大小的字符串表示
	 */
	public static String formatSpaceSize(double spaceUsed) {
		if (spaceUsed < 1) {
			return "不到 1M";
		} else {
			DecimalFormat decimalFormat = new DecimalFormat("#0.00");
			return decimalFormat.format(spaceUsed) + "M";
		}
	}

	/**
	 * 得到所需的bitmap
	 * 
	 * @param cachePath
	 *            图片的缓存文件夹
	 * @param profileImageUrl
	 *            头像的URL
	 * @param handler
	 *            要发送和接收message的Handler
	 * @param imageView
	 *            ImageView 控件
	 * @param type
	 *            图片类型
	 */
	public static void restoreBitmap(String cachePath, String profileImageUrl, Handler handler, ImageView imageView,
			int type) {
		Bitmap image = CacheUtil.restoreBitmap(cachePath, profileImageUrl);
		if (image != null) {
			image = ImageUtil.getRoundedCornerBitmap(image);// round it
			imageView.setImageBitmap(image);
		} else {
			asyncLoadWeiboImage(handler, new WeiboImage(imageView, profileImageUrl, null), type);
		}
	}

	/**
	 * 得到意见反馈的前缀
	 * 
	 * @return 意见反馈的前缀
	 */
	public static String getSuggetionPrefix() {
		StringBuffer stringBuffer = new StringBuffer(ConstantUtil.SUGGESTION_PREFIX);
		stringBuffer.append("型号 " + android.os.Build.MODEL + ",");
		stringBuffer.append("系统 " + android.os.Build.VERSION.RELEASE + ",反馈意见: ");
		return stringBuffer.toString();
	}

	/**
	 * 异步加载用户信息
	 * 
	 * @param handler
	 *            要发送和接收message的Handler
	 */
	public static void asyncLoadUserData(final Handler handler) {
		final UserData userData = MainWeibo.userData;
		new Thread(new Runnable() {
			public void run() {
				Users users = new Users();
				users.client.setToken(userData.getToken());
				User user = null;
				try {
					user = users.showUserById(userData.getUserid());// here may be blocked!
				} catch (WeiboException e) {
				}
				Message message = new Message();
				message.obj = user;
				message.what = ConstantUtil.MESSAGE_TYPE_USERDATA;
				handler.sendMessage(message);
			}
		}).start();
	}

	/**
	 * 异步加载微博图片
	 * 
	 * @param handler
	 *            要发送和接收message的Handler
	 * @param weiboImage
	 *            封装的微博图片对象
	 * @param type
	 *            图片类型
	 */
	public static void asyncLoadWeiboImage(final Handler handler, final WeiboImage weiboImage, final int type) {
		new Thread(new Runnable() {
			public void run() {
				URL url = null;
				try {
					url = new URL(weiboImage.imageurl);// protocol not found
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setDoInput(true);
					connection.setUseCaches(false);
					Bitmap bitmap = null;
					if (connection.getInputStream() != null) {// connection reset by peer
						bitmap = BitmapFactory.decodeStream(connection.getInputStream());
					}
					if (bitmap == null) {
						return;
					}
					weiboImage.bitmap = ImageUtil.getRoundedCornerBitmap(bitmap);

					Message message = new Message();
					message.what = ConstantUtil.MESSAGE_TYPE_WEIBOIMAGE;
					message.obj = weiboImage;
					handler.sendMessage(message);

					if (type == ConstantUtil.IMAGE_TYPE_PROFILE) {
						CacheUtil.saveImageToPath(weiboImage.bitmap,
								CacheUtil.PROFILE_CACHE_PATH + EncryptDecryptUtil.encrypt(weiboImage.imageurl));
					} else if (type == ConstantUtil.IMAGE_TYPE_IMAGE) {
						CacheUtil.saveImageToPath(weiboImage.bitmap,
								CacheUtil.IMAGE_CACHE_PATH + EncryptDecryptUtil.encrypt(weiboImage.imageurl));
					}

				} catch (Exception e) {
				}
			}
		}).start();
	}
}
