package com.app.pao.adapter.ViewHolder;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.entity.event.EventPlayVoice;
import com.app.pao.entity.network.LiveSocial;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Raul on 2016/1/25.
 * 评论点赞View Holder
 */
public class LiveSocialViewHolder extends RecyclerView.ViewHolder {

    /* contains */
    private static final int VIEW_TYPE_NORMAL = 0x01;//普通评论类型
    private static final int VIEW_TYPE_VOICE = 0x02;//语音评论类型

    private CircularImage mPhotoIv;//头像
    private ImageView mThumbIv;//点赞图标
    private TextView mCommentsTv;//评论文本

    private ImageView mVoiceStateIv;
    private View mVoiceLengthIv;
    private TextView mVoiceLengthTv;
    private int itemType;
    private LinearLayout voiceLl;

    public LiveSocialViewHolder(View itemView, int itemType) {
        super(itemView);
        this.itemType = itemType;
        mPhotoIv = (CircularImage) itemView.findViewById(R.id.iv_photo);
        if (itemType == VIEW_TYPE_VOICE) {
            mVoiceStateIv = (ImageView) itemView.findViewById(R.id.iv_voice);
            mVoiceLengthIv = itemView.findViewById(R.id.view_voice);
            mVoiceLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            voiceLl = (LinearLayout) itemView.findViewById(R.id.ll_voice);
        } else {
            mThumbIv = (ImageView) itemView.findViewById(R.id.iv_thumb);
            mCommentsTv = (TextView) itemView.findViewById(R.id.tv_comments);
        }

    }

    public void bindItem(final LiveSocial social, BitmapUtils bitmapUtils) {
        ImageUtils.loadUserImage(social.getUseravatar(), mPhotoIv);
        if (itemType == VIEW_TYPE_VOICE) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mVoiceLengthIv.getLayoutParams();
            float voiceLength = 10 + 5 * social.getLength();
            if (voiceLength > 160) {
                voiceLength = 160;
            }
            lp.width = (int) DeviceUtils.dpToPixel(voiceLength);
            mVoiceLengthIv.setLayoutParams(lp);
            mVoiceLengthTv.setText(social.getLength() + "''");

            voiceLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new EventPlayVoice(getAdapterPosition()));
                }
            });

            if (social.isReadnow()) {
                mVoiceStateIv.setImageResource(R.drawable.anim_play_voice);
                AnimationDrawable  animationDrawable = (AnimationDrawable) mVoiceStateIv.getDrawable();
                animationDrawable.start();
            } else {
                if (social.isHasread()) {
                    mVoiceStateIv.setImageResource(R.drawable.icon_voice_play);
                } else {
                    mVoiceStateIv.setImageResource(R.drawable.icon_voice_not_play);
                }
            }
        } else {
            if (social.getType() == 1) {
                mThumbIv.setVisibility(View.VISIBLE);
                mCommentsTv.setVisibility(View.GONE);
            } else {
                mThumbIv.setVisibility(View.GONE);
                mCommentsTv.setVisibility(View.VISIBLE);
                mCommentsTv.setText(social.getComment());
            }
        }
    }
}
