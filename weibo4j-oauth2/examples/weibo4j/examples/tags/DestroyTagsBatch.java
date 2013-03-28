package weibo4j.examples.tags;

import java.util.List;

import weibo4j.Tags;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Tag;
import weibo4j.model.WeiboException;

public class DestroyTagsBatch {

	public static void main(String[] args) {
		String access_token = args[0];
		Tags tm = new Tags();
		tm.client.setToken(access_token);
		String ids = args[1];
		List<Tag> tags = null;
		try {
			tags = tm.destroyTagsBatch(ids);
			for(Tag t : tags){
				Log.logInfo(t.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
