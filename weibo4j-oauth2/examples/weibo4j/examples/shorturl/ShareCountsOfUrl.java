package weibo4j.examples.shorturl;

import weibo4j.ShortUrl;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class ShareCountsOfUrl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String access_token = args[0];
		String url = args[1];
		ShortUrl su = new ShortUrl();
		su.client.setToken(access_token);
		try {
			JSONObject jo = su.shareCountsOfUrl(url);
			System.out.println(jo.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
