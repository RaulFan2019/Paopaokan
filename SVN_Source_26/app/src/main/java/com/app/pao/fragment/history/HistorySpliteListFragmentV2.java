package com.app.pao.fragment.history;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ListView;

import com.app.pao.R;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.adapter.HistorySpliteListAdapter;
import com.app.pao.data.db.HeartrateData;
import com.app.pao.data.db.SpliteData;
import com.app.pao.entity.db.DBEntitySplite;
import com.app.pao.entity.db.DBEntityWorkout;
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
public class HistorySpliteListFragmentV2 extends BaseFragment {

    /**
     * local data
     **/
    private HistorySpliteListAdapter mAdapter;
    private String workoutName;// 跑步历史名称
    private DBEntityWorkout mDbWorkout;// 跑步历史对象
    List<DBEntitySplite> mSpliteList = new ArrayList<DBEntitySplite>();
    private boolean hasHeartrate = true;
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

    public static HistorySpliteListFragmentV2 newInstance() {
        HistorySpliteListFragmentV2 fragment = new HistorySpliteListFragmentV2();
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

    public void updateView() {
        //获取分段数据
        HistoryInfoActivityV2 activity = (HistoryInfoActivityV2) getActivity();
        workoutName = activity.getWorkoutName();
        mDbWorkout = activity.getDBWorkout();
        mSpliteList = SpliteData.getSpliteFromWorkout(mContext, workoutName);

        //检查是否有心率数据
        if (HeartrateData.getAllHeartrateFromWork(mContext, workoutName).size() == 0) {
            mHeartrateTv.setVisibility(View.GONE);
            hasHeartrate = false;
        }
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
        if (mDbWorkout != null && mSpliteList != null) {
            mAvgTimeSpeedTv.setText(TimeUtils
                    .formatSecondsToSpeedTime((long) (mDbWorkout.duration * 1000 / mDbWorkout.length)));
            mMaxTimeSpeedTv.setText(TimeUtils
                    .formatSecondsToSpeedTime(minSpeed));
            mAdapter = new HistorySpliteListAdapter(getActivity(), mSpliteList,
                    maxSpeed, hasHeartrate,mDbWorkout.length,mDbWorkout.duration);
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
        mDbWorkout = null;
    }
}
