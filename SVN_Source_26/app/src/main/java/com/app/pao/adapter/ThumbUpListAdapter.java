package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.R;
import com.app.pao.entity.network.GetThumbsResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by Raul on 2015/11/26.
 * 点赞列表适配器
 */
public class ThumbUpListAdapter extends SimpleBaseAdapter<GetThumbsResult> {


    private BitmapUtils mBitmapU;

    public ThumbUpListAdapter(Context context, List<GetThumbsResult> datas) {
        super(context, datas);
        mBitmapU = new BitmapUtils(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_thumb_up_list, null);
            mHolder = new FriendViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (FriendViewHolder) convertView.getTag();
        }
        //用户信息对象
        GetThumbsResult entity = datas.get(position);

        mHolder.mFriendNameTv.setText(entity.getNickname() + "");
        ImageUtils.loadUserImage(entity.getAvatar(), mHolder.mFriendPhotoCiv);

        return convertView;
    }

    /*好友适配界面**/
    class FriendViewHolder {
        TextView mFriendNameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像

        public FriendViewHolder(View v) {
            mFriendNameTv = (TextView) v.findViewById(R.id.tv_friends_nickname);
            mFriendPhotoCiv = (CircularImage) v.findViewById(R.id.iv_friends_photo);
        }
    }

}
