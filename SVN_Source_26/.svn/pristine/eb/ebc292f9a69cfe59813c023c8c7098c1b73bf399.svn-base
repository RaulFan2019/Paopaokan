package com.app.pao.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.model.HistorySpliteEntity;
import com.app.pao.utils.TimeUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分段列表适配器
 *
 * @author Raul
 */
public class FriendHistorySpliteListAdapter extends BaseAdapter {

    private static final String TAG = "HistoryInfoLapListAdapter";

    private LayoutInflater layoutInflater;
    private Activity activity;
    private List<HistorySpliteEntity> splits;
    private int maxTimeSpeed;// 最大配速
    private boolean hasHeartrate;// 是否有心率
    private Typeface typeFace;//字体
    private float mTotalLength;
    private long mTotalTime;

    public FriendHistorySpliteListAdapter(Activity activity, List<HistorySpliteEntity> splits, int maxTimeSpeed,
                                          boolean hasHeartrate, float TotalLength, long TotalTime) {
        this.activity = activity;
        this.layoutInflater = LayoutInflater.from(activity);
        this.splits = splits;
        this.maxTimeSpeed = maxTimeSpeed;
        this.hasHeartrate = hasHeartrate;
        typeFace = Typeface.createFromAsset(activity.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        this.mTotalLength = TotalLength;
        this.mTotalTime = TotalTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler mHodler;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_splite_list, null);
            mHodler = new ViewHodler(convertView);
            convertView.setTag(mHodler);
        } else {
            mHodler = (ViewHodler) convertView.getTag();
        }

        mHodler.mLengthNumTv.setText((position + 1) + "");
        if (hasHeartrate) {
            mHodler.mHeartbeatTv.setText(splits.get(position).getAvgheartrate() + "");
        } else {
            mHodler.mHeartbeatTv.setVisibility(View.GONE);
        }

        int timeSpeed = 0;
        if (splits.get(position).getLength() > 0) {
            timeSpeed = (int) (splits.get(position).getDuration() * 1000 / splits.get(position).getLength());
        }
        // 进度条
        if (maxTimeSpeed == 0) {
            mHodler.mProgress.setProgress(100);
        } else {
            mHodler.mProgress.setProgress(timeSpeed * 100 / maxTimeSpeed);
        }
        mHodler.mTimeSpeedTv.setText(TimeUtils.formatSecondsToSpeedTime(timeSpeed));

        //每5条数据显示 或 最后一条数据显示总公里数据
        if (position == (splits.size() - 1)) {
//            int TotalTime = 0;
//            long TotalLength = 0;
//            for (int i = 0; i < (position + 1); i++) {
//                TotalTime += splits.get(i).getDuration();
//                TotalLength += splits.get(i).getLength();
//            }
            BigDecimal lengthb = new BigDecimal(mTotalLength / 1000f);

            mHodler.mButtomLl.setVisibility(View.VISIBLE);
            String str = lengthb.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()
                    + "公里  总用时" + TimeUtils.formatSecondsToLongHourTime(mTotalTime);
            mHodler.mButtomTv.setText(str);
        } else {
            mHodler.mButtomLl.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return splits.size();
    }

    @Override
    public Object getItem(int position) {
        return splits.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHodler {
        TextView mLengthNumTv;//公里数文本
        TextView mTimeSpeedTv;//速度文本
        TextView mHeartbeatTv;//心率文本
        LinearLayout mButtomLl;//统计布局
        TextView mButtomTv;//统计布局左边文本
        ProgressBar mProgress;//进度条

        public ViewHodler(View v) {
            mLengthNumTv = (TextView) v.findViewById(R.id.tv_num);
            mTimeSpeedTv = (TextView) v.findViewById(R.id.tv_time_speed);
            mHeartbeatTv = (TextView) v.findViewById(R.id.tv_heartbeat);
            mButtomLl = (LinearLayout) v.findViewById(R.id.ll_buttom);
            mButtomTv = (TextView) v.findViewById(R.id.tv_buttom);
            mProgress = (ProgressBar) v.findViewById(R.id.progress);

            mLengthNumTv.setTypeface(typeFace);
            mTimeSpeedTv.setTypeface(typeFace);
            mHeartbeatTv.setTypeface(typeFace);
            mButtomTv.setTypeface(typeFace);
        }
    }
}
