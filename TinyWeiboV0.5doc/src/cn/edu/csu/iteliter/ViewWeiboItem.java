/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.edu.csu.iteliter.model.WeiboItem;
import cn.edu.csu.iteliter.util.CacheUtil;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.WeiboUtil;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.iflytek.ui.SynthesizerDialog;

/**
 * @filename ResultWeiboItem.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 查看一条微博
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午2:33:13
 * @version 1.0
 * 
 */
public class ViewWeiboItem extends Activity implements SynthesizerPlayerListener {

	/** 缓存对象 */
	private SharedPreferences mSharedPreferences;

	/** 合成对象 */
	private SynthesizerPlayer mSynthesizerPlayer;

	/** 弹出提示 */
	private Toast mToast;

	/** 缓冲进度 */
	private int mPercentForBuffering = 0;

	/** 播放进度 */
	private int mPercentForPlaying = 0;

	/** 合成对话框 */
	private SynthesizerDialog ttsDialog;

	/** 用户的头像 */
	private ImageView iv_shakeweiboitem_profile;

	/** 用户的性别 */
	private ImageView iv_shakeweiboitem_gender;

	/** 微博的图片 */
	private ImageView iv_shakeweiboitem_statusImage;

	/** 原微博的图片 */
	private ImageView iv_shakeweiboitem_sourceImage;

	/** 原微博用户的昵称 */
	private TextView tv_shakeweiboitem_screenname;

	/** 用户的所在地 */
	private TextView tv_shakeweiboitem_location;

	/** 微博发布的时间 */
	private TextView tv_shakeweiboitem_time;

	/** 微博的内容 */
	private TextView tv_shakeweiboitem_content;

	/** 微博的来源 */
	private TextView tv_shakeweiboitem_from;

	/** 原微博的内容 */
	private TextView tv_shakeweiboitem_sourceContent;

	/** 原微博的用户的昵称 */
	private TextView tv_shakeweiboitem_sourceName;

	/** 原微博组件 */
	private LinearLayout ll_shakeweiboitem_source;

	/** 评论部分 */
	private LinearLayout ll_shakeweiboitem_comment;

	/** 转发部分 */
	private LinearLayout ll_shakeweiboitem_forward;

	/** 语音部分 */
	private LinearLayout ll_shakeweiboitem_read;

