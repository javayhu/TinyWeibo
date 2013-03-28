package weibo4j.examples.comment;

import weibo4j.Comments;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Comment;
import weibo4j.model.CommentWapper;
import weibo4j.model.WeiboException;

public class GetCommentById {

	public static void main(String[] args) {
		String access_token = "2.00RQs9XC0gdCQY15dd6eda18QiojdE";
		String id = args[1];
		Comments cm =new Comments();
		cm.client.setToken(access_token);
		try {
			CommentWapper comment = cm.getCommentById(id);
			for(Comment c : comment.getComments()){
				Log.logInfo(c.toString());
			}
			System.out.println(comment.getTotalNumber());
			System.out.println(comment.getNextCursor());
			System.out.println(comment.getNextCursor());
			System.out.println(comment.getHasvisible());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}

}
