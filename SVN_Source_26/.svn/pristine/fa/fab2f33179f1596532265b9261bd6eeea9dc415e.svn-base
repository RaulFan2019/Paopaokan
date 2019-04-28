package com.app.pao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.model.UserOptMedalEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MedalChangeAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<UserOptMedalEntity> mMedalOptList;

    public MedalChangeAdapter(Context context,List<UserOptMedalEntity> mMedalOptList) {
        this.mMedalOptList = mMedalOptList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMedalOptList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMedalOptList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MedalHolder medalHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_medal_change_dialog,null);
            medalHolder = new MedalHolder(convertView);
            convertView.setTag(medalHolder);
        }else {
            medalHolder = (MedalHolder) convertView.getTag();
        }
        UserOptMedalEntity mMedal = mMedalOptList.get(position);
        String mMedalNameStr = "";
        String mLastMedal = "";
        String mCurrentMedal = "";
        int mMedalRes = R.drawable.icon_no_medal;

        switch (mMedal.getMedalType()) {
            //最长距离
            case UserOptMedalEntity.TYPE_LONGEST:
                mMedalNameStr = "耐力";

                if (mMedal.getUserMedal().getRanking() >= 90) {
                    mMedalRes = R.drawable.icon_gold_longest;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getUserMedal().getRanking() >= 70) {
                    mMedalRes = R.drawable.icon_silver_longest;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getUserMedal().getRanking() >= 40) {
                    mMedalRes = R.drawable.icon_copper_longest;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_longest;
                    mCurrentMedal = "铁牌";
                }
                break;
            //最长时间
            case UserOptMedalEntity.TYPE_MAX_DURATION:
                mMedalNameStr = "坚持";
                if (mMedal.getUserMedal().getRanking() >= 90) {
                    mMedalRes = R.drawable.icon_gold_max_duration;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getUserMedal().getRanking() >= 70) {
                    mMedalRes = R.drawable.icon_silver_max_duration;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getUserMedal().getRanking() >= 40) {
                    mMedalRes = R.drawable.icon_copper_max_duration;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_max_duration;
                    mCurrentMedal = "铁牌";
                }
                break;
            //最快配速
            case UserOptMedalEntity.TYPE_FASTEST_PACE:
                mMedalNameStr = "速度";

                if (mMedal.getUserMedal().getRanking() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_pace;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getUserMedal().getRanking() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_pace;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getUserMedal().getRanking() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_pace;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_pace;
                    mCurrentMedal = "铁牌";
                }
                break;
            //5公里最快
            case UserOptMedalEntity.TYPE_FASTEST_5:
                mMedalNameStr = "五公里";
                if (mMedal.getUserMedal().getRanking() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_5;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getUserMedal().getRanking() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_5;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getUserMedal().getRanking() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_5;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_5;
                    mCurrentMedal = "铁牌";
                }
                break;
            //10公里最快
            case UserOptMedalEntity.TYPE_FASTEST_10:
                mMedalNameStr = "十公里";
                if (mMedal.getUserMedal().getRanking() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_10;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getUserMedal().getRanking() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_10;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getUserMedal().getRanking() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_10;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_10;
                    mCurrentMedal = "铁牌";
                }
                break;
            //半马最快
            case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                mMedalNameStr = "半马";
                if (mMedal.getUserMedal().getRanking() >= 90) {
                    mMedalRes = R.drawable.icon_gold_half_marathon;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getUserMedal().getRanking() >= 70) {
                    mMedalRes = R.drawable.icon_silver_half_marathon;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getUserMedal().getRanking() >= 40) {
                    mMedalRes = R.drawable.icon_copper_half_marathon;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_half_marathon;
                    mCurrentMedal = "铁牌";
                }
                break;
            //全马最快
            case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                mMedalNameStr = "全马";
                if (mMedal.getUserMedal().getRanking() >= 90) {
                    mMedalRes = R.drawable.icon_gold_full_marathon;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getUserMedal().getRanking() >= 70) {
                    mMedalRes = R.drawable.icon_silver_full_marathon;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getUserMedal().getRanking() >= 40) {
                    mMedalRes = R.drawable.icon_copper_full_marathon;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_full_marathon;
                    mCurrentMedal = "铁牌";
                }
                break;
        }
        if (mMedal.getUserMedal().getPreranking() >= 90) {
            mLastMedal = "金牌";
        } else if (mMedal.getUserMedal().getPreranking() >= 70) {
            mLastMedal = "银牌";
        } else if (mMedal.getUserMedal().getPreranking() >= 40) {
            mLastMedal = "铜牌";
        } else {
            mLastMedal = "铁牌";
        }


        medalHolder.mChangeMedalIv.setImageResource(mMedalRes);
        medalHolder.mChangeTypeTv.setText(mMedalNameStr + mLastMedal + "被降成" + mCurrentMedal);
        medalHolder.mChangeValueTv.setText("战胜人数由 "+mMedal.getUserMedal().getPreranking()+"% 降为 "+mMedal.getUserMedal().getRanking()+"%");

        return convertView;
    }

    class MedalHolder{
        TextView mChangeTypeTv;
        TextView mChangeValueTv;
        ImageView mChangeMedalIv;
        public MedalHolder(View view) {
            mChangeTypeTv = (TextView) view.findViewById(R.id.tv_change_type);
            mChangeValueTv = (TextView) view.findViewById(R.id.tv_change_value);
            mChangeMedalIv = (ImageView) view.findViewById(R.id.iv_change_medal);
        }
    }
}
