package cn.edu.csu.iteliter.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class CacheUtil {

	
	public static final String TINYWEIBO_ROOT_PATH = android.os.Environment.getExternalStorageDirectory()
			.getAbsolutePath() + File.separator + "tinyweibo" + File.separator;
	// weibo image
	public static final String IMAGE_CACHE_PATH = TINYWEIBO_ROOT_PATH + "weiboimage" + File.separator;
	// taken picture
	public static final String PICTURE_CACHE_PATH = TINYWEIBO_ROOT_PATH + "takenpicture" + File.separator;
	// profile image
	public static final String PROFILE_CACHE_PATH = TINYWEIBO_ROOT_PATH + "profileimage" + File.separator;

	static {
		// makeDirectory();
		// System.out.println("make directory...");
		File file = new File(TINYWEIBO_ROOT_PATH);
		if (!file.exists()) {
			file.mkdirs();// create complete path
		}
		file = new File(PROFILE_CACHE_PATH);
		if (!file.exists()) {
			file.mkdirs();// create complete path
		}
		file = new File(IMAGE_CACHE_PATH);
		if (!file.exists()) {
			file.mkdirs();// create complete path
		}
		file = new File(PICTURE_CACHE_PATH);
		if (!file.exists()) {
			file.mkdirs();// create complete path
		}
	}

	// calculate space used
	public static double calculateSpaceUsed() {
		return getSize(new File(TINYWEIBO_ROOT_PATH));
	}

	// clear cache
	public static void clearCache() {
		deleteDirectory(IMAGE_CACHE_PATH);
		deleteDirectory(PICTURE_CACHE_PATH);
		deleteDirectory(PROFILE_CACHE_PATH);
	}

	// delete files in the specified directory
	private static void deleteDirectory(String cachePath) {
		File file = new File(cachePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			for (File file2 : files) {
				file2.delete();
			}
		}
	}

	// get size of the specified path
	public static double getSize(File file) {
		if (file.exists()) {
			if (!file.isFile()) {
				File[] fl = file.listFiles();
				double ss = 0;
				for (File f : fl) {
					ss += getSize(f);
				}
				return ss;
			} else {
				double ss = (double) file.length() / 1024 / 1024;
				return ss;
			}
		} else {
			return 0.0;
		}
	}

	// restore the bitmap by imagepath
	public static Bitmap restoreBitmap(String parentpath, String imageurl) {
		// System.out.println("restore bitmap : path = " + parentpath + EncryptDecrypt.encrypt(imageurl));
		String path = parentpath + EncryptDecrypt.encrypt(imageurl);
		File file = new File(path);
		if (file.exists()) {// if image file exists, just decode it and return
			try {
				// System.out.println("restore bitmap ok ");
				return BitmapFactory.decodeStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;// exception: is a directory
			}
		}
		return null;
	}

	// save file to specific path
	public static void saveImageToPath(Bitmap bitmap, String imagepath) {
		// System.out.println("save bitmap : path = " + imagepath);
		File file = new File(imagepath);
		try {
			if (!file.exists()) {
				file.createNewFile();
			}
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));// quality = 100
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
