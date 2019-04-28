package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.LiveSocialViewHolder;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.LiveSocial;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul on 2016/1/25.
 * 直播评论点赞适配器
 */
public class LiveSocialRvAdapter extends RecyclerView.Adapter<LiveSocialViewHolder> {

    /* contains */
    private static final int VIEW_TYPE_NORMAL = 0x01;//普通评论类型
    private static final int VIEW_TYPE_VOICE = 0x02;//语音评论类型

    private List<LiveSocial> mDataList;
    private BitmapUtils mBitmapU;


    public LiveSocialRvAdapter(List<LiveSocial> DataList, Context context) {
        this.mDataList = DataList;
        mBitmapU = new BitmapUtils(context);

    }

    @Override
    public LiveSocialViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == VIEW_TYPE_VOICE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_live_social_voice_list, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_live_social_list, parent, false);
        }
        return new LiveSocialViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(LiveSocialViewHolder holder, int position) {
        final LiveSocial social = mDataList.get(position);
        holder.bindItem(social, mBitmapU);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataList.get(position).getMediatype() == 2) {
            return VIEW_TYPE_VOICE;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
}
