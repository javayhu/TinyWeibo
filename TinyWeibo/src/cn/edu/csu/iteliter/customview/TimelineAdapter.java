package cn.edu.csu.iteliter.customview;

import java.util.List;

import weibo4j.model.Status;
import weibo4j.model.User;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.edu.csu.iteliter.HomeWeiboComment;
import cn.edu.csu.iteliter.HomeWeiboRepost;
import cn.edu.csu.iteliter.MainWeibo;
import cn.edu.csu.iteliter.R;
import cn.edu.csu.iteliter.ResultWeiboItem;
import cn.edu.csu.iteliter.model.WeiboItem;
import cn.edu.csu.iteliter.util.CacheUtil;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * timeline adapter for specific use
 * 
 * @author hjw
 */
public class TimelineAdapter extends BaseAdapter {

	private List<Status> listStatus;

	public TimelineAdapter(List<Status> listStatus) {
		this.listStatus = listStatus;
	}

	@Override
	public int getCount() {
		return listStatus.size();
	}

	@Override
	public Object getItem(int arg0) {// no need to override
		return null;
	}

	@Override
	public long getItemId(int arg0) {// no need to override
		return 0;
	}

	@Override
	public View getView(final int position, View itemView, ViewGroup viewGroup) {// return a weibo item view
		WeiboItemHolder holder = null;

		if (itemView == null) {
			holder = new WeiboItemHolder();
			itemView = MainWeibo.instance.inflater.inflate(R.layout.item_weibo, null);
			holder.iv_weiboitem_head = (ImageView) itemView.findViewById(R.id.iv_weiboitem_head);
			// holder.iv_weiboitem_crown = (ImageView) itemView.findViewById(R.id.iv_weiboitem_crown);
			holder.iv_weiboitem_gender = (ImageView) itemView.findViewById(R.id.iv_weiboitem_gender);
			holder.iv_weiboitem_verify = (ImageView) itemView.findViewById(R.id.iv_weiboitem_verify);
			holder.iv_weiboitem_sourceImage = (ImageView) itemView.findViewById(R.id.iv_weiboitem_sourceImage);
			holder.iv_weiboitem_statusImage = (ImageView) itemView.findViewById(R.id.iv_weiboitem_statusImage);
			holder.tv_weiboitem_name = (TextView) itemView.findViewById(R.id.tv_weiboitem_name);
			holder.tv_weiboitem_time = (TextView) itemView.findViewById(R.id.tv_weiboitem_time);
			holder.tv_weiboitem_content = (TextView) itemView.findViewById(R.id.tv_weiboitem_content);
			holder.tv_weiboitem_forwordnum = (TextView) itemView.findViewById(R.id.tv_weiboitem_forwordnum);
			holder.tv_weiboitem_commentnum = (TextView) itemView.findViewById(R.id.tv_weiboitem_commentnum);
			holder.tv_weiboitem_from = (TextView) itemView.findViewById(R.id.tv_weiboitem_from);//
			holder.tv_weiboitem_location = (TextView) itemView.findViewById(R.id.tv_weiboitem_location);//

			holder.ll_weiboitem_source = (LinearLayout) itemView.findViewById(R.id.ll_weiboitem_source);
			holder.tv_weiboitem_sourceName = (TextView) itemView.findViewById(R.id.tv_weiboitem_sourceName);
			holder.tv_weiboitem_sourceContent = (TextView) itemView.findViewById(R.id.tv_weiboitem_sourceContent);

			holder.ll_weiboitem_comment = (LinearLayout) itemView.findViewById(R.id.ll_weiboitem_comment);
			holder.ll_weiboitem_forward = (LinearLayout) itemView.findViewById(R.id.ll_weiboitem_forward);

			itemView.setTag(holder);
		} else {
			holder = (WeiboItemHolder) itemView.getTag();// tag is used to cache the specific view
		}
		final Status status = listStatus.get(position);

		// System.out.println(status);

		holder.tv_weiboitem_name.setText(status.getUser().getName());
		holder.tv_weiboitem_time.setText(WeiboUtil.formatWeiboDate(status.getCreatedAt()));
		holder.tv_weiboitem_content.setText(status.getText());
		holder.tv_weiboitem_forwordnum.setText(status.getRepostsCount() + "");
		holder.tv_weiboitem_commentnum.setText(status.getCommentsCount() + "");
		holder.tv_weiboitem_from.setText(status.getSource().getName());
		// holder.tv_weiboitem_source.setText(status.getSource().);//

		User user = status.getUser();
		holder.tv_weiboitem_location.setText(user.getLocation());
		// gender
		if (user.getGender().equalsIgnoreCase("f")) {// female
			holder.tv_weiboitem_location.setVisibility(View.VISIBLE);
			holder.iv_weiboitem_gender.setImageResource(R.drawable.user_info_female);
		} else if (user.getGender().equalsIgnoreCase("m")) {
			holder.tv_weiboitem_location.setVisibility(View.VISIBLE);
			holder.iv_weiboitem_gender.setImageResource(R.drawable.user_info_male);
		} else {// not know
			holder.iv_weiboitem_gender.setVisibility(View.GONE);
		}
		// verify
		if (!user.isVerified()) {
			holder.iv_weiboitem_verify.setVisibility(View.GONE);
		} else {
			holder.iv_weiboitem_verify.setVisibility(View.VISIBLE);
		}

		Status sourceStatus = status.getRetweetedStatus();
		if (sourceStatus == null) {// no source status --> original status
			holder.ll_weiboitem_source.setVisibility(View.GONE);// no source -- check original image
			String sourceurl = null;
			if (status.getThumbnailPic() != null) {
				sourceurl = status.getThumbnailPic();
			} else if (status.getBmiddlePic() != null) {
				sourceurl = status.getBmiddlePic();
			} else if (status.getOriginalPic() != null) {
				sourceurl = status.getOriginalPic();
			}
			if ((sourceurl == null) || sourceurl.equalsIgnoreCase("")) {
				holder.iv_weiboitem_statusImage.setVisibility(View.GONE);
			} else {
				holder.iv_weiboitem_statusImage.setVisibility(View.VISIBLE);
				WeiboUtil.restoreBitmap(CacheUtil.IMAGE_CACHE_PATH, sourceurl, MainWeibo.imageHandler,
						holder.iv_weiboitem_statusImage, ConstantUtil.IMAGE_TYPE_IMAGE);
			}

		} else {
			holder.iv_weiboitem_statusImage.setVisibility(View.GONE);
			holder.ll_weiboitem_source.setVisibility(View.VISIBLE);// must do this
			// holder.tv_weiboitem_sourceName.setText("@" + sourceStatus.getUser().getScreenName());// @ username
			if ((sourceStatus.getUser() != null) && (sourceStatus.getUser().getScreenName() != null)) {
				holder.tv_weiboitem_sourceName.setText("@" + sourceStatus.getUser().getScreenName());// @ username
			} else {// weibo may be deleted by author
				System.out.println(status.getText());
				holder.tv_weiboitem_sourceName.setText("@source name");// @ username
			}
			holder.tv_weiboitem_sourceContent.setText(sourceStatus.getText());

			String sourceurl = null;
			if (sourceStatus.getThumbnailPic() != null) {
				sourceurl = sourceStatus.getThumbnailPic();
			} else if (sourceStatus.getBmiddlePic() != null) {
				sourceurl = sourceStatus.getBmiddlePic();
			} else if (sourceStatus.getOriginalPic() != null) {
				sourceurl = sourceStatus.getOriginalPic();
			}
			if ((sourceurl == null) || sourceurl.equalsIgnoreCase("")) {
				holder.iv_weiboitem_sourceImage.setVisibility(View.GONE);
			} else {
				holder.iv_weiboitem_sourceImage.setVisibility(View.VISIBLE);
				WeiboUtil.restoreBitmap(CacheUtil.IMAGE_CACHE_PATH, sourceurl, MainWeibo.imageHandler,
						holder.iv_weiboitem_sourceImage, ConstantUtil.IMAGE_TYPE_IMAGE);
			}
		}
		// System.out.println(sourceStatus);
		WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, status.getUser().getProfileImageUrl(),
				MainWeibo.imageHandler, holder.iv_weiboitem_head, ConstantUtil.IMAGE_TYPE_PROFILE);

