package com.app.pao.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.model.PhoneNumEntity;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * 搜索好友适配器
 *
 * @author Administrator
 */
public class SearchFriendsFromPhoneAdapter extends SimpleBaseAdapter<PhoneNumEntity> {


    private BitmapUtils mBitmapU;
    private Context mContext;

    public SearchFriendsFromPhoneAdapter(Context context, List<PhoneNumEntity> userList) {
        super(context, userList);
        mBitmapU = new BitmapUtils(context);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShearchViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_search_friends_by_phone, null);
            mHolder = new ShearchViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ShearchViewHolder) convertView.getTag();
        }
        //用户信息对象
        PhoneNumEntity entity = datas.get(position);
        //手机中的名称
        mHolder.mPhoneNameTv.setText(entity.getContactName());

        //若已注册
        if (entity.getUsersEntity() != null) {
            mHolder.m123NameTv.setText(entity.getUsersEntity().getNickname());
            //若已是好友
            if (entity.getUsersEntity().getIsFriend() == AppEnum.IsFriend.FRIEND) {
                mHolder.mStatusTv.setText("已添加");
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mAgreeBtn.setVisibility(View.GONE);
            } else if (entity.getUsersEntity().getHasSendApply() == AppEnum.HasApply.APPLY) {
                mHolder.mStatusTv.setText("已申请");
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mAgreeBtn.setVisibility(View.GONE);
            } else {
                mHolder.m123NameTv.setText(entity.getPhoneNumber() + "");
                mHolder.mAgreeBtn.setText("加好友");
                mHolder.mAgreeBtn.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setVisibility(View.INVISIBLE);
            }

            //若是自己
            if (entity.getUsersEntity().getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                mHolder.mAgreeBtn.setVisibility(View.GONE);
                mHolder.mStatusTv.setVisibility(View.VISIBLE);
                mHolder.mStatusTv.setText("我");
            }
            ImageUtils.loadUserImage(entity.getUsersEntity().getAvatar(), mHolder.mFriendPhotoCiv);
            mHolder.mFriendPhotoCiv.setVisibility(View.VISIBLE);
        } else {
            mHolder.m123NameTv.setText(entity.getPhoneNumber() + "");
            mHolder.mAgreeBtn.setText("短信邀请");
            mHolder.mAgreeBtn.setVisibility(View.VISIBLE);
            mHolder.mFriendPhotoCiv.setVisibility(View.GONE);
            mHolder.mStatusTv.setVisibility(View.INVISIBLE);
        }

        final int pos = position;
        mHolder.mAgreeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onItemBtnClick(pos);
            }
        });

        return convertView;
    }

    /** **/
    class ShearchViewHolder {
        TextView m123NameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像
        TextView mPhoneNameTv;//来源文本
        Button mAgreeBtn;   //操作按钮
        TextView mStatusTv; //状态文本

        public ShearchViewHolder(View v) {
            mAgreeBtn = (Button) v.findViewById(R.id.btn_agree);
            mPhoneNameTv = (TextView) v.findViewById(R.id.tv_phoneName);
            m123NameTv = (TextView) v.findViewById(R.id.tv_123Name);
            mFriendPhotoCiv = (CircularImage) v.findViewById(R.id.iv_search_friends_photo);
            mStatusTv = (TextView) v.findViewById(R.id.tv_friend_status);
        }
    }

}
