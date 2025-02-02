package com.app.pao.ui.widget.helper;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.entity.event.EventComment;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by zhonganyun on 15/10/12.
 */
public class LinkTouchMovementMethod extends LinkMovementMethod {

    private TouchSpan mPressedSpan;

    private int userId;
    private int position;
    private String nickName;
    private Context context;

    @Override
    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mPressedSpan = getPressedSpan(textView, spannable, event);
            if (mPressedSpan != null) {
                mPressedSpan.setPressed(true);
                Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan),
                        spannable.getSpanEnd(mPressedSpan));
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            TouchSpan touchedSpan = getPressedSpan(textView, spannable, event);
            if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                mPressedSpan.setPressed(false);
                mPressedSpan = null;
                Selection.removeSelection(spannable);
            }
        } else {
            if (mPressedSpan != null) {
                mPressedSpan.setPressed(false);
                super.onTouchEvent(textView, spannable, event);
            } else {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (userId == LocalApplication.getInstance().getLoginUser(context).getUserId()) {
                        userId = 0;
                        nickName = "";
                    }
                    EventComment eventComment = new EventComment(position, userId, nickName);
                    EventBus.getDefault().post(eventComment);
                }
            }
            mPressedSpan = null;
            Selection.removeSelection(spannable);
        }
        return true;
    }

    TouchSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= textView.getTotalPaddingLeft();
        y -= textView.getTotalPaddingTop();

        x += textView.getScrollX();
        y += textView.getScrollY();

        Layout layout = textView.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        TouchSpan[] link = spannable.getSpans(off, off, TouchSpan.class);
        TouchSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
