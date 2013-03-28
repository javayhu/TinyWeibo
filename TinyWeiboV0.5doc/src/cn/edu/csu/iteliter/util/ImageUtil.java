/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:31
 */
package cn.edu.csu.iteliter.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * @filename ImageUtil.java
 * @package cn.edu.csu.iteliter.util
 * @project TinyWeibo 微微博
 * @description 图片处理工具类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 上午11:00:57
 * @version 1.0
 * 
 */
public class ImageUtil {

	/**
	 * 对指定的图片进行圆角处理
	 * 
	 * @param bitmap
	 *            要圆角处理的图片的bitmap信息
	 * @return 圆角之后的图片的bitmap信息
	 */
	public static Bitmap GetRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
			final float roundPx = 4;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 对指定的图片进行旋转处理
	 * 
	 * @param bitmap
	 *            要旋转的图片的bitmap信息
	 * @return 旋转之后的图片的bitmap信息
	 */
	public static Bitmap RotateBitmap(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(90);// Setting post rotate to 90
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

}
