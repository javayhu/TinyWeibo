/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter;

import java.util.Random;

import weibo4j.Place;
import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.edu.csu.iteliter.listener.ShakeListener;
import cn.edu.csu.iteliter.listener.ShakeListener.OnShakeListener;
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.model.WeiboImage;
import cn.edu.csu.iteliter.model.WeiboItem;
import cn.edu.csu.iteliter.util.CacheUtil;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.NetworkUtil;
import cn.edu.csu.iteliter.util.ToastUtil;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * @filename ShakeWeibo.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 摇微博的界面
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午2:50:21
 * @version 1.0
 * 
 */
public class ShakeWeibo extends Activity {

	/** 最多显示的微博内容的长度 */
	private static final int MAX_CONTENT_LENGTH = 50;

	/** 监听手机晃动的接口 */
	private ShakeListener mShakeListener;

	/** 界面中央图片的上部分 */
	private RelativeLayout rl_shake_handup;

	/** 界面中央图片的下部分 */
	private RelativeLayout rl_shake_handdown;

	/** 摇出来的微博组件 */
	private RelativeLayout rl_shake_result;

	/** 寻找微博的加载部分组件 */
	private RelativeLayout rl_shake_loading;

	/** 摇微博界面的标题栏的标题 */
	private TextView tv_shake_title;

	/** 用户的昵称 */
	private TextView tv_shakeweiboitem_name;

	/** 微博的内容 */
	private TextView tv_shakeweiboitem_content;

	/** 用户的距离 */
	private TextView tv_shakeweiboitem_distance;

	/** 用户的性别 */
	private ImageView iv_shakeweiboitem_gender;

	/** 用户的头像 */
	private ImageView iv_shakeweiboitem_head;

	/** 摇微博的类型 */
	private int shaketype;// shake type

	/** 要摇的微博的类型 */
	private int weibotype;// weibo type

	/** 处理message的handler */
	private Handler shakeHandler;

	/** 微博 */
	private Status status;

	/** 用户 */
	private User user;

	/** 用户数据 */
	private UserData userData;

	/** 振动器 */
	private Vibrator mVibrator;

	/** 定位器 */
	private LocationManager locationManager;

	/** 网络提供定位 */
	private String network_provider;

	/** GPS提供定位 */
	private String gps_provider;

	/** 摇出来的结果值 */
	private int shake_result;

	/** 没有网络 */
	private final int NO_NETWORK = 0;

	/** 找不到位置 */
	private final int NO_LOCATION = 1;

	/** 没有找到 */
	private final int NO_MATCH = 2;

	/** 找到 */
	private final int MATCH = 3;

	/** 声音效果 */
	private int[] sounds = { R.raw.shake_sound, R.raw.shake_match, R.raw.shake_nomatch };

