package com.app.pao.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.friend.UserInfoActivity;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.GetSearchUserResult;
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
public class SearchFriendsAdapter extends SimpleBaseAdapter<GetSearchUserResult.UsersEntity> {

    private BitmapUtils mBitmapU;
    private Context mContext;

    public SearchFriendsAdapter(Context context, List<GetSearchUserResult.UsersEntity> userList) {
        super(context, userList);
        mBitmapU = new BitmapUtils(context);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ShearchViewHolder mHolder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_search_friends_list, null);
            mHolder = new ShearchViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ShearchViewHolder) convertView.getTag();
        }

        //防止自动加载数据时滑动列表导致崩溃
        if (datas.size() == 0) {
            return null;
        }

        //用户信息对象
        GetSearchUserResult.UsersEntity fEntity = datas.get(position);

        //用户性别
        if (fEntity.getGender() == AppEnum.UserGander.MAN) {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_man);
        } else {
            mHolder.mGanderIv.setBackgroundResource(R.drawable.icon_women);
        }
        //用户来源
        mHolder.mFriendNameTv.setText(fEntity.getNickname() + "");
        if (fEntity.getFrom() == 2) {
            mHolder.mFriendFromTv.setText("来自于微信");
        } else {
            mHolder.mFriendFromTv.setText("来自于手机");
        }
        //头像
        ImageUtils.loadUserImage( fEntity.getAvatar(), mHolder.mFriendPhotoCiv);

        //如果是朋友关系
        if (fEntity.getIsFriend() == AppEnum.IsFriend.FRIEND) {
            mHolder.mAgreeBtn.setVisibility(View.GONE);
            mHolder.mStatusTv.setVisibility(View.VISIBLE);
            mHolder.mStatusTv.setText("已添加");
            //已发出邀请
        } else if (fEntity.getHasSendApply() == AppEnum.HasApply.APPLY) {
            mHolder.mAgreeBtn.setVisibility(View.GONE);
            mHolder.mStatusTv.setVisibility(View.VISIBLE);
            mHolder.mStatusTv.setText("已申请");
        } else {
            mHolder.mAgreeBtn.setVisibility(View.VISIBLE);
            mHolder.mStatusTv.setVisibility(View.GONE);
        }

        //若是自己
        if (fEntity.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
            mHolder.mAgreeBtn.setVisibility(View.GONE);
            mHolder.mStatusTv.setVisibility(View.VISIBLE);
            mHolder.mStatusTv.setText("我");
        }
        final int pos = position;
        mHolder.mAgreeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onItemBtnClick(pos);
            }
        });

        final GetSearchUserResult.UsersEntity entity = fEntity;
        mHolder.mitemll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.getId() == LocalApplication.getInstance().getLoginUser(mContext).getUserId()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt("userid", entity.getId());
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    /** **/
    class ShearchViewHolder {
        TextView mFriendNameTv;// 姓名文本
        CircularImage mFriendPhotoCiv;// 头像
        TextView mFriendFromTv;//来源文本
        Button mAgreeBtn;
        TextView mStatusTv;
        ImageView mGanderIv;//性别图标
        LinearLayout mitemll;

        public ShearchViewHolder(View v) {
            mAgreeBtn = (Button) v.findViewById(R.id.btn_agree);
            mFriendNameTv = (TextView) v.findViewById(R.id.tv_friends_name);
            mFriendFromTv = (TextView) v.findViewById(R.id.tv_freinds_from);
            mFriendPhotoCiv = (CircularImage) v.findViewById(R.id.iv_search_friends_photo);
            mStatusTv = (TextView) v.findViewById(R.id.tv_friend_status);
            mGanderIv = (ImageView) v.findViewById(R.id.iv_gander);
            mitemll = (LinearLayout) v.findViewById(R.id.ll_item);
        }
    }

}
