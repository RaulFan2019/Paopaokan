package com.app.pao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetLabelMemberListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */
public class GroupSingleTagAdapter extends RecyclerView.Adapter<GroupSingleTagAdapter.SingleTagHolder> {
    private List<GetLabelMemberListResult> mDatas;//成员列表
    private BitmapUtils mBitmapU;

    public GroupSingleTagAdapter(List<GetLabelMemberListResult> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public SingleTagHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_single_tag_member, parent,false);
        mBitmapU = new BitmapUtils(parent.getContext());
        SingleTagHolder singleTagHolder = new SingleTagHolder(v);
        return singleTagHolder;
    }

    @Override
    public void onBindViewHolder(SingleTagHolder holder, final int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public List<GetLabelMemberListResult> getDatas() {
        return mDatas;
    }

    public class SingleTagHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircularImage mMemberPhotoIv;
        TextView mNickNameTv;
        ImageView mGenderIv;
        ImageView mCheckIv;


        public SingleTagHolder(View itemView) {
            super(itemView);
            mMemberPhotoIv = (CircularImage) itemView.findViewById(R.id.ci_member_photo);
            mNickNameTv = (TextView) itemView.findViewById(R.id.tv_member_nickname);
            mGenderIv = (ImageView) itemView.findViewById(R.id.iv_member_gander);
            mCheckIv = (ImageView) itemView.findViewById(R.id.iv_tag_check);
            itemView.setOnClickListener(this);
        }

        public void bindItem(int position) {
            final GetLabelMemberListResult mMember = mDatas.get(position);
            mNickNameTv.setText(mMember.getNickname() + "");
            ImageUtils.loadUserImage(mMember.getAvatar(), mMemberPhotoIv);
            //性别
            if (mMember.getGender() == AppEnum.UserGander.MAN) {
                mGenderIv.setBackgroundResource(R.drawable.icon_man);
            } else {
                mGenderIv.setBackgroundResource(R.drawable.icon_women);
            }

            //角色
            if (mMember.getHasLabel() == 1) {
                mCheckIv.setBackgroundResource(R.drawable.icon_manager_y);
            } else {
                mCheckIv.setBackgroundResource(R.drawable.icon_manager_n);
            }
        }

        @Override
        public void onClick(View v) {
            final GetLabelMemberListResult mMember = mDatas.get(getAdapterPosition());

            if (mMember.getHasLabel() == 1) {
                mMember.setHasLabel(0);
            } else {
                mMember.setHasLabel(1);
            }
            mDatas.set(getAdapterPosition(), mMember);
            notifyItemChanged(getAdapterPosition());
        }
    }
}
