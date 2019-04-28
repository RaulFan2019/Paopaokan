package com.app.pao.activity.settings;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.config.URLConfig;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by Administrator on 2015/12/2.
 */
@ContentView(R.layout.activity_feedback)
public class FeedbcakActivity extends BaseAppCompActivity implements View.OnClickListener{

    private static final int MSG_POST_ERROR = 0;

    private static final int MSG_POST_OK = 1;

    @ViewInject(R.id.et_feedback)
    private EditText mFeedBackEt;
    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏


    Handler mPostHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_POST_ERROR:
                    T.showShort(mContext, (String) msg.obj);
                    mDialogBuilder.progressDialog.dismiss();
                    break;
                case MSG_POST_OK:
                    T.showShort(mContext, getResources().getString(R.string.Toast_FeedbackActivity));
                    mDialogBuilder.progressDialog.dismiss();
                    finish();
                    break;
            }
        }
    };

    /**
     * Toolbar 点击事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    @OnClick(R.id.btn_submit_feedback)
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit_feedback:
                submitFeedBack();
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
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
     * 检查并发送意见
     */
    private void submitFeedBack(){
        String feedbackStr = mFeedBackEt.getText().toString();

        if( feedbackStr.isEmpty()){
            T.showShort(mContext,getResources().getString(R.string.Helper_FeedbackActivity));
            return;
        }
        feedbackStr ="版本信息：\n"+URLConfig.USER_ANGENT+" \n 反馈内容：\n"+ feedbackStr;
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, getResources().getString(R.string.Load_waiting), false);
        mDialogBuilder.progressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                cancelPostHandler();
            }
        });

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PUBLISH_COMMENT;
        RequestParams params = RequestParamsBuild.buildSubmitFeedbackRequest(mContext,feedbackStr);
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
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = MSG_POST_OK;
                    mPostMsg.obj = Response;
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

    /**
     * 取消请求
     */
    private void cancelPostHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
    }

}
