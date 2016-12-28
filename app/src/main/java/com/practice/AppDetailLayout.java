package com.practice;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by gaofeng on 2016-12-28.
 */

public class AppDetailLayout extends LinearLayout {

    View topFrameLayout;
    ImageView appCoverIv;
    ImageView appLogo;
    View closeBar;
    TextView appNameTv;
    View brifBar;
    View tabBar;
    NestedScrollView nestedScrollView;
    int nestedScrollHeight;

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
        nestedScrollHeight=topFrameLayout.getMeasuredHeight()+appNameTv.getMeasuredHeight()+brifBar.getMeasuredHeight()-closeBar.getMeasuredHeight();
        LinearLayout.LayoutParams nestedScrollViewLayoutParams= (LinearLayout.LayoutParams) nestedScrollView.getLayoutParams();
        nestedScrollViewLayoutParams.height=getMeasuredHeight()-tabBar.getMeasuredHeight();
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        topFrameLayout=findViewById(R.id.topFrameLayout);
        appCoverIv= (ImageView) findViewById(R.id.appCoverIv);
        appLogo= (ImageView) findViewById(R.id.appLogo);
        closeBar=findViewById(R.id.closeBar);
        appNameTv= (TextView) findViewById(R.id.appNameTv);
        brifBar=findViewById(R.id.briefBar);
        tabBar=findViewById(R.id.tabBar);
        nestedScrollView= (NestedScrollView) findViewById(R.id.nestedScrollView);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void scrollTo(int x, int y) {
        y = y < 0 ? 0 : y;
        y = y > nestedScrollHeight ? nestedScrollHeight : y;
        float scrollFactor = (float) y / nestedScrollHeight;
        closeBar.setAlpha(scrollFactor);
        closeBar.setTranslationY(y);
        appCoverIv.setScaleX(1+scrollFactor);
        appCoverIv.setScaleY(1+scrollFactor);
        appCoverIv.setAlpha(1-scrollFactor*2);
        super.scrollTo(x, y);
    }
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if ((dy > 0 && getScrollY() < nestedScrollHeight) || dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1)) {
            consumed[1] = dy;
            scrollBy(0, dy);
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getScrollY() > 0 && getScrollY() < nestedScrollHeight) {
            return true;
        } else {
            return super.onNestedPreFling(target, velocityX, velocityY);
        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
    }
}
