package myproject.com.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by apple on 17-7-13.
 */

public class SearchActivity extends Activity {
    private RelativeLayout mSearchBGTxt;
    private TextView bottom;
    private FrameLayout mContentFrame;
    private View frame_content_bg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        mSearchBGTxt = (RelativeLayout) findViewById(R.id.tv_search_rlt);
        mContentFrame = (FrameLayout) findViewById(R.id.frame_content_bg);
        bottom = (TextView) findViewById(R.id.bottom);
        frame_content_bg = findViewById(R.id.frame_content_bg);

        mSearchBGTxt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mSearchBGTxt.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                performEnterAnimation();
            }
        });
    }

    private void performEnterAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGTxt.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];



        //放到前一个页面的位置
        mSearchBGTxt.setY(mSearchBGTxt.getY() + translateY);
        ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator alphaVa2 = ValueAnimator.ofFloat(1f, 0);
        alphaVa2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                bottom.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        alphaVa.setDuration(500);
        alphaVa2.setDuration(300);

        alphaVa.start();
        alphaVa2.start();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(-1000 , 150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                frame_content_bg.setTranslationY((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }

    @Override
    public void onBackPressed() {
        performExitAnimation();
    }

    private void performExitAnimation() {
        float originY = getIntent().getIntExtra("y", 0);

        int location[] = new int[2];
        mSearchBGTxt.getLocationOnScreen(location);

        final float translateY = originY - (float) location[1];


        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY()+translateY);
        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
            }
        });
        translateVa.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                finish();
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });
        ValueAnimator alphaVa2 = ValueAnimator.ofFloat(0, 1f);
        alphaVa2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                bottom.setAlpha((Float) valueAnimator.getAnimatedValue());
            }
        });

        alphaVa.setDuration(500);
        alphaVa2.setDuration(500);
        translateVa.setDuration(500);
        alphaVa.start();
        alphaVa2.start();
        translateVa.start();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(150 , -1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                frame_content_bg.setTranslationY((int) animation.getAnimatedValue());
            }
        });
        valueAnimator.setDuration(500);
        valueAnimator.start();
    }
}
