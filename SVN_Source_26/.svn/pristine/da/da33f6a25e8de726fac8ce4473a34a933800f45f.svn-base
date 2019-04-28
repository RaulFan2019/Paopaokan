package com.app.pao.ui.widget;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.Log;

/**
 * Created by Raul.Fan on 2016/3/2.
 */
public class ScrollViewHasMapview extends ScrollView {


    private static final String TAG = "ScrollViewHasMapview";
    private View mapView;
    private View leftButton;
    private View rightButton;
    private View nullView;
    private boolean onScroll = false;
    private boolean onLeftBtn = false;
    private boolean onRightBtn = false;
    private boolean onMap = false;

    private static final int BtnBottom = (int) DeviceUtils.dpToPixel(320f);
    private static final int BtnTop = (int) DeviceUtils.dpToPixel(270f);
    private static final int MapBottom = (int) DeviceUtils.dpToPixel(300);
    private static final int MapTop = (int) DeviceUtils.dpToPixel(60);


    public ScrollViewHasMapview(Context context) {
        super(context);
    }

    public ScrollViewHasMapview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewHasMapview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMapView(View mapView) {
        this.mapView = mapView;
    }

    public void setRightButton(View rightButton) {
        this.rightButton = rightButton;
    }

    public void setLeftButton(View leftButton) {
        this.leftButton = leftButton;
    }

    public void setNullView(View nullView) {
        this.nullView = nullView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.v(TAG,"onLeftBtn:" + onLeftBtn);
//        Log.v(TAG,"BtnBottom:" + BtnBottom);
//        Log.v(TAG,"BtnTop:" + BtnTop);
//        Log.v(TAG,"leftButton.getLeft():" + leftButton.getLeft());
//        Log.v(TAG,"leftButton.getRight():" + leftButton.getRight());
//        Log.v(TAG,"ev.getX():" + ev.getX());
//        Log.v(TAG, "ev.getY():" + ev.getY());
//        Log.v(TAG, "MapBottom:" + MapBottom);
//        Log.v(TAG, "MapTop:" + MapTop);
//        Log.v(TAG, "onMap" + onMap);

        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (nullView != null && ev.getY() > (nullView.getBottom() - getScrollY())) {
                    onScroll = true;
                    onLeftBtn = false;
                    onRightBtn = false;
                    onMap = false;
                    super.dispatchTouchEvent(ev);
                    return true;
                }
                if (leftButton != null && ev.getY() < BtnBottom
                        && ev.getY() > BtnTop
                        && ev.getX() > leftButton.getLeft()
                        && ev.getX() < leftButton.getRight()) {
                    onScroll = false;
                    onLeftBtn = true;
                    onRightBtn = false;
                    onMap = false;
                    leftButton.onTouchEvent(ev);
                    return true;
                }
                if (rightButton != null
                        && ev.getY() < BtnBottom
                        && ev.getY() > BtnTop
                        && ev.getX() > rightButton.getLeft()
                        && ev.getX() < rightButton.getRight()) {
                    onScroll = false;
                    onLeftBtn = false;
                    onRightBtn = true;
                    onMap = false;
                    rightButton.onTouchEvent(ev);
                    return true;
                }
                if (mapView != null
                        && ev.getY() < MapBottom
                        && ev.getY() > MapTop) {
                    onScroll = false;
                    onLeftBtn = false;
                    onRightBtn = false;
                    onMap = true;
                    mapView.dispatchTouchEvent(ev);
                    return true;
                }
                onScroll = true;
                //抬起
            } else {
                if (onScroll) {
                    super.dispatchTouchEvent(ev);
                    if (ev.getAction() == MotionEvent.ACTION_UP) {
                        onScroll = false;
                    }
                    return true;
                }
                if (leftButton != null && onLeftBtn) {
                    if (ev.getAction() == MotionEvent.ACTION_UP) {
                        onLeftBtn = false;
                        leftButton.performClick();
                    }
                    return true;
                }
                if (rightButton != null && onRightBtn) {
                    if (ev.getAction() == MotionEvent.ACTION_UP) {
                        onRightBtn = false;
                        rightButton.performClick();
                    }
                    return true;
                }

                if (mapView != null && onMap) {
                    mapView.dispatchTouchEvent(ev);
                    if (ev.getAction() == MotionEvent.ACTION_UP) {
                        onMap = false;
                    }
                    return true;
                }
//                if (!onScroll) {
//                    if (leftButton != null && ev.getY() < BtnBottom
//                            && ev.getY() > BtnTop
//                            && ev.getX() > leftButton.getLeft()
//                            && ev.getX() < leftButton.getRight()) {
//                        leftButton.dispatchTouchEvent(ev);
//                        return true;
//                    }
//                    if (rightButton != null && ev.getY() < BtnBottom
//                            && ev.getY() > BtnTop
//                            && ev.getX() > rightButton.getLeft()
//                            && ev.getX() < rightButton.getRight()) {
//                        rightButton.dispatchTouchEvent(ev);
//                        return true;
//                    }
//                    if (mapView != null
//                            && ev.getY() < MapBottom
//                            && ev.getY() > MapTop) {
//                        mapView.dispatchTouchEvent(ev);
//                        return true;
//                    }
//                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
//            super.dispatchTouchEvent(ev);
//            Log.v(TAG,"catch IllegalArgumentException");
            return true;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

}
