package com.app.pao.ui.widget;

import android.content.Context;
import android.util.AttributeSet;

public class PaoVideoView extends VideoView {

    public static int WIDTH;
    public static int HEIGHT;


    public PaoVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(WIDTH, widthMeasureSpec);
        int height = getDefaultSize(HEIGHT, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}