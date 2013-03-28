package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;

public class GetCommentsWeekly {
	public static void main(String[] args) {
		String access_token = args[0];
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			JSONArray status = tm.getCommentsWeekly();
			Log.logInfo(status.toString());

		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
