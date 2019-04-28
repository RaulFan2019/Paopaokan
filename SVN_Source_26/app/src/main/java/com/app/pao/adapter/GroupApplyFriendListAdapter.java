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
import com.app.pao.entity.network.GetFriendListByGroup;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 跑团邀请好友状态
 */
public class GroupApplyFriendListAdapter extends SimpleBaseAdapter<GetFriendListByGroup.FriendsEntity> {


    private BitmapUtils mBitmapU;

    public GroupApplyFriendListAdapter(Context context, List<GetFriendListByGroup.FriendsEntity> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_group_apply_member_list, null);
            mHolder = new FriendViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (FriendViewHolder) convertView.getTag();
        }
        //用户信息对象
        GetFriendListByGroup.FriendsEntity entity = datas.get(position);

        mHolder.mFriendNameTv.setText(entity.nickname + "");
        ImageUtils.loadUserImage(entity.getAvatar(), mHolder.mFriendPhotoCiv);
        //性别
        if (entity.getGender() == AppEnum.UserGander.MAN) {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //总里程
        mHolder.mLengthTv.setText(NumUtils.retainTheDecimal(entity.getTotallength()) + "公里");

        //收到的请求
        //若不是团友
        if (entity.getGrouprole() == AppEnum.friendInGroupRole.NOT_MEMBER) {
            mHolder.mAgreeBtn.setVisibility(View.VISIBLE);
            mHolder.mAgreeBtn.setText("邀请");
            mHolder.mStatusTv.setVisibility(View.GONE);
        } else {
            mHolder.mAgreeBtn.setVisibility(View.GONE);
            mHolder.mStatusTv.setVisibility(View.VISIBLE);
            if (entity.getGrouprole() == AppEnum.friendInGroupRole.APPLY) {
                mHolder.mStatusTv.setText("已申请");
            } else if (entity.getGrouprole() == AppEnum.friendInGroupRole.INVITE) {
                mHolder.mStatusTv.setText("已邀请");
            } else {
                mHolder.mStatusTv.setText("团友");
            }
        }
        final int pos = position;
        mHolder.mAgreeBtn.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     mListener.onItemBtnClick(pos);
                                                 }
                                             }
        );

        final GetFriendListByGroup.FriendsEntity Ientity = entity;
        mHolder.mitemll.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (LocalApplication.getInstance().getLoginUser(context).userId != Ientity.getId()) {
                                                       Bundle bundle = new Bundle();
                                                       bundle.putInt("userid", Ientity.getId());
                                                       Intent intent = new Intent(context, UserInfoActivity.class);
                                                       intent.putExtras(bundle);
                                                       context.startActivity(intent);
                                                   }
                                               }
                                           }

        );

        return convertView;
    }

    /*好友适配界面**/
    class FriendViewHolder {
        TextView mFriendNameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像
        com.rey.material.widget.Button mAgreeBtn;
        TextView mStatusTv;
        ImageView mGanderIv;//性别图标
        TextView mLengthTv;
        LinearLayout mitemll;

        public FriendViewHolder(View v) {
            mAgreeBtn = (com.rey.material.widget.Button) v.findViewById(R.id.btn_agree);
            mFriendNameTv = (TextView) v.findViewById(R.id.tv_friends_name);
            mFriendPhotoCiv = (CircularImage) v.findViewById(R.id.iv_search_friends_photo);
            mStatusTv = (TextView) v.findViewById(R.id.tv_friend_status);
            mGanderIv = (ImageView) v.findViewById(R.id.iv_gander);
            mitemll = (LinearLayout) v.findViewById(R.id.ll_item);
            mLengthTv = (TextView) v.findViewById(R.id.tv_length);
        }
    }

}
