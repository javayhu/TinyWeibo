package weibo4j.examples.favorites;

import weibo4j.Favorite;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;

public class DestroyFavoritesBatch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String access_token = args[0];
		boolean result = false;
		String ids = args[1];
		Favorite fm = new Favorite();
		fm.client.setToken(access_token);
		try {
			result = fm.destroyFavoritesTagsBatch(ids);
			Log.logInfo(String.valueOf(result));
		} catch (WeiboException e) {

			e.printStackTrace();
		}
	}

}
