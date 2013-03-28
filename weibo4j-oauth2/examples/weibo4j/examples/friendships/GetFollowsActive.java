package weibo4j.examples.friendships;

import weibo4j.Friendships;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

public class GetFollowsActive {

	public static void main(String[] args) {
		String access_token = "2.00RQs9XC0gdCQY15dd6eda18QiojdE";
		String uid = "2326766521";
		Friendships fm = new Friendships();
		fm.client.setToken(access_token);
		try {
			UserWapper users = fm.getFollowersActive(uid);
			for(User u : users.getUsers()){
				Log.logInfo(u.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
