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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import cn.edu.csu.iteliter.util.CacheUtil;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.ToastUtil;

import com.aviary.android.feather.Constants;
import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.R;
import com.aviary.android.feather.library.utils.DecodeUtils;
import com.aviary.android.feather.library.utils.ImageLoader.ImageSizes;
import com.aviary.android.feather.library.utils.StringUtils;

public class WeiboWriteImage extends Activity {

	class DownloadAsync extends AsyncTask<Uri, Void, Bitmap> implements OnCancelListener {

		private ProgressDialog mProgress;
		private Uri mUri;

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
			// final int w = ll_weibowrite_imagecontainer.getWidth();//
			ImageSizes sizes = new ImageSizes();
			bitmap = DecodeUtils.decode(WeiboWriteImage.this, mUri, imageWidth, imageHeight, sizes);
			return bitmap;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			cancel(true);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

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

	}

	/** apikey is required http://developers.aviary.com/ */
	private static final String API_KEY = ConstantUtil.AVIARY_KEY;

	public static final String LOG_TAG = "WeiboWriteImage";
	private Button btn_weibowrite_imagedelete;

	private Button btn_weibowrite_imageok;
	private String imagePath;
	private Uri imageUri;
	private int imageWidth, imageHeight;
	private ImageView iv_weibowrite_image;

	private View ll_weibowrite_imagecontainer;

	/** session id for the hi-res post processing */
	private String mSessionId;

	// btn_weibowrite_showdialog
	public void btn_weibowrite_showdialog(View v) {
		Intent intent = new Intent(WeiboWriteImage.this, HomeWeiboWriteTopRightDialog.class);
		startActivityForResult(intent, ConstantUtil.REQUEST_PICTURE_TYPE);
	}

