package com.app.pao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.entity.db.DBEntityRecord;
import com.app.pao.entity.model.UserOptMedalEntity;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/3/16.
 */
public class MedalChangeAdapterOfWalkOut extends BaseAdapter {
    private LayoutInflater mInflater;
    List<DBEntityRecord> mMedalOptList;

    public MedalChangeAdapterOfWalkOut(Context context, List<DBEntityRecord> mMedalOptList) {
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
            convertView = mInflater.inflate(R.layout.item_medal_change_dialog_of_walkout,null);
            medalHolder = new MedalHolder(convertView);
            convertView.setTag(medalHolder);
        }else {
            medalHolder = (MedalHolder) convertView.getTag();
        }
        DBEntityRecord mMedal = mMedalOptList.get(position);
        String mMedalNameStr = "";
        String mCurrentMedal = "";
        int mMedalRes = R.drawable.icon_no_medal;

        switch (mMedal.getType()) {
            //最长距离
            case UserOptMedalEntity.TYPE_LONGEST:
                mMedalNameStr = "最长距离";

                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_longest;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_longest;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_longest;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_longest;
                    mCurrentMedal = "铁牌";
                }
                medalHolder.mChangeTypeTvLeft.setText(mMedalNameStr+"新纪录");
                medalHolder.mChangeTypeTvMid.setText(NumUtils.retainTheDecimal(mMedal.getRecord()));
                medalHolder.mChangeTypeTvRight.setText("公里");
                break;
            //最长时间
            case UserOptMedalEntity.TYPE_MAX_DURATION:
                mMedalNameStr = "最长时间";
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_max_duration;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_max_duration;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_max_duration;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_max_duration;
                    mCurrentMedal = "铁牌";
                }
                medalHolder.mChangeTypeTvLeft.setText(mMedalNameStr+"新纪录");
                medalHolder.mChangeTypeTvMid.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //最快配速
            case UserOptMedalEntity.TYPE_FASTEST_PACE:
                mMedalNameStr = "最快配速";

                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_pace;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_pace;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_pace;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_pace;
                    mCurrentMedal = "铁牌";
                }
                medalHolder.mChangeTypeTvLeft.setText(mMedalNameStr+"新纪录");
                medalHolder.mChangeTypeTvMid.setText(TimeUtils.getPaceWithFh((long) mMedal.getRecord()));
                medalHolder.mChangeTypeTvRight.setText("/公里");
                break;
            //5公里最快
            case UserOptMedalEntity.TYPE_FASTEST_5:
                mMedalNameStr = "五公里";
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_5;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_5;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_5;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_5;
                    mCurrentMedal = "铁牌";
                }
                medalHolder.mChangeTypeTvLeft.setText(mMedalNameStr+"新纪录");
                medalHolder.mChangeTypeTvMid.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //10公里最快
            case UserOptMedalEntity.TYPE_FASTEST_10:
                mMedalNameStr = "十公里";
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_10;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_10;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_10;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_10;
                    mCurrentMedal = "铁牌";
                }
                medalHolder.mChangeTypeTvLeft.setText(mMedalNameStr+"新纪录");
                medalHolder.mChangeTypeTvMid.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //半马最快
            case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                mMedalNameStr = "半马";
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_half_marathon;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_half_marathon;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_half_marathon;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_half_marathon;
                    mCurrentMedal = "铁牌";
                }
                medalHolder.mChangeTypeTvLeft.setText(mMedalNameStr+"新纪录");
                medalHolder.mChangeTypeTvMid.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //全马最快
            case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                mMedalNameStr = "全马";
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_full_marathon;
                    mCurrentMedal = "金牌";
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_full_marathon;
                    mCurrentMedal = "银牌";
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_full_marathon;
                    mCurrentMedal = "铜牌";
                } else {
                    mMedalRes = R.drawable.icon_iron_full_marathon;
                    mCurrentMedal = "铁牌";
                }
                medalHolder.mChangeTypeTvLeft.setText(mMedalNameStr+"新纪录");
                medalHolder.mChangeTypeTvMid.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
        }

        String preMedal = getPreMedal(mMedal.getPrepercentage());

        if(mMedal.getPrerecord() == 0){
            medalHolder.mChangeValueTv.setText("将" + mMedalNameStr + mCurrentMedal + "收入囊中!");
        }else if(!(mCurrentMedal.equals(preMedal)) && mMedal.getPercentage() > mMedal.getPrepercentage()){
            medalHolder.mChangeValueTv.setText(mMedalNameStr + preMedal + "升级为" + mCurrentMedal + "!");
        }
        medalHolder.mChangeMedalIv.setImageResource(mMedalRes);

        return convertView;
    }

    /**
     * 用于判断上一次记录是什么奖牌
     * @param prePercentage
     * @return
     */
    private String getPreMedal(int prePercentage){
        if (prePercentage >= 90) {
            return "金牌";
        } else if (prePercentage >= 70) {
            return "银牌";
        } else if (prePercentage >= 40) {
            return "铜牌";
        } else {
            return "铁牌";
        }
    }

    class MedalHolder{
        TextView mChangeTypeTvLeft,mChangeTypeTvMid,mChangeTypeTvRight;
        TextView mChangeValueTv;
        ImageView mChangeMedalIv;
        public MedalHolder(View view) {
            mChangeTypeTvLeft = (TextView) view.findViewById(R.id.tv_change_type_left);
            mChangeTypeTvMid = (TextView) view.findViewById(R.id.tv_change_type_mid);
            mChangeTypeTvRight = (TextView) view.findViewById(R.id.tv_change_type_right);
            mChangeValueTv = (TextView) view.findViewById(R.id.tv_change_value);
            mChangeMedalIv = (ImageView) view.findViewById(R.id.iv_change_medal);
        }
    }
}
