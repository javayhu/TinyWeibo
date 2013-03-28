package cn.edu.csu.iteliter.customview;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.edu.csu.iteliter.R;
import cn.edu.csu.iteliter.util.WeiboUtil;

/**
 * timeline listview
 * 
 * @author hjw
 * 
 */
public class TimelineListView extends ListView implements OnScrollListener {
	public interface OnRefreshListener {
		public void onRefresh();
	}

	private final static int DONE = 3;// complete refreshing
	private final static int LOADING = 4;
	private final static int PULL_To_REFRESH = 1;// pull down refresh
	private final static int RATIO = 3;// hjw: ratio? related to how long can be pulled!!!

	private final static int REFRESHING = 2;// refreshing
	private final static int RELEASE_To_REFRESH = 0;// release refresh
	private RotateAnimation animation;
	private ImageView arrowImageView;
	private int firstItemIndex;
	private int headContentHeight;
	private int headContentWidth;

	private LinearLayout headView;
	private LayoutInflater inflater;
	private boolean isBack;// isBack = true,means there is no need to refresh! head view can be back
	private boolean isRecored;// whether starty is already recorded
	private boolean isRefreshable;// whether the listview can be freshed or not
	private TextView lastUpdatedTextView;
	private ProgressBar progressBar;
	private OnRefreshListener refreshListener;
	private RotateAnimation reverseAnimation;
	private int startY;
	private int state;

	// private int paddingHeight;//hjw

	// int i = 1;

	private TextView tipsTextview;

	public TimelineListView(Context context) {
		super(context);
		init(context);
	}

