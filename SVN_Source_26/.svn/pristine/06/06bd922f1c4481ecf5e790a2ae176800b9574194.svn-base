package com.app.pao.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Raul on 2015/12/11.
 */
public class FrameLayoutFroScoll extends FrameLayout {

    public FrameLayoutFroScoll(Context context) {
        super(context);
    }

    public FrameLayoutFroScoll(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FrameLayoutFroScoll(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
