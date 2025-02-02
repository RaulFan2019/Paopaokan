package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupApplyMemberListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/12/8.
 * 跑团申请列表适配器
 */
public class GroupApplyMemberListAdapter extends SimpleBaseAdapter<GetGroupApplyMemberListResult.ApplyEntity> {

    private BitmapUtils mBitmapU;

    public GroupApplyMemberListAdapter(Context context, List<GetGroupApplyMemberListResult.ApplyEntity> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemberViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_group_apply_member_list, null);
            mHolder = new MemberViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (MemberViewHolder) convertView.getTag();
        }
        //用户信息对象
        GetGroupApplyMemberListResult.ApplyEntity entity = datas.get(position);

        //用户性别
        if (entity.getGender() == AppEnum.UserGander.MAN) {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //用户来源
        mHolder.mFriendNameTv.setText(entity.nickname + "");

        //头像
        ImageUtils.loadUserImage(entity.avatar, mHolder.mFriendPhotoCiv);

        mHolder.mLengthTv.setText(NumUtils.retainTheDecimal(entity.getTotallength()) + "公里");
        //申请入团
        if (entity.getType() == AppEnum.groupApplyMemberType.APPLY) {
            if (entity.getStatus() == AppEnum.groupApplyMemberStatus.AGREE) {
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setText("已加入");
            } else if (entity.getStatus() == AppEnum.groupApplyMemberStatus.REFUSE) {
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setText("已拒绝");
            } else if (entity.getStatus() == AppEnum.groupApplyMemberStatus.WAIT) {
                mHolder.mAgreeBtn.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setVisibility(View.GONE);
            }else{
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.INVISIBLE);
            }
            //邀请入团
        } else {
            if (entity.getStatus() == AppEnum.groupApplyMemberStatus.AGREE) {
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setText("已加入");
            } else if (entity.getStatus() == AppEnum.groupApplyMemberStatus.REFUSE) {
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setText("已拒绝");
            } else if (entity.getStatus() == AppEnum.groupApplyMemberStatus.WAIT) {
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setText("已邀请");
            }else{
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.INVISIBLE);
            }
        }
        final int pos = position;
        mHolder.mAgreeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onItemBtnClick(pos);
            }
        });

        final GetGroupApplyMemberListResult.ApplyEntity Ientity = entity;
        mHolder.mitemll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Ientity.getUserid() == LocalApplication.getInstance().getLoginUser(context).getUserId()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("userid", Ientity.getUserid());
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    /** **/
    class MemberViewHolder {
        TextView mFriendNameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像
        Button mAgreeBtn;
        TextView mStatusTv;
        ImageView mGanderIv;//性别图标
        TextView mLengthTv;
        LinearLayout mitemll;

        public MemberViewHolder(View v) {
            mAgreeBtn = (Button) v.findViewById(R.id.btn_agree);
            mFriendNameTv = (TextView) v.findViewById(R.id.tv_friends_name);
            mFriendPhotoCiv = (CircularImage) v.findViewById(R.id.iv_search_friends_photo);
            mStatusTv = (TextView) v.findViewById(R.id.tv_friend_status);
            mGanderIv = (ImageView) v.findViewById(R.id.iv_gander);
            mitemll = (LinearLayout) v.findViewById(R.id.ll_item);
            mLengthTv = (TextView) v.findViewById(R.id.tv_length);
        }
    }
}
