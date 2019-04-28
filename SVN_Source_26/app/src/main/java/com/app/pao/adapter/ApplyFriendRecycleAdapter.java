package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.ApplyFriendListActivity;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetApplyFriendListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by LY on 2016/4/26.
 */
public class ApplyFriendRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<GetApplyFriendListResult> datas;

    public ApplyFriendRecycleAdapter(Context mContext, List<GetApplyFriendListResult> datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_apply_friends_list, parent, false);
        ViewsHolder holder = new ViewsHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewsHolder) holder).bindItem(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder {
        TextView mFriendNameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像
        ImageView mGanderIv;//性别图像
        TextView mLengthTv;//总里程文本
        Button mAgreeBtn;//同意按钮
        TextView mHasAddTv;//已添加文本

        public ViewsHolder(View itemView) {
            super(itemView);
            mFriendNameTv = (TextView) itemView.findViewById(R.id.tv_friends_nickname);
            mFriendPhotoCiv = (CircularImage) itemView.findViewById(R.id.iv_friends_photo);
            mGanderIv = (ImageView) itemView.findViewById(R.id.iv_gander);
            mLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            mAgreeBtn = (Button) itemView.findViewById(R.id.btn_agree);
            mHasAddTv = (TextView) itemView.findViewById(R.id.tv_has_add);
        }

        public void bindItem(final int position) {
            //用户信息对象
            final GetApplyFriendListResult entity = datas.get(position);
            //用户名
            mFriendNameTv.setText(entity.nickname + "");
            //用户头像
            ImageUtils.loadUserImage(entity.avatar, mFriendPhotoCiv);
            //性别
            if (entity.getGender() == AppEnum.UserGander.MAN) {
                mGanderIv.setBackgroundResource(R.drawable.icon_man);
            } else {
                mGanderIv.setBackgroundResource(R.drawable.icon_women);
            }
            //总里程
            mLengthTv.setText(NumUtils.retainTheDecimal(entity.getTotallength()) + "公里");
            //收到的请求
            if (entity.getType() == AppEnum.friendApplyType.GET) {
                if (entity.getStatus() == AppEnum.friendApplyStatus.AGREE) {
                    mAgreeBtn.setVisibility(View.INVISIBLE);
                    mHasAddTv.setVisibility(View.VISIBLE);
                    mHasAddTv.setText("已同意");
                } else if (entity.getStatus() == AppEnum.friendApplyStatus.DEPAND) {
                    mAgreeBtn.setVisibility(View.INVISIBLE);
                    mHasAddTv.setVisibility(View.VISIBLE);
                    mHasAddTv.setText("已拒绝");
                } else {
                    mAgreeBtn.setVisibility(View.VISIBLE);
                    mHasAddTv.setVisibility(View.INVISIBLE);
                }
                //发出的请求
            } else {
                if (entity.getStatus() == AppEnum.friendApplyStatus.AGREE) {
                    mAgreeBtn.setVisibility(View.INVISIBLE);
                    mHasAddTv.setVisibility(View.VISIBLE);
                    mHasAddTv.setText("已通过");
                } else {
                    mAgreeBtn.setVisibility(View.INVISIBLE);
                    mHasAddTv.setVisibility(View.VISIBLE);
                    mHasAddTv.setText("请求中");
                }
            }
            final int pos = position;
            final ApplyFriendListActivity activity = (ApplyFriendListActivity) mContext;
            mAgreeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.PostAgreeRequest(pos);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LocalApplication.getInstance().getLoginUser(mContext).userId != entity.getId()) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("userid", entity.getId());
                        Intent intent = new Intent(mContext, UserInfoActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent, bundle);
                    }
                }
            });
        }
    }
}
