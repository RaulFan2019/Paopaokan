package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.MessageData;
import com.app.pao.entity.db.DBEntityMessage;
import com.app.pao.entity.network.GetGroupListResult;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/22.
 * <p/>
 * 跑团列表adapter Replace
 */
public class GroupListAdapterReplace extends RecyclerView.Adapter<GroupListAdapterReplace.GroupListHolder> {

    private List<GetGroupListResult> mDataList;
    private BitmapUtils bitmapUtils;
    List<Integer> mHasApplyGroupIds;
    private Context context;

    public GroupListAdapterReplace(Context context, List<GetGroupListResult> mDataList, List<Integer> HasApplyGroupIds) {
        this.mDataList = mDataList;
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
        this.mHasApplyGroupIds = HasApplyGroupIds;
    }


    @Override
    public GroupListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list_replace, parent, false);
        GroupListHolder holder = new GroupListHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(GroupListHolder holder, int position) {
        holder.bindItem(mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    public class GroupListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mGroupAvatarIv;
        TextView mGroupNameTv;
        TextView mLocationTv;
        TextView mMemberNumTv;
        TextView mGroupLengthTv;
        TextView mGroupStateTv;
        TextView mGroupHaspushTv;
        TextView mIdentityTv;

        public GroupListHolder(View itemView) {
            super(itemView);
            mGroupAvatarIv = (ImageView) itemView.findViewById(R.id.iv_group_avatar);
            mGroupNameTv = (TextView) itemView.findViewById(R.id.tv_group_name);
            mLocationTv = (TextView) itemView.findViewById(R.id.tv_location);
            mMemberNumTv = (TextView) itemView.findViewById(R.id.tv_member_count);
            mGroupLengthTv = (TextView) itemView.findViewById(R.id.tv_group_length);
            mGroupStateTv = (TextView) itemView.findViewById(R.id.tv_status);
            mGroupHaspushTv = (TextView) itemView.findViewById(R.id.tv_haspush);
            mIdentityTv = (TextView) itemView.findViewById(R.id.tv_identity);
            itemView.setOnClickListener(this);
        }

        public void bindItem(final GetGroupListResult group) {
            //头像
            ImageUtils.loadGroupImage(group.avatar, mGroupAvatarIv);
            //名称
            mGroupNameTv.setText(group.name);
            //位置
            mLocationTv.setText(group.locationprovince + " " + group.locationcity);
            //人数
            mMemberNumTv.setText(group.membercount + "人");
            //平均周跑量
            mGroupLengthTv.setText(NumUtils.retainTheDecimal(group.avgweeklength) + "公里/人周");

            //   团长身份,状态
            mGroupHaspushTv.setVisibility(View.GONE);
            if (group.role == AppEnum.groupRole.OWNER) {
                mIdentityTv.setVisibility(View.VISIBLE);
                mIdentityTv.setText("团长");
                if (mHasApplyGroupIds.contains(group.id)) {
                    mGroupHaspushTv.setVisibility(View.VISIBLE);
                    mGroupHaspushTv.setText("有入团审核");
                    mGroupStateTv.setVisibility(View.GONE);
                }

                if (group.status == AppEnum.groupStatus.WAITING) {
                    mGroupStateTv.setVisibility(View.VISIBLE);
                    mGroupStateTv.setText("审核中");
                } else if (group.status == AppEnum.groupStatus.DISMISS) {
                    mGroupStateTv.setVisibility(View.VISIBLE);
                    mGroupStateTv.setText("解散审核中");
                } else {
                    mGroupStateTv.setVisibility(View.INVISIBLE);
                }
                //管理者
            } else if (group.role == AppEnum.groupRole.MANAGER) {
                //   mHolder.badgeView.hide();
                mIdentityTv.setVisibility(View.VISIBLE);
                mIdentityTv.setText("管理员");
                mGroupStateTv.setVisibility(View.INVISIBLE);
                //其他情况
            } else {
                //    mHolder.badgeView.hide();
                mIdentityTv.setVisibility(View.INVISIBLE);
                //若是成员
                if (group.role == AppEnum.groupRole.MEMBER) {
                    mGroupStateTv.setVisibility(View.INVISIBLE);
                } else {
                    mGroupStateTv.setVisibility(View.VISIBLE);
                    if (group.status == AppEnum.groupStatus.WAITING) {
                        mGroupStateTv.setText("申请中");
                    } else if (group.getRole() == AppEnum.groupRole.HAS_APPRO) {
                        mGroupStateTv.setText("被邀请");
                    } else {
                        mGroupStateTv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

        @Override
        public void onClick(View v) {
            GetGroupListResult entity = mDataList.get(getAdapterPosition());
            if (entity.getStatus() == AppEnum.groupStatus.DISMISS) {
                T.showShort(context, "该跑团已申请解散..");
            } else if (entity.getStatus() == AppEnum.groupStatus.WAITING && entity.getRole() == AppEnum.groupRole.OWNER) {
                T.showShort(context, "该跑团正在审核中..");
            } else {
                Intent i = new Intent(context, GroupInfoActivity.class);
                i.putExtra("groupid", entity.getId());
                context.startActivity(i);
            }
        }
    }
}
