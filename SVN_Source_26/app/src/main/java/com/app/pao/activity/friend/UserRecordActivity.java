package com.app.pao.activity.friend;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.workout.FriendHistoryInfoActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetPersonRecordResult;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Raul on 2015/12/3.
 * 个人纪录
 */
@ContentView(R.layout.activity_user_record)
public class UserRecordActivity extends BaseAppCompActivity implements View.OnClickListener {

    /* contains */
    private static final String TAG = "UserRecordActivity";

    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;

    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.tv_5fast_value)
    private TextView m5FastVTv;//5公里最快文本
    @ViewInject(R.id.tv_5fast_time)
    private TextView m5FastTimeTv;//5公里最快时间文本
    @ViewInject(R.id.tv_10fast_value)
    private TextView m10FastVTv;//10公里最快文本
    @ViewInject(R.id.tv_10fast_time)
    private TextView m10FastTimeTv;//10公里最快时间文本
    @ViewInject(R.id.tv_halffast_value)
    private TextView mHalfFastVTv;//半程马拉松公里最快文本
    @ViewInject(R.id.tv_halffast_time)
    private TextView mHalfFastTimeTv;//半程马拉松最快时间文本
    @ViewInject(R.id.tv_holefast_value)
    private TextView mHoleFastVTv;//全程马拉松最快文本
    @ViewInject(R.id.tv_holefast_time)
    private TextView mHoleFastTimeTv;//全程马拉松最快时间文本

    /* local data */
    private int mUserid;//用户id
    private GetPersonRecordResult mPersonRecordEntity;//跑步信息
    private int mIsFriend;//是否是好友
    private String mNickName;//昵称
    private int mAge;//年龄
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
    /**
     * 请求返回处理
     */
    Handler mPostHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //发送请求错误
                case MSG_POST_ERROR:
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    T.showShort(mContext, (String) msg.obj);
                    break;
                //刷新成功
                case MSG_POST_OK:
                    updateViewByPostResult();
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
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
    protected void initData() {
        mUserid = getIntent().getExtras().getInt("userid");
        mIsFriend = getIntent().getExtras().getInt("isFriend");
        mAge = getIntent().getExtras().getInt("Age");
        mNickName = getIntent().getExtras().getString("nickname");

    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
    }

    @Override
    protected void doMyOnCreate() {
        postGetUserRunInfo();
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

    /**
     * 获取用户的跑步记录信息
     */
    private void postGetUserRunInfo() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PERSON_RECORDS;
        RequestParams params = RequestParamsBuild.buildGetPersonRecordRequest(mContext,mUserid);
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
                mPersonRecordEntity = JSON.parseObject(Response, GetPersonRecordResult.class);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
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
     * 更新用户纪录信息
     */
    private void updateViewByPostResult() {
        //5公里最快
        if (mPersonRecordEntity.getFastest5().getLength() == 0) {
            m5FastVTv.setText("未完成");
            m5FastTimeTv.setText("未完成");
        } else {
            m5FastVTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastest5().getDuration()));
            try {
                m5FastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity.getFastest5()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //10公里最快
        if (mPersonRecordEntity.getFastest10().getLength() == 0) {
            m10FastVTv.setText("未完成");
            m10FastTimeTv.setText("未完成");
        } else {
            m10FastVTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastest10().getDuration()));
            try {
                m10FastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity.getFastest10()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //半程最快
        if (mPersonRecordEntity.getFastesthalfmarathon().getLength() == 0) {
            mHalfFastVTv.setText("未完成");
            mHalfFastTimeTv.setText("未完成");
        } else {
            mHalfFastVTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastesthalfmarathon().getDuration
                    ()));
            try {
                mHalfFastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity
                        .getFastesthalfmarathon()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //全程最快
        if (mPersonRecordEntity.getFastestfullmarathon().getLength() == 0) {
            mHoleFastVTv.setText("未完成");
            mHoleFastTimeTv.setText("未完成");
        } else {
            mHoleFastVTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastestfullmarathon().getDuration
                    ()));
            try {
                mHoleFastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity
                        .getFastestfullmarathon()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消Handler
     */
    private void cancelHandler() {
        if (mPostHandler != null) {
            mPostHandler.removeMessages(MSG_POST_ERROR);
            mPostHandler.removeMessages(MSG_POST_OK);
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    @OnClick({R.id.ll_5_fast, R.id.ll_10_fast, R.id.ll_half_fast, R.id.ll_full_fast})
    public void onClick(View v) {
        if (mIsFriend != AppEnum.IsFriend.FRIEND
                && mUserid != LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
            T.showShort(mContext, "你们还不是好友");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("mAge", mAge);
        bundle.putString("nickname", mNickName);
        switch (v.getId()) {
            //最快5公里
            case R.id.ll_5_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastest5().getLength() != 0) {
                    bundle.putString("workoutname", mPersonRecordEntity.getFastest5().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastest5().getWorkoutid());
                    startActivity(FriendHistoryInfoActivityV2.class, bundle);
                }
                break;
            //最快10公里
            case R.id.ll_10_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastest10().getLength() != 0) {
                    bundle.putString("workoutname", mPersonRecordEntity.getFastest10().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastest10().getWorkoutid());
                    startActivity(FriendHistoryInfoActivityV2.class, bundle);
                }
                break;
            //最快半程
            case R.id.ll_half_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastesthalfmarathon().getLength() != 0) {
                    bundle.putString("workoutname", mPersonRecordEntity.getFastesthalfmarathon().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastesthalfmarathon().getWorkoutid());
                    startActivity(FriendHistoryInfoActivityV2.class, bundle);
                }
                break;
            //最快全程
            case R.id.ll_full_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastestfullmarathon().getLength() != 0) {
                    bundle.putString("workoutname", mPersonRecordEntity.getFastestfullmarathon().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastestfullmarathon().getWorkoutid());
                    startActivity(FriendHistoryInfoActivityV2.class, bundle);
                }
                break;
        }
    }
}
