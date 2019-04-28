package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.activity.group.GroupSingleTagActivity;
import com.app.pao.entity.network.GetTagManagerListResult;
import com.app.pao.ui.widget.helper.ItemTouchHelperAdapter;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 团标签适配器
 */
public class TagManageRvAdapter extends RecyclerView.Adapter<TagManageRvAdapter.FriendViewHolder> implements
        ItemTouchHelperAdapter {

    private List<GetTagManagerListResult> mDatas;//数据
    private Context mContext;


    public TagManageRvAdapter(Context mContext ,List<GetTagManagerListResult> data) {
        this.mDatas = data;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_manage, parent, false);
        FriendViewHolder holder = new FriendViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }


    /* ViewHolder*/
    public class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mNameTv;
        TextView mCountTv;

        public FriendViewHolder(View rootView) {
            super(rootView);
            mNameTv = (TextView) rootView.findViewById(R.id.tv_tag_name);
            mCountTv = (TextView) rootView.findViewById(R.id.tv_tag_count);
            rootView.setOnClickListener(this);
        }

        public void bindItem(int position) {
            GetTagManagerListResult entity = mDatas.get(position);
            mNameTv.setText(entity.getName());
            mCountTv.setText(entity.getMembercount() + "人");
        }

        @Override
        public void onClick(View v) {
            Intent  i = new Intent(mContext, GroupSingleTagActivity.class);
            i.putExtra("labelId",mDatas.get(getAdapterPosition()).getId());
            mContext.startActivity(i);
        }

    }
}
