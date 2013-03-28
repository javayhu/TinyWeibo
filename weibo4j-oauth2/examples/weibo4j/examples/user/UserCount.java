package weibo4j.examples.user;

import weibo4j.Users;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;

public class UserCount {

	public static void main(String[] args) {
		String access_token = args[0];
		String uids =  args[1];
		Users um = new Users();
		um.client.setToken(access_token);
		try {
			JSONArray user = um.getUserCount(uids);
			Log.logInfo(user.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
