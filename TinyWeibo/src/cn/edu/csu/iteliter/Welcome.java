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
 * welcome [login and register]
 * 
 * @author hjw
 * 
 */
public class Welcome extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	}

	// login
	public void welcome_login(View v) {
		AndroidWeibo.getInstance().authorize(Welcome.this, new AuthDialogListener());
	}

	// enter mainweibo
	private void enterMainWeibo() {
		Intent intent = new Intent(Welcome.this, Whatsnew.class);
		startActivity(intent);
		Welcome.this.finish();
	}

	// register
	public void welcome_register(View v) {
		Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantUtil.REGISTER_URL));
		startActivity(webIntent);
	}

	// weibo oauth dialog listener
	class AuthDialogListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			ToastUtil.showShortToast(getApplicationContext(), "授权成功");
			// System.out.println("complete");
			String token = values.getString("access_token");// 2.00UQHQSD0FCGwJa84974706d6E1OjB
			String expiresIn = values.getString("expires_in");// 626038 // remind_in 626038
			String uid = values.getString("uid");// 3018213722
			// access_token remind_in uid expires_in
			long expiresTime = System.currentTimeMillis() + Long.parseLong(expiresIn) * 1000;
			UserDataUtil.updateLocalToken(Welcome.this, new UserData(token, String.valueOf(expiresTime), uid));// userdata
			enterMainWeibo();
		}

		@Override
		public void onError(WeiboDialogError e) {
			ToastUtil.showShortToast(getApplicationContext(), "授权失败");
		}

		@Override
		public void onCancel() {
			ToastUtil.showShortToast(getApplicationContext(), "取消授权");
		}

		@Override
		public void onWeiboException(WeiboException e) {
			ToastUtil.showShortToast(getApplicationContext(), "授权异常");
		}
	}

}
