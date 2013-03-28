package cn.edu.csu.iteliter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import cn.edu.csu.iteliter.model.UserData;

/**
 * user data tool
 * 
 * @author hjw
 */
public class UserDataUtil {

	public static final String EXPIRESTIME = "expirestime";
	public static final String FIRSTRUN = "firstrun";
	public static final String FOLLOW = "follow";
	public static final String NICKNAME = "nickname";
	public static final String PROFILE = "profileimage";
	public static final String SOUND = "sound";
	public static final String TOKEN = "token";
	public static final String USERID = "userid";

	/**
	 * clear userdata
	 */
	public static void clearUserData(Context context) {
		SharedPreferences.Editor editor = context.getSharedPreferences(ConstantUtil.TINYWEIBO, Context.MODE_PRIVATE)
				.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * validate token
	 */
	public static boolean isTokenValid(String accessToken, String time) {
		long expirestime = Long.parseLong(time);
		return (!TextUtils.isEmpty(accessToken) && ((expirestime == 0) || (System.currentTimeMillis() < expirestime)));
	}

	/**
	 * read user data
	 */
	public static UserData readUserData(Context context) {
		SharedPreferences pref = context.getSharedPreferences(ConstantUtil.TINYWEIBO, Context.MODE_PRIVATE);
		return new UserData(pref.getString(TOKEN, ""), pref.getString(EXPIRESTIME, "0"), pref.getString(USERID, ""));
	}

	/**
	 * update userdata
	 */
	public static void updateLocalToken(Context context, UserData userData) {
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
}
