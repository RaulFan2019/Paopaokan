package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.group.GroupMemberActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupMemberSortResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/29.
 */
public class GroupMemberRankingMonthAdapter extends RecyclerView.Adapter<GroupMemberRankingMonthAdapter.RankingHolder>{
    private List<GetGroupMemberSortResult> mRankingList ;
    private BitmapUtils bitmapUtils;
    private GetGroupDetailInfoRequest mGroupInfo;
    private Context mContext;

    public GroupMemberRankingMonthAdapter(Context context,GetGroupDetailInfoRequest mGroupInfo,  List<GetGroupMemberSortResult> mRankingList) {
        this.mRankingList = mRankingList;
        bitmapUtils = new BitmapUtils(context);
        this.mGroupInfo = mGroupInfo;
        mContext =context;
    }

    @Override
    public RankingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_week_ranking,parent,false);
        RankingHolder holder = new RankingHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mRankingList.size();
    }

    @Override
    public void onBindViewHolder(RankingHolder holder, int position) {
        holder.bindItem(position);
    }

    public class RankingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout mItemBgLl;
        CircularImage mUserAvatarCi;
        TextView mUserNameTv;
        TextView mIdentityTv;
        LinearLayout mUserGenderBgLl;
        ImageView mUserGenderIv;
        TextView mUserAgeTv;
        TagView mGroupTagTagv;
        TextView mUserLengthTv;

        public RankingHolder(View itemView) {
            super(itemView);
            mItemBgLl = (LinearLayout) itemView.findViewById(R.id.ll_item_bg);
            mUserAvatarCi = (CircularImage) itemView.findViewById(R.id.ci_user_avatar);
            mUserNameTv = (TextView) itemView.findViewById(R.id.tv_user_name);
            mIdentityTv = (TextView) itemView.findViewById(R.id.tv_identity);
            mUserGenderBgLl = (LinearLayout) itemView.findViewById(R.id.ll_user_gender_bg);
            mUserGenderIv = (ImageView) itemView.findViewById(R.id.iv_user_gender);
            mUserAgeTv = (TextView) itemView.findViewById(R.id.tv_user_age);
            mUserLengthTv = (TextView) itemView.findViewById(R.id.tv_user_length);
            mGroupTagTagv = (TagView) itemView.findViewById(R.id.tagv_group_tag);
            itemView.setOnClickListener(this);
        }

        public void bindItem(int position){
            GetGroupMemberSortResult ranking = mRankingList.get(position);
            ImageUtils.loadUserImage( ranking.getAvatar(), mUserAvatarCi);
            mUserNameTv.setText(ranking.getDisplayname());
            mUserLengthTv.setText(NumUtils.retainTheDecimal(ranking.getLength())+"公里");
            mUserAgeTv.setText(TimeUtils.getAgeFromBirthDay(ranking.getBirthdate())+"");
            //判断是否是管理
            if(ranking.getRole() == AppEnum.groupRole.OWNER){
                mIdentityTv.setVisibility(View.VISIBLE);
                mIdentityTv.setText("团长");
            }else if(ranking.getRole() == AppEnum.groupRole.MANAGER){
                mIdentityTv.setVisibility(View.VISIBLE);
                mIdentityTv.setText("管理员");
            }else {
                mIdentityTv.setVisibility(View.GONE);
            }

            //判断是否是自己
            if(ranking.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()){
                mUserLengthTv.setTextColor(Color.parseColor("#f06522"));
                mItemBgLl.setBackgroundColor(Color.parseColor("#fffaf8"));
            }else {
                mUserLengthTv.setTextColor(Color.parseColor("#222222"));
                mItemBgLl.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            if(ranking.getGender() == AppEnum.UserGander.WOMEN){
                mUserGenderBgLl.setBackgroundResource(R.drawable.bg_pink_square_box);
                mUserGenderIv.setImageResource(R.drawable.icon_women);
                mUserAgeTv.setTextColor(Color.parseColor("#e84d6e"));
            }else {
                mUserGenderBgLl.setBackgroundResource(R.drawable.bg_blue_square_box);
                mUserGenderIv.setImageResource(R.drawable.icon_man);
                mUserAgeTv.setTextColor(Color.parseColor("#2aa5de"));
            }

            //显示标签
            showGroupTag(ranking);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(mContext,GroupMemberActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("group",mGroupInfo);
            b.putInt("userid", mRankingList.get(getAdapterPosition()).getId());
            i.putExtras(b);
            mContext.startActivity(i);
        }

        /**
         * 显示跑团的tag
         */
        private void showGroupTag(GetGroupMemberSortResult ranking){
            List<GetGroupMemberSortResult.Label> labelList =  ranking.getLabel();

            if(labelList == null){
                return;
            }
            List<Tag> tagList = new ArrayList<>();
            for(int i = 0;i < labelList.size();i++){
                Tag tag = new Tag(""+labelList.get(i).getName());
                tag.tagTextSize= 10;
                tag.radius = 2;
                tag.layoutColor= Color.TRANSPARENT;
                tag.tagTextColor = Color.parseColor("#F06522");
                tag.layoutBorderColor=Color.parseColor("#F06522");
                tag.layoutBorderSize = 1;
                tagList.add(tag);
            }

            mGroupTagTagv.addTags(tagList);
        }
    }


}
