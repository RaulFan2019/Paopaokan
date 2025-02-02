package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetSearchUserResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by LY on 2016/4/15.
 */
public class SearchFriendsRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GetSearchUserResult.UsersEntity> mLists;
    private Context mContext;
    private BitmapUtils mBitmapUtils;
    private OnAddFriendButClickListener listener;

    public SearchFriendsRvAdapter(List<GetSearchUserResult.UsersEntity> mLists, Context mContext,BitmapUtils mBitmapUtils) {
        this.mLists = mLists;
        this.mContext = mContext;
        this.mBitmapUtils = mBitmapUtils;
    }

    public void setOnAddFriendButClickListener(OnAddFriendButClickListener listener){
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_search_friends_list, parent, false);
        ItemHolder holder = new ItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder)holder).bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        CircularImage mFriendAvatarCi;
        TextView mFriendNameTv;
        ImageView mFriendGanderIv;
        TextView mFriendFromTv;
        Button mAddFriendBut;
        TextView mFriendStateTv;

        public ItemHolder(View itemView) {
            super(itemView);
            mFriendAvatarCi = (CircularImage) itemView.findViewById(R.id.iv_search_friends_photo);
            mFriendNameTv = (TextView) itemView.findViewById(R.id.tv_friends_name);
            mFriendGanderIv = (ImageView) itemView.findViewById(R.id.iv_gander);
            mFriendFromTv = (TextView) itemView.findViewById(R.id.tv_freinds_from);
            mAddFriendBut = (Button) itemView.findViewById(R.id.btn_agree);
            mFriendStateTv = (TextView) itemView.findViewById(R.id.tv_friend_status);
        }

        public void bindItem(final int position){
            GetSearchUserResult.UsersEntity fEntity = mLists.get(position);
            //用户性别
            if (fEntity.getGender() == AppEnum.UserGander.MAN) {
                mFriendGanderIv.setBackgroundResource(R.drawable.icon_man);
            } else {
                mFriendGanderIv.setBackgroundResource(R.drawable.icon_women);
            }
            //用户来源
            mFriendNameTv.setText(fEntity.nickname+"");
            if(fEntity.getFrom()==2){
                mFriendFromTv.setText("来自于微信");
            }else{
                mFriendFromTv.setText("来自于手机");
            }
            //用户头像
            ImageUtils.loadUserImage(fEntity.getAvatar(),mFriendAvatarCi);
            //是否是好友
            if(fEntity.getIsFriend() == AppEnum.IsFriend.FRIEND){
                mAddFriendBut.setVisibility(View.GONE);
                mFriendStateTv.setVisibility(View.VISIBLE);
                mFriendStateTv.setText("已添加");
            }else if(fEntity.getHasSendApply() == AppEnum.HasApply.APPLY){
                mAddFriendBut.setVisibility(View.GONE);
                mFriendStateTv.setVisibility(View.VISIBLE);
                mFriendStateTv.setText("已申请");
            }else{
                mFriendStateTv.setVisibility(View.GONE);
                mAddFriendBut.setVisibility(View.VISIBLE);
            }

            if(fEntity.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()){
                mAddFriendBut.setVisibility(View.GONE);
                mFriendStateTv.setVisibility(View.VISIBLE);
                mFriendStateTv.setText("我");
            }
            mAddFriendBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        listener.addFriend(position);
                    }
                }
            });
            final GetSearchUserResult.UsersEntity entity = fEntity;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid", entity.getId());
                    Intent intent = new Intent(mContext, UserInfoActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public interface OnAddFriendButClickListener{
        void addFriend(int pos);
    }
}
