package myproject.com.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by apple on 17-7-5.
 */

public class TouchActivity extends Activity implements AppBarLayout.OnOffsetChangedListener, TouchFrameLayout.DragListener {
    private TouchFrameLayout touchFrameLayout;
    private AppBarLayout appBarLayout;
    private View bottomview , topview , root_view;
    private TouchCpordinatorLayout body;
    private boolean forbidAppBarScroll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touchview);
        touchFrameLayout = (TouchFrameLayout) findViewById(R.id.dl);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        root_view = findViewById(R.id.root_view);
        body = (TouchCpordinatorLayout) findViewById(R.id.body);
        touchFrameLayout.setOrientation(TouchFrameLayout.Orientation.Vertical);

        bottomview = findViewById(R.id.bottom);
        topview = findViewById(R.id.topview);
        touchFrameLayout.setDragListener(this);
        root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOpenStatus();
            }
        });
    }

    public void setOpenStatus(){
        body.setVisibility(View.VISIBLE);
        touchFrameLayout.setOpenStatus();
        onDrag(1.0f);
        onOpen();
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
        body.setOpened(true);
    }

    @Override
    public void onClose() {
        forbidAppBarScroll(false);
        body.setOpened(false);
    }

    @Override
    public void onDrag(float percent) {
        float f1 = 1 - percent * 0.1f;
        appBarLayout.setScaleX(f1);
        topview.setScaleX(f1);
        body.setScaleY(f1);
        bottomview.setAlpha(1 - percent* 1.11f * 1.12f);
        bottomview.setTranslationY(body.getHeight() * (percent / 60));
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
