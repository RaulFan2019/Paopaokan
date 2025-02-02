package com.app.pao.adapter.ViewHolder;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventPlayVoice;
import com.app.pao.entity.network.GetCommentsResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Raul on 2016/1/15.
 * 点赞列表ViewHolder
 */
public class CommentsListViewHolder extends RecyclerView.ViewHolder {

    /* contains */
    private static final int VIEW_TYPE_NORMAL = 0x01;//普通评论类型
    private static final int VIEW_TYPE_VOICE = 0x02;//语音评论类型

    private TextView mFriendNameTv;// 姓名文本
    private CircularImage mFriendPhotoCiv;// 头像
    private TextView mTimeTv;//时间文本
    private TextView mCommentTv;//评论文本
    private TextView mReplyTv;//回复文本
    private TextView mReplyNameTv;//回复人员文本
    private LinearLayout mBasell;

    private ImageView mVoiceStateIv;
    private View mVoiceLengthIv;
    private TextView mVoiceLengthTv;
    private int itemType;
    private LinearLayout voiceLl;


    public CommentsListViewHolder(View itemView , int itemType) {
        super(itemView);
        this.itemType = itemType;

        mFriendNameTv = (TextView) itemView.findViewById(R.id.tv_user_name);
        mFriendPhotoCiv = (CircularImage) itemView.findViewById(R.id.iv_friends_photo);
        mTimeTv = (TextView) itemView.findViewById(R.id.tv_time);
        mReplyTv = (TextView) itemView.findViewById(R.id.tv_reply);
        mReplyNameTv = (TextView) itemView.findViewById(R.id.tv_reply_name);
        mBasell = (LinearLayout) itemView.findViewById(R.id.ll_comments);

        if (itemType == VIEW_TYPE_VOICE) {
            mVoiceStateIv = (ImageView) itemView.findViewById(R.id.iv_voice);
            mVoiceLengthIv = itemView.findViewById(R.id.view_voice);
            mVoiceLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            voiceLl = (LinearLayout) itemView.findViewById(R.id.ll_voice);
        }else {
            mCommentTv = (TextView) itemView.findViewById(R.id.tv_comments);
        }

    }

    public void bindItem(final Context context,final GetCommentsResult item, BitmapUtils mBtimapUtils) {
        mFriendNameTv.setText(item.getNickname());
        ImageUtils.loadUserImage( item.getAvatar(), mFriendPhotoCiv);
        mTimeTv.setText(TimeUtils.getTimestampString(TimeUtils.stringToDate(item.getCommenttime())));

        if (item.getReplyuserid() == 0) {
            mReplyNameTv.setVisibility(View.GONE);
            mReplyTv.setVisibility(View.GONE);
        } else {
            mReplyNameTv.setVisibility(View.VISIBLE);
            mReplyTv.setVisibility(View.VISIBLE);
            mReplyNameTv.setText(item.getReplynickname());
        }

        if (itemType == VIEW_TYPE_VOICE) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVoiceLengthIv.getLayoutParams();
            float voiceLength = 10 + 5 * item.getLength();
            if (voiceLength > 160) {
                voiceLength = 160;
            }
            lp.width = (int) DeviceUtils.dpToPixel(voiceLength);
            mVoiceLengthIv.setLayoutParams(lp);
            mVoiceLengthTv.setText(item.getLength() + "''");

            voiceLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventPlayVoice(getAdapterPosition()));
                }
            });

            if (item.isReadnow()) {
                mVoiceStateIv.setImageResource(R.drawable.anim_play_voice_gray);
                AnimationDrawable animationDrawable = (AnimationDrawable) mVoiceStateIv.getDrawable();
                animationDrawable.start();
            } else {
                if (item.isHasread()) {
                    mVoiceStateIv.setImageResource(R.drawable.icon_voice_play_gray_3);
                } else {
                    mVoiceStateIv.setImageResource(R.drawable.icon_voice_not_play);
                }
            }
        }else {
            mCommentTv.setText(item.getComment());
        }
        mBasell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventComment mCurrCommentEnvent;
                if (item.getUserid() == LocalApplication.getInstance().getLoginUser(context).getUserId()) {
                    mCurrCommentEnvent = new EventComment(0, 0, "");
                } else {
                    mCurrCommentEnvent = new EventComment(0, item.getUserid(), item.getNickname());
                }
                EventBus.getDefault().post(mCurrCommentEnvent);
            }
        });

    }
}
