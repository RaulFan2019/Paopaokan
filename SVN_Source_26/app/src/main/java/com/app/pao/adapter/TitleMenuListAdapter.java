package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.data.db.MessageData;

import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 消息列表适配器
 */
public class TitleMenuListAdapter extends SimpleBaseAdapter<String> {
    private int applyGroupNum=0;

    public TitleMenuListAdapter(Context context, List<String> datas,int applyGroupNum) {
        super(context, datas);
        this.applyGroupNum = applyGroupNum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_title_menu, null);
            mHolder = new FriendViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (FriendViewHolder) convertView.getTag();
        }

        mHolder.mInfoTv.setText(datas.get(position));
        if((datas.get(position)).equals("入团审核") && applyGroupNum>0){
            mHolder.mInfoIv.setVisibility(View.VISIBLE);
        }else{
            mHolder.mInfoIv.setVisibility(View.GONE);
        }

        return convertView;
    }

    /*好友适配界面**/
    class FriendViewHolder {
        TextView mInfoTv;
        ImageView mInfoIv;

        public FriendViewHolder(View v) {
            mInfoTv = (TextView) v.findViewById(R.id.tv_menu_text);
            mInfoIv = (ImageView) v.findViewById(R.id.iv_menu_has_apply);
        }
    }

}
