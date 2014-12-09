/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:32
 */
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
import cn.edu.csu.iteliter.ViewWeiboItem;
import cn.edu.csu.iteliter.model.WeiboItem;
import cn.edu.csu.iteliter.util.CacheUtil;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * @filename TimelineAdapter.java
 * @package cn.edu.csu.iteliter.customview
 * @project TinyWeibo 微微博
 * @description 微博列表的数据adapter
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午12:20:09
 * @version 1.0
 * 
 */
public class TimelineAdapter extends BaseAdapter {

	/** 微博列表 */
	private List<Status> listStatus;

	/**
	 * 构造器
	 * 
	 * @param listStatus
	 *            微博列表
	 */
	public TimelineAdapter(List<Status> listStatus) {
		this.listStatus = listStatus;
	}

	/*
	 * (non-Javadoc)得到总数
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return listStatus.size();
	}

	/*
	 * (non-Javadoc)返回指定位置的对象
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int arg0) {// no need to override
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int arg0) {// no need to override
		return 0;
	}

	/*
	 * (non-Javadoc)返回要显示的View控件
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View itemView, ViewGroup viewGroup) {// return a weibo item view
		WeiboItemHolder holder = null;
		if (itemView == null) {
			holder = new WeiboItemHolder();
			itemView = MainWeibo.instance.inflater.inflate(R.layout.item_weibo, null);
			holder.iv_weiboitem_head = (ImageView) itemView.findViewById(R.id.iv_weiboitem_head);
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

		holder.tv_weiboitem_name.setText(status.getUser().getName());
		holder.tv_weiboitem_time.setText(WeiboUtil.formatWeiboDate(status.getCreatedAt()));
		holder.tv_weiboitem_content.setText(status.getText());
		holder.tv_weiboitem_forwordnum.setText(status.getRepostsCount() + "");
		holder.tv_weiboitem_commentnum.setText(status.getCommentsCount() + "");
		holder.tv_weiboitem_from.setText(status.getSource().getName());

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
			if (sourceurl == null || sourceurl.equalsIgnoreCase("")) {
				holder.iv_weiboitem_statusImage.setVisibility(View.GONE);
			} else {
				holder.iv_weiboitem_statusImage.setVisibility(View.VISIBLE);
				WeiboUtil.restoreBitmap(CacheUtil.IMAGE_CACHE_PATH, sourceurl, MainWeibo.imageHandler,
						holder.iv_weiboitem_statusImage, ConstantUtil.IMAGE_TYPE_IMAGE);
			}

		} else {
			holder.iv_weiboitem_statusImage.setVisibility(View.GONE);
			holder.ll_weiboitem_source.setVisibility(View.VISIBLE);// must do this
			if (sourceStatus.getUser() != null && sourceStatus.getUser().getScreenName() != null) {
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
			if (sourceurl == null || sourceurl.equalsIgnoreCase("")) {
				holder.iv_weiboitem_sourceImage.setVisibility(View.GONE);
			} else {
				holder.iv_weiboitem_sourceImage.setVisibility(View.VISIBLE);
				WeiboUtil.restoreBitmap(CacheUtil.IMAGE_CACHE_PATH, sourceurl, MainWeibo.imageHandler,
						holder.iv_weiboitem_sourceImage, ConstantUtil.IMAGE_TYPE_IMAGE);
			}
		}
		WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, status.getUser().getProfileImageUrl(),
				MainWeibo.imageHandler, holder.iv_weiboitem_head, ConstantUtil.IMAGE_TYPE_PROFILE);

		// 评论微博
		holder.ll_weiboitem_comment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainWeibo.instance, HomeWeiboComment.class);
				intent.putExtra(ConstantUtil.STATUS_ID, status.getId());
				MainWeibo.instance.startActivity(intent);
			}
		});

		// 转发微博
		holder.ll_weiboitem_forward.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainWeibo.instance, HomeWeiboRepost.class);
				intent.putExtra(ConstantUtil.STATUS_ID, status.getId());
				MainWeibo.instance.startActivity(intent);
			}
		});

		// 查看微博
		itemView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MainWeibo.instance, ViewWeiboItem.class);
				intent.putExtra("status", new WeiboItem(status));
				MainWeibo.instance.startActivity(intent);
			}
		});

		return itemView;
	}
}

/**
 * 
 * @project TinyWeibo 微微博
 * @description 一条微博的显示内容控件封装类
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午12:23:34
 * @version 1.0
 * 
 */
class WeiboItemHolder {

	/** 用户的头像 */
	public ImageView iv_weiboitem_head;

	/** 用户的性别 */
	public ImageView iv_weiboitem_gender;

	/** 用户是否是认证用户 */
	public ImageView iv_weiboitem_verify;

	/** 用户的昵称 */
	public TextView tv_weiboitem_name;

	/** 微博发布时间 */
	public TextView tv_weiboitem_time;

	/** 微博的内容 */
	public TextView tv_weiboitem_content;

	/** 微博的评论数 */
	public TextView tv_weiboitem_commentnum;

	/** 微博的转发数 */
	public TextView tv_weiboitem_forwordnum;

	/** 微博信息的来源 */
	public TextView tv_weiboitem_from;

	/** 用户的所在地 */
	public TextView tv_weiboitem_location;

	/** 原始微博 */
	public LinearLayout ll_weiboitem_source;

	/** 原始微博的用户昵称 */
	public TextView tv_weiboitem_sourceName;

	/** 原始微博的内容 */
	public TextView tv_weiboitem_sourceContent;

	/** 原始微博的图片 */
	public ImageView iv_weiboitem_sourceImage;

	/** 微博的评论部分 */
	public LinearLayout ll_weiboitem_comment;

	/** 微博的转发部分 */
	public LinearLayout ll_weiboitem_forward;

	/** 微博的原始图片 */
	public ImageView iv_weiboitem_statusImage;

}