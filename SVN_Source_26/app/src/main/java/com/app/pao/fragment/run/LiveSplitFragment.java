package com.app.pao.fragment.run;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ListView;

import com.app.pao.R;
import com.app.pao.adapter.HistorySpliteListAdapter;
import com.app.pao.adapter.LiveSplitListAdapterV3;
import com.app.pao.entity.model.LiveSplite;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/12.
 * 直播分段页面
 */
public class LiveSplitFragment extends BaseFragment {

    /* local data */
    private LiveSplitListAdapterV3 mAdapter;
    private Typeface typeFace;//字体

    private int minSpeed = 0;

    /* local view */
    @ViewInject(R.id.tv_agv_time_speed)
    private TextView mAvgTimeSpeedTv;// 平均配速文本
    @ViewInject(R.id.tv_max_time_speed)
    private TextView mMaxTimeSpeedTv;// 最快配速文本
    @ViewInject(R.id.listview)
    private ListView mLapLv;// 分段列表
    @ViewInject(R.id.tv_heartrate)
    private TextView mHeartrateTv;

    public static LiveSplitFragment newInstance() {
        LiveSplitFragment fragment = new LiveSplitFragment();
        return fragment;
    }

    public void updateSplitView(List<LiveSplite> mSplitList,String avgPace) {
        Log.v(TAG,"updateSplitView");
        mHeartrateTv.setVisibility(View.GONE);
        //找最快配速和最慢配速
        int maxSpeed = 0;
        if (mSplitList != null && mSplitList.size() > 0) {// 防止数组越界
            if (mSplitList.get(0).length != 0) {
                minSpeed = (int) (mSplitList.get(0).duration * 1000 / mSplitList.get(0).length);
                // 获取最快配速, 最慢配速
                for (int i = 0; i < mSplitList.size(); i++) {
                    int tempSpeed = (int) (mSplitList.get(i).duration * 1000 / mSplitList.get(i).length);
                    if (tempSpeed > maxSpeed) {
                        maxSpeed = tempSpeed;
                    }
                    if (tempSpeed != 0 && tempSpeed < minSpeed) {
                        minSpeed = tempSpeed;
                    }
                }
            }
        }
        mAvgTimeSpeedTv.setText(avgPace);
        mMaxTimeSpeedTv.setText(TimeUtils
                .formatSecondsToSpeedTime(minSpeed));
        mAdapter = new LiveSplitListAdapterV3(getActivity(), mSplitList, maxSpeed, false);
        mLapLv.setAdapter(mAdapter);
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
}
