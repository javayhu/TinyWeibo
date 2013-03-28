package weibo4j.examples.publicservice;

import weibo4j.PublicService;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class GetTimeZone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {;
		String access_token = args[0];
		PublicService ps = new PublicService();
		ps.client.setToken(access_token);
		try {
			JSONObject	jo = ps.getTomeZone();
			System.out.println(jo.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
	}

}
