package cn.edu.csu.iteliter.model;

import weibo4j.model.Status;
import android.os.Parcel;
import android.os.Parcelable;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * weiboitem used for passing between activities
 * 
 * @author hjw
 * 
 */
public class WeiboItem implements Parcelable {

	private Status status;// not able to pass
	private String statusid = "";
	private String userid = "";
	private String username = "";
	private String gender = "";
	private String profileImageUrl = "";
	private String location = "";
	private String content = "";
	private String statusImageUrl = "";
	private String sourceName = "";
	private String sourceContent = "";
	private String sourceImageUrl = "";
	private String from = "";
	private String time = "";

	public WeiboItem(Status status) {
		this.status = status;
		this.statusid = status.getId();
		this.userid = status.getUser().getId();
		this.username = status.getUser().getScreenName();
		this.gender = status.getUser().getGender();
		this.profileImageUrl = status.getUser().getProfileImageUrl();
		this.statusImageUrl = status.getThumbnailPic();
		this.location = status.getUser().getLocation();
		this.content = status.getText();
		if (status.getRetweetedStatus() != null) {
			this.sourceName = status.getRetweetedStatus().getUser().getScreenName();
			this.sourceContent = status.getRetweetedStatus().getText();
			this.sourceImageUrl = status.getRetweetedStatus().getThumbnailPic();
		}
		this.from = status.getSource().getName();
		this.time = WeiboUtil.formatWeiboDate(status.getCreatedAt());
	}

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

	@Override
	public int describeContents() {
		return 0;// just return 0
	}

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

	public static final Parcelable.Creator<WeiboItem> CREATOR = new Parcelable.Creator<WeiboItem>() {
		@Override
		public WeiboItem createFromParcel(Parcel source) {
			return new WeiboItem(source.readString(), source.readString(), source.readString(), source.readString(),
					source.readString(), source.readString(), source.readString(), source.readString(),
					source.readString(), source.readString(), source.readString(), source.readString(),
					source.readString());
		}

		@Override
		public WeiboItem[] newArray(int size) {
			return null;
		}

	};

	public String getStatusid() {
		return statusid;
	}

	public void setStatusid(String statusid) {
		this.statusid = statusid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatusImageUrl() {
		return statusImageUrl;
	}

	public void setStatusImageUrl(String statusImageUrl) {
		this.statusImageUrl = statusImageUrl;
	}

	public String getSourceImageUrl() {
		return sourceImageUrl;
	}

	public void setSourceImageUrl(String sourceImageUrl) {
		this.sourceImageUrl = sourceImageUrl;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getSourceContent() {
		return sourceContent;
	}

	public void setSourceContent(String sourceContent) {
		this.sourceContent = sourceContent;
	}

}
