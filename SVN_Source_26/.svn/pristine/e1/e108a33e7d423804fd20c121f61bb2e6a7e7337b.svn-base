package com.app.pao.fragment.history;

import android.graphics.Typeface;
import android.widget.ListView;

import com.app.pao.R;
import com.app.pao.adapter.FriendHistorySpliteListAdapter;
import com.app.pao.entity.model.HistorySpliteEntity;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/11/27.
 * 跑步历史 - 分段页面
 */
public class FriendHistorySpliteListFragmentV2 extends BaseFragment {

    /**
     * local data
     **/
    private FriendHistorySpliteListAdapter mAdapter;
    List<HistorySpliteEntity> mSpliteList = new ArrayList<HistorySpliteEntity>();//分段列表

    private int minSpeed = 0;
    private Typeface typeFace;//字体

    /* local view */
    @ViewInject(R.id.tv_agv_time_speed)
    private TextView mAvgTimeSpeedTv;// 平均配速文本
    @ViewInject(R.id.tv_max_time_speed)
    private TextView mMaxTimeSpeedTv;// 最快配速文本
    @ViewInject(R.id.listview)
    private ListView mLapLv;// 分段列表
    @ViewInject(R.id.tv_heartrate)
    private TextView mHeartrateTv;

    public static FriendHistorySpliteListFragmentV2 newInstance() {
        FriendHistorySpliteListFragmentV2 fragment = new FriendHistorySpliteListFragmentV2();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_splite_list;
    }

    @Override
    protected void initParams() {
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        mAvgTimeSpeedTv.setTypeface(typeFace);
        mMaxTimeSpeedTv.setTypeface(typeFace);
    }

    /**
     * 更新界面
     *
     * @param spliteList
     * @param hasHeartrate
     * @param avgPace
     */
    public void updateView(final List<HistorySpliteEntity> spliteList,
                           boolean hasHeartrate, int avgPace,float totalLength,
                           long duration) {
        //获取分段数据
        mSpliteList = spliteList;

        //找最快配速和最慢配速
        int maxSpeed = 0;
        if (mSpliteList != null && mSpliteList.size() > 0) {// 防止数组越界
            if (mSpliteList.get(0).getLength() != 0) {
                minSpeed = (int) (mSpliteList.get(0).getDuration() * 1000 / mSpliteList
                        .get(0).getLength());
                // 获取最快配速, 最慢配速
                for (int i = 0; i < mSpliteList.size(); i++) {
                    int tempSpeed = (int) (mSpliteList.get(i).getDuration() * 1000 / mSpliteList
                            .get(i).getLength());
                    if (tempSpeed > maxSpeed) {
                        maxSpeed = tempSpeed;
                    }
                    if (tempSpeed != 0 && tempSpeed < minSpeed) {
                        minSpeed = tempSpeed;
                    }
                }
            }
        }
        //显示统计数据
        if (mSpliteList != null) {
            mAvgTimeSpeedTv.setText(TimeUtils.formatSecondsToSpeedTime(avgPace));
            mMaxTimeSpeedTv.setText(TimeUtils
                    .formatSecondsToSpeedTime(minSpeed));
            mAdapter = new FriendHistorySpliteListAdapter(getActivity(), mSpliteList,
                    maxSpeed, hasHeartrate,totalLength,duration);
            mLapLv.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        causeGC();
    }

    private void causeGC() {
        mSpliteList.clear();
    }
}