	// btn back
	public void btnback(View v) {
		// // return restult
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Delete a file without throwing any exception<br />
	 * 
	 * @param path
	 * @return
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
	 * Load the incoming Image
	 * 
	 * @param uri
	 */
	private void loadAsync(final Uri uri) {
		Log.i(LOG_TAG, "loadAsync: " + uri);//

		Drawable toRecycle = iv_weibowrite_image.getDrawable();
		if ((toRecycle != null) && (toRecycle instanceof BitmapDrawable)) {
			if (((BitmapDrawable) iv_weibowrite_image.getDrawable()).getBitmap() != null) {
				((BitmapDrawable) iv_weibowrite_image.getDrawable()).getBitmap().recycle();
			}
		}
		iv_weibowrite_image.setImageDrawable(null);
		// imageUri = null;//这里有误！
		DownloadAsync task = new DownloadAsync();
		task.execute(uri);
	}

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
				System.out.println("pick uri = " + intent.getData());// content://
				loadAsync(intent.getData());
			} else if (requestCode == ConstantUtil.REQUEST_TAKE_PICTURE) {
				System.out.println("take uri = " + intent.getData());// file://
				loadAsync(intent.getData());//
			} else if (requestCode == ConstantUtil.REQUEST_AVIARY_FEATURE) {
				updateMedia(imagePath);
				System.out.println("feature uri = " + intent.getData());// file:///mnt/sdcard/tinyweibo/weiboimage/aviary_xxxx.jpg
				loadAsync(intent.getData());
			}
		}
	}

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
		if ((bundle != null) && (bundle.getString(ConstantUtil.IMAGEPATH) != null)) {
			imagePath = bundle.getString(ConstantUtil.IMAGEPATH);
			imageUri = Uri.parse(bundle.getString(ConstantUtil.IMAGEURI));
			loadAsync(imageUri);
		}

	}

	@Override
	protected void onDestroy() {
		Log.i(LOG_TAG, "onDestroy");
		super.onDestroy();
		// mOutputFilePath = null;//
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		setResult(RESULT_CANCELED);
		return super.onKeyDown(keyCode, event);
	}

	// 本地相册
	public void pickMobilePicture() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);// uri --> content:// /external/images/media/181
		intent.setType("image/*");
		startActivityForResult(intent, ConstantUtil.REQUEST_PICK_PICTURE);
	}

	/**
	 * Given an Uri load the bitmap into the current ImageView and resize it to fit the image container size
	 * 
	 * @param uri
	 */
	private boolean setImageURI(final Uri uri, final Bitmap bitmap) {
		// Log.d(LOG_TAG, "image size: " + bitmap.getWidth() + "x" + bitmap.getHeight());//
		btn_weibowrite_imageok.setEnabled(true);
		iv_weibowrite_image.setImageBitmap(bitmap);
		imageUri = uri;
		imagePath = CacheUtil.PICTURE_CACHE_PATH + "tmppic_" + SystemClock.currentThreadTimeMillis() + ".jpg";
		System.out.println("mOutputFilePath = " + imagePath);
		CacheUtil.saveImageToPath(bitmap, imagePath);// save the image
		return true;
	}

	/**
	 * Once you've chosen an image you can start the feather activity
	 * 
	 * @param uri
	 * @param outputFilePath
	 */
	private void startFeather(Uri uri, String outputFilePath) {
		Log.d(LOG_TAG, "uri: " + uri);// uri: content://media/external/images/media/182 -- > gallery
		// file:///data/data/com.miui.camera/files/image_temp.jpg -- > camera

		// create a temporary file where to store the resulting image
		// File file = new File(CacheUtil.PICTURE_CACHE_PATH + "aviary_" + System.currentTimeMillis() +
		// ".jpg");//处理过后的图片存储位置
		// if (null != file) {
		// mOutputFilePath = file.getAbsolutePath();
		// }
		// Create the intent needed to start feather
		Intent newIntent = new Intent(this, FeatherActivity.class);
		// set the source image uri
		newIntent.setData(uri);
		// pass the required api_key and secret ( see
		// http://developers.aviary.com/ )
		newIntent.putExtra("API_KEY", API_KEY);
		// pass the uri of the destination image file (optional)
		// This will be the same uri you will receive in the onActivityResult
		newIntent.putExtra("output", Uri.parse("file://" + outputFilePath));// 返回时data中保存的uri
		System.out.println("output = " + outputFilePath);
		// format of the destination image (optional)
		newIntent.putExtra(Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.name());
		// output format quality (optional)
		newIntent.putExtra(Constants.EXTRA_OUTPUT_QUALITY, 90);
		// If you want to disable the external effects
		// newIntent.putExtra( Constants.EXTRA_EFFECTS_ENABLE_EXTERNAL_PACKS, false );

		// If you want to disable the external effects
		// newIntent.putExtra( Constants.EXTRA_STICKERS_ENABLE_EXTERNAL_PACKS, false );

		// enable fast rendering preview
		// newIntent.putExtra( Constants.EXTRA_EFFECTS_ENABLE_FAST_PREVIEW, true );

		// you can force feather to display only a certain ( see FilterLoaderFactory#Filters )
		// you can omit this if you just wanto to display the default tools

		/*
		 * newIntent.putExtra( "tools-list", new String[] { FilterLoaderFactory.Filters.ENHANCE.name(),
		 * FilterLoaderFactory.Filters.EFFECTS.name(), FilterLoaderFactory.Filters.STICKERS.name(),
		 * FilterLoaderFactory.Filters.ADJUST.name(), FilterLoaderFactory.Filters.CROP.name(),
		 * FilterLoaderFactory.Filters.BRIGHTNESS.name(), FilterLoaderFactory.Filters.CONTRAST.name(),
		 * FilterLoaderFactory.Filters.SATURATION.name(), FilterLoaderFactory.Filters.SHARPNESS.name(),
		 * FilterLoaderFactory.Filters.DRAWING.name(), FilterLoaderFactory.Filters.TEXT.name(),
		 * FilterLoaderFactory.Filters.MEME.name(), FilterLoaderFactory.Filters.RED_EYE.name(),
		 * FilterLoaderFactory.Filters.WHITEN.name(), FilterLoaderFactory.Filters.BLEMISH.name(),
		 * FilterLoaderFactory.Filters.COLORTEMP.name(), } );
		 */

		// you want the result bitmap inline. (optional)
		// newIntent.putExtra( Constants.EXTRA_RETURN_DATA, true );

		// you want to hide the exit alert dialog shown when back is pressed
		// without saving image first
		// newIntent.putExtra( Constants.EXTRA_HIDE_EXIT_UNSAVE_CONFIRMATION, true );

		// -- VIBRATION --
		// Some aviary tools use the device vibration in order to give a better experience
		// to the final user. But if you want to disable this feature, just pass
		// any value with the key "tools-vibration-disabled" in the calling intent.
		// This option has been added to version 2.1.5 of the Aviary SDK
		newIntent.putExtra(Constants.EXTRA_TOOLS_DISABLE_VIBRATION, true);
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int max_size = Math.min(metrics.widthPixels, metrics.heightPixels);
		// you can pass the maximum allowed image size, otherwise feather will determine
		// the max size based on the device memory
		// Here we're passing the current display size as max image size because after
		// the execution of Aviary we're saving the HI-RES image so we don't need a big
		// image for the preview
		max_size = (int) (max_size / 0.8);
		// Log.d(LOG_TAG, "max-image-size: " + max_size);//
		newIntent.putExtra("max-image-size", max_size);
		// Enable/disable the default borders for the effects
		newIntent.putExtra("effect-enable-borders", true);
		// You need to generate a new session id key to pass to Aviary feather
		// this is the key used to operate with the hi-res image ( and must be unique for every new instance of Feather
		// )
		// The session-id key must be 64 char length
		mSessionId = StringUtils.getSha256(System.currentTimeMillis() + API_KEY);
		// Log.d(LOG_TAG, "session: " + mSessionId + ", size: " + mSessionId.length());//
		newIntent.putExtra("output-hires-session-id", mSessionId);
		// ..and start feather
		startActivityForResult(newIntent, ConstantUtil.REQUEST_AVIARY_FEATURE);
	}

	// 拍照功能
	public void takeCameraPicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, ConstantUtil.REQUEST_TAKE_PICTURE);
	}

	/**
	 * We need to notify the MediaScanner when a new file is created. In this way all the gallery applications will be
	 * notified too.
	 * 
	 * @param file
	 */
	private void updateMedia(String filepath) {
		Log.i(LOG_TAG, "updateMedia: " + filepath);
		MediaScannerConnection.scanFile(getApplicationContext(), new String[] { filepath }, null, null);
		// 当图片在compress（）保存之后，使用gallery去查看的时候，发现刚才保存的图片不存在。
		// 这种情况下，需要使用 MediaScannerConnection 去通知系统扫描多媒体数据库
	}

	// delete image
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

	public void weibowrite_imageok(View v) {
		if ((imagePath != null) && !imagePath.equalsIgnoreCase("")) {
			Intent intent = new Intent();
			intent.putExtra(ConstantUtil.IMAGEPATH, imagePath);
			intent.putExtra(ConstantUtil.IMAGEURI, imageUri.toString());
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	public void weibowrite_launcherAviary(View v) {
		if (imageUri != null) {
			startFeather(imageUri, imagePath);
		}
	}
}
