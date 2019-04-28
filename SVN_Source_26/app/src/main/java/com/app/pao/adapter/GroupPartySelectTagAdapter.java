package com.app.pao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.network.GetTagManagerListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public class GroupPartySelectTagAdapter  extends RecyclerView.Adapter<GroupPartySelectTagAdapter.PartyTagHolder>{

    private List<GetTagManagerListResult> mDataList;
    private List<GetTagManagerListResult> mCheckedList;

    public GroupPartySelectTagAdapter(List<GetTagManagerListResult> mDataList) {
        this.mDataList = mDataList;
        mCheckedList = new ArrayList<>();
    }

    @Override
    public PartyTagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_tag_list,parent,false);
        PartyTagHolder holder = new PartyTagHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PartyTagHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<GetTagManagerListResult> getmCheckedList() {
        return mCheckedList;
    }

    public void setmCheckedList(List<GetTagManagerListResult> mCheckedList) {
        this.mCheckedList = mCheckedList;
    }

    public class PartyTagHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mTagNameTv;
        ImageView mTagCheckIv;

        public PartyTagHolder(View itemView) {
            super(itemView);

            mTagNameTv = (TextView) itemView.findViewById(R.id.tv_tag_name);
            mTagCheckIv = (ImageView) itemView.findViewById(R.id.iv_tag_check);
            itemView.setOnClickListener(this);
        }

        public void bindItem(int position){
            GetTagManagerListResult tag = mDataList.get(position);
            mTagNameTv.setText(tag.getName());

            if(tag.getIsChecked() == 1){
                mCheckedList.add(tag);
                mTagCheckIv.setVisibility(View.VISIBLE);
            }else{
                mCheckedList.remove(tag);
                mTagCheckIv.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            GetTagManagerListResult tag = mDataList.get(getAdapterPosition());
            if(tag.getIsChecked() == 1){
                mCheckedList.remove(tag);
                mTagCheckIv.setVisibility(View.GONE);
            }else {
                mCheckedList.add(tag);
                mTagCheckIv.setVisibility(View.VISIBLE);
            }
        }
    }
}
