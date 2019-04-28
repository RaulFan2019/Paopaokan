package com.app.pao.activity.group;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.main.ZoomBigImageActivity;
import com.app.pao.activity.party.CreateGroupPartyActivity;
import com.app.pao.activity.party.PartyInfoActivity;
import com.app.pao.activity.settings.MultiImageSelectorActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.db.DBEntityMessage;
import com.app.pao.entity.event.EventJpush;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetSmsInviteResult;
import com.app.pao.entity.network.GetUploadAvatarResult;
import com.app.pao.entity.network.GetWxInviteResult;
import com.app.pao.fragment.title.menu.TitleMenuFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.dialog.v1.MyDialogBuilderV1;
import com.app.pao.ui.widget.BadgeView;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.FileUtils;
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
import com.rey.material.widget.Button;
import com.rey.material.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/24.
 * <p/>
 * 跑团详情页面
 */
@ContentView(R.layout.activity_group_info)
public class GroupInfoActivity extends BaseAppCompActivity implements View.OnClickListener, View
        .OnTouchListener {

    private static final String TAG = "GroupInfoActivity";

    private static int REQUEST_ADD_PHOTO = 1;

    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    @ViewInject(R.id.tv_title_group_name)
    private TextView mTitleTv;      //标题名
    @ViewInject(R.id.ibtn_share_group)
    private ImageButton mShareGroupIBtn;//分享按钮
    @ViewInject(R.id.ibtn_group_more)
    private ImageButton mGroupMoreIBtn;//更多功能按钮

    @ViewInject(R.id.btn_apply_in_group)
    private Button mApplyInGroupBtn;    //申请按钮
    @ViewInject(R.id.ll_apply_in_group)
    private LinearLayout mApplyInGroupLl;//
    @ViewInject(R.id.iv_group_picture)
    private ImageView mGroupPictureIv;  //跑团图片
    @ViewInject(R.id.iv_edit_photo)
    private ImageView mGroupPictureEditIv;//跑团图片设置图标

    @ViewInject(R.id.tv_group_name)
    private TextView mGroupNameTv;  //团名
    @ViewInject(R.id.tv_group_location)
    private TextView mGroupLocationTv; //团位置
    @ViewInject(R.id.tagv_group_info_tag)
    private TagView mGroupInfoTagTv;        //标签

    @ViewInject(R.id.tv_group_introduce)
    private TextView mGroupIntroduceTv; //跑团介绍
    @ViewInject(R.id.iv_group_photo)
    private ImageView mGroupPhotoIv;
    @ViewInject(R.id.ll_group_all)
    private LinearLayout mGroupAllLl;   //跑团详情
    @ViewInject(R.id.ll_group_location)
    private LinearLayout mGroupLocationLl;

    @ViewInject(R.id.ll_group_organizer)
    private LinearLayout mGroupOrganizerLl; //跑团团长布局
    @ViewInject(R.id.ci_group_organizer_photo)
    private CircularImage mGroupOrganizerPhotoCi;//团长图片
    @ViewInject(R.id.tv_group_organizer_name)
    private TextView mGroupOrganizerNameTv;//团长名称
    @ViewInject(R.id.ll_group_admin)
    private LinearLayout mGroupAdminLl;//跑团管理员
    @ViewInject(R.id.ci_group_admin_photo)
    private CircularImage mGroupAdminPhotoCi;//管理员头像
    @ViewInject(R.id.tv_group_admin_name)
    private TextView mGroupAdminNameTv;//管理员名称
    @ViewInject(R.id.tv_group_admin_edit)
    private TextView mGroupAdminEditTv;//管理员编辑文字
    @ViewInject(R.id.iv_group_admin_edit)
    private ImageView mGroupAdminEditIv;//管理员编辑图标
    @ViewInject(R.id.tv_group_admin_num)
    private TextView mGroupAdminNumTv;//管理员的数量
    @ViewInject(R.id.tv_week_speed)
    private TextView mGroupWeekSpeedTv;//周人均配速
    @ViewInject(R.id.tv_week_km)
    private TextView mGroupWeekKmTv;//周人均公里数

    @ViewInject(R.id.ll_first_member)
    private LinearLayout mFirstMemberLl;        //第一名
    @ViewInject(R.id.tv_first_member_name)
    private TextView mFirstMemberNameTv;
    @ViewInject(R.id.fl_first_member_pb)
    private FrameLayout mFirstMemberPbFl;
    @ViewInject(R.id.pb_first_member_show)
    private ProgressBar mFirstMemberShowPb;
    @ViewInject(R.id.ci_first_member_avatar)
    private CircularImage mFirstMemberAvatarCi;
    @ViewInject(R.id.tv_first_member_km)
    private TextView mFirstMemberKmTv;
    @ViewInject(R.id.iv_first_is_own)
    private ImageView mFirstIsOwnIv;

    @ViewInject(R.id.ll_second_member)
    private LinearLayout mSecondMemberLl;       //第二名
    @ViewInject(R.id.tv_second_member_name)
    private TextView mSecondMemberNameTv;
    @ViewInject(R.id.fl_second_member_pb)
    private FrameLayout mSecondMemberPbFl;
    @ViewInject(R.id.pb_second_member_show)
    private ProgressBar mSecondMemberShowPb;
    @ViewInject(R.id.ci_second_member_avatar)
    private CircularImage mSecondMemberAvatarCi;
    @ViewInject(R.id.tv_second_member_km)
    private TextView mSecondMemberKmTv;
    @ViewInject(R.id.iv_second_is_own)
    private ImageView mSecondIsOwnIv;

    @ViewInject(R.id.ll_thirdly_member)
    private LinearLayout mThirdlyMemberLl;      //第三名
    @ViewInject(R.id.tv_thirdly_member_name)
    private TextView mThirdlyMemberNameTv;
    @ViewInject(R.id.fl_thirdly_member_pb)
    private FrameLayout mThirdlyMemberPbFl;
    @ViewInject(R.id.pb_thirdly_member_show)
    private ProgressBar mThirdlyMemberShowPb;
    @ViewInject(R.id.ci_thirdly_member_avatar)
    private CircularImage mThirdlyMemberAvatarCi;
    @ViewInject(R.id.tv_thirdly_member_km)
    private TextView mThirdlyMemberKmTv;
    @ViewInject(R.id.iv_thirdly_is_own)
    private ImageView mThirdlyIsOwnIv;

    @ViewInject(R.id.tv_rank_tip)
    private TextView mRankTipTv;            //排行提示文字
    @ViewInject(R.id.btn_member_ranking)
    private TextView mRankInfoBtn;            //进入排行详情列表页面按钮
    @ViewInject(R.id.tv_member_ranking)
    private TextView mRankTv;

    @ViewInject(R.id.ll_group_party_1)
    private LinearLayout mGroupPartyLl_1;       //活动一
    @ViewInject(R.id.tv_group_party_name_1)
    private TextView mGroupPartyNameTv_1;
    @ViewInject(R.id.tv_group_party_location_1)
    private TextView mGroupPartyLocationTv_1;
    @ViewInject(R.id.tv_group_party_state_1)
    private TextView mGroupPartyStateTv_1;
    @ViewInject(R.id.iv_party_underway_1)
    private ImageView mGroupPartyUnderwayIv_1;

    @ViewInject(R.id.ll_group_party_2)
    private LinearLayout mGroupPartyLl_2;       //活动二
    @ViewInject(R.id.tv_group_party_name_2)
    private TextView mGroupPartyNameTv_2;
    @ViewInject(R.id.tv_group_party_location_2)
    private TextView mGroupPartyLocationTv_2;
    @ViewInject(R.id.tv_group_party_state_2)
    private TextView mGroupPartyStateTv_2;
    @ViewInject(R.id.iv_party_underway_2)
    private ImageView mGroupPartyUnderwayIv_2;
    @ViewInject(R.id.tv_no_party)
    private TextView mNoGroupParty_2;

    @ViewInject(R.id.tv_no_party_all)
    private TextView mNoGroupPartyAll;
    @ViewInject(R.id.btn_member_party)
    private TextView mMoreGroupPartyBtn;
    @ViewInject(R.id.tv_member_party)
    private TextView mMoreGroupPartyTv;

    @ViewInject(R.id.ll_party_all)
    private LinearLayout mGroupPartyAllLl;
    @ViewInject(R.id.btn_create_party)
    private Button mCreateGroupPartyBtn;

    @ViewInject(R.id.rl_load)
    private LinearLayout mLoadViewRl;//加载View
    @ViewInject(R.id.loadview)
    private LoadingView mLoadV;
    @ViewInject(R.id.tv_reload)
    private com.rey.material.widget.TextView mReladV;

    private boolean isShowMessage;
    private boolean isFirstIn;
    private boolean isSetPicture = false;//是否设置跑团图片
    private int mGroupId;
    private int mUserId;
    private GetGroupDetailInfoRequest mGroupInfo;

    private BitmapUtils bitmapUtils;

    private TitleMenuFragment mMenuFragment;//菜单栏
    private FragmentManager fragmentManager;//Fragment管理器

    private WxShareManager share;

    @ViewInject(R.id.ll_join_group)
    private LinearLayout mGroupApplyLl;
    //申请入团的布局
    @ViewInject(R.id.iv_join_member_1)
    private CircularImage mJoinIv1;
    @ViewInject(R.id.iv_join_member_2)
    private CircularImage mJoinIv2;
    @ViewInject(R.id.iv_join_member_3)
    private CircularImage mJoinIv3;
    @ViewInject(R.id.tv_join_member)
    private com.rey.material.widget.TextView mJoinTv;
    private BadgeView badgeView;
    private int userid;
    private List<DBEntityMessage> mMessageList;
    private int applyGroupNum;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_POST_ERROR) {
                T.showShort(mContext, msg.obj.toString());
                handler.removeCallbacks(mPostRunnable);
                mLoadV.setLoadingText("加载失败");
                mReladV.setVisibility(View.VISIBLE);
            } else if (msg.what == MSG_POST_OK) {
                mLoadV.setVisibility(View.GONE);
                mLoadViewRl.setVisibility(View.GONE);
                handler.removeCallbacks(mPostRunnable);
                initAllViews();
            }

            if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
                mDialogBuilder.progressDialog.dismiss();
            }
        }
    };

    Runnable mPostRunnable = new Runnable() {
        @Override
        public void run() {
            mDialogBuilder.showProgressDialog(mContext, "正在加载", true);
        }
    };

    /**
     * onActivityResult()发生在onResume()之前。
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_PHOTO) {
                isSetPicture = true;
                mDialogBuilder.showProgressDialog(mContext, "正在上传照片..", false);
                ArrayList<String> mSelectImagePathList = data.getStringArrayListExtra(MultiImageSelectorActivity
                        .EXTRA_RESULT);
                Bitmap resultBmap = ImageUtils.compressImage(mSelectImagePathList.get(0));
                postAvatar(FileUtils.saveBigBitmap(resultBmap));
            }
        }
    }

    @Override
    @OnClick({R.id.iv_group_picture, R.id.title_bar_left_menu, R.id.btn_apply_in_group, R.id.ibtn_share_group, R.id
            .ibtn_group_more, R.id.ll_group_all, R.id.ll_group_organizer, R.id.ll_group_admin, R.id.btn_member_ranking,
            R.id.ll_group_party_1, R.id.ll_group_party_2, R.id.btn_member_party, R.id.btn_create_party,
            R.id.ll_first_member, R.id.ll_second_member, R.id.ll_thirdly_member, R.id.iv_group_photo,
            R.id.ll_join_group, R.id.iv_no, R.id.tv_reload})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_group_picture:
                addPhoto(REQUEST_ADD_PHOTO);
                break;
            case R.id.title_bar_left_menu:
                finish();
                break;
            case R.id.btn_apply_in_group:
                doInGroup();
                break;
            case R.id.ibtn_share_group:
//                shareGroup();
                getShareGroupText();
                break;
            case R.id.ibtn_group_more:
                if ((mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.MANAGER
                        || mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER)) {
                    showManagerTitleMenu();
                } else {
                    showMemberTitleMenu();
                }
                break;
            //点击跑团简介
            case R.id.ll_group_all:
                launchGroupIntroduce();
                break;
            case R.id.ll_group_organizer:

                break;
            case R.id.ll_group_admin:
                launchAddGroupManage();
                break;
            case R.id.btn_member_ranking:
                launchGroupRanking();
                break;
            //第一个左边的活动
            case R.id.ll_group_party_1:
                launchPartyInfo(0);
                break;
            //第二个右边的活动
            case R.id.ll_group_party_2:
                launchPartyInfo(1);
                break;
            case R.id.btn_member_party:
                launchPartyList();
                break;
            case R.id.btn_create_party:
                launchCreateParty();
                break;
            case R.id.tv_reload:
                getGroupInfo();
                mLoadV.setLoadingText("加载中...");
                mReladV.setVisibility(View.GONE);
                break;
            case R.id.ll_first_member:
                launchMemberInfo(0);
                break;
            case R.id.ll_second_member:
                launchMemberInfo(1);
                break;
            case R.id.ll_thirdly_member:
                launchMemberInfo(2);
                break;
            case R.id.iv_group_photo:
                Bundle bundle = new Bundle();
                bundle.putString("avatar", mGroupInfo.getRungroup().getAvatar());
                startActivity(ZoomBigImageActivity.class, bundle);
                break;
            case R.id.ll_join_group:
                MessageData.readAllNewGroupMessage(mContext, userid);
                launchApplyInGroup();
                break;
            case R.id.iv_no:
                MessageData.showAllGroupMessage(mContext, userid);
                mGroupApplyLl.setVisibility(View.GONE);
                break;
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
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                transaction.remove(mMenuFragment).commit();
                isShowMessage = !isShowMessage;
                return true;
            }
        }
        return isShowMessage;
    }

    @Override
    protected void initData() {
        userid = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        applyGroupNum = 0;
        mMessageList = new ArrayList<>();
        isShowMessage = false;
        isFirstIn = true;
        fragmentManager = getSupportFragmentManager();
        mGroupId = getIntent().getIntExtra("groupid", 0);
        mUserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
        bitmapUtils = new BitmapUtils(mContext);
        share = new WxShareManager(GroupInfoActivity.this);
    }

    @Override
    protected void initViews() {
        badgeView = new BadgeView(mContext, mGroupMoreIBtn);
        badgeView.setTextSize(9);
        badgeView.setBackgroundResource(R.drawable.icon_red_circle);
        badgeView.setTextColor(Color.parseColor("#ffffff"));
        badgeView.setBadgeMargin((int) DeviceUtils.dpToPixel(3));
    }

    @Override
    protected void doMyOnCreate() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void updateData() {
        updateApplyGroup();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    /**
     * 接收到Jpush改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventJpush event) {
        int type = event.getMsgType();
        if (type == AppEnum.messageType.APPLY_JOIN_RUNGROUP) {
            updateApplyGroup();
            updateApplyGroupBut();
        }
    }

    @Override
    protected void updateViews() {
        if (isSetPicture) {
            isSetPicture = false;
            return;
        }
        getGroupInfo();
        if (isFirstIn) {
            isFirstIn = false;
        } else {
            handler.postDelayed(mPostRunnable, 500);
        }
        updateApplyGroupBut();
    }

    @Override
    protected void destroy() {
        EventBus.getDefault().unregister(this);
        cancelHandler();
    }

    private void updateApplyGroup() {
        mMessageList = MessageData.getGroupShowMsgByGroupId(mContext, userid, mGroupId);
        doApplyGroup();
    }

    private void doApplyGroup() {
        if (mMessageList.size() == 0) {
            mGroupApplyLl.setVisibility(View.GONE);
        } else {
            mGroupApplyLl.setVisibility(View.VISIBLE);
            mJoinIv1.setVisibility(View.VISIBLE);
            if (mMessageList.size() == 1) {
                mJoinIv2.setVisibility(View.GONE);
                mJoinIv3.setVisibility(View.GONE);
                ImageUtils.loadUserImage(mMessageList.get(0).fromuseravatar, mJoinIv1);
            } else if (mMessageList.size() == 2) {
                mJoinIv2.setVisibility(View.VISIBLE);
                mJoinIv3.setVisibility(View.GONE);
                ImageUtils.loadUserImage(mMessageList.get(0).fromuseravatar, mJoinIv1);
                ImageUtils.loadUserImage(mMessageList.get(1).fromuseravatar, mJoinIv2);
            } else {
                mJoinIv2.setVisibility(View.VISIBLE);
                mJoinIv3.setVisibility(View.VISIBLE);
                ImageUtils.loadUserImage(mMessageList.get(0).fromuseravatar, mJoinIv1);
                ImageUtils.loadUserImage(mMessageList.get(1).fromuseravatar, mJoinIv2);
                ImageUtils.loadUserImage(mMessageList.get(2).fromuseravatar, mJoinIv3);
            }
            mJoinTv.setText(mMessageList.get(0).getFromusernickname() + "等" + mMessageList.size() + "人请求加入团");
        }
    }

    private void cancelHandler() {
        if (handler != null) {
            handler.removeMessages(MSG_POST_OK);
            handler.removeMessages(MSG_POST_ERROR);
            handler.removeCallbacks(mPostRunnable);
        }
    }

    /**
     * 初始化显示
     */
    private void initAllViews() {
        mShareGroupIBtn.setVisibility(View.VISIBLE);
        mGroupMoreIBtn.setVisibility(View.VISIBLE);

        GetGroupDetailInfoRequest.GroupDetailInfo group = mGroupInfo.getRungroup();

//        mTitleTv.setText(group.getName());
        mTitleTv.setText("跑团主页");
        mGroupNameTv.setText(group.getName());
        mGroupLocationTv.setText(group.getLocationprovince() + " " + group.getLocationcity());
        mGroupIntroduceTv.setText(group.getDescription());
        showGroupTag();
        //人均周跑量
        mGroupWeekKmTv.setText(NumUtils.retainTheDecimal(group.getAvgweeklength()) + "");
        //跑团头像;
        ImageUtils.loadGroupImage(group.getAvatar(), mGroupPhotoIv);
        //活动图片
        ImageUtils.loadGroupBigImage(group.getWallpaper(), mGroupPictureIv);

        //若是成员 确定跑团首页申请按钮状态
        if (mGroupInfo.getRungroup().getRole() < AppEnum.groupRole.APPLY && mGroupInfo.getRungroup().getRole() !=
                AppEnum.groupRole.NOT_MEMBER) {
            mApplyInGroupLl.setVisibility(View.GONE);
            mGroupMoreIBtn.setVisibility(View.VISIBLE);
            mShareGroupIBtn.setVisibility(View.VISIBLE);

            Spanned showSortText = Html.fromHtml("<font>" + group.getMembercount() + "名成员中您排"
                    + "</font><font" + " color=\"#F06522\" ><big> " + group.getMyranking() + " </big></font>名");
//            mRankInfoBtn.setText(showSortText);
            mRankTv.setText(showSortText);
            Spanned showGroupText = Html.fromHtml("<font>" + "跑团已组织了 " + "</font><font" +
                    " color=\"#F06522\" ><big>" + group.getPartycount() + "</big></font> 次活动");
//            mMoreGroupPartyBtn.setText(showGroupText);
            mMoreGroupPartyTv.setText(showGroupText);
        } else {
            mApplyInGroupLl.setVisibility(View.VISIBLE);
            mShareGroupIBtn.setVisibility(View.GONE);
            mGroupMoreIBtn.setVisibility(View.GONE);
            Spanned showSortText = Html.fromHtml("<font>" + "该跑团一共" + "</font><font" +
                    " color=\"#F06522\" ><big> " + group.getMembercount() + " </big></font><font>名成员</font>");
//            mRankInfoBtn.setText(showSortText);
            mRankTv.setText(showSortText);
            Spanned showGroupText = Html.fromHtml("<font>" + "跑团已组织了 " + "</font><font" +
                    " color=\"#F06522\" ><big> " + group.getPartycount() + " </big></font><font> 次活动</font>");
//            mMoreGroupPartyBtn.setText(showGroupText);
            mMoreGroupPartyTv.setText(showGroupText);
            if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.APPLY) {
                mApplyInGroupBtn.setText("已申请");
                mApplyInGroupBtn.setBackgroundResource(R.drawable.bg_round_rect_disable);
                mApplyInGroupBtn.setEnabled(false);
            } else if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.NOT_MEMBER) {
                mApplyInGroupBtn.setText("申请入团");
                mApplyInGroupBtn.setBackgroundResource(R.drawable.bg_round_rect);
                mApplyInGroupBtn.setEnabled(true);
            } else if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.HAS_APPRO) {
                mApplyInGroupBtn.setText("接受邀请");
                mApplyInGroupBtn.setBackgroundResource(R.drawable.bg_round_rect);
                mApplyInGroupBtn.setEnabled(true);
            } else {
                // TODO: 2016/2/19
                //DO ANYTHING
            }
        }

        //团长头像
        ImageUtils.loadUserImage(group.getOwneravatar(), mGroupOrganizerPhotoCi);
        mGroupOrganizerNameTv.setText(group.getOwnernickname());
        //管理员
        showGroupAdmin();
        //周配速
        mGroupWeekSpeedTv.setText(TimeUtils.formatSecondsToSpeedTime(group.getAvgpace(), "'", "''"));
        //周人均公里数
        mGroupWeekKmTv.setText(NumUtils.retainTheDecimal(group.getAvgweeklength()) + "");

        //显示排名
        showRankingAvatar();

        //显示近期活动
        showRecentParty();


        if (group.getRole() == AppEnum.groupRole.MANAGER || group.getRole() == AppEnum.groupRole.OWNER) {
            //创建活动按钮
            mCreateGroupPartyBtn.setVisibility(View.VISIBLE);
            //图片是否可编辑按钮
            mGroupPictureEditIv.setVisibility(View.VISIBLE);
            //大图片可点击
            mGroupPictureIv.setEnabled(true);
        } else {
            mCreateGroupPartyBtn.setVisibility(View.GONE);
            mGroupPictureEditIv.setVisibility(View.GONE);
            mGroupPictureIv.setEnabled(false);
        }
    }

    /**
     * 显示跑团的tag
     */
    private void showGroupTag() {
        List<GetGroupDetailInfoRequest.GroupDetailInfo.Label> labelList = mGroupInfo.getRungroup().getLabel();

        if (labelList == null) {
            return;
        }
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
        mGroupInfoTagTv.addTags(tagList);
    }

    /**
     * 显示管理员UI
     */
    private void showGroupAdmin() {
        List<GetGroupDetailInfoRequest.GroupDetailInfo.MemberManager> managerList = mGroupInfo.getRungroup()
                .getManager();
        if (managerList.size() > 0) {
            ImageUtils.loadUserImage(managerList.get(0).getAvatar(), mGroupAdminPhotoCi);
            mGroupAdminNameTv.setText(managerList.get(0).getNickname());
            if (managerList.size() > 1) {
                mGroupAdminNumTv.setVisibility(View.VISIBLE);
                mGroupAdminNumTv.setText("等" + mGroupInfo.getRungroup().getManagercount() + "人");
            } else {
                mGroupAdminNumTv.setVisibility(View.GONE);
            }
        } else {
            mGroupAdminNameTv.setText("暂无");
            mGroupAdminNumTv.setVisibility(View.GONE);
        }
        if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER) {
            mGroupAdminEditIv.setVisibility(View.VISIBLE);
            mGroupAdminLl.setEnabled(true);
        } else {
            mGroupAdminEditIv.setVisibility(View.GONE);
            mGroupAdminLl.setEnabled(false);
        }
    }

    // 显示排行版
    private void showRankingAvatar() {
        List<GetGroupDetailInfoRequest.GroupDetailInfo.Ranking> rankingList = mGroupInfo.getRungroup().getRanking();
        if (rankingList.size() > 0) {
            if (rankingList.size() == 1) {
                mFirstMemberLl.setVisibility(View.VISIBLE);
                mSecondMemberLl.setVisibility(View.GONE);
                mThirdlyMemberLl.setVisibility(View.GONE);
                mRankTipTv.setVisibility(View.VISIBLE);
                showFirstAvatar(rankingList);
                mRankTipTv.setText("还没人抢占第2名，赶紧！");
            } else if (rankingList.size() == 2) {
                mFirstMemberLl.setVisibility(View.VISIBLE);
                mSecondMemberLl.setVisibility(View.VISIBLE);
                mThirdlyMemberLl.setVisibility(View.GONE);
                mRankTipTv.setVisibility(View.VISIBLE);
                showFirstAvatar(rankingList);
                showSecondAvatar(rankingList);
                mRankTipTv.setText("还没人抢占第3名，赶紧！");
            } else if (rankingList.size() > 2) {
                mFirstMemberLl.setVisibility(View.VISIBLE);
                mSecondMemberLl.setVisibility(View.VISIBLE);
                mThirdlyMemberLl.setVisibility(View.VISIBLE);
                mRankTipTv.setVisibility(View.GONE);
                showFirstAvatar(rankingList);
                showSecondAvatar(rankingList);
                showThirdlyAvatar(rankingList);
            }
        } else {
            mFirstMemberLl.setVisibility(View.GONE);
            mSecondMemberLl.setVisibility(View.GONE);
            mThirdlyMemberLl.setVisibility(View.GONE);
            mRankTipTv.setVisibility(View.VISIBLE);
            mRankTipTv.setText("还没人抢占第1名，赶紧！");
        }
    }

    /**
     * 显示第一名的头像及位置
     */
    private void showFirstAvatar(List<GetGroupDetailInfoRequest.GroupDetailInfo.Ranking> rankingList) {
        ImageUtils.loadUserImage(rankingList.get(0).getAvatar(), mFirstMemberAvatarCi);
        mFirstMemberNameTv.setText(rankingList.get(0).getNickname());
        if (rankingList.get(0).getId() == mUserId) {
            mFirstIsOwnIv.setVisibility(View.VISIBLE);
        } else {
            mFirstIsOwnIv.setVisibility(View.GONE);
        }
        mFirstMemberKmTv.setText(NumUtils.retainTheDecimal(rankingList.get(0).getLength()) + "");
        int mAvatarMargin = (int) (DeviceUtils.getScreenWidth() - DeviceUtils.dpToPixel(190)) * mFirstMemberShowPb
                .getProgress() / 100;
        if (mAvatarMargin < 0) {
            mAvatarMargin = 0;
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mFirstMemberAvatarCi.getLayoutParams();
        params.setMargins(mAvatarMargin, 0, 0, 0);
        mFirstMemberAvatarCi.setLayoutParams(params);
    }

    /**
     * 显示第二名的头像及位置
     */
    private void showSecondAvatar(List<GetGroupDetailInfoRequest.GroupDetailInfo.Ranking> rankingList) {
        int mPbPercent = (int) (rankingList.get(1).getLength() / rankingList.get(0).getLength() * 100);
        mSecondMemberShowPb.setProgress(mPbPercent);
        ImageUtils.loadUserImage(rankingList.get(1).getAvatar(), mSecondMemberAvatarCi);
        mSecondMemberNameTv.setText(rankingList.get(1).getNickname());
        if (rankingList.get(1).getId() == mUserId) {
            mSecondIsOwnIv.setVisibility(View.VISIBLE);
        } else {
            mSecondIsOwnIv.setVisibility(View.GONE);
        }
        mSecondMemberKmTv.setText(NumUtils.retainTheDecimal(rankingList.get(1).getLength()) + "");
        int mAvatarMargin = (int) ((int) (DeviceUtils.getScreenWidth() - DeviceUtils.dpToPixel(161)) * mSecondMemberShowPb
                .getProgress() / 100 - DeviceUtils.dpToPixel(20));
        if (mAvatarMargin < 0) {
            mAvatarMargin = 0;
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mSecondMemberAvatarCi.getLayoutParams();
        params.setMargins(mAvatarMargin, 0, 0, 0);
        mSecondMemberAvatarCi.setLayoutParams(params);
    }

    /**
     * 显示第三名的头像及位置
     */
    private void showThirdlyAvatar(List<GetGroupDetailInfoRequest.GroupDetailInfo.Ranking> rankingList) {
        int mPbPercent = (int) (rankingList.get(2).getLength() / rankingList.get(0).getLength() * 100);
        mThirdlyMemberShowPb.setProgress(mPbPercent);
        ImageUtils.loadUserImage(rankingList.get(2).getAvatar(), mThirdlyMemberAvatarCi);

        mThirdlyMemberNameTv.setText(rankingList.get(2).getNickname());
        if (rankingList.get(2).getId() == mUserId) {
            mThirdlyIsOwnIv.setVisibility(View.VISIBLE);
        } else {
            mThirdlyIsOwnIv.setVisibility(View.GONE);
        }
        mThirdlyMemberKmTv.setText(NumUtils.retainTheDecimal(rankingList.get(2).getLength()) + "");
        int mAvatarMargin = (int) ((int) (DeviceUtils.getScreenWidth() - DeviceUtils.dpToPixel(161)) * mThirdlyMemberShowPb
                .getProgress() / 100 - DeviceUtils.dpToPixel(20));
        if (mAvatarMargin < 0) {
            mAvatarMargin = 0;
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mThirdlyMemberAvatarCi.getLayoutParams();
        params.setMargins(mAvatarMargin, 0, 0, 0);
        mThirdlyMemberAvatarCi.setLayoutParams(params);
    }

    /**
     * 显示近期活动
     */
    private void showRecentParty() {
        List<GetGroupDetailInfoRequest.GroupDetailInfo.Party> partyList = mGroupInfo.getRungroup().getParty();
        if (partyList.size() > 0) {
            mGroupPartyAllLl.setVisibility(View.VISIBLE);
            mNoGroupPartyAll.setVisibility(View.GONE);
            mGroupPartyNameTv_1.setText(partyList.get(0).getName());
            mGroupPartyLocationTv_1.setText(partyList.get(0).getLocation());
            if (partyList.get(0).getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
                mGroupPartyStateTv_1.setText(TimeUtils.getTimestampAfterString(TimeUtils.stringToDateAndMinS
                        (partyList.get(0).getEndtime())) + "结束");
                mGroupPartyStateTv_1.setTextColor(getResources().getColor(R.color.colorPrimary));
                mGroupPartyUnderwayIv_1.setVisibility(View.VISIBLE);
            } else if (partyList.get(0).getStatus() == AppEnum.groupPartyStatus.REGIST_START) {
                mGroupPartyStateTv_1.setText(TimeUtils.getTimestampAfterString(TimeUtils.stringToDateAndMinS
                        (partyList.get(0).getStarttime())) + "开始");
                mGroupPartyStateTv_1.setTextColor(getResources().getColor(R.color.grey_dark));
                mGroupPartyUnderwayIv_1.setVisibility(View.GONE);
            } else if (partyList.get(0).getStatus() == AppEnum.groupPartyStatus.PARTY_STOP) {
                mGroupPartyStateTv_1.setText(TimeUtils.getTimestampString(TimeUtils.stringToDateAndMinS(partyList.get
                        (0).getEndtime())) + "结束");
                mGroupPartyStateTv_1.setTextColor(getResources().getColor(R.color.grey_dark));
                mGroupPartyUnderwayIv_1.setVisibility(View.GONE);
            }

            if (partyList.size() == 1) {
                mGroupPartyLl_2.setVisibility(View.GONE);
                mNoGroupParty_2.setVisibility(View.VISIBLE);
            } else {
                mGroupPartyLl_2.setVisibility(View.VISIBLE);
                mNoGroupParty_2.setVisibility(View.GONE);

                mGroupPartyNameTv_2.setText(partyList.get(1).getName());
                mGroupPartyLocationTv_2.setText(partyList.get(1).getLocation());
                if (partyList.get(1).getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
                    mGroupPartyStateTv_2.setText(TimeUtils.getTimestampAfterString(TimeUtils.stringToDateAndMinS
                            (partyList.get(1).getEndtime())) + "结束");
                    mGroupPartyStateTv_2.setTextColor(getResources().getColor(R.color.colorPrimary));
                    mGroupPartyUnderwayIv_2.setVisibility(View.VISIBLE);
                } else if (partyList.get(1).getStatus() == AppEnum.groupPartyStatus.REGIST_START) {
                    mGroupPartyStateTv_2.setText(TimeUtils.getTimestampAfterString(TimeUtils.stringToDateAndMinS
                            (partyList.get(1).getStarttime())) + "开始");
                    mGroupPartyStateTv_2.setTextColor(getResources().getColor(R.color.grey_dark));
                    mGroupPartyUnderwayIv_2.setVisibility(View.GONE);
                } else if (partyList.get(1).getStatus() == AppEnum.groupPartyStatus.PARTY_STOP) {
                    mGroupPartyStateTv_2.setText(TimeUtils.getTimestampString(TimeUtils.stringToDateAndMinS(partyList.get
                            (1).getEndtime())) + "结束");
                    mGroupPartyStateTv_2.setTextColor(getResources().getColor(R.color.grey_dark));
                    mGroupPartyUnderwayIv_2.setVisibility(View.GONE);
                } else if (partyList.get(1).getStatus() == AppEnum.groupPartyStatus.PARTY_CANCEL) {
                    mGroupPartyStateTv_2.setText("已取消");
                    mGroupPartyStateTv_2.setTextColor(getResources().getColor(R.color.grey_dark));
                    mGroupPartyUnderwayIv_2.setVisibility(View.GONE);
                }

            }
        } else {
            mGroupPartyAllLl.setVisibility(View.GONE);
            mNoGroupPartyAll.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 更换照片
     */
    private void addPhoto(int Request) {
        Intent intent = new Intent(mContext, MultiImageSelectorActivity.class);
        // 是否显示拍摄图片
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大可选择图片数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
        // 选择模式
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
        startActivityForResult(intent, Request);
    }


    /**
     * 处理申请入团按钮逻辑
     */
    private void doInGroup() {
        if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.NOT_MEMBER) {
            sendApplyGroupRequest();
        } else {
            sendAgreeGroupRequest();
        }
    }

    /**
     * 获取分享跑团的文本
     */
    private void getShareGroupText() {
        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
            return;
        }
        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false);
        HttpUtils https = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_RUN_GROUP_SMS_INVITE;
        final RequestParams params = RequestParamsBuild.BuildGetSMSGroupInviteRequest(mContext, mGroupInfo.getRungroup().getId());
        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                final GetSmsInviteResult result = JSON.parseObject(Response, GetSmsInviteResult.class);
                if (share.WxIsInstall()) {

                    mDialogBuilder.showShareDialog(mContext, "邀请函已复制", result.getText());
                    mDialogBuilder.setShareListener(new MyDialogBuilderV1.ShareDialogListener() {
                        @Override
                        public void onCancelShare() {

                        }

                        @Override
                        public void onGoShare() {
                            //跑团微信邀请埋点记录
                            MaidianData.saveMaidian(mContext, new DBEntityMaidian(System.currentTimeMillis() + "", AppEnum.MaidianType.GroupInviteByWx, TimeUtils.NowTime()));
                            ClipboardManager cbManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData textCd = ClipData.newPlainText("123Go", result.getText());
                            cbManager.setPrimaryClip(textCd);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
                            intent.setPackage("com.tencent.mm");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                } else {
                    T.showShort(mContext, "未安装微信");
                }
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
     * 分享跑团
     */
//    private void shareGroup() {
//        if (mDialogBuilder.progressDialog != null && mDialogBuilder.progressDialog.isShowing()) {
//            return;
//        }
//        mDialogBuilder.showProgressDialog(mContext, "正在获取分享信息..", false, false);
//        // 发送好友请求
//        HttpUtils https = new HttpUtils();
//        String POST_URL = URLConfig.URL_GET_WEIXIN_SHARE_TEXT;
//        final RequestParams params = RequestParamsBuild.BuildGetGroupWxInviteRequest(mGroupInfo.getRungroup().getId());
//        https.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
//
//            @Override
//            protected void onErrorResponse(int errorCode, String errorMsg) {
//                T.showShort(mContext, errorMsg);
//            }
//
//            @Override
//            protected void onRightResponse(String Response) {
//                final GetWxInviteResult result = JSON.parseObject(Response, GetWxInviteResult.class);
//           //     if (result.getTitle() != null && !result.getTitle().isEmpty()) {
//                 //   share.startWxShareUrl(result.getText(), result.getTitle(), result.getLink());
//           //     }
//
//                if (share.WxIsInstall()) {
//
//                    mDialogBuilder.showShareDialog(mContext, "邀请函已复制", result.getText());
//                    mDialogBuilder.setShareListener(new MyDialogBuilderV1.ShareDialogListener() {
//                        @Override
//                        public void onCancelShare() {
//
//                        }
//
//                        @Override
//                        public void onGoShare() {
//                            ClipboardManager cbManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
//                            ClipData textCd = ClipData.newPlainText("123Go", result.getText());
//                            cbManager.setPrimaryClip(textCd);
//                            Intent intent = new Intent(Intent.ACTION_VIEW);
//                            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
//                            intent.setPackage("com.tencent.mm");
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
//                    });
//
//                }else {
//                    T.showShort(mContext, "未安装微信");
//                }
//
//            }
//
//            @Override
//            protected void onFailureResponse(HttpException e, String s) {
//
//            }
//
//            @Override
//            protected void onFinish() {
//                if (mDialogBuilder.progressDialog != null) {
//                    mDialogBuilder.progressDialog.dismiss();
//                }
//            }
//        });
//    }

    /**
     * 进入跑团介绍页面
     */
    private void launchGroupIntroduce() {
        Bundle b = new Bundle();
        b.putInt("groupId", mGroupId);
        startActivity(GroupIntroduceActivity.class, b);
    }

    /**
     * 进入创建活动页面
     */
    private void launchCreateParty() {
        Bundle b = new Bundle();
        GetGroupDetailInfoRequest request = mGroupInfo;
        request.getRungroup().setParty(null);
        b.putSerializable("group", request);
        startActivity(CreateGroupPartyActivity.class, b);
    }

    /**
     * 设置跑团管理员
     */
    private void launchAddGroupManage() {
        Bundle bundle = new Bundle();
        bundle.putInt("groupid", mGroupId);
        startActivity(GroupAddMangerActivity.class, bundle);
    }

    /**
     * 邀请好友
     */
    private void launchApplyFriend() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", mGroupInfo);
        startActivity(GroupApplyFriendListActivity.class, bundle);
    }

    /**
     * 跑团二维码
     */
    private void launchGroupQrCode() {
        Bundle bundle = new Bundle();
        bundle.putString("avatar", mGroupInfo.getRungroup().getAvatar());
        bundle.putString("nickName", mGroupInfo.getRungroup().getName());
        bundle.putString("qrcode", mGroupInfo.getRungroup().getQrcode());
        bundle.putInt("hasSys", 0);
        startActivity(GroupQrCodeActivity.class, bundle);
    }

    /**
     * 解散. 退出跑团
     */
    private void exitGroup() {
        //团长解散跑团
        if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER) {
            Bundle bundle = new Bundle();
            bundle.putInt("groupid", mGroupId);
            startActivity(DismissGroupActivity.class, bundle);
            //非团长离开跑团
        } else {
            showExitGroupDialog();
        }
    }

    /**
     * 排序页面
     */
    private void launchGroupRanking() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", mGroupInfo);
        startActivity(GroupMemberRankingActivity.class, bundle);
    }

    /**
     * 活动详情
     */
    private void launchPartyInfo(int position) {
        Bundle b = new Bundle();
        b.putInt("partyid", mGroupInfo.getRungroup().getParty().get(position).getId());
        startActivity(PartyInfoActivity.class, b);
    }

    /**
     * 活动列表
     */
    private void launchPartyList() {
        Bundle b = new Bundle();
        GetGroupDetailInfoRequest request = mGroupInfo;
        request.getRungroup().setParty(null);
        b.putSerializable("group", request);
        startActivity(GroupPartyListActivity.class, b);
    }

    /**
     * 申请入团
     */
    private void launchApplyInGroup() {
        Bundle b = new Bundle();
        b.putInt("groupid", mGroupId);
        startActivity(GroupApplyMemberListActivity.class, b);
    }


    /**
     * 进入团员主页
     */
    private void launchMemberInfo(int index) {
        GetGroupDetailInfoRequest.GroupDetailInfo.Ranking ranker = mGroupInfo.getRungroup().getRanking().get(index);
        Bundle bundle = new Bundle();
        bundle.putSerializable("group", mGroupInfo);
        bundle.putInt("userid", ranker.getId());
        startActivity(GroupMemberActivity.class, bundle);
    }

    /**
     * 显示菜单
     */
    private void showManagerTitleMenu() {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
        if (isShowMessage) {
            transaction.remove(mMenuFragment);
        } else {
            //   if (mGroupTitleMenuFragment == null) {
            ArrayList<String> mMenuTextList = new ArrayList<String>();
            mMenuTextList.add("邀请好友");
            mMenuTextList.add("入团审核");
            mMenuTextList.add("跑团二维码");

            if (mGroupInfo.getRungroup().getRole() == AppEnum.groupRole.OWNER) {
                mMenuTextList.add("解散跑团");
            } else {
                mMenuTextList.add("退出跑团");
            }

            mMenuFragment = TitleMenuFragment.getInstance(mMenuTextList, new TitleMenuFragment
                    .OnTitleItemClickListener() {
                @Override
                public void OnTitleItemClick(int position) {

                    switch (position) {
                        //邀请好友
                        case 0:
                            launchApplyFriend();
                            break;
                        //入团审核
                        case 1:
                            MessageData.readAllNewGroupMessage(mContext, userid);
                            launchApplyInGroup();
                            break;
                        //跑团二维码
                        case 2:
                            launchGroupQrCode();
                            break;
                        //解散跑团 退出跑团
                        case 3:
                            exitGroup();
                            break;
                    }
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
                    transaction.remove(mMenuFragment).commit();
                    isShowMessage = !isShowMessage;
                }
            });

            transaction.add(R.id.ll_fragment_message, mMenuFragment);
//            } else {
//                transaction.show(mGroupTitleMenuFragment);
//            }
        }
        isShowMessage = !isShowMessage;
        transaction.commit();
    }

    /**
     * 显示一般人的权限
     */
    private void showMemberTitleMenu() {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.title_menu_in, R.anim.title_menu_out);
        if (isShowMessage) {
            transaction.remove(mMenuFragment);
        } else {
            //   if (mGroupTitleMenuFragment == null) {
            ArrayList<String> mMenuTextList = new ArrayList<String>();
            mMenuTextList.add("推荐好友");
            mMenuTextList.add("跑团二维码");
            mMenuTextList.add("退出跑团");

            mMenuFragment = TitleMenuFragment.getInstance(mMenuTextList, new TitleMenuFragment
                    .OnTitleItemClickListener() {
                @Override
                public void OnTitleItemClick(int position) {

                    switch (position) {
                        //邀请好友
                        case 0:
                            launchApplyFriend();
                            break;
                        //跑团二维码
                        case 1:
                            launchGroupQrCode();
                            break;
                        //解散跑团 退出跑团
                        case 2:
                            exitGroup();
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
     * 显示退出跑团的对话框
     */
    private void showExitGroupDialog() {
        mDialogBuilder.showChoiceTwoBtnDialog(mContext, "提醒", "是否退出跑团", "取消", "退出");
        mDialogBuilder.setListener(new MyDialogBuilderV1.ChoiceTwoBtnDialogListener() {
            @Override
            public void onLeftBtnClick() {

            }

            @Override
            public void onRightBtnClick() {
                postExitRequest();
            }

            @Override
            public void onCancel() {
            }
        });
    }

    /**
     * 获取跑团信息
     */
    private void getGroupInfo() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_GROUP_DETAIL_INFO;
        RequestParams params = RequestParamsBuild.buildGetGroupDetailInfoRequest(mContext, mGroupId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {

                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                Log.v(TAG, Response);
                mGroupInfo = JSON.parseObject(Response, GetGroupDetailInfoRequest.class);
                //若是好友
                if (handler != null) {
                    handler.sendEmptyMessage(MSG_POST_OK);
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 发送申请入团请求
     */
    private void sendApplyGroupRequest() {
        mDialogBuilder.showProgressDialog(mContext, "发送中..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_APPLY_JOIN_GROUP;
        RequestParams params = RequestParamsBuild.buildApplyJoinGroupRequest(mContext, mGroupId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "已申请");
                mApplyInGroupBtn.setText("已申请");
                mApplyInGroupBtn.setBackgroundResource(R.drawable.bg_round_rect_disable);
                mApplyInGroupBtn.setEnabled(false);
                updateData();
                updateViews();
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
     * 发送同意入团的请求
     */
    private void sendAgreeGroupRequest() {
        mDialogBuilder.showProgressDialog(mContext, "处理中...", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_ACCEPT_JOIN_GROUP;
        RequestParams params = RequestParamsBuild.buildAcceptJoinGroupRequest(mContext, mGroupId);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "已加入");
                mApplyInGroupLl.setVisibility(View.GONE);
                updateData();
                updateViews();
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
     * 发送退出跑团的请求
     */
    private void postExitRequest() {
        mDialogBuilder.showProgressDialog(mContext, "正在请求退出跑团..", false);
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GROUP_REMOVE_MEMBER;
        RequestParams params = RequestParamsBuild.buildRemoveGroupMemberRequest(mContext, mGroupInfo.getRungroup().getId(),
                LocalApplication.getInstance().getLoginUser(mContext).getUserId());
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);

            }

            @Override
            protected void onRightResponse(String Response) {
                T.showShort(mContext, "您已退出跑团");
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

    /**
     * 上传头像到服务器
     */
    private void postAvatar(final File mAvatarF) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPLOAD_AVATAR;
        RequestParams params = RequestParamsBuild.buildUploadAvatarRequest(mContext, mAvatarF);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = errorMsg;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onRightResponse(String Response) {
                GetUploadAvatarResult resultEntity = JSON.parseObject(Response, GetUploadAvatarResult.class);
                postGroupPicture(resultEntity.getUrl(), mAvatarF);
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {
                if (handler != null) {
                    Message msg = new Message();
                    msg.what = MSG_POST_ERROR;
                    msg.obj = s;
                    handler.sendMessage(msg);
                }
            }

            @Override
            protected void onFinish() {

            }
        });
    }

    /**
     * 修改跑团图片信息
     */
    private void postGroupPicture(final String wallpaper, final File mAvatarF) {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_UPDATE_GROUP_INFO;
        RequestParams params = RequestParamsBuild.buildSetGroupPictureRequest(mContext, mGroupInfo.getRungroup().getId()
                , wallpaper);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {

            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
                T.showShort(mContext, errorMsg);
            }

            @Override
            protected void onRightResponse(String Response) {
//                ImageUtils.loadGroupImage(mBitmapUtils, avatar, mAvatarIv);
                bitmapUtils.display(mGroupPictureIv, String.valueOf(Uri.fromFile(mAvatarF)));
                T.showShort(mContext, "上传成功");
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

    private void updateApplyGroupBut() {
        applyGroupNum = mMessageList.size();
        if (applyGroupNum == 0) {
            badgeView.hide();
        } else {
            badgeView.show();
            badgeView.setText(applyGroupNum + "");
        }
    }
}
