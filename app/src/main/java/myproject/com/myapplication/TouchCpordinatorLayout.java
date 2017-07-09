package myproject.com.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by apple on 17-7-9.
 */

public class TouchCpordinatorLayout extends CoordinatorLayout {
    private Context context;
    private boolean isOpened = false;
    private float startY = 0;//按下时y值
    public TouchCpordinatorLayout(Context context) {
        super(context);
        init(context);
    }

    public TouchCpordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TouchCpordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isMove = false;
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                isMove = false;
                break;
            case MotionEvent.ACTION_MOVE:
                isMove = true;
                if (ev.getY() - startY >= 0 && isOpened) {
                    Log.e("LM" , "onTouchEvent--move-    " + (- (int) (ev.getY() - startY)));
                    scrollBy(0 , - (int) (ev.getY() - startY));
                }
                startY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.e("LM" , "onTouchEvent--xia-    " + ev.getY());
                if(ev.getY() > 20 && isMove){
                    if(isOpened){
                        ValueAnimator v = ValueAnimator.ofInt((int)ev.getY() , 1500);
                        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                scrollTo(0 , -(Integer) animation.getAnimatedValue());
                            }
                        });
                        v.setDuration(200);
                        v.setInterpolator(new AccelerateInterpolator());
                        v.start();
                        v.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                setVisibility(View.GONE);
                                scrollBy(0 , 1500);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
}
