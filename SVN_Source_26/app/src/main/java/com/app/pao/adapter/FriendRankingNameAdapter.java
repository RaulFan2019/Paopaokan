package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.FriendRankNameWithPyFirst;
import com.app.pao.entity.network.GetFriendRankResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by LY on 2016/3/17.
 */
public class FriendRankingNameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int First_ITEM = 0;
    private static final int NOMAL_ITEM = 1;
    private static final int LAST_ITEM = 2;

    private List<FriendRankNameWithPyFirst> friendRankList ;
    private Context mContext;
    private BitmapUtils mBitMapU;

    public FriendRankingNameAdapter(List<FriendRankNameWithPyFirst> friendRankList, Context mContext) {
        this.friendRankList = friendRankList;
        this.mContext = mContext;
        mBitMapU = new BitmapUtils(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == NOMAL_ITEM){
            View viewNomal = LayoutInflater.from(mContext).inflate(R.layout.item_friend_name_ranking,parent,false);
            NomalItemHolder holder = new NomalItemHolder(viewNomal);
            return holder;
        }else if(viewType == LAST_ITEM){
            View viewNomal = LayoutInflater.from(mContext).inflate(R.layout.item_friend_name_rank_lastline,parent,false);
            LastItemHolder holder = new LastItemHolder(viewNomal);
            return holder;
        }else {
            View viewNomal = LayoutInflater.from(mContext).inflate(R.layout.item_friend_name_rank_firstpy,parent,false);
            FirstItemHolder holder = new FirstItemHolder(viewNomal);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FriendRankNameWithPyFirst friendResult = friendRankList.get(position);
        if(friendResult!=null){
            if(holder instanceof LastItemHolder){
                ((LastItemHolder) holder).bindLastItem(position);
            }else if(holder instanceof NomalItemHolder){
                ((NomalItemHolder) holder).bindNomalItem(position);
            }else{
                ((FirstItemHolder) holder).bindFirstItem(position);
            }
        }
    }

    @Override
    public int getItemCount() {
        return friendRankList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FriendRankNameWithPyFirst friendResultFirst = friendRankList.get(position);
        int type = friendResultFirst.getType();
        if(type == First_ITEM){
            return First_ITEM;
        }else if(type == NOMAL_ITEM){
            return NOMAL_ITEM;
        }else {
            return LAST_ITEM;
        }
    }

    /**
     * 普通布局的viewholder
     */
    public class NomalItemHolder extends RecyclerView.ViewHolder{
        CircularImage mAvatarCi;
        TextView mUserNameTv;
        ImageView mUserGenderIv;
        TextView mUserLoactionProvinceTv;
        TextView mUserLoactionCityTv;
        ImageView mIsRunningIv;
        LinearLayout mItemLl;

        public NomalItemHolder(View itemView) {
            super(itemView);
            mAvatarCi = (CircularImage) itemView.findViewById(R.id.ci_user_avatar);
            mUserNameTv = (TextView) itemView.findViewById(R.id.tv_user_name);
            mUserGenderIv = (ImageView) itemView.findViewById(R.id.iv_user_gender);
            mUserLoactionProvinceTv = (TextView) itemView.findViewById(R.id.tv_user_location_province);
            mUserLoactionCityTv = (TextView) itemView.findViewById(R.id.tv_user_location_city);
            mIsRunningIv = (ImageView) itemView.findViewById(R.id.iv_is_running);
            mItemLl = (LinearLayout) itemView.findViewById(R.id.ll_item);
        }

        public void bindNomalItem(final int position){
            //获取数据
            FriendRankNameWithPyFirst friendResultFirst = friendRankList.get(position);
            GetFriendRankResult friendResult = friendResultFirst.getFriendRankResult();

            //加载头像
            ImageUtils.loadUserImage( friendResult.getAvatar(), mAvatarCi);
            //姓名
            mUserNameTv.setText(friendResult.getNickname());

            //性别
            if(friendResult.getGender()== AppEnum.UserGander.MAN){
                mUserGenderIv.setBackgroundResource(R.drawable.icon_man);
            }else{
                mUserGenderIv.setBackgroundResource(R.drawable.icon_women);
            }
            //省市
            mUserLoactionProvinceTv.setText(friendResult.getLocationprovince());
            mUserLoactionCityTv.setText(friendResult.getLocationcity());

            //是否在跑步
            if(friendResult.getIsrunning()==AppEnum.IsRunning.NOT_RUNNING){
                mIsRunningIv.setVisibility(View.GONE);
            }else {
                mIsRunningIv.setVisibility(View.VISIBLE);
            }

            final int id = friendResult.getId();
            final String nickName = friendResult.getNickname();
            final int gender = friendResult.getGender();
            final String avatar = friendResult.getAvatar();

            mIsRunningIv.setOnClickListener(new View.OnClickListener() {
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

            mItemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(LocalApplication.getInstance().getLoginUser(mContext).userId != id){
                        Bundle bundle = new Bundle();
                        bundle.putInt("userid", id);
                        Intent intent = new Intent(mContext,UserInfoActivity.class);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent, bundle);
                    }
                }
            });
        }
    }

    public class LastItemHolder extends RecyclerView.ViewHolder{
        TextView mLastFriendTv;

        public LastItemHolder(View itemView) {
            super(itemView);
            mLastFriendTv = (TextView) itemView.findViewById(R.id.tv_last_friend);
        }

        public void bindLastItem(int position){
            FriendRankNameWithPyFirst friendResultFirst = friendRankList.get(position);
            mLastFriendTv.setText("共" + friendResultFirst.getLastLine() + "位好友");
        }
    }

    public class FirstItemHolder extends RecyclerView.ViewHolder{
        TextView mFirstPinYinTv;

        public FirstItemHolder(View itemView) {
            super(itemView);
            mFirstPinYinTv = (TextView) itemView.findViewById(R.id.tv_first_pinyin);
        }

        public void bindFirstItem(int position){
            FriendRankNameWithPyFirst friendResultFirst = friendRankList.get(position);
            mFirstPinYinTv.setText(friendResultFirst.getPinYinFirst()+"");
        }
    }

    public int getPositionForSelection(char selection){
        for(int i = 0 ; i < friendRankList.size() ; i ++){
            if(friendRankList.get(i).getType()==0){
                char fir = friendRankList.get(i).getPinYinFirst();
                if(fir == selection){
                    return i;
                }
            }
        }
        return -1;
    }
}
