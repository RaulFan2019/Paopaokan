package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupMemberListResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/12/5.
 * 跑团管理员列表适配器
 */
public class GroupManagerListAdapter extends SimpleBaseAdapter<GetGroupMemberListResult.MemberEntity> {

    private BitmapUtils mBitmapU;

    public GroupManagerListAdapter(Context context, List<GetGroupMemberListResult.MemberEntity> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemberViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_group_manager_list, null);
            mHolder = new MemberViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (MemberViewHolder) convertView.getTag();
        }
        GetGroupMemberListResult.MemberEntity entity = datas.get(position);

        //昵称
        mHolder.mNickNameTv.setText(entity.getNickname());
        //头像
        ImageUtils.loadUserImage(entity.getAvatar(), mHolder.mPhotoIv);
        //性别
        if (entity.getGender() == AppEnum.UserGander.MAN) {
            mHolder.mGenderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGenderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //角色
        if (entity.getRole() == AppEnum.groupRole.MANAGER) {
            mHolder.mRoleIv.setBackgroundResource(R.drawable.icon_manager_y);
        } else {
            mHolder.mRoleIv.setBackgroundResource(R.drawable.icon_manager_n);
        }

        return convertView;
    }

    /*跑团成员列表适配界面**/
    class MemberViewHolder {
        CircularImage mPhotoIv;//成员头像
        TextView mNickNameTv;//成员昵称
        ImageView mGenderIv;//性别图标
        ImageView mRoleIv;//角色图标

        public MemberViewHolder(View v) {
            mPhotoIv = (CircularImage) v.findViewById(R.id.iv_photo);
            mNickNameTv = (TextView) v.findViewById(R.id.tv_nickname);
            mGenderIv = (ImageView) v.findViewById(R.id.iv_gender);
            mRoleIv = (ImageView) v.findViewById(R.id.iv_role);
        }
    }
}
