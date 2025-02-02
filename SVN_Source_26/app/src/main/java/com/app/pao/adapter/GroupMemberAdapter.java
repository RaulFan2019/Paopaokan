package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
 * 跑团成员列表适配器
 */
public class GroupMemberAdapter extends SimpleBaseAdapter<GetGroupMemberListResult.MemberEntity> {

    private BitmapUtils mBitmapU;

    public GroupMemberAdapter(Context context, List<GetGroupMemberListResult.MemberEntity> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemberViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_group_member_list, null);
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
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //角色
        if (entity.getRole() == AppEnum.groupRole.OWNER) {
            mHolder.mRoleLl.setVisibility(View.VISIBLE);
            mHolder.mRoleLl.setBackgroundResource(R.drawable.bg_round_rect_icon_orange);
            mHolder.mRoleTv.setText("团长");
        } else if (entity.getRole() == AppEnum.groupRole.MANAGER) {
            mHolder.mRoleLl.setVisibility(View.VISIBLE);
            mHolder.mRoleLl.setBackgroundResource(R.drawable.bg_round_rect_icon_yellow);
            mHolder.mRoleTv.setText("管理员");
        } else {
            mHolder.mRoleLl.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    /*跑团成员列表适配界面**/
    class MemberViewHolder {
        CircularImage mPhotoIv;//成员头像
        TextView mNickNameTv;//成员昵称
        ImageView mGanderIv;//性别图标
        LinearLayout mRoleLl;//角色图标
        TextView mRoleTv;//角色文字

        public MemberViewHolder(View v) {
            mPhotoIv = (CircularImage) v.findViewById(R.id.iv_photo);
            mNickNameTv = (TextView) v.findViewById(R.id.tv_nickname);
            mGanderIv = (ImageView) v.findViewById(R.id.iv_gander);
            mRoleLl = (LinearLayout) v.findViewById(R.id.ll_role);
            mRoleTv = (TextView) v.findViewById(R.id.tv_role);
        }
    }
}
