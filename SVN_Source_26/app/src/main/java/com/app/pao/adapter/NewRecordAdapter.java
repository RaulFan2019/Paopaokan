package com.app.pao.adapter;

import android.content.Context;
import android.graphics.Typeface;
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
public class NewRecordAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<DBEntityRecord> mMedalOptList;
    private Typeface typeface;//用于设置字体类型

    public NewRecordAdapter(Context context, List<DBEntityRecord> mMedalOptList) {
        this.mMedalOptList = mMedalOptList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_new_record, null);
            medalHolder = new MedalHolder(convertView);
            convertView.setTag(medalHolder);
        } else {
            medalHolder = (MedalHolder) convertView.getTag();
        }
        DBEntityRecord mMedal = mMedalOptList.get(position);
        String mMedalNameStr = "";
        String mMedalTypeStr = "";
        switch (mMedal.type) {
            //最长距离
            case UserOptMedalEntity.TYPE_LONGEST:
                mMedalNameStr = "最长距离新记录";
                mMedalTypeStr = "耐力";
                medalHolder.mChangeValueTv.setText(NumUtils.retainTheDecimal(mMedal.getRecord()));
                break;
            //最长时间
            case UserOptMedalEntity.TYPE_MAX_DURATION:
                mMedalNameStr = "最长时间新记录";
                mMedalTypeStr = "坚持";
                medalHolder.mChangeValueTv.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //最快配速
            case UserOptMedalEntity.TYPE_FASTEST_PACE:
                mMedalNameStr = "最快配速新记录";
                mMedalTypeStr = "配速";
                medalHolder.mChangeValueTv.setText(TimeUtils.getPaceWithFh((long) mMedal.getRecord()));
                break;
            //5公里最快
            case UserOptMedalEntity.TYPE_FASTEST_5:
                mMedalNameStr = "五公里新记录";
                mMedalTypeStr = "五公里";
                medalHolder.mChangeValueTv.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //10公里最快
            case UserOptMedalEntity.TYPE_FASTEST_10:
                mMedalNameStr = "十公里新记录";
                mMedalTypeStr = "十公里";
                medalHolder.mChangeValueTv.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //半马最快
            case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                mMedalNameStr = "半马新纪录";
                mMedalTypeStr = "半马";
                medalHolder.mChangeValueTv.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
            //全马最快
            case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                mMedalNameStr = "全马新记录";
                mMedalTypeStr = "全马";
                medalHolder.mChangeValueTv.setText(TimeUtils.getTimeWithFh((long) mMedal.getRecord()));
                break;
        }
        medalHolder.mTypeTv.setText(mMedalNameStr);
        //若奖牌发生变化
        if (mMedal.percentage > mMedal.prepercentage
                && getPreMedal(mMedal.percentage).equals(getPreMedal(mMedal.prepercentage))) {
            medalHolder.mGetMadelTv.setText("恭喜斩获" + mMedalTypeStr + getPreMedal(mMedal.percentage) + "!");
        }
        return convertView;
    }

    /**
     * 用于判断上一次记录是什么奖牌
     *
     * @param prePercentage
     * @return
     */
    private String getPreMedal(int prePercentage) {
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

    class MedalHolder {
        TextView mChangeValueTv;//记录的具体数据
        TextView mTypeTv;//记录类型文本
        TextView mGetMadelTv;//斩获奖牌文本

        public MedalHolder(View view) {
            mGetMadelTv = (TextView) view.findViewById(R.id.tv_get_madel);
            mChangeValueTv = (TextView) view.findViewById(R.id.tv_change_value);
            mTypeTv = (TextView) view.findViewById(R.id.tv_type);
            mChangeValueTv.setTypeface(typeface);
        }
    }
}
