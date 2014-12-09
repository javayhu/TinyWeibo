/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:31
 */
package cn.edu.csu.iteliter;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import cn.edu.csu.iteliter.util.CacheUtil;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.ToastUtil;

import com.aviary.android.feather.Constants;
import com.aviary.android.feather.FeatherActivity;

//import com.aviary.android.feather.R;//comment here

import com.aviary.android.feather.library.utils.DecodeUtils;
import com.aviary.android.feather.library.utils.ImageLoader.ImageSizes;
import com.aviary.android.feather.library.utils.StringUtils;

/**
 * @filename WeiboWriteImage.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 写微博的图片选择和处理界面
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午3:18:39
 * @version 1.0
 * 
 */
public class WeiboWriteImage extends Activity {

	/** Aviary应用使用的App Key */
	private static final String API_KEY = ConstantUtil.AVIARY_KEY;

	/** 删除图片的按钮 */
	private Button btn_weibowrite_imagedelete;

	/** 确定图片的按钮 */
	private Button btn_weibowrite_imageok;

	/** 显示图片的组件 */
	private ImageView iv_weibowrite_image;

	/** 包裹图片的父组件 */
	private View ll_weibowrite_imagecontainer;

	/** 图片的路径 */
	private String imagePath;

	/** 图片的URI */
	private Uri imageUri;

	/** 图片的宽度和高度 */
	private int imageWidth, imageHeight;

