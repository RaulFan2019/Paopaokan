package com.app.pao.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupMemberSortResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 */
public class GroupMemberSortListAdapter extends SimpleBaseAdapter<GetGroupMemberSortResult> {


    private BitmapUtils mBitmapU;
    private int userId;

    public GroupMemberSortListAdapter(Context context, List<GetGroupMemberSortResult> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
        userId = LocalApplication.getInstance().getLoginUser(context).getUserId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SortViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_group_member_sort_list, null);
            mHolder = new SortViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (SortViewHolder) convertView.getTag();
        }
        //用户信息对象
        final GetGroupMemberSortResult entity = datas.get(position);

        //如果是自己
        if (entity.getId() == userId) {
            convertView.setBackgroundColor(Color.parseColor("#f8f8f8"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#ffffff"));
        }

        //姓名
        mHolder.mNameTv.setText(entity.getNickname() + "");
        ImageUtils.loadUserImage(entity.getAvatar(), mHolder.mPhotoCiv);
        //性别
        if (entity.getGender() == AppEnum.UserGander.MAN) {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //总里程
        mHolder.mLengthTv.setText(NumUtils.retainTheDecimal((float)entity.getLength()) + "公里");
        //位置
        mHolder.mLocationTv.setText(entity.getLocationprovince() + " " + entity.getLocationcity());
        mHolder.mSortNumTv.setText((position+1) + "");
//        mHolder.mSortItemLl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (entity.getId() == userId) {
//                    return;
//                }
//                Bundle bundle = new Bundle();
//                bundle.putInt("userid", entity.getId());
//                Intent intent = new Intent(context, UserInfoFriendActivity.class);
//                intent.putExtras(bundle);
//                context.startActivity(intent);
//            }
//        });
        return convertView;
    }

    /*好友适配界面**/
    class SortViewHolder {
        TextView mNameTv;// 姓名文本
        CircularImage mPhotoCiv;// 头像
        ImageView mGanderIv;//性别图像
        TextView mLengthTv;//总里程文本
        TextView mLocationTv;//位置文本
        TextView mSortNumTv;//排行数字文本
        LinearLayout mSortItemLl;

        public SortViewHolder(View v) {
            mNameTv = (TextView) v.findViewById(R.id.tv_nickname);
            mPhotoCiv = (CircularImage) v.findViewById(R.id.iv_photo);
            mGanderIv = (ImageView) v.findViewById(R.id.iv_gander);
            mLengthTv = (TextView) v.findViewById(R.id.tv_length);
            mLocationTv = (TextView) v.findViewById(R.id.tv_location);
            mSortNumTv = (TextView) v.findViewById(R.id.tv_sort_num);
    //        mSortItemLl = (LinearLayout) v.findViewById(R.id.ll_member_sort);
        }
    }

}
