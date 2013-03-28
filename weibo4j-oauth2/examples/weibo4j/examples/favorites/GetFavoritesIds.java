package weibo4j.examples.favorites;

import weibo4j.Favorite;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class GetFavoritesIds {

	public static void main(String[] args) {
		String access_token =args[0];
		Favorite fm = new Favorite();
		fm.client.setToken(access_token);
		try {
			JSONObject ids = fm.getFavoritesIds();
			Log.logInfo(ids.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
