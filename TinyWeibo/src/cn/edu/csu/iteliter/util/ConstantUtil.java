package cn.edu.csu.iteliter.util;

/**
 * constant tool interface
 * 
 * @author hjw
 */
public interface ConstantUtil {

	// author sinaweibo user id
	public static final String AUTHOR_UID = "3018213722";
	// aviary app key
	public static final String AVIARY_KEY = "a402c5831";

	// just share
	// public static String APP_KEY = "838435498";
	// public static String APP_SECRET = "2c418e9f114d93264245a14e9624d869";

	// app key app secret
	public static String CONSUMER_KEY = "757483506";// 146833241 // 757483506 [itelite // chinaitelite] 757483506

	public static String CONSUMER_SECRET = "1fdb082da2744c57294523e9151105d4";
	// 6dc8e747f71e5aa27e16a8aaa97a028d // //1fdb082da2744c57294523e9151105d4

	// msc app key
	public static final String MSC_KEY = "50d19dae";
	// redirect url
	public static final String REDIRECT_URL = "http://www.sina.com";

	// register url
	public static final String REGISTER_URL = "http://3g.sina.com.cn/prog/wapsite/sso/register.php?vt=3&revalid=2&ns=1&type=m&fw=1&UA=Mozilla&m=mJ402p4K6x89&mCnt=2";
	public static final int REQUEST_AVIARY_FEATURE = 0x4;

	public static final int REQUEST_PICK_PICTURE = 0x3;
	public static final int REQUEST_PICTURE_TYPE = 0x1;
	public static final int REQUEST_TAKE_PICTURE = 0x2;

	// request code
	public static final int REQUEST_WEIBO_TYPE = 0x0;
	public static final int REQUEST_WEIBOWRITE_IMAGE = 0x5;
	// repost comment : passing status id
	public static final String STATUS_ID = "statusid";
	// for suggestion prefix
	public static final String SUGGESTION_PREFIX = "@csuitelite #微微博Android客户端# 版本 1.0 ";
	public static final int TIMELINE_FRIENDS = 0x11;
	public static final int TIMELINE_MENTIONS = 0x13;

	// timeline type
	public static final int TIMELINE_PUBLIC = 0x10;
	public static final int TIMELINE_USER = 0x12;
	// sharedpreference name
	public static final String TINYWEIBO = "TinyWeibo";
	// max length
	public static final int WEIBO_MAX_LENGTH = 140;
	// image file type
	public final int IMAGE_TYPE_CAMERA = 0x30;
	public final int IMAGE_TYPE_IMAGE = 0x30;
	public final int IMAGE_TYPE_MOBILE = 0x31;

	public final int IMAGE_TYPE_PICTURE = 0x30;
	public final int IMAGE_TYPE_PROFILE = 0x31;
	public final String IMAGEPATH = "imagepath";
	// request type or keys
	public final String IMAGEURI = "imageuri";

	public final int MESSAGE_TYPE_SHAKE_STATUS = 0x24;
	public final int MESSAGE_TYPE_SHAKE_USER = 0x25;
	public final int MESSAGE_TYPE_TIMELINE = 0x21;
	// message type
	public final int MESSAGE_TYPE_USERDATA = 0x20;

	public final int MESSAGE_TYPE_USERINFO = 0x23;
	public final int MESSAGE_TYPE_WEIBOIMAGE = 0x22;

	public final String PICTURE_TYPE = "picture_type";
	public final String SHAKE_TYPE = "shake_type";
	public final int SHAKE_TYPE_ADDFRIEND = 0x41;
	public final int SHAKE_TYPE_FEATUREWEIBO = 0x43;
	public final int SHAKE_TYPE_MODEWEIBO = 0x42;

	// shake type
	public final int SHAKE_TYPE_NEARBYWEIBO = 0x40;
	public final String TIMELINE_TYPE = "timeline_type";
	public final String WEIBO_TYPE = "weibo_type";
	// weiboitem type
	public final int WEIBO_TYPE_HAPPY = 0x51;

	public final int WEIBO_TYPE_MUSIC = 0x54;
	public final int WEIBO_TYPE_PICTURE = 0x55;
	public final int WEIBO_TYPE_SAD = 0x53;
	public final int WEIBO_TYPE_VIDEO = 0x56;
	public final int WEIBO_TYPE_WULIAO = 0x52;
	public final String WRITE_WEIBO_TYPE = "write_weibo_type";

	public final int WRITE_WEIBO_TYPE_SUGGESTION = 0x62;
	// write weibo type
	public final int WRITE_WEIBO_TYPE_WRITEWEIBO = 0x61;

}
