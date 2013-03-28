package weibo4j.examples.search;

import weibo4j.Search;
import weibo4j.model.WeiboException;

public class SearchSuggestionsAt_users {

	public static void main(String[] args) {
		String access_token=args[0];
		Search search = new Search();
		search.client.setToken(access_token);
		try {
			search.searchSuggestionsAt_users(args[1], 1);
		} catch (WeiboException e) {
			e.printStackTrace();
		}


	}

}
