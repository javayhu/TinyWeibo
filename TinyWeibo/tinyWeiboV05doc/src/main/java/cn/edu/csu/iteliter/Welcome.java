/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
package cn.edu.csu.iteliter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.ToastUtil;
import cn.edu.csu.iteliter.util.UserDataUtil;

import com.weibo.sdk.android.AndroidWeibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

/**
 * @filename Welcome.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 用户登录注册界面
 * @author 孟丹
 * @team China ITElite Team
 * @email 495179346@qq.com
 * @updatetime 2012-12-21 下午3:09:37
 * @version 1.0
 * 
 */
public class Welcome extends Activity {

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	}

	/**
	 * 登录方法
	 * 
	 * @param v
	 *            View组件
	 */
	public void welcome_login(View v) {
		AndroidWeibo.getInstance().authorize(Welcome.this, new AuthDialogListener());
	}

	/**
	 * 进入主界面
	 */
	private void enterMainWeibo() {
		Intent intent = new Intent(Welcome.this, Whatsnew.class);
		startActivity(intent);
		Welcome.this.finish();
	}

	/**
	 * 用户注册
	 * 
	 * @param v
	 *            View组件
	 */
	public void welcome_register(View v) {
		Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantUtil.REGISTER_URL));
		startActivity(webIntent);
	}

	/**
	 * The listener interface for receiving authDialog events. The class that is interested in processing a authDialog
	 * event implements this interface, and the object created with that class is registered with a component using the
	 * component's <code>addAuthDialogListener<code> method. When
	 * the authDialog event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see AuthDialogEvent
	 */
	class AuthDialogListener implements WeiboAuthListener {

		/*
		 * (non-Javadoc)授权完成的回调方法
		 * 
		 * @see com.weibo.sdk.android.WeiboAuthListener#onComplete(android.os.Bundle)
		 */
		@Override
		public void onComplete(Bundle values) {
			ToastUtil.showShortToast(getApplicationContext(), "授权成功");
			String token = values.getString("access_token");
			String expiresIn = values.getString("expires_in");
			String uid = values.getString("uid");
			long expiresTime = System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000;
			UserDataUtil.updateUserData(Welcome.this, new UserData(token, String.valueOf(expiresTime), uid));
			enterMainWeibo();
		}

		/*
		 * (non-Javadoc)授权出错的回调方法
		 * 
		 * @see com.weibo.sdk.android.WeiboAuthListener#onError(com.weibo.sdk.android.WeiboDialogError)
		 */
		@Override
		public void onError(WeiboDialogError e) {
			ToastUtil.showShortToast(getApplicationContext(), "授权失败");
		}

		/*
		 * (non-Javadoc)授权取消的回调方法
		 * 
		 * @see com.weibo.sdk.android.WeiboAuthListener#onCancel()
		 */
		@Override
		public void onCancel() {
			ToastUtil.showShortToast(getApplicationContext(), "取消授权");
		}

		/*
		 * (non-Javadoc)授权出现异常的回调方法
		 * 
		 * @see com.weibo.sdk.android.WeiboAuthListener#onWeiboException(com.weibo.sdk.android.WeiboException)
		 */
		@Override
		public void onWeiboException(WeiboException e) {
			ToastUtil.showShortToast(getApplicationContext(), "授权异常");
		}
	}

}
