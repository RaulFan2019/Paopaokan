package com.app.pao.activity.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.activity.BaseActivityV3;
import com.app.pao.fragment.dynamic.TouristDynamicFragmentV2;
import com.app.pao.fragment.friend.TouristFriendFragmentV2;
import com.app.pao.fragment.group.TouristGroupFragmentV2;
import com.app.pao.fragment.run.ReadyRunFragment;
import com.app.pao.fragment.settings.TouristUserFragmentV2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Raul.Fan on 2016/9/13.
 */
public class TouristMainActivity extends BaseActivityV3 {

    /* contains */
    public static final int TAB_DYNAMIC = 0;
    public static final int TAB_FRIEND = 1;
    public static final int TAB_RUN = 2;
    public static final int TAB_GROUP = 4;
    public static final int TAB_USER = 3;


    /* local view*/
    @BindView(R.id.iv_dynamic)
    ImageView mDynamicIv;//动态图标
    @BindView(R.id.tv_dynamic)
    TextView mDynamicTv;//动态文本
    @BindView(R.id.iv_group_is_read)
    ImageView mGroupIsReadIv;//团消息是否已读
    @BindView(R.id.iv_group)
    ImageView mGroupIv;//团图标
    @BindView(R.id.tv_group)
    TextView mGroupTv;//团文本
    @BindView(R.id.iv_run)
    ImageView mRunIv;//跑步图标
    @BindView(R.id.tv_run)
    TextView mRunTv;//跑步文本
    @BindView(R.id.iv_friend_is_read)
    ImageView mFriendIsReadIv;//好友消息是否已读
    @BindView(R.id.iv_friend)
    ImageView mFriendIv;//好友图标
    @BindView(R.id.tv_friend)
    TextView mFriendTv;//好友文本
    @BindView(R.id.iv_me)
    ImageView mMeIv;//我的图标
    @BindView(R.id.tv_me)
    TextView mMeTv;//我的文本

    /* local data */
    private TouristDynamicFragmentV2 mDynamicFragment;//游客动态页面
    private TouristGroupFragmentV2 mGroupFragment;//游客跑团页面
    private TouristFriendFragmentV2 mFriendFragment;//游客好友页面
    private TouristUserFragmentV2 mUserFragment;//游客用户页面
    private ReadyRunFragment mReadyRunFragment;//跑步页面

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_v2;
    }

    @OnClick({R.id.ll_dynamic, R.id.ll_group, R.id.ll_run, R.id.ll_friend, R.id.ll_me})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击动态
            case R.id.ll_dynamic:
                setTab(TAB_DYNAMIC);
                break;
            //点击团
            case R.id.ll_group:
                setTab(TAB_GROUP);
                break;
            //点击开始跑步
            case R.id.ll_run:
                setTab(TAB_RUN);
                break;
            //点击好友
            case R.id.ll_friend:
                setTab(TAB_FRIEND);
                break;
            //点击我
            case R.id.ll_me:
                setTab(TAB_USER);
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViews() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        mFriendIsReadIv.setVisibility(View.INVISIBLE);
        mGroupIsReadIv.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void doMyCreate() {
        setTab(TAB_RUN);
    }

    @Override
    protected void causeGC() {

    }

    /**
     * 选择TAB
     *
     * @param index
     */
    private void setTab(final int index) {
        resetBtn();
        // 开启一个Fragment事务
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragments(transaction);
        switch (index) {
            //点击动态
            case TAB_DYNAMIC:
                mDynamicTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mDynamicIv.setImageResource(R.drawable.icon_dynamic_selected);
                if (mDynamicFragment == null) {
                    mDynamicFragment = TouristDynamicFragmentV2.newInstance();
                    transaction.add(R.id.ll_fragment_root, mDynamicFragment);
                } else {
                    transaction.show(mDynamicFragment);
                }
                break;
            //点击好友
            case TAB_FRIEND:
                mFriendTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mFriendIv.setImageResource(R.drawable.icon_friend_selected);
                if (mFriendFragment == null) {
                    mFriendFragment = TouristFriendFragmentV2.newInstance();
                    transaction.add(R.id.ll_fragment_root, mFriendFragment);
                } else {
                    transaction.show(mFriendFragment);
                }
                break;
            //点击跑团
            case TAB_GROUP:
                mGroupTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mGroupIv.setImageResource(R.drawable.icon_group_selected);
                if (mGroupFragment == null) {
                    mGroupFragment = TouristGroupFragmentV2.newInstance();
                    transaction.add(R.id.ll_fragment_root, mGroupFragment);
                } else {
                    transaction.show(mGroupFragment);
                }
                break;
            //点击跑步
            case TAB_RUN:
                mRunTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                mRunIv.setImageResource(R.drawable.icon_run_selected);
                if (mReadyRunFragment == null) {
                    mReadyRunFragment = ReadyRunFragment.newInstance();
                    transaction.add(R.id.ll_fragment_root, mReadyRunFragment);
                } else {
                    transaction.show(mReadyRunFragment);
                }
                break;
            //点击我
            case TAB_USER:
                mMeIv.setImageResource(R.drawable.icon_me_selected);
                mMeTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Selected));
                if (mUserFragment == null) {
                    mUserFragment = TouristUserFragmentV2.newInstance();
                    transaction.add(R.id.ll_fragment_root, mUserFragment);
                } else {
                    transaction.show(mUserFragment);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }


    /**
     * 清除掉所有的选中状态。
     */
    private void resetBtn() {
        mDynamicTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mGroupTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mFriendTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mMeTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));
        mRunTv.setTextColor(getResources().getColor(R.color.Tab_TextView_Noraml));

        mDynamicIv.setImageResource(R.drawable.icon_dynamic_normal);
        mGroupIv.setImageResource(R.drawable.icon_group_normal);
        mFriendIv.setImageResource(R.drawable.icon_friend_normal);
        mMeIv.setImageResource(R.drawable.icon_me_normal);
        mRunIv.setImageResource(R.drawable.icon_run_normal);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mDynamicFragment != null) {
            transaction.hide(mDynamicFragment);
        }
        if (mFriendFragment != null) {
            transaction.hide(mFriendFragment);
        }
        if (mGroupFragment != null) {
            transaction.hide(mGroupFragment);
        }
        if (mUserFragment != null) {
            transaction.hide(mUserFragment);
        }
        if (mReadyRunFragment != null) {
            transaction.hide(mReadyRunFragment);
        }
    }
}
