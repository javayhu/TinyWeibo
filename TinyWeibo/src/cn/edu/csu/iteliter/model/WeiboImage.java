package cn.edu.csu.iteliter.model;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * weibo item image<br/>
 * for better use,the image contains : profile and weibo image
 * 
 * @author hjw
 * 
 */
public class WeiboImage {

	public Bitmap bitmap;// bitmap
	public String imageurl;// url
	public ImageView imageView;// control

	public WeiboImage(ImageView imageView, String imageurl, Bitmap bitmap) {
		this.imageView = imageView;
		this.imageurl = imageurl;
		this.bitmap = bitmap;
	}

}
