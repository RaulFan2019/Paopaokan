package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.ThumbListViewHolder;
import com.app.pao.entity.network.GetThumbsResult;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul on 2016/1/15.
 * 点赞适配器
 */
public class ThumbRvAdapter extends RecyclerView.Adapter<ThumbListViewHolder> {

    private List<GetThumbsResult> mThumbList;
    private BitmapUtils mBitmapU;

    public ThumbRvAdapter(Context context, List<GetThumbsResult> ThumbList) {
        this.mThumbList = ThumbList;
        mBitmapU = new BitmapUtils(context);
    }


    @Override
    public ThumbListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_thumb_up_list, parent, false);
        return new ThumbListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ThumbListViewHolder holder, int position) {
        final GetThumbsResult result = mThumbList.get(mThumbList.size() - 1 - position);
        holder.bindItem(result, mBitmapU);
    }

    @Override
    public int getItemCount() {
        return mThumbList.size();
    }
}
