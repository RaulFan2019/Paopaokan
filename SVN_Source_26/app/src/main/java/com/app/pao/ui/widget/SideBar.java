package com.app.pao.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.app.pao.utils.DeviceUtils;

/**
 * Created by LY on 2016/3/15.
 */
public class SideBar extends View {
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    //    public String[] A_Z = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
//            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
//            "W", "X", "Y", "Z", "#" };
    public String[] A_Z = {"A"};
    private int choose = -1;
    private Paint paint = new Paint();
    private static int singleHeight = (int) DeviceUtils.dpToPixel(13f);// 获取每一个字母的高度
    private static int textSize = (int) DeviceUtils.dpToPixel(11f);//字体大小
//    private TextView mTextDia;

//    public void setTextView(TextView textView){
//        this.mTextDia = textView;
//    }

    public void setStrArr(String[] strarr) {
        A_Z = strarr;
        invalidate();

    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
//        int height = getHeight();// 获取对应高度
        int height = 40 * A_Z.length;
        int width = getWidth(); // 获取对应宽度
        for (int i = 0; i < A_Z.length; i++) {
            paint.setColor(Color.parseColor("#888888")); //设置字体颜色
            paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体
            paint.setAntiAlias(true); //设置抗锯齿
            paint.setTextSize(textSize); //设置字母字体大小
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff")); //选中的字母改变颜色
                paint.setFakeBoldText(true); //设置字体为粗体
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(A_Z[i]) / 2;
            float yPos = getHeight() / 2 + (i - A_Z.length / 2) * singleHeight;
            canvas.drawText(A_Z[i], xPos, yPos, paint); //绘制所有的字母
            paint.reset();// 重置画笔
        }
//        this.setMeasuredDimension(width,height);//重新绘制控件的宽和高
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
//        final int c = (int) (y / getHeight() * A_Z.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        final int c = (int) ((y - getHeight() / 2) / singleHeight + A_Z.length / 2);
        switch (action) {
            case MotionEvent.ACTION_UP:
//                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;
                invalidate();
//                if (mTextDia != null) {
//                    mTextDia.setVisibility(View.INVISIBLE);
//                }
                break;
            default:
//                setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != c) { //判断选中字母是否发生改变
                    if (c >= 0 && c < A_Z.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(A_Z[c]);
                        }
//                        if (mTextDia != null) {
//                            mTextDia.setText(A_Z[c]);
//                            mTextDia.setVisibility(View.VISIBLE);
//                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 对外公开的方法
     *
     * @param listener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener) {
        this.onTouchingLetterChangedListener = listener;
    }

    /**
     * 对外接口
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}
