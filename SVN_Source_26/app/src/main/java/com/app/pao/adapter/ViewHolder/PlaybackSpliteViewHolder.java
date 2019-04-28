package com.app.pao.adapter.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.app.pao.R;
import com.app.pao.entity.model.PlayBackSpliteEntity;
import com.app.pao.utils.TimeUtils;
import com.rey.material.widget.TextView;

/**
 * Created by Raul on 2016/1/20.
 * 分段ViewHolder
 */
public class PlaybackSpliteViewHolder extends RecyclerView.ViewHolder {

    private TextView numTv;
    private TextView paceTv;
    private ProgressBar progressBar;

    public PlaybackSpliteViewHolder(View itemView) {
        super(itemView);
        numTv = (TextView) itemView.findViewById(R.id.tv_num);
        paceTv = (TextView) itemView.findViewById(R.id.tv_pace);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
    }


    public void bindItem(final PlayBackSpliteEntity item, final int maxPace, Context context) {
        numTv.setText((getAdapterPosition()+1) + "");
        paceTv.setText(TimeUtils.formatSecondsToSpeedTime(item.getPace()));
        progressBar.setProgress(item.getPace() * 100 / maxPace);
        if (item.isCurrPace()) {
            progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable
                    .progress_playback_splite_horizontal_curr));
        } else {
            progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable
                    .progress_playback_splite_horizontal));
        }
    }
}
