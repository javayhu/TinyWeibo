/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:31
 */
package cn.edu.csu.iteliter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import cn.edu.csu.iteliter.model.UserData;

/**
 * @filename UserDataUtil.java
 * @package cn.edu.csu.iteliter.util
 * @project TinyWeibo 微微博
 * @description 用户数据的工具处理类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:32:11
 * @version 1.0
 * 
 */
public class UserDataUtil {

	/** 用户ID */
	public static final String USERID = "userid";

	/** access token */
	public static final String TOKEN = "token";

	/** token过期时间 */
	public static final String EXPIRESTIME = "expirestime";

	/** 用户的头像URL */
	public static final String PROFILE = "profileimage";

	/** 用户的昵称 */
	public static final String NICKNAME = "nickname";

	/** 是否关注作者微博 */
	public static final String FOLLOW = "follow";

	/** 是否开启声音效果 */
	public static final String SOUND = "sound";

	/** 是否是第一次运行 */
	public static final String FIRSTRUN = "firstrun";

	/**
	 * 检查accessToken是否有效
	 * 
	 * @param accessToken
	 *            accessToken
	 * @param time
	 *            失效时间
	 * @return 如果有效返回true
	 */
	public static boolean isTokenValid(String accessToken, String time) {
		long expirestime = Long.parseLong(time);
		return (!TextUtils.isEmpty(accessToken) && (expirestime == 0 || (System.currentTimeMillis() < expirestime)));
	}

	/**
	 * 更新userData信息
	 * 
	 * @param context
	 *            Context
	 * @param userData
	 *            userData信息
	 */
	public static void updateUserData(Context context, UserData userData) {
		SharedPreferences.Editor editor = context.getSharedPreferences(ConstantUtil.TINYWEIBO, Context.MODE_PRIVATE)
				.edit();
		editor.putString(USERID, userData.getUserid());
		editor.putString(TOKEN, userData.getToken());
		editor.putString(EXPIRESTIME, userData.getExpirestime());
		editor.putString(NICKNAME, userData.getNickname());
		editor.putString(PROFILE, userData.getProfileimage());
		editor.putBoolean(FOLLOW, userData.isFollowauthor());
		editor.putBoolean(SOUND, userData.isSoundPlay());
		editor.putBoolean(FIRSTRUN, userData.isFirstrun());
		editor.commit();
	}

	/**
	 * 删除userData
	 * 
	 * @param context
	 *            Context
	 */
	public static void clearUserData(Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(ConstantUtil.TINYWEIBO, Context.MODE_PRIVATE)
				.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * 读取userData
	 * 
	 * @param context
	 *            Context
	 * @return 用户信息
	 */
	public static UserData readUserData(Context context) {
		SharedPreferences pref = context.getSharedPreferences(ConstantUtil.TINYWEIBO, Context.MODE_PRIVATE);
		return new UserData(pref.getString(TOKEN, ""), pref.getString(EXPIRESTIME, "0"), pref.getString(USERID, ""));
	}
}