		holder.ll_weiboitem_comment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ToastUtil.showShortToast(getApplicationContext(), "comment id = " + status.getIdstr());
				Intent intent = new Intent();
				intent.setClass(MainWeibo.instance, HomeWeiboComment.class);
				intent.putExtra(ConstantUtil.STATUS_ID, status.getId());
				// Bundle bundle = new Bundle();
				// bundle.putSerializable("status", status);
				// intent.putExtras(bundle);
				MainWeibo.instance.startActivity(intent);
			}
		});

		holder.ll_weiboitem_forward.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// ToastUtil.showShortToast(getApplicationContext(), "forward id = " + status.getIdstr());
				Intent intent = new Intent();
				intent.setClass(MainWeibo.instance, HomeWeiboRepost.class);
				intent.putExtra(ConstantUtil.STATUS_ID, status.getId());
				// Bundle bundle = new Bundle();
				// bundle.putSerializable("status", status);
				// intent.putExtras(bundle);
				MainWeibo.instance.startActivity(intent);
			}
		});

		// cancle click
		itemView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainWeibo.instance, ResultWeiboItem.class);
				intent.putExtra("status", new WeiboItem(status));
				MainWeibo.instance.startActivity(intent);
			}
		});

		return itemView;
	}
}

/**
 * view holder for weibo item
 */
class WeiboItemHolder {
	public ImageView iv_weiboitem_gender;
	public ImageView iv_weiboitem_head;
	public ImageView iv_weiboitem_sourceImage;
	public ImageView iv_weiboitem_statusImage;
	// public ImageView iv_weiboitem_crown;//
	public ImageView iv_weiboitem_verify;
	public LinearLayout ll_weiboitem_comment;
	public LinearLayout ll_weiboitem_forward;
	public LinearLayout ll_weiboitem_source;
	public TextView tv_weiboitem_commentnum;
	public TextView tv_weiboitem_content;
	public TextView tv_weiboitem_forwordnum;
	public TextView tv_weiboitem_from;//
	public TextView tv_weiboitem_location;//
	public TextView tv_weiboitem_name;
	public TextView tv_weiboitem_sourceContent;
	public TextView tv_weiboitem_sourceName;
	public TextView tv_weiboitem_time;
}