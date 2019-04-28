package com.app.pao.activity.group;

import android.view.View;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.FriendInfoAdapter;
import com.app.pao.adapter.GroupMemberInfoAdapter;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.ui.widget.CustomViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TabPageIndicator;
import com.app.pao.adapter.GroupMemberInfoAdapter.Tab;

/**
 * Created by Administrator on 2016/1/28.
 * 跑团成员 排行页面
 */
@ContentView(R.layout.activity_group_member_ranking)
public class GroupMemberRankingActivity extends BaseAppCompActivity implements View.OnClickListener {
    @ViewInject(R.id.vp_ranking_page)
    private CustomViewPager mRankingVp;
    @ViewInject(R.id.tbi_friend)
    private TabPageIndicator mTpi;//Tab控件

    private GroupMemberInfoAdapter memberRankingAdapter;
    private Tab[] mItems;
    private GetGroupDetailInfoRequest mGroupInfo;
    private int mGroupId;


    @Override
    @OnClick({ R.id.title_bar_left_menu})
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.title_bar_left_menu:
                finish();
                break;
        }
    }


    @Override
    protected void initData() {
        mGroupInfo = (GetGroupDetailInfoRequest) getIntent().getSerializableExtra("group");
        mGroupId = mGroupInfo.getRungroup().getId();
    }

    @Override
    protected void initViews() {
        mItems = new GroupMemberInfoAdapter.Tab[]{GroupMemberInfoAdapter.Tab.FRIEND, GroupMemberInfoAdapter.Tab.RANK};
        memberRankingAdapter = new GroupMemberInfoAdapter(getSupportFragmentManager(),mItems);
        mRankingVp.setAdapter(memberRankingAdapter);
        mRankingVp.setOffscreenPageLimit(1);
        mTpi.setViewPager(mRankingVp);
        mRankingVp.setCurrentItem(0);
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {
        int userId = LocalApplication.getInstance().getLoginUser(mContext).getUserId();
    }

    @Override
    protected void updateViews() {
    }

    @Override
    protected void destroy() {

    }

    /* get or set */

    public int getmGroupId() {
        return mGroupId;
    }

    public GetGroupDetailInfoRequest getmGroupInfo() {
        return mGroupInfo;
    }


}
