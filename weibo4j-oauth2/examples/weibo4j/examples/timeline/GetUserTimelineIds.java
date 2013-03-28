package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class GetUserTimelineIds {
	public static void main(String[] args) {
		String access_token = args[0];
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			JSONObject ids = tm.getUserTimelineIdsByUid(args[1]);
			Log.logInfo(ids.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
}
