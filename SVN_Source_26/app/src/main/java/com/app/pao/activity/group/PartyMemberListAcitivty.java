package com.app.pao.activity.group;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.PartyMemberListAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetPartyMemberResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/12/7.
 * 活动成员列表界面
 */
@ContentView(R.layout.activity_party_member_list)
public class PartyMemberListAcitivty extends BaseAppCompActivity {

    /* contains */
    private static final String TAG = "PartyMemberListAcitivty";

    /* local data */
    private List<GetPartyMemberResult> mMemberList;//参与活动的人员列表
    private int hasLogin;//已签到人数
    private BitmapUtils mBitmapU;

    private PartyMemberListAdapter mAdapter;//适配器
    /* local view */
    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.iv_owner_photo)
    private CircularImage mOwnerIv;//组织者头像
    @ViewInject(R.id.tv_nickname)
    private TextView mOwnerTv;//组织者名称
    @ViewInject(R.id.iv_gender)
    private ImageView mOwnerGenderIv;//组织者性别
    @ViewInject(R.id.listview)
    private ListView mListView;
    @ViewInject(R.id.tv_member_count)
    private TextView mCountTv;

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
        mMemberList = (List<GetPartyMemberResult>) getIntent().getExtras().getSerializable("member");
        hasLogin = 0;
        for (int i = 0; i < mMemberList.size(); i++) {
            if (mMemberList.get(i).getPersonstatus() == AppEnum.groupPartyPersonStatus.LOGIN) {
                hasLogin++;
            }
        }
        mBitmapU = new BitmapUtils(mContext);
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
        //组织者头像
        ImageUtils.loadUserImage(mMemberList.get(0).getAvatar(), mOwnerIv);
        //组织者姓名
        mOwnerTv.setText(mMemberList.get(0).getNickname());
        //组织者性别
        if (mMemberList.get(0).getGender() == AppEnum.UserGander.MAN) {
            mOwnerGenderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mOwnerGenderIv.setBackgroundResource(R.drawable.icon_women);
        }
        mCountTv.setText("已报名成员(" + hasLogin + "/" + (mMemberList.size() - 1) + ")");
        mAdapter = new PartyMemberListAdapter(mContext, mMemberList);
        mListView.setAdapter(mAdapter);
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

    }
}
