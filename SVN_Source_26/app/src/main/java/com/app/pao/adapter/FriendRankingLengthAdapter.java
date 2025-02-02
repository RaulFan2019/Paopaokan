package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetFriendRankResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by LY on 2016/3/17.
 */
public class FriendRankingLengthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<GetFriendRankResult> friendRankList;
    private Context mContext;
    private BitmapUtils mBitMapU;
    private Typeface typeface;//用于设置字体类型

    public FriendRankingLengthAdapter(List<GetFriendRankResult> friendRankList, Context mContext) {
        this.friendRankList = friendRankList;
        this.mContext = mContext;
        mBitMapU = new BitmapUtils(mContext);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_friend_ranking, parent, false);
        ItemHolder holder = new ItemHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ItemHolder) holder).bindItem(position);
    }

    @Override
    public int getItemCount() {
        return friendRankList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        LinearLayout mBaseLl;
        CircularImage mAvatarCi;
        TextView mUserNameTv;
        ImageView mUserGenderIv;
        TextView mUserLoactionProvinceTv;
        TextView mUserLoactionCityTv;
        TextView mUserLengthTv;

        public ItemHolder(View itemView) {
            super(itemView);
            mBaseLl = (LinearLayout) itemView.findViewById(R.id.ll_base);
            mAvatarCi = (CircularImage) itemView.findViewById(R.id.ci_user_avatar);
            mUserNameTv = (TextView) itemView.findViewById(R.id.tv_user_name);
            mUserGenderIv = (ImageView) itemView.findViewById(R.id.iv_user_gender);
            mUserLoactionProvinceTv = (TextView) itemView.findViewById(R.id.tv_user_location_province);
            mUserLoactionCityTv = (TextView) itemView.findViewById(R.id.tv_user_location_city);
            mUserLengthTv = (TextView) itemView.findViewById(R.id.tv_user_length);
            mBaseLl = (LinearLayout) itemView.findViewById(R.id.ll_base);

            mUserLengthTv.setTypeface(typeface);
        }

        public void bindItem(int position) {
            GetFriendRankResult friendResult = friendRankList.get(position);
            ImageUtils.loadUserImage(friendResult.getAvatar(), mAvatarCi);
            mUserNameTv.setText(friendResult.getNickname());
            if (friendResult.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId())
                mBaseLl.setBackgroundColor(Color.parseColor("#fef7f4"));
            if (friendResult.getGender() == AppEnum.UserGander.MAN) {
                mUserGenderIv.setBackgroundResource(R.drawable.icon_man);
            } else {
                mUserGenderIv.setBackgroundResource(R.drawable.icon_women);
            }
            mUserLoactionProvinceTv.setText(friendResult.getLocationprovince());
            mUserLoactionCityTv.setText(friendResult.getLocationcity());
            mUserLengthTv.setText(NumUtils.retainTheDecimal(friendResult.getLength()) + "公里");
            if (friendResult.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                mUserLengthTv.setTextColor(Color.parseColor("#f06522"));
                mBaseLl.setBackgroundColor(Color.parseColor("#fffaf8"));
            } else {
                mUserLengthTv.setTextColor(Color.parseColor("#222222"));
                mBaseLl.setBackgroundResource(R.drawable.bg_list_item);
            }

            final int id = friendResult.getId();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (id != LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
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
