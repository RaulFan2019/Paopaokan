package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.CommentsListViewHolder;
import com.app.pao.adapter.ViewHolder.DynamicRunnerViewHolder;
import com.app.pao.adapter.ViewHolder.LiveSpliteViewHolder;
import com.app.pao.entity.network.GetCommentsResult;
import com.app.pao.entity.network.GetRunningUserResult;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul on 2016/1/26.
 */
public class DynamicRunnnerRvAdapter extends RecyclerView.Adapter<DynamicRunnerViewHolder> {

    private List<GetRunningUserResult.FriendsEntity> mDatas;
    private BitmapUtils mBitmapU;
    private Context mContext;

    public DynamicRunnnerRvAdapter(Context context, List<GetRunningUserResult.FriendsEntity> mList) {
        this.mDatas = mList;
        mBitmapU = new BitmapUtils(context);
        this.mContext = context;
    }

    @Override
    public DynamicRunnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dynamic_runner_list, parent, false);
        return new DynamicRunnerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DynamicRunnerViewHolder holder, int position) {
        final GetRunningUserResult.FriendsEntity entity = mDatas.get(position);
        holder.bindItem(mContext, entity, mBitmapU);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
