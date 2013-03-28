package com.iflytek.mscdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.TextKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.mscdemo.R;
import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;
import com.iflytek.ui.SynthesizerDialog;

/**
 * 合成页面,调用SDK的SynthesizerDialog实现语音合成.
 * @author iFlytek
 * @since 20120823
 */
public class TtsDemoActivity extends Activity implements OnClickListener,
		SynthesizerPlayerListener {

	//合成的文本
	private EditText mSourceText;

	//缓存对象.
	private SharedPreferences mSharedPreferences;

	//合成对象.
	private SynthesizerPlayer mSynthesizerPlayer;

	//弹出提示
	private Toast mToast;
	
	//缓冲进度
	private int mPercentForBuffering = 0;
	
	//播放进度
	private int mPercentForPlaying = 0;

	//合成Dialog
	private SynthesizerDialog ttsDialog;
	
	
	/**
	 * 合成界面入口函数
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.demo);

		((TextView) findViewById(android.R.id.title))
				.setGravity(Gravity.CENTER);

		Button ttsButton = (Button) findViewById(android.R.id.button1);
		ttsButton.setOnClickListener(this);
		ttsButton.setText(R.string.text_tts);
		Button settingButton = (Button) findViewById(android.R.id.button2);
		settingButton.setOnClickListener(this);
		settingButton.setText(R.string.text_setting);
		mSourceText = (EditText) findViewById(R.id.txt_result);
		mSourceText.setText(R.string.text_tts_source);
		mSourceText.setKeyListener(TextKeyListener.getInstance());
		
		//设置EditText的输入方式.
		mSourceText.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE);

		mSharedPreferences = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);

		mToast = Toast.makeText(this,
				String.format(getString(R.string.tts_toast_format), 0, 0),
				Toast.LENGTH_LONG);
		
		//初始化合成Dialog.
		ttsDialog = new SynthesizerDialog(this, "appid=" + getString(R.string.app_id));
	}

	/**
	 * SynthesizerPlayerListener的"停止播放"回调接口.
	 * @param
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
	 * 按钮点击事件.
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case android.R.id.button1:
			boolean show = mSharedPreferences.getBoolean(
					getString(R.string.preference_key_tts_show), true);
			if (show) {
				//显示合成Dialog.
				showSynDialog();
			} else {
				//不显示Dialog，后台合成语音.
				synthetizeInSilence();
			}
			break;
		//跳转到合成设置界面.
		case android.R.id.button2:
			startActivity(new Intent(this, TtsPreferenceActivity.class));
			break;
		default:
			break;
		}
	}

	/**
	 * 使用SynthesizerPlayer合成语音，不弹出合成Dialog.
	 * @param
	 */
	private void synthetizeInSilence() {
		if (null == mSynthesizerPlayer) {
			//创建合成对象.
			mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(
					this, "appid=" + getString(R.string.app_id));
		}

		//设置合成发音人.
		String role = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_role),
				getString(R.string.preference_default_tts_role));
		mSynthesizerPlayer.setVoiceName(role);

		//设置发音人语速
		int speed = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_speed),
				50);
		mSynthesizerPlayer.setSpeed(speed);

		//设置音量.
		int volume = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_volume),
				50);
		mSynthesizerPlayer.setVolume(volume);

		//设置背景音.
		String music = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_music),
				getString(R.string.preference_default_tts_music));
		mSynthesizerPlayer.setBackgroundSound(music);

		//获取合成文本.
		Editable editable = mSourceText.getText();
		String source = null;
		if (null != editable) {
			source = editable.toString();
		}

		//进行语音合成.
		mSynthesizerPlayer.playText(source, null,this);
		mToast.setText(String
				.format(getString(R.string.tts_toast_format), 0, 0));
		mToast.show();
	}
	
	/**
	 * 弹出合成Dialog，进行语音合成
	 * @param
	 */
	public void showSynDialog()
	{

		Editable editable = mSourceText.getText();
		String source = null;
		if (null != editable) {
			source = editable.toString();
		}
		//设置合成文本.
		ttsDialog.setText(source, null);

		//设置发音人.
		String role = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_role),
				getString(R.string.preference_default_tts_role));
		ttsDialog.setVoiceName(role);

		//设置语速.
		int speed = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_speed),
				50);
		ttsDialog.setSpeed(speed);

		//设置音量.
		int volume = mSharedPreferences.getInt(
				getString(R.string.preference_key_tts_volume),
				50);
		ttsDialog.setVolume(volume);

		//设置背景音.
		String music = mSharedPreferences.getString(
				getString(R.string.preference_key_tts_music),
				getString(R.string.preference_default_tts_music));
		ttsDialog.setBackgroundSound(music);

		//弹出合成Dialog
		ttsDialog.show();
	}

	/**
	 * SynthesizerPlayerListener的"播放进度"回调接口.
	 * @param percent,beginPos,endPos
	 */
	@Override
	public void onBufferPercent(int percent,int beginPos,int endPos) {
		mPercentForBuffering = percent;
		mToast.setText(String.format(getString(R.string.tts_toast_format),
				mPercentForBuffering, mPercentForPlaying));
		mToast.show();
	}

	/**
	 * SynthesizerPlayerListener的"开始播放"回调接口.
	 * @param 
	 */
	@Override
	public void onPlayBegin() {
	}

	/**
	 * SynthesizerPlayerListener的"暂停播放"回调接口.
	 * @param 
	 */
	@Override
	public void onPlayPaused() {
	}

	/**
	 * SynthesizerPlayerListener的"播放进度"回调接口.
	 * @param percent,beginPos,endPos
	 */
	@Override
	public void onPlayPercent(int percent,int beginPos,int endPos) {
		mPercentForPlaying = percent;
		mToast.setText(String.format(getString(R.string.tts_toast_format),
				mPercentForBuffering, mPercentForPlaying));
		mToast.show();
	}

	/**
	 * SynthesizerPlayerListener的"恢复播放"回调接口，对应onPlayPaused
	 * @param 
	 */
	@Override
	public void onPlayResumed() {
	}

	/**
	 * SynthesizerPlayerListener的"结束会话"回调接口.
	 * @param error
	 */
	@Override
	public void onEnd(SpeechError error) {
	}
}
