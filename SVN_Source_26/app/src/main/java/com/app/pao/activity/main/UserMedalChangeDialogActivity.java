package com.app.pao.activity.main;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.MedalChangeAdapter;
import com.app.pao.entity.model.UserOptMedalEntity;
import com.app.pao.ui.widget.CircleFlowIndicator;
import com.app.pao.ui.widget.ViewFlow;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
@ContentView(R.layout.activity_user_medal_change_dialog)
public class UserMedalChangeDialogActivity extends BaseAppCompActivity implements View.OnClickListener {
    @ViewInject(R.id.tv_medal_change_title)
    private TextView mMedalTitleTv;
    @ViewInject(R.id.tv_medal_change_tip)
    private TextView mMedalTipTv;
    @ViewInject(R.id.vf_medal_change)
    private ViewFlow mMedalChangeVf;
    @ViewInject(R.id.cfi_medal_change_point)
    private CircleFlowIndicator mPointCfi;

    List<UserOptMedalEntity> mChangeMedalList;

    @Override
    @OnClick({R.id.ll_medal_change_dialog,R.id.ll_dialog_content})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_medal_change_dialog:
                finish();
                break;
            case R.id.ll_dialog_content:
                break;
        }
    }

    @Override
    protected void initData() {
        mChangeMedalList = (List<UserOptMedalEntity>) getIntent().getExtras().getSerializable("changeList");
    }

    @Override
    protected void initViews() {
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mMedalTitleTv.setText("你有" + mChangeMedalList.size() + "枚奖牌发生变化");

        mPointCfi.setFillColor(getResources().getColor(R.color.grey_check));
        mPointCfi.setStrokeColor(getResources().getColor(R.color.grey_uncheck));
        mPointCfi.setInactiveType(CircleFlowIndicator.STYLE_FILL);
        mMedalChangeVf.setAdapter(new MedalChangeAdapter(mContext, mChangeMedalList));
        mMedalChangeVf.setmSideBuffer(mChangeMedalList.size());
        mMedalChangeVf.setFlowIndicator(mPointCfi);
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
