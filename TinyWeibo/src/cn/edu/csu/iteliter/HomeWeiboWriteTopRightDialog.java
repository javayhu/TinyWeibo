package cn.edu.csu.iteliter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import cn.edu.csu.iteliter.util.ConstantUtil;

/**
 * Home weibowrite top right dialog <br/>
 * 
 * @author hjw
 * 
 */
public class HomeWeiboWriteTopRightDialog extends Activity {

	public void ll_pickpic(View v) {
		setResult(RESULT_OK, (new Intent()).putExtra(ConstantUtil.PICTURE_TYPE, ConstantUtil.REQUEST_PICK_PICTURE));
		finish();
	}

	public void ll_takepic(View v) {
		setResult(RESULT_OK, (new Intent()).putExtra(ConstantUtil.PICTURE_TYPE, ConstantUtil.REQUEST_TAKE_PICTURE));
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_weibowrite_dialog_choosetype);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		setResult(RESULT_CANCELED);
		finish();
		return true;
	}

}
