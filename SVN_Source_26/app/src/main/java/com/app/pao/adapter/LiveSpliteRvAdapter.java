package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.LiveSpliteViewHolder;
import com.app.pao.entity.model.LiveSplite;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul on 2016/1/15.
 * 回放分段适配器
 */
public class LiveSpliteRvAdapter extends RecyclerView.Adapter<LiveSpliteViewHolder> {

    private List<LiveSplite> mDataList;
    private BitmapUtils mBitmapU;
    private int maxPace;
    private Context mContext;

    public LiveSpliteRvAdapter(Context context, List<LiveSplite> dataList, int maxPace) {
        this.mDataList = dataList;
        mBitmapU = new BitmapUtils(context);
        this.maxPace = maxPace;
        mContext = context;
    }


    @Override
    public LiveSpliteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playback_splite_list, parent, false);
        return new LiveSpliteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LiveSpliteViewHolder holder, int position) {
        final LiveSplite result = mDataList.get(position);
        if (position == mDataList.size() - 1) {
            holder.bindItem(result, maxPace, mContext, true);
        } else {

            holder.bindItem(result, maxPace, mContext, false);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
