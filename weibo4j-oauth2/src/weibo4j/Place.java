package weibo4j;

import weibo4j.model.PostParameter;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;
import weibo4j.util.WeiboConfig;

public class Place extends Weibo{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/****************动态读取************************/
	/**
	 * 注：没有写完
	 * 
	 * 获取当前登录用户与其好友的位置动态 
	 */
	public StatusWapper friendsTimeLine () throws WeiboException {
		return Status.constructWapperStatus(client.get(WeiboConfig.getValue("baseURL") + "place/friends_timeline.json"));
	}
	
	
	/****************用户读取************************/
	/**
	 * 获取LBS位置服务内的用户信息 
	 * 
	 * 
	 */
	public JSONObject userInfoInLBS (String uid) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/users/show.json",new PostParameter[] {
			new PostParameter("uid", uid)
		}).asJSONObject();
	}
	
	public JSONObject userInfoInLBS (String uid,int base_app) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/users/show.json",new PostParameter[] {
			new PostParameter("uid", uid),
			new PostParameter("base_app", base_app)
		}).asJSONObject();
	}
	
	/**
	 * 获取用户签到过的地点列表
	 * 
	 *   注：没有写完
	 */
	public JSONObject checkinsList (String uid) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/users/checkins.json",new PostParameter[] {
			new PostParameter("uid", uid)
		}).asJSONObject();
	}
	
	/**
	 * 获取用户的照片列表 
	 * 
	 * 注：没有写完
	 */
	
	public JSONObject photoList (String uid) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/users/photos.json",new PostParameter[] {
			new PostParameter("uid", uid)
		}).asJSONObject();
	}
	
	/**
	 * 获取用户的点评列表 
	 * 
	 * 注：没有写完
	 */
	
	public JSONObject tipsList (String uid) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/users/tips.json",new PostParameter[] {
			new PostParameter("uid", uid)
		}).asJSONObject();
	}
	
	/****************地点读取************************/
	/**
	 * 获取地点详情 
	 * 
	 * 
	 */
	public JSONObject poisShow (String poiid) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/pois/show.json",new PostParameter[] {
			new PostParameter("poiid", poiid)
		}).asJSONObject();
	}
	
	public JSONObject poisShow (String poiid,int base_app) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/pois/show.json",new PostParameter[] {
			new PostParameter("poiid", poiid),
			new PostParameter("base_app",base_app)
		}).asJSONObject();
	}
	
	/**
	 * 获取在某个地点签到的人的列表 
	 * 
	 * 注：没写完
	 */
	public JSONObject poisUsersList (String poiid) throws WeiboException {
		return client.get(WeiboConfig.getValue("baseURL") + "place/pois/show.json",new PostParameter[] { 
			new PostParameter("poiid", poiid) }).asJSONObject();
	}
	
	/**
	 * 获取在某个地点点评的列表 
	 * 
	 * 注：没写完
	 */
	public User poisTipsList (String poiid) throws WeiboException {
		return new User(client.get(WeiboConfig.getValue("baseURL") + "place/pois/tips.json",new PostParameter[] { 
			new PostParameter("poiid", poiid) }).asJSONObject());
	}
	
}
