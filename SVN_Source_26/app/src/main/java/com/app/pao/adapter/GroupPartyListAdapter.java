package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetGroupPartyListResult;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/12/7.
 * 跑团活动列表适配器
 */
public class GroupPartyListAdapter extends SimpleBaseAdapter<GetGroupPartyListResult> {

    public GroupPartyListAdapter(Context context, List<GetGroupPartyListResult> datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PartyViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_group_party_list, null);
            mHolder = new PartyViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (PartyViewHolder) convertView.getTag();
        }
        GetGroupPartyListResult entity = datas.get(position);

        //名称
        mHolder.mNameTv.setText(entity.getName());
        //地点
        mHolder.mLocationTv.setText(entity.getLocation());
        //时间
        mHolder.mTimeTv.setText(entity.getStarttime());
        //状态
        if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.LOGIN) {
            mHolder.mStatusTv.setText("已签到");
        } else if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_REGIST) {
            mHolder.mStatusTv.setText("未报名");
        } else if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_OWNER) {
            mHolder.mStatusTv.setText("组织人");
        } else if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.REGIST) {
            mHolder.mStatusTv.setText("已报名");
        } else if (entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.NOT_MEMBER) {
            mHolder.mStatusTv.setText("");
        }else if(entity.getPersonstatus() == AppEnum.groupPartyPersonStatus.PARTY_LEADER){
            mHolder.mStatusTv.setText("负责人");
        }

        return convertView;
    }


    /*活动列表适配界面**/
    class PartyViewHolder {
        TextView mNameTv;//活动名称
        TextView mStatusTv;//成员状态文本
        TextView mLocationTv;//位置文本
        TextView mTimeTv;//时间文本

        public PartyViewHolder(View v) {
            mNameTv = (TextView) v.findViewById(R.id.tv_party_name);
            mStatusTv = (TextView) v.findViewById(R.id.tv_party_person_status);
            mLocationTv = (TextView) v.findViewById(R.id.tv_party_location);
            mTimeTv = (TextView) v.findViewById(R.id.tv_party_time);
        }
    }
}
