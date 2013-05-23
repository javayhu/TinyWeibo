/**
 * TinyWeibo 微微博 
 * Copyright 2012 China ITElite Team
 * All Rights Reserved.
 * Created on 2012-12-20 23:53:33
 */
package cn.edu.csu.iteliter;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Friendships;
import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.edu.csu.iteliter.customview.TimelineAdapter;
import cn.edu.csu.iteliter.customview.TimelineListView;
import cn.edu.csu.iteliter.customview.TimelineListView.OnRefreshListener;
import cn.edu.csu.iteliter.model.UserData;
import cn.edu.csu.iteliter.model.WeiboImage;
import cn.edu.csu.iteliter.util.CacheUtil;
import cn.edu.csu.iteliter.util.ConstantUtil;
import cn.edu.csu.iteliter.util.ToastUtil;
import cn.edu.csu.iteliter.util.UserDataUtil;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * @filename MainWeibo.java
 * @package cn.edu.csu.iteliter
 * @project TinyWeibo 微微博
 * @description 主要的功能界面
 * @author 胡家威
 * @team China ITElite Team
 * @email yinger090807@qq.com
 * @updatetime 2012-12-21 下午3:41:59
 * @version 1.0
 * 
 */
public class MainWeibo extends Activity implements ConstantUtil {

	/** 该类的实例 */
	public static MainWeibo instance = null;

	/** 登陆的用户 */
	public static User user;

	/** 用户数据 */
	public static UserData userData;

	/** 图片异步加载的处理handler */
	public static Handler imageHandler;

	/** 刷新的声效 */
	private int sound_refresh = R.raw.sinaweibo_refresh;

	/** 播放声效 */
	public MediaPlayer musicPlayer;

	/** viewpager组件 */
	private ViewPager mTabPager;

	/** tab图片 */
	private ImageView mTabImg;//

	/** 四个选项卡的图片 */
	private ImageView mTab1, mTab2, mTab3, mTab4;

	/** 当前索引 */
	private int currIndex = 0;//

	/** 距离0 */
	private int zero = 0;//

	/** 距离1 */
	private int one;//

	/** 距离2 */
	private int two;

	/** 距离3 */
	private int three;

	/** 退出按钮 */
	private LinearLayout btn_menu_close;

	/** 退出的界面 */
	private View mainMenu;

	/** 退出界面是否正在显示 */
	private boolean menu_display = false;

	/** 退出界面 */
	private PopupWindow menuWindow;

	/** 布局渲染器 */
	public LayoutInflater inflater;

	/** 微博界面 */
	private View tabHome;

	/** 用户信息界面 */
	private View tabUserInfo;

	/** 摇一摇界面 */
	private View tabShake;

	/** 设置界面 */
	private View tabSettings;

	/*
	 * (non-Javadoc)创建界面的回调方法
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_weibo);
		instance = this;
		userData = UserDataUtil.readUserData(getApplicationContext());
		musicPlayer = MediaPlayer.create(getApplicationContext(), sound_refresh);
		musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mTab1 = (ImageView) findViewById(R.id.img_weixin);
		mTab2 = (ImageView) findViewById(R.id.img_address);
		mTab3 = (ImageView) findViewById(R.id.img_friends);
		mTab4 = (ImageView) findViewById(R.id.img_settings);
		mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		mTab4.setOnClickListener(new MyOnClickListener(3));
		int displayWidth = getWindowManager().getDefaultDisplay().getWidth();
		one = displayWidth / 4; //
		two = one * 2;
		three = one * 3;
		tabHome = inflater.inflate(R.layout.main_tab_home, null);
		tabUserInfo = inflater.inflate(R.layout.main_tab_userinfo, null);
		tabShake = inflater.inflate(R.layout.main_tab_shake, null);
		tabSettings = inflater.inflate(R.layout.main_tab_settings, null);
		final ArrayList<View> views = new ArrayList<View>();
		views.add(tabHome);
		views.add(tabUserInfo);
		views.add(tabShake);
		views.add(tabSettings);
		mTabPager = (ViewPager) findViewById(R.id.tabpager_mainweibo);
		mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
		PagerAdapter mPagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return views.size();
			}

			@Override
			public void destroyItem(View container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}

			@Override
			public Object instantiateItem(View container, int position) {
				((ViewPager) container).addView(views.get(position));
				return views.get(position);
			}
		};

		mTabPager.setAdapter(mPagerAdapter);
		imageHandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.what == MESSAGE_TYPE_WEIBOIMAGE) {
					WeiboImage weiboImage = (WeiboImage) message.obj;
					weiboImage.imageView.setImageBitmap(weiboImage.bitmap);
				}
			}
		};

		initTabHomeUI();
		initTabUserInfoUI();
		initTabShakeUI();
		initTabSettingsUI();
		loadTimeline(TIMELINE_FRIENDS);
	}

	/**
	 * The listener interface for receiving myOnClick events. The class that is interested in processing a myOnClick event implements this
	 * interface, and the object created with that class is registered with a component using the component's
	 * <code>addMyOnClickListener<code> method. When
	 * the myOnClick event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see MyOnClickEvent
	 */
	public class MyOnClickListener implements View.OnClickListener {

