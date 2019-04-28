package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.entity.network.GetPartyPhotoWallListResult;
import com.app.pao.utils.DeviceUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/11.
 * 照片墙 recycleView adapter
 */
public class GroupPartyPhotoWallAdapter extends RecyclerView.Adapter<GroupPartyPhotoWallAdapter.PhotoWallHolder> {
    private Context mContext;
    private List<GetPartyPhotoWallListResult.UserPicture> mDataList;
    private BitmapUtils bitmapUtils;
    private OnItemOnClickListener onItemOnClickListener;

    public GroupPartyPhotoWallAdapter(Context mContext, List<GetPartyPhotoWallListResult.UserPicture> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public PhotoWallHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_photo_wall, parent, false);
        PhotoWallHolder holder = new PhotoWallHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(PhotoWallHolder holder, int position) {
        holder.bindItem(position);
    }

    public void setOnItemOnClickListener(OnItemOnClickListener onItemOnClickListener) {
        this.onItemOnClickListener = onItemOnClickListener;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class PhotoWallHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        FrameLayout baseFl;
        TextView mMemberNickNameTv;
        ImageView mMemberPictureIv;
        TextView mMemberPictureNumTv;
        ImageView mEditIv;

        public PhotoWallHolder(View itemView) {
            super(itemView);
            mMemberNickNameTv = (TextView) itemView.findViewById(R.id.tv_member_nickname);
            mMemberPictureIv = (ImageView) itemView.findViewById(R.id.iv_member_picture);
            mMemberPictureNumTv = (TextView) itemView.findViewById(R.id.tv_prcture_num);
            baseFl = (FrameLayout) itemView.findViewById(R.id.fl_base);
            mEditIv = (ImageView) itemView.findViewById(R.id.iv_edit);
            itemView.setOnClickListener(this);
        }

        public void bindItem(int position) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) baseFl.getLayoutParams();
            layoutParams.width = (int) (DeviceUtils.getScreenWidth() / 3);
            layoutParams.height = layoutParams.width;
            baseFl.setLayoutParams(layoutParams);

            GetPartyPhotoWallListResult.UserPicture photoWall = mDataList.get(position);

            if (photoWall.getUserid() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                mMemberNickNameTv.setText("我的照片");
                mEditIv.setVisibility(View.VISIBLE);
            } else {
                mMemberNickNameTv.setText(photoWall.getNickname());
                mEditIv.setVisibility(View.INVISIBLE);
            }

            mMemberPictureNumTv.setText(photoWall.getPicture().size() + "张");
            if (photoWall.getPicture().size() > 0) {
                bitmapUtils.display(mMemberPictureIv, photoWall.getPicture().get(0).getUrl());
            }
        }

        @Override
        public void onClick(View v) {
            if (onItemOnClickListener != null) {
                onItemOnClickListener.onItemOnClick(getAdapterPosition());
            }

        }
    }

    public interface OnItemOnClickListener {
        public void onItemOnClick(int position);
    }
}
