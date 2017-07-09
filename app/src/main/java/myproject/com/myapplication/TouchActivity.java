package myproject.com.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by apple on 17-7-5.
 */

public class TouchActivity extends Activity implements AppBarLayout.OnOffsetChangedListener, TouchFrameLayout.DragListener {
    private TouchFrameLayout touchFrameLayout;
    private AppBarLayout appBarLayout;
    private NestedScrollView nsv;
    private View bottomview , topview;
    private View containerView , body;
    private List<String> datas;
    private boolean forbidAppBarScroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touchview);
        touchFrameLayout = (TouchFrameLayout) findViewById(R.id.dl);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        body = findViewById(R.id.body);
        nsv = (NestedScrollView) findViewById(R.id.nsv);
        containerView = findViewById(R.id.containerView);
        touchFrameLayout.setOrientation(TouchFrameLayout.Orientation.Vertical);

        bottomview = findViewById(R.id.bottom);
        topview = findViewById(R.id.topview);
        touchFrameLayout.setDragListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset == 0) {
            touchFrameLayout.setCanScroll(true);
        } else {
            touchFrameLayout.setCanScroll(false);
        }
    }

    @Override
    public void onOpen() {
        forbidAppBarScroll(true);
    }

    @Override
    public void onClose() {
        forbidAppBarScroll(false);
    }

    @Override
    public void onDrag(float percent) {
        float f1 = 1 - percent * 0.1f;
        appBarLayout.setScaleX(f1);
        topview.setScaleX(f1);
        body.setScaleY(f1);
        bottomview.setAlpha(1 - percent* 1.11f * 1.12f);
        bottomview.setTranslationY(body.getHeight() * (percent / 60));
        touchFrameLayout.getBackground().setColorFilter(
                evaluate(percent, Color.TRANSPARENT, Color.GRAY),
                PorterDuff.Mode.SRC_OVER);
    }

    private Integer evaluate(float fraction, Object startValue, Integer endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;
        int endInt = endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return ((startA + (int) (fraction * (endA - startA))) << 24)
                | ((startR + (int) (fraction * (endR - startR))) << 16)
                | ((startG + (int) (fraction * (endG - startG))) << 8)
                | ((startB + (int) (fraction * (endB - startB))));
    }

    private void forbidAppBarScroll(boolean forbid) {
        if (forbid == forbidAppBarScroll) {
            return;
        }
        if (forbid) {
            forbidAppBarScroll = true;
            if (ViewCompat.isLaidOut(appBarLayout)) {
                setAppBarDragCallback(new AppBarLayout.Behavior.DragCallback() {

                    @Override public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                        return false;
                    }
                });
            } else {
                appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        setAppBarDragCallback(new AppBarLayout.Behavior.DragCallback() {

                            @Override public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                                return false;
                            }
                        });
                    }
                });
            }
        } else {
            forbidAppBarScroll = false;
            if (ViewCompat.isLaidOut(appBarLayout)) {
                setAppBarDragCallback(null);
            } else {
                appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                        setAppBarDragCallback(null);
                    }
                });
            }
        }
    }

    private void setAppBarDragCallback(AppBarLayout.Behavior.DragCallback dragCallback) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.setDragCallback(dragCallback);
    }
}
