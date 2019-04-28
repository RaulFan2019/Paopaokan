package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.group.GroupApplyMemberListActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.activity.party.PartyInfoActivity;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.db.DBEntityMessage;
import com.app.pao.utils.TimeUtils;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 消息列表适配器
 */
public class MessageRvAdapter extends RecyclerView.Adapter<MessageRvAdapter.FriendViewHolder> {

    private List<DBEntityMessage> mDatas;//数据
    private Context mContext;

    SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public MessageRvAdapter(Context context, List<DBEntityMessage> data) {
        this.mDatas = data;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
        FriendViewHolder vh = new FriendViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        //用户信息对象
        DBEntityMessage entity = mDatas.get(position);
        holder.mInfoTv.setText(entity.getMessage());
        if (entity.getStatus() == AppEnum.messageRead.NEW) {
            holder.mIsReadIv.setVisibility(View.VISIBLE);
        } else {
            holder.mIsReadIv.setVisibility(View.INVISIBLE);
        }
        try {
            Date date = myFormatter.parse(entity.getSendtime());
            holder.mTimeTv.setText(TimeUtils.getTimestampString(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
//
//    @Override
//    public boolean onItemMove(int fromPosition, int toPosition) {
//        return false;
//    }
//
//    @Override
//    public void onItemDismiss(int position) {
//        mDatas.remove(position);
//        notifyItemRemoved(position);
//    }


    /* ViewHolder*/
    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIsReadIv;
        TextView mInfoTv;
        TextView mTimeTv;

        public FriendViewHolder(View rootView) {
            super(rootView);
            mIsReadIv = (ImageView) rootView.findViewById(R.id.iv_is_read);
            mInfoTv = (TextView) rootView.findViewById(R.id.tv_info);
            mTimeTv = (TextView) rootView.findViewById(R.id.tv_time);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            DBEntityMessage entityMessage = mDatas.get(getAdapterPosition());
            entityMessage.setStatus(AppEnum.messageRead.OLD);
            MessageData.updateMessage(mContext,entityMessage);
            Bundle bundle = new Bundle();
            Intent intent = new Intent();
            if (entityMessage.getType() == AppEnum.messageType.APPLY_ADD_FRIEND || entityMessage.getType() == AppEnum
                    .messageType.APPROVE_ADD_FRIEND) {
                bundle.putInt("userid", entityMessage.getFromuserid());
                intent.setClass(mContext, UserInfoActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            } else if (entityMessage.getType() == AppEnum.messageType.APPROVE_JOIN_RUN_GROUP
                    || entityMessage.getType() == AppEnum.messageType.AGREE_JOIN_GROUP
                    || entityMessage.getType() == AppEnum.messageType.GROUP_RECOMMEND
                    || entityMessage.getType() == AppEnum.messageType.INVITE_JOIN_RUN_GROUP) {
                bundle.putInt("groupid", Integer.parseInt(entityMessage.getExtra()));
                intent.setClass(mContext, GroupInfoActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            } else if (entityMessage.getType() == AppEnum.messageType.APPLY_JOIN_RUNGROUP) {
                bundle.putInt("groupid", Integer.parseInt(entityMessage.getExtra()));
                intent.setClass(mContext, GroupApplyMemberListActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                //邀请活动
            } else if(entityMessage.getType() == AppEnum.messageType.APPLY_JOIN_PARTY){
                bundle.putInt("partyid",Integer.parseInt(entityMessage.getExtra()));
                intent.setClass(mContext, PartyInfoActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            } else if(entityMessage.getType() == AppEnum.messageType.ADD_KUDOS ||
                        entityMessage.getType() == AppEnum.messageType.ADD_COMMENT){
                bundle.putInt("workoutid",entityMessage.getWorkoutId());
                bundle.putString("workoutname",entityMessage.getWorkoutStarttime());
                intent.setClass(mContext, HistoryInfoActivityV2.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        }
    }
}
