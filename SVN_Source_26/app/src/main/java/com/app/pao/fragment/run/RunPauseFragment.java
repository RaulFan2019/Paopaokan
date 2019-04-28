package com.app.pao.fragment.run;

import android.graphics.Typeface;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.event.EventRuningHeartrate;
import com.app.pao.fragment.BaseFragmentV2;
import com.app.pao.utils.LengthUtils;
import com.app.pao.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.hwh.sdk.ble.BleManagerService;

/**
 * Created by Raul.Fan on 2016/9/12.
 */
public class RunPauseFragment extends BaseFragmentV2 {

    /* local view */
    @BindView(R.id.tv_run_pause_length)
    TextView mLengthTv;
    @BindView(R.id.tv_run_pause_time)
    TextView mTimeTv;
    @BindView(R.id.tv_run_pause_pace)
    TextView mPaceTv;
    @BindView(R.id.tv_run_pause_heartrate)
    TextView mHrTv;

    /* 构造函数 */
    public static RunPauseFragment newInstance() {
        RunPauseFragment fragment = new RunPauseFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_run_pause;
    }


    /**
     * 收到心率变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRuningHeartrate event) {
        int status = event.status;
        if (status == BleManagerService.MSG_NEW_HEARTBEAT) {
            mHrTv.setText(event.heartrate);
        } else {
            mHrTv.setText("- -");
        }
    }


    @Override
    protected void initParams() {
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        mLengthTv.setTypeface(typeFace);
        mTimeTv.setTypeface(typeFace);
        mPaceTv.setTypeface(typeFace);
        mHrTv.setTypeface(typeFace);
    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onVisible() {
        DBEntityWorkout mWorkout = WorkoutData.getUnFinishWorkout(getActivity(), LocalApplication.getInstance().getLoginUser(getActivity()).userId);
        if (mWorkout == null) {
            return;
        }
        mLengthTv.setText(LengthUtils.formatLength(mWorkout.length));
        mTimeTv.setText(TimeUtils.formatDurationStr(mWorkout.duration));
        if (mWorkout.getLength() == 0) {
            mPaceTv.setText("--:--");
        } else {
            mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime((long) (mWorkout.duration * 1000 / mWorkout.length)));
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onInVisible() {
        EventBus.getDefault().unregister(this);
    }

}
