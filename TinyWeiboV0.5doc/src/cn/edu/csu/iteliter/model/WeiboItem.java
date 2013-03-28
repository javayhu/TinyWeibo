/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:33
 */
package cn.edu.csu.iteliter.model;

import weibo4j.model.Status;
import android.os.Parcel;
import android.os.Parcelable;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * @filename WeiboItem.java
 * @package cn.edu.csu.iteliter.model
 * @project TinyWeibo 微微博
 * @description 一条微博内容中的重要信息封装类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:50:03
 * @version 1.0
 * 
 */
public class WeiboItem implements Parcelable {

	/** 微博ID */
	private String statusid = "";

	/** 用户ID */
	private String userid = "";

	/** 用户昵称 */
	private String username = "";

	/** 用户性别 */
	private String gender = "";

	/** 用户头像URL */
	private String profileImageUrl = "";

	/** 用户所在地 */
	private String location = "";

	/** 微博内容 */
	private String content = "";

	/** 微博的图片URL地址 */
	private String statusImageUrl = "";

	/** 微博来源的用户昵称 */
	private String sourceName = "";

	/** 微博来源的原始内容 */
	private String sourceContent = "";

	/** 微博来源的原始图片的URL */
	private String sourceImageUrl = "";

	/** 微博信息来源 */
	private String from = "";

	/** 微博发布时间 */
	private String time = "";

	/**
	 * 构造器
	 * 
	 * @param status
	 *            一条微博
	 */
	public WeiboItem(Status status) {
		statusid = status.getId();
		userid = status.getUser().getId();
		username = status.getUser().getScreenName();
		gender = status.getUser().getGender();
		profileImageUrl = status.getUser().getProfileImageUrl();
		statusImageUrl = status.getThumbnailPic();
		location = status.getUser().getLocation();
		content = status.getText();
		if (status.getRetweetedStatus() != null) {
			sourceName = status.getRetweetedStatus().getUser().getScreenName();
			sourceContent = status.getRetweetedStatus().getText();
			sourceImageUrl = status.getRetweetedStatus().getThumbnailPic();
		}
		from = status.getSource().getName();
		time = WeiboUtil.formatWeiboDate(status.getCreatedAt());
	}

	/**
	 * 构造器
	 * 
	 * @param statusid
	 *            the statusid
	 * @param userid
	 *            the userid
	 * @param username
	 *            the username
	 * @param gender
	 *            the gender
	 * @param profileImageUrl
	 *            the profile image url
	 * @param location
	 *            the location
	 * @param content
	 *            the content
	 * @param statusImageUrl
	 *            the status image url
	 * @param sourceName
	 *            the source name
	 * @param sourceContent
	 *            the source content
	 * @param sourceImageUrl
	 *            the source image url
	 * @param from
	 *            the from
	 * @param time
	 *            the time
	 */
	public WeiboItem(String statusid, String userid, String username, String gender, String profileImageUrl,
			String location, String content, String statusImageUrl, String sourceName, String sourceContent,
			String sourceImageUrl, String from, String time) {
		this.statusid = statusid;
		this.userid = userid;
		this.username = username;
		this.gender = gender;
		this.profileImageUrl = profileImageUrl;
		this.location = location;
		this.content = content;
		this.statusImageUrl = statusImageUrl;
		this.sourceName = sourceName;
		this.sourceContent = sourceContent;
		this.sourceImageUrl = sourceImageUrl;
		this.from = from;
		this.time = time;
	}

	/*
	 * (non-Javadoc) 描述内容
	 * 
	 * @see android.os.Parcelable#describeContents()
	 */
	@Override
	public int describeContents() {
		return 0;// just return 0
	}

	/*
	 * (non-Javadoc) 写出[序列化]
	 * 
	 * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {// write
		dest.writeString(statusid);
		dest.writeString(userid);
		dest.writeString(username);
		dest.writeString(gender);
		dest.writeString(profileImageUrl);
		dest.writeString(location);
		dest.writeString(content);
		dest.writeString(statusImageUrl);
		dest.writeString(sourceName);
		dest.writeString(sourceContent);
		dest.writeString(sourceImageUrl);
		dest.writeString(from);
		dest.writeString(time);
	}

	/** Parcelable 对象的产生器 */
	public static final Parcelable.Creator<WeiboItem> CREATOR = new Parcelable.Creator<WeiboItem>() {

		/*
		 * (non-Javadoc)反序列化
		 * 
		 * @see android.os.Parcelable.Creator#createFromParcel(android.os.Parcel)
		 */
		public WeiboItem createFromParcel(Parcel source) {
			return new WeiboItem(source.readString(), source.readString(), source.readString(), source.readString(),
					source.readString(), source.readString(), source.readString(), source.readString(),
					source.readString(), source.readString(), source.readString(), source.readString(),
					source.readString());
		}

		public WeiboItem[] newArray(int size) {
			return null;
		}

	};

	/**
	 * Gets the statusid.
	 * 
	 * @return the statusid
	 */
	public String getStatusid() {
		return statusid;
	}

	/**
	 * Sets the statusid.
	 * 
	 * @param statusid
	 *            the new statusid
	 */
	public void setStatusid(String statusid) {
		this.statusid = statusid;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username
	 *            the new username
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * Gets the profile image url.
	 * 
	 * @return the profile image url
	 */
	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	/**
	 * Sets the profile image url.
	 * 
	 * @param profileImageUrl
	 *            the new profile image url
	 */
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	/**
	 * Gets the gender.
	 * 
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets the gender.
	 * 
	 * @param gender
	 *            the new gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location
	 *            the new location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets the time.
	 * 
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets the time.
	 * 
	 * @param time
	 *            the new time
	 */
	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * Gets the content.
	 * 
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 * 
	 * @param content
	 *            the new content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the status image url.
	 * 
	 * @return the status image url
	 */
	public String getStatusImageUrl() {
		return statusImageUrl;
	}

	/**
	 * Sets the status image url.
	 * 
	 * @param statusImageUrl
	 *            the new status image url
	 */
	public void setStatusImageUrl(String statusImageUrl) {
		this.statusImageUrl = statusImageUrl;
	}

	/**
	 * Gets the source image url.
	 * 
	 * @return the source image url
	 */
	public String getSourceImageUrl() {
		return sourceImageUrl;
	}

	/**
	 * Sets the source image url.
	 * 
	 * @param sourceImageUrl
	 *            the new source image url
	 */
	public void setSourceImageUrl(String sourceImageUrl) {
		this.sourceImageUrl = sourceImageUrl;
	}

	/**
	 * Gets the from.
	 * 
	 * @return the from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * Sets the from.
	 * 
	 * @param from
	 *            the new from
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * Gets the source name.
	 * 
	 * @return the source name
	 */
	public String getSourceName() {
		return sourceName;
	}

	/**
	 * Sets the source name.
	 * 
	 * @param sourceName
	 *            the new source name
	 */
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	/**
	 * Gets the source content.
	 * 
	 * @return the source content
	 */
	public String getSourceContent() {
		return sourceContent;
	}

	/**
	 * Sets the source content.
	 * 
	 * @param sourceContent
	 *            the new source content
	 */
	public void setSourceContent(String sourceContent) {
		this.sourceContent = sourceContent;
	}

}