	/** 图片处理的session id，供aviary使用 */
	private String mSessionId;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_weibo_write_pic);

		btn_weibowrite_imagedelete = (Button) findViewById(cn.edu.csu.iteliter.R.id.btn_weibowrite_imagedelete);
		btn_weibowrite_imageok = (Button) findViewById(cn.edu.csu.iteliter.R.id.btn_weibowrite_imageok);
		btn_weibowrite_imageok.setEnabled(false);

		iv_weibowrite_image = ((ImageView) findViewById(R.id.iv_weibowrite_image));
		ll_weibowrite_imagecontainer = findViewById(R.id.ll_weibowrite_imagecontainer);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		imageWidth = (int) (metrics.widthPixels / 1.5);
		imageHeight = (int) (metrics.heightPixels / 1.5);

		// 如果传递过来的数据不为空的话，那么就加载图片
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString(ConstantUtil.IMAGEPATH) != null) {
			imagePath = bundle.getString(ConstantUtil.IMAGEPATH);
			imageUri = Uri.parse(bundle.getString(ConstantUtil.IMAGEURI));
			loadAsync(imageUri);
		}

	}

	/**
	 * 删除图片
	 * 
	 * @param v
	 *            View组件
	 */
	public void weibowrite_imagedelete(View v) {
		Dialog dialog = new AlertDialog.Builder(this).setTitle(R.string.weibosdk_attention)
				.setMessage(R.string.weibosdk_del_pic)
				.setPositiveButton(R.string.weibosdk_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						iv_weibowrite_image.setImageResource(cn.edu.csu.iteliter.R.drawable.tinyweibologo);
						if (imagePath != null) {
							deleteFileNoThrow(imagePath);
							imagePath = null;
						}
						imageUri = null;
						btn_weibowrite_imageok.setEnabled(false);
					}
				}).setNegativeButton(R.string.weibosdk_cancel, null).create();
		dialog.show();
	}

	/**
	 * 启动Aviary图片编辑器
	 * 
	 * @param v
	 *            View组件
	 */
	public void weibowrite_launcherAviary(View v) {
		if (imageUri != null) {
			startFeather(imageUri, imagePath);
		}
	}

	/**
	 * 确定图片
	 * 
	 * @param v
	 *            View组件
	 */
	public void weibowrite_imageok(View v) {
		if (imagePath != null && !imagePath.equalsIgnoreCase("")) {
			Intent intent = new Intent();
			intent.putExtra(ConstantUtil.IMAGEPATH, imagePath);
			intent.putExtra(ConstantUtil.IMAGEURI, imageUri.toString());
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	/*
	 * (non-Javadoc)当用户按下手机键时的回调方法
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		setResult(RESULT_CANCELED);
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 界面返回
	 * 
	 * @param v
	 *            the v
	 */
	public void btnback(View v) {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * 显示选择要插入的图片的类型
	 * 
	 * @param v
	 *            View组件
	 */
	public void btn_weibowrite_showdialog(View v) {
		Intent intent = new Intent(WeiboWriteImage.this, HomeWeiboWriteTopRightDialog.class);
		startActivityForResult(intent, ConstantUtil.REQUEST_PICTURE_TYPE);
	}

	/*
	 * (non-Javadoc)当Activity返回结果时的回调方法
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == ConstantUtil.REQUEST_PICTURE_TYPE) {
				if (intent.getIntExtra(ConstantUtil.PICTURE_TYPE, ConstantUtil.REQUEST_PICK_PICTURE) == ConstantUtil.REQUEST_PICK_PICTURE) {
					pickMobilePicture();
				} else {
					takeCameraPicture();
				}
			} else if (requestCode == ConstantUtil.REQUEST_PICK_PICTURE) {
				loadAsync(intent.getData());
			} else if (requestCode == ConstantUtil.REQUEST_TAKE_PICTURE) {
				loadAsync(intent.getData());//
			} else if (requestCode == ConstantUtil.REQUEST_AVIARY_FEATURE) {
				updateMedia(imagePath);
				loadAsync(intent.getData());
			}
		}
	}

		/**
		 * 拍照得到图片
		 */
		public void takeCameraPicture() {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(intent, ConstantUtil.REQUEST_TAKE_PICTURE);
		}
		
		/**
		 * 从本地相册选择图片
		 */
		public void pickMobilePicture() {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// uri --> content:// /external/images/media/181
			intent.setType("image/*");
			startActivityForResult(intent, ConstantUtil.REQUEST_PICK_PICTURE);
		}

	/**
	 * 异步加载图片
	 * 
	 * @param uri
	 *            图片的URI
	 */
	private void loadAsync(final Uri uri) {
		Drawable toRecycle = iv_weibowrite_image.getDrawable();
		if (toRecycle != null && toRecycle instanceof BitmapDrawable) {
			if (((BitmapDrawable) iv_weibowrite_image.getDrawable()).getBitmap() != null) {
				((BitmapDrawable) iv_weibowrite_image.getDrawable()).getBitmap().recycle();
			}
		}
		iv_weibowrite_image.setImageDrawable(null);
		DownloadAsync task = new DownloadAsync();
		task.execute(uri);
	}

	/**
	 * 以不抛出异常的方式删除文件
	 * 
	 * @param path
	 *            文件的路径
	 * @return 删除成功返回true
	 */
	private boolean deleteFileNoThrow(String path) {
		File file;
		try {
			file = new File(path);
		} catch (NullPointerException e) {
			return false;
		}
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 设置图片的URI和其他的信息
	 * 
	 * @param uri
	 *            图片的URI
	 * @param bitmap
	 *            图片的Bitmap
	 */
	private void setImageURI(final Uri uri, final Bitmap bitmap) {
		btn_weibowrite_imageok.setEnabled(true);
		iv_weibowrite_image.setImageBitmap(bitmap);
		imageUri = uri;
		imagePath = CacheUtil.PICTURE_CACHE_PATH + "tmppic_" + SystemClock.currentThreadTimeMillis() + ".jpg";
		CacheUtil.saveImageToPath(bitmap, imagePath);// save the image
	}

	/**
	 * 更新media库
	 * 
	 * @param filepath
	 *            文件的路径
	 */
	private void updateMedia(String filepath) {
		// 当图片在compress（）保存之后，使用gallery去查看的时候，发现刚才保存的图片不存在。
		// 这种情况下，需要使用 MediaScannerConnection 去通知系统扫描多媒体数据库
		MediaScannerConnection.scanFile(getApplicationContext(), new String[] { filepath }, null, null);
	}

	/**
	 * 启动Aviary图片编辑器
	 * 
	 * @param uri
	 *            图片的URI
	 * @param outputFilePath
	 *            输出的图片的路径
	 */
	private void startFeather(Uri uri, String outputFilePath) {
		// Create the intent needed to start feather
		Intent newIntent = new Intent(this, FeatherActivity.class);
		// set the source image uri
		newIntent.setData(uri);
		// pass the required api_key and secret
		newIntent.putExtra("API_KEY", API_KEY);
		// pass the uri of the destination image file (optional)
		// This will be the same uri you will receive in the onActivityResult
		newIntent.putExtra("output", Uri.parse("file://" + outputFilePath));// 返回时data中保存的uri
		// format of the destination image (optional)
		newIntent.putExtra(Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.name());
		// output format quality (optional)
		newIntent.putExtra(Constants.EXTRA_OUTPUT_QUALITY, 90);
		newIntent.putExtra(Constants.EXTRA_TOOLS_DISABLE_VIBRATION, true);
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int max_size = Math.min(metrics.widthPixels, metrics.heightPixels);
		max_size = (int) (max_size / 0.8);
		newIntent.putExtra("max-image-size", max_size);
		// Enable/disable the default borders for the effects
		newIntent.putExtra("effect-enable-borders", true);
		// The session-id key must be 64 char length
		mSessionId = StringUtils.getSha256(System.currentTimeMillis() + API_KEY);
		newIntent.putExtra("output-hires-session-id", mSessionId);
		startActivityForResult(newIntent, ConstantUtil.REQUEST_AVIARY_FEATURE);
	}

	/**
	 * 异步加载图片的任务类
	 */
	class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements OnCancelListener {

		/** 含有进度条的对话框 */
		private ProgressDialog mProgress;

		/** 图片的URI */
		private Uri mUri;

		/*
		 * (non-Javadoc)加载之前的回调方法
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgress = new ProgressDialog(WeiboWriteImage.this);
			mProgress.setIndeterminate(true);
			mProgress.setCancelable(true);
			mProgress.setMessage("加载图片...");
			mProgress.setOnCancelListener(this);
			mProgress.show();
		}

		/*
		 * (non-Javadoc)后台执行的方法
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Bitmap doInBackground(Uri... params) {
			mUri = params[0];
			Bitmap bitmap = null;
			while (ll_weibowrite_imagecontainer.getWidth() < 1) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ImageSizes sizes = new ImageSizes();
			bitmap = DecodeUtils.decode(WeiboWriteImage.this, mUri, imageWidth, imageHeight, sizes);
			return bitmap;
		}

		/*
		 * (non-Javadoc)加载之后的回调方法
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (mProgress.getWindow() != null) {
				mProgress.dismiss();
			}
			if (result != null) {
				setImageURI(mUri, result);
			} else {
				ToastUtil.showShortToast(getApplicationContext(), "图片加载失败");
			}
		}

		/*
		 * (non-Javadoc)取消加载的回调方法
		 * 
		 * @see android.content.DialogInterface.OnCancelListener#onCancel(android.content.DialogInterface)
		 */
		@Override
		public void onCancel(DialogInterface dialog) {
			cancel(true);
		}

		/*
		 * (non-Javadoc)加载取消之后的回调方法
		 * 
		 * @see android.os.AsyncTask#onCancelled()
		 */
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

	}
}
