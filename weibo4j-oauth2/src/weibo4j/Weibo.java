package weibo4j;

import weibo4j.http.HttpClient;

public class Weibo implements java.io.Serializable {

	private static final long serialVersionUID = 4282616848978535016L;

	public HttpClient client = new HttpClient();

	public  void setToken(String token) {
		client.setToken(token);
	}

}