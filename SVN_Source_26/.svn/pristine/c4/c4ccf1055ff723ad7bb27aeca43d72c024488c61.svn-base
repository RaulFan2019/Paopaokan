package com.app.pao.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.entity.network.GetGroupPartyAllPhotoResult;
import com.app.pao.utils.DeviceUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/1/17.
 */
public class GroupPartyAllPhotoAdapter extends RecyclerView.Adapter<GroupPartyAllPhotoAdapter.GroupAllPhotoHolder> {
    private BitmapUtils bitmapUtils;
    private List<GetGroupPartyAllPhotoResult.PartyPicture> mDataList;
 //   private Context mContext;
    private Activity activity;

    public GroupPartyAllPhotoAdapter(Activity activity, List<GetGroupPartyAllPhotoResult.PartyPicture> mDataList) {
        bitmapUtils = new BitmapUtils(activity);
        this.mDataList = mDataList;
        this.activity = activity;
    }

    @Override
    public GroupAllPhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_all_photo, parent, false);
        GroupAllPhotoHolder holder = new GroupAllPhotoHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(GroupAllPhotoHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class GroupAllPhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView photoIv;

        public GroupAllPhotoHolder(View itemView) {
            super(itemView);
            photoIv = (ImageView) itemView.findViewById(R.id.iv_photo);

            itemView.setOnClickListener(this);
            int border = (int) DeviceUtils.getScreenWidth() / 3;
            itemView.setLayoutParams(new FrameLayout.LayoutParams(border, border));
        }

        public void bindItem(int position) {
            bitmapUtils.display(photoIv, mDataList.get(position).getUrl());
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            Bundle b = new Bundle();
            b.putSerializable("photo",mDataList.get(getAdapterPosition()));
            i.putExtras(b);
            activity.setResult(Activity.RESULT_OK, i);
            activity.finish();
        }
    }
}
