package com.app.pao.activity.party;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.group.GroupPartyMemberListActivity;
import com.app.pao.activity.group.GroupPartyPhotoWallActivity;
import com.app.pao.activity.group.GroupPartySelectWorkOutActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.entity.network.GetGroupPartyPaceLevelResult;
import com.app.pao.entity.network.GetPartyInfoDetailResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.fragment.title.menu.TitleMenuFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.app.pao.utils.TimeUtils;
import com.app.pao.utils.wx.WxShareManager;
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
import com.rey.material.widget.TextView;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Raul on 2016/1/27.
 * 活动详情页面
 */
@ContentView(R.layout.activity_group_party_info_replace)
public class PartyInfoActivity extends BaseAppCompActivity implements View.OnClickListener, View.OnTouchListener {

    /* contains */
    private static final String TAG = "PartyInfoActivity";

    private static final int MSG_POST_ERROR = 1;//请求失败
    private static final int MSG_POST_OK = 3;//请求成功

    /* local view for load*/
    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private TextView mReladV;

    /* local view for common */
    @ViewInject(R.id.iv_party_photo)
    private ImageView mPartyPhoto;
    @ViewInject(R.id.tv_party_name)
    private TextView mPartyNameTv;//活动名称文本
    @ViewInject(R.id.tv_group_name)
    private TextView mGroupNameTv;//跑团名称文本
    @ViewInject(R.id.iv_group_avatar)
    private ImageView mGroupIv;//跑团图标
    @ViewInject(R.id.tv_party_description)
    private TextView mPartyDescriptionTv;//活动描述
    @ViewInject(R.id.iv_group_owner)
    private CircularImage mOwnerIv;//跑团创建者头像
    @ViewInject(R.id.tv_group_owner_name)
    private TextView mOwnerNameTv;//跑团创建者名称
    @ViewInject(R.id.tv_party_location)
    private TextView mPartyLocationTv;//活动位置
    @ViewInject(R.id.tv_party_time)
    private TextView mPartyTimeTv;//活动时间
    @ViewInject(R.id.tv_party_plan_signuptime)
    private TextView mPartySignupdueTimeTv;//报名截止时间文本
    @ViewInject(R.id.tv_party_plan_end_time)
    private TextView mPartyEndTimeTv;//活动结束时间文本
    @ViewInject(R.id.tagv_party_tag)
    private TagView mPartyTagv;
    @ViewInject(R.id.btn_more)
    private ImageButton mMoreBtn;//更多
    @ViewInject(R.id.ll_tag)
    private LinearLayout mTagLl;

    /* local view for member */
    @ViewInject(R.id.iv_plan_1)
    private CircularImage mMemberPhoto1;
    @ViewInject(R.id.iv_plan_2)
    private CircularImage mMemberPhoto2;
    @ViewInject(R.id.iv_plan_3)
    private CircularImage mMemberPhoto3;
    @ViewInject(R.id.iv_plan_4)
    private CircularImage mMemberPhoto4;
    @ViewInject(R.id.iv_plan_5)
    private CircularImage mMemberPhoto5;
    @ViewInject(R.id.iv_plan_6)
    private CircularImage mMemberPhoto6;
    @ViewInject(R.id.tv_sign_up_count)
    private TextView mSignupCountTv;


    /* local view for plan*/
    @ViewInject(R.id.iv_party_plan)
    private ImageView mPlanIv;//计划轴图标
    @ViewInject(R.id.line_party_plan)
    private View mPlanLine;//计划的时间轴
    @ViewInject(R.id.tv_party_plan)
    private TextView mPlanTv;//计划轴文本
    @ViewInject(R.id.tv_signup_auth)
    private TextView mSignAuthTv;//报名权限文本
    @ViewInject(R.id.btn_signup)
    private LinearLayout mSignUpBtn;//报名按钮
    @ViewInject(R.id.tv_signup_contract_time)
    private TextView mSignUpContractTimeTv;//距报名截止时间差距

