package com.app.pao.activity.settings;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2015/11/29.
 */
@ContentView(R.layout.activity_edit_user_gender)
public class EditUserGenderActivity extends BaseAppCompActivity implements View.OnClickListener {

    private static final int MSG_POST_ERROR = 1;//上传出错
    private static final int MSG_POST_GENDER_OK = 2;//性别

    @ViewInject(R.id.fl_man)
    private FrameLayout mManLl;
    @ViewInject(R.id.fl_women)
    private FrameLayout mWomenLl;
    @ViewInject(R.id.iv_man_check)
    private ImageView mManIv;
    @ViewInject(R.id.iv_women_check)
    private ImageView mWomenIv;

    private boolean mPostAble = true;

    private DBUserEntity mUserEntity;//用户信息


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
                //上传性别
                case MSG_POST_GENDER_OK:
                    mPostAble = true;
                    mDialogBuilder.progressDialog.dismiss();
                    setResult(RESULT_OK);
                    finish();
                    break;
            }
        }
    };

    @Override
    @OnClick({R.id.fl_man, R.id.fl_women, R.id.title_bar_left_menu})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_women:
                postUpdateGender(AppEnum.UserGander.WOMEN);
                break;
            case R.id.fl_man:
                postUpdateGender(AppEnum.UserGander.MAN);
                break;
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }

    @Override
    protected void initData() {
        mUserEntity = LocalApplication.getInstance().getLoginUser(mContext);
    }

    @Override
    protected void initViews() {
        if (mUserEntity.getGender() == AppEnum.UserGander.WOMEN) {
            mManIv.setVisibility(View.GONE);
            mWomenIv.setVisibility(View.VISIBLE);
        } else {
            mManIv.setVisibility(View.VISIBLE);
            mWomenIv.setVisibility(View.GONE);
        }
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
     * 修改用户性别
     *
     * @param gender
     */
    private void postUpdateGender(final int gender) {
        if (!mPostAble) {
            return;
        }
        showProgressDialog("正在更新···");

        if (gender == AppEnum.UserGander.WOMEN) {
            mManIv.setVisibility(View.GONE);
            mWomenIv.setVisibility(View.VISIBLE);
        } else {
            mManIv.setVisibility(View.VISIBLE);
            mWomenIv.setVisibility(View.GONE);
        }

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_USER_INFO;
        RequestParams params = RequestParamsBuild.buildResetUserGenderRequest(mContext,gender);
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
                mUserEntity.setGender(gender);
                UserData.updateUser(mContext,mUserEntity);
                LocalApplication.getInstance().setLoginUser(mUserEntity);
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_GENDER_OK;
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
    private void showProgressDialog(String msg) {
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
            mPostHandler.removeMessages(MSG_POST_GENDER_OK);
            mPostHandler.removeMessages(MSG_POST_ERROR);
        }
    }
}
