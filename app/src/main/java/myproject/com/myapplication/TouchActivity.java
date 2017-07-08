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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 17-7-5.
 */

public class TouchActivity extends Activity implements AppBarLayout.OnOffsetChangedListener, TouchFrameLayout.DragListener {
    private TouchFrameLayout touchFrameLayout;
    private AppBarLayout appBarLayout;
    private NestedScrollView nsv;
    private RecyclerView recyclerView;
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

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        initData();
        TouchActivity.RecyclerViewAdapter adapter=new TouchActivity.RecyclerViewAdapter(datas);
        recyclerView.setAdapter(adapter);
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

    public void initData(){
        datas = new ArrayList<String>();
        for(int i =0;i<17;i++){
            datas.add("item "+i);
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
        body.setScaleX(f1);
        body.setScaleY(f1);
        nsv.setAlpha(1 - percent);
        nsv.setTranslationY(body.getHeight() * (percent / 30));
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
        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;
        return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
                | (int) ((startR + (int) (fraction * (endR - startR))) << 16)
                | (int) ((startG + (int) (fraction * (endG - startG))) << 8)
                | (int) ((startB + (int) (fraction * (endB - startB))));
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<TouchActivity.RecyclerViewAdapter.ViewHolder> {

        private List<String> datas;

        public RecyclerViewAdapter(List<String> datas) {
            this.datas = datas;
        }

        @Override
        public TouchActivity.RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
            TouchActivity.RecyclerViewAdapter.ViewHolder vh = new TouchActivity.RecyclerViewAdapter.ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(TouchActivity.RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.item.setTag(position);
            holder.tv.setText(datas.get(position));
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            public View item;
            public TextView tv;

            public ViewHolder(View view) {
                super(view);
                item = view;
                tv = (TextView) view.findViewById(R.id.text);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

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
