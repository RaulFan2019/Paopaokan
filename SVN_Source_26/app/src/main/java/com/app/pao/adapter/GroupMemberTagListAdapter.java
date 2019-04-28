package com.app.pao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.network.GetMamberTagListResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/10.
 *
 *设置团成员标签adapter
 */
public class GroupMemberTagListAdapter extends RecyclerView.Adapter<GroupMemberTagListAdapter.TagListHolder> {

    private List<GetMamberTagListResult> mDataList;

    public GroupMemberTagListAdapter(List<GetMamberTagListResult> mDataList){
        this.mDataList = mDataList;
    }

    @Override
    public TagListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_tag_list,parent,false);
        TagListHolder holder = new TagListHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TagListHolder holder, final int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public List<GetMamberTagListResult> getmDataList() {
        return mDataList;
    }

    public void setmDataList(List<GetMamberTagListResult> mDataList) {
        this.mDataList = mDataList;
    }



    public class TagListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mTagNameTv;
        ImageView mTagCheckIv;
        public TagListHolder(View itemView) {
            super(itemView);

            mTagNameTv = (TextView) itemView.findViewById(R.id.tv_tag_name);
            mTagCheckIv = (ImageView) itemView.findViewById(R.id.iv_tag_check);

            itemView.setOnClickListener(this);
        }

        public void bindItem(int position){
            final GetMamberTagListResult personLabel = mDataList.get(position);
            mTagNameTv.setText(personLabel.getName());

            if(personLabel.getHaslabel() == 1){
                mTagCheckIv.setVisibility(View.VISIBLE);
            }else{
                mTagCheckIv.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            final GetMamberTagListResult personLabel = mDataList.get(getAdapterPosition());
            if(personLabel.getHaslabel() == 1){
                personLabel.setHaslabel(0);
            }else{
                personLabel.setHaslabel(1);
            }
            notifyItemChanged(getAdapterPosition());
        }
    }
}
