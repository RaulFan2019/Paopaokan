package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.PlaybackSpliteViewHolder;
import com.app.pao.entity.model.PlayBackSpliteEntity;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul on 2016/1/15.
 * 回放分段适配器
 */
public class PlaybackSpliteRvAdapter extends RecyclerView.Adapter<PlaybackSpliteViewHolder> {

    private List<PlayBackSpliteEntity> mDataList;
    private BitmapUtils mBitmapU;
    private int maxPace;//最大配速
    private Context mContext;

    public PlaybackSpliteRvAdapter(Context context, List<PlayBackSpliteEntity> dataList, int maxPace) {
        this.mDataList = dataList;
        mBitmapU = new BitmapUtils(context);
        this.maxPace = maxPace;
        mContext = context;
    }


    @Override
    public PlaybackSpliteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playback_splite_list, parent, false);
        return new PlaybackSpliteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaybackSpliteViewHolder holder, int position) {
        final PlayBackSpliteEntity result = mDataList.get(position);
        holder.bindItem(result, maxPace, mContext);
    }

    @Override
    public int getItemCount() {
        if (mDataList == null) {
            return 0;
        } else {
            return mDataList.size();
        }
    }
}
