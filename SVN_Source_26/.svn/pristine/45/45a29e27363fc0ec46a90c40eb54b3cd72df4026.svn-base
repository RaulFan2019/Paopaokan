package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.network.GetGroupPartyAllPhotoResult;
import com.app.pao.entity.network.GetGroupPartylistSummarySectionResult;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/14.
 * <p/>
 * 获取活动的总结列表adapter
 */
public class GroupPartyEditSummaryAdapter extends RecyclerView.Adapter<GroupPartyEditSummaryAdapter.PartyEditSummaryHolder> {
    private List<GetGroupPartyAllPhotoResult.PartyPicture> mDataList;
    private BitmapUtils bitmapUtils;
    private Context mContext;
    private actionListener mListener;

    public interface actionListener {
        void DeletePhoto(int index);
    }

    public GroupPartyEditSummaryAdapter(Context mContext, List<GetGroupPartyAllPhotoResult.PartyPicture> mDataList, actionListener listener) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        bitmapUtils = new BitmapUtils(mContext);
        this.mListener = listener;
    }

    @Override
    public PartyEditSummaryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_edit_summary, parent, false);
        PartyEditSummaryHolder holder = new PartyEditSummaryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PartyEditSummaryHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class PartyEditSummaryHolder extends RecyclerView.ViewHolder {
        ImageView mSummaryPhotoIv;
        ImageView mEditPhotoIv;
        ImageView mDeletePhotoIv;

        public PartyEditSummaryHolder(View itemView) {
            super(itemView);
            mSummaryPhotoIv = (ImageView) itemView.findViewById(R.id.iv_summary_photo);
            mEditPhotoIv = (ImageView) itemView.findViewById(R.id.iv_edit_photo);
            mDeletePhotoIv = (ImageView) itemView.findViewById(R.id.iv_delete_photo);
        }

        public void bindItem(final int position) {
            bitmapUtils.display(mSummaryPhotoIv, mDataList.get(position).getUrl());
            mDeletePhotoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.DeletePhoto(position);
                }
            });
        }
    }
}
