package com.app.pao.adapter;

import android.app.Application;
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
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.model.PartyMemberListEntity;
import com.app.pao.entity.network.GetGroupPartyMemberListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.ui.widget.tagview.Tag;
import com.app.pao.ui.widget.tagview.TagView;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.T;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/19.
 * <p/>
 * 活动成员adapter
 */
public class GroupPartyMemberListAdapter extends RecyclerView.Adapter<GroupPartyMemberListAdapter.PartyMemberHolder> {
    private static int TYPE_HAS_TITLE = 0;
    private static int TYPE_NORMAL = 1;

    private List<PartyMemberListEntity> mDataList;
    private BitmapUtils bitmapUtils;
    private int mPartyOwner;
    Context context;

    public GroupPartyMemberListAdapter(Context context, int mPartyOwner, List<PartyMemberListEntity> mDataList) {
        this.mDataList = mDataList;
        this.context = context;
        this.mPartyOwner = mPartyOwner;
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public PartyMemberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HAS_TITLE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_member_has_title,
                    parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_member_normal, parent,
                    false);
        }
        PartyMemberHolder holder = new PartyMemberHolder(view, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(PartyMemberHolder holder, int position) {
        if (mDataList.get(position).isHeader()) {
            holder.bindItemTtile();
        } else {
            holder.bindItemMember();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        PartyMemberListEntity mCurrentMember = mDataList.get(position);
        if (mCurrentMember.isHeader()) {
            return TYPE_HAS_TITLE;
        } else {
            return TYPE_NORMAL;
        }
    }

    public class PartyMemberHolder extends RecyclerView.ViewHolder {
        private TextView mTitlePaceTv;
        private TextView mTitleNumTv;
        private CircularImage mMemberPhotoCi;
        private ImageView mLoginUserIv;
        private TextView mMemberNickNameTv;
        private TextView mIsOrzTv;
        private TextView mIsLeaderTv;
        private TagView mGroupTagTagv;
        private LinearLayout mBaseDatall;


        public PartyMemberHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_HAS_TITLE) {
                mTitlePaceTv = (TextView) itemView.findViewById(R.id.tv_member_title_pace);
                mTitleNumTv = (TextView) itemView.findViewById(R.id.tv_member_title_num);
            } else {
                mMemberPhotoCi = (CircularImage) itemView.findViewById(R.id.ci_member_photo);
                mLoginUserIv = (ImageView) itemView.findViewById(R.id.iv_is_login_user);
                mMemberNickNameTv = (TextView) itemView.findViewById(R.id.tv_member_nickname);
                mIsOrzTv = (TextView) itemView.findViewById(R.id.tv_is_orz);
                mIsLeaderTv = (TextView) itemView.findViewById(R.id.tv_is_leader);
                mBaseDatall = (LinearLayout) itemView.findViewById(R.id.ll_base);
                mGroupTagTagv = (TagView) itemView.findViewById(R.id.tagv_group_tag);
            }
        }

        //title
        public void bindItemTtile() {
            PartyMemberListEntity title = mDataList.get(getAdapterPosition());
            mTitlePaceTv.setText(title.getHeaderStr());
            mTitleNumTv.setText(title.getHeaderNumStr() + "人");
        }

        //member
        public void bindItemMember() {
            GetGroupPartyMemberListResult partyMember = mDataList.get(getAdapterPosition()).getMember();
            ImageUtils.loadUserImage(partyMember.getAvatar(), mMemberPhotoCi);
            mMemberNickNameTv.setText(partyMember.getNickname());
            if (partyMember.getIsleader() == 1) {
                mIsLeaderTv.setVisibility(View.VISIBLE);
            } else {
                mIsLeaderTv.setVisibility(View.GONE);
            }

          if (partyMember.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER) {
              mIsOrzTv.setVisibility(View.VISIBLE);
            } else {
              mIsOrzTv.setVisibility(View.INVISIBLE);
          }

            showGroupTag(partyMember);

            final int userid = partyMember.getUserid();
            if (userid == LocalApplication.getInstance().getLoginUser(context).getUserId()) {
                mLoginUserIv.setVisibility(View.VISIBLE);
            }else {
                mLoginUserIv.setVisibility(View.GONE);
            }
            mBaseDatall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userid == LocalApplication.getInstance().getLoginUser(context).getUserId()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid", userid);
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
        }

        /**
         * 显示跑团的tag
         */
        private void showGroupTag(GetGroupPartyMemberListResult ranking) {
            List<GetGroupPartyMemberListResult.Label> labelList = ranking.getLabel();
            if (labelList == null) {
                return;
            }
            List<Tag> tagList = new ArrayList<>();
            for (int i = 0; i < labelList.size(); i++) {
                Tag tag = new Tag("" + labelList.get(i).getName());
                tag.tagTextSize = 10;
                tag.radius = 2;
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
