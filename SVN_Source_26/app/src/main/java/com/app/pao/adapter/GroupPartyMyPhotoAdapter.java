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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/12.
 * 我的照片adapter
 */
public class GroupPartyMyPhotoAdapter extends RecyclerView.Adapter<GroupPartyMyPhotoAdapter.MyPhotoHolder> {
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_UNCHECKED = 1;
    public static final int TYPE_CHECKED = 2;

    private List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList;
    private BitmapUtils bitmapUtils;
    private onItemDeleteListener mOnItemDeleteListener;

    public interface onItemDeleteListener {
        void deletePhoto(int pos);
        void showPhoto(int pos);
    }


    private List<GetGroupPartyMyPhotoReqult.UserPicture> mCheckedPhoto
            = new ArrayList<GetGroupPartyMyPhotoReqult.UserPicture>();

    public GroupPartyMyPhotoAdapter(Context mContext, List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList,
                                    onItemDeleteListener OnItemDeleteListener) {
        this.mDataList = mDataList;
        bitmapUtils = new BitmapUtils(mContext);
        this.mOnItemDeleteListener = OnItemDeleteListener;
    }

    @Override
    public MyPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_my_photo, parent, false);
        MyPhotoHolder holder = new MyPhotoHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyPhotoHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 获取选择的对象
     *
     * @return
     */
    public List<GetGroupPartyMyPhotoReqult.UserPicture> getmCheckedPhoto() {
        return mCheckedPhoto;
    }

    public class MyPhotoHolder extends RecyclerView.ViewHolder {
        ImageView mPhotoIv;
        ImageView mCheckIv;
        FrameLayout mBaseFl;

        public MyPhotoHolder(View itemView) {
            super(itemView);
            mPhotoIv = (ImageView) itemView.findViewById(R.id.iv_photo);
            mCheckIv = (ImageView) itemView.findViewById(R.id.iv_delete);
            mBaseFl = (FrameLayout) itemView.findViewById(R.id.fl_base);
            int border = (int) DeviceUtils.getScreenWidth() / 3;
            itemView.setLayoutParams(new FrameLayout.LayoutParams(border, border));
        }

        public void bindItem(int position) {
            GetGroupPartyMyPhotoReqult.UserPicture picture = mDataList.get(position);
            bitmapUtils.display(mPhotoIv, picture.getUrl());
            final int pos = position;
            mCheckIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemDeleteListener.deletePhoto(pos);
                }
            });
            mBaseFl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemDeleteListener.showPhoto(pos);
                }
            });
        }

    }

}
