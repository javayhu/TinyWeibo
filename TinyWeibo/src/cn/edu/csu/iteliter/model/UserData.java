package cn.edu.csu.iteliter.model;

import com.weibo.sdk.android.Oauth2AccessToken;

/**
 * user data,saved in sharedpreference
 * 
 * @author hjw
 * 
 */
public class UserData {

	private String expirestime;// long
	private boolean firstrun = true;
	private boolean followauthor = false;
	private String nickname = "";// user nick name
	private String profileimage = "";// user profile image url
	private boolean soundPlay = true;
	private String token;// access token
	private String userid;// user id

	public UserData(String token, String expirestime, String userid) {
		super();
		this.token = token;
		this.expirestime = expirestime;
		this.userid = userid;
	}

	public String getExpirestime() {
		return expirestime;
	}

	public String getNickname() {
		return nickname;
	}

	public String getProfileimage() {
		return profileimage;
	}

	public String getToken() {
		return token;
	}

	public String getUserid() {
		return userid;
	}

	public boolean isFirstrun() {
		return firstrun;
	}

	public boolean isFollowauthor() {
		return followauthor;
	}

	public boolean isSoundPlay() {
		return soundPlay;
	}

	public Oauth2AccessToken obtainOauth2AccessToken() {
		return new Oauth2AccessToken(token, expirestime);
	}

	public void setExpirestime(String expirestime) {
		this.expirestime = expirestime;
	}

	public void setFirstrun(boolean firstrun) {
		this.firstrun = firstrun;
	}

	public void setFollowauthor(boolean followauthor) {
		this.followauthor = followauthor;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setProfileimage(String profileimage) {
		this.profileimage = profileimage;
	}

	public void setSoundPlay(boolean soundPlay) {
		this.soundPlay = soundPlay;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

}
