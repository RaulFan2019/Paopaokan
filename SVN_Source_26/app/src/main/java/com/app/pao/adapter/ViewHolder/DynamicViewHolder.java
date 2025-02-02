package com.app.pao.adapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.activity.group.GroupInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.adapter.DynamicLineItem;
import com.app.pao.entity.event.EventDynamicClick;
import com.app.pao.entity.event.EventThumb;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;

/**
 * Created by Raul on 2016/1/11.
 */
public class DynamicViewHolder extends RecyclerView.ViewHolder {

    int viewType;

    private static final int VIEW_TYPE_HEADER = 0x01;//标题类型
    private static final int VIEW_TYPE_PARTY = 0x02;//活动类型
    private static final int VIEW_TYPE_WOKROUT = 0x03;//跑步历史类型

    /* header view */
    private TextView mHeaderTv;//标题文本

    /* common view*/
    private LinearLayout mLongOnClickRl;
    private LinearLayout mThumbupBtn;

    /* workout view */
    private TextView runnerInfoBtn;
    private TextView lengthTv;
    private TextView timeTv;
    private TextView paceTv;
    private TextView nickNameTv;
    private CircularImage photoIv;
    private TextView mDyTime;
    private LinearLayout mCommentsBtn;
    private TextView mCommentsCountTv;
    private ImageView thumbupCanIv;
    private TextView thumbupCountTv;
    private ImageView heartrateIv;
    private TextView heartrateTv;
    private View hasVideoV;
    private ImageView workoutAvatarIv;

    /* party view */
//    private TextView groupInfoBtn;
    private ImageView mGroupAvatar;
    private TextView groupNameTv;
    private TextView partyNameTv;
    private TextView contractTimeTv;
    private ImageView partyIv;
    private TextView locationTv;
    private TextView partyTimeTv;
    private TextView partyLengthTv;
    private TextView partyPersonCountTv;
    private LinearLayout signLl;//未开始状态布局
    private TextView signTv;//报名状态文本
    private LinearLayout startLl;//开始状态布局
    private LinearLayout endLl;//结束状态布局

    public DynamicViewHolder(View itemView, int viewType, Typeface typeface) {
        super(itemView);
        this.viewType = viewType;
        //标题布局
        if (viewType == VIEW_TYPE_HEADER) {
            mHeaderTv = (TextView) itemView.findViewById(R.id.tv_header);
            //活动布局
        } else {
            mThumbupBtn = (LinearLayout) itemView.findViewById(R.id.btn_thumbup);
            mDyTime = (TextView) itemView.findViewById(R.id.tv_dynamic_starttime);
        }
        if (viewType == VIEW_TYPE_WOKROUT) {
            lengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            paceTv = (TextView) itemView.findViewById(R.id.tv_pace);
            mLongOnClickRl = (LinearLayout) itemView.findViewById(R.id.rl_history_child);
            mCommentsBtn = (LinearLayout) itemView.findViewById(R.id.rl_comments);
            mCommentsCountTv = (TextView) itemView.findViewById(R.id.tv_comments_count);
            thumbupCanIv = (ImageView) itemView.findViewById(R.id.iv_can_thumbup);
            thumbupCountTv = (TextView) itemView.findViewById(R.id.tv_thumb_count);
            photoIv = (CircularImage) itemView.findViewById(R.id.iv_dynamic_user_avatar);
            nickNameTv = (TextView) itemView.findViewById(R.id.tv_dynamic_nickname);
            heartrateIv = (ImageView) itemView.findViewById(R.id.iv_heartrate);
            heartrateTv = (TextView) itemView.findViewById(R.id.tv_heartrate);
            hasVideoV = itemView.findViewById(R.id.iv_has_video);
            workoutAvatarIv = (ImageView) itemView.findViewById(R.id.iv_history_avatar);
            //字体
            lengthTv.setTypeface(typeface);
            timeTv.setTypeface(typeface);
            paceTv.setTypeface(typeface);
            mCommentsCountTv.setTypeface(typeface);
            thumbupCountTv.setTypeface(typeface);
            heartrateTv.setTypeface(typeface);

        } else if(viewType == VIEW_TYPE_PARTY){
            mLongOnClickRl = (LinearLayout) itemView.findViewById(R.id.rl_history_child);
            groupNameTv = (TextView) itemView.findViewById(R.id.tv_group_name);
            contractTimeTv = (TextView) itemView.findViewById(R.id.tv_party_contrast_time);
            partyIv = (ImageView) itemView.findViewById(R.id.iv_party);
            locationTv = (TextView) itemView.findViewById(R.id.tv_location);
            partyNameTv = (TextView) itemView.findViewById(R.id.tv_party_name);
            partyTimeTv = (TextView) itemView.findViewById(R.id.tv_party_time);
            mGroupAvatar = (ImageView) itemView.findViewById(R.id.iv_dynamic_group_avatar);
            partyLengthTv = (TextView) itemView.findViewById(R.id.tv_party_length);
            partyPersonCountTv = (TextView) itemView.findViewById(R.id.tv_party_person_count);
            signLl = (LinearLayout) itemView.findViewById(R.id.ll_party_signup);
            startLl = (LinearLayout) itemView.findViewById(R.id.ll_party_start);
            endLl = (LinearLayout) itemView.findViewById(R.id.ll_party_end);
            signTv = (TextView) itemView.findViewById(R.id.tv_has_signup);

            partyTimeTv.setTypeface(typeface);
            partyLengthTv.setTypeface(typeface);
            partyPersonCountTv.setTypeface(typeface);
        }

    }

