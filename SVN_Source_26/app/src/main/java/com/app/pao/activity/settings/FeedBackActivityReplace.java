package com.app.pao.activity.settings;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.TalkBetweenBgAndUserAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.OpinionFromUser;
import com.app.pao.entity.network.TalkBetweenBgAndUser;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LY on 2016/3/21.
 */
@ContentView(R.layout.activity_feedback_replace)
public class FeedBackActivityReplace extends BaseAppCompActivity implements View.OnClickListener{


    private static final String TAG ="FeedBackActivityReplace";

    private static final int GET_COMMENT_ERROR = 0 ;
    private static final int GET_COMMENT_OK = 1 ;
    private static final int SEND_COMMENT_ERROR = 2;
    private static final int SEND_COMMENT_OK = 3 ;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadLl;
    @ViewInject(R.id.lv_load)
    private LoadingView mLoadLv;
    @ViewInject(R.id.tv_reload)
    private TextView mReLoadTv;
    @ViewInject(R.id.toolbar)
    private Toolbar mToolBar;//标题栏
    @ViewInject(R.id.rv_user_opinion)
    private RecyclerView mTalkRv;//通话记录
    @ViewInject(R.id.et_user_opinion_input)
    private EditText mUserSendEt;//用户输入框

    private List<OpinionFromUser.CommentsBean> m0pinionFromUsersList;
    private List<TalkBetweenBgAndUser> mTalkList;
    private TalkBetweenBgAndUserAdapter mAdapter;
    private String feedBack;

    private Handler mPostHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == GET_COMMENT_ERROR){
                if(mLoadLl.getVisibility() == View.VISIBLE){
                    mLoadLv.setLoadingText("加载失败");
                    mReLoadTv.setVisibility(View.VISIBLE);
                }
            }else if(msg.what == GET_COMMENT_OK){
                updateViewByGetComment();
                mLoadLv.setVisibility(View.GONE);
                mLoadLl.setVisibility(View.GONE);
            }else if(msg.what == SEND_COMMENT_ERROR){
                mDialogBuilder.progressDialog.dismiss();
            }else if(msg.what == SEND_COMMENT_OK){
                mDialogBuilder.progressDialog.dismiss();
                submitCommentSuccess();
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
    @OnClick({R.id.tv_user_opinion_submit, R.id.tv_reload})
    public void onClick(View v) {
        if(v.getId() == R.id.tv_user_opinion_submit){
            submitFeedBack();
        }if(v.getId() == R.id.tv_reload){
            getTalkContentResult();
            mLoadLv.setLoadingText("加载中...");
            mReLoadTv.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        m0pinionFromUsersList = new ArrayList<>();
        mTalkList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolBar);
        mAdapter = new TalkBetweenBgAndUserAdapter(mTalkList,mContext);
        mTalkRv.setLayoutManager(new LinearLayoutManager(mContext));
        mTalkRv.setAdapter(mAdapter);

        //监听编辑框的Action
        mUserSendEt.setOnEditorActionListener(new android.widget.TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView v, int actionId, KeyEvent event) {
                if (!mUserSendEt.getText().toString().equals("")) {
                    submitFeedBack();
                }
                return true;
            }
        });
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        getTalkContentResult();
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelPostHandler();
    }

    /**
     * 从后台获取用户与后台的对话
     */
    private void getTalkContentResult(){
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_COMMENT_LIST;
        RequestParams params = RequestParamsBuild.getCommentListRequest(mContext);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    T.showShort(mContext,errorMsg);
                    mPostHandler.sendEmptyMessage(GET_COMMENT_ERROR);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
//                Log.i("TAG", "Response:" + Response);
                if (m0pinionFromUsersList != null) {
                    m0pinionFromUsersList.clear();
                }
                if (mTalkList != null) {
                    mTalkList.clear();
                }
                m0pinionFromUsersList.addAll(JSON.parseObject(Response, OpinionFromUser.class).getComments());
                mTalkList.add(new TalkBetweenBgAndUser(null, "欢迎你提出宝贵意见和建议", AppEnum.TalkBetweenBgAndUser.BACKGROUND));
                for (OpinionFromUser.CommentsBean user : m0pinionFromUsersList) {
                    String comment = user.getComment();
                    String commentDate = getFriendlyTime(user.getDate());
                    mTalkList.add(new TalkBetweenBgAndUser(commentDate, comment, AppEnum.TalkBetweenBgAndUser.USER));
                    List<OpinionFromUser.CommentsBean.ResponseBean> responseList = user.getResponse();
                    for (OpinionFromUser.CommentsBean.ResponseBean response : responseList) {
                        String respo = response.getResponse();
                        String respoDate = getFriendlyTime(response.getDate());
                        mTalkList.add(new TalkBetweenBgAndUser(respoDate, respo, AppEnum.TalkBetweenBgAndUser.BACKGROUND));
                    }
                }

                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(GET_COMMENT_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    T.showShort(mContext,s);
                    mPostHandler.sendEmptyMessage(GET_COMMENT_ERROR);
                }
            }

            @Override
            protected void onFinish() {
            }
        });
    }

    /**
     * 获得对话列表后的操作
     */
    private void updateViewByGetComment(){
        mAdapter.notifyDataSetChanged();
        mTalkRv.scrollToPosition(mTalkList.size() - 1);
    }

    /**
     * 提交意见或者建议的操作
     */
    private void submitFeedBack(){
        feedBack = mUserSendEt.getText().toString();

        if( feedBack.isEmpty()){
            T.showShort(mContext,getResources().getString(R.string.Helper_FeedbackActivity));
            return;
        }
//        String feedbackStr ="版本信息：\n"+URLConfig.USER_ANGENT+" \n 反馈内容：\n"+ feedBack;
        //发送验证码请求提示
        mDialogBuilder.showProgressDialog(this, getResources().getString(R.string.Load_waiting), true);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PUBLISH_COMMENT;
        RequestParams params = RequestParamsBuild.buildSubmitFeedbackRequest(mContext,feedBack);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = SEND_COMMENT_ERROR;
                    mPostMsg.obj = errorMsg;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = SEND_COMMENT_OK;
                    mPostMsg.obj = Response;
                    mPostHandler.sendMessage(mPostMsg);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message mPostMsg = new Message();
                    mPostMsg.what = SEND_COMMENT_ERROR;
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
     * 意见提交成功后的操作
     */
    private void submitCommentSuccess(){
        mUserSendEt.setText("");
        String date = TimeUtils.getTimestampString(new Date());
        mTalkList.add(new TalkBetweenBgAndUser(date,feedBack,AppEnum.TalkBetweenBgAndUser.USER));
        mAdapter.notifyDataSetChanged();
        mTalkRv.scrollToPosition(mTalkList.size() - 1);
    }

    /**
     * 取消请求
     */
    private void cancelPostHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(GET_COMMENT_OK);
            mPostHandler.removeMessages(GET_COMMENT_ERROR);
            mPostHandler.removeMessages(SEND_COMMENT_OK);
            mPostHandler.removeMessages(SEND_COMMENT_ERROR);
        }
    }

    private String getFriendlyTime(String time){
        Date date = TimeUtils.stringToDate(time);
        String hourAndMin = TimeUtils.getDynamicDayTime(time);
        String isToday = TimeUtils.getTimestampDayString(date);
        return isToday+" "+hourAndMin;
    }
}
