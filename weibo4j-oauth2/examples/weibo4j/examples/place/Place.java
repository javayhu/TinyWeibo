package weibo4j.examples.place;

import weibo4j.model.WeiboException;

public class Place {

	public static void main(String[] args) {
		weibo4j.Place wp = new weibo4j.Place();
		wp.client.setToken(args[1]);
		try {
			wp.poisUsersList(args[0]);
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
