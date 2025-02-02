package com.app.pao.fragment.friend;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.ApplyFriendListActivity;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.adapter.FriendInfoAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.db.DBEntityMessage;
import com.app.pao.entity.event.EventJpush;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.ui.widget.BadgeView;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.CustomViewPager;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TabPageIndicator;
import com.rey.material.widget.TextView;
import com.app.pao.adapter.FriendInfoAdapter.Tab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LY on 2016/3/16.
 */
public class FriendListFragment extends BaseFragment implements View.OnClickListener {
    /* contains*/
    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;
    private static final int SEARCH_FRIEND_OK = 2;

    @ViewInject(R.id.ll_join_friend)
    private LinearLayout mFriendApplyLl;
    @ViewInject(R.id.vp_ranking_page)
    private CustomViewPager mRankingVp;
    @ViewInject(R.id.tbi_friend)
    private TabPageIndicator mTpi;//Tab控件

    //申请好友的布局
    @ViewInject(R.id.iv_join_member_1)
    private CircularImage mJoinIv1;
    @ViewInject(R.id.iv_join_member_2)
    private CircularImage mJoinIv2;
    @ViewInject(R.id.iv_join_member_3)
    private CircularImage mJoinIv3;
    @ViewInject(R.id.tv_join_member)
    private TextView mJoinTv;
    @ViewInject(R.id.btn_apply_friend)
    private ImageButton mApplyBut;
    private BadgeView badgeView;

    /* local data */
    private static List<DBEntityMessage> mMessageList = new ArrayList<>();//消息列表

    private BitmapUtils mBitmapU;
    private boolean isFirstIn;
    private int userid;

    private Tab[] mItems;
    private FriendInfoAdapter mFriendAdapter;

    public static FriendListFragment newInstance() {
        FriendListFragment fragment = new FriendListFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friendlist_replace;
    }

    @Override
    protected void initParams() {
        initData();
        initView();
    }

    private void initData() {
        userid = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
    }

    private void initView() {
        badgeView = new BadgeView(mContext, mApplyBut);
        badgeView.setTextSize(9);
        badgeView.setBadgeMargin((int) DeviceUtils.dpToPixel(3));
//        badgeView.setBackgroundColor(Color.WHITE);
        badgeView.setBackgroundResource(R.drawable.icon_red_circle);
        badgeView.setTextColor(Color.parseColor("#ffffff"));

        mBitmapU = new BitmapUtils(mContext);
        mItems = new Tab[]{Tab.FRIEND, Tab.RANK};
        mFriendAdapter = new FriendInfoAdapter(getFragmentManager(), mItems);
        mRankingVp.setAdapter(mFriendAdapter);
        mRankingVp.setOffscreenPageLimit(1);
        mTpi.setViewPager(mRankingVp);
        mRankingVp.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        //更新新好友请求数量
        updateApplyFriend();
        updateApplyBut();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }


    @Override
    @OnClick({R.id.ll_join_friend, R.id.iv_no, R.id.btn_add_friend, R.id.btn_apply_friend})
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            //好友申请推送
            case R.id.ll_join_friend:
                startActivity(ApplyFriendListActivity.class);
                break;
            //点击弹出添加好友列表
            case R.id.btn_add_friend:
                MainActivityV2 activity = (MainActivityV2) getActivity();
                activity.showFriendTitleMenuList();
                break;

            case R.id.btn_apply_friend:
                startActivity(ApplyFriendListActivity.class);
                break;

            case R.id.iv_no:
                mFriendApplyLl.setVisibility(View.GONE);
                MessageData.showAllFriendMessage(mContext, userid);
                break;
        }
    }

    /**
     * 接收到Jpush改变
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventJpush event) {
        int type = event.getMsgType();
        if (type < AppEnum.messageType.APPLY_JOIN_RUNGROUP) {
            // 检查邀请数
            updateApplyFriend();
            updateApplyBut();
//            JPushInterface.clearNotificationById(mContext,
//                    event.getNotifactionId());
        }
    }

    /**
     * 更新好友请求数量
     */
    private void updateApplyFriend() {
        mMessageList = MessageData.getFriendApplyNeedShowMsg(mContext, userid);
        doApplyFriend();
    }

    /**
     * 更新申请好友的消息内容
     */
    private void doApplyFriend() {

        if (mMessageList.size() == 0) {
            mFriendApplyLl.setVisibility(View.GONE);
        } else {
            mFriendApplyLl.setVisibility(View.VISIBLE);
            mJoinIv1.setVisibility(View.VISIBLE);
            if (mMessageList.size() == 1) {
                mJoinIv2.setVisibility(View.GONE);
                mJoinIv3.setVisibility(View.GONE);
                ImageUtils.loadUserImage(mMessageList.get(0).getFromuseravatar(), mJoinIv1);
            } else if (mMessageList.size() == 2) {
                mJoinIv2.setVisibility(View.VISIBLE);
                mJoinIv3.setVisibility(View.GONE);
                ImageUtils.loadUserImage(mMessageList.get(0).getFromuseravatar(), mJoinIv1);
                ImageUtils.loadUserImage(mMessageList.get(1).getFromuseravatar(), mJoinIv2);
            } else {
                mJoinIv2.setVisibility(View.VISIBLE);
                mJoinIv3.setVisibility(View.VISIBLE);
                ImageUtils.loadUserImage(mMessageList.get(0).getFromuseravatar(), mJoinIv1);
                ImageUtils.loadUserImage(mMessageList.get(1).getFromuseravatar(), mJoinIv2);
                ImageUtils.loadUserImage(mMessageList.get(2).getFromuseravatar(), mJoinIv3);
            }
            mJoinTv.setText(mMessageList.get(0).getFromusernickname() + "等" + mMessageList.size() + "人请求加您为好友");
        }
    }

    private void updateApplyBut() {
        int applyNum = MessageData.getNewFriendMsgCount(mContext, userid);
        //TEST
//        applyNum = 7;
        if (applyNum == 0) {
            badgeView.hide();
        } else {
            badgeView.show();
            if (applyNum <= 99) {
                badgeView.setText(applyNum + "");
            } else {
                badgeView.setText(99 + "+");
            }
        }
    }
}
