package com.app.pao.adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.pao.R;
import com.app.pao.entity.network.GetThumbsResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

/**
 * Created by Raul on 2016/1/15.
 * 点赞列表ViewHolder
 */
public class ThumbListViewHolder extends RecyclerView.ViewHolder {

    private TextView mFriendNameTv;// 姓名文本
    private CircularImage mFriendPhotoCiv;// 头像
    private TextView mTimeTv;

    public ThumbListViewHolder(View itemView) {
        super(itemView);
        mFriendNameTv = (TextView) itemView.findViewById(R.id.tv_friends_nickname);
        mFriendPhotoCiv = (CircularImage) itemView.findViewById(R.id.iv_friends_photo);
        mTimeTv = (TextView) itemView.findViewById(R.id.tv_time);
    }

    public void bindItem(final GetThumbsResult item, BitmapUtils mBtimapUtils) {
        mFriendNameTv.setText(item.getNickname() + "");
        ImageUtils.loadUserImage(item.getAvatar(), mFriendPhotoCiv);
        mTimeTv.setText(TimeUtils.getTimestampString(TimeUtils.stringToDate(item.getThumbuptime())));
    }
}
