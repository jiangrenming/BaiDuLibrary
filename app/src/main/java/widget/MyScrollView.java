package widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by jrm on 2017-5-2.
 */

public class MyScrollView extends ScrollView{

    /**
     * 屏幕高度
     */
    private int mScreenHeight;
    private boolean init = false;
    private float fator = 0.2f;
    private int factorHeight;
    private BaiduSearchView mView;
    private boolean upShow = true;

    public MyScrollView(Context context) {
        super(context);
        init(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenHeight = dm.heightPixels;
        factorHeight = (int) (mScreenHeight * fator);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!init) {

            RelativeLayout parentView = (RelativeLayout) getChildAt(0);
            //获得内部的两个子view
            mView = (BaiduSearchView) parentView.getChildAt(1);
           //并设定其高度为屏幕高度
           // mView.getLayoutParams().height = mScreenHeight;
            init = true;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            scrollTo(0, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                int scrollY = getScrollY();
                if (upShow) {
                    if (scrollY <= factorHeight) {
                        smoothScrollTo(0, 0);
                    } else {
                        smoothScrollTo(0, mScreenHeight);
                        upShow = false;

                    }
                } else {
                    int scrollpadding = mScreenHeight - scrollY;
                    if (scrollpadding >= factorHeight) {
                        this.smoothScrollTo(0, 0);
                        upShow = true;

                    } else {
                        this.smoothScrollTo(0, mScreenHeight);
                    }

                }

                return true;

        }
        return super.onTouchEvent(ev);
    }
}