	/** 微博对象 */
	private WeiboItem weiboItem;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake_weibo_result);
		weiboItem = getIntent().getParcelableExtra("status");
		iv_shakeweiboitem_gender = (ImageView) findViewById(R.id.iv_shakeweiboitem_gender);
		iv_shakeweiboitem_profile = (ImageView) findViewById(R.id.iv_shakeweiboitem_profile);
		iv_shakeweiboitem_statusImage = (ImageView) findViewById(R.id.iv_shakeweiboitem_statusImage);
		iv_shakeweiboitem_sourceImage = (ImageView) findViewById(R.id.iv_shakeweiboitem_sourceImage);

		tv_shakeweiboitem_screenname = (TextView) findViewById(R.id.tv_shakeweiboitem_screenname);
		tv_shakeweiboitem_location = (TextView) findViewById(R.id.tv_shakeweiboitem_location);
		tv_shakeweiboitem_time = (TextView) findViewById(R.id.tv_shakeweiboitem_time);
		tv_shakeweiboitem_content = (TextView) findViewById(R.id.tv_shakeweiboitem_content);
		tv_shakeweiboitem_from = (TextView) findViewById(R.id.tv_shakeweiboitem_from);
		tv_shakeweiboitem_sourceName = (TextView) findViewById(R.id.tv_shakeweiboitem_sourceName);
		tv_shakeweiboitem_sourceContent = (TextView) findViewById(R.id.tv_shakeweiboitem_sourceContent);

		ll_shakeweiboitem_source = (LinearLayout) findViewById(R.id.ll_shakeweiboitem_source);
		ll_shakeweiboitem_comment = (LinearLayout) findViewById(R.id.ll_shakeweiboitem_comment);
		ll_shakeweiboitem_forward = (LinearLayout) findViewById(R.id.ll_shakeweiboitem_forward);
		ll_shakeweiboitem_read = (LinearLayout) findViewById(R.id.ll_shakeweiboitem_read);

		tv_shakeweiboitem_screenname.setText(weiboItem.getUsername());
		tv_shakeweiboitem_from.setText(weiboItem.getFrom());
		tv_shakeweiboitem_content.setText(weiboItem.getContent());
		tv_shakeweiboitem_location.setText(weiboItem.getLocation());
		tv_shakeweiboitem_time.setText(weiboItem.getTime());
		tv_shakeweiboitem_sourceContent.setText(weiboItem.getSourceContent());
		tv_shakeweiboitem_sourceName.setText("@" + weiboItem.getSourceName());// @ name

		if (weiboItem.getGender().equalsIgnoreCase("f")) {// female
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_female);
		} else if (weiboItem.getGender().equalsIgnoreCase("m")) {
			iv_shakeweiboitem_gender.setVisibility(View.VISIBLE);
			iv_shakeweiboitem_gender.setImageResource(R.drawable.user_info_male);
		} else {// not know
			iv_shakeweiboitem_gender.setVisibility(View.GONE);
		}

		if (weiboItem.getProfileImageUrl() != null && !weiboItem.getProfileImageUrl().equalsIgnoreCase("")) {
			WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, weiboItem.getProfileImageUrl(),
					MainWeibo.imageHandler, iv_shakeweiboitem_profile, ConstantUtil.IMAGE_TYPE_PROFILE);
		}

		if (weiboItem.getSourceName() == null || "".equalsIgnoreCase(weiboItem.getSourceName())) {// no source
			ll_shakeweiboitem_source.setVisibility(View.GONE);
			if (weiboItem.getStatusImageUrl() != null && !weiboItem.getStatusImageUrl().equalsIgnoreCase("")) {// status
				iv_shakeweiboitem_statusImage.setVisibility(View.VISIBLE);
				WeiboUtil.restoreBitmap(CacheUtil.IMAGE_CACHE_PATH, weiboItem.getStatusImageUrl(),
						MainWeibo.imageHandler, iv_shakeweiboitem_statusImage, ConstantUtil.IMAGE_TYPE_IMAGE);
			} else {
				iv_shakeweiboitem_statusImage.setVisibility(View.GONE);
			}
		} else {// have source
			iv_shakeweiboitem_statusImage.setVisibility(View.GONE);
			ll_shakeweiboitem_source.setVisibility(View.VISIBLE);
			if (weiboItem.getSourceImageUrl() != null && !weiboItem.getSourceImageUrl().equalsIgnoreCase("")) {// source
				WeiboUtil.restoreBitmap(CacheUtil.IMAGE_CACHE_PATH, weiboItem.getSourceImageUrl(),
						MainWeibo.imageHandler, iv_shakeweiboitem_sourceImage, ConstantUtil.IMAGE_TYPE_IMAGE);
			} else {
				iv_shakeweiboitem_sourceImage.setVisibility(View.GONE);
			}
		}

		ll_shakeweiboitem_comment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ViewWeiboItem.this, HomeWeiboComment.class);
				intent.putExtra(ConstantUtil.STATUS_ID, weiboItem.getStatusid());
				startActivity(intent);
			}
		});

		ll_shakeweiboitem_forward.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ViewWeiboItem.this, HomeWeiboRepost.class);
				intent.putExtra(ConstantUtil.STATUS_ID, weiboItem.getStatusid());
				startActivity(intent);
			}
		});

		ll_shakeweiboitem_read.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showSynDialog();
			}
		});

		mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
		mToast = Toast.makeText(this, String.format(getString(R.string.tts_toast_format), 0, 0), Toast.LENGTH_LONG);
		// 初始化合成Dialog.
		ttsDialog = new SynthesizerDialog(this, "appid=" + getString(R.string.app_id));

	}

	/**
	 * 界面返回
	 * 
	 * @param v
	 *            the v
	 */
	public void btn_back(View v) {
		finish();
	}

	/*
	 * (non-Javadoc)界面停止的回调方法
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		mToast.cancel();
		if (null != mSynthesizerPlayer) {
			mSynthesizerPlayer.cancel();
		}
		super.onStop();
	}

	/**
	 * 显示合成对话框
	 */
	public void showSynDialog() {
		String source = weiboItem.getContent();
		// 设置合成文本.
		ttsDialog.setText(source, null);

		// 设置发音人.
		String role = mSharedPreferences.getString(getString(R.string.preference_key_tts_role),
				getString(R.string.preference_default_tts_role));
		ttsDialog.setVoiceName(role);

		// 设置语速.
		int speed = mSharedPreferences.getInt(getString(R.string.preference_key_tts_speed), 50);
		ttsDialog.setSpeed(speed);

		// 设置音量.
		int volume = mSharedPreferences.getInt(getString(R.string.preference_key_tts_volume), 50);
		ttsDialog.setVolume(volume);

		// 设置背景音.
		String music = mSharedPreferences.getString(getString(R.string.preference_key_tts_music),
				getString(R.string.preference_default_tts_music));
		ttsDialog.setBackgroundSound(music);

		// 弹出合成Dialog
		ttsDialog.show();
	}

	/*
	 * (non-Javadoc)缓存成功
	 * 
	 * @see com.iflytek.speech.SynthesizerPlayerListener#onBufferPercent(int, int, int)
	 */
	@Override
	public void onBufferPercent(int percent, int beginPos, int endPos) {
		mPercentForBuffering = percent;
		mToast.setText(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
		mToast.show();
	}

	/*
	 * (non-Javadoc)开始播放
	 * 
	 * @see com.iflytek.speech.SynthesizerPlayerListener#onPlayBegin()
	 */
	@Override
	public void onPlayBegin() {
	}

	/*
	 * (non-Javadoc)播放暂停
	 * 
	 * @see com.iflytek.speech.SynthesizerPlayerListener#onPlayPaused()
	 */
	@Override
	public void onPlayPaused() {
	}

	/*
	 * (non-Javadoc)播放
	 * 
	 * @see com.iflytek.speech.SynthesizerPlayerListener#onPlayPercent(int, int, int)
	 */
	@Override
	public void onPlayPercent(int percent, int beginPos, int endPos) {
		mPercentForPlaying = percent;
		mToast.setText(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
		mToast.show();
	}

	/*
	 * (non-Javadoc)播放继续
	 * 
	 * @see com.iflytek.speech.SynthesizerPlayerListener#onPlayResumed()
	 */
	@Override
	public void onPlayResumed() {
	}

	/*
	 * (non-Javadoc)播放结束
	 * 
	 * @see com.iflytek.speech.SynthesizerPlayerListener#onEnd(com.iflytek.speech.SpeechError)
	 */
	@Override
	public void onEnd(SpeechError error) {
	}

}