package cn.edu.csu.iteliter;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import cn.edu.csu.iteliter.util.ToastUtil;
import cn.edu.csu.iteliter.util.UserDataUtil;

/**
 * dialog exit in settings
 * 
 * @author hjw
 * 
 */
public class DialogSettingsExit extends Activity {
	private LinearLayout ll_settings_exit;

	public void btn_settings_exitcancle(View v) {
		finish();
	}

	// exit ok
	public void btn_settings_exitok(View v) {
		UserDataUtil.clearUserData(MainWeibo.instance);
		finish();
		MainWeibo.instance.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_settings_exit);
		// dialog=new MyDialog(this);
		ll_settings_exit = (LinearLayout) findViewById(R.id.ll_settings_exit);
		ll_settings_exit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ToastUtil.showShortToast(getApplicationContext(), "点击窗口外部关闭窗口！");
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
