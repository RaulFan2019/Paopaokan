package com.app.pao.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.app.pao.utils.DeviceUtils;

/**
 * Created by Raul.Fan on 2016/4/24.
 */
public class SclrollViewFroBasicHistory extends ScrollView {

    private static final int TopY = (int) DeviceUtils.dpToPixel(320f);

    public interface TouchListener {
        void onTouch(MotionEvent ev);
    }

    private TouchListener mTouchListener;

    public void setMyTouchListener(TouchListener tuchListener){
        this.mTouchListener = tuchListener;
    }

    public SclrollViewFroBasicHistory(Context context) {
        super(context);
    }

    public SclrollViewFroBasicHistory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SclrollViewFroBasicHistory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getY() < TopY && mTouchListener != null) {
                mTouchListener.onTouch(ev);
                return true;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return true;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return false;
    }

}
