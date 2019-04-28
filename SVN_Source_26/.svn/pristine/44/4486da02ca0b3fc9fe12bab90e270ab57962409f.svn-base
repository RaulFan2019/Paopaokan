package com.app.pao.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public class DoubleRecycleFramelayout extends FrameLayout{

    private List<RecyclerView> mViews;

    public DoubleRecycleFramelayout(Context context) {
        super(context);
    }

    public DoubleRecycleFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DoubleRecycleFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmViews(List<RecyclerView> mViews) {
        this.mViews = mViews;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(mViews != null){
            for(int i = 0; i < mViews.size();i++){
                mViews.get(i).onTouchEvent(event);
            }
        }
        return true;
    }

}
