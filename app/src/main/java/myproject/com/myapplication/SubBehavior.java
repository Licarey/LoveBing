package myproject.com.myapplication;

import android.animation.Animator;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by apple on 17-7-1.
 */

public class SubBehavior extends CoordinatorLayout.Behavior<View> {

    private View title;
    public SubBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //1.判断滑动的方向 我们需要垂直滑动
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {
        if(title == null){
            title = coordinatorLayout.findViewById(R.id.text_title);
        }
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    //2.根据滑动的距离显示和隐藏footer view
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed) {
        if(target.getScrollY() < 205){
            child.setTranslationY(-target.getScrollY());
            title.setTranslationY(-target.getScrollY());
        }
        Log.e("LM" , "----" + target.getScrollY());
    }
}
