package com.app.pao.adapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.event.EventComment;
import com.app.pao.entity.event.EventPlayVoice;
import com.app.pao.entity.event.EventUpdateSocialPos;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Raul.Fan on 2016/5/8.
 * 评论，点赞，语音 View Holder
 */
public class HistorySocialViewHolder extends RecyclerView.ViewHolder {

    /* contains */
    public static final int VIEW_TYPE_NORMAL = 0x01;//普通评论类型
    public static final int VIEW_TYPE_VOICE = 0x02;//语音评论类型
    public static final int VIEW_TYPE_THUMB = 0x03;//点赞类型


    private int itemType;
    /* local view same*/
    private LinearLayout mBaseLl;//整体布局
    private CircularImage mAvatarIv;//发起者头像
    private TextView mNickNameTv;//发起者昵称
    private TextView mSocialLocationTv;//点赞评论的位置
    private View mSelectV;
    private View mSelectBtn;

    /* comments view */
    private TextView mReplyTv;//回复文本
    private TextView mReplyNicknameTv;//回复的昵称
    private TextView mCommentsTv;//回复内容

    /* voice view */
    private ImageView mVoiceStateIv;
    private View mVoiceLengthIv;
    private TextView mVoiceLengthTv;
    private LinearLayout voiceLl;


    public HistorySocialViewHolder(View itemView, int itemType) {
        super(itemView);
        this.itemType = itemType;
        //same ui
        mBaseLl = (LinearLayout) itemView.findViewById(R.id.ll_base);
        mAvatarIv = (CircularImage) itemView.findViewById(R.id.iv_photo);
        mNickNameTv = (TextView) itemView.findViewById(R.id.tv_nickname);
        mSocialLocationTv = (TextView) itemView.findViewById(R.id.tv_social_loc);
        mSelectV = itemView.findViewById(R.id.v_select);
        mSelectBtn = itemView.findViewById(R.id.btn_select);

        //comments ui
        if (itemType == VIEW_TYPE_NORMAL) {
            mReplyTv = (TextView) itemView.findViewById(R.id.tv_reply);
            mReplyNicknameTv = (TextView) itemView.findViewById(R.id.tv_reply_name);
            mCommentsTv = (TextView) itemView.findViewById(R.id.tv_comments);
            return;
        }
        //voice ui
        if (itemType == VIEW_TYPE_VOICE) {
            mVoiceStateIv = (ImageView) itemView.findViewById(R.id.iv_voice);
            mVoiceLengthIv = itemView.findViewById(R.id.view_voice);
            mVoiceLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            voiceLl = (LinearLayout) itemView.findViewById(R.id.ll_voice);
        }
    }

    public void bindItem(final Context context, final GetSocialListResult.SocialsEntity entity, final BitmapUtils mBtimapUtils) {
        //same ui
        ImageUtils.loadUserImage( entity.avatar, mAvatarIv);
        mNickNameTv.setText(entity.nickname);
        String socialLoc = NumUtils.retainTheDecimal(entity.position) + "公里" + "  " +
                TimeUtils.getTimestampString(TimeUtils.stringToDate(entity.socialtime));
        mSocialLocationTv.setText(socialLoc);
        if (entity.isSelect) {
            mSelectV.setBackgroundResource(R.drawable.icon_history_social_select);
            mBaseLl.setBackgroundColor(Color.parseColor("#fffaf8"));
        } else {
            mSelectV.setBackgroundResource(R.drawable.icon_history_social_normal);
            mBaseLl.setBackgroundResource(android.R.color.white);
        }
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
        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventUpdateSocialPos(getAdapterPosition(), entity.latitude, entity.longitude));
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
        //comments ui
        if (itemType == VIEW_TYPE_NORMAL) {
            if (entity.replyuserid == 0) {
                mReplyNicknameTv.setVisibility(View.GONE);
                mReplyTv.setVisibility(View.GONE);
            } else {
                mReplyNicknameTv.setVisibility(View.VISIBLE);
                mReplyTv.setVisibility(View.VISIBLE);
                mReplyNicknameTv.setText(entity.replynickname);
            }
            mCommentsTv.setText(entity.comment);
            return;
        }
        //voice ui
        if (itemType == VIEW_TYPE_VOICE) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) voiceLl.getLayoutParams();
            float voiceLength = 50 + 5 * entity.length;
            if (voiceLength > 160) {
                voiceLength = 160;
            }
            lp.width = (int) DeviceUtils.dpToPixel(voiceLength);
            voiceLl.setLayoutParams(lp);
            mVoiceLengthTv.setText(entity.length + "''");

            voiceLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventPlayVoice(getAdapterPosition()));
                }
            });
            if (entity.isReadnow()) {
                mVoiceStateIv.setImageResource(R.drawable.anim_play_voice_gray);
                AnimationDrawable animationDrawable = (AnimationDrawable) mVoiceStateIv.getDrawable();
                animationDrawable.start();
            } else {
                mVoiceStateIv.setImageResource(R.drawable.icon_voice_play_gray_3);
            }
        }
    }
}
