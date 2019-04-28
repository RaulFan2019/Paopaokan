package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupPartyMemberListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 *
 */
public class GroupPartyManagerAdapter extends RecyclerView.Adapter<GroupPartyManagerAdapter.ChangeOwnerHolder> {
    private List<GetGroupPartyMemberListResult> mData;
    private BitmapUtils bitmapUtils;

    private OnItemClickListener mItemListener;

    public GroupPartyManagerAdapter(Context context, List<GetGroupPartyMemberListResult> mData, OnItemClickListener mItemListener){
        this.mData = mData;
        bitmapUtils = new BitmapUtils(context);
        this.mItemListener = mItemListener;
    }

    @Override
    public ChangeOwnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_manager_list,parent,false);
        ChangeOwnerHolder holder = new ChangeOwnerHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChangeOwnerHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ChangeOwnerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CircularImage mMemberPhotoCi;
        TextView mMemberNickNameTv;
        ImageView mMemberGenderIv;
        ImageView mMemberOwnerIv;

        public ChangeOwnerHolder(View itemView) {
            super(itemView);
            mMemberPhotoCi = (CircularImage) itemView.findViewById(R.id.ci_member_photo);
            mMemberNickNameTv = (TextView) itemView.findViewById(R.id.tv_member_nickname);
            mMemberGenderIv = (ImageView) itemView.findViewById(R.id.iv_menmber_gender);
            mMemberOwnerIv = (ImageView) itemView.findViewById(R.id.iv_is_owner);

            itemView.setOnClickListener(this);
        }

        public void bindItem(int postion){
            GetGroupPartyMemberListResult member = mData.get(postion);
            //头像
            ImageUtils.loadUserImage(member.getAvatar(), mMemberPhotoCi);
            mMemberNickNameTv.setText(member.getNickname());
            //性别
            if (member.getGender() == AppEnum.UserGander.MAN) {
                mMemberGenderIv.setBackgroundResource(R.drawable.icon_man);
            } else {
                mMemberGenderIv.setBackgroundResource(R.drawable.icon_women);
            }
            if(member.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER){
                mMemberOwnerIv.setVisibility(View.VISIBLE);
            }else{
                mMemberOwnerIv.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            GetGroupPartyMemberListResult member = mData.get(getAdapterPosition());
            if(mItemListener != null){
                mItemListener.onItemClick(getAdapterPosition());
            }
            if(member.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER){
                member.setPersonstatus(AppEnum.groupPartyPersonStatus.LOGIN);
            }else{
                member.setPersonstatus(AppEnum.groupPartyPersonStatus.PARTY_LEADER);
            }

            notifyItemChanged(getAdapterPosition());
        }
    }

    public interface OnItemClickListener{
         void onItemClick(int position);
    }
}
