package weibo4j.examples.tags;

import java.util.List;

import weibo4j.Tags;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Tag;
import weibo4j.model.WeiboException;

public class GetTags {

	public static void main(String[] args) {
		String access_token =args[0];
		Tags tm = new Tags();
		tm.client.setToken(access_token);
		List<Tag> tags = null;
		String uid =args[1];
		try {
			tags = tm.getTags(uid);
			for(Tag tag:tags ){				
				Log.logInfo(tag.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
