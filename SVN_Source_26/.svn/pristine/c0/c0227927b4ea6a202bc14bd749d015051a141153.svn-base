package com.app.pao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/10.
 */
public class HistoryVideoListAdapter extends RecyclerView.Adapter<HistoryVideoListAdapter.VideoListViewHolder> {

    private Context mContext;
    private BitmapUtils mBitmapU;
    private List<GetPlaybackCameraListResult.VideoEntity> mVideoList;
    private Typeface typeFace;//字体
    public int mHighlight = -1;

    private MyItemClickListener mItemClickListener;

    public interface MyItemClickListener {
        public void onItemClick(int postion);
    }


    public HistoryVideoListAdapter(Context Context, List<GetPlaybackCameraListResult.VideoEntity> VideoList) {
        this.mContext = Context;
        this.mVideoList = VideoList;
        mBitmapU = new BitmapUtils(mContext);
        typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
    }

    @Override
    public VideoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_history_video_list, parent, false);
        VideoListViewHolder holder = new VideoListViewHolder(v, typeFace);
        return holder;
    }

    @Override
    public void onBindViewHolder(VideoListViewHolder holder, int position) {
        GetPlaybackCameraListResult.VideoEntity entity = mVideoList.get(position);
        holder.bindItem(entity, mBitmapU, isSelect(position));
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    /**
     * 判断某个条目是否是选中状态（居中状态）
     *
     * @param position
     * @return
     */
    private boolean isSelect(int position) {
        return mHighlight == position;
    }

    /**
     * 高亮中心, 更新前后位置
     *
     * @param position 在list中的位置
     */
    public void highlightItem(int position) {
        int tepPos = mHighlight;
        mHighlight = position;
        notifyItemChanged(tepPos);
        notifyItemChanged(position);
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public class VideoListViewHolder extends RecyclerView.ViewHolder {

        private ImageView mVideoIv;//视频图片
        private TextView mNameTv;//视频名称文本
        private TextView mLengthTv;//距离文本
        private TextView mDurationTv;//用时文本
        private TextView mPaceTv;//配速文本
        private LinearLayout mBasell;//背景布局


        public VideoListViewHolder(View itemView, Typeface typeFace) {
            super(itemView);
            mVideoIv = (ImageView) itemView.findViewById(R.id.iv_video);
            mNameTv = (TextView) itemView.findViewById(R.id.tv_video_name);
            mLengthTv = (TextView) itemView.findViewById(R.id.tv_length);
            mDurationTv = (TextView) itemView.findViewById(R.id.tv_duration);
            mPaceTv = (TextView) itemView.findViewById(R.id.tv_pace);
            mBasell = (LinearLayout) itemView.findViewById(R.id.ll_base);

            mLengthTv.setTypeface(typeFace);
            mDurationTv.setTypeface(typeFace);
            mPaceTv.setTypeface(typeFace);
        }

        public void bindItem(final GetPlaybackCameraListResult.VideoEntity item,
                             BitmapUtils mBitmapU, boolean isSelect) {
            ImageUtils.loadVideoHImage(mBitmapU, item.horizontalcover, mVideoIv);
            mNameTv.setText("视频" + (getAdapterPosition() + 1));
            mLengthTv.setText(NumUtils.retainTheDecimal(item.positionmeters));
            mDurationTv.setText(TimeUtils.getTimeWithFh(item.startoffset));
            mPaceTv.setText(TimeUtils.formatSecondsToSpeedTime(item.pace, "'"));

            if (isSelect) {
                mBasell.setBackgroundColor(Color.parseColor("#fffaf8"));
                mVideoIv.setBackgroundColor(Color.parseColor("#f06522"));
                mNameTv.setTextColor(Color.parseColor("#f06522"));
            } else {
                mVideoIv.setBackgroundColor(Color.parseColor("#00f06522"));
                mNameTv.setTextColor(Color.parseColor("#ffffff"));
                mBasell.setBackgroundColor(Color.parseColor("#ffffff"));
            }

            mBasell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
