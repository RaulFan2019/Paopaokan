package com.app.pao.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.app.pao.R;

/**
 * Created by LY on 2016/4/11.
 */
public class StopWalkPopupWindow extends PopupWindow {
    private TextView mStopAndSaveTv,mKeepWalkingTv;
    private LinearLayout mMenuViewLl;
//    private LinearLayout mBaseLl;
    private View mMenuView;
    private OnDialogDismissListener listener;

    public StopWalkPopupWindow(Activity context,OnClickListener itemsOnClick,OnDialogDismissListener listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.dialog_stop_walk,null);
        mStopAndSaveTv = (TextView) mMenuView.findViewById(R.id.tv_stop_and_save);
        mKeepWalkingTv = (TextView) mMenuView.findViewById(R.id.tv_keep_walking);
        mMenuViewLl = (LinearLayout) mMenuView.findViewById(R.id.pop_layout);
        this.listener = listener;
//        mBaseLl = (LinearLayout) mMenuView.findViewById(R.id.ll_base);
//        mBaseLl.getBackground().setAlpha(100);//透明度0~225之间
        //继续跑步按钮
        mKeepWalkingTv.setOnClickListener(itemsOnClick);
        mStopAndSaveTv.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuViewLl.getTop();
                int left = mMenuViewLl.getLeft();
                int right = mMenuViewLl.getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height || x <left || x > right) {
                        dismiss();
                        if(StopWalkPopupWindow.this.listener != null){
                            StopWalkPopupWindow.this.listener.dialogDismiss();
                        }
                    }
                }
                return true;
            }
        });
    }

    public interface OnDialogDismissListener{
        void dialogDismiss();
    }

}
