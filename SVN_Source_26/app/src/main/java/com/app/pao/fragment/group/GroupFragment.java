package com.app.pao.fragment.group;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.main.MainActivityV2;
import com.app.pao.adapter.GroupInfoAdapter;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.ui.widget.CustomViewPager;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.ImageButton;
import com.rey.material.widget.TabPageIndicator;
import com.app.pao.adapter.GroupInfoAdapter.Tab;

/**
 * Created by Administrator on 2016/1/22.
 * <p>
 * 跑团分类
 */
public class GroupFragment extends BaseFragment implements View.OnClickListener {

    /* contains */
    private static final int TYPE_FRAGMENT_GROUP_LIST = 0;//跑团列表
    private static final int TYPE_FRAGMENT_PARTY_LIST = 1;//活动列表

    private static final int MSG_POST_ERROR = 0;
    private static final int MSG_POST_OK = 1;

    /* local view */
    @ViewInject(R.id.btn_add_group)
    private ImageButton mTitleFunIbtn;
    @ViewInject(R.id.vp_group)
    private CustomViewPager mViewPager;
    @ViewInject(R.id.tbi_group)
    private TabPageIndicator mTpi;//Tab控件

    /* local data */
    //Fragment 管理
    private FragmentManager fragmentManager;

    public static GroupFragment newInstance() {
        GroupFragment fragment = new GroupFragment();
        return fragment;
    }


    //当前的
    private GroupListFragmentReplace mGroupListFragment;
    private GroupMyPartyListFragment mPartyListFragment;
    private int mUserId;
    private boolean isFirstIn = true;

    private Tab[] mItems;
    private GroupInfoAdapter mGroupAdapter;

    @Override
    @OnClick({R.id.btn_add_group})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_group:
                String groupName = ((GroupListFragmentReplace)(mGroupAdapter.getItem(0))).checkHasGroup();
                MainActivityV2 activity = (MainActivityV2) getActivity();
                if (groupName != null) {
                    activity.showGroupTitleMenuList(true, groupName);
                } else {
                    activity.showGroupTitleMenuList(false, groupName);
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initParams() {
        mUserId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
//        mGroupListFragment = GroupListFragmentReplace.newInstance();
//        mPartyListFragment = GroupMyPartyListFragment.newInstance();
        fragmentManager = getChildFragmentManager();
        initView();
    }

    private void initView(){
        mItems = new Tab[]{Tab.GROUP, Tab.PARTY};
        mGroupAdapter = new GroupInfoAdapter(getFragmentManager(),mItems);
        mViewPager.setAdapter(mGroupAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTpi.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /* get or set*/
    public int getmUserId() {
        return mUserId;
    }

    public ImageButton getmTitleFunIbtn() {
        return mTitleFunIbtn;
    }
}
