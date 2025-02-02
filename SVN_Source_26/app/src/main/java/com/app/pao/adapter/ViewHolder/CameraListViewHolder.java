package com.app.pao.adapter.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.activity.workout.HistoryVideoPlayActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.event.EventShareHistoryVideo;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/3/16.
 * 视频列表
 */
public class CameraListViewHolder extends RecyclerView.ViewHolder {

    private TextView nameTv;
    private TextView tipTv;
    private View shareView;
    private LinearLayout mBaseLl;


    private boolean isMine;

    public CameraListViewHolder(View itemView, boolean isMine) {
        super(itemView);
        nameTv = (TextView) itemView.findViewById(R.id.tv_camera_name);
        tipTv = (TextView) itemView.findViewById(R.id.tv_camera_tip);
        shareView = itemView.findViewById(R.id.iv_share);
        mBaseLl = (LinearLayout) itemView.findViewById(R.id.ll_base);
        this.isMine = isMine;
    }

    public void bindItem(final Context context, final GetPlaybackCameraListResult.VideoEntity item,
                        final List<GetPlaybackCameraListResult.VideoEntity> mDataList,final int workoutid) {
        nameTv.setText("镜头#" + (getAdapterPosition()+1));
        String length = NumUtils.formatLength(item.getPositionmeters() / 1000f) + "km  ";
        String time = TimeUtils.formatDurationStr(TimeUtils.getTimesetFromStartTime(item.getStarttime(), item.getEndtime()));
        tipTv.setText(length + time);
        if(isMine){
            shareView.setVisibility(View.VISIBLE);
        }else {
            shareView.setVisibility(View.GONE);
        }
        mBaseLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HistoryVideoPlayActivity.class);
                Bundle b = new Bundle();
                b.putInt("pos", getAdapterPosition());
                b.putSerializable("list", (Serializable) mDataList);
                b.putInt("workoutid",workoutid);
                i.putExtras(b);
                context.startActivity(i);
            }
        });
        shareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaidianData.saveMaidian(context, new DBEntityMaidian(System.currentTimeMillis() + "",
                        AppEnum.MaidianType.SharePersonalVideo, TimeUtils.NowTime()));
                EventBus.getDefault().post(new EventShareHistoryVideo(item.getId()));
            }
        });
    }
}