	/** 声效播放器 */
	private MediaPlayer[] soundPlayers;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_weibo);
		mVibrator = (Vibrator) getApplication().getSystemService(VIBRATOR_SERVICE);
		soundPlayers = new MediaPlayer[sounds.length];
		for (int i = 0; i < soundPlayers.length; i++) {
			soundPlayers[i] = MediaPlayer.create(this, sounds[i]);
			soundPlayers[i].setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
		userData = MainWeibo.userData;
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		network_provider = LocationManager.NETWORK_PROVIDER;// use network to location
		gps_provider = LocationManager.GPS_PROVIDER;
		tv_shakeweiboitem_distance = (TextView) findViewById(R.id.tv_shakeweiboitem_distance);
		tv_shake_title = (TextView) findViewById(R.id.tv_shake_title);
		shaketype = getIntent().getIntExtra(ConstantUtil.SHAKE_TYPE, ConstantUtil.SHAKE_TYPE_NEARBYWEIBO);
		weibotype = getIntent().getIntExtra(ConstantUtil.WEIBO_TYPE, ConstantUtil.WEIBO_TYPE_PICTURE);
		switch (shaketype) {
		case ConstantUtil.SHAKE_TYPE_ADDFRIEND:
			tv_shake_title.setText("摇附近朋友");
			break;
		case ConstantUtil.SHAKE_TYPE_NEARBYWEIBO:
			tv_shake_title.setText("摇周边动态");
			break;
		case ConstantUtil.SHAKE_TYPE_MODEWEIBO:
			tv_shake_title.setText("摇心情微博");
			break;
		case ConstantUtil.SHAKE_TYPE_FEATUREWEIBO:
			tv_shake_title.setText("摇好友微博");
			tv_shakeweiboitem_distance.setVisibility(View.GONE);
			break;
		default:
			tv_shake_title.setText("摇周边动态");
			break;
		}

		rl_shake_result = (RelativeLayout) findViewById(R.id.rl_shake_result);
		rl_shake_result.setVisibility(View.GONE);
		rl_shake_loading = (RelativeLayout) findViewById(R.id.rl_shake_loading);
		rl_shake_loading.setVisibility(View.GONE);
		tv_shakeweiboitem_name = (TextView) findViewById(R.id.tv_shakeweiboitem_name);
		tv_shakeweiboitem_content = (TextView) findViewById(R.id.tv_shakeweiboitem_content);
		iv_shakeweiboitem_gender = (ImageView) findViewById(R.id.iv_shakeweiboitem_gender);
		iv_shakeweiboitem_head = (ImageView) findViewById(R.id.iv_shakeweiboitem_head);
		rl_shake_handup = (RelativeLayout) findViewById(R.id.rl_shake_handup);
		rl_shake_handdown = (RelativeLayout) findViewById(R.id.rl_shake_handdown);
		tv_shake_title = (TextView) findViewById(R.id.tv_shake_title);

		shakeHandler = new Handler() {
			public void handleMessage(Message message) {
				if (message.what == ConstantUtil.MESSAGE_TYPE_WEIBOIMAGE) {
					WeiboImage weiboImage = (WeiboImage) message.obj;
					weiboImage.imageView.setImageBitmap(weiboImage.bitmap);
				}
			}
		};

		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {// sensor senses shake event
				startVibrate();
				if (userData.isSoundPlay()) {
					soundPlayers[0].start();
				}
				if (rl_shake_result.getVisibility() != View.GONE) {
					TranslateAnimation downAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
							Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
							+5f);// move it off
					downAnimation.setDuration(500);
					downAnimation.setFillAfter(true);
					rl_shake_result.startAnimation(downAnimation);
					rl_shake_result.setVisibility(View.GONE);// direct gone no use
				}
				startAnimation();
				mShakeListener.stop();
				rl_shake_loading.setVisibility(View.VISIBLE);
				new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
					protected Void doInBackground(Void... params) {
						loadShakeResult();
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						System.out.println("shake result = " + shake_result);
						rl_shake_loading.setVisibility(View.GONE);
						if (shake_result == NO_NETWORK) {
							if (userData.isSoundPlay()) {
								soundPlayers[2].start();
							}
							ToastUtil.showShortToast(getApplicationContext(), "您现在米有网络哟！");
						} else if (shake_result == NO_LOCATION) {
							if (userData.isSoundPlay()) {
								soundPlayers[2].start();
							}
							ToastUtil.showShortToast(getApplicationContext(), "无法获取您的位置！");
						} else if (shake_result == NO_MATCH) {
							if (userData.isSoundPlay()) {
								soundPlayers[2].start();
							}
							ToastUtil.showShortToast(getApplicationContext(), "米有摇到任何信息！");
						} else {
							if (status != null) {
								if (userData.isSoundPlay()) {
									soundPlayers[1].start();
								}
								if (shaketype == ConstantUtil.SHAKE_TYPE_NEARBYWEIBO) {
									postShakeNearbyWeibo();
								} else if (shaketype == ConstantUtil.SHAKE_TYPE_ADDFRIEND) {
									postShakeAddFriend();
								} else if (shaketype == ConstantUtil.SHAKE_TYPE_MODEWEIBO) {
									postShakeModeWeibo();
								} else if (shaketype == ConstantUtil.SHAKE_TYPE_FEATUREWEIBO) {
									postShakeFeatureWeibo();
								}
							}
						}
						mVibrator.cancel();
						mShakeListener.start();
					}
				}.execute();

			}
		});
	}

	/**
	 * 寻找摇微博的结果
	 */
	private void loadShakeResult() {
		status = null;
		user = null;
		shake_result = NO_MATCH;
		if (NetworkUtil.getNetworkState(getApplicationContext()) == NetworkUtil.NONE) {
			shake_result = NO_NETWORK;
			mVibrator.cancel();
			mShakeListener.start();
			return;
		}
		try {
			if (shaketype == ConstantUtil.SHAKE_TYPE_NEARBYWEIBO) {
				shakeNearbyWeibo();
			} else if (shaketype == ConstantUtil.SHAKE_TYPE_ADDFRIEND) {
				shakeAddFriend();
			} else if (shaketype == ConstantUtil.SHAKE_TYPE_MODEWEIBO) {
				shakeModeWeibo();
			} else if (shaketype == ConstantUtil.SHAKE_TYPE_FEATUREWEIBO) {
				shakeFeatureWeibo();
			}
		} catch (Exception e) {
			if (userData.isSoundPlay()) {
				soundPlayers[2].start();
			}
			shake_result = NO_MATCH;
			mVibrator.cancel();
			mShakeListener.start();
		}
	}

	/**
	 * 查找周边动态的微博
	 * 
	 * @throws WeiboException
	 *             微博异常
	 */
	private void shakeNearbyWeibo() throws WeiboException {
		Location location = locationManager.getLastKnownLocation(network_provider);// network
		if (location == null) {
			location = locationManager.getLastKnownLocation(gps_provider);// gps
		}
		shake_result = MATCH;
		Place place = new Place();
		place.client.setToken(userData.getToken());
		StatusWapper statusWapper;
		if (location != null) {
			statusWapper = place.nearbyTimeline(location.getLatitude(), location.getLongitude());//
		} else {
			statusWapper = place.nearbyTimeline(28.15675335, 112.934191);// my location
		}
		Random random = new Random();
		int randomint = random.nextInt(statusWapper.getStatuses().size());
		status = statusWapper.getStatuses().get(randomint);
		if (status == null) {
			shake_result = NO_MATCH;
		}
	}

	/**
	 * 得到周边动态的结果之后
	 */
	private void postShakeNearbyWeibo() {
		tv_shakeweiboitem_name.setText(status.getUser().getScreenName());
		tv_shakeweiboitem_distance.setText(status.getDistance() + " M");
		String content = status.getText();
		if (content.length() > MAX_CONTENT_LENGTH) {
			content = content.substring(0, MAX_CONTENT_LENGTH) + "...";
		}
		tv_shakeweiboitem_content.setText(content);
		User user = status.getUser();
		if (user.getGender().equalsIgnoreCase("f")) {// female
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_female);
		} else if (user.getGender().equalsIgnoreCase("m")) {// male
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_male);
		} else {// not know
			iv_shakeweiboitem_gender.setVisibility(View.GONE);
		}
		WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, user.getProfileImageUrl(), shakeHandler,
				iv_shakeweiboitem_head, ConstantUtil.IMAGE_TYPE_PROFILE);
		TranslateAnimation downAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.2f, Animation.RELATIVE_TO_SELF, 0f);
		downAnimation.setDuration(1000);
		downAnimation.setFillAfter(true);
		rl_shake_result.setVisibility(View.VISIBLE);
		rl_shake_result.startAnimation(downAnimation);

		rl_shake_result.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ShakeWeibo.this, ViewWeiboItem.class);
				intent.putExtra("status", new WeiboItem(status));
				startActivity(intent);
			}
		});
	}

	/**
	 * 查找附近的好友
	 * 
	 * @throws WeiboException
	 *             微博异常
	 */
	private void shakeAddFriend() throws WeiboException {
		Location location = locationManager.getLastKnownLocation(network_provider);// network
		if (location == null) {
			location = locationManager.getLastKnownLocation(gps_provider);// gps
		}
		shake_result = MATCH;
		Place place = new Place();
		place.client.setToken(userData.getToken());
		UserWapper userWapper;
		if (location != null) {
			userWapper = place.nearbyUsers(location.getLatitude(), location.getLongitude());//
		} else {
			userWapper = place.nearbyUsers(28.15675335, 112.934191);// my location
		}
		Random random = new Random();
		int randomint = random.nextInt(userWapper.getUsers().size());
		user = userWapper.getUsers().get(randomint);
		status = user.getStatus();
		if (user == null || status == null) {
			shake_result = NO_MATCH;
		}
	}

	/**
	 * 查找完附近好友之后
	 */
	private void postShakeAddFriend() {
		tv_shakeweiboitem_name.setText(user.getScreenName());
		tv_shakeweiboitem_distance.setText(user.getDistance() + " M");
		String content = status.getText();
		if (content.length() > MAX_CONTENT_LENGTH) {
			content = content.substring(0, MAX_CONTENT_LENGTH) + "...";
		}
		tv_shakeweiboitem_content.setText(content);
		if (user.getGender().equalsIgnoreCase("f")) {// female
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_female);
		} else if (user.getGender().equalsIgnoreCase("m")) {// male
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_male);
		} else {// not know
			iv_shakeweiboitem_gender.setVisibility(View.GONE);
		}
		WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, user.getProfileImageUrl(), shakeHandler,
				iv_shakeweiboitem_head, ConstantUtil.IMAGE_TYPE_PROFILE);
		TranslateAnimation downAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.2f, Animation.RELATIVE_TO_SELF, 0f);
		downAnimation.setDuration(1000);
		downAnimation.setFillAfter(true);
		rl_shake_result.setVisibility(View.VISIBLE);
		rl_shake_result.startAnimation(downAnimation);
		final WeiboItem weiboItem = new WeiboItem(status.getId(), user.getId(), user.getScreenName(), user.getGender(),
				user.getProfileImageUrl(), user.getLocation(), status.getText(), status.getThumbnailPic(), "", "", "",
				status.getSource().getName(), WeiboUtil.formatWeiboDate(status.getCreatedAt()));
		rl_shake_result.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ShakeWeibo.this, ViewWeiboItem.class);
				intent.putExtra("status", weiboItem);
				startActivity(intent);
			}
		});
	}

	/**
	 * 摇心情微博【此功能已经删除】
	 * 
	 * @throws WeiboException
	 *             微博异常
	 */
	private void shakeModeWeibo() throws WeiboException {
		shake_result = MATCH;
		status = null;
		user = null;
		Location location = locationManager.getLastKnownLocation(network_provider);// network
		if (location == null) {
			location = locationManager.getLastKnownLocation(gps_provider);// gps
		}
		Place place = new Place();
		place.client.setToken(userData.getToken());
		StatusWapper statusWapper;
		if (location != null) {
			statusWapper = place.nearbyTimeline(location.getLatitude(), location.getLongitude());//
		} else {
			statusWapper = place.nearbyTimeline(28.15675335, 112.934191);// my location
		}
		Random random = new Random();
		int randomint = random.nextInt(statusWapper.getStatuses().size());
		status = statusWapper.getStatuses().get(randomint);
		if (status == null) {
			shake_result = NO_MATCH;
		}
	}

	/**
	 * 摇出心情微博之后
	 */
	private void postShakeModeWeibo() {
		tv_shakeweiboitem_name.setText(status.getUser().getScreenName());
		tv_shakeweiboitem_distance.setText(status.getDistance() + " M");
		String content = status.getText();
		if (content.length() > MAX_CONTENT_LENGTH) {
			content = content.substring(0, MAX_CONTENT_LENGTH) + "...";
		}
		tv_shakeweiboitem_content.setText(content);
		User user = status.getUser();
		if (user.getGender().equalsIgnoreCase("f")) {// female
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_female);
		} else if (user.getGender().equalsIgnoreCase("m")) {// male
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_male);
		} else {// not know
			iv_shakeweiboitem_gender.setVisibility(View.GONE);
		}
		WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, user.getProfileImageUrl(), shakeHandler,
				iv_shakeweiboitem_head, ConstantUtil.IMAGE_TYPE_PROFILE);
		TranslateAnimation downAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.2f, Animation.RELATIVE_TO_SELF, 0f);
		downAnimation.setDuration(1000);
		downAnimation.setFillAfter(true);
		rl_shake_result.setVisibility(View.VISIBLE);
		rl_shake_result.startAnimation(downAnimation);

		rl_shake_result.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ShakeWeibo.this, ViewWeiboItem.class);
				intent.putExtra("status", new WeiboItem(status));
				startActivity(intent);
			}
		});
	}

	/**
	 * 摇好友微博
	 * 
	 * @throws WeiboException
	 *             微博异常
	 */
	private void shakeFeatureWeibo() throws WeiboException {
		shake_result = MATCH;
		status = null;
		user = null;
		int baseAPP = 0;// default
		int feature = 0;
		if (weibotype == ConstantUtil.WEIBO_TYPE_PICTURE) {
			feature = 2;
		} else if (weibotype == ConstantUtil.WEIBO_TYPE_MUSIC) {
			feature = 4;
		} else if (weibotype == ConstantUtil.WEIBO_TYPE_VIDEO) {
			feature = 3;
		}
		Paging paging = new Paging();
		Timeline timeline = new Timeline();
		timeline.client.setToken(userData.getToken());
		StatusWapper statusWapper = timeline.getFriendsTimeline(baseAPP, feature, paging);
		Random random = new Random();
		int randomint = random.nextInt(statusWapper.getStatuses().size());
		status = statusWapper.getStatuses().get(randomint);
		if (status == null) {
			shake_result = NO_MATCH;
		}
	}

	/**
	 * 摇出好友微博之后
	 */
	private void postShakeFeatureWeibo() {
		tv_shakeweiboitem_name.setText(status.getUser().getScreenName());
		tv_shakeweiboitem_distance.setText(status.getDistance() + " M");
		String content = status.getText();
		if (content.length() > MAX_CONTENT_LENGTH) {
			content = content.substring(0, MAX_CONTENT_LENGTH) + "...";
		}
		tv_shakeweiboitem_content.setText(content);
		User user = status.getUser();
		if (user.getGender().equalsIgnoreCase("f")) {// female
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_female);
		} else if (user.getGender().equalsIgnoreCase("m")) {// male
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_male);
		} else {// not know
			iv_shakeweiboitem_gender.setVisibility(View.GONE);
		}
		WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, user.getProfileImageUrl(), shakeHandler,
				iv_shakeweiboitem_head, ConstantUtil.IMAGE_TYPE_PROFILE);
		TranslateAnimation downAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1.2f, Animation.RELATIVE_TO_SELF, 0f);
		downAnimation.setDuration(1000);
		downAnimation.setFillAfter(true);
		rl_shake_result.setVisibility(View.VISIBLE);
		rl_shake_result.startAnimation(downAnimation);

		rl_shake_result.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(ShakeWeibo.this, ViewWeiboItem.class);
				intent.putExtra("status", new WeiboItem(status));
				startActivity(intent);
			}
		});
	}

	/**
	 * 开始显示动画
	 */
	public void startAnimation() {
		AnimationSet animup = new AnimationSet(true);
		TranslateAnimation mytranslateanimup0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
		mytranslateanimup0.setDuration(1000);
		TranslateAnimation mytranslateanimup1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
		mytranslateanimup1.setDuration(1000);
		mytranslateanimup1.setStartOffset(1000);
		animup.addAnimation(mytranslateanimup0);
		animup.addAnimation(mytranslateanimup1);
		rl_shake_handup.startAnimation(animup);

		AnimationSet animdn = new AnimationSet(true);
		TranslateAnimation mytranslateanimdn0 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, +0.5f);
		mytranslateanimdn0.setDuration(1000);
		TranslateAnimation mytranslateanimdn1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -0.5f);
		mytranslateanimdn1.setDuration(1000);
		mytranslateanimdn1.setStartOffset(1000);
		animdn.addAnimation(mytranslateanimdn0);
		animdn.addAnimation(mytranslateanimdn1);
		rl_shake_handdown.startAnimation(animdn);
	}

	/**
	 * 开始振动
	 */
	public void startVibrate() {
		mVibrator.vibrate(new long[] { 500, 1000, 500, 1000 }, -1);
	}

	/**
	 * 返回主界面
	 * 
	 * @param v
	 *            View组件
	 */
	public void backToMainWeibo(View v) {
		finish();
	}

	/*
	 * (non-Javadoc)界面销毁时回调方法
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (MediaPlayer p : soundPlayers) {
			if (p.isPlaying()) {
				p.stop();
			}
			p.release();
		}
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}
}
