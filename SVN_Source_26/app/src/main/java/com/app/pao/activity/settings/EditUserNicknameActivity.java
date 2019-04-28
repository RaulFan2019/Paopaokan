package com.app.pao.activity.settings;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.UserData;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.utils.StringUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Raul on 2015/11/17.
 * 修改用户昵称界面
 */
@ContentView(R.layout.activity_edit_nickname)
public class EditUserNicknameActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "EditUserNicknameActivity";

    private static final int MSG_POST_ERROR = 1;//上传出错
    private static final int MSG_POST_OK = 2;//上传头像成功

    /* local view */
    @ViewInject(R.id.et_nickname)
    private EditText mNicknameEt;//用户昵称输入框

    /* local data */
    private boolean mPostAble;//是否可以上传
    private DBUserEntity mUserEntity;
    /**
     * 请求Handler
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //上传出现错误
                case MSG_POST_ERROR:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    T.showLong(mContext, (String) msg.obj);
                    break;
                //上传成功
                case MSG_POST_OK:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    };

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.commit, R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            //保存
            case R.id.commit:
                saveNickname();
                break;
            //取消
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }


    @Override
    protected void initData() {
        mPostAble = true;
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
    }

    @Override
    protected void initViews() {
        mNicknameEt.setText(mUserEntity.getNickname());
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {

    }

    /**
     * 保存昵称
     */
    private void saveNickname() {
        if (!mPostAble) {
            return;
        }
        //检查昵称
        final String nickname = mNicknameEt.getText().toString();
        String error = StringUtils.checkNickNameError(mContext, nickname);
        if (!error.equals(AppEnum.DEFAULT_CHECK_ERROR)) {
            T.showShort(mContext, error);
            mNicknameEt.setError(error);
            return;
        }
        showProgressDialog("提示", "正在提交修改...");
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserNicknameRequest(mContext,nickname);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserEntity.setNickname(nickname);
                UserData.updateUser(mContext,mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_OK;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_ERROR;
                    mPostMsg.obj = s;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    //显示等待信息
    private void showProgressDialog(String title, String msg) {
        mDialogBuilder.showProgressDialog(this, msg, true);
        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener() {
            @Override
            public void onCancelBtnClick() {
                mPostAble = true;
                cancelPostHandler();
            }

            @Override
            public void onCancel() {
                mPostAble = true;
                cancelPostHandler();
            }
        });
    }

    /**
     * 取消请求
     */
    private void cancelPostHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }
}
