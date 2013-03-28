package cn.edu.csu.iteliter.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import cn.edu.csu.iteliter.MainWeibo;
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.model.WeiboImage;

/**
 * weibo tool
 * 
 * @author hjw
 * 
 */
public class WeiboUtil {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm MM-dd");

	// async load user data
	public static void asyncLoadUserData(final Handler handler) {
		// System.out.println("asyncLoadUserData");
		// final UserData userData = UserDataUtil.readUserData(MainWeibo.instance);
		final UserData userData = MainWeibo.userData;
		// request for user info
		new Thread(new Runnable() {
			public void run() {
				// System.out.println("asyncLoadUserData running ...");
				Users users = new Users();
				users.client.setToken(userData.getToken());
				User user = null;
				try {
					user = users.showUserById(userData.getUserid());// here may be blocked!
				} catch (WeiboException e) {
					e.printStackTrace();// TODO:how to deal with the error
					// send null message!
				}

				Message message = new Message();
				message.obj = user;
				message.what = ConstantUtil.MESSAGE_TYPE_USERDATA;
				handler.sendMessage(message);

			}
		}).start();
	}

	// aysnc load weibo profile or image
	public static void asyncLoadWeiboImage(final Handler handler, final WeiboImage weiboImage, final int type) {
		// System.out.println("asyncLoadWeiboImage url = " + weiboImage.imageurl);

		new Thread(new Runnable() {
			public void run() {
				// System.out.println("load weibo image running ...");
				URL url = null;
				try {
					// connect and get inputstream
					url = new URL(weiboImage.imageurl);// protocol not found
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setDoInput(true);
					connection.setUseCaches(false);
					// construct a bitmap and round it
					Bitmap bitmap = null;
					if (connection.getInputStream() != null) {// connection reset by peer
						bitmap = BitmapFactory.decodeStream(connection.getInputStream());
					}
					if (bitmap == null) {
						return;
					}
					weiboImage.bitmap = ImageUtil.GetRoundedCornerBitmap(bitmap);

					Message message = new Message();
					message.what = ConstantUtil.MESSAGE_TYPE_WEIBOIMAGE;
					message.obj = weiboImage;
					handler.sendMessage(message);

					if (type == ConstantUtil.IMAGE_TYPE_PROFILE) {
						CacheUtil.saveImageToPath(weiboImage.bitmap,
								CacheUtil.PROFILE_CACHE_PATH + EncryptDecrypt.encrypt(weiboImage.imageurl));
					} else if (type == ConstantUtil.IMAGE_TYPE_IMAGE) {
						CacheUtil.saveImageToPath(weiboImage.bitmap,
								CacheUtil.IMAGE_CACHE_PATH + EncryptDecrypt.encrypt(weiboImage.imageurl));
					}

				} catch (Exception e) {
					e.printStackTrace();// TODO:handle this? // may happen
				}
			}
		}).start();
	}

	public static String formatSpaceSize(double spaceUsed) {
		if (spaceUsed < 1) {
			return "不到 1M";
		} else {
			DecimalFormat decimalFormat = new DecimalFormat("#0.00");
			return decimalFormat.format(spaceUsed) + "M";
		}
	}

	// format the date returned from weibo
	public static String formatWeiboDate(Date date) {
		return simpleDateFormat.format(date);
	}

	// get some phone infomation for suggestion prefix
	public static String getSuggetionPrefix() {
		// String phoneInfo = "Product: " + android.os.Build.PRODUCT;
		// phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
		// phoneInfo += ", TAGS: " + android.os.Build.TAGS;
		// phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
		// phoneInfo += ", MODEL: " + android.os.Build.MODEL;
		// phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
		// phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
		// phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
		// phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
		// phoneInfo += ", BRAND: " + android.os.Build.BRAND;
		// phoneInfo += ", BOARD: " + android.os.Build.BOARD;
		// phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
		// phoneInfo += ", ID: " + android.os.Build.ID;
		// phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
		// phoneInfo += ", USER: " + android.os.Build.USER;
		// System.out.println(phoneInfo);
		// Product: libra_galaxysmtd, CPU_ABI: armeabi-v7a, TAGS: test-keys, VERSION_CODES.BASE: 1, MODEL: GT-I9000,
		// SDK: 10, VERSION.RELEASE: 2.3.7, DEVICE: galaxysmtd, DISPLAY: MIUI.2.4.13, BRAND: samsung, BOARD: aries,
		// FINGERPRINT: samsung/libra_galaxysmtd/galaxysmtd:2.3.7/MIUI/2.4.13:userdebug/test-keys, ID: MIUI,
		// MANUFACTURER: samsung, USER: builder
		StringBuffer stringBuffer = new StringBuffer(ConstantUtil.SUGGESTION_PREFIX);
		stringBuffer.append("型号 " + android.os.Build.MODEL + ",");
		stringBuffer.append("系统 " + android.os.Build.VERSION.RELEASE + ",反馈意见: ");
		return stringBuffer.toString();
	}

	// restore bitmap:first load from cache,if no,then http request
	public static void restoreBitmap(String cachePath, String profileImageUrl, Handler handler, ImageView imageView,
			int type) {
		Bitmap image = CacheUtil.restoreBitmap(cachePath, profileImageUrl);
		// System.out.println("validate image  userprofile : " + image);
		if (image != null) {
			image = ImageUtil.GetRoundedCornerBitmap(image);// round it
			imageView.setImageBitmap(image);
		} else {
			asyncLoadWeiboImage(handler, new WeiboImage(imageView, profileImageUrl, null), type);
		}
	}
}