    /* local view for start */
    @ViewInject(R.id.iv_party_start)
    private ImageView mStartIv;//进行中轴图标
    @ViewInject(R.id.line_party_start)
    private View mStartLine;//进行中的时间轴
    @ViewInject(R.id.tv_party_start)
    private TextView mStartTv;//进行中轴文本
    @ViewInject(R.id.btn_checkin)
    private LinearLayout mCheckBtn;

    /* local view for end*/
    @ViewInject(R.id.iv_party_end)
    private ImageView mEndIv;//总结轴图标
    @ViewInject(R.id.line_party_end)
    private View mEndLine;//总结的时间轴
    @ViewInject(R.id.tv_party_end)
    private TextView mEndTv;//总结轴文本
    @ViewInject(R.id.tv_check_tip)
    private TextView mCheckTipTv;//打卡提示文本
    @ViewInject(R.id.tv_length_tip)
    private TextView mLengthTipTv;//跑量提示文本
    @ViewInject(R.id.tv_pace_tip)
    private TextView mPaceTipTv;//配速提示文本
    @ViewInject(R.id.ll_end_data)
    private LinearLayout mEndDataLl;//总结布局
    @ViewInject(R.id.tv_check_position)
    private TextView mEndCheckPositionTv;//打开序号文本
    @ViewInject(R.id.tv_check_total)
    private TextView mEndCheckTotalTv;//打开总人数
    @ViewInject(R.id.tv_length_position)
    private TextView mEndLengthPostionTv;//我的平均跑量
    @ViewInject(R.id.tv_length_total)
    private TextView mEndLengthTotalTv;//所有人平均跑量
    @ViewInject(R.id.tv_pace_position)
    private TextView mEndPacePositionTv;//我的配速
    @ViewInject(R.id.tv_pace_total)
    private TextView mEndPaceTotalTv;//所有人的平均配速

    /* local data */
    private BitmapUtils mBitmapUtils;//画图工具

    //活动相关
    private int mPartyId;//活动id
    private GetPartyInfoDetailResult mPartyInfor;//活动内容

    private List<CircularImage> mMemberViewList;//参加活动人员头像列表
    private FragmentManager fragmentManager;//Fragment管理器

    private boolean isShowMenu;
    private TitleMenuFragment mTitleMenuFragment;//菜单栏

