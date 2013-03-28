package com.iflytek.mscdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * 应用程序主界面,可以向语音转写,识别,合成跳转.
 * 
 * @author iFlytek
 * @since 20120821
 */
public class MainActivity extends Activity implements OnClickListener {

	/**
	 * 界面初始化入口函数.
	 * 
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		setTitle(R.string.title);
		((TextView) findViewById(android.R.id.title)).setGravity(Gravity.CENTER);
		((TextView) findViewById(R.id.text_isr_introduction)).setMovementMethod(ScrollingMovementMethod.getInstance());
		findViewById(R.id.btn_iat_demo).setOnClickListener(this);
		findViewById(R.id.btn_isr_demo).setOnClickListener(this);
		findViewById(R.id.btn_tts_demo).setOnClickListener(this);
	}

	/**
	 * 按钮点击事件.
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		// 点击转写按钮，跳转到语音转写页面.
		case R.id.btn_iat_demo:
			intent = new Intent(this, IatDemoActivity.class);
			break;
		// 点击识别按钮，跳转到语音识别页面.
		case R.id.btn_isr_demo:
			intent = new Intent(this, IsrDemoActivity.class);
			break;
		// 点击合成按钮，跳转到语音合成页面.
		case R.id.btn_tts_demo:
			intent = new Intent(this, TtsDemoActivity.class);
			break;
		}
		startActivity(intent);
	}
}
