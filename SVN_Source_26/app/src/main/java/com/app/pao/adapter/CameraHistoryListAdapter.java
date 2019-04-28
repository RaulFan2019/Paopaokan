package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.CameraListViewHolder;
import com.app.pao.entity.network.GetGroupListResult;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/3/16.
 * 视频列表适配器
 */
public class CameraHistoryListAdapter extends RecyclerView.Adapter<CameraListViewHolder> {


    private List<GetPlaybackCameraListResult.VideoEntity> mDataList;
    private Context context;
    private boolean isMine;//是否是自己的视频
    private int workoutid;


    public CameraHistoryListAdapter(Context context,int workoutid,
                                    List<GetPlaybackCameraListResult.VideoEntity> data,
                                    boolean isMine) {
        this.context = context;
        this.mDataList = data;
        this.isMine = isMine;
        this.workoutid = workoutid;
    }

    @Override
    public CameraListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_camera_list, parent, false);
        CameraListViewHolder holder = new CameraListViewHolder(view,isMine);
        return holder;
    }

    @Override
    public void onBindViewHolder(CameraListViewHolder holder, int position) {
        holder.bindItem(context, mDataList.get(position), mDataList,workoutid);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
