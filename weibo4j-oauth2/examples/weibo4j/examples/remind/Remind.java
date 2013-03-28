package weibo4j.examples.remind;

import weibo4j.Reminds;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class Remind {

	public static void main(String[] args) {
		String access_token = args[0];
		Reminds rm = new Reminds();
		rm.client.setToken(access_token);
		try {
			JSONObject jo = rm.getUnreadCountOfRemind();
			System.out.println(jo.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
	}

}