    private boolean isFirstLoading;

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
                    mReladV.setVisibility(View.VISIBLE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
                //刷新成功
                case MSG_POST_OK:
                    updateViewByPostResult();
                    mLoadV.setVisibility(View.GONE);
                    mLoadViewRl.setVisibility(View.GONE);
                    if (mDialogBuilder.progressDialog != null) {
                        mDialogBuilder.progressDialog.dismiss();
                    }
                    break;
            }
        }
    };


    /**
     * on Touch
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    @OnTouch(R.id.ll_fragment_message)
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.ll_fragment_message) {
            if (mTitleMenuFragment != null && mTitleMenuFragment.isVisible()) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                transaction.remove(mTitleMenuFragment).commit();
                isShowMenu = !isShowMenu;
                return true;
            }
        }
        return isShowMenu;
    }

    /**
     * on Click
     *
     * @param v
     */
    @Override
    @OnClick({R.id.tv_reload, R.id.btn_des_more, R.id.iv_party_photo, R.id.btn_signup, R.id.btn_checkin,
            R.id.ll_end_data, R.id.ll_sighup, R.id.title_bar_left_menu, R.id.btn_more})
    public void onClick(View v) {
        //点击重新加载
        if (v.getId() == R.id.tv_reload) {
            postGetPartyInfo();
            mReladV.setVisibility(View.GONE);
            mLoadV.setLoadingText("加载中...");
            //跳转照片墙
        } else if (v.getId() == R.id.iv_party_photo) {
            launchPartyPhotoWall();
            //显示活动简介
        } else if (v.getId() == R.id.btn_des_more) {
            showPartyDesDialog();
            //报名按钮
        } else if (v.getId() == R.id.btn_signup) {
            getPartyPaceLevel();
            //跑步打卡
        } else if (v.getId() == R.id.btn_checkin) {
            launchPartySelectWorkOutActivity();
            //跑步总结
        } else if (v.getId() == R.id.ll_end_data) {
            launchSummary();
            //查看活动报名列表
        } else if (v.getId() == R.id.ll_sighup) {
            launchPartyMemberListActivity();
        } else if (v.getId() == R.id.title_bar_left_menu) {
            finish();
        } else if (v.getId() == R.id.btn_more) {
            showGroupTitleMenuList();
        }
    }

    @Override
    protected void initData() {
        isShowMenu = false;
        isFirstLoading = true;
        fragmentManager = getSupportFragmentManager();
        mBitmapUtils = new BitmapUtils(mContext);
        if (getIntent().hasExtra("partyid")) {
            mPartyId = getIntent().getIntExtra("partyid", 0);
        }
        mMemberViewList = new ArrayList<CircularImage>();
        mMemberViewList.add(mMemberPhoto1);
        mMemberViewList.add(mMemberPhoto2);
        mMemberViewList.add(mMemberPhoto3);
        mMemberViewList.add(mMemberPhoto4);
        mMemberViewList.add(mMemberPhoto5);
        mMemberViewList.add(mMemberPhoto6);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        postGetPartyInfo();
    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {

    }


    /**
     * 获取活动详情
     */
    private void postGetPartyInfo() {
        if (isFirstLoading) {
            isFirstLoading = false;
        } else {
            mDialogBuilder.showProgressDialog(mContext, "正在获取活动详情", false);
        }
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_DETAIL_INFO;
        RequestParams params = RequestParamsBuild.buildGetPartyDetailInfoRequest(mContext, mPartyId);
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
                mPartyInfor = JSON.parseObject(Response, GetPartyInfoDetailResult.class);
                if (mPostHandler != null) {
                    mPostHandler.sendEmptyMessage(MSG_POST_OK);
                }
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
     * 根据POST结果更新页面
     */
    private void updateViewByPostResult() {
        //若活动已取消
        if (mPartyInfor.status == AppEnum.groupPartyStatus.PARTY_CANCEL) {
            showCancelPartyDialog();
            return;
        }
        updateCommonView();
        updateMemberView();
        updateViewByPartyStatus();
    }

    /**
     * 更新活动界面
     */
    private void updateCommonView() {
        ImageUtils.loadPartyBigImage(mPartyInfor.getPicture(), mPartyPhoto);
        ImageUtils.loadGroupImage(mPartyInfor.getGroupavatar(), mGroupIv);
        ImageUtils.loadUserImage(mPartyInfor.getOwneravatar(), mOwnerIv);
        mOwnerNameTv.setText(mPartyInfor.getOwnerdisplayname());
        mPartyNameTv.setText(mPartyInfor.getName());
        mGroupNameTv.setText(mPartyInfor.getGroupname());
        mPartyDescriptionTv.setText(mPartyInfor.getDescription());
        mPartyLocationTv.setText(mPartyInfor.getLocation());
        mPartyTimeTv.setText(mPartyInfor.getStarttime());
        mPartySignupdueTimeTv.setText(mPartyInfor.getSignupduetime());
        mPartyEndTimeTv.setText(mPartyInfor.getEndtime());
        if (mPartyInfor.getPersonrole() == AppEnum.PersonRole.OWNER) {
            mMoreBtn.setVisibility(View.VISIBLE);
        } else {
            mMoreBtn.setVisibility(View.GONE);
        }
        showPartyTag();
    }

    /**
     * 更新活动人员相关界面
     */
    private void updateMemberView() {
        List<GetPartyInfoDetailResult.MembersEntity> membersEntityList = mPartyInfor.getMembers();
        mSignupCountTv.setText(membersEntityList.size() + "人报名");
        //先隐藏
        for (int i = 0; i < mMemberViewList.size(); i++) {
            mMemberViewList.get(i).setVisibility(View.GONE);
        }
        //根据参与人员数量显示
        for (int i = 0; i < mMemberViewList.size() && i < membersEntityList.size(); i++) {
            mMemberViewList.get(i).setVisibility(View.VISIBLE);
            ImageUtils.loadUserImage(membersEntityList.get(i).getAvatar(), mMemberViewList.get(i));
        }
    }

    /**
     * 根据活动状态改变页面
     */
    private void updateViewByPartyStatus() {
        resetStatusViews();
        updatePartyStatusViews();
        updatePersonActionViews();
    }

    /**
     * 初始化与状态相关的页面
     */
    private void resetStatusViews() {
        //Plan
        mPlanIv.setBackgroundResource(R.drawable.icon_party_plan);
        mPlanLine.setBackgroundColor(Color.parseColor("#eeeeee"));
        mPlanTv.setTextColor(Color.parseColor("#888888"));
        mSignAuthTv.setVisibility(View.GONE);
        mSignUpBtn.setVisibility(View.GONE);
        //Start
        mStartIv.setBackgroundResource(R.drawable.icon_party_start);
        mStartLine.setBackgroundColor(Color.parseColor("#eeeeee"));
        mStartTv.setTextColor(Color.parseColor("#888888"));
        mCheckBtn.setVisibility(View.GONE);
        //End
        mEndIv.setBackgroundResource(R.drawable.icon_party_end);
//        mEndLine.setBackgroundColor(Color.parseColor("#eeeeee"));
        mEndTv.setTextColor(Color.parseColor("#888888"));
        mEndDataLl.setVisibility(View.GONE);
        mEndCheckPositionTv.setVisibility(View.GONE);
        mEndLengthPostionTv.setVisibility(View.GONE);
        mEndPacePositionTv.setVisibility(View.GONE);
    }


    /**
     * 更新活动状态页面
     */
    private void updatePartyStatusViews() {
        //活动未开始
        if (mPartyInfor.getStatus() < AppEnum.groupPartyStatus.PARTY_START) {
            mPlanIv.setBackgroundResource(R.drawable.icon_party_plan_light);
            mPlanLine.setBackgroundColor(Color.parseColor("#f06522"));
            mPlanTv.setTextColor(Color.parseColor("#f06522"));
            //活动进行中
        } else if (mPartyInfor.getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
            mStartIv.setBackgroundResource(R.drawable.icon_party_start_light);
            mStartLine.setBackgroundColor(Color.parseColor("#f06522"));
            mStartTv.setTextColor(Color.parseColor("#f06522"));
        } else {
            mEndIv.setBackgroundResource(R.drawable.icon_party_end_light);
//            mEndLine.setBackgroundColor(Color.parseColor("#f06522"));
            mEndTv.setTextColor(Color.parseColor("#f06522"));
            mEndDataLl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据个人行动状态改变的UI
     */
    private void updatePersonActionViews() {
//        Log.v(TAG, "mPartyInfor.getPersonstatus():" + mPartyInfor.getPersonstatus());
//        Log.v(TAG, "mPartyInfor.getStatus():" + mPartyInfor.getStatus());
        //未报名,有报名权限,未超过报名截止时间
        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST
                && TimeUtils.stringToDate(mPartyInfor.getSignupduetime()).after(new Date())) {
            mSignUpBtn.setVisibility(View.VISIBLE);
            long contractTime = TimeUtils.stringToDate(mPartyInfor.getSignupduetime()).getTime() - new Date().getTime();
            mSignUpContractTimeTv.setText(TimeUtils.formatSignUpContractString(contractTime / 1000) + "后截止");
            return;
        }

        //未报名,已超过报名截止时间,活动未开始
        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST
                && TimeUtils.stringToDate(mPartyInfor.getSignupduetime()).before(new Date())
                && mPartyInfor.getStatus() < AppEnum.groupPartyStatus.PARTY_STOP) {
            mSignAuthTv.setVisibility(View.VISIBLE);
            mSignAuthTv.setText("活动报名已截止");
            return;
        }

        //无权限  && 未超过报名时间 && 活动未结束
        if ((mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_MEMBER)
                && TimeUtils.stringToDate(mPartyInfor.getSignupduetime()).after(new Date())
                && mPartyInfor.getStatus() < AppEnum.groupPartyStatus.PARTY_STOP) {
            mSignAuthTv.setVisibility(View.VISIBLE);
            mSignAuthTv.setText("您没有报名权限");
            return;
        }

        //未报名 && (已超过报名截止时间 || 不是团员 ) && 活动已结束
        if ((mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST
                || mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_MEMBER)
                && TimeUtils.stringToDate(mPartyInfor.getSignupduetime()).before(new Date())
                && mPartyInfor.getStatus() > AppEnum.groupPartyStatus.PARTY_START) {
            mCheckTipTv.setText("打卡人数");
            mLengthTipTv.setText("平均跑量");
            mPaceTipTv.setText("平均配速");
            mEndCheckTotalTv.setText(mPartyInfor.getCheckincount() + "");
            mEndLengthTotalTv.setText(NumUtils.formatLength(mPartyInfor.getAvglength() / 1000f) + "公里");
            mEndPaceTotalTv.setText(TimeUtils.formatSecondsToSpeedTime(mPartyInfor.getAvgpace()));
            return;
        }

        //已报名未打卡 && 活动未结束
        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST
                && mPartyInfor.getStatus() <= AppEnum.groupPartyStatus.PARTY_START) {
            mCheckBtn.setVisibility(View.VISIBLE);
            return;
        }

        //已报名未打卡 && 活动结束
        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST
                && mPartyInfor.getStatus() > AppEnum.groupPartyStatus.PARTY_START) {
            mCheckBtn.setVisibility(View.VISIBLE);
            mCheckTipTv.setText("打卡人数");
            mLengthTipTv.setText("平均跑量");
            mPaceTipTv.setText("平均配速");
            mEndCheckTotalTv.setText(mPartyInfor.getCheckincount() + "");
            mEndLengthTotalTv.setText(NumUtils.formatLength(mPartyInfor.getAvglength() / 1000f) + "公里");
            mEndPaceTotalTv.setText(TimeUtils.formatSecondsToSpeedTime(mPartyInfor.getAvgpace()));
            return;
        }

        //已打卡
        if (mPartyInfor.getPersonstatus() == AppEnum.groupPartyPersonStatus.LOGIN) {
            mEndCheckPositionTv.setVisibility(View.VISIBLE);
            mEndLengthPostionTv.setVisibility(View.VISIBLE);
            mEndPacePositionTv.setVisibility(View.VISIBLE);
            mCheckTipTv.setText("你的序号/打卡人数");
            mLengthTipTv.setText("你的/平均跑量");
            mPaceTipTv.setText("你的/平均配速");
            mEndCheckTotalTv.setText("/" + mPartyInfor.getCheckincount() + "");
            mEndLengthTotalTv.setText("/" + NumUtils.formatLength(mPartyInfor.getAvglength() / 1000f) + "公里");
            mEndPaceTotalTv.setText("/" + TimeUtils.formatSecondsToSpeedTime(mPartyInfor.getAvgpace()));
            mEndCheckPositionTv.setText(mPartyInfor.getRanking() + "");
            mEndLengthPostionTv.setText(NumUtils.formatLength(mPartyInfor.getLength() / 1000f) + "");
            mEndPacePositionTv.setText(TimeUtils.formatSecondsToSpeedTime(mPartyInfor.getPace()));
            mEndDataLl.setVisibility(View.VISIBLE);
            return;
        }
    }

    /**
     * 显示跑团的tag
     */
    private void showPartyTag() {
        List<GetPartyInfoDetailResult.Label> labelList = mPartyInfor.getLabel();

        if (labelList == null || labelList.size() == 0) {
            mTagLl.setVisibility(View.GONE);
            return;
        }
        mTagLl.setVisibility(View.VISIBLE);
        List<Tag> tagList = new ArrayList<>();
        for (int i = 0; i < labelList.size(); i++) {
            Tag tag = new Tag("" + labelList.get(i).getName());
            tag.tagTextSize = 10;
            tag.radius = 2;
            tag.layoutColor = Color.TRANSPARENT;
            tag.tagTextColor = Color.parseColor("#F06522");
            tag.layoutBorderColor = Color.parseColor("#F06522");
            tag.layoutBorderSize = 1;
            tagList.add(tag);
        }
        mPartyTagv.addTags(tagList);
    }

    /**
     * 活动成员页面
     */
    private void launchPartyMemberListActivity() {
        Bundle b = new Bundle();
        b.putSerializable("party", mPartyInfor);
        startActivity(GroupPartyMemberListActivity.class, b);
    }

    /**
     * 显示活动介绍对话框
     */
    private void showPartyDesDialog() {
        partyDesDialogFragment dialog = new partyDesDialogFragment();
        dialog.setDes(mPartyInfor.getDescription());
        dialog.show(getFragmentManager(), "EditNameDialog");
    }


    public static class partyDesDialogFragment extends DialogFragment {

        private String des;

        public partyDesDialogFragment() {
        }

        public void setDes(String des) {
            this.des = des;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            View view = inflater.inflate(R.layout.dialog_party_des, container);
            TextView contentTv = (TextView) view.findViewById(R.id.tv_content);
            contentTv.setText(des);
            return view;
        }
    }

    /**
     * 活动照片墙
     */
    private void launchPartyPhotoWall() {
        Bundle b = new Bundle();
        b.putInt("partyId", mPartyId);
        startActivity(GroupPartyPhotoWallActivity.class, b);
    }

    /**
     * 获取跑团活动中的配速分档
     */
    private void getPartyPaceLevel() {
        mDialogBuilder.showProgressDialog(mContext, "请稍后...", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_PACE_LEVEL;
        RequestParams params = RequestParamsBuild.buildGetPartyPaceLevel(mContext);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                GetGroupPartyPaceLevelResult paceLevel = JSON.parseObject(Response, GetGroupPartyPaceLevelResult.class);
                ArrayList<String> data = new ArrayList<String>();
//                data.add("无配速");
                data.addAll(paceLevel.getPace());
                Bundle b = new Bundle();
                b.putStringArrayList("pace", data);
                b.putInt("partyid", mPartyId);
                startActivity(signUpPartyActivity.class, b);
//                mDialogBuilder.showSpinnerDialog(mContext, data);
//                mDialogBuilder.setSpinnerListener(new MyDialogBuilderV1.SpinnerDialogListener() {
//                    @Override
//                    public void onCancelBtnClick() {
//
//                    }
//
//                    @Override
//                    public void onConfirmBtnClick(String spStr, boolean isChecked) {
//                        if (isChecked) {
//                            signupParty(spStr, 1);
//                        } else {
//                            signupParty(spStr, 0);
//                        }
//
//                    }
//                });
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                T.showShort(mContext, s);
            }

            @Override
            protected void onFinish() {
                if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                    mDialogBuilder.progressDialog.dismiss();
                }
            }
        });
    }

    /**
     * 报名活动
     */
    private void signupParty(String pace, int isleader) {
        mDialogBuilder.showProgressDialog(mContext, "报名中..", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_SIGNUP;
        RequestParams params = RequestParamsBuild.BuildSignupPartyParams(mContext, mPartyId, pace, isleader);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "报名成功");
                if (mDialogBuilder.progressDialog != null) {
                    mDialogBuilder.progressDialog.dismiss();
                }
                updateData();
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
     * 启动总结页面
     */
    private void launchSummary() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("party", mPartyInfor);
        startActivity(GroupPartySummaryActivity.class, bundle);
    }

    /**
     * 活动历史选择
     */
    private void launchPartySelectWorkOutActivity() {
        Bundle b = new Bundle();
        b.putInt("partyId", mPartyId);
        startActivity(GroupPartySelectWorkOutActivity.class, b);
    }

    /**
     * 显示消息列表页面
     */
    public void showGroupTitleMenuList() {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
        if (isShowMenu) {
            transaction.remove(mTitleMenuFragment);
        } else {
            //   if (mGroupTitleMenuFragment == null) {
            ArrayList<String> mMenuTextList = new ArrayList<String>();
            mMenuTextList.add("活动分享");
            mMenuTextList.add("取消活动");


            mTitleMenuFragment = TitleMenuFragment.getInstance(mMenuTextList, new TitleMenuFragment
                    .OnTitleItemClickListener() {
                @Override
                public void OnTitleItemClick(int position) {

                    switch (position) {
                        case 0:
                            shareParty();
                            break;
                        //取消活动
                        case 1:
                            cancelParty();
                            break;
                    }
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                    transaction.remove(mTitleMenuFragment).commit();
                    isShowMenu = !isShowMenu;
                }
            });

            transaction.add(R.id.ll_fragment_message, mTitleMenuFragment);
//            } else {
//                transaction.show(mGroupTitleMenuFragment);
//            }
        }
        isShowMenu = !isShowMenu;
        transaction.commit();
    }


    /**
     * 分享活动
     */
    private void shareParty() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        // 发送请求
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_PARTY_SHARE_TEXT;
        final RequestParams params = RequestParamsBuild.BuildGetPartySumShareRequest(mContext, mPartyInfor.getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
                if (result.getTitle() != null && !result.getTitle().isEmpty()) {
                    final WxShareManager share = new WxShareManager(PartyInfoActivity.this);
                    mDialogBuilder.setShareWxFriendDialog(mContext, "分享活动详情到");
                    mDialogBuilder.setListener(new MyDialogBuilderV1.ShareWxFriendDialogListener() {
                        @Override
                        public void onWxFriendClick() {
                            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN, result.getText(), result.getTitle(), result.getLink(),result.image);
                        }

                        @Override
                        public void onWxFriendCircleClick() {
                            share.startWxShareUrlByCustom(SHARE_MEDIA.WEIXIN_CIRCLE, result.getText(), result.getTitle(), result.getLink(),result.image);
                        }

                        @Override
                        public void onCancle() {

                        }
                    });
//                    share.startWxShareUrl(result.getText(), result.getTitle(), result.getLink());
                }
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
     * 取消活动
     */
    private void cancelParty() {
        if (mPartyInfor.getStatus() <= AppEnum.groupPartyStatus.PARTY_START) {
            mDialogBuilder.showChoiceTwoBtnDialog(mContext, "确定取消活动？", "活动取消后将在活动列表中消失，并将无法恢复", "放弃", "确定取消");
            mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
                @Override
                public void onLeftBtnClick() {
                }

                @Override
                public void onRightBtnClick() {
                    postCancelParty();
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            T.showShort(mContext, "活动已结束,不能取消");
        }
    }

    /**
     * 发送取消活动请求
     */
    private void postCancelParty() {
        mDialogBuilder.showProgressDialog(mContext, "正在发送请求", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_PARTY_CANCEL;
        RequestParams params = RequestParamsBuild.BuildCancelPartyParams(mContext, mPartyId);
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "活动已取消");
                finish();
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


    /**
     * 显示已取消的提示
     */
    private void showCancelPartyDialog() {
        SpannableString spanStr = new SpannableString("本活动已取消");
        mDialogBuilder.showSimpleMsgDialog(mContext, "抱歉", spanStr);
        mDialogBuilder.SimpleMsgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });

    }
}
