package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetClockResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;

import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/16.
 */
public class MyClockListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<GetClockResult.ClocksEntity> datas;
    private BitmapUtils mBitmapU;
    private Context mContext;

    public MyClockListAdapter(Context context, List<GetClockResult.ClocksEntity> datas) {
        this.datas = datas;
        this.mContext = context;
        this.mBitmapU = new BitmapUtils(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_clock_list, parent, false);
        ViewsHolder holder = new ViewsHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewsHolder) holder).bindItem(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewsHolder extends RecyclerView.ViewHolder {
        TextView mFriendNameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像
        View mGanderIv;//性别图像
        TextView mLocTv;//位置文本
        TextView mTimeTv;//时间文本
        View mClockV;//时钟图片

        public ViewsHolder(View itemView) {
            super(itemView);
            mFriendNameTv = (TextView) itemView.findViewById(R.id.tv_name);
            mFriendPhotoCiv = (CircularImage) itemView.findViewById(R.id.iv_avatar);
            mGanderIv = itemView.findViewById(R.id.iv_gander);
            mLocTv = (TextView) itemView.findViewById(R.id.tv_loc);
            mTimeTv = (TextView) itemView.findViewById(R.id.tv_time);
            mClockV = itemView.findViewById(R.id.v_clock);
        }

        public void bindItem(final int position) {
            //用户信息对象
            final GetClockResult.ClocksEntity entity = datas.get(position);
            //用户名
            mFriendNameTv.setText(entity.nickname + "");
            //用户头像
            ImageUtils.loadUserImage(entity.avatar, mFriendPhotoCiv);
            //性别
            if (entity.gender == AppEnum.UserGander.MAN) {
                mGanderIv.setBackgroundResource(R.drawable.icon_man);
            } else {
                mGanderIv.setBackgroundResource(R.drawable.icon_women);
            }
            mLocTv.setText(entity.province + " " + entity.city);
            mTimeTv.setText(TimeUtils.getHistoryMapStartTime(entity.clocktime));
            if (entity.clockindex == 1) {
                mClockV.setBackgroundResource(R.drawable.icon_clock_1);
            } else if (entity.clockindex == 2) {
                mClockV.setBackgroundResource(R.drawable.icon_clock_2);
            } else {
                mClockV.setBackgroundResource(R.drawable.icon_clock_3);
            }

            mFriendPhotoCiv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.userid == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("userid", entity.userid);
                    Intent intent = new Intent(mContext,UserInfoActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent, bundle);
                }
            });
        }
    }
}
