package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetFriendListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyFriendViewHolder> {


    private BitmapUtils mBitmapU;
    private List<GetFriendListResult.FriendsEntity> mDataList;
    private Context mContext;

    public FriendListAdapter(Context context, List<GetFriendListResult.FriendsEntity> datas) {
        mBitmapU = new BitmapUtils(context);
        this.mDataList = datas;
        this.mContext = context;
    }

    @Override
    public MyFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friends_list, parent, false);
        MyFriendViewHolder holder = new MyFriendViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyFriendViewHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyFriendViewHolder extends RecyclerView.ViewHolder {
        TextView mFriendNameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像
        ImageView mGanderIv;//性别图像
        TextView mLengthTv;//总里程文本
        ImageButton mRunningBtn;//跑步状态按钮

        public MyFriendViewHolder(View itemView) {
            super(itemView);
            mFriendNameTv = (TextView) itemView.findViewById(R.id.tv_friends_nickname);
            mFriendPhotoCiv = (CircularImage) itemView.findViewById(R.id.iv_friends_photo);
            mGanderIv = (ImageView) itemView.findViewById(R.id.iv_gander);
            mLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            mRunningBtn = (ImageButton) itemView.findViewById(R.id.btn_running);
        }

        public void bindItem(int position) {
            //用户信息对象
            GetFriendListResult.FriendsEntity entity = mDataList.get(position);
            mFriendNameTv.setText(entity.nickname + "");
            ImageUtils.loadUserImage(entity.avatar, mFriendPhotoCiv);
            //性别
            if (entity.getGender() == AppEnum.UserGander.MAN) {
                mGanderIv.setBackgroundResource(R.drawable.icon_man);
            } else {
                mGanderIv.setBackgroundResource(R.drawable.icon_women);
            }
            //总里程
            mLengthTv.setText(NumUtils.retainTheDecimal(entity.getTotallength()) + "公里");
            if (entity.isrunning == AppEnum.IsRunning.RUNNING) {
                mRunningBtn.setVisibility(View.VISIBLE);
            } else {
                mRunningBtn.setVisibility(View.INVISIBLE);
            }

            final int id = entity.getId();
            final String nickName = entity.getNickname();
            final int gender = entity.getGender();
            final String avatar = entity.getAvatar();
            //跑步按钮点击事件
            mRunningBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LiveActivityV3.class);
                    intent.putExtra("userId", id);
                    intent.putExtra("userNickName", nickName);
                    intent.putExtra("userGender", gender);
                    intent.putExtra("avatar", avatar);
                    mContext.startActivity(intent);
                }
            });
            //行点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LocalApplication.getInstance().getLoginUser(mContext).userId != id) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("userid", id);
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent, bundle);
                    }
                }
            });
        }
    }
}
