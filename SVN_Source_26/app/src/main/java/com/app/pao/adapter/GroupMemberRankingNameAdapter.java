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
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupDetailInfoRequest;
import com.app.pao.entity.network.GetGroupMemberSortResult;
import com.app.pao.entity.network.GroupRankNameWithFirstPy;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/29.
 */
public class GroupMemberRankingNameAdapter extends RecyclerView.Adapter<GroupMemberRankingNameAdapter.NameRankingHolder> {
    private static final String TAG = "GroupMemberRankingNameAdapter";

    private static final int ITEM_GROUP_TITLE = 0;
    private static final int ITEM_CHILD_CON = 1;
    private static final int ITEM_TOTAL_NUM = 2;

    //    private List<GetGroupMemberSortResult> mNameRankingList;
    private List<GroupRankNameWithFirstPy> mDataRankingList;
    private BitmapUtils bitmapUtils;
    private GetGroupDetailInfoRequest mGroupInfo;
    private Context mContext;

    public GroupMemberRankingNameAdapter(Context context, GetGroupDetailInfoRequest mGroupInfo, List<GroupRankNameWithFirstPy> mDataRankingList) {
        this.mDataRankingList = mDataRankingList;
        this.mGroupInfo = mGroupInfo;
        mContext = context;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public NameRankingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_GROUP_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_name_rank_firstpy, parent, false);
        } else if (viewType == ITEM_CHILD_CON) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_name_ranking, parent, false);
        } else if (viewType == ITEM_TOTAL_NUM){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend_name_rank_lastline, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_name_ranking, parent, false);
        }
        return new NameRankingHolder(view,viewType);
    }

    @Override
    public int getItemCount() {
        return mDataRankingList.size();
    }

    @Override
    public void onBindViewHolder(NameRankingHolder holder, int position) {
        if(mDataRankingList.get(position).getType() == ITEM_CHILD_CON) {
            holder.bindItem(position);
        }else if (mDataRankingList.get(position).getType() == ITEM_GROUP_TITLE){
            holder.bindPyTitleItem(position);
        }else {
            holder.bindTotalNum(position);
        }
    }

    @Override
    public int getItemViewType(int position) {
//        if (mDataRankingList.get(position).getType() == ITEM_GROUP_TITLE) {
//            return ITEM_GROUP_TITLE;
//        } else if(mDataRankingList.get(position).getType() == ITEM_CHILD_CON) {
//            return ITEM_CHILD_CON;
//        }
        return mDataRankingList.get(position).getType();
    }

    public int getPositionForSelection(char selection){
        for(int i = 0 ; i < mDataRankingList.size() ; i ++){
            if(mDataRankingList.get(i).getType()==0){
                char fir = mDataRankingList.get(i).getFirstPinYin();
                if(fir == selection){
                    return i;
                }
            }
        }
        return -1;
    }

    public class NameRankingHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout mItemBgLl;
        CircularImage mUserAvatarCi;
        TextView mUserNameTv;
        TextView mIdentityTv;
        LinearLayout mUserGenderBgLl;
        ImageView mUserGenderIv;
        TextView mUserAgeTv;
        ImageView mUserIsRunningIv;
        TagView mGroupTagTagv;
        TextView mFirstPyTitleTv;
        TextView mTotalNumTv;

        public NameRankingHolder(View itemView,int viewType) {
            super(itemView);
            if(viewType == ITEM_CHILD_CON) {
                mItemBgLl = (LinearLayout) itemView.findViewById(R.id.ll_item_bg);
                mUserAvatarCi = (CircularImage) itemView.findViewById(R.id.ci_user_avatar);
                mUserNameTv = (TextView) itemView.findViewById(R.id.tv_user_name);
                mIdentityTv = (TextView) itemView.findViewById(R.id.tv_identity);
                mUserGenderBgLl = (LinearLayout) itemView.findViewById(R.id.ll_user_gender_bg);
                mUserGenderIv = (ImageView) itemView.findViewById(R.id.iv_user_gender);
                mUserAgeTv = (TextView) itemView.findViewById(R.id.tv_user_age);
                mUserIsRunningIv = (ImageView) itemView.findViewById(R.id.iv_is_running);
                mGroupTagTagv = (TagView) itemView.findViewById(R.id.tagv_group_tag);
                itemView.setOnClickListener(this);
            }else if(viewType == ITEM_GROUP_TITLE){
                mFirstPyTitleTv  = (TextView) itemView.findViewById(R.id.tv_first_pinyin);
            }else if(viewType == ITEM_TOTAL_NUM){
                mTotalNumTv = (TextView) itemView.findViewById(R.id.tv_last_friend);
            }
        }

        public void bindPyTitleItem(int position) {
            mFirstPyTitleTv.setText(mDataRankingList.get(position).getFirstPinYin()+"");
        }

        public void bindTotalNum(int position){
            mTotalNumTv.setText("共" + mDataRankingList.get(position).getLastLine() + "名成员");
        }

        public void bindItem(int position) {
            GetGroupMemberSortResult ranking = mDataRankingList.get(position).getGroupMember();
            ImageUtils.loadUserImage( ranking.getAvatar(), mUserAvatarCi);
            mUserNameTv.setText(ranking.getDisplayname());
            mUserAgeTv.setText(TimeUtils.getAgeFromBirthDay(ranking.getBirthdate()) + "");
            //判断是否是管理
            if (ranking.getRole() == AppEnum.groupRole.OWNER) {
                mIdentityTv.setVisibility(View.VISIBLE);
                mIdentityTv.setText("团长");
            } else if (ranking.getRole() == AppEnum.groupRole.MANAGER) {
                mIdentityTv.setVisibility(View.VISIBLE);
                mIdentityTv.setText("管理员");
            } else {
                mIdentityTv.setVisibility(View.GONE);
            }

            //判断是否是自己
            if (ranking.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                mItemBgLl.setBackgroundColor(Color.parseColor("#fffaf8"));
            } else {
                mItemBgLl.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            if (ranking.getGender() == AppEnum.UserGander.WOMEN) {
                mUserGenderBgLl.setBackgroundResource(R.drawable.bg_pink_square_box);
                mUserGenderIv.setImageResource(R.drawable.icon_women);
                mUserAgeTv.setTextColor(Color.parseColor("#e84d6e"));
            } else {
                mUserGenderBgLl.setBackgroundResource(R.drawable.bg_blue_square_box);
                mUserGenderIv.setImageResource(R.drawable.icon_man);
                mUserAgeTv.setTextColor(Color.parseColor("#2aa5de"));
            }

            //显示标签
            showGroupTag(ranking);
            //跑步状态
            if (ranking.getIsrunning() == AppEnum.RunningStatus.RUNNING) {
                mUserIsRunningIv.setVisibility(View.VISIBLE);
            } else {
                mUserIsRunningIv.setVisibility(View.GONE);
            }

            final int userid = ranking.getId();
            final String nickname = ranking.getNickname();
            final int usergender = ranking.getGender();
            final String avatar = ranking.getAvatar();
            //跑步按钮点击事件
            mUserIsRunningIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LiveActivityV3.class);
                    intent.putExtra("userId", userid);
                    intent.putExtra("userNickName", nickname);
                    intent.putExtra("userGender", usergender);
                    intent.putExtra("avatar", avatar);
                    mContext.startActivity(intent);
                }
            });
        }


        @Override
        public void onClick(View v) {
            Intent i = new Intent(mContext, GroupMemberActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("group", mGroupInfo);
            b.putInt("userid", mDataRankingList.get(getAdapterPosition()).getGroupMember().getId());
            i.putExtras(b);
            mContext.startActivity(i);
        }

        /**
         * 显示跑团的tag
         */
        private void showGroupTag(GetGroupMemberSortResult ranking) {
            List<GetGroupMemberSortResult.Label> labelList = ranking.getLabel();

            if (labelList == null) {
                return;
            }
            List<Tag> tagList = new ArrayList<>();
            for (int i = 0; i < labelList.size(); i++) {
                Tag tag = new Tag("" + labelList.get(i).getName());
                tag.tagTextSize = 10;
                tag.radius = (float) 1.5;
                tag.layoutColor = Color.TRANSPARENT;
                tag.tagTextColor = Color.parseColor("#F06522");
                tag.layoutBorderColor = Color.parseColor("#F06522");
                tag.layoutBorderSize = 1;
                tagList.add(tag);
            }

            mGroupTagTagv.addTags(tagList);
        }
    }


}
