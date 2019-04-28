package com.app.pao.adapter.ViewHolder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.app.pao.R;
import com.app.pao.entity.model.LiveSplite;
import com.app.pao.utils.TimeUtils;
import com.rey.material.widget.TextView;

/**
 * Created by Raul on 2016/1/20.
 * 分段ViewHolder
 */
public class LiveSpliteViewHolder extends RecyclerView.ViewHolder {

    private TextView numTv;
    private TextView paceTv;
    private ProgressBar progressBar;

    public LiveSpliteViewHolder(View itemView) {
        super(itemView);
        numTv = (TextView) itemView.findViewById(R.id.tv_num);
        paceTv = (TextView) itemView.findViewById(R.id.tv_pace);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
    }


    public void bindItem(final LiveSplite item, final int maxPace, Context context, boolean isLastPostion) {
        numTv.setText((getAdapterPosition() + 1) + "");
        int pace = (int) (item.getDuration() * 1000 / item.getLength());
        paceTv.setText(TimeUtils.formatSecondsToSpeedTime(pace));
        if (maxPace == 0) {
            progressBar.setProgress(0);
        } else {
            progressBar.setProgress(pace * 100 / maxPace);
        }
        if (isLastPostion) {
            progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable
                    .progress_playback_splite_horizontal_curr));
        } else {
            progressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable
                    .progress_playback_splite_horizontal));
        }
    }
}