	public TimelineListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	// change view by current state
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText("释放刷新微博");
			// Log.v("@@@@@@", "RELEASE_To_REFRESH 这是第  " + i++ + "步" + 12 + "请释放 刷新");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
				// tipsTextview.setText("isBack  is true ！！！");//hjw do not show this
			} else {
				// tipsTextview.setText("isBack  is false ！！！");//hjw do not show this
			}
			// Log.v("@@@@@@", "PULL_To_REFRESH 这是第  " + i++ + "步" + 13 + "  changeHeaderViewByState()");
			break;
		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新微博...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			// Log.v("@@@@@@", "REFRESHING 这是第  " + i++ + "步" + "正在加载中 ...REFRESHING");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);// hjw
			// headView.setPadding(0, -1 * paddingHeight, 0, 0);// hjw
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow_down);
			tipsTextview.setText("微博刷新完毕");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			// Log.v("@@@@@@", "DONE 这是第  " + i++ + "步" + "已经加载完毕- DONE ");
			break;
		}
	}

	private void init(Context context) {
		// setCacheColorHint(context.getResources().getColor(R.color.transparent));//hjw : xml do this
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.item_weibo_refresh, null);
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		// arrowImageView.setAdjustViewBounds(true);
		// arrowImageView.setMaxWidth(30);
		// arrowImageView.setMaxHeight(30);
		// arrowImageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// arrowImageView.setMinimumWidth(70);//hjw better not do this
		// arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		// paddingHeight = headContentHeight + 20;

		headView.setPadding(0, -1 * 1000, 0, 0);// hjw
		// headView.setPadding(0, -1 * paddingHeight, 0, 0);// hjw

		headView.invalidate();
		// System.out.println("width:" + headContentWidth + " height:" + headContentHeight + " paddingheight:"
		// + paddingHeight);
		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	@Override
	// hjw :no use
	public boolean isInEditMode() {
		return true;
	}

	// hjw: measure head view --> I do not understand!
	// for hide this!
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		System.out.println("height" + lpHeight);
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {// this is important ! here lpHight <0
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();// call the listview on refresh method ,to load new datas
			// Log.v("@@@@@@", "onRefresh被调用，这是第  " + i++ + "步");
		}
	}

	public void onRefreshComplete() {
		state = DONE;
		// lastUpdatedTextView.setText("加载完成: " + new Date().toLocaleString());//hjw
		lastUpdatedTextView.setText("刷新完成: " + WeiboUtil.formatWeiboDate(new Date()));
		changeHeaderViewByState();
		// Log.v("@@@@@@", "onRefreshComplete() 被调用。。。");
	}

	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2, int arg3) {
		firstItemIndex = firstVisiableItem;
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if ((firstItemIndex == 0) && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					// Log.v("@@@@@@", "ACTION_DOWN 这是第  " + i++ + "步" + 1);
				}
				break;
			case MotionEvent.ACTION_UP:
				if ((state != REFRESHING) && (state != LOADING)) {
					if (state == DONE) {
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						// Log.v("@@@@@@", "ACTION_UP PULL_To_REFRESH and changeHeaderViewByState()" + " 这是第  " + i++
						// + "步前" + 2);
						changeHeaderViewByState();
						// Log.v("@@@@@@", "ACTION_UP PULL_To_REFRESH and changeHeaderViewByState() " + "这是第  " + i++
						// + "步后" + 2);
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						// Log.v("@@@@@@", "ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState() " + "这是第  " + i++ +
						// "步"
						// + 3);
						changeHeaderViewByState();
						onRefresh();
						// Log.v("@@@@@@", "ACTION_UP RELEASE_To_REFRESH changeHeaderViewByState()" + " 这是第  " + i++ +
						// "步"
						// + 3);
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && (firstItemIndex == 0)) {
					isRecored = true;
					startY = tempY;
					// Log.v("@@@@@@", "ACTION_MOVE 这是第  " + i++ + "步" + 4);
				}
				if ((state != REFRESHING) && isRecored && (state != LOADING)) {
					if (state == RELEASE_To_REFRESH) {
						setSelection(0);
						if ((((tempY - startY) / RATIO) < headContentHeight) && ((tempY - startY) > 0)) {// hjw
							// if (((tempY - startY) / RATIO < paddingHeight) && (tempY - startY) > 0) {// hjw
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
							// Log.v("@@@@@@", "changeHeaderViewByState() 这是第  " + i++ + "步" + 5);
						} else if ((tempY - startY) <= 0) {
							state = DONE;
							changeHeaderViewByState();
							// Log.v("@@@@@@", "ACTION_MOVE RELEASE_To_REFRESH 2  changeHeaderViewByState " + "这是第  "
							// + i++ + "步" + 6);
						}
					}
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO) >= headContentHeight) {// hjw
							// if ((tempY - startY) / RATIO >= paddingHeight) {// hjw
							state = RELEASE_To_REFRESH;
							isBack = true;
							// Log.v("@@@@@@", "changeHeaderViewByState " + "这是第  " + i++ + "步前" + 7);
							changeHeaderViewByState();
							// Log.v("@@@@@@", "changeHeaderViewByState " + "这是第  " + i++ + "步后" + 7);
						} else if ((tempY - startY) <= 0) {
							state = DONE;
							changeHeaderViewByState();
							// Log.v("@@@@@@", "ACTION_MOVE changeHeaderViewByState PULL_To_REFRESH 2" + " 这是第  " + i++
							// + "步" + 8);
						}
					}
					if (state == DONE) {
						if ((tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							// Log.v("@@@@@@", "ACTION_MOVE DONE changeHeaderViewByState " + "这是第  " + i++ + "步前" + 9);
							changeHeaderViewByState();
							// Log.v("@@@@@@", "ACTION_MOVE DONE changeHeaderViewByState " + "这是第  " + i++ + "步后" + 9);
						}
					}
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, (-1 * headContentHeight) + ((tempY - startY) / RATIO), 0, 0);// hjw
						// headView.setPadding(0, -1 * paddingHeight + (tempY - startY) / RATIO, 0, 0);

						// Log.v("@@@@@@", -1 * headContentHeight + (tempY - startY) / RATIO
						// + "ACTION_MOVE PULL_To_REFRESH 3  这是第  " + i++ + "步" + 10);
					}
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, ((tempY - startY) / RATIO) - headContentHeight, 0, 0);// hjw
						// headView.setPadding(0, (tempY - startY) / RATIO - paddingHeight, 0, 0);// hjw

						// Log.v("@@@@@@", "ACTION_MOVE PULL_To_REFRESH 4 这是第  " + i++ + "步" + 11);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	public void setAdapter(BaseAdapter adapter) {
		// lastUpdatedTextView.setText("this is in MyListView:" + new Date().toLocaleString());//hjw
		lastUpdatedTextView.setText("上次刷新:" + WeiboUtil.formatWeiboDate(new Date()));
		super.setAdapter(adapter);
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}
}