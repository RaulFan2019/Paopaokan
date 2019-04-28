package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.adapter.ViewHolder.CommentsListViewHolder;
import com.app.pao.entity.network.GetCommentsResult;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul on 2016/1/15.
 * 评论适配器
 */
public class CommentsRvAdapter extends RecyclerView.Adapter<CommentsListViewHolder> {


    /* contains */
    private static final int VIEW_TYPE_NORMAL = 0x01;//普通评论类型
    private static final int VIEW_TYPE_VOICE = 0x02;//语音评论类型

    private List<GetCommentsResult> mCommentsList;
    private BitmapUtils mBitmapU;
    private Context mContext;

    public CommentsRvAdapter(Context context, List<GetCommentsResult> ThumbList) {
        this.mCommentsList = ThumbList;
        mBitmapU = new BitmapUtils(context);
        this.mContext = context;
    }


    @Override
    public CommentsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == VIEW_TYPE_VOICE){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_media_comments_list, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_comments_list, parent, false);
        }
        return new CommentsListViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(CommentsListViewHolder holder, int position) {
        final GetCommentsResult result = mCommentsList.get(position);
        holder.bindItem(mContext,result, mBitmapU);
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mCommentsList.get(position).getMediatype() == 2) {
            return VIEW_TYPE_VOICE;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }
}
