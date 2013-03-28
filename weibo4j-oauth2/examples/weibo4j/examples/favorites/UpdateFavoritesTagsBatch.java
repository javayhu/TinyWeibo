package weibo4j.examples.favorites;

import weibo4j.Favorite;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class UpdateFavoritesTagsBatch {

	public static void main(String[] args) {
		String access_token = args[0];
		Favorite fm = new Favorite();
		fm.client.setToken(access_token);
		String tid = args[1];
		String tag= args[2];
		try {
			JSONObject json = fm.updateFavoritesTagsBatch(tid, tag);
			Log.logInfo(json.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
