package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.entity.network.GetGroupPartyMyPhotoReqult;
import com.app.pao.utils.DeviceUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public class GroupPartyUserPhotoAdapter extends RecyclerView.Adapter<GroupPartyUserPhotoAdapter.UserPhotoHolder> {

    private List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList;
    private BitmapUtils bitmapUtils;
    private onClickListener mListener;

    public interface onClickListener {
        void showPhoto(int pos);
    }

    public GroupPartyUserPhotoAdapter(Context mContext, List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList,onClickListener listener) {
        this.mDataList = mDataList;
        bitmapUtils = new BitmapUtils(mContext);
        this.mListener = listener;
    }

    @Override
    public UserPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_my_photo, parent, false);
        UserPhotoHolder holder = new UserPhotoHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(UserPhotoHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class UserPhotoHolder extends RecyclerView.ViewHolder {
        ImageView mPhotoIv;
        ImageView mCheckIv;
        FrameLayout mBaseFl;

        public UserPhotoHolder(View itemView) {
            super(itemView);
            mPhotoIv = (ImageView) itemView.findViewById(R.id.iv_photo);
            mCheckIv = (ImageView) itemView.findViewById(R.id.iv_delete);
            mBaseFl = (FrameLayout) itemView.findViewById(R.id.fl_base);
            int border = (int) DeviceUtils.getScreenWidth() / 3;
            itemView.setLayoutParams(new FrameLayout.LayoutParams(border, border));
        }

        public void bindItem(final int position) {
            GetGroupPartyMyPhotoReqult.UserPicture picture = mDataList.get(position);
            mCheckIv.setVisibility(View.GONE);
            bitmapUtils.display(mPhotoIv, picture.getUrl());
            final int pos = position;
            mBaseFl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.showPhoto(pos);
                }
            });
        }
    }
}