    public void bindItem(final Context context, final DynamicLineItem item) {
        if (item.isHeader) {
            mHeaderTv.setText(item.hearderString);
            return;
        }

        //跑步动态
        if (item.result.getDynamictype() == AppEnum.dynamicType.WORKOUT) {
            bindWorkoutView(context, item.result);
        } else {
            bindPartyView(context, item.result);
        }

    }


    private void bindWorkoutView(final Context context, final GetDynamicListResult item) {
        final GetDynamicListResult.Workout entity = JSON.parseObject(item.getInfo(), GetDynamicListResult.Workout
                .class);
        // 长度
        double workoutLength = (entity.length / 1000f);
        final BigDecimal bg = new BigDecimal(workoutLength);
        double length = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        lengthTv.setText(length + "公里");
        //配速
        paceTv.setText(TimeUtils.formatSecondsToSpeedTime((long) (entity.duration * 1000 / entity.length), "'"));
        //时间
        timeTv.setText(TimeUtils.formatSecondsToLongHourTime(entity.duration));
//        头像
        ImageUtils.loadUserImage(item.avatar, photoIv);
        //昵称
        nickNameTv.setText(item.nickname);

        ImageUtils.loadWorkoutBgImage(entity.getThumbnail(), workoutAvatarIv);
        //开始时间
        mDyTime.setText(TimeUtils.getDynamicDayTime(item.starttime));

        //点击历史
        mLongOnClickRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventDynamicClick(getAdapterPosition(), item));
            }
        });

        //心率
        if (entity.getAvgheartrate() == 0) {
            heartrateTv.setVisibility(View.GONE);
            heartrateIv.setVisibility(View.GONE);
        } else {
            heartrateIv.setVisibility(View.VISIBLE);
            heartrateTv.setVisibility(View.VISIBLE);
            heartrateTv.setText(entity.getAvgheartrate() + "");
        }
        //评论按钮
        mCommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventDynamicClick(getAdapterPosition(), item));
            }
        });


        final int userid = item.getUserid();
        //点击人物信息 runnerInfoBtn
        photoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userid != LocalApplication.getInstance().getLoginUser(context).userId) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid", userid);
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        //点赞相关
        final int targetid = item.id;
        final int type = item.dynamictype;
        final int postion = getAdapterPosition();
        //是否可以点赞
        if (item.hasGiveThumbup == AppEnum.hasGiveThumbup.YES) {
            thumbupCanIv.setBackgroundResource(R.drawable.icon_thumbup_can_not);
        } else {
            thumbupCanIv.setBackgroundResource(R.drawable.icon_thumbup_can);
        }

        //点赞数量
        thumbupCountTv.setText(item.thumbupcount + "");
        //点攒监听事件
        mThumbupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventThumb(targetid, type, postion));
            }
        });
        //评论文本
        mCommentsCountTv.setText(item.commentscount + "");
        //是否有视频
        if (entity.videoready == 0) {
            hasVideoV.setVisibility(View.INVISIBLE);
        } else {
            hasVideoV.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 活动页面
     *
     * @param context
     * @param item
     */
    private void bindPartyView(final Context context, final GetDynamicListResult item) {
        final GetDynamicListResult.Party entity = JSON.parseObject(item.getInfo(), GetDynamicListResult.Party.class);

        //状态判断
        if (entity.status > AppEnum.groupPartyStatus.PARTY_START) {
            endLl.setVisibility(View.VISIBLE);
            signLl.setVisibility(View.GONE);
            startLl.setVisibility(View.GONE);
        } else if (entity.status == AppEnum.groupPartyStatus.PARTY_START) {
            endLl.setVisibility(View.GONE);
            signLl.setVisibility(View.GONE);
            startLl.setVisibility(View.VISIBLE);
        } else {
            endLl.setVisibility(View.GONE);
            signLl.setVisibility(View.VISIBLE);
            startLl.setVisibility(View.GONE);
            if (entity.personstatus == AppEnum.groupPartyPersonStatus.REGIST) {
                signTv.setText("已报名");
            } else {
                signTv.setText("未报名");
            }
        }

        //跑团图标
        ImageUtils.loadGroupImage(entity.rungroupavatar, mGroupAvatar);
        //跑团名称
        groupNameTv.setText(entity.rungroupname);
        //活动名称
        partyNameTv.setText("-" + entity.name);
        //活动尚未结束
        if (entity.getStatus() < AppEnum.groupPartyStatus.PARTY_STOP) {
            contractTimeTv.setTextColor(Color.parseColor("#f06522"));
        } else {
            contractTimeTv.setTextColor(Color.parseColor("#888888"));
        }
        //活动未开始
        if (entity.status < AppEnum.groupPartyStatus.PARTY_START) {
            contractTimeTv.setText(TimeUtils.getTimestampAfterString(TimeUtils.stringToDate(entity.getStarttime())) +
                    "开始");
            //活动进行中
        } else if (entity.status == AppEnum.groupPartyStatus.PARTY_START) {
            contractTimeTv.setText("活动进行中");
            //活动结束
        } else if (entity.status == AppEnum.groupPartyStatus.PARTY_STOP) {
            contractTimeTv.setText(TimeUtils.getTimestampString(TimeUtils.stringToDate(entity.getEndtime())) + "结束");
        } else if (entity.status == AppEnum.groupPartyStatus.PARTY_CANCEL) {
            contractTimeTv.setText("活动已取消");
        }
        //活动图片
        ImageUtils.loadPartySmallImage(entity.getPicture(), partyIv);
        //活动位置
        locationTv.setText(entity.location);
        //活动时间
        partyTimeTv.setText(TimeUtils.getDynamicDayTime(entity.starttime));
        //活动距离
        double workoutLength = (entity.avglength / 1000f);
        final BigDecimal bg = new BigDecimal(workoutLength);
        double length = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        partyLengthTv.setText(length + "公里");
        //活动人数
        partyPersonCountTv.setText(entity.signupcount + "人");

        mLongOnClickRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventDynamicClick(getAdapterPosition(), item));
            }
        });
//        groupInfoBtn.setOnClickListener();
        mGroupAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("groupid", entity.rungroupid);
                Intent intent = new Intent(context, GroupInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

}