		/** 选中的索引 */
		private int index = 0;

		/**
		 * 实例化监听器
		 * 
		 * @param i
		 *            索引
		 */
		public MyOnClickListener(int i) {
			index = i;
		}

		/*
		 * (non-Javadoc)点击后的回调方法
		 * 
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View v) {
			mTabPager.setCurrentItem(index);
		}
	};

	/**
	 * The listener interface for receiving myOnPageChange events. The class that is interested in processing a myOnPageChange event
	 * implements this interface, and the object created with that class is registered with a component using the component's
	 * <code>addMyOnPageChangeListener<code> method. When
	 * the myOnPageChange event occurs, that object's appropriate
	 * method is invoked.
	 * 
	 * @see MyOnPageChangeEvent
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		/*
		 * (non-Javadoc)选中某个page的回调方法
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
		 */
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_pressed));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
				}
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_pressed));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_weixin_normal));
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_normal));
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(150);
			mTabImg.startAnimation(animation);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	/*
	 * (non-Javadoc)用户按钮手机按键时的回调方法
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (menu_display) {
				menuWindow.dismiss();
				menu_display = false;
			} else {
				Intent intent = new Intent();
				intent.setClass(MainWeibo.this, DialogExit.class);
				startActivity(intent);
			}
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (!menu_display) {
				mainMenu = inflater.inflate(R.layout.main_menu, null);
				menuWindow = new PopupWindow(mainMenu, android.view.ViewGroup.LayoutParams.FILL_PARENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
				menuWindow.showAtLocation(findViewById(R.id.rl_mainweibo), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				btn_menu_close = (LinearLayout) mainMenu.findViewById(R.id.btn_menu_close);

				btn_menu_close.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent();
						intent.setClass(MainWeibo.this, DialogExit.class);
						startActivity(intent);
						menuWindow.dismiss();
					}
				});
				menu_display = true;
			} else {
				menuWindow.dismiss();
				menu_display = false;
			}
			return false;
		}
		return false;
	}

	/*
	 * (non-Javadoc)当activity返回数据时的回调方法
	 * 
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_WEIBO_TYPE) {// request for which weibo type
			if (resultCode == RESULT_OK) {// result is ok
				loadTimeline(data.getIntExtra(TIMELINE_TYPE, TIMELINE_PUBLIC));
			}
		}
	}

	/*
	 * (non-Javadoc)界面销毁时的回调方法
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (musicPlayer.isPlaying()) {
			musicPlayer.stop();
		}
		musicPlayer.release();
	}

	// /////////////// tab home ///////////////////////

	/** 微博选项卡界面中的字段和方法 */

	/** 显示正在加载的界面 */
	private View rlHomeLoading;

	/** 显示公共微博的listview组件 */
	private TimelineListView lvPublicTimeline;

	/** 显示我的微博的listview的组件 */
	private TimelineListView lvUserTimeline;

	/** 显示提到我的微博的listview的组件 */
	private TimelineListView lvMentionTimeline;

	/** 显示好友的微博的listview的组件 */
	private TimelineListView lvFriendsTimeline;

	/** 公共微博列表 */
	private List<Status> listPublicTimeline;

	/** 我的微博列表 */
	private List<Status> listUserTimeline;

	/** 好友微博列表 */
	private List<Status> listFriendsTimeline;

	/** 提到我的微博列表 */
	private List<Status> listMentionsTimeline;

	/** 公共微博的数据adapter */
	private TimelineAdapter timelineAdapterPublic;

	/** 我的微博的数据adapter */
	private TimelineAdapter timelineAdapterUser;

	/** 好友微博的数据adapter */
	private TimelineAdapter timelineAdapterFriends;

	/** 提到我的微博的数据adapter */
	private TimelineAdapter timelineAdapterMentions;

	/** 公共微博的起始ID */
	private long sinceIdPublic = 2L;// default is 2,must > 1

	/** 我的微博的起始ID */
	private long sinceIdUser = 2L;

	/** 好友微博的起始ID */
	private long sinceIdFriends = 2L;

	/** 提到我的微博的起始ID */
	private long sinceIdMentions = 2L;

	/** 基于本应用，默认值是0 */
	private int baseAPP = 0;

	/** 微博的类型，默认值是0 */
	private int feature = 0;

	/** 默认加载的数量 */
	private int count = 20;

	/** 用户的头像 */
	private ImageView iv_home_head;

	/** 用户的昵称 */
	private TextView tv_home_name;

	/** 当前显示的微博列表的类型，默认是好友微博 */
	private int currentType = TIMELINE_FRIENDS;

	/** 处理微博界面的message的handler */
	private Handler tabhomehandler;

	/**
	 * 初始化微博界面的UI
	 */
	private void initTabHomeUI() {
		lvPublicTimeline = (TimelineListView) tabHome.findViewById(R.id.lvPublicTimeline);
		lvUserTimeline = (TimelineListView) tabHome.findViewById(R.id.lvUserTimeline);
		lvMentionTimeline = (TimelineListView) tabHome.findViewById(R.id.lvMentionTimeline);
		lvFriendsTimeline = (TimelineListView) tabHome.findViewById(R.id.lvFriendsTimeline);
		rlHomeLoading = tabHome.findViewById(R.id.rlHomeLoading);
		iv_home_head = (ImageView) tabHome.findViewById(R.id.iv_home_head);
		tv_home_name = (TextView) tabHome.findViewById(R.id.tv_home_name);

		listFriendsTimeline = new ArrayList<Status>();
		listPublicTimeline = new ArrayList<Status>();
		listUserTimeline = new ArrayList<Status>();
		listMentionsTimeline = new ArrayList<Status>();

		lvFriendsTimeline.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {// AsyncTask:call in UI thread
					protected Void doInBackground(Void... params) {
						doLoadTimeline(currentType);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						if (userData.isSoundPlay()) {
							musicPlayer.start();
						}
						timelineAdapterFriends.notifyDataSetChanged();
						lvFriendsTimeline.onRefreshComplete();
					}
				}.execute();
			}
		});

		lvPublicTimeline.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
					protected Void doInBackground(Void... params) {
						doLoadTimeline(currentType);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						if (userData.isSoundPlay()) {
							musicPlayer.start();
						}
						timelineAdapterPublic.notifyDataSetChanged();
						lvPublicTimeline.onRefreshComplete();
					}
				}.execute();
			}
		});

		lvUserTimeline.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
					protected Void doInBackground(Void... params) {
						doLoadTimeline(currentType);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						if (userData.isSoundPlay()) {
							musicPlayer.start();
						}
						timelineAdapterUser.notifyDataSetChanged();
						lvUserTimeline.onRefreshComplete();
					}
				}.execute();
			}
		});

		lvMentionTimeline.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
					protected Void doInBackground(Void... params) {
						doLoadTimeline(currentType);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						if (userData.isSoundPlay()) {
							musicPlayer.start();
						}
						timelineAdapterMentions.notifyDataSetChanged();
						lvMentionTimeline.onRefreshComplete();
					}
				}.execute();
			}
		});

		tabhomehandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.what == MESSAGE_TYPE_TIMELINE) {// after get timeline
					refreshTimeline(message);
				} else if (message.what == MESSAGE_TYPE_USERDATA) {
					user = (User) message.obj;//
					tv_home_name.setText(user.getScreenName());
					userData.setNickname(user.getScreenName());
					userData.setProfileimage(user.getProfileImageUrl());
					UserDataUtil.updateUserData(getApplicationContext(), userData);// update user info --->save
					WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, user.getProfileImageUrl(), imageHandler, iv_home_head,
							IMAGE_TYPE_PROFILE);
				}
			}
		};

		if (userData.getNickname() == null || userData.getNickname().equalsIgnoreCase("") || userData.getProfileimage() == null
				|| userData.getProfileimage().equalsIgnoreCase("")) {
			WeiboUtil.asyncLoadUserData(tabhomehandler);//
		} else {// do not validate user profile image, for user may change his profile image
			tv_home_name.setText(userData.getNickname());// nick name
			WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, userData.getProfileimage(), imageHandler, iv_home_head,
					IMAGE_TYPE_PROFILE);
		}

	}

	/**
	 * 隐藏所有的listview
	 */
	private void hideAllListVIew() {
		lvPublicTimeline.setVisibility(View.GONE);
		lvUserTimeline.setVisibility(View.GONE);
		lvMentionTimeline.setVisibility(View.GONE);
		lvFriendsTimeline.setVisibility(View.GONE);
		rlHomeLoading.setVisibility(View.VISIBLE);
	}

	/**
	 * 加载微博列表
	 * 
	 * @param type
	 *            微博列表的类型
	 */
	private void loadTimeline(final int type) {
		ListAdapter listAdapter = null;
		if (type == TIMELINE_PUBLIC) {
			listAdapter = lvPublicTimeline.getAdapter();
		} else if (type == TIMELINE_FRIENDS) {
			listAdapter = lvFriendsTimeline.getAdapter();
		} else if (type == TIMELINE_USER) {
			listAdapter = lvUserTimeline.getAdapter();
		} else if (type == TIMELINE_MENTIONS) {
			listAdapter = lvMentionTimeline.getAdapter();
		}
		if (listAdapter != null) {// change timeline
			showTimeline(type);// direct show the timeline
			return;
		}
		hideAllListVIew();
		new Thread(new Runnable() {
			public void run() {
				doLoadTimeline(type);
				Message message = new Message();
				message.what = MESSAGE_TYPE_TIMELINE;
				message.arg1 = type;
				tabhomehandler.sendMessage(message);
			}
		}).start();
	}

	/**
	 * 真正的执行微博列表的加载
	 * 
	 * @param type
	 *            微博列表的类型
	 */
	private void doLoadTimeline(final int type) {
		Timeline timeline = new Timeline();
		timeline.client.setToken(userData.getToken());
		StatusWapper statusWapper = null;
		Paging paging = new Paging();
		try {
			switch (type) {
			case TIMELINE_PUBLIC:
				paging.setSinceId(sinceIdPublic);
				statusWapper = timeline.getPublicTimeline(count, baseAPP);
				sinceIdPublic = Long.parseLong(statusWapper.getStatuses().get(0).getId());
				insertTimelineBefore(listPublicTimeline, statusWapper.getStatuses());
				break;
			case TIMELINE_FRIENDS:
				paging.setSinceId(sinceIdFriends);
				statusWapper = timeline.getFriendsTimeline(baseAPP, feature, paging);
				if (statusWapper.getStatuses().size() > 0) {
					sinceIdFriends = Long.parseLong(statusWapper.getStatuses().get(0).getId());
				}
				insertTimelineBefore(listFriendsTimeline, statusWapper.getStatuses());
				break;
			case TIMELINE_USER:
				paging.setSinceId(sinceIdUser);
				statusWapper = timeline.getUserTimelineByUid(userData.getUserid(), paging, baseAPP, feature);// userid
				if (statusWapper.getStatuses().size() > 0) {
					sinceIdUser = Long.parseLong(statusWapper.getStatuses().get(0).getId());
				}
				insertTimelineBefore(listUserTimeline, statusWapper.getStatuses());
				break;
			case TIMELINE_MENTIONS:
				paging.setSinceId(sinceIdMentions);
				statusWapper = timeline.getMentions(paging, 0, 0, 0);// mentions three filters
				if (statusWapper.getStatuses().size() > 0) {
					sinceIdMentions = Long.parseLong(statusWapper.getStatuses().get(0).getId());
				}
				insertTimelineBefore(listMentionsTimeline, statusWapper.getStatuses());
				break;
			default:
				paging.setSinceId(sinceIdPublic);
				statusWapper = timeline.getPublicTimeline(count, baseAPP);
				if (statusWapper.getStatuses().size() > 0) {
					sinceIdPublic = Long.parseLong(statusWapper.getStatuses().get(0).getId());
				}
				insertTimelineBefore(listPublicTimeline, statusWapper.getStatuses());
				break;
			}
		} catch (WeiboException e) {
		}
		if (statusWapper == null) {
			return;
		}
	}

	/**
	 * 在列表前插入另一个新的列表
	 * 
	 * @param listTimeline
	 *            原来的列表
	 * @param statuses
	 *            新加入的列表
	 */
	private void insertTimelineBefore(List<Status> listTimeline, List<Status> statuses) {
		listTimeline.addAll(0, statuses);
	}

	/**
	 * 刷新微博列表
	 * 
	 * @param msg
	 *            Message
	 */
	private void refreshTimeline(Message msg) {
		switch (msg.arg1) {
		case TIMELINE_PUBLIC:
			timelineAdapterPublic = new TimelineAdapter(listPublicTimeline);
			lvPublicTimeline.setAdapter(timelineAdapterPublic);
			break;
		case TIMELINE_FRIENDS:
			timelineAdapterFriends = new TimelineAdapter(listFriendsTimeline);
			lvFriendsTimeline.setAdapter(timelineAdapterFriends);
			break;
		case TIMELINE_USER:
			timelineAdapterUser = new TimelineAdapter(listUserTimeline);
			lvUserTimeline.setAdapter(timelineAdapterUser);
			break;
		case TIMELINE_MENTIONS:
			timelineAdapterMentions = new TimelineAdapter(listMentionsTimeline);
			lvMentionTimeline.setAdapter(timelineAdapterMentions);
			break;
		default:
			timelineAdapterPublic = new TimelineAdapter(listPublicTimeline);
			lvPublicTimeline.setAdapter(timelineAdapterPublic);
			break;
		}
		showTimeline(msg.arg1);// show
	}

	/**
	 * 显示微博列表
	 * 
	 * @param type
	 *            微博列表的类型
	 */
	private void showTimeline(int type) {
		if (userData.isSoundPlay()) {
			musicPlayer.start();
		}
		currentType = type;
		hideAllListVIew();
		rlHomeLoading.setVisibility(View.GONE);//
		switch (type) {
		case TIMELINE_PUBLIC:
			lvPublicTimeline.setVisibility(View.VISIBLE);
			break;
		case TIMELINE_FRIENDS:
			lvFriendsTimeline.setVisibility(View.VISIBLE);
			break;
		case TIMELINE_USER:
			lvUserTimeline.setVisibility(View.VISIBLE);
			break;
		case TIMELINE_MENTIONS:
			lvMentionTimeline.setVisibility(View.VISIBLE);
			break;
		default:
			lvPublicTimeline.setVisibility(View.VISIBLE);
			break;
		}
	}

	/**
	 * 显示选择微博列表的对话框
	 * 
	 * @param v
	 *            View组件
	 */
	public void btn_home_showdialog(View v) {
		Intent intent = new Intent(MainWeibo.this, HomeTopRightDialog.class);
		startActivityForResult(intent, REQUEST_WEIBO_TYPE);
	}

	/**
	 * 进入写微博界面
	 * 
	 * @param v
	 *            View组件
	 */
	public void btn_home_writeweibo(View v) {
		Intent intent = new Intent(MainWeibo.this, HomeWeiboWrite.class);
		intent.putExtra(WRITE_WEIBO_TYPE, WRITE_WEIBO_TYPE_WRITEWEIBO);
		startActivity(intent);
	}

	// /////////////// tab user info ///////////////
	/** 个人信息选项卡界面中的字段和方法 */

	/** 用户头像 */
	private ImageView iv_info_photo;

	/** 用户昵称 */
	private TextView tv_info_screenname;

	/** 用户性别 */
	private ImageView iv_info_gender;

	/** 用户所在地 */
	private TextView tv_info_location;

	/** 用户在线状态 */
	private TextView tv_info_online_status;

	/** 用户的个人简介 */
	private TextView tv_info_description;

	/** 博客地址 */
	private TextView tv_info_blogurl;

	/** 粉丝数 */
	private TextView tv_info_followers_count;

	/** 关注数 */
	private TextView tv_info_friends_count;

	/** 微博数 */
	private TextView tv_info_statuses_count;

	/** 收藏数 */
	private TextView tv_info_favourites_count;

	/** 信息加载界面 */
	private RelativeLayout rlInfoLoading;

	/** 用户信息组件 */
	private ScrollView sv_userinfo_info;

	/** 处理用户信息界面中的message的handler */
	private Handler tabinfohandler;

	/** 微博的URL的最大显示长度 */
	private final int BLOG_URL_MAX_LENGTH = 10;

	/**
	 * 初始化用户信息界面
	 */
	public void initTabUserInfoUI() {
		iv_info_photo = (ImageView) tabUserInfo.findViewById(R.id.iv_info_photo);
		tv_info_screenname = (TextView) tabUserInfo.findViewById(R.id.tv_info_screenname);
		iv_info_gender = (ImageView) tabUserInfo.findViewById(R.id.iv_info_gender);
		tv_info_location = (TextView) tabUserInfo.findViewById(R.id.tv_info_location);
		tv_info_online_status = (TextView) tabUserInfo.findViewById(R.id.tv_info_online_status);
		tv_info_blogurl = (TextView) tabUserInfo.findViewById(R.id.tv_info_blogurl);
		tv_info_description = (TextView) tabUserInfo.findViewById(R.id.tv_info_description);
		tv_info_followers_count = (TextView) tabUserInfo.findViewById(R.id.tv_info_followers_count);
		tv_info_friends_count = (TextView) tabUserInfo.findViewById(R.id.tv_info_friends_count);
		tv_info_statuses_count = (TextView) tabUserInfo.findViewById(R.id.tv_info_statuses_count);
		tv_info_favourites_count = (TextView) tabUserInfo.findViewById(R.id.tv_info_favourites_count);
		rlInfoLoading = (RelativeLayout) tabUserInfo.findViewById(R.id.rlInfoLoading);
		sv_userinfo_info = (ScrollView) tabUserInfo.findViewById(R.id.sv_userinfo_info);

		tabinfohandler = new Handler() {
			@Override
			public void handleMessage(Message message) {
				if (message.what == MESSAGE_TYPE_USERDATA) {
					user = (User) message.obj;//
					setUserInfo();
				}
			}
		};

		if (user == null) {
			rlInfoLoading.setVisibility(View.VISIBLE);
			sv_userinfo_info.setVisibility(View.GONE);
			WeiboUtil.asyncLoadUserData(tabinfohandler);
		} else {
			rlInfoLoading.setVisibility(View.GONE);
			sv_userinfo_info.setVisibility(View.VISIBLE);
			setUserInfo();
		}

	}

	/**
	 * 设置用户的信息到组件上
	 */
	private void setUserInfo() {
		rlInfoLoading.setVisibility(View.GONE);
		sv_userinfo_info.setVisibility(View.VISIBLE);
		tv_info_screenname.setText(user.getScreenName());
		tv_info_location.setText(user.getLocation());
		if (user.getOnlineStatus() == 0) {
			tv_info_online_status.setText("不在线");
		} else if (user.getOnlineStatus() == 1) {
			tv_info_online_status.setText("在线");
		}

		if (user.getUrl() == null || "".equalsIgnoreCase(user.getUrl())) {
			tv_info_blogurl.setText("暂无博客");
		} else {
			if (user.getUrl().length() > BLOG_URL_MAX_LENGTH) {
				tv_info_blogurl.setText(user.getUrl().substring(0, BLOG_URL_MAX_LENGTH) + "...");
			} else {
				tv_info_blogurl.setText(user.getUrl());
			}
		}

		if (user.getDescription() == null) {
			tv_info_description.setText("暂无简介");
		} else {
			tv_info_description.setText(user.getDescription());
		}

		tv_info_followers_count.setText(user.getFollowersCount() + "");
		tv_info_friends_count.setText(user.getFriendsCount() + "");
		tv_info_statuses_count.setText(user.getStatusesCount() + "");
		tv_info_favourites_count.setText(user.getFavouritesCount() + "");
		if (user.getGender().equalsIgnoreCase("f")) {// female
			iv_info_gender.setImageResource(R.drawable.userinfo_female);
		} else if (user.getGender().equalsIgnoreCase("m")) {
			iv_info_gender.setImageResource(R.drawable.userinfo_male);
		} else {// not know
			iv_info_gender.setVisibility(View.GONE);
		}
		WeiboUtil.restoreBitmap(CacheUtil.PROFILE_CACHE_PATH, user.getProfileImageUrl(), tabinfohandler, iv_info_photo, IMAGE_TYPE_PROFILE);
	}

	// ////////// tab shake wweibo ////////////

	/** 摇一摇选项卡界面中的字段和方法 */

	/** 好友微博类型的单选组 */
	private RadioGroup rg_shake_feature;

	/**
	 * 初始化摇一摇界面
	 */
	private void initTabShakeUI() {
		rg_shake_feature = (RadioGroup) tabShake.findViewById(R.id.rg_shake_feature);
	}

	/**
	 * 摇周边动态
	 * 
	 * @param v
	 *            View组件
	 */
	public void shake_nearbyweibo(View v) {
		Intent intent = new Intent(MainWeibo.this, ShakeWeibo.class);
		intent.putExtra(SHAKE_TYPE, SHAKE_TYPE_NEARBYWEIBO);
		startActivity(intent);
	}

	/**
	 * 摇附近好友
	 * 
	 * @param v
	 *            View组件
	 */
	public void shake_addfriend(View v) {
		Intent intent = new Intent(MainWeibo.this, ShakeWeibo.class);
		intent.putExtra(SHAKE_TYPE, SHAKE_TYPE_ADDFRIEND);
		startActivity(intent);
	}

	/**
	 * 摇好友微博
	 * 
	 * @param v
	 *            View组件
	 */
	public void shake_featureweibo(View v) {
		int type = WEIBO_TYPE_PICTURE;
		if (rg_shake_feature.getCheckedRadioButtonId() == R.id.rb_feature_music) {
			type = WEIBO_TYPE_MUSIC;
		} else if (rg_shake_feature.getCheckedRadioButtonId() == R.id.rb_feature_video) {
			type = WEIBO_TYPE_VIDEO;
		}
		Intent intent = new Intent(MainWeibo.this, ShakeWeibo.class);
		intent.putExtra(SHAKE_TYPE, SHAKE_TYPE_FEATUREWEIBO);
		intent.putExtra(WEIBO_TYPE, type);
		startActivity(intent);
	}

	// /////////////// tab setting /////////////////
	/** 设置选项卡界面中的字段和方法 */

	/** 是否开启声效的checkbox */
	private CheckBox cb_settings_sound;//

	/** 关注作者微博的组件 */
	private RelativeLayout rl_settings_followauthor;

	/** 是否关注作者微博的显示内容 */
	private TextView tv_settings_followauthor;

	/** 显示已经使用的空间大小的组件 */
	private TextView tv_settings_spaceused;

	/** 用户是否关注作者微博 */
	private boolean isFollowd = false;

	/** 已经使用的空间 */
	private double spaceUsed;

	/**
	 * 初始化设置界面
	 */
	public void initTabSettingsUI() {
		cb_settings_sound = (CheckBox) tabSettings.findViewById(R.id.cb_settings_sound);
		if (userData.isSoundPlay()) {
			cb_settings_sound.setChecked(true);
		} else {
			cb_settings_sound.setChecked(false);
		}
		rl_settings_followauthor = (RelativeLayout) tabSettings.findViewById(R.id.rl_settings_followauthor);
		tv_settings_followauthor = (TextView) tabSettings.findViewById(R.id.tv_settings_followauthor);
		tv_settings_spaceused = (TextView) tabSettings.findViewById(R.id.tv_settings_spaceused);
		rl_settings_followauthor.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!isFollowd) {// follow
					followAuthor();
				} else {// not follow
					unfollowAuthor();
				}
			}
		});
		cb_settings_sound.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				userData.setSoundPlay(isChecked);
				UserDataUtil.updateUserData(getApplicationContext(), userData);
			}
		});
		if (userData.isFollowauthor()) {
			tv_settings_followauthor.setText("已经关注了");
		} else {
			tv_settings_followauthor.setText("还未关注哟");
		}
		tv_settings_spaceused.setText("");
		asyncLoadSpaceUsed();
	}

	/**
	 * 异步加载使用的空间大小
	 */
	private void asyncLoadSpaceUsed() {
		new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
			protected Void doInBackground(Void... params) {
				spaceUsed = CacheUtil.calculateSpaceUsed();
				return null;
			}

			protected void onPostExecute(Void result) {
				tv_settings_spaceused.setText("已用空间 " + WeiboUtil.formatSpaceSize(spaceUsed));
			}
		}.execute();
	}

	/**
	 * 进入关于我们界面
	 * 
	 * @param v
	 *            View组件
	 */
	public void rl_settings_aboutus(View v) {
		Intent intent = new Intent(MainWeibo.this, SettingsAboutUs.class);
		startActivity(intent);
	}

	/**
	 * 清空缓存图片
	 * 
	 * @param v
	 *            View组件
	 */
	public void rl_settings_clearcache(View v) {
		ToastUtil.showShortToast(getApplicationContext(), "正在清空缓存图片，稍等哈...");
		new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
			protected Void doInBackground(Void... params) {
				CacheUtil.clearCache();
				return null;
			}

			protected void onPostExecute(Void result) {
				tv_settings_spaceused.setText("缓存已清空");
				ToastUtil.showShortToast(getApplicationContext(), "缓存图片已清空！");
			}
		}.execute();
	}

	/**
	 * 进入意见反馈界面
	 * 
	 * @param v
	 *            View组件
	 */
	public void rl_settings_suggestion(View v) {
		Intent intent = new Intent(MainWeibo.this, HomeWeiboWrite.class);
		intent.putExtra(WRITE_WEIBO_TYPE, WRITE_WEIBO_TYPE_SUGGESTION);
		startActivity(intent);
	}

	/**
	 * 退出登录
	 * 
	 * @param v
	 *            View组件
	 */
	public void btn_settings_exit(View v) {
		Intent intent = new Intent(MainWeibo.this, DialogSettingsExit.class);
		startActivity(intent);
	}

	/**
	 * 关注作者微博
	 */
	private void followAuthor() {
		ToastUtil.showShortToast(getApplicationContext(), "正在关注作者微博，稍等哈...");
		new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
			protected Void doInBackground(Void... params) {
				Friendships fm = new Friendships();
				fm.client.setToken(userData.getToken());
				try {
					fm.createFriendshipsById(ConstantUtil.AUTHOR_UID);
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				isFollowd = true;
				tv_settings_followauthor.setText("已经关注了");
				ToastUtil.showShortToast(getApplicationContext(), "成功关注作者微博！");
				userData.setFollowauthor(isFollowd);
				UserDataUtil.updateUserData(getApplicationContext(), userData);
			}
		}.execute();
	}

	/**
	 * 取消关注作者微博
	 */
	private void unfollowAuthor() {
		ToastUtil.showShortToast(getApplicationContext(), "正在取消关注作者微博，稍等哈...");
		new AsyncTask<Void, Void, Void>() {// AsyncTask:call from UI thread
			protected Void doInBackground(Void... params) {
				Friendships fm = new Friendships();
				fm.client.setToken(userData.getToken());
				try {
					fm.destroyFriendshipsDestroyById(ConstantUtil.AUTHOR_UID);
				} catch (WeiboException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				isFollowd = false;
				tv_settings_followauthor.setText("还未关注哟");
				ToastUtil.showShortToast(getApplicationContext(), "取消关注作者微博！");
				userData.setFollowauthor(isFollowd);
				UserDataUtil.updateUserData(getApplicationContext(), userData);
			}
		}.execute();
	}

}
