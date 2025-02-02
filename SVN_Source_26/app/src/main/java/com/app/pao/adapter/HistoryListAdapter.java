package com.app.pao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.entity.network.GetWorkoutListResult;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 历史列表适配器
 */
public class HistoryListAdapter extends SimpleBaseAdapter<GetWorkoutListResult.WorkoutEntity> {


    private BitmapUtils mBitmapU;
    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;

    public HistoryListAdapter(Context context, List<GetWorkoutListResult.WorkoutEntity> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_history_list, null);
            mHolder = new FriendViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (FriendViewHolder) convertView.getTag();
        }
        //用户信息对象
        GetWorkoutListResult.WorkoutEntity entity = datas.get(position);

        Date curDate = new Date();
        long splitTime = curDate.getTime() - TimeUtils.partyStringToDate(entity.getStarttime()).getTime() - entity.getDuration();
        if (splitTime < (2 * ONE_HOUR)) {
            mHolder.mTimeTv.setText("刚刚");
            mHolder.mTimeTv.setTextColor(Color.parseColor("#f2763b"));
        } else {
            mHolder.mTimeTv.setText(entity.getName());
            mHolder.mTimeTv.setTextColor(Color.parseColor("#888888"));
        }
        mHolder.mLengthTv.setText(NumUtils.retainTheDecimal(entity.getLength()) + "");
        mHolder.mDurationTv.setText(TimeUtils.formatDurationStr(entity.getDuration()));
        mHolder.mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime(
                (long) (entity.getDuration() * 1000 / entity.getLength()), "'", "'''"));
        //视频是否准备完毕
        if (entity.getVideoready() == 0) {
            mHolder.mHasVideoIv.setVisibility(View.INVISIBLE);
        } else {
            mHolder.mHasVideoIv.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    /*好友适配界面**/
    class FriendViewHolder {
        TextView mTimeTv;
        TextView mLengthTv;
        TextView mDurationTv;
        TextView mPaceTv;
        View mHasVideoIv;//有视频标志

        public FriendViewHolder(View v) {
            mTimeTv = (TextView) v.findViewById(R.id.tv_time);
            mLengthTv = (TextView) v.findViewById(R.id.tv_length);
            mDurationTv = (TextView) v.findViewById(R.id.tv_duration);
            mPaceTv = (TextView) v.findViewById(R.id.tv_pace);
            mHasVideoIv = v.findViewById(R.id.iv_has_video);
        }

    }

}
