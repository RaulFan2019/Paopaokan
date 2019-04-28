package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.db.DBEntityMessage;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 消息列表适配器
 */
public class MessageListAdapter extends SimpleBaseAdapter<DBEntityMessage> {


    private BitmapUtils mBitmapU;

    SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public MessageListAdapter(Context context, List<DBEntityMessage> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_message_list, null);
            mHolder = new FriendViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (FriendViewHolder) convertView.getTag();
        }
        //用户信息对象
        DBEntityMessage entity = datas.get(position);
        mHolder.mInfoTv.setText(entity.getMessage());
        if (entity.getStatus() == AppEnum.messageRead.NEW) {
            mHolder.mIsReadIv.setVisibility(View.VISIBLE);
        } else {
            mHolder.mIsReadIv.setVisibility(View.INVISIBLE);
        }
        try {
            Date date = myFormatter.parse(entity.getSendtime());
            mHolder.mTimeTv.setText(TimeUtils.getTimestampString(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    /*好友适配界面**/
    class FriendViewHolder {
        ImageView mIsReadIv;
        TextView mInfoTv;
        TextView mTimeTv;

        public FriendViewHolder(View v) {
            mIsReadIv = (ImageView) v.findViewById(R.id.iv_is_read);
            mInfoTv = (TextView) v.findViewById(R.id.tv_info);
            mTimeTv = (TextView) v.findViewById(R.id.tv_time);
        }
    }

}
