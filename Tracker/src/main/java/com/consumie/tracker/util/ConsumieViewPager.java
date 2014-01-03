package com.consumie.tracker.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Arango on 1/2/14.
 */
public class ConsumieViewPager extends ViewPager {
    private boolean enabled;

    public ConsumieViewPager(Context context) {
        super(context);
        this.enabled = false;
    }

    public ConsumieViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;
    }
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return this.enabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.enabled;
    }
}
