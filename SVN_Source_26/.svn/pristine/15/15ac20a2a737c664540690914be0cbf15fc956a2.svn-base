package com.app.pao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.network.GetWorkOutListForPartyResult;
import com.app.pao.utils.Log;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 * <p/>
 * 活动历史adapter
 */
public class GroupPartySelectWorkOutAdapter extends RecyclerView.Adapter<GroupPartySelectWorkOutAdapter.SelectWorkOutHolder> {
    private List<GetWorkOutListForPartyResult.PartyWorkOut> mDataList;


    public GroupPartySelectWorkOutAdapter(List<GetWorkOutListForPartyResult.PartyWorkOut> mDataList) {
        this.mDataList = mDataList;

    }

    @Override
    public SelectWorkOutHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_select_workout, parent,false);
        SelectWorkOutHolder holder = new SelectWorkOutHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(SelectWorkOutHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<GetWorkOutListForPartyResult.PartyWorkOut> getmDataList() {
        return mDataList;
    }

    public class SelectWorkOutHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mWorkOutTimeTv;
        TextView mWorkOutLengthTv;
        TextView mWorkOutDurationTv;
        ImageView mWorkOutCheckIv;

        public SelectWorkOutHolder(View itemView) {
            super(itemView);
            mWorkOutTimeTv = (TextView) itemView.findViewById(R.id.tv_workout_time);
            mWorkOutLengthTv = (TextView) itemView.findViewById(R.id.tv_workout_length);
            mWorkOutDurationTv = (TextView) itemView.findViewById(R.id.tv_workout_duration);
            mWorkOutCheckIv = (ImageView) itemView.findViewById(R.id.iv_workout_check);
            itemView.setOnClickListener(this);
        }

        public void bindItem(int position) {
            GetWorkOutListForPartyResult.PartyWorkOut workOut = mDataList.get(position);
            mWorkOutTimeTv.setText(workOut.getStarttime());
            mWorkOutLengthTv.setText(NumUtils.retainTheDecimal(workOut.getLength()));
            mWorkOutDurationTv.setText(TimeUtils.formatDurationStr(workOut.getDuration()));
            if(workOut.getSelected() == 1){
                mWorkOutCheckIv.setImageResource(R.drawable.icon_check);
            }else {
                mWorkOutCheckIv.setImageResource(R.drawable.icon_uncheck);
            }
        }

        @Override
        public void onClick(View v) {
            GetWorkOutListForPartyResult.PartyWorkOut workOut = mDataList.get(getAdapterPosition());
            if(workOut.getSelected() == 1){
                workOut.setSelected(0);
            }else{
                workOut.setSelected(1);
            }
            mDataList.set(getAdapterPosition(),workOut);
            notifyItemChanged(getAdapterPosition());
        }
    }


}
