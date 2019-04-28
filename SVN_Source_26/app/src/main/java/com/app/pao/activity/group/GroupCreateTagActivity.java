package com.app.pao.activity.group;

import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by Administrator on 2016/1/10.
 */
@ContentView(R.layout.activity_group_create_tag)
public class GroupCreateTagActivity extends BaseAppCompActivity implements View.OnClickListener{

    /* contains */
    private static final String TAG = "GroupCreateTagActivity";
    private static final int MSG_POST_OK = 0;
    private static final int MSG_POST_ERROR = 1;

    /* local view */
    @ViewInject(R.id.et_tag_name)
    private EditText mTagNameEt;

    /* local data */
    private String mTagName;
    private int mGroupId;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //若返回ok
            if(msg.what == MSG_POST_OK){
                T.showShort(mContext,"创建成功。");
                finish();

                //否则
            }else if(msg.what == MSG_POST_ERROR) {
                T.showShort(mContext,msg.obj.toString());
            }
        }
    };

    @Override
    @OnClick({R.id.btn_create_tag,R.id.title_bar_left_menu})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.title_bar_left_menu:
                finish();
            break;

            case R.id.btn_create_tag:
                createTag();
            break;
        }
    }


    @Override
    protected void initData() {
        mGroupId = getIntent().getIntExtra("groupId",0);
    }

    @Override
    protected void initViews() {
        mTagNameEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    createTag();
                }
                return false;
            }
        });
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
        cancelHandler();
    }

    private void cancelHandler(){
        if(handler != null){
            handler.removeMessages(MSG_POST_ERROR);
            handler.removeMessages(MSG_POST_OK);
        }
    }

    /**\
     * 开始创建标签
     */
    private void createTag(){
        mTagName = mTagNameEt.getText().toString();
        if(mTagName == null || mTagName.isEmpty()){
            mTagNameEt.setError("请输入标签名称");
            return;
        }

        mDialogBuilder.showProgressDialog(mContext,"请稍后..",false);

        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_CREATE_LABEL;
        RequestParams params = RequestParamsBuild.buildCreateGroupLabel(mContext,mGroupId,mTagName);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if(handler != null){
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if(handler != null){
                    Message msg = new Message();
                    msg.what = MSG_POST_OK;
                    msg.obj = Response;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if(handler != null){
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {
                if(mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()){
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }


}
