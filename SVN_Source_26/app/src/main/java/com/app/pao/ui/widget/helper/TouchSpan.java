package com.app.pao.ui.widget.helper;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.app.pao.entity.event.EventCommentUserClick;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by zhonganyun on 15/10/12.
 */
public class TouchSpan extends ClickableSpan {

    private boolean mIsPressed;
    private int mPressedBackgroundColor;
    private int mNormalTextColor;
    private int mPressedTextColor;


    private int userId;

    public TouchSpan(Context context, int userId) {
        this.userId = userId;
        mNormalTextColor = Color.parseColor("#4f6591");
        mPressedTextColor = mNormalTextColor;
        mPressedBackgroundColor = Color.LTGRAY;

    }

    public TouchSpan(int normalTextColor, int pressedTextColor, int pressedBackgroundColor) {
        mNormalTextColor = normalTextColor;
        mPressedTextColor = pressedTextColor;
        mPressedBackgroundColor = pressedBackgroundColor;
    }

    public void setPressed(boolean isSelected) {
        mIsPressed = isSelected;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(mIsPressed ? mPressedTextColor : mNormalTextColor);
        ds.bgColor = mIsPressed ? mPressedBackgroundColor : 0x00eeeeee;
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        EventBus.getDefault().post(new EventCommentUserClick(userId));
    }
}
