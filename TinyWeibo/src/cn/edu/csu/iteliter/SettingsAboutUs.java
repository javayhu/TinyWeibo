package cn.edu.csu.iteliter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * settings about us
 * 
 * @author hjw
 * 
 */
public class SettingsAboutUs extends Activity {

	// back btn
	public void btn_back(View v) {
		finish();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_aboutus);
	}
}