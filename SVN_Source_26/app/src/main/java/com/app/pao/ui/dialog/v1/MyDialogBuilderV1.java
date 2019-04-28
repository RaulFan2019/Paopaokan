package com.app.pao.ui.dialog.v1;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.adapter.ListDialogAdapter;
import com.app.pao.adapter.MedalChangeAdapter;
import com.app.pao.entity.model.UserOptMedalEntity;
import com.app.pao.ui.widget.CircleFlowIndicator;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.SwitchView;
import com.app.pao.ui.widget.ViewFlow;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.Button;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.ProgressView;

import java.util.List;

/**
 * V1 版本的对话框生成工具
 */
public class MyDialogBuilderV1 {

    private ChoiceDilaogListener mChoiceListener;
    private ClipDialogListener mClipDialogListener;
    private ChoiceTwoBtnDialogListener mChoiceTwoBtnDialogListener;
    private ProgressDilaogListener mProgressListner;
    private ListDialogListener mListListener;
    private SpinnerDialogListener mSpinnerListener;
    private ShareDialogListener mShareListener;
    private BluetoothSetDialogListener mBluetoothSetListener;
    private ChoiceUserPushDialogListener mChoiceUserPushDialogListener;
    private ChoiceGroupPushDialogListener mChoiceGroupPushDialogListener;
    private OnlyNewsDialogListener mOnlyNewsDialogListener;
    private ShareWxFriendDialogListener mShareWxFriendDialogListener;
    private ResetPasswordDialogListener mResetPasswordDialogListener;

    public Dialog bluttoothSetDialog;//蓝牙设置对话框
    public Dialog choiceDialog; // 选择类型对话框
    public Dialog progressDialog;// 等待对话框
    public Dialog listDialog;
    public Dialog spinnerDialog;
    public Dialog clipDialog;//剪贴板显示的对话框
    public Dialog choiceTwoBtnDialog;//两个按钮的对话框
    public Dialog SimpleMsgDialog;//简单的信息对话框
    public Dialog userPushDialog;//个人用户相关推送对话框
    public Dialog groupPushDialog;//跑团相关推送对话框
    public Dialog onlyNewsDialog;//仅有消息标题和正文的对话框
    public Dialog shareWxFriendDialog;//分享给微信好友朋友圈对话框
    public Dialog showResetPasswordDialog;//重置密码对话框

    /**
     * view variable
     **/
    private static Button leftBtn;// 对话框左边按钮
    private static Button middleBtn;// 对话框中间按钮
    private static Button rightBtn;// 对话框右边按钮
    private static EditText inputEt;// 对话框输入框
    private static TextView titleTv;// 对话框标题文本
    private static TextView contentTv;// 对话框信息文本
    public static TextView msgTv;// 对话框消息文本
    private static Button cancelBtn;// 取消按钮
    private static ProgressView mLoadProgress;//加载中进度条
    private static Spinner mSpeedSp;//配速
    private static CheckBox mLeadCb;//领跑

    private ListView contentLv;


    /**
     * listener
     **/
    public interface ChoiceDilaogListener {
        void onLeftBtnClick();

        void onMiddleBtnClick();

        void onRightBtnClick();

        void onCancel();
    }

    public interface ClipDialogListener {
        void onLeftBtnClick();

        void onRightBtnClick();

        void onCancel();
    }

    public interface ChoiceTwoBtnDialogListener {
        void onLeftBtnClick();

        void onRightBtnClick();

        void onCancel();
    }

    public interface ChoiceUserPushDialogListener {
        void onLeftBtnClick();

        void onRightBtnClick();

        void onCancel();
    }

    public interface ChoiceGroupPushDialogListener {
        void onLeftBtnClick();

        void onRightBtnClick();

        void onCancel();
    }

    public interface ProgressDilaogListener {
        void onCancelBtnClick();

        void onCancel();
    }

    public interface ListDialogListener {
        void onItemClick(int position);

        void onCancel();
    }

    public interface SpinnerDialogListener {
        void onCancelBtnClick();

        void onConfirmBtnClick(String spStr, boolean isChecked);
    }

    public interface ShareDialogListener {
        void onCancelShare();

        void onGoShare();
    }

    public interface BluetoothSetDialogListener {
        void onLeftBtnClick();

        void onRightBtnClick();

        void onBuletoothSetClick();

        void onCancel();
    }

    public interface OnlyNewsDialogListener {
        void onCancel();
    }

    public interface ShareWxFriendDialogListener {
        void onWxFriendClick();

        void onWxFriendCircleClick();

        void onCancle();
    }

    public interface ResetPasswordDialogListener{
        void onLeftBtnClick();

        void onRightBtnClick(String pwd);

        void onCancel();
    }

    /**
     * set listener
     **/
    public void setListener(ChoiceDilaogListener mListener) {
        this.mChoiceListener = mListener;
    }

    public void setListener(ClipDialogListener mListener) {
        this.mClipDialogListener = mListener;
    }

    public void setListener(ProgressDilaogListener mListener) {
        this.mProgressListner = mListener;
    }

    public void setListener(ListDialogListener mListener) {
        this.mListListener = mListener;
    }

    public void setSpinnerListener(SpinnerDialogListener mSpinnerListener) {
        this.mSpinnerListener = mSpinnerListener;
    }

    public void setShareListener(ShareDialogListener mShareListener) {
        this.mShareListener = mShareListener;
    }

    public void setListener(ChoiceTwoBtnDialogListener listener) {
        this.mChoiceTwoBtnDialogListener = listener;
    }

    public void setBluetoothSetListener(BluetoothSetDialogListener listener) {
        this.mBluetoothSetListener = listener;
    }

    public void setListener(ChoiceUserPushDialogListener listener) {
        this.mChoiceUserPushDialogListener = listener;
    }

    public void setListener(ChoiceGroupPushDialogListener listener) {
        this.mChoiceGroupPushDialogListener = listener;
    }

    public void setListener(OnlyNewsDialogListener listener) {
        this.mOnlyNewsDialogListener = listener;
    }

    public void setListener(ShareWxFriendDialogListener listener) {
        this.mShareWxFriendDialogListener = listener;
    }

    public void setListener(ResetPasswordDialogListener listener){
        this.mResetPasswordDialogListener = listener;
    }


    /**
     * 显示选择类型对话框
     *
     * @param context
     * @param title
     * @param content
     */
    public void showSimpleMsgDialog(Context context, String title, Spanned content) {
        /** find view **/
        SimpleMsgDialog = new Dialog(context, R.style.DialogThemeV1);
        SimpleMsgDialog.setContentView(R.layout.dialog_simple_msg);
        SimpleMsgDialog.setCancelable(true);
        SimpleMsgDialog.setCanceledOnTouchOutside(true);
        TextView titleTv = (TextView) SimpleMsgDialog.findViewById(R.id.tv_dialog_title);
        TextView contentTv = (TextView) SimpleMsgDialog.findViewById(R.id.tv_dialog_content);
        /** init views **/
        titleTv.setText(title);
        contentTv.append(content);
        SimpleMsgDialog.show();
    }

