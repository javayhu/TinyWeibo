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
 * app start
 * 
 * @author hjw
 * 
 */
public class Appstart extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				UserData userdata = UserDataUtil.readUserData(getApplicationContext());//
				System.out.println("userid = " + userdata.getUserid());
				if (NetworkUtil.getNetworkState(Appstart.this) != NetworkUtil.NONE) {
					String localToken = userdata.getToken();
					String localExpiresIn = userdata.getExpirestime();
					Intent intent = null;
					if (UserDataUtil.isTokenValid(localToken, localExpiresIn)) {// valida
						if (userdata.isFirstrun()) {
							System.out.println("go to whats new");
							intent = new Intent(Appstart.this, Whatsnew.class);
						} else {
							System.out.println("go to mainweibo");
							intent = new Intent(Appstart.this, MainWeibo.class);
						}
					} else {// not valid -> oauth
						System.out.println("go to welcome");
						intent = new Intent(Appstart.this, Welcome.class);
					}
					startActivity(intent);
					Appstart.this.finish();
				} else {// network
					ToastUtil.showShortToast(Appstart.this, "网络不可用哟");
				}
			}
		}, 2000);
	}
}