package com.app.pao.activity.friend;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ZoomBigImageActivity;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.activity.settings.UserLevelCardActivity;
import com.app.pao.activity.workout.FriendHistoryInfoActivityV2;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.activity.workout.MyClockListActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.model.UserOptMedalEntity;
import com.app.pao.entity.network.GetPersonRecordResult;
import com.app.pao.entity.network.GetUserInfoResult;
import com.app.pao.fragment.title.menu.TitleMenuFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.ui.widget.pulltozoomview.PullToZoomScrollViewEx;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnTouch;
import com.rey.material.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/17.
 */
@ContentView(R.layout.activity_user_info)
public class UserInfoActivity extends BaseAppCompActivity implements View.OnClickListener, View.OnTouchListener {

    /* contains */
    private static final int MSG_POST_ERROR = 1;
    private static final int MSG_POST_OK = 3;

    /* local view */
    @ViewInject(R.id.sv_local_user_base)
    private PullToZoomScrollViewEx mScorllView;
    @ViewInject(R.id.ll_title)
    public LinearLayout mTitleLl;
    private View headView, zoomView, contentView;
    private BitmapUtils mBitmapUtils;
    @ViewInject(R.id.ibtn_group_more)
    private ImageButton mMoneMenuIb;

    /*headView*/
    private TextView mCardTypeTv;//卡片类型文本
    private TextView mFriendTitleTv;//好友标题文本
    private TextView mFriendNumTv;//好友数量文本
    private TextView mGroupTitleTv;//跑团标题文本
    private TextView mGroupNumTv;//跑团数量文本
    private CircularImage mUserAvatarCi;//用户头像
    private LinearLayout mCardTypeLl;//卡片类型框，用于实现查看卡片详情的点击事件
    private TextView mNickNameTv;//用户昵称文本
    private LinearLayout mClockRemindLl;//提醒闹钟布局
    private LinearLayout mFriendLl;//好友布局
    private LinearLayout mGroupLl;//跑团布局

    /*zoomView*/
    private ImageView mRunCardIv;//拉伸图片

    /* content view */
    private LinearLayout mClockView;//闹钟布局
    private TextView mClockNumTv;//闹钟数量文本
    private TextView mAlarmTv;//闹钟提醒文本
    private LinearLayout mNotFriendLl;//非好友布局
    private Button mFriendControlBtn;//关系按钮
    private LinearLayout mTotalRunCountLl;//跑步总次数布局
    private TextView mTotalRunCountTv;//总跑步数量文本
    private TextView mTotalDistanceTv;//总里程文本
    private TextView mWeekAverageTv;//平均周跑量文本
    private TextView mTotalCalorieTv;//总消耗文本
    private LinearLayout mAllMedalLl;//奖牌布局
    private LinearLayout mNoMedalLl;//无奖牌布局
    private TextView mMedalTitleTv;//奖牌标题文本
    private LinearLayout mRunningLl;//正在跑步布局
    private TextView mRunningLengthTv;//正在跑步距离
    private ImageView mAlarmArrowIv;//箭头
    private ImageView mRunTimeArrowIv;//跑步次数箭头

    /* local view about load */
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private com.rey.material.widget.TextView mReladV;

    /* local data */
    private int mUserId;//用户id
    private GetUserInfoResult mUserInfo;//用户信息
    private GetPersonRecordResult mPersonRecordEntity;//跑步信息
    private Typeface typeface;//用于设置字体类型

    /* local data about menu */
    private TitleMenuFragment mMenuFragment;//菜单栏
    private FragmentManager fragmentManager;//Fragment管理器
    private boolean isShowMessage;
    private boolean isFirstIn = true;//是否是第一次进入


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
                    T.showShort(mContext, (String) msg.obj);
                    mLoadV.setLoadingText("加载失败");
                    if (isFirstIn) {
                        mReladV.setVisibility(View.VISIBLE);
                        mScorllView.setVisibility(View.INVISIBLE);
                    }
                    break;
                //刷新成功
                case MSG_POST_OK:
                    updateViewByPostResult();
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
                    mScorllView.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    @OnClick({R.id.title_bar_left_menu, R.id.ibtn_group_more, R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.ibtn_group_more:
                showManagerTitleMenu();
                break;
            case R.id.tv_reload:
                getUserInfo();
                break;
        }
    }

    @Override
    protected void initData() {
        //获取好友信息
        mUserId = getIntent().getExtras().getInt("userid");
        mBitmapUtils = new BitmapUtils(mContext);
        fragmentManager = getSupportFragmentManager();
        isShowMessage = false;
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
    }

    @Override
    protected void initViews() {
        inflaterViews();
        initScrollViews();
        setHeaderUI();
        setZoomUI();
        setContentUi();
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        getUserInfo();
        if (isFirstIn) {
            isFirstIn = false;
        }
    }

    /**
     * 监听消息显示框的点击事件，防止消息打开并点击时下层相应点击的问题
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    @OnTouch({R.id.ll_fragment_message})
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.ll_fragment_message) {
            //判断团菜单是否展开
            if (mMenuFragment != null && mMenuFragment.isVisible()) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                transaction.remove(mMenuFragment).commit();
                isShowMessage = !isShowMessage;
                return true;
            }
        }
        return isShowMessage;
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        cancelHandler();
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


    /*-- post fun --*/

    /**
     * 获取用户个人信息
     */
    private void getUserInfo() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_USER_INFO;
        RequestParams params = RequestParamsBuild.buildGetUserInfoRequest(mContext, mUserId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                mUserInfo = JSON.parseObject(Response, GetUserInfoResult.class);
                postGetUserRunInfo();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (mPostHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    mPostHandler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 获取用户的跑步记录信息
     */
    private void postGetUserRunInfo() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PERSON_RECORDS;
        RequestParams params = RequestParamsBuild.buildGetPersonRecordRequest(mContext, mUserId);
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
     * 添加闹钟
     */
    private void increaseClock() {
        MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                AppEnum.MaidianType.UserInfoClockBtn, TimeUtils.NowTime()));
        mDialogBuilder.showProgressDialog(mContext, "发送跑步提醒..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_INCREASE_CLOCK;
        RequestParams params = RequestParamsBuild.buildIncreaseClock(mContext, mUserId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "提醒成功");
                mUserInfo.clockcount++;
                mClockNumTv.setText("已收到" + mUserInfo.clockcount + "个跑步提醒");
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                mDialogBuilder.progressDialog.dismiss();
            }
        });
    }

    /**
     * 同意对方好友请求
     */
    private void postAgreeFriend() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }

        mDialogBuilder.showProgressDialog(mContext, "处理中,请稍后...", false);
        // 发送好友请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_APPROVE_ADD_FRIEND;
        RequestParams params = RequestParamsBuild.buildApproveAddFriend(mContext, mUserInfo.getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                //处理成功后，重新获取刷新数据
                mUserInfo.setIsFriend(AppEnum.IsFriend.FRIEND);
                updateViewByPostResult();
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 向对方发送好友请求
     */
    private void addFriend() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }

        mDialogBuilder.showProgressDialog(mContext, "正在向对方发送请求..", false);
        // 发送好友请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_APPLY_ADD_FRIEND;
        RequestParams params = RequestParamsBuild.BuildAddFriendParams(mContext, mUserInfo.getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {


            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "请求已发送");
                mFriendControlBtn.setText("已申请");
                mFriendControlBtn.setEnabled(false);
                mFriendControlBtn.setTextColor(Color.parseColor("#888888"));
                mFriendControlBtn.setBackground(null);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /*-- UI --*/
    private void inflaterViews() {
        headView = LayoutInflater.from(mContext).inflate(R.layout.header_user_info, null, false);
        zoomView = LayoutInflater.from(mContext).inflate(R.layout.zoom_local_user, null, false);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.content_user_info, null, false);
    }

    private void initScrollViews() {
        mScorllView.setHeaderView(headView);
        mScorllView.setZoomView(zoomView);
        mScorllView.setScrollContentView(contentView);
//        mScorllView.setZoomEnabled(false);//用于设置Zoom部分是否可以实现阻尼效果，true为可以，false为不可以，默认为true
        mScorllView.setParallax(false);//用于设置Zoom部分和Content部分位置关联
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        final int mScreenHeight = localDisplayMetrics.heightPixels;
        final int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (5.0F * (mScreenWidth / 6.0F)));

        mScorllView.setHeaderLayoutParams(localObject);
        mScorllView.setShowTitleColorListener(new PullToZoomScrollViewEx.ShowTitleColorListener() {

            @Override
            public void scrollYChange(int y) {
                float alpha = y / 600.0f;
                mTitleLl.setAlpha(alpha);
            }
        });
    }

    private void setHeaderUI() {
        mNickNameTv = (TextView) headView.findViewById(R.id.tv_nickname);
        mCardTypeTv = (TextView) headView.findViewById(R.id.tv_card_type);
        mFriendNumTv = (TextView) headView.findViewById(R.id.tv_friend_num);
        mFriendTitleTv = (TextView) headView.findViewById(R.id.tv_friend_friends_title);
        mGroupNumTv = (TextView) headView.findViewById(R.id.tv_group_num);
        mGroupTitleTv = (TextView) headView.findViewById(R.id.tv_friend_groups_title);
        mUserAvatarCi = (CircularImage) headView.findViewById(R.id.civ_user_avatar);
        mCardTypeLl = (LinearLayout) headView.findViewById(R.id.ll_card_type);
        mFriendLl = (LinearLayout) headView.findViewById(R.id.ll_friend);
        mGroupLl = (LinearLayout) headView.findViewById(R.id.ll_group);
        mUserAvatarCi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", mUserInfo.avatar);
                startActivity(ZoomBigImageActivity.class, bundle);
            }
        });
        mCardTypeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("avatar", mUserInfo.avatar);
                bundle.putString("nickname", mUserInfo.nickname);
                bundle.putFloat("totallength", mPersonRecordEntity.totallength);
                startActivity(UserLevelCardActivity.class, bundle);
            }
        });
        mFriendLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo.isFriend == AppEnum.IsFriend.FRIEND) {
                    launchFriendList();
                } else {
                    launchSameFriendList();
                }

            }
        });
        mGroupLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGroupList();
            }
        });
    }

    /**
     * 关联Zoom部分的UI和点击事件
     */
    private void setZoomUI() {
        mRunCardIv = (ImageView) zoomView.findViewById(R.id.iv_run_card);
    }

    /**
     * 关联Content部分的UI和点击事件
     */
    private void setContentUi() {
        mRunningLl = (LinearLayout) contentView.findViewById(R.id.ll_running);
        mRunningLengthTv = (TextView) contentView.findViewById(R.id.tv_running_length);
        mClockView = (LinearLayout) contentView.findViewById(R.id.ll_user_alarm);
        mClockNumTv = (TextView) contentView.findViewById(R.id.tv_user_alarm_num);
        mClockRemindLl = (LinearLayout) contentView.findViewById(R.id.ll_clock_remind);
        mAlarmTv = (TextView) contentView.findViewById(R.id.tv_alarm);
        mAlarmArrowIv = (ImageView) contentView.findViewById(R.id.iv_alarm_arrow);
        mRunTimeArrowIv = (ImageView) contentView.findViewById(R.id.iv_runtime_arrow);
        mNotFriendLl = (LinearLayout) contentView.findViewById(R.id.ll_not_friend);
        mFriendControlBtn = (Button) contentView.findViewById(R.id.btn_add_friend);
        mTotalRunCountLl = (LinearLayout) contentView.findViewById(R.id.ll_total_run_count);
        mTotalRunCountTv = (TextView) contentView.findViewById(R.id.tv_total_run_count);
        mTotalDistanceTv = (TextView) contentView.findViewById(R.id.tv_user_total_distance);
        mTotalDistanceTv.setTypeface(typeface);
        mWeekAverageTv = (TextView) contentView.findViewById(R.id.tv_user_week_average_mileage);
        mWeekAverageTv.setTypeface(typeface);
        mTotalCalorieTv = (TextView) contentView.findViewById(R.id.tv_user_total_calorie);
        mTotalCalorieTv.setTypeface(typeface);
        mMedalTitleTv = (TextView) contentView.findViewById(R.id.tv_medal_title);
        mAllMedalLl = (LinearLayout) contentView.findViewById(R.id.ll_obt_medal);
        mNoMedalLl = (LinearLayout) contentView.findViewById(R.id.ll_no_medal);
        mClockView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo.clockcount > 0) {
                    launchClockAct();
                }
            }
        });
        mClockRemindLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseClock();
            }
        });
        mTotalRunCountLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchDynamicList();
            }
        });
        mFriendControlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo.getHasSendApply() == AppEnum.HasApply.REV_APPLY) {
                    postAgreeFriend();
                } else if (mUserInfo.hasSendApply == AppEnum.HasApply.APPLY) {
                    //DO NOTHING
                } else {
                    addFriend();
                }
            }
        });
        mRunningLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserInfo != null) {
                    Intent intent = new Intent(mContext, LiveActivityV3.class);
                    intent.putExtra("userId", mUserId);
                    intent.putExtra("userNickName", mUserInfo.nickname);
                    intent.putExtra("userGender", mUserInfo.gender);
                    intent.putExtra("avatar", mUserInfo.avatar);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    private void updateViewByPostResult() {
        updateUserViews();
        updateClockViews();
        updateMedalView();
        //更新性别词语
        if (mUserInfo.gender == AppEnum.UserGander.MAN) {
            mFriendTitleTv.setText("他的好友");
            mGroupTitleTv.setText("他的跑团");
            mMedalTitleTv.setText("他的奖牌");
            mAlarmTv.setText("提醒他");
        } else {
            mFriendTitleTv.setText("她的好友");
            mGroupTitleTv.setText("她的跑团");
            mMedalTitleTv.setText("她的奖牌");
            mAlarmTv.setText("提醒她");
        }
        if (mUserInfo.isFriend != AppEnum.IsFriend.FRIEND) {
            mFriendTitleTv.setText("共同好友");
            mRunTimeArrowIv.setVisibility(View.GONE);
        } else {
            mRunTimeArrowIv.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 更新用户相关布局
     */
    private void updateUserViews() {
        if (mUserInfo.getIsFriend() == AppEnum.IsFriend.FRIEND) {
            mMoneMenuIb.setVisibility(View.VISIBLE);
            if (mUserInfo.isrunning) {
                mRunningLl.setVisibility(View.VISIBLE);
                mRunningLengthTv.setTypeface(typeface);
                mRunningLengthTv.setText("已跑了" + NumUtils.retainTheDecimal(mUserInfo.length) + "公里...");
            }
        } else {
            mMoneMenuIb.setVisibility(View.GONE);
            if (mUserInfo.getHasSendApply() == AppEnum.HasApply.APPLY) {
                mFriendControlBtn.setText("已申请");
                mFriendControlBtn.setEnabled(false);
                mFriendControlBtn.setTextColor(Color.parseColor("#888888"));
                mFriendControlBtn.setBackground(null);
            } else if (mUserInfo.getHasSendApply() == AppEnum.HasApply.REV_APPLY) {
                mFriendControlBtn.setText("同意");
                mFriendControlBtn.setTextColor(Color.parseColor("#3c3b39"));
                mFriendControlBtn.setBackgroundResource(R.drawable.btn_add_friend);
                mFriendControlBtn.setEnabled(true);
            } else {
                mFriendControlBtn.setText("加好友");
                mFriendControlBtn.setTextColor(Color.parseColor("#3c3b39"));
                mFriendControlBtn.setBackgroundResource(R.drawable.btn_add_friend);
                mFriendControlBtn.setEnabled(true);
            }
        }
        ImageUtils.loadUserImage(mUserInfo.avatar, mUserAvatarCi);
        mNickNameTv.setText(mUserInfo.nickname);
        float length = mPersonRecordEntity.totallength;
        mCardTypeTv.setText(NumUtils.getCardTypeString(length) + "行者");
        mGroupNumTv.setText(mUserInfo.rungroupcount + "");
        if (mUserInfo.isFriend == AppEnum.IsFriend.FRIEND) {
            mFriendNumTv.setText(mUserInfo.friendcount + "");
        } else {
            mFriendNumTv.setText(mUserInfo.commonfriendcount + "");
        }

        mRunCardIv.setImageResource(NumUtils.parseBgFromLength(length));

        mTotalRunCountTv.setText(mPersonRecordEntity.getTotalcount() + "");
        mTotalDistanceTv.setText(NumUtils.retainTheDecimal(mPersonRecordEntity.getTotallength()));
        mWeekAverageTv.setText(NumUtils.retainTheDecimal(mPersonRecordEntity.getWeeklength()));
        mTotalCalorieTv.setText(NumUtils.retainTheDecimal(mPersonRecordEntity.getTotalcalorie() * 1000));
    }

    /**
     * 更新闹钟界面
     */
    private void updateClockViews() {
        if (mUserInfo.getIsFriend() == AppEnum.IsFriend.FRIEND) {
            mClockView.setVisibility(View.VISIBLE);
            mNotFriendLl.setVisibility(View.GONE);
        } else {
            mClockView.setVisibility(View.GONE);
            mNotFriendLl.setVisibility(View.VISIBLE);
        }
        if (mUserInfo.clockcount > 0) {
            mAlarmArrowIv.setVisibility(View.VISIBLE);
            mClockNumTv.setText("已收到" + mUserInfo.clockcount + "个跑步提醒");
        } else {
            mAlarmArrowIv.setVisibility(View.GONE);
            mClockNumTv.setText("还未收到跑步提醒");
        }

    }

    /**
     * 更新奖牌页面
     */
    private void updateMedalView() {
        List<UserOptMedalEntity> mMedalList = new ArrayList<>();
        List<UserOptMedalEntity> mChangeMedalList = new ArrayList<>();
        //移除上次UI
        mAllMedalLl.removeAllViews();
        //将数据重新插入到list中，便于操作
        //最长距离
        if (mPersonRecordEntity.getLongest().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getLongest(), UserOptMedalEntity.TYPE_LONGEST);
            mMedalList.add(mMedalEntity);

        }
        //最长时间
        if (mPersonRecordEntity.getMaxduration().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getMaxduration(), UserOptMedalEntity.TYPE_MAX_DURATION);
            mMedalList.add(mMedalEntity);

        }
        //最快配速
        if (mPersonRecordEntity.getFastestpace().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastestpace(), UserOptMedalEntity.TYPE_FASTEST_PACE);
            mMedalList.add(mMedalEntity);

        }
        //5公里最快
        if (mPersonRecordEntity.getFastest5().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastest5(), UserOptMedalEntity.TYPE_FASTEST_5);
            mMedalList.add(mMedalEntity);

        }
        //10公里最快
        if (mPersonRecordEntity.getFastest10().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastest10(), UserOptMedalEntity.TYPE_FASTEST_10);
            mMedalList.add(mMedalEntity);

        }
        //半马最快
        if (mPersonRecordEntity.getFastesthalfmarathon().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastesthalfmarathon(), UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON);
            mMedalList.add(mMedalEntity);

        }
        //全马最快
        if (mPersonRecordEntity.getFastestfullmarathon().getWorkoutid() != 0) {
            UserOptMedalEntity mMedalEntity = new UserOptMedalEntity(mPersonRecordEntity.getFastestfullmarathon(), UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON);
            mMedalList.add(mMedalEntity);

        }

        //若数据为0 则无符合奖牌
        if (mMedalList.size() <= 0) {
            mNoMedalLl.setVisibility(View.VISIBLE);
            mAllMedalLl.setVisibility(View.GONE);
            return;
        } else {
            mNoMedalLl.setVisibility(View.GONE);
            mAllMedalLl.setVisibility(View.VISIBLE);
        }

        //遍历数组开始根据数据在页面上添加View
        LinearLayout mLineLl = null;
        for (int i = 0; i < mMedalList.size(); i++) {
            //添加居中横向容器
            if (i == 0 || i == 3 || i == 6) {

                mLineLl = new LinearLayout(mContext);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                mLineLl.setGravity(Gravity.CENTER);
                mLineLl.setOrientation(LinearLayout.HORIZONTAL);
                mAllMedalLl.addView(mLineLl, lp);

                boolean addLine = false;
                if (mMedalList.size() % 3 == 0) {
                    if ((mMedalList.size() / 3 - 1) != i / 3) {
                        addLine = true;
                    }
                } else {
                    if (mMedalList.size() / 3 != i / 3) {
                        addLine = true;
                    }
                }
                if (addLine) {
                    View dividerV = new View(mContext);
                    dividerV.setBackgroundColor(Color.parseColor("#e7e7e7"));
                    LinearLayout.LayoutParams dlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
                    dividerV.setLayoutParams(dlp);
                    mAllMedalLl.addView(dividerV);
                }
            }
            //向横向居中容器中插入item；
            final UserOptMedalEntity mMedal = mMedalList.get(i);

            //初始化item布局参数
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.include_user_obt_medal, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.width = (int) (DeviceUtils.getScreenWidth() / 3);
            //初始化布局控件
            ImageView mMedalIv = (ImageView) v.findViewById(R.id.iv_medal);
            TextView mMedalNameTv = (TextView) v.findViewById(R.id.tv_medal_name);
            TextView mMedalValueTv = (TextView) v.findViewById(R.id.tv_medal_value);
            TextView mMedalUnitTv = (TextView) v.findViewById(R.id.tv_medal_unit);
            mMedalValueTv.setTypeface(typeface);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (mMedal.getMedalType()) {
                        case UserOptMedalEntity.TYPE_LONGEST:
                            Bundle bundleLongest = new Bundle();
                            if (mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                                bundleLongest.putString("workoutname", mPersonRecordEntity.getLongest().getStarttime());
                                bundleLongest.putInt("workoutid", mPersonRecordEntity.getLongest().getWorkoutid());
                                startActivity(HistoryInfoActivityV2.class, bundleLongest);
                            } else {
                                bundleLongest.putString("workoutname", mPersonRecordEntity.getLongest().getStarttime());
                                bundleLongest.putInt("workoutid", mPersonRecordEntity.getLongest().getWorkoutid());
                                bundleLongest.putString("nickname", mUserInfo.getNickname());
                                bundleLongest.putInt("Age", TimeUtils.getAgeFromBirthDay(mUserInfo.getBirthdate()));
                                bundleLongest.putString("avatar", mUserInfo.getAvatar());
                                startActivity(FriendHistoryInfoActivityV2.class, bundleLongest);
                            }
                            break;
                        case UserOptMedalEntity.TYPE_MAX_DURATION:
                            Bundle bundleDuration = new Bundle();
                            if (mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                                bundleDuration.putString("workoutname", mPersonRecordEntity.getMaxduration().getStarttime());
                                bundleDuration.putInt("workoutid", mPersonRecordEntity.getMaxduration().getWorkoutid());
                                startActivity(HistoryInfoActivityV2.class, bundleDuration);
                            } else {
                                bundleDuration.putString("workoutname", mPersonRecordEntity.getMaxduration().getStarttime());
                                bundleDuration.putInt("workoutid", mPersonRecordEntity.getMaxduration().getWorkoutid());
                                bundleDuration.putString("nickname", mUserInfo.getNickname());
                                bundleDuration.putInt("Age", TimeUtils.getAgeFromBirthDay(mUserInfo.getBirthdate()));
                                bundleDuration.putString("avatar", mUserInfo.getAvatar());
                                startActivity(FriendHistoryInfoActivityV2.class, bundleDuration);
                            }
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_PACE:
                            Bundle bundlePace = new Bundle();
                            if (mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                                bundlePace.putString("workoutname", mPersonRecordEntity.getFastestpace().getStarttime());
                                bundlePace.putInt("workoutid", mPersonRecordEntity.getFastestpace().getWorkoutid());
                                startActivity(HistoryInfoActivityV2.class, bundlePace);
                            } else {
                                bundlePace.putString("workoutname", mPersonRecordEntity.getFastestpace().getStarttime());
                                bundlePace.putInt("workoutid", mPersonRecordEntity.getFastestpace().getWorkoutid());
                                bundlePace.putString("nickname", mUserInfo.getNickname());
                                bundlePace.putInt("Age", TimeUtils.getAgeFromBirthDay(mUserInfo.getBirthdate()));
                                bundlePace.putString("avatar", mUserInfo.getAvatar());
                                startActivity(FriendHistoryInfoActivityV2.class, bundlePace);
                            }
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_5:
                            Bundle bundle5Fast = new Bundle();
                            if (mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                                bundle5Fast.putString("workoutname", mPersonRecordEntity.getFastest5().getStarttime());
                                bundle5Fast.putInt("workoutid", mPersonRecordEntity.getFastest5().getWorkoutid());
                                startActivity(HistoryInfoActivityV2.class, bundle5Fast);
                            } else {
                                bundle5Fast.putString("workoutname", mPersonRecordEntity.getFastest5().getStarttime());
                                bundle5Fast.putInt("workoutid", mPersonRecordEntity.getFastest5().getWorkoutid());
                                bundle5Fast.putString("nickname", mUserInfo.getNickname());
                                bundle5Fast.putInt("Age", TimeUtils.getAgeFromBirthDay(mUserInfo.getBirthdate()));
                                bundle5Fast.putString("avatar", mUserInfo.getAvatar());
                                startActivity(FriendHistoryInfoActivityV2.class, bundle5Fast);
                            }
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_10:
                            Bundle bundle10Fast = new Bundle();
                            if (mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                                bundle10Fast.putString("workoutname", mPersonRecordEntity.getFastest10().getStarttime());
                                bundle10Fast.putInt("workoutid", mPersonRecordEntity.getFastest10().getWorkoutid());
                                startActivity(HistoryInfoActivityV2.class, bundle10Fast);
                            } else {
                                bundle10Fast.putString("workoutname", mPersonRecordEntity.getFastest10().getStarttime());
                                bundle10Fast.putInt("workoutid", mPersonRecordEntity.getFastest10().getWorkoutid());
                                bundle10Fast.putString("nickname", mUserInfo.getNickname());
                                bundle10Fast.putInt("Age", TimeUtils.getAgeFromBirthDay(mUserInfo.getBirthdate()));
                                bundle10Fast.putString("avatar", mUserInfo.getAvatar());
                                startActivity(FriendHistoryInfoActivityV2.class, bundle10Fast);
                            }
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                            Bundle bundleHalf = new Bundle();
                            if (mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                                bundleHalf.putString("workoutname", mPersonRecordEntity.getFastesthalfmarathon().getStarttime());
                                bundleHalf.putInt("workoutid", mPersonRecordEntity.getFastesthalfmarathon().getWorkoutid());
                                startActivity(HistoryInfoActivityV2.class, bundleHalf);
                            } else {
                                bundleHalf.putString("workoutname", mPersonRecordEntity.getFastesthalfmarathon().getStarttime());
                                bundleHalf.putInt("workoutid", mPersonRecordEntity.getFastesthalfmarathon().getWorkoutid());
                                bundleHalf.putString("nickname", mUserInfo.getNickname());
                                bundleHalf.putInt("Age", TimeUtils.getAgeFromBirthDay(mUserInfo.getBirthdate()));
                                bundleHalf.putString("avatar", mUserInfo.getAvatar());
                                startActivity(FriendHistoryInfoActivityV2.class, bundleHalf);
                            }
                            break;
                        case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                            Bundle bundleFull = new Bundle();
                            if (mUserId == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                                bundleFull.putString("workoutname", mPersonRecordEntity.getFastestfullmarathon().getStarttime());
                                bundleFull.putInt("workoutid", mPersonRecordEntity.getFastestfullmarathon().getWorkoutid());
                                startActivity(HistoryInfoActivityV2.class, bundleFull);
                            } else {
                                bundleFull.putString("workoutname", mPersonRecordEntity.getFastestfullmarathon().getStarttime());
                                bundleFull.putInt("workoutid", mPersonRecordEntity.getFastestfullmarathon().getWorkoutid());
                                bundleFull.putString("nickname", mUserInfo.getNickname());
                                bundleFull.putInt("Age", TimeUtils.getAgeFromBirthDay(mUserInfo.getBirthdate()));
                                bundleFull.putString("avatar", mUserInfo.getAvatar());
                                startActivity(FriendHistoryInfoActivityV2.class, bundleFull);
                            }
                            break;
                    }
                }
            });
            //初始化、整理显示数据
            String mMedalNameStr = "";
            String mMedalValueStr = "";
            String mMedalUnitStr = "";
            int mMedalRes = R.drawable.icon_no_medal;
            switch (mMedal.getMedalType()) {
                //最长距离
                case UserOptMedalEntity.TYPE_LONGEST:
                    mMedalNameStr = "最长距离";
                    mMedalValueStr = NumUtils.retainTheDecimal(mMedal.getUserMedal().getLength());
                    mMedalUnitStr = "公里";
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_longest;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_longest;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_longest;
                    } else {
                        mMedalRes = R.drawable.icon_iron_longest;
                    }
                    break;
                //最长时间
                case UserOptMedalEntity.TYPE_MAX_DURATION:
                    mMedalNameStr = "最长时间";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_max_duration;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_max_duration;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_max_duration;
                    } else {
                        mMedalRes = R.drawable.icon_iron_max_duration;
                    }
                    break;
                //最快配速
                case UserOptMedalEntity.TYPE_FASTEST_PACE:
                    mMedalNameStr = "最快配速";
                    mMedalValueStr = TimeUtils.getPaceWithFh(mMedal.getUserMedal().getPace());
                    mMedalUnitStr = "/公里";
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_fastest_pace;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_fastest_pace;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_fastest_pace;
                    } else {
                        mMedalRes = R.drawable.icon_iron_fastest_pace;
                    }
                    break;
                //5公里最快
                case UserOptMedalEntity.TYPE_FASTEST_5:
                    mMedalNameStr = "5公里最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_fastest_5;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_fastest_5;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_fastest_5;
                    } else {
                        mMedalRes = R.drawable.icon_iron_fastest_5;
                    }
                    break;
                //10公里最快
                case UserOptMedalEntity.TYPE_FASTEST_10:
                    mMedalNameStr = "10公里最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_fastest_10;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_fastest_10;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_fastest_10;
                    } else {
                        mMedalRes = R.drawable.icon_iron_fastest_10;
                    }
                    break;
                //半马最快
                case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                    mMedalNameStr = "半马最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_half_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_half_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_half_marathon;
                    } else {
                        mMedalRes = R.drawable.icon_iron_half_marathon;
                    }
                    break;
                //全马最快
                case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                    mMedalNameStr = "全马最快";
                    mMedalValueStr = TimeUtils.getTimeWithFh(mMedal.getUserMedal().getDuration());
                    if (mMedal.getUserMedal().getRanking() >= 90) {
                        mMedalRes = R.drawable.icon_gold_full_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 70) {
                        mMedalRes = R.drawable.icon_silver_full_marathon;
                    } else if (mMedal.getUserMedal().getRanking() >= 40) {
                        mMedalRes = R.drawable.icon_copper_full_marathon;
                    } else {
                        mMedalRes = R.drawable.icon_iron_full_marathon;
                    }
                    break;
            }
            //初始显示，将数据显示
            mMedalIv.setImageResource(mMedalRes);
            mMedalNameTv.setText(mMedalNameStr);
            mMedalValueTv.setText(mMedalValueStr);
            mMedalUnitTv.setText(mMedalUnitStr);
            //将每个奖牌控件加入到横向居中的行 布局中显示
            mLineLl.addView(v, lp);
//            mLineLl.addView(v);
        }
    }

    /**
     * 启动好友列表
     */
    private void launchFriendList() {
        Bundle bundle = new Bundle();
        bundle.putInt("userid", mUserId);
        bundle.putString("nickname", mUserInfo.getNickname());
        startActivity(UserFriendListActivity.class, bundle);
    }

    /**
     * 启动相同好友页面
     */
    private void launchSameFriendList() {
        Bundle b = new Bundle();
        b.putInt("userid", mUserInfo.id);
        b.putString("nickname", mUserInfo.nickname);
        startActivity(SameFriendListActivity.class, b);
    }

    /**
     * 启动团列表
     */
    private void launchGroupList() {
        Bundle bundle = new Bundle();
        bundle.putInt("userid", mUserId);
        bundle.putString("nickname", mUserInfo.nickname);
        startActivity(UserGroupListActivity.class, bundle);
    }

    /**
     * 启动历史列表
     */
    private void launchDynamicList() {
        if (mUserInfo.isFriend != AppEnum.IsFriend.FRIEND) {
            T.showShort(mContext, "抱歉，只有好友才能看跑步记录");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("userid", mUserInfo.id);
        bundle.putString("nickname", mUserInfo.nickname);
        startActivity(UserDynamicListActivityReplace.class, bundle);
    }

    /**
     * 进入闹钟列表界面
     */
    private void launchClockAct() {
        MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "",
                AppEnum.MaidianType.UserInfoClock, TimeUtils.NowTime()));
        Intent intent = new Intent(mContext, MyClockListActivity.class);
        intent.putExtra("userid", mUserId);
        startActivity(intent);
    }

    private void showManagerTitleMenu() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
        if (isShowMessage) {
            transaction.remove(mMenuFragment);
        } else {
            ArrayList<String> mMenuTextList = new ArrayList<String>();
            mMenuTextList.add("删除好友");
            mMenuFragment = TitleMenuFragment.getInstance(mMenuTextList, new TitleMenuFragment.OnTitleItemClickListener() {
                @Override
                public void OnTitleItemClick(int position) {
                    switch (position) {
                        case 0:
                            showDeleteFriendDialog();
                            break;
                    }
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                    transaction.remove(mMenuFragment).commit();
                    isShowMessage = !isShowMessage;
                }
            });
            transaction.add(R.id.ll_fragment_message, mMenuFragment);
        }
        isShowMessage = !isShowMessage;
        transaction.commit();
    }

    /**
     * 显示删除好友对话框
     */
    private void showDeleteFriendDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "确定删除好友？", "好友删除后将在好友列表中消失", "取消", "确定");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                deleteFriend();
            }

            @Override
            public void onCancel() {

            }
        });
    }

    /**
     * 删除好友
     */
    private void deleteFriend() {
        mDialogBuilder.showProgressDialog(mContext, "正在删除好友", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_REMOVE_FRIEND;
        RequestParams params = RequestParamsBuild.BuildRemoveFriendParams(mContext, mUserInfo.id);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                finish();
            }


            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

}
