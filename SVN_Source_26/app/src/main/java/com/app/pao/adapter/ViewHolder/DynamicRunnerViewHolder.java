package com.app.pao.adapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.activity.run.LiveActivityV3;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.GetRunningUserResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import java.math.BigDecimal;

/**
 * Created by Raul on 2016/1/26.
 * 动态中跑步人员列表
 */
public class DynamicRunnerViewHolder extends RecyclerView.ViewHolder {

    private CircularImage mPhotoIv;
    private TextView mRunnerTv;
    private TextView mLengthTv;
    private TextView mTimeTv;
    private LinearLayout mBaseLl;
    private View mLineV;

    public DynamicRunnerViewHolder(View itemView) {
        super(itemView);
        mPhotoIv = (CircularImage) itemView.findViewById(R.id.iv_photo);
        mRunnerTv = (TextView) itemView.findViewById(R.id.tv_runnner);
        mLengthTv = (TextView) itemView.findViewById(R.id.tv_party_length);
        mTimeTv = (TextView) itemView.findViewById(R.id.tv_party_time);
        mBaseLl = (LinearLayout) itemView.findViewById(R.id.ll_base);
        mLineV = itemView.findViewById(R.id.line);
    }

    public void bindItem(final Context context, final GetRunningUserResult.FriendsEntity item, BitmapUtils
            mBtimapUtils) {
        //跑步状态
        mRunnerTv.setText(item.getNickname());
        //头像
        ImageUtils.loadUserImage(item.getAvatar(), mPhotoIv);
        //距离
        double workoutLength = (item.getLength() / 1000f);
        final BigDecimal bg = new BigDecimal(workoutLength);
        double length = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        mLengthTv.setText(length + "km");
        //时间
//        mTimeTv.setText(TimeUtils.formatSecondsToShortHourTime(item.getDuration()));
        //配速
        mTimeTv.setText(TimeUtils.formatSecondsToSpeedTime((long) (item.getDuration() * 1000 / item.getLength()), "'", "''"));
        //线
        if (getAdapterPosition() == 0) {
            mLineV.setVisibility(View.GONE);
        } else {
            mLineV.setVisibility(View.VISIBLE);
        }
        mBaseLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaidianData.saveMaidian(context, new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.IntoLivingRoomInDynamic, TimeUtils.NowTime()));
                Intent intent = new Intent(context, LiveActivityV3.class);
                intent.putExtra("userId", item.getUserid());
                intent.putExtra("userNickName", item.getNickname());
                intent.putExtra("userGender", item.getGender());
                intent.putExtra("avatar", item.getAvatar());
                context.startActivity(intent);
            }
        });
    }
}
