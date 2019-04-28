package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.HistorySocialViewHolder;
import com.app.pao.adapter.ViewHolder.LiveSocialViewHolderV3;
import com.app.pao.entity.network.GetSocialListResult;
import com.app.pao.entity.network.LiveSocial;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/8.
 * 跑步历史评论点赞语音列表适配器
 */
public class LiveSocialRvAdapterV3 extends RecyclerView.Adapter<LiveSocialViewHolderV3> {

    private List<LiveSocial> mSocialsEntityList;
    private BitmapUtils mBitmapU;
    private Context mContext;

    public LiveSocialRvAdapterV3(final Context context, final List<LiveSocial> socialsEntityList) {
        mBitmapU = new BitmapUtils(context);
        this.mContext = context;
        this.mSocialsEntityList = socialsEntityList;
    }

    @Override
    public LiveSocialViewHolderV3 onCreateViewHolder(ViewGroup parent, int viewType) {
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
        return new LiveSocialViewHolderV3(view, viewType);
    }

    @Override
    public void onBindViewHolder(LiveSocialViewHolderV3 holder, int position) {
        final LiveSocial result = mSocialsEntityList.get(position);
        holder.bindItem(mContext,result, mBitmapU);
    }

    @Override
    public int getItemCount() {
        return mSocialsEntityList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //点赞类型
        if (mSocialsEntityList.get(position).type == 1) {
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
