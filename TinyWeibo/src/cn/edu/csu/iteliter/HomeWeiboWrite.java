package cn.edu.csu.iteliter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import weibo4j.Timeline;
import weibo4j.http.ImageItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.ToastUtil;
import cn.edu.csu.iteliter.util.WeiboUtil;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

/**
 * home weibo comment
 * 
 * @author hjw
 * 
 */
public class HomeWeiboWrite extends Activity implements ConstantUtil, RecognizerDialogListener {

	public static byte[] readFileImage(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filename));
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

	private EditText et_share_write;// 文本编辑框
	// 识别Dialog
	private RecognizerDialog iatDialog;

	// private ImageView iv_photo;// 图片和删除照片
	private Button ib_send;// 发送微博
	// private FrameLayout fl_share_photo;
	private String imagePath;// 照片的路径
	private String imageUri;
	private String mContent = "";// 微博内容

	// 缓存，保存当前的引擎参数到下一次启动应用程序使用.
	private SharedPreferences mSharedPreferences;
	private TextView tv_word_counter;// 统计字数
	private TextView tv_writeweibo_title;//

	private int type;

	private UserData userData;

	// back
	public void backToMainWeiboHome(View v) {
		finish();
	}

	public void choosePicture(View v) {
		Intent intent = new Intent(HomeWeiboWrite.this, WeiboWriteImage.class);
		if ((imagePath != null) && !imagePath.equalsIgnoreCase("")) {
			intent.putExtra(ConstantUtil.IMAGEPATH, imagePath);
			intent.putExtra(ConstantUtil.IMAGEURI, imageUri);
		}
		startActivityForResult(intent, ConstantUtil.REQUEST_WEIBOWRITE_IMAGE);
	}

	// delete weibo
	public void deleteWeibo(View v) {
		Dialog dialog = new AlertDialog.Builder(this).setTitle(R.string.weibosdk_attention)
				.setMessage(R.string.weibosdk_delete_all)
				.setPositiveButton(R.string.weibosdk_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						et_share_write.setText("");
					}
				}).setNegativeButton(R.string.weibosdk_cancel, null).create();
		dialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantUtil.REQUEST_WEIBOWRITE_IMAGE) {
				imagePath = intent.getStringExtra(ConstantUtil.IMAGEPATH);
				System.out.println("result path = " + imagePath);
				imageUri = intent.getStringExtra(ConstantUtil.IMAGEURI);
				System.out.println("result uri = " + imageUri);
				// result path = file:///data/data/com.miui.camera/files/image-temp.jpg --> wrong
				// result path = /mnt/sdcard/DCIM/Camera/IMG_20121206_192954.jpg --> right
			}
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View mainview = getLayoutInflater().inflate(R.layout.home_weibo_write, null);
		setContentView(mainview);
		type = getIntent().getIntExtra(ConstantUtil.WRITE_WEIBO_TYPE, ConstantUtil.WRITE_WEIBO_TYPE_WRITEWEIBO);
		userData = MainWeibo.userData;
		tv_writeweibo_title = (TextView) mainview.findViewById(R.id.tv_writeweibo_title);
		if (type == ConstantUtil.WRITE_WEIBO_TYPE_WRITEWEIBO) {
			tv_writeweibo_title.setText("写微博");
		} else {
			tv_writeweibo_title.setText("意见反馈");
		}
		et_share_write = (EditText) mainview.findViewById(R.id.et_share_write);
		tv_word_counter = (TextView) mainview.findViewById(R.id.tv_word_counter);
		ib_send = (Button) mainview.findViewById(R.id.ib_send);
		tv_word_counter.setText(ConstantUtil.WEIBO_MAX_LENGTH + "");
		et_share_write.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String mText = et_share_write.getText().toString();
				int len = mText.length();
				if (len <= WEIBO_MAX_LENGTH) {
					len = WEIBO_MAX_LENGTH - len;
					tv_word_counter.setTextColor(Color.BLACK);
					if (!ib_send.isEnabled()) {
						ib_send.setEnabled(true);
					}
				} else {
					len = len - WEIBO_MAX_LENGTH;
					tv_word_counter.setTextColor(Color.RED);
					if (ib_send.isEnabled()) {
						ib_send.setEnabled(false);
					}
				}
				tv_word_counter.setText(String.valueOf(len));
			}
		});
		et_share_write.setText(mContent);

		// 初始化转写Dialog, appid需要在http://open.voicecloud.cn获取.
		iatDialog = new RecognizerDialog(this, "appid=" + ConstantUtil.MSC_KEY);
		iatDialog.setListener(this);

		// 初始化缓存对象.
		mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
	}

	/**
	 * RecognizerDialogListener的"结束识别会话"回调接口. 参数详见<MSC开发指南>.
	 * 
	 * @param error
	 */
	@Override
	public void onEnd(SpeechError error) {
	}

	/**
	 * RecognizerDialogListener的"返回识别结果"回调接口. 通常服务端会多次返回结果调用此接口，结果需要进行累加.
	 * 
	 * @param results
	 * @param isLast
	 */
	@Override
	public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		et_share_write.append(builder);
		et_share_write.setSelection(et_share_write.length());
	}

	// send weibo
	public void sendWeibo(View v) {
		if (!TextUtils.isEmpty(userData.getToken())) {
			mContent = et_share_write.getText().toString();
			if (type == ConstantUtil.WRITE_WEIBO_TYPE_SUGGESTION) {
				mContent = WeiboUtil.getSuggetionPrefix() + mContent;// add some prefix
			}
			if (TextUtils.isEmpty(mContent)) {
				ToastUtil.showShortToast(getApplicationContext(), "朋友，请输入内容");
				return;
			}
			ToastUtil.showShortToast(getApplicationContext(), "微博发送中,请稍等...");//
			new AsyncTask<Void, Void, Boolean>() {// AsyncTask:call from UI thread
				protected Boolean doInBackground(Void... params) {
					Timeline timeline = new Timeline();
					timeline.client.setToken(userData.getToken());
					if ((imagePath == null) || "".equalsIgnoreCase(imagePath)) {
						try {
							timeline.UpdateStatus(mContent);
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					} else {
						try {
							String content = java.net.URLEncoder.encode(mContent, "utf-8");// 这里必须要进行编码处理！
							byte[] imagecontent = readFileImage(imagePath);
							ImageItem imageitem = new ImageItem(imagecontent);
							timeline.UploadStatus(content, imageitem);
						} catch (Exception e) {
							e.printStackTrace();
							return false;
						}
					}
					return true;
				}

				protected void onPostExecute(Boolean result) {
					if (result == Boolean.TRUE) {
						ToastUtil.showShortToast(getApplicationContext(), "微博发送成功");//
					} else {
						ToastUtil.showShortToast(getApplicationContext(), "微博发送失败");//
					}
				}
			}.execute();
			finish();// exit this activity
		} else {
			ToastUtil.showShortToast(getApplicationContext(), "朋友，请先登录");// 这种情况貌似不可能
		}
	}

	/**
	 * 显示转写对话框.
	 * 
	 * @param
	 */
	public void showIatDialog() {
		// 设置采样率参数，由于绝大部分手机只支持8K和16K，所以设置11K和22K采样率将无法启动录音.
		String rate = mSharedPreferences.getString(getString(R.string.preference_key_iat_rate),
				getString(R.string.preference_default_iat_rate));
		if (rate.equals("rate8k")) {
			iatDialog.setSampleRate(RATE.rate8k);
		} else if (rate.equals("rate11k")) {
			iatDialog.setSampleRate(RATE.rate11k);
		} else if (rate.equals("rate16k")) {
			iatDialog.setSampleRate(RATE.rate16k);
		} else if (rate.equals("rate22k")) {
			iatDialog.setSampleRate(RATE.rate22k);
		}
		// et_share_write.setText(null);//不要删除原来的内容
		// 弹出转写Dialog.
		iatDialog.show();
	}

	// /
	public void weibowrite_voice(View v) {
		showIatDialog();
	}

}