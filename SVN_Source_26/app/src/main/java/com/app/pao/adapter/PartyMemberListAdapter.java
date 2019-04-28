package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetPartyMemberResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/12/7.
 * 跑团活动参与人员适配器
 */
public class PartyMemberListAdapter extends SimpleBaseAdapter<GetPartyMemberResult> {

    BitmapUtils bitmapUtils;

    public PartyMemberListAdapter(Context context, List<GetPartyMemberResult> datas) {
        super(context, datas);
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemberViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_party_member_list, null);
            mHolder = new MemberViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (MemberViewHolder) convertView.getTag();
        }
        //用户信息对象
        GetPartyMemberResult entity = datas.get(position + 1);

        mHolder.mNameTv.setText(entity.getNickname() + "");
        ImageUtils.loadUserImage( entity.getAvatar(), mHolder.mPhotoCiv);
        //性别
        if (entity.getGender() == AppEnum.UserGander.MAN) {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //是否已签到
        if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.LOGIN) {
            mHolder.mHasLoginTv.setText("已签到");
            mHolder.mHasLoginTv.setVisibility(View.VISIBLE);
        } else {
            mHolder.mHasLoginTv.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return datas != null ? (datas.size() - 1) : 0;
    }

    @Override
    public GetPartyMemberResult getItem(int position) {
        return datas != null ? datas.get(position + 1) : null;
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    /*好友适配界面**/
    class MemberViewHolder {
        TextView mNameTv;// 姓名文本
        CircularImage mPhotoCiv;// 头像
        ImageView mGanderIv;//性别图像
        TextView mHasLoginTv;//已签到状态文本

        public MemberViewHolder(View v) {
            mNameTv = (TextView) v.findViewById(R.id.tv_friends_nickname);
            mPhotoCiv = (CircularImage) v.findViewById(R.id.iv_friends_photo);
            mGanderIv = (ImageView) v.findViewById(R.id.iv_gander);
            mHasLoginTv = (TextView) v.findViewById(R.id.tv_has_login);
        }
    }


}
