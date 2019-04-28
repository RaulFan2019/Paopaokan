package com.app.pao.ui.dialog;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.pao.R;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;

/**
 * Created by Administrator on 2015/12/9.
 */
public class SimpleTipMsgDialog {

    private onMsgDialogClickListener clickListener;

    private FragmentManager fm;

    private Context mContext;

    public interface onMsgDialogClickListener{
        void onPositiveClicked();
        void onNegativeClicked();
    }

    public void setOnMsgDialogClickListener(onMsgDialogClickListener clickListener){
        this.clickListener = clickListener;
    }

    public SimpleTipMsgDialog( FragmentManager fm, Context mContext,onMsgDialogClickListener clickListener) {
        this.clickListener = clickListener;
        this.fm = fm;
        this.mContext = mContext;
    }

    public SimpleTipMsgDialog( FragmentManager fm, Context mContextr) {
        this.fm = fm;
        this.mContext = mContext;
    }

    /**
     * 显示带取消按钮的msg提示框
     */
    public void showSimpleCancelMsgDialog(final String mMsg){
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
            @Override
            protected void onBuildDone(Dialog dialog) {
              //  dialog.layoutParams(DeviceUtils.dp2px(mContext,260), DeviceUtils.dp2px(mContext,140));
                dialog.layoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView mMsgTv = (TextView) dialog.findViewById(R.id.tv_dialog_msg);
                mMsgTv.setText(mMsg);
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                if(clickListener != null) {
                    clickListener.onPositiveClicked();
                }
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                if(clickListener != null) {
                    clickListener.onNegativeClicked();
                }
                super.onNegativeActionClicked(fragment);
            }
        };
        builder.positiveAction("确定").negativeAction("取消").contentView(R.layout.dialog_simple_msg);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(fm,null);
    }

    /**
     * 显示一个按钮的消息显示框
     */
    public void showSimpleMsgDialog( final String mMsg){
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight){
            @Override
            protected void onBuildDone(Dialog dialog) {
           //     dialog.layoutParams(DeviceUtils.dp2px(mContext,260), DeviceUtils.dp2px(mContext,140));
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView mMsgTv = (TextView) dialog.findViewById(R.id.tv_dialog_msg);
                mMsgTv.setText(mMsg);
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                if(clickListener != null) {
                    clickListener.onPositiveClicked();
                }
                super.onPositiveActionClicked(fragment);
            }

        };
        builder.title("提示").positiveAction("确定").contentView(R.layout.dialog_simple_msg);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(fm,null);
    }




}
