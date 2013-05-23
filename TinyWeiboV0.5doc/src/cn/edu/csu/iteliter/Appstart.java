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
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.util.NetworkUtil;
import cn.edu.csu.iteliter.util.ToastUtil;
import cn.edu.csu.iteliter.util.UserDataUtil;

/**
 * @filename Appstart.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 应用启动界面
 * @author 孟丹
 * @team China ITElite Team
 * @email 495179346@qq.com
 * @updatetime 2012-12-21 下午12:33:23
 * @version 1.0
 * 
 */
public class Appstart extends Activity {

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				UserData userdata = UserDataUtil.readUserData(getApplicationContext());// 得到用户数据
				if (NetworkUtil.getNetworkState(Appstart.this) != NetworkUtil.NONE) {// 有网络
					String localToken = userdata.getToken();
					System.out.println("token = " + localToken);
					String localExpiresIn = userdata.getExpirestime();
					Intent intent = null;
					if (UserDataUtil.isTokenValid(localToken, localExpiresIn)) {// token还有效
						if (userdata.isFirstrun()) {// 第一次运行，进入欢迎界面
							intent = new Intent(Appstart.this, Whatsnew.class);
						} else {// 不是第一次运行，进入主界面
							intent = new Intent(Appstart.this, MainWeibo.class);
						}
					} else {// 无效，重新授权登陆
						intent = new Intent(Appstart.this, Welcome.class);
					}
					startActivity(intent);
					Appstart.this.finish();
				} else {// 没有网络
					ToastUtil.showShortToast(Appstart.this, "网络不可用哟");
				}
			}
		}, 2000);
	}
}