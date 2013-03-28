package weibo4j.examples.suggestions;

import weibo4j.Suggestion;
import weibo4j.model.WeiboException;

public class SuggestionsUsersMayInterested {

	public static void main(String[] args) {
		String access_token=args[0];
		Suggestion suggestion = new Suggestion();
		suggestion.client.setToken(access_token);
		try {
			suggestion.suggestionsUsersMayInterested();
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