    /**
     * 显示选择类型对话框
     *
     * @param context
     * @param title
     * @param content
     * @param leftBtnTv
     * @param middleBtnTv
     * @param rightBtnTv
     */
    public void showChoiceDialog(Context context, String title, String content, String leftBtnTv, String middleBtnTv,
                                 String rightBtnTv) {
        /** find view **/
        choiceDialog = new Dialog(context, R.style.DialogThemeV1);
        choiceDialog.setContentView(R.layout.dialog_bluetooth_set);
        TextView titleTv = (TextView) choiceDialog.findViewById(R.id.tv_dialog_title);
        TextView contentTv = (TextView) choiceDialog.findViewById(R.id.tv_dialog_content);
        TextView setTv = (TextView) choiceDialog.findViewById(R.id.tv_dialog_set);
        LinearLayout leftBtn = (LinearLayout) choiceDialog.findViewById(R.id.ll_left);
        LinearLayout rightBtn = (LinearLayout) choiceDialog.findViewById(R.id.ll_right);
        TextView leftTv = (TextView) choiceDialog.findViewById(R.id.tv_left);
        TextView rightTv = (TextView) choiceDialog.findViewById(R.id.tv_right);
        /** init views **/
        titleTv.setText(title);
        contentTv.setText(content);
        leftTv.setText(leftBtnTv);
        rightTv.setText(rightBtnTv);

        if (middleBtnTv == null) {
            setTv.setVisibility(View.GONE);
        } else {
            setTv.setText(middleBtnTv);
        }
        if (rightBtnTv == null) {
            leftBtn.setBackgroundResource(R.drawable.bg_dialog_btn_single_shape_v2);
            rightBtn.setVisibility(View.GONE);
        } else {
            rightBtn.setVisibility(View.VISIBLE);
            rightTv.setText(rightBtnTv);
            leftBtn.setBackgroundResource(R.drawable.bg_dialog_btn_left_shape_v2);
        }

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceDialogLeftBtnClick();
                choiceDialog.dismiss();
            }
        });
        setTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceDialogMiddleBtnClick();
                choiceDialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceDialogRightBtnClick();
                choiceDialog.dismiss();
            }
        });

        choiceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ChoiceDialogCancel();
            }
        });
        choiceDialog.show();
    }

    private void ChoiceDialogLeftBtnClick() {
        if (mChoiceListener != null) {
            mChoiceListener.onLeftBtnClick();
        }
    }

    private void ChoiceDialogMiddleBtnClick() {
        if (mChoiceListener != null) {
            mChoiceListener.onMiddleBtnClick();
        }
    }

    private void ChoiceDialogRightBtnClick() {
        if (mChoiceListener != null) {
            mChoiceListener.onRightBtnClick();
        }
    }

    private void ChoiceDialogCancel() {
        if (mChoiceListener != null) {
            mChoiceListener.onCancel();
        }
    }

    /**
     * 显示蓝牙设置专用对话框
     *
     * @param context
     * @param title
     * @param content
     * @param leftBtnTv
     * @param rightBtnTv
     */
    public void showBlueToothSetDialog(Context context, String title, String content, String leftBtnTv,
                                       String rightBtnTv) {
        /** find view **/
        bluttoothSetDialog = new Dialog(context, R.style.DialogThemeV1);
        bluttoothSetDialog.setContentView(R.layout.dialog_bluetooth_set);
        TextView titleTv = (TextView) bluttoothSetDialog.findViewById(R.id.tv_dialog_title);
        TextView contentTv = (TextView) bluttoothSetDialog.findViewById(R.id.tv_dialog_content);
        TextView setTv = (TextView) bluttoothSetDialog.findViewById(R.id.tv_dialog_set);
        LinearLayout leftBtn = (LinearLayout) bluttoothSetDialog.findViewById(R.id.ll_left);
        LinearLayout rightBtn = (LinearLayout) bluttoothSetDialog.findViewById(R.id.ll_right);
        TextView leftTv = (TextView) bluttoothSetDialog.findViewById(R.id.tv_left);
        TextView rightTv = (TextView) bluttoothSetDialog.findViewById(R.id.tv_right);
        /** init views **/
        titleTv.setText(title);
        contentTv.setText(content);
        leftTv.setText(leftBtnTv);
        rightTv.setText(rightBtnTv);

        if (rightBtnTv == null) {
            rightBtn.setVisibility(View.GONE);
        } else {
            rightTv.setText(rightBtnTv);
        }

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothDialogLeftBtnClick();
                bluttoothSetDialog.dismiss();
            }
        });
        setTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothDialogSetBtnClick();
                bluttoothSetDialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothDialogRightBtnClick();
                bluttoothSetDialog.dismiss();
            }
        });
        bluttoothSetDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                bluetoothSetDialogCancel();
            }
        });
        bluttoothSetDialog.show();
    }

    private void bluetoothDialogLeftBtnClick() {
        if (mBluetoothSetListener != null) {
            mBluetoothSetListener.onLeftBtnClick();
        }
    }

    private void bluetoothDialogSetBtnClick() {
        if (mBluetoothSetListener != null) {
            mBluetoothSetListener.onBuletoothSetClick();
        }
    }

    private void bluetoothDialogRightBtnClick() {
        if (mBluetoothSetListener != null) {
            mBluetoothSetListener.onRightBtnClick();
        }
    }

    private void bluetoothSetDialogCancel() {
        if (mBluetoothSetListener != null) {
            mBluetoothSetListener.onCancel();
        }
    }

    /**
     * 显示选择类型对话框
     *
     * @param context
     * @param title
     * @param content
     * @param leftBtnTv
     * @param rightBtnTv
     */
    public void showClipDialog(Context context, String title, String name, String avatar,
                               String content, String leftBtnTv, String rightBtnTv) {
        /** find view **/
        clipDialog = new Dialog(context, R.style.DialogThemeV1);
        clipDialog.setContentView(R.layout.dialog_clip);
        TextView titleTv = (TextView) clipDialog.findViewById(R.id.tv_title);
        CircularImage iv = (CircularImage) clipDialog.findViewById(R.id.iv_avatar);
        TextView nameTv = (TextView) clipDialog.findViewById(R.id.tv_name);
        TextView contentTv = (TextView) clipDialog.findViewById(R.id.tv_content);
        LinearLayout leftBtn = (LinearLayout) clipDialog.findViewById(R.id.ll_left);
        LinearLayout rightBtn = (LinearLayout) clipDialog.findViewById(R.id.ll_right);
        TextView leftTv = (TextView) clipDialog.findViewById(R.id.tv_left);
        TextView rightTv = (TextView) clipDialog.findViewById(R.id.tv_right);

        /** init views **/
        titleTv.setText(title);
        nameTv.setText(name);
        contentTv.setText(content);
        leftTv.setText(leftBtnTv);
        rightTv.setText(rightBtnTv);
        ImageUtils.loadUserImage(avatar, iv);


        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipDialogLeftBtnClick();
                clipDialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipDialogRightBtnClick();
                clipDialog.dismiss();
            }
        });
        clipDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ClipDialogCancel();
            }
        });
        clipDialog.show();
    }

    private void ClipDialogLeftBtnClick() {
        if (mClipDialogListener != null) {
            mClipDialogListener.onLeftBtnClick();
        }
    }

    private void ClipDialogRightBtnClick() {
        if (mClipDialogListener != null) {
            mClipDialogListener.onRightBtnClick();
        }
    }

    private void ClipDialogCancel() {
        if (mClipDialogListener != null) {
            mClipDialogListener.onCancel();
        }
    }


    /**
     * 显示选择类型对话框
     *
     * @param context
     * @param title
     * @param content
     * @param leftBtnTv
     * @param rightBtnTv
     */
    public void showChoiceTwoBtnDialog(Context context, String title,
                                       String content, String leftBtnTv, String rightBtnTv) {
        /** find view **/
        choiceTwoBtnDialog = new Dialog(context, R.style.DialogThemeV1);
        choiceTwoBtnDialog.setContentView(R.layout.dialog_choice_two_btn_v2);
        TextView titleTv = (TextView) choiceTwoBtnDialog.findViewById(R.id.tv_dialog_title);
        TextView contentTv = (TextView) choiceTwoBtnDialog.findViewById(R.id.tv_dialog_content);
        LinearLayout leftBtn = (LinearLayout) choiceTwoBtnDialog.findViewById(R.id.ll_left);
        LinearLayout rightBtn = (LinearLayout) choiceTwoBtnDialog.findViewById(R.id.ll_right);
        TextView leftTv = (TextView) choiceTwoBtnDialog.findViewById(R.id.tv_left);
        TextView rightTv = (TextView) choiceTwoBtnDialog.findViewById(R.id.tv_right);

        /** init views **/
        titleTv.setText(title);
        contentTv.setText(content);
        leftTv.setText(leftBtnTv);
        rightTv.setText(rightBtnTv);

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceTwoBtnDialogLeftBtnClick();
                choiceTwoBtnDialog.dismiss();
            }
        });
        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ChoiceTwoBtnDialogRightBtnClick();
                choiceTwoBtnDialog.dismiss();
            }
        });
        choiceTwoBtnDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ChoiceTwoBtnDialogCancel();
            }
        });
        choiceTwoBtnDialog.show();
    }

    private void ChoiceTwoBtnDialogLeftBtnClick() {
        if (mChoiceTwoBtnDialogListener != null) {
            mChoiceTwoBtnDialogListener.onLeftBtnClick();
        }
    }

    private void ChoiceTwoBtnDialogRightBtnClick() {
        if (mChoiceTwoBtnDialogListener != null) {
            mChoiceTwoBtnDialogListener.onRightBtnClick();
        }
    }

    private void ChoiceTwoBtnDialogCancel() {
        if (mChoiceTwoBtnDialogListener != null) {
            mChoiceTwoBtnDialogListener.onCancel();
        }
    }


    /**
     * 显示进度条对话框
     *
     * @param mContext
     */
    public void showProgressDialog(Context mContext, String msg, boolean cancelAble) {
        progressDialog = new Dialog(mContext, R.style.DialogThemeV1);
        progressDialog.setContentView(R.layout.dialog_progressbar_v1);
        progressDialog.setCancelable(cancelAble);
        progressDialog.setCanceledOnTouchOutside(false);

        mLoadProgress = (ProgressView) progressDialog.findViewById(R.id.progressview_loading);
        cancelBtn = (Button) progressDialog.findViewById(R.id.btn_cancel_pro);
        msgTv = (TextView) progressDialog.findViewById(R.id.tv_dialog_msg);

        mLoadProgress.start();
        msgTv.setText(msg);
        cancelBtn.setVisibility(View.GONE);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mProgressListner != null) {
                    mProgressListner.onCancelBtnClick();
                }
                mLoadProgress.stop();
                progressDialog.dismiss();
            }
        });
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mLoadProgress != null) {
                    mLoadProgress.stop();
                }
                if (mProgressListner != null) {
                    mProgressListner.onCancel();
                }

            }
        });
        progressDialog.show();
    }

    /**
     * destroy
     */
    public void Destroy() {
        if (choiceDialog != null) {
            if (choiceDialog.isShowing()) {
                choiceDialog.dismiss();
                if (mLoadProgress != null) {
                    mLoadProgress.stop();
                }
            }
        }
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                if (mLoadProgress != null) {
                    mLoadProgress.stop();
                }
            }
        }
        if (listDialog != null) {
            if (listDialog.isShowing()) {
                listDialog.dismiss();
            }
        }

        if (spinnerDialog != null) {
            if (spinnerDialog.isShowing()) {
                listDialog.dismiss();
            }
        }

        if (clipDialog != null) {
            if (clipDialog.isShowing()) {
                clipDialog.dismiss();
            }
        }

        if (SimpleMsgDialog != null) {
            if (SimpleMsgDialog.isShowing()) {
                SimpleMsgDialog.dismiss();
            }
        }

        if (choiceTwoBtnDialog != null) {
            if (choiceTwoBtnDialog.isShowing()) {
                choiceTwoBtnDialog.dismiss();
            }
        }

        if (bluttoothSetDialog != null) {
            if (bluttoothSetDialog.isShowing()) {
                bluttoothSetDialog.dismiss();
            }
        }
    }

    /**
     * 列表dialog
     *
     * @param mContext
     * @param mData
     */
    public void showListDialog(Context mContext, List<String> mData) {
        listDialog = new Dialog(mContext, R.style.DialogThemeV1);
        listDialog.setContentView(R.layout.dialog_list);
        listDialog.setCancelable(true);
        listDialog.setCanceledOnTouchOutside(true);

        ListDialogAdapter adapter = new ListDialogAdapter(mContext, mData);
        contentLv = (ListView) listDialog.findViewById(R.id.lv_dialog);
        cancelBtn = (Button) listDialog.findViewById(R.id.btn_cancel_pro);

        contentLv.setAdapter(adapter);

        contentLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mListListener != null) {
                    mListListener.onItemClick(position);
                }
                listDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListListener != null) {
                    mListListener.onCancel();
                }

                listDialog.dismiss();
            }
        });

        listDialog.show();
    }

    /**
     * spinner dialog
     *
     * @param mContext
     * @param mData
     */
    public void showSpinnerDialog(Context mContext, final List<String> mData) {
        spinnerDialog = new Dialog(mContext, R.style.DialogThemeV1);
        spinnerDialog.setContentView(R.layout.dialog_spinner);
        spinnerDialog.setCancelable(true);
        spinnerDialog.setCanceledOnTouchOutside(true);

        final SwitchView leadSv = (SwitchView) spinnerDialog.findViewById(R.id.sv_jpush);
        LinearLayout leftll = (LinearLayout) spinnerDialog.findViewById(R.id.ll_left);
        LinearLayout rightll = (LinearLayout) spinnerDialog.findViewById(R.id.ll_right);
        final Spinner spinner = (Spinner) spinnerDialog.findViewById(R.id.spinner_label);

        ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.item_dialog_spinner_speed, mData);
        spinner.setAdapter(adapter);

        leftll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinnerListener != null) {
                    mSpinnerListener.onCancelBtnClick();
                }
                spinnerDialog.dismiss();

            }
        });

        rightll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSpinnerListener != null) {
                    mSpinnerListener.onConfirmBtnClick(mData.get(spinner.getSelectedItemPosition()), leadSv.isOpened());
                }
                spinnerDialog.dismiss();
            }
        });
        spinnerDialog.show();
    }

    /**
     * 分享提示Dialog
     *
     * @param context
     * @param title
     * @param shareText
     */
    public void showShareDialog(Context context, String title, String shareText) {
        /** find view **/
        choiceDialog = new Dialog(context, R.style.DialogThemeV1);
        choiceDialog.setContentView(R.layout.dialog_share_wx_text);

        titleTv = (TextView) choiceDialog.findViewById(R.id.tv_share_title);
        contentTv = (TextView) choiceDialog.findViewById(R.id.tv_share_context);

        TextView cancelShareTv = (TextView) choiceDialog.findViewById(R.id.tv_share_cancel);
        TextView shareTv = (TextView) choiceDialog.findViewById(R.id.tv_share_go);

        titleTv.setText(title);
        contentTv.setText(shareText);

        cancelShareTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareListener != null) {
                    mShareListener.onCancelShare();
                }
                choiceDialog.dismiss();
            }
        });

        shareTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareListener != null) {
                    mShareListener.onGoShare();
                }
                choiceDialog.dismiss();
            }
        });
        choiceDialog.show();
    }

    /**
     * 用户个人推送专用对话框
     *
     * @param context      上下文
     * @param title        对话框标题
     * @param userAvatar   用户头像
     * @param userName     用户名字
     * @param leftText     名字下方左侧对话框文本
     * @param leftBut      左侧按钮文本
     * @param rightBut     右侧按钮文本
     */
    public void setUserPushDialog(Context context,String title, String userAvatar, String userName,
                                  String leftText, String leftBut, String rightBut) {
        userPushDialog = new Dialog(context, R.style.DialogThemeV1);
        userPushDialog.setContentView(R.layout.dialog_user_push);

        TextView mTitleTv = (TextView) userPushDialog.findViewById(R.id.tv_dialog_title);
        ImageView mUserAvatarIv = (ImageView) userPushDialog.findViewById(R.id.iv_user_avatar);
        TextView mUserNameTv = (TextView) userPushDialog.findViewById(R.id.tv_user_name);
        TextView mLeftTextTv = (TextView) userPushDialog.findViewById(R.id.tv_left_text);
        TextView mLeftButTv = (TextView) userPushDialog.findViewById(R.id.tv_left);
        TextView mRightButTv = (TextView) userPushDialog.findViewById(R.id.tv_right);
        LinearLayout mLeftButLl = (LinearLayout) userPushDialog.findViewById(R.id.ll_left);
        LinearLayout mRightButLl = (LinearLayout) userPushDialog.findViewById(R.id.ll_right);

        mTitleTv.setText(title);
        ImageUtils.loadUserImage(userAvatar, mUserAvatarIv);
        mUserNameTv.setText(userName);
        mLeftTextTv.setText(leftText);
        if (leftBut == null) {
            mLeftButLl.setVisibility(View.GONE);
        } else {
            mLeftButTv.setText(leftBut);
        }
        if (rightBut == null) {
            mRightButLl.setVisibility(View.GONE);
        } else {
            mRightButTv.setText(rightBut);
        }

        mLeftButLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChoiceUserPushDialogListener != null) {
                    mChoiceUserPushDialogListener.onLeftBtnClick();
                    userPushDialog.dismiss();
                }
            }
        });
        mRightButLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChoiceUserPushDialogListener != null) {
                    mChoiceUserPushDialogListener.onRightBtnClick();
                    userPushDialog.dismiss();
                }
            }
        });
        userPushDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mChoiceUserPushDialogListener != null) {
                    mChoiceUserPushDialogListener.onCancel();
                    userPushDialog.dismiss();
                }
            }
        });
        userPushDialog.show();
    }

    /**
     * 跑团个人推送专用对话框
     *
     * @param context      上下文
     * @param mBitmapUtils 图片加载工具
     * @param title        对话框标题
     * @param userAvatar   跑团头像
     * @param userName     跑团名字
     * @param leftText     对话框的主要内容
     * @param leftBut      左侧按钮文本
     * @param rightBut     右侧按钮文本
     */
    public void setGroupPushDialog(Context context, String title, String userAvatar, String userName,
                                   String leftText, String leftBut, String rightBut) {
        groupPushDialog = new Dialog(context, R.style.DialogThemeV1);
        groupPushDialog.setContentView(R.layout.dialog_group_push);

        LinearLayout mBtnLl = (LinearLayout) groupPushDialog.findViewById(R.id.ll_btn);
        TextView mTitleTv = (TextView) groupPushDialog.findViewById(R.id.tv_dialog_title);
        ImageView mUserAvatarIv = (ImageView) groupPushDialog.findViewById(R.id.iv_user_avatar);
        TextView mUserNameTv = (TextView) groupPushDialog.findViewById(R.id.tv_user_name);
        TextView mLeftTextTv = (TextView) groupPushDialog.findViewById(R.id.tv_left_text);
        TextView mLeftButTv = (TextView) groupPushDialog.findViewById(R.id.tv_left);
        TextView mRightButTv = (TextView) groupPushDialog.findViewById(R.id.tv_right);
        LinearLayout mLeftButLl = (LinearLayout) groupPushDialog.findViewById(R.id.ll_left);
        LinearLayout mRightButLl = (LinearLayout) groupPushDialog.findViewById(R.id.ll_right);

        mTitleTv.setText(title);
        ImageUtils.loadGroupImage(userAvatar, mUserAvatarIv);
        mUserNameTv.setText(userName);
        mLeftTextTv.setText(leftText);
        if (leftBut == null) {
            mLeftButLl.setVisibility(View.GONE);
        } else {
            mLeftButTv.setText(leftBut);
        }

        if (rightBut == null) {
            mRightButLl.setVisibility(View.GONE);
        } else {
            mRightButTv.setText(rightBut);
        }

        if (leftBut == null && rightBut == null) {
            mBtnLl.setVisibility(View.GONE);
        } else {
            mBtnLl.setVisibility(View.VISIBLE);
        }

        mLeftButLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChoiceGroupPushDialogListener != null) {
                    mChoiceGroupPushDialogListener.onLeftBtnClick();
                    groupPushDialog.dismiss();
                }
            }
        });
        mRightButLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChoiceGroupPushDialogListener != null) {
                    mChoiceGroupPushDialogListener.onRightBtnClick();
                    groupPushDialog.dismiss();
                }
            }
        });
        groupPushDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mChoiceGroupPushDialogListener != null) {
                    mChoiceGroupPushDialogListener.onCancel();
                    groupPushDialog.dismiss();
                }
            }
        });
        groupPushDialog.show();
    }

    /**
     * 纯文本推送消息对话框
     *
     * @param context 上下文
     * @param title   文本标题
     * @param text    文本内容
     */
    public void setOnlyNewsDialog(Context context, String title, String text) {
        onlyNewsDialog = new Dialog(context, R.style.DialogThemeV1);
        onlyNewsDialog.setContentView(R.layout.dialog_only_news_push);

        TextView mTitleTv = (TextView) onlyNewsDialog.findViewById(R.id.tv_dialog_title);
        TextView mTextTv = (TextView) onlyNewsDialog.findViewById(R.id.tv_dialog_text);

        mTitleTv.setText(title);
        mTextTv.setText(text);
        onlyNewsDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mOnlyNewsDialogListener != null) {
                    mOnlyNewsDialogListener.onCancel();
                    onlyNewsDialog.dismiss();
                }
            }
        });
        onlyNewsDialog.show();
    }

    /**
     * 分享到微信好友朋友圈对话框
     *
     * @param context
     * @param title
     */
    public void setShareWxFriendDialog(Context context, String title) {
        shareWxFriendDialog = new Dialog(context, R.style.DialogThemeV1);
        shareWxFriendDialog.setContentView(R.layout.dialog_share_wx);

        TextView mTitleTv = (TextView) shareWxFriendDialog.findViewById(R.id.tv_share_wx_title);
        LinearLayout mShareWxFriendLl = (LinearLayout) shareWxFriendDialog.findViewById(R.id.ll_share_wx_friend);
        LinearLayout mShareWxFriendCircleLl = (LinearLayout) shareWxFriendDialog.findViewById(R.id.ll_share_wx_friend_circle);

        mTitleTv.setText(title);

        mShareWxFriendLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareWxFriendDialogListener != null) {
                    mShareWxFriendDialogListener.onWxFriendClick();
                    shareWxFriendDialog.dismiss();
                }
            }
        });

        mShareWxFriendCircleLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShareWxFriendDialogListener != null) {
                    mShareWxFriendDialogListener.onWxFriendCircleClick();
                    shareWxFriendDialog.dismiss();
                }
            }
        });

        shareWxFriendDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mShareWxFriendDialogListener != null) {
                    mShareWxFriendDialogListener.onCancle();
                    shareWxFriendDialog.dismiss();
                }
            }
        });

        shareWxFriendDialog.show();
    }

    public void setResetPasswordDialog(Context context,String leftBttonTv,String rightButtonTv){
        showResetPasswordDialog = new Dialog(context, R.style.DialogThemeV1);
        showResetPasswordDialog.setContentView(R.layout.dialog_reset_password);

        final EditText mPswEt = (EditText)showResetPasswordDialog.findViewById(R.id.et_old_password);
        TextView mLeftButTv = (TextView) showResetPasswordDialog.findViewById(R.id.tv_left);
        TextView mRightButTv = (TextView) showResetPasswordDialog.findViewById(R.id.tv_right);
        LinearLayout mLeftButLl = (LinearLayout) showResetPasswordDialog.findViewById(R.id.ll_left);
        LinearLayout mRightButLl = (LinearLayout) showResetPasswordDialog.findViewById(R.id.ll_right);

        mLeftButTv.setText(leftBttonTv);
        mRightButTv.setText(rightButtonTv);

        mLeftButLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResetPasswordDialogListener != null) {
                    mResetPasswordDialogListener.onLeftBtnClick();
                    showResetPasswordDialog.dismiss();
                }
            }
        });

        mRightButLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mResetPasswordDialogListener != null) {
                    String password = mPswEt.getText().toString();
                    mResetPasswordDialogListener.onRightBtnClick(password);
                    showResetPasswordDialog.dismiss();
                }
            }
        });

        showResetPasswordDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mResetPasswordDialogListener != null) {
                    mResetPasswordDialogListener.onCancel();
                    showResetPasswordDialog.dismiss();
                }
            }
        });

        mPswEt.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager)mPswEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mPswEt, 0);
        showResetPasswordDialog.show();
    }
}
