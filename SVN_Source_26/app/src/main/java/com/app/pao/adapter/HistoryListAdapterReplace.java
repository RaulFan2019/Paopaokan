package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.entity.network.GetWorkoutListResult;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;
import com.rey.material.widget.TextView;

import java.util.Date;
import java.util.List;

import static com.app.pao.R.id.iv_has_video;

/**
 * Created by LY on 2016/3/27.
 * Changed by LY on 2016/3/31
 */
public class HistoryListAdapterReplace extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GetWorkoutListResult.WorkoutEntity> datas;
    private Context mContext;
    private OnItemClickListener listener;
    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;

    public interface OnItemClickListener {
        void setOnItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HistoryListAdapterReplace(Context mContext, List<GetWorkoutListResult.WorkoutEntity> datas) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_history_list, parent, false);
        ItemHolder holder = new ItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).bindItem(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    /**
     * viewHolder的设置
     */
    public class ItemHolder extends RecyclerView.ViewHolder {
        TextView mLengthTv;
        TextView mPaceTv;
        TextView mDurationTv;
        TextView mTimeTv;
        View mHasVideoIv;
        ImageView mLoadingView;

        public ItemHolder(View itemView) {
            super(itemView);
            mLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            mPaceTv = (TextView) itemView.findViewById(R.id.tv_pace);
            mDurationTv = (TextView) itemView.findViewById(R.id.tv_duration);
            mTimeTv = (TextView) itemView.findViewById(R.id.tv_time);
            mHasVideoIv = itemView.findViewById(R.id.iv_has_video);
            mLoadingView = (ImageView) itemView.findViewById(R.id.iv_uploading);
        }

        public void bindItem(final int position) {
            final GetWorkoutListResult.WorkoutEntity entity = datas.get(position);
            Date curDate = new Date();
            String startTime = null;
            if (entity.getStarttime() != null) {
                startTime = entity.getStarttime();
            } else {
                startTime = entity.getName();
            }
            long splitTime = curDate.getTime() - TimeUtils.partyStringToDate(startTime).getTime() - entity.getDuration();
            if (splitTime < (2 * ONE_HOUR)) {
                mTimeTv.setText("刚刚");
//                mTimeTv.setTextColor(Color.parseColor("#f2763b"));
            } else {
                startTime = startTime.substring(0,startTime.lastIndexOf(":"));
                String oldYear = startTime.substring(0,5);
                String curYear = TimeUtils.NowTime().substring(0,5);
                if(oldYear.equals(curYear)){
                    startTime = startTime.substring(5,startTime.length());
                }
                mTimeTv.setText(startTime);
//                mTimeTv.setTextColor(Color.parseColor("#888888"));
            }
            mLengthTv.setText(NumUtils.retainTheDecimal(entity.getLength()) + "");
            mDurationTv.setText(TimeUtils.formatDurationStr(entity.getDuration()));
            mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime(
                    (long) (entity.getDuration() * 1000 / entity.getLength()), "'", "''"));
            //视频是否准备完毕
            if (entity.getVideoready() == 0) {
                mHasVideoIv.setVisibility(View.INVISIBLE);
            } else {
                mHasVideoIv.setVisibility(View.VISIBLE);
            }
            //用于判断内容是否是未上传的部分，该部分UI还未设计，停留待用
            if (entity.getType() == 0) {
                RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                        0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setInterpolator(new LinearInterpolator());
                animation.setRepeatCount(100);//重复次数
                animation.setDuration(1000);
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadingView.setAnimation(animation);
                mLoadingView.startAnimation(animation);
                animation.start();
            } else {
                mLoadingView.clearAnimation();
                mLoadingView.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.setOnItemClickListener(position);
                    }
                }
            });
        }
    }
}
