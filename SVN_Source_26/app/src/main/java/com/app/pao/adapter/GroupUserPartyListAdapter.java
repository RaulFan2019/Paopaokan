package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.activity.group.GroupPartyInfoActivity;
import com.app.pao.activity.party.PartyInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.entity.network.GetGroupPartyListResult;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/1/21.
 * <p/>
 * 团成员的活动adapter
 */
public class GroupUserPartyListAdapter extends RecyclerView.Adapter<GroupUserPartyListAdapter.UserPartyHolder> {


    private List<GetGroupPartyListResult> mDataList;
    private BitmapUtils bitmapUtils;
    private Context mContext;

    public GroupUserPartyListAdapter(Context mContext, List<GetGroupPartyListResult> mDataList) {
        this.mDataList = mDataList;
        this.mContext = mContext;
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public UserPartyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_user_party, parent,
                false);
        UserPartyHolder holder = new UserPartyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserPartyHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class UserPartyHolder extends RecyclerView.ViewHolder {
        TextView mPartyDateTv;
        TextView partyNameTv;
        TextView contractTimeTv;
        ImageView partyIv;
        TextView locationTv;
        TextView partyTimeTv;
        TextView partyLengthTv;
        TextView partyPersonCountTv;
        LinearLayout signLl;//未开始状态布局
        TextView signTv;//报名状态文本
        LinearLayout startLl;//开始状态布局
        LinearLayout endLl;//结束状态布局
        TextView mLongOnClickRl;
        TextView mPersonStatusTv;

        public UserPartyHolder(View itemView) {
            super(itemView);
            mPartyDateTv = (TextView) itemView.findViewById(R.id.tv_party_date);
            mLongOnClickRl = (TextView) itemView.findViewById(R.id.rl_history_child);
            contractTimeTv = (TextView) itemView.findViewById(R.id.tv_party_contrast_time);
            partyIv = (ImageView) itemView.findViewById(R.id.iv_party);
            locationTv = (TextView) itemView.findViewById(R.id.tv_location);
            partyNameTv = (TextView) itemView.findViewById(R.id.tv_party_name);
            partyTimeTv = (TextView) itemView.findViewById(R.id.tv_party_time);
            partyLengthTv = (TextView) itemView.findViewById(R.id.tv_party_length);
            partyPersonCountTv = (TextView) itemView.findViewById(R.id.tv_party_person_count);
            signLl = (LinearLayout) itemView.findViewById(R.id.ll_party_signup);
            startLl = (LinearLayout) itemView.findViewById(R.id.ll_party_start);
            endLl = (LinearLayout) itemView.findViewById(R.id.ll_party_end);
            signTv = (TextView) itemView.findViewById(R.id.tv_has_signup);
            mPersonStatusTv = (TextView) itemView.findViewById(R.id.tv_person_status);
        }

        public void bindItem(int position) {
            GetGroupPartyListResult entity = mDataList.get(position);
            mPartyDateTv.setText(TimeUtils.getTimestampDayString(TimeUtils.stringToDateAndMinS(entity.getStarttime())));

            //状态判断
            if (entity.getStatus() > AppEnum.groupPartyStatus.PARTY_START) {
                endLl.setVisibility(View.VISIBLE);
                signLl.setVisibility(View.GONE);
                startLl.setVisibility(View.GONE);
            } else if (entity.getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
                endLl.setVisibility(View.GONE);
                signLl.setVisibility(View.GONE);
                startLl.setVisibility(View.VISIBLE);
            } else {
                endLl.setVisibility(View.GONE);
                signLl.setVisibility(View.VISIBLE);
                startLl.setVisibility(View.GONE);
                if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST) {
                    signTv.setText("已报名");
                } else {
                    signTv.setText("未报名");
                }
            }

            if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST) {
                mPersonStatusTv.setText("已报名");
            } else {
                mPersonStatusTv.setText("已签到");
            }

            //活动名称
            partyNameTv.setText(entity.getName());
            //活动尚未结束
            if (entity.getStatus() < AppEnum.groupPartyStatus.PARTY_STOP) {
                contractTimeTv.setTextColor(Color.parseColor("#f06522"));
            } else {
                contractTimeTv.setTextColor(Color.parseColor("#888888"));
            }
            //活动未开始
            if (entity.getStatus() < AppEnum.groupPartyStatus.PARTY_START) {
                contractTimeTv.setText(
                        TimeUtils.getTimestampAfterString(TimeUtils.stringToDate(entity.getStarttime())) + " 开始");
                //活动进行中
            } else if (entity.getStatus() == AppEnum.groupPartyStatus.PARTY_START) {
                contractTimeTv.setText("活动进行中");
                //活动结束
            } else if (entity.getStatus() == AppEnum.groupPartyStatus.PARTY_STOP) {
                contractTimeTv.setText(
                        TimeUtils.getTimestampString(TimeUtils.stringToDate(entity.getEndtime())) + " " + "结束");
            } else if (entity.getStatus() == AppEnum.groupPartyStatus.PARTY_CANCEL) {
                contractTimeTv.setText("活动已取消");
            }
            //活动图片
            ImageUtils.loadPartySmallImage(entity.getPicture(), partyIv);
            //活动位置
            locationTv.setText(entity.getLocation());
            //活动时间
            partyTimeTv.setText(TimeUtils.getDynamicDayTime(entity.getStarttime()));
            //活动距离
            double workoutLength = (entity.getAvglength() / 1000f);
            final BigDecimal bg = new BigDecimal(workoutLength);
            double length = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            partyLengthTv.setText(length + "km");
            //活动人数
            partyPersonCountTv.setText(entity.getSignupcount() + "人");

            mLongOnClickRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, PartyInfoActivity.class);
                    i.putExtra("partyid", mDataList.get(getAdapterPosition()).getId());
                    mContext.startActivity(i);
                }
            });
        }
    }
}
