package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/10.
 */
public class HistoryVideoSocialAdapter extends RecyclerView.Adapter<HistoryVideoSocialAdapter.VideoSocialViewHolder>{

    private List<GetSocialListResult.SocialsEntity> mSocialsEntityList;
    private BitmapUtils mBitmapU;
    private Context mContext;

    public HistoryVideoSocialAdapter(final Context context, final List<GetSocialListResult.SocialsEntity> socialsEntityList) {
        mBitmapU = new BitmapUtils(context);
        this.mContext = context;
        this.mSocialsEntityList = socialsEntityList;
    }

    @Override
    public VideoSocialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history_video_list_social, parent, false);
        return new VideoSocialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoSocialViewHolder holder, int position) {
        final GetSocialListResult.SocialsEntity result = mSocialsEntityList.get(position);
        holder.bindItem(mContext,result, mBitmapU);
    }


    @Override
    public int getItemCount() {
        return mSocialsEntityList.size();
    }


    public class VideoSocialViewHolder extends RecyclerView.ViewHolder {

        /* local view same*/
        private LinearLayout mBaseLl;//整体布局
        private CircularImage mAvatarIv;//发起者头像
        private TextView mNickNameTv;//发起者昵称
        private TextView mReplyTv;//回复文本
        private TextView mReplyNicknameTv;//回复的昵称
        private TextView mSocialLocationTv;//点赞评论的位置
        private TextView mCommentsTv;//回复内容


        public VideoSocialViewHolder(View itemView) {
            super(itemView);
            mBaseLl = (LinearLayout) itemView.findViewById(R.id.ll_base);
            mAvatarIv = (CircularImage) itemView.findViewById(R.id.iv_photo);
            mNickNameTv = (TextView) itemView.findViewById(R.id.tv_nickname);
            mSocialLocationTv = (TextView) itemView.findViewById(R.id.tv_social_loc);
            mReplyTv = (TextView) itemView.findViewById(R.id.tv_reply);
            mReplyNicknameTv = (TextView) itemView.findViewById(R.id.tv_reply_name);
            mCommentsTv = (TextView) itemView.findViewById(R.id.tv_comments);

        }

        public void bindItem(final Context context, final GetSocialListResult.SocialsEntity entity,
                             BitmapUtils mBitmapU) {
            ImageUtils.loadUserImage( entity.avatar, mAvatarIv);
            mNickNameTv.setText(entity.nickname);
            String socialLoc = TimeUtils.getTimestampString(TimeUtils.stringToDate(entity.socialtime));
            mSocialLocationTv.setText(socialLoc);

            mBaseLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventComment mCurrCommentEnvent;
                    if (entity.getUserid() == LocalApplication.getInstance().getLoginUser(context).userId) {
                        DBUserEntity user = LocalApplication.getInstance().getLoginUser(context);
                        mCurrCommentEnvent = new EventComment(getAdapterPosition(), entity.latitude,
                                entity.longitude, 0, "", user.gender, user.avatar, user.name);
                    } else {
                        mCurrCommentEnvent = new EventComment(getAdapterPosition(), entity.latitude, entity.longitude,
                                entity.userid, entity.nickname, entity.gender, entity.avatar, entity.username);
                    }
                    EventBus.getDefault().post(mCurrCommentEnvent);
                }
            });
            //点击头像跳转
            mAvatarIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.userid != LocalApplication.getInstance().getLoginUser(context).userId) {
                        Intent i = new Intent(context, UserInfoActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("userid", entity.userid);
                        i.putExtras(b);
                        context.startActivity(i);
                    }
                }
            });
            if (entity.replyuserid == 0) {
                mReplyNicknameTv.setVisibility(View.GONE);
                mReplyTv.setVisibility(View.GONE);
            } else {
                mReplyNicknameTv.setVisibility(View.VISIBLE);
                mReplyTv.setVisibility(View.VISIBLE);
                mReplyNicknameTv.setText(entity.replynickname);
            }
            mCommentsTv.setText(entity.comment);
        }
    }

}
