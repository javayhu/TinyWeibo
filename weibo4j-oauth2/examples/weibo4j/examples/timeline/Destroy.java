package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;

public class Destroy {

	public static void main(String[] args) {		
		String access_token = args[0];
		String id =  args[1];
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			Status status = tm.Destroy(id);
			Log.logInfo(status.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
