package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupListResult;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2015/12/24.
 */
public class UserGroupListAdapter extends SimpleBaseAdapter<GetGroupListResult> {

    public UserGroupListAdapter(Context context, List<GetGroupListResult> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserGroupViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_group_list, null);
            mHolder = new UserGroupViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (UserGroupViewHolder) convertView.getTag();
        }
        //用户信息对象
        GetGroupListResult entity = datas.get(position);

        //团名称
        mHolder.mNameTv.setText(entity.getName());
        //头像
        ImageUtils.loadGroupImage(entity.getAvatar(), mHolder.mAvatarIv);

        //团长身份,状态
        if (entity.getRole() == AppEnum.groupRole.OWNER) {
            mHolder.mIdentityLl.setVisibility(View.VISIBLE);
            mHolder.mIdentityLl.setBackgroundResource(R.drawable.bg_round_rect_icon_orange);
            mHolder.mIdentityTv.setText("团长");
//            if (mGroupMsgCount == 0) {
//                mHolder.badgeView.hide();
//            } else {
//                mHolder.badgeView.setText(mGroupMsgCount + "");
//                mHolder.badgeView.show();
//            }
//            if (entity.getStatus() == AppEnum.groupStatus.WAITING) {
//                mHolder.mStatusTv.setVisibility(View.VISIBLE);
//                mHolder.mStatusTv.setText("审核中");
//            } else
            if (entity.getStatus() == AppEnum.groupStatus.DISMISS) {
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setText("已申请解散");
            } else {
                mHolder.mStatusTv.setVisibility(View.INVISIBLE);
            }
            //管理者
        } else if (entity.getRole() == AppEnum.groupRole.MANAGER) {
        //    mHolder.badgeView.hide();
            mHolder.mIdentityLl.setVisibility(View.VISIBLE);
            mHolder.mIdentityLl.setBackgroundResource(R.drawable.bg_round_rect_icon_yellow);
            mHolder.mIdentityTv.setText("管理员");
            mHolder.mStatusTv.setVisibility(View.INVISIBLE);
            //其他情况
        } else {
      //      mHolder.badgeView.hide();
            mHolder.mIdentityLl.setVisibility(View.INVISIBLE);
            //若是成员
//            if (entity.getRole() == AppEnum.groupRole.MEMBER) {
//                mHolder.mStatusTv.setVisibility(View.INVISIBLE);
//            } else {
//                mHolder.mStatusTv.setVisibility(View.VISIBLE);
//                if (entity.getStatus() == AppEnum.groupStatus.WAITING) {
//                    mHolder.mStatusTv.setText("申请中");
//                } else {
                    mHolder.mStatusTv.setVisibility(View.INVISIBLE);
//                }
//            }
        }

        //位置
        mHolder.mLocationTv.setText(entity.getLocationcity() + " " + entity.getLocationprovince());
        //成员数量
        mHolder.mMemberCountTv.setText(entity.getMembercount() + "人");
        //总里程
        mHolder.mLengthTv.setText(NumUtils.retainTheDecimal(entity.getTotallength()) + "公里");

        return convertView;
    }

    /**
     * 好友跑团 ViewHolder
     */
    class UserGroupViewHolder{
        ImageView mAvatarIv;//团标
        TextView mNameTv;//团名称
        LinearLayout mIdentityLl;//身份图标布局
        TextView mIdentityTv;//身份图标文字
        TextView mMemberCountTv;//成员数量
        TextView mLengthTv;//总里程
        TextView mStatusTv;//状态文本
        TextView mLocationTv;//位置文本
        ImageView mArrowIv;
       // BadgeView badgeView;

        public UserGroupViewHolder(View v) {
            mAvatarIv = (ImageView) v.findViewById(R.id.iv_group_avatar);
            mNameTv = (TextView) v.findViewById(R.id.tv_group_name);
            mIdentityLl = (LinearLayout) v.findViewById(R.id.ll_identity);
            mIdentityTv = (TextView) v.findViewById(R.id.tv_identity);
            mMemberCountTv = (TextView) v.findViewById(R.id.tv_member_count);
            mLengthTv = (TextView) v.findViewById(R.id.tv_group_length);
            mStatusTv = (TextView) v.findViewById(R.id.tv_status);
            mLocationTv = (TextView) v.findViewById(R.id.tv_location);
            mArrowIv = (ImageView) v.findViewById(R.id.iv_arrow);
            mArrowIv.setVisibility(View.INVISIBLE);

//            badgeView = new BadgeView(mContext, mAvatarIv);
//            badgeView.setTextSize(9);
//            badgeView.setBackgroundColor(Color.parseColor("#f06522"));
//            badgeView.setTextColor(Color.WHITE);
//            badgeView.setBadgeMargin(0);
        }
    }
}
