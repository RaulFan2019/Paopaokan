package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.model.RacePersonEntity;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/12/5.
 * 比赛直播选手列表
 */
public class RacePersonAdapter extends SimpleBaseAdapter<RacePersonEntity> {

    private BitmapUtils mBitmapU;

    public RacePersonAdapter(Context context, List<RacePersonEntity> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RacePersonViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_race_person_list, null);
            mHolder = new RacePersonViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (RacePersonViewHolder) convertView.getTag();
        }

        RacePersonEntity racePersonEntity = datas.get(position);

        //昵称
        mHolder.mNickNameTv.setText(racePersonEntity.getUserEntity().getNickname());
        //头像
        ImageUtils.loadUserImage(racePersonEntity.getUserEntity().getAvatar(), mHolder.mCiv);
        //性别
        if (racePersonEntity.getUserEntity().getGender() == AppEnum.UserGander.MAN) {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //距离文本
        mHolder.mLengthTv.setText((int) (racePersonEntity.getUserEntity().getLength() / 1000) + "");
        //时间文本
        mHolder.mTimeTv.setText(TimeUtils.formatVoiceString(racePersonEntity.getUserEntity().getDuration()));
//        if (racePersonEntity.getRaceType() == AppEnum.racePersonType.STAR) {
//            mHolder.mRoleIv.setBackgroundResource(R.drawable.icon_race_start);
//        } else if(racePersonEntity.getRaceType() == AppEnum.racePersonType.FRIEND){
//            mHolder.mRoleIv.setBackgroundResource(R.drawable.icon_race_friend);
//        }else{
//            mHolder.mRoleIv.setBackgroundResource(R.drawable.icon_race_group);
//        }
        return convertView;
    }


    /*好友适配界面**/
    class RacePersonViewHolder {
        TextView mNickNameTv;// 姓名文本
        CircularImage mCiv;// 头像
        ImageView mGanderIv;//性别图像
        TextView mLengthTv;//距离文本
        TextView mTimeTv;//时间文本
        ImageView mRoleIv;//跑步角色图片

        public RacePersonViewHolder(View v) {
            mNickNameTv = (TextView) v.findViewById(R.id.tv_nickname);
            mCiv = (CircularImage) v.findViewById(R.id.iv_photo);
            mGanderIv = (ImageView) v.findViewById(R.id.iv_gander);
            mLengthTv = (TextView) v.findViewById(R.id.tv_length);
            mTimeTv = (TextView) v.findViewById(R.id.tv_time);
            mRoleIv = (ImageView) v.findViewById(R.id.image_race);
        }
    }
}
