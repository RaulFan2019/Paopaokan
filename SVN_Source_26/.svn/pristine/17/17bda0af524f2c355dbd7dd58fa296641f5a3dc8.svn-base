package com.app.pao.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.entity.network.GetGroupPartyMyPhotoReqult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/18.
 */
public class GroupPartyMyPhotoCheckAdapter extends RecyclerView.Adapter<GroupPartyMyPhotoCheckAdapter.MyPhotoCheckViewHolder>{

    private List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList;
    private List<Integer> mSelectTypeList = new ArrayList<Integer>();

    public GroupPartyMyPhotoCheckAdapter(List<GetGroupPartyMyPhotoReqult.UserPicture> mDataList) {
        this.mDataList = mDataList;
        for (int i = 0;i < mDataList.size();i++){
            mSelectTypeList.add(0);
        }
    }

    @Override
    public MyPhotoCheckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_party_my_photo_check,parent,false);
        MyPhotoCheckViewHolder holder = new MyPhotoCheckViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyPhotoCheckViewHolder holder, int position) {
        holder.bindItem(position);
    }

    @Override
    public int getItemCount() {
        return mSelectTypeList.size();
    }

    public class MyPhotoCheckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mIsCheckIv;

        public MyPhotoCheckViewHolder(View itemView) {
            super(itemView);
            mIsCheckIv = (ImageView) itemView.findViewById(R.id.iv_is_check);
            itemView.setOnClickListener(this);
        }

        public void bindItem(int position){
            int mSelectType = mSelectTypeList.get(position);
            if(mSelectType == 0){
         //       mIsCheckIv.setVisibility(View.INVISIBLE);
                mIsCheckIv.setImageResource(R.drawable.icon_manager_n);
            }else if(mSelectType == 1){
                mIsCheckIv.setVisibility(View.VISIBLE);
                mIsCheckIv.setImageResource(R.drawable.icon_manager_n);
            }else if( mSelectType == 2){
                mIsCheckIv.setVisibility(View.VISIBLE);
                mIsCheckIv.setImageResource(R.drawable.icon_manager_y);
            }
        }

        @Override
        public void onClick(View v) {
            setItemChecked(getAdapterPosition());
        }
    }

    /**
     * 进入编辑模式
     */
    public void inEditMode(){
        mSelectTypeList.clear();
        for(int i =0;i<mDataList.size();i++){
            mSelectTypeList.add(1);
        }
        notifyDataSetChanged();
    }

    /**
     * 取消编辑模式
     */
    public void outEditMode(){
        mSelectTypeList.clear();
        for(int i =0;i<mDataList.size();i++){
            mSelectTypeList.add(0);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置item选择
     *
     * @param position
     */
    public void setItemChecked(int position){
        mSelectTypeList.set(position, 2);
        notifyItemChanged(position);
    }
}
