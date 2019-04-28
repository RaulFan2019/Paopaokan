package com.app.pao.fragment.run;

import android.graphics.Typeface;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.data.db.WorkoutData;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.event.EventRuningHeartrate;
import com.app.pao.entity.event.EventRuningLocation;
import com.app.pao.entity.event.EventRuningTime;
import com.app.pao.fragment.BaseFragmentV2;
import com.app.pao.utils.LengthUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import cn.hwh.sdk.ble.BleManagerService;

/**
 * Created by Raul.Fan on 2016/9/12.
 */
public class RunRunningFragment extends BaseFragmentV2 {


    private static final String TAG = "RunRunningFragment";


    @BindView(R.id.tv_run_running_time)
    TextView mTimeTv;
    @BindView(R.id.tv_running_length)
    TextView mLengthTv;
    @BindView(R.id.tv_running_heartrate)
    TextView mHrTv;
    @BindView(R.id.tv_run_running_pace)
    TextView mPaceTv;


    /* 构造函数 */
    public static RunRunningFragment newInstance() {
        RunRunningFragment fragment = new RunRunningFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_run_running;
    }

    /**
     * 接收到时间变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRuningTime event) {
        long time = event.getTime();
        if (time > 0) {
            mTimeTv.setText(TimeUtils.formatDurationStr(time));
        }
    }

    /**
     * 收到位置变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventRuningLocation event) {
        mLengthTv.setText(LengthUtils.formatLength(event.length));
        int speed = event.speed;
        if (speed != 0) {
            mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime(speed));
        } else {
            mPaceTv.setText("- -");
        }
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
            mHrTv.setText(event.heartrate + "");
        } else {
            mHrTv.setText("- -");
        }
    }


    @Override
    protected void initParams() {
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        mTimeTv.setTypeface(typeFace);
        mPaceTv.setTypeface(typeFace);
        mLengthTv.setTypeface(typeFace);
        mHrTv.setTypeface(typeFace);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void causeGC() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onVisible() {

        DBEntityWorkout workout = WorkoutData.getUnFinishWorkout(getActivity(), LocalApplication.getInstance().getLoginUser(getActivity()).userId);
        if (workout != null) {
            mTimeTv.setText(TimeUtils.formatDurationStr(workout.duration));
            mLengthTv.setText(LengthUtils.formatLength(workout.length));
        }
    }

    @Override
    protected void onInVisible() {

    }

}
