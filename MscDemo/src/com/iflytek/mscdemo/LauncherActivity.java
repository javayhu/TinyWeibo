package com.iflytek.mscdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 启动Activity，可以通过在onCreate中设置跳转代码 来确定程序的第一个页面.
 * 
 * @author iFlytek
 * @since 20120823
 */
public class LauncherActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 跳转到MainActivity.
		startActivity(new Intent(this, MainActivity.class));

		finish();
	}
}
