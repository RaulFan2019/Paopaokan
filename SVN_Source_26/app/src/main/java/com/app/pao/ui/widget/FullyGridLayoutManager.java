package com.app.pao.ui.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/1/20.
 * <p/>
 * 解决 由于RecycleView在ScrollView中导致高度错误
 */
public class FullyGridLayoutManager extends GridLayoutManager {
    public FullyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FullyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public FullyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    private int[] mMeasuredDimension = new int[2];

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        int widthMode = View.MeasureSpec.getMode(widthSpec);
        int heightMode = View.MeasureSpec.getMode(heightSpec);
        int widthSize = View.MeasureSpec.getSize(widthSpec);
        int heightSize = View.MeasureSpec.getSize(heightSpec);


        int width = 0;
        int height = 0;

        //判断item的数量是否为整除
        if (getItemCount() % getSpanCount() == 0) {
            for (int i = 0; i < getItemCount() / getSpanCount(); i++) {
                mMeasuredDimension = new int[2];
                onMeasureChildSpec(recycler, i, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);
                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
        } else {
            int tempHeight = 0;
            int tempWidth = 0;
            //去除未完全填满的一行
            int count = (getItemCount() - (getItemCount() % getSpanCount())) / getSpanCount();
            //去除最后一行后item高度和
            for (int i = 0; i < count; i++) {
                mMeasuredDimension = new int[2];
                onMeasureChildSpec(recycler, i, View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), mMeasuredDimension);
                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                    if (i == count - 1) {
                        tempWidth = mMeasuredDimension[0];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                    if (i == count - 1) {
                        tempHeight = mMeasuredDimension[1];
                    }
                }
            }

            if (getOrientation() == HORIZONTAL) {
                //加上最后一行的宽度
                width = width + tempWidth;
            } else {
                //加上最后一行的高度
                height = height + tempHeight;
            }
        }

        switch (widthMode) {
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        setMeasuredDimension(width, height);
    }

    private void onMeasureChildSpec(RecyclerView.Recycler recycler, int position, int heightSpec, int widthSpec, int[] mMeasuredDimension) {
        View childView = recycler.getViewForPosition(position);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) childView.getLayoutParams();

        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingStart() + getPaddingEnd(), lp.width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), lp.height);

        childView.measure(childWidthSpec, childHeightSpec);
        mMeasuredDimension[0] = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        mMeasuredDimension[1] = childView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

        recycler.recycleView(childView);
    }
}
