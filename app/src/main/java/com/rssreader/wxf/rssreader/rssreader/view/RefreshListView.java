package com.rssreader.wxf.rssreader.rssreader.view;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rssreader.wxf.rssreader.R;

public class RefreshListView extends ListView {

    private static final String tag = "RefreshListView";
    private View mHeaderview;
    private float moveY;
    private float downY;
    private int measuredHeight;
    private  int state = 0;
    private  int PULL_RFRESH = 0;
    private  int RELEASE_REFRESH = 1;
    private  int REFRESHING = 2;
    private RotateAnimation mDownRotateAnimation;
    private RotateAnimation mUpRotateAnimation;
    private TextView tv_refresf_state;
    private ImageView iv_arrow;
    private ProgressBar pb_shape;
    private OnRefreshListener onRefreshListener;
    private TextView tv_refresf_date;
    private View mLoadMoreTilte;
    boolean isLoadOrRefresh;
    private int measuredLoadMoreHeight;
    public RefreshListView(Context context) {
        super(context);
        init();

    }



    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    private void init() {
        initSetTitle();
        initAnimation();
        initLoadMore();
        setOnScrollListener(new  OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_IDLE&&getLastVisiblePosition()>=getCount()-1){
                    mLoadMoreTilte.setPadding(0, 0, 0, 0);
                    setSelection(getCount());
                    onRefreshListener.LoadMore();
                    isLoadOrRefresh=true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }
    private void initLoadMore(){
        mLoadMoreTilte = View.inflate(getContext(),
                R.layout.loadmore_listview_title, null);
        mLoadMoreTilte.measure(0, 0);
        measuredLoadMoreHeight = mLoadMoreTilte.getMeasuredHeight();
        mLoadMoreTilte.setPadding(0, -measuredLoadMoreHeight, 0, 0);
        addFooterView(mLoadMoreTilte);
    }



    private void initSetTitle() {
        setTitle();
    }

    private void setTitle() {
        mHeaderview = View.inflate(getContext(),
                R.layout.refresh_listview_title, null);
        iv_arrow = (ImageView) mHeaderview.findViewById(R.id.iv_arrow);
        tv_refresf_state = (TextView) mHeaderview
                .findViewById(R.id.tv_refresf_state);
        tv_refresf_date = (TextView) mHeaderview.findViewById(R.id.tv_refresf_date);
        pb_shape = (ProgressBar) mHeaderview.findViewById(R.id.pb_shape);
        mHeaderview.measure(0, 0);
        measuredHeight = mHeaderview.getMeasuredHeight();
        mHeaderview.setPadding(0, -measuredHeight, 0, 0);
        addHeaderView(mHeaderview);
    }

    private void initAnimation() {

        mDownRotateAnimation = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mDownRotateAnimation.setDuration(300);
        mDownRotateAnimation.setFillAfter(true);

        mUpRotateAnimation = new RotateAnimation(180, 360,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mUpRotateAnimation.setDuration(300);
        mUpRotateAnimation.setFillAfter(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (state == REFRESHING) {
                    return super.onTouchEvent(ev);
                }
                moveY = ev.getY();
                Log.d("downY",downY+"");
                int moveDistance = (int) (moveY - 300);
                Log.i(tag, moveDistance + "");

                if (moveDistance > 0 && getFirstVisiblePosition() == 0) {
                    int titleHeight = -measuredHeight + moveDistance;

                    if (titleHeight < measuredHeight && state != PULL_RFRESH) {
                        iv_arrow.startAnimation(mUpRotateAnimation);
                        tv_refresf_state.setText("下拉刷新");
                        state = PULL_RFRESH;
                    } else if (titleHeight > measuredHeight
                            && state != RELEASE_REFRESH) {
                        iv_arrow.startAnimation(mDownRotateAnimation);
                        tv_refresf_state.setText("释放刷新");
                        state = RELEASE_REFRESH;
                    }
                    mHeaderview.setPadding(0, titleHeight, 0, 0);
                }

                break;
            case MotionEvent.ACTION_UP:
                float upY = ev.getY();

                if (state == RELEASE_REFRESH) {
                    iv_arrow.clearAnimation();
                    iv_arrow.setVisibility(View.INVISIBLE);
                    pb_shape.setVisibility(View.VISIBLE);
                    mHeaderview.setPadding(0, 0, 0, 0);
                    tv_refresf_state.setText("正在刷新...");
                    state = REFRESHING;
                    onRefreshListener.onRefresh();
                    isLoadOrRefresh=false;
                }else if(state==PULL_RFRESH){
                    mHeaderview.setPadding(0, -measuredHeight, 0, 0);
                }

                break;

        }

        return super.onTouchEvent(ev);
    }
    public interface OnRefreshListener{
        void onRefresh();
        void LoadMore();
    }
    ;	public void setRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener=onRefreshListener;
    }

    public void setOnRefresh() {
        if(isLoadOrRefresh){
            mLoadMoreTilte.setPadding(0, -measuredLoadMoreHeight, 0, 0);
        }else{
            iv_arrow.setVisibility(View.VISIBLE);
            pb_shape.setVisibility(View.INVISIBLE);
            tv_refresf_state.setText("下拉刷新");
            String time = getTime();
            tv_refresf_date.setText("最后一次刷新的时间为："+time);
            mHeaderview.setPadding(0, -measuredHeight, 0, 0);
            state=PULL_RFRESH;
        }
    }


    private String getTime() {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(currentTimeMillis);
    }

}