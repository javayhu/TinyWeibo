/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-21 11:48:06
 */
package cn.edu.csu.iteliter.model;

import com.weibo.sdk.android.Oauth2AccessToken;

/**
 * 
 * @filename UserData.java
 * @package cn.edu.csu.iteliter.model
 * @project TinyWeibo 微微博
 * @description 用户数据信息
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:44:17
 * @version 1.0
 */
public class UserData {

	/** access token. */
	private String token;

	/** 失效时间. */
	private String expirestime;

	/** 用户ID. */
	private String userid;

	/** 用户昵称. */
	private String nickname = "";

	/** 用户头像URL地址. */
	private String profileimage = "";

	/** 是否关注作者. */
	private boolean followauthor = false;

	/** 是否开启声效. */
	private boolean soundPlay = true;

	/** 是否第一次运行. */
	private boolean firstrun = true;

	/**
	 * 构造函数.
	 * 
	 * @param token
	 *            access token
	 * @param expirestime
	 *            失效时间
	 * @param userid
	 *            用户ID
	 */
	public UserData(String token, String expirestime, String userid) {
		super();
		this.token = token;
		this.expirestime = expirestime;
		this.userid = userid;
	}

	/**
	 * 获取oauth2认证的access token.
	 * 
	 * @return oauth2 access token
	 */
	public Oauth2AccessToken obtainOauth2AccessToken() {
		return new Oauth2AccessToken(token, expirestime);
	}

	/**
	 * Gets the token.
	 * 
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 * 
	 * @param token
	 *            the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets the expirestime.
	 * 
	 * @return the expirestime
	 */
	public String getExpirestime() {
		return expirestime;
	}

	/**
	 * Sets the expirestime.
	 * 
	 * @param expirestime
	 *            the new expirestime
	 */
	public void setExpirestime(String expirestime) {
		this.expirestime = expirestime;
	}

	/**
	 * Gets the userid.
	 * 
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}

	/**
	 * Sets the userid.
	 * 
	 * @param userid
	 *            the new userid
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * Gets the nickname.
	 * 
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Sets the nickname.
	 * 
	 * @param nickname
	 *            the new nickname
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Gets the profileimage.
	 * 
	 * @return the profileimage
	 */
	public String getProfileimage() {
		return profileimage;
	}

	/**
	 * Sets the profileimage.
	 * 
	 * @param profileimage
	 *            the new profileimage
	 */
	public void setProfileimage(String profileimage) {
		this.profileimage = profileimage;
	}

	/**
	 * Checks if is the followauthor.
	 * 
	 * @return the followauthor
	 */
	public boolean isFollowauthor() {
		return followauthor;
	}

	/**
	 * Sets the followauthor.
	 * 
	 * @param followauthor
	 *            the new followauthor
	 */
	public void setFollowauthor(boolean followauthor) {
		this.followauthor = followauthor;
	}

	/**
	 * Checks if is the sound play.
	 * 
	 * @return the sound play
	 */
	public boolean isSoundPlay() {
		return soundPlay;
	}

	/**
	 * Sets the sound play.
	 * 
	 * @param soundPlay
	 *            the new sound play
	 */
	public void setSoundPlay(boolean soundPlay) {
		this.soundPlay = soundPlay;
	}

	/**
	 * Checks if is the firstrun.
	 * 
	 * @return the firstrun
	 */
	public boolean isFirstrun() {
		return firstrun;
	}

	/**
	 * Sets the firstrun.
	 * 
	 * @param firstrun
	 *            the new firstrun
	 */
	public void setFirstrun(boolean firstrun) {
		this.firstrun = firstrun;
	}

}
