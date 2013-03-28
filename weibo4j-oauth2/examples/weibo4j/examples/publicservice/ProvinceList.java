package weibo4j.examples.publicservice;

import weibo4j.PublicService;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;

public class ProvinceList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String access_token = args[0];
		String country =args[1];
		PublicService ps = new PublicService();
		ps.client.setToken(access_token);
		try {
			JSONArray jo = ps.provinceList(country);
			System.out.println(jo.toString());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
		
	}

}
