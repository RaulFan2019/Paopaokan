package com.app.pao.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.network.GetPlaybackCameraListResult;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/9.
 */
public class HistoryVideoAdapter extends RecyclerView.Adapter<HistoryVideoAdapter.VideoItemViewHolder> {

    private static final String TAG = "HistoryVideoAdapter";

    private static final int VIEW_TYPE_BANK = 1;
    private static final int VIEW_TYPE_NOT_BANK = 2;

    public int ITEM_NUM = 5;

    private Context mContext;
    private BitmapUtils mBitmapU;
    public int mHighlight = 2;
    private Typeface typeFace;//字体

    private List<GetPlaybackCameraListResult.VideoEntity> mVideoList = new ArrayList<>();

    private MyItemClickListener mItemClickListener;

    public interface MyItemClickListener {
        public void onItemClick(int postion);
    }

    public HistoryVideoAdapter(Context context, List<GetPlaybackCameraListResult.VideoEntity> VideoList) {
        this.mContext = context;
        this.mVideoList = VideoList;
        mBitmapU = new BitmapUtils(context);
        typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
    }

    @Override
    public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_NOT_BANK) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_history_video, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_history_video_bank, parent, false);
        }
        //设置宽度
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = (int) getItemWidth();
        return new VideoItemViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(VideoItemViewHolder holder, final int position) {

//        Log.v(TAG,"position: " + position + ",holder.ViewType :" + holder.ViewType);
        if (holder.ViewType == VIEW_TYPE_NOT_BANK) {
            GetPlaybackCameraListResult.VideoEntity videoEntity = mVideoList.get(position - 2);

            holder.mVideoNumTv.setText((position - 1) + "");
            holder.mLengthTv.setText(NumUtils.retainTheDecimal(videoEntity.positionmeters) + "公里");
            ImageUtils.loadVideoVImage(mBitmapU, videoEntity.verticalcover, holder.mVideoIv);

            holder.mVideoIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(position);
                }
            });

//            if (isSelect(position)) {
//                holder.mLengthTv.setTypeface(Typeface.DEFAULT_BOLD);
//            } else {
//                holder.mLengthTv.setTypeface(Typeface.DEFAULT);
//            }
        } else {

        }
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mVideoList.size() + 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == 1
                || position == mVideoList.size() + 2
                || position == mVideoList.size() + 3) {
            return VIEW_TYPE_BANK;
        } else {
            return VIEW_TYPE_NOT_BANK;
        }
    }

    /**
     * ViewHolder类
     */
    public class VideoItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView mVideoIv;
        private TextView mVideoNumTv;
        private TextView mLengthTv;
        private int ViewType;

        public VideoItemViewHolder(View itemView, int viewType) {
            super(itemView);
            this.ViewType = viewType;
            if (viewType == VIEW_TYPE_NOT_BANK) {
                mVideoIv = (ImageView) itemView.findViewById(R.id.iv_video);
                mVideoNumTv = (TextView) itemView.findViewById(R.id.tv_video_num);
                mLengthTv = (TextView) itemView.findViewById(R.id.tv_video_length);
                mVideoNumTv.setTypeface(typeFace);
                mLengthTv.setTypeface(typeFace);
            } else {

            }
        }

    }

    /**
     * 获取每一个条目的宽度
     *
     * @return
     */
    public float getItemWidth() {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels / ITEM_NUM;
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
        mHighlight = position;
//        int offset = ITEM_NUM / 2;
//        for (int i = position - offset; i <= position + offset; ++i)
//            notifyItemChanged(i);
    }
}
