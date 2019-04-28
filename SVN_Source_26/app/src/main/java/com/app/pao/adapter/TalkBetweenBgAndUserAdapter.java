package com.app.pao.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.entity.network.TalkBetweenBgAndUser;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.ImageUtils;
import com.lidroid.xutils.BitmapUtils;
import com.rey.material.widget.TextView;

import java.util.List;

/**
 * Created by LY on 2016/3/22.
 */
public class TalkBetweenBgAndUserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<TalkBetweenBgAndUser> mTalkList;
    private Context mContext;
    private BitmapUtils mBitMap;

    public TalkBetweenBgAndUserAdapter(List<TalkBetweenBgAndUser> mTalkList, Context mContext) {
        this.mTalkList = mTalkList;
        this.mContext = mContext;
        mBitMap = new BitmapUtils(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == AppEnum.TalkBetweenBgAndUser.USER){
            View viewUser = LayoutInflater.from(mContext).inflate(R.layout.item_user_opinion,parent,false);
            UserHolder userHolder = new UserHolder(viewUser);
            return userHolder;
        }else{
            View viewBg = LayoutInflater.from(mContext).inflate(R.layout.item_background_return,parent,false);
            BackgroundHolder bgHolder = new BackgroundHolder(viewBg);
            return bgHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TalkBetweenBgAndUser talk = mTalkList.get(position);
        if(talk!=null) {
            if (holder instanceof UserHolder)
                ((UserHolder) holder).bindUserItem(position);
            else
                ((BackgroundHolder) holder).bindBgItem(position);
        }
    }

    @Override
    public int getItemCount() {
        return mTalkList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = mTalkList.get(position).getType();
        if(type == AppEnum.TalkBetweenBgAndUser.USER)
            return AppEnum.TalkBetweenBgAndUser.USER;
        else
            return AppEnum.TalkBetweenBgAndUser.BACKGROUND;
    }

    /**
     * 用户意见Item的设置
     */
    public class UserHolder extends RecyclerView.ViewHolder{
        TextView mUserOpinionTimeTv;
        TextView mUserOpinionTv;
        CircularImage mUserAvatarCi;

        public UserHolder(View itemView){
            super(itemView);
            mUserOpinionTimeTv = (TextView) itemView.findViewById(R.id.tv_user_opinion_time);
            mUserOpinionTv = (TextView) itemView.findViewById(R.id.tv_user_opinion);
            mUserAvatarCi = (CircularImage) itemView.findViewById(R.id.ci_user_avatar);
        }

        public void bindUserItem(int opinion){
            TalkBetweenBgAndUser talk = mTalkList.get(opinion);
            mUserOpinionTimeTv.setText(talk.getTime());
            mUserOpinionTv.setText(talk.getTalkContent());
            String avatar = LocalApplication.getInstance().getLoginUser(mContext).getAvatar();
            ImageUtils.loadUserImage(avatar,mUserAvatarCi);
        }
    }

    /**
     * 后台回复item的设置
     */
    public class BackgroundHolder extends RecyclerView.ViewHolder{
        TextView mBgReturnTimeTv;
        TextView getmBgReturnTv;

        public BackgroundHolder(View itemView){
            super(itemView);
            mBgReturnTimeTv = (TextView) itemView.findViewById(R.id.tv_background_return_time);
            getmBgReturnTv = (TextView) itemView.findViewById(R.id.tv_background_return);
        }

        public void bindBgItem(int position){
            TalkBetweenBgAndUser talk = mTalkList.get(position);
            mBgReturnTimeTv.setText(talk.getTime());
            getmBgReturnTv.setText(talk.getTalkContent());
        }
    }
}
