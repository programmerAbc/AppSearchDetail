package com.practice;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;


/**
 * Created by gaofeng on 2016-12-28.
 */

public class AppDetailLayout extends LinearLayout implements NestedScrollingParent {
    public static final String TAG = AppDetailLayout.class.getSimpleName();
    View topFrameLayout;
    ImageView appCoverIv;
    ImageView appLogo;
    ImageView closeBtn;
    TextView appNameTv;
    View brifBar;
    View tabBar;
    NestedScrollView nestedScrollView;
    int nestedScrollHeight = -1;
    int closeScrollDistance = -1;
    Scroller scroller;
    boolean finished = false;
    int maximumVelocity;

    public AppDetailLayout(Context context) {
        super(context);
    }

    public AppDetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppDetailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (nestedScrollHeight < 0) {
            nestedScrollHeight = topFrameLayout.getMeasuredHeight() - closeBtn.getMeasuredHeight();
        }
        if (closeScrollDistance < 0) {
            closeScrollDistance = getMeasuredHeight() - topFrameLayout.getMeasuredHeight();
        }
        LinearLayout.LayoutParams nestedScrollViewLayoutParams = (LinearLayout.LayoutParams) nestedScrollView.getLayoutParams();
        nestedScrollViewLayoutParams.height = getMeasuredHeight() - tabBar.getMeasuredHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        maximumVelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        topFrameLayout = findViewById(R.id.topFrameLayout);
        appCoverIv = (ImageView) findViewById(R.id.appCoverIv);
        appLogo = (ImageView) findViewById(R.id.appLogo);
        closeBtn = (ImageView) findViewById(R.id.closeBtn);
        closeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doFinish(2000);
            }
        });
        appNameTv = (TextView) findViewById(R.id.appNameTv);
        brifBar = findViewById(R.id.briefBar);
        tabBar = findViewById(R.id.tabBar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        scroller = new Scroller(getContext());
        scroller.startScroll(0, -getContext().getResources().getDisplayMetrics().heightPixels, 0, getContext().getResources().getDisplayMetrics().heightPixels, 900);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(0, scroller.getCurrY());
            invalidate();
        } else if (finished) {
            ((Activity) getContext()).finish();
            ((Activity) getContext()).overridePendingTransition(0, 0);
        }
    }


    @Override
    public void scrollTo(int x, int y) {
        //y = y < 0 ? 0 : y;
        y = y > nestedScrollHeight ? nestedScrollHeight : y;
        float scrollFactor = (float) y / nestedScrollHeight;
        closeBtn.setAlpha(Math.max(0, Math.min(scrollFactor * 3, 1)));
        closeBtn.setTranslationY(y);
        appCoverIv.setAlpha(Math.max(0, Math.min(1 - Math.abs(scrollFactor) * 3, 1)));
        if (scrollFactor < 0) {
            topFrameLayout.setBackgroundResource(android.R.color.transparent);
            appCoverIv.setScaleX(1 - scrollFactor * 3);
            appCoverIv.setScaleY(1 - scrollFactor * 3);
            appCoverIv.setTranslationY(y);
        } else {
            topFrameLayout.setBackgroundResource(android.R.color.white);
            appCoverIv.setTranslationY(0);
        }


        if (appCoverIv.getAlpha() == 0f) {
            closeBtn.setBackgroundResource(android.R.color.white);
        } else {
            closeBtn.setBackgroundResource(android.R.color.transparent);
        }
        super.scrollTo(x, y);
    }

    //NestedScrollingParent
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if (finished) {
            return false;
        }
        scroller.abortAnimation();
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if ((dy > 0 && getScrollY() < nestedScrollHeight) || dy < 0 && !ViewCompat.canScrollVertically(target, -1)) {
            consumed[1] = dy;
            scrollBy(0, dy);
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() > 0 && getScrollY() < nestedScrollHeight) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        return ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onStopNestedScroll(View child) {
        if (getScrollY() < -getContext().getResources().getDisplayMetrics().heightPixels / 2) {
            doFinish(300);
        } else if (getScrollY() < 0) {
            scroller.startScroll(0, getScrollY(), 0, -getScrollY(), 300);
            invalidate();
        }
    }

    public void doFinish(int animationDuration) {
        if (finished == false) {
            finished = true;
            scroller.startScroll(0, getScrollY(), 0, -getMeasuredHeight() - getScrollY(), animationDuration);
            invalidate();
        }
    }


    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }


}
