package com.app.pao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.entity.adapter.HistoryListWithTitleItem;
import com.app.pao.entity.network.GetWorkoutListResult;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by LY on 2016/5/10.
 */
public class HistoryListAdapterV2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<HistoryListWithTitleItem> datas;
    private Context mContext;
    private OnItemClickListener listener;
    private BitmapUtils mBitmapUtils;
    private Typeface typeface;//用于设置字体类型
    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;

    public interface OnItemClickListener {
        void setOnItemClickListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public HistoryListAdapterV2(Context mContext, List<HistoryListWithTitleItem> datas) {
        this.datas = datas;
        this.mContext = mContext;
        mBitmapUtils = new BitmapUtils(mContext);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        ItemHolder holder;
        if(viewType == HistoryListWithTitleItem.ITEM_HISTORY) {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_history_list_v2, parent, false);
            holder = new ItemHolder(v,HistoryListWithTitleItem.ITEM_HISTORY);
        }else{
            v = LayoutInflater.from(mContext).inflate(R.layout.item_history_title, parent, false);
            holder = new ItemHolder(v,HistoryListWithTitleItem.ITEM_TITEL);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(datas.get(position).type == HistoryListWithTitleItem.ITEM_HISTORY) {
            ((ItemHolder) holder).bindItem(position);
        }else{
            ((ItemHolder) holder).bindTitleItem(position);
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).type;
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
        ImageView mHistoryAvatar;
        TextView mGroupNameTv;
        View mHasSocialIv;
        LinearLayout mHeartRaceLl;
        TextView mHeartRaceTv;
        TextView mTitleTv;


        public ItemHolder(View itemView,int type) {
            super(itemView);
            if(type == HistoryListWithTitleItem.ITEM_TITEL){
                mTitleTv = (TextView) itemView.findViewById(R.id.tv_first_pinyin);
            }else {
                mLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
                mPaceTv = (TextView) itemView.findViewById(R.id.tv_pace);
                mDurationTv = (TextView) itemView.findViewById(R.id.tv_duration);
                mTimeTv = (TextView) itemView.findViewById(R.id.tv_time);
                mHasVideoIv = itemView.findViewById(R.id.iv_has_video);
                mLoadingView = (ImageView) itemView.findViewById(R.id.iv_uploading);
                mHistoryAvatar = (ImageView) itemView.findViewById(R.id.iv_history_avatar);
                mGroupNameTv = (TextView) itemView.findViewById(R.id.tv_group_name);
                mHasSocialIv = itemView.findViewById(R.id.iv_has_social_count);
                mHeartRaceLl = (LinearLayout) itemView.findViewById(R.id.ll_heart_race);
                mHeartRaceTv = (TextView) itemView.findViewById(R.id.tv_heart_race);
            }
        }

        public void bindTitleItem(final int position){
            final String title = datas.get(position).title;
            mTitleTv.setTextColor(Color.parseColor("#888888"));
            mTitleTv.setText(title.substring(0,4)+"年"+title.substring(5,7) + "月");
        }

        public void bindItem(final int position) {
            final GetWorkoutListResult.WorkoutEntity entity = datas.get(position).entity;
            String startTime = null;
            ImageUtils.loadWorkoutBgImage(entity.getThumbnail(), mHistoryAvatar);
            if(entity.getSocialcount() > 0){
                mHasSocialIv.setVisibility(View.VISIBLE);
            }else{
                mHasSocialIv.setVisibility(View.GONE);
            }
            if(entity.getAvgheartrate() == 0){
                mHeartRaceLl.setVisibility(View.INVISIBLE);
            }else{
                mHeartRaceLl.setVisibility(View.VISIBLE);
                mHeartRaceTv.setTypeface(typeface);
                mHeartRaceTv.setText(entity.getAvgheartrate()+"");
            }
            if(entity.getRacename() == ""){
                mGroupNameTv.setVisibility(View.INVISIBLE);
            }else{
                mGroupNameTv.setVisibility(View.VISIBLE);
                mGroupNameTv.setText(entity.getRacename());
            }
            if (entity.getStarttime() != null) {
                startTime = entity.getStarttime();
            } else {
                startTime = entity.getName();
            }
            String friendlyTime = TimeUtils.getFriendlyTimeV2(startTime,entity.getDuration());
            mTimeTv.setText(friendlyTime);
//            long splitTime = curDate.getTime() - TimeUtils.partyStringToDate(startTime).getTime() - entity.getDuration();
//            if (splitTime < (2 * ONE_HOUR)) {
//                mTimeTv.setText("刚刚");
//            } else {
//                startTime = startTime.substring(0,startTime.lastIndexOf(":"));
//                String oldYear = startTime.substring(0,5);
//                String curYear = TimeUtils.NowTime().substring(0,5);
//                if(oldYear.equals(curYear)){
//                    startTime = startTime.substring(5,startTime.length());
//                }
//                mTimeTv.setText(startTime);
//            }
            mLengthTv.setTypeface(typeface);
            mLengthTv.setText(NumUtils.retainTheDecimal(entity.getLength()) + "");
            mDurationTv.setTypeface(typeface);
            mDurationTv.setText(TimeUtils.formatDurationStr(entity.getDuration()));
            mPaceTv.setTypeface(typeface);
            mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime(
                    (long) (entity.getDuration() * 1000 / entity.getLength()), "'", "''"));
            //视频是否准备完毕
            if (entity.getVideoready() == 0) {
                mHasVideoIv.setVisibility(View.GONE);
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
