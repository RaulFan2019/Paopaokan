package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.HistorySocialViewHolder;
import com.app.pao.entity.network.GetSocialListResult;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/8.
 * 跑步历史评论点赞语音列表适配器
 */
public class HistorySocialRvAdapter extends RecyclerView.Adapter<HistorySocialViewHolder> {

    private List<GetSocialListResult.SocialsEntity> mSocialsEntityList;
    private BitmapUtils mBitmapU;
    private Context mContext;

    public HistorySocialRvAdapter(final Context context, final List<GetSocialListResult.SocialsEntity> socialsEntityList) {
        mBitmapU = new BitmapUtils(context);
        this.mContext = context;
        this.mSocialsEntityList = socialsEntityList;
    }

    @Override
    public HistorySocialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        //点赞类型
        if (viewType == HistorySocialViewHolder.VIEW_TYPE_THUMB) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history_social_thumb, parent, false);
        } else if (viewType == HistorySocialViewHolder.VIEW_TYPE_NORMAL) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history_social_comment, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_history_social_voice, parent, false);
        }
        return new HistorySocialViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(HistorySocialViewHolder holder, int position) {
        final GetSocialListResult.SocialsEntity result = mSocialsEntityList.get(position);
        holder.bindItem(mContext,result, mBitmapU);
    }

    @Override
    public int getItemCount() {
        return mSocialsEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //点赞类型
        if (mSocialsEntityList.get(position).socialtype == 1) {
            return HistorySocialViewHolder.VIEW_TYPE_THUMB;
        }
        //语音类型
        if (mSocialsEntityList.get(position).mediatype == 2) {
            return HistorySocialViewHolder.VIEW_TYPE_VOICE;
            //普通评论类型
        } else {
            return HistorySocialViewHolder.VIEW_TYPE_NORMAL;
        }
    }

}
