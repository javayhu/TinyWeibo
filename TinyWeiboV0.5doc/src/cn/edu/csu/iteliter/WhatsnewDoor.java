/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.util.UserDataUtil;

/**
 * @filename WhatsnewDoor.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 进入主界面之前的界面，展示一个开门的动画效果
 * @author 孟丹
 * @team China ITElite Team
 * @email 495179346@qq.com
 * @updatetime 2012-12-21 下午3:08:03
 * @version 1.0
 * 
 */
public class WhatsnewDoor extends Activity {

	/** 主界面中央图片的左侧 */
	private ImageView mLeft;

	/** 主界面中央图片的右侧 */
	private ImageView mRight;

	/** 主界面中央上方显示的文字 */
	private TextView mText;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.whats_door);

		mLeft = (ImageView) findViewById(R.id.imageLeft);
		mRight = (ImageView) findViewById(R.id.imageRight);
		mText = (TextView) findViewById(R.id.anim_text);

		AnimationSet anim = new AnimationSet(true);
		TranslateAnimation mytranslateanim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		mytranslateanim.setDuration(2000);
		anim.setStartOffset(800);
		anim.addAnimation(mytranslateanim);
		anim.setFillAfter(true);
		mLeft.startAnimation(anim);

		AnimationSet anim1 = new AnimationSet(true);
		TranslateAnimation mytranslateanim1 = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, +1f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		mytranslateanim1.setDuration(1500);
		anim1.addAnimation(mytranslateanim1);
		anim1.setStartOffset(800);
		anim1.setFillAfter(true);
		mRight.startAnimation(anim1);

		AnimationSet anim2 = new AnimationSet(true);
		ScaleAnimation myscaleanim = new ScaleAnimation(1f, 3f, 1f, 3f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		myscaleanim.setDuration(1000);
		AlphaAnimation myalphaanim = new AlphaAnimation(1, 0.0001f);
		myalphaanim.setDuration(1500);
		anim2.addAnimation(myscaleanim);
		anim2.addAnimation(myalphaanim);
		anim2.setFillAfter(true);
		mText.startAnimation(anim2);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				UserData userData = UserDataUtil.readUserData(getApplicationContext());
				userData.setFirstrun(false);
				UserDataUtil.updateUserData(getApplicationContext(), userData);// update next time no go here
				Intent intent = new Intent(WhatsnewDoor.this, MainWeibo.class);
				startActivity(intent);
				WhatsnewDoor.this.finish();
			}
		}, 2300);
	}

}
