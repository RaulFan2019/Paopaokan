package com.app.pao.activity.settings;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.entity.db.DBUserEntity;
import com.app.pao.entity.model.CardEntity;
import com.app.pao.entity.network.GetPersonRecordResult;
import com.app.pao.ui.widget.CircularImage;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.ImageUtils;
import com.app.pao.utils.NumUtils;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/12.
 */
@ContentView(R.layout.activity_user_level_card)
public class UserLevelCardActivity extends BaseAppCompActivity implements View.OnClickListener {
    private static final int TYPE_UP = 0;//上
    private static final int TYPE_SHOW = 1;//显示item
    private static final int TYPE_DOWN = 2;//下

    private int showPos = 0;
    private List<CardEntity> mCardList;

    private int currentKm = 0;

    @ViewInject(R.id.fl_card_content)
    private FrameLayout mCardContentFl;
    private BitmapUtils bitmapUtils;
    private String avatar, nickname;
    private float totallength;
    private Typeface typeface;//用于设置字体类型

    //初始化展示数据
    @Override
    protected void initData() {
        bitmapUtils = new BitmapUtils(mContext);
        mCardList = new ArrayList<CardEntity>();
        avatar = getIntent().getStringExtra("avatar");
        nickname = getIntent().getStringExtra("nickname");
        totallength = getIntent().getFloatExtra("totallength", 0f);
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");

        showPos = NumUtils.parseLevelFromLength(totallength);
        for (int i = 0; i < 8; i++) {
            CardEntity mCard = getCard(i);
            if (i < showPos) {
                mCard.currentLv = TYPE_UP;
            } else if (i == showPos) {
                mCard.currentLv = TYPE_SHOW;
            } else if (i > showPos) {
                mCard.currentLv = TYPE_DOWN;
            }
            mCardList.add(mCard);
        }
    }

    @Override
    protected void initViews() {
        addCard();
    }

    @Override
    protected void doMyOnCreate() {

    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        mCardContentFl.removeAllViews();
        mCardList.clear();
    }

    //插入卡片页面
    private void addCard() {
        UpDownHolder mUpDownHolder = null;
        ShowHolder mShowHolder = null;
        //画出上半部分
        for (int i = 0; i < 8; i++) {
            CardEntity mCard = mCardList.get(i);
            View v = null;
            if (mCard.currentLv == TYPE_UP) {
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.item_user_level_card_up_down, null);
                mUpDownHolder = new UpDownHolder(v);
                mUpDownHolder.mCardBgIv.setImageResource(mCard.cardSmallBg);
                mUpDownHolder.mCardUpNameTv.setText(mCard.name);
                mUpDownHolder.mCardUpKmTv.setText(mCard.kmStr);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //第一张卡片时去除上部分阴影
                if (mCard.level == 0) {
                    mUpDownHolder.mCardShadowUpIv.setVisibility(View.GONE);
                    lp.setMargins(0, 0, 0, 0);
                } else {
                    //去除阴影后，才去计算距离顶部的高度
                    lp.setMargins(0, i * 150 - 20, 0, 0);
                }
                mCardContentFl.addView(v, lp);
            } else if (mCard.currentLv == TYPE_SHOW) {
                v = null;//将最后一个view置为null后，GC
                break;
            }
        }

        //画出底部
        for (int i = mCardList.size(); i > (showPos + 1); i--) {
            View v = null;
            CardEntity mCard = mCardList.get(i - 1);
            if (mCard.currentLv == TYPE_DOWN) {
                LayoutInflater inflater = getLayoutInflater();
                v = inflater.inflate(R.layout.item_user_level_card_up_down, null);
                mUpDownHolder = new UpDownHolder(v);
                mUpDownHolder.mCardBgIv.setImageResource(mCard.cardSmallBg);
                mUpDownHolder.mCardDownNameTv.setText(mCard.name);
                mUpDownHolder.mCardDownKmTv.setText(mCard.kmStr);
                //去除底部阴影
                if (mCard.level == (mCardList.size() - 1)) {
                    mUpDownHolder.mCardShadowDownIv.setVisibility(View.GONE);
                }
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, i * 150 + 460, 0, 0);
                mCardContentFl.addView(v, lp);
            } else {
                v = null;
                break;
            }
        }

        //画出中部
        LayoutInflater showInflater = getLayoutInflater();
        View showV = showInflater.inflate(R.layout.item_user_level_card, null);
        CardEntity mShowCard = mCardList.get(showPos);
        mShowHolder = new ShowHolder(showV);
        mShowHolder.mCardBgIv.setImageResource(mShowCard.cardBigBg);
        mShowHolder.mCardShowKmTv.setText(mShowCard.kmStr);
        mShowHolder.mCardShowNameTv.setText(mShowCard.name);
        mShowHolder.mCardUserNameTv.setText(nickname);
        mShowHolder.mTotalLength.setTypeface(typeface);
        mShowHolder.mTotalLength.setText(NumUtils.retainTheDecimal(totallength));
//        mShowHolder.mCardProgressTv.setText((mPersonRecord.getTotallength()/1000)+"/"+mShowCard.nextKm);
        int level = NumUtils.getTotalLengthLevel(totallength);
        int lastLevelLength = NumUtils.getLevelLength(level);
        int nextLevelLength = NumUtils.getNextLevelLength(level);
        int percent = 100;
        if (level != 8) {
            percent = ((((int) (totallength / 1000)) - lastLevelLength / 1000) * 100 / ((nextLevelLength - lastLevelLength) / 1000));
            mShowHolder.mLevelLl.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mShowHolder.mKmProgressPb.getLayoutParams();
            lp.height = (int) (DeviceUtils.dpToPixel(275) * percent / 100);
            mShowHolder.mKmProgressPb.setLayoutParams(lp);
            mShowHolder.mCardProgressTv.setText((((int) (totallength / 1000)) - lastLevelLength / 1000) + "/" + (nextLevelLength - lastLevelLength) / 1000);
        } else {
            mShowHolder.mLevelLl.setVisibility(View.INVISIBLE);
        }
//        mShowHolder.mKmProgressPb.setProgress((int) (100 * ()));
        ImageUtils.loadUserImage(avatar, mShowHolder.mUserAvatarIv);
//        LinearLayout.LayoutParams imgLp = (LinearLayout.LayoutParams) mShowHolder.mProgressBarProgressIv.getLayoutParams();
//        imgLp.height = DeviceUtils.dip2px(this, (float) (284 * 01));
//        mShowHolder.mProgressBarProgressIv.setLayoutParams(imgLp);

        FrameLayout.LayoutParams showLp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mShowCard.level == 0) {
            mShowHolder.mCardShadowUpIv.setVisibility(View.GONE);
            showLp.setMargins(0, showPos * 150, 0, 0);
        } else {
            showLp.setMargins(0, showPos * 150 - 20, 0, 0);
        }
        if (mShowCard.level == (mCardList.size() - 1)) {
            mShowHolder.mCardShadowDownIv.setVisibility(View.GONE);
        }
        mCardContentFl.addView(showV, showLp);
        showV = null;//GC
    }

    //根据不同级别new不同的实体类
    private CardEntity getCard(int level) {
        if (level == 0) {
            return new CardEntity(level, "10000公里", 10000, "海王星", R.drawable.bg_card_big_level_8, R.drawable.bg_card_small_level_8);
        } else if (level == 1) {
            return new CardEntity(level, "5000公里", 10000, "天王星", R.drawable.bg_card_big_level_7, R.drawable.bg_card_small_level_7);
        } else if (level == 2) {
            return new CardEntity(level, "2000公里", 5000, "土星", R.drawable.bg_card_big_level_6, R.drawable.bg_card_small_level_6);
        } else if (level == 3) {
            return new CardEntity(level, "1000公里", 2000, "木星", R.drawable.bg_card_big_level_5, R.drawable.bg_card_small_level_5);
        } else if (level == 4) {
            return new CardEntity(level, "500公里", 1000, "火星", R.drawable.bg_card_big_level_4, R.drawable.bg_card_small_level_4);
        } else if (level == 5) {
            return new CardEntity(level, "200公里", 500, "月球", R.drawable.bg_card_big_level_3, R.drawable.bg_card_small_level_3);
        } else if (level == 6) {
            return new CardEntity(level, "50公里", 200, "金星", R.drawable.bg_card_big_level_2, R.drawable.bg_card_small_level_2);
        } else if (level == 7) {
            return new CardEntity(level, "起步", 50, "水星", R.drawable.bg_card_big_level_1, R.drawable.bg_card_small_level_1);
        } else {
            return new CardEntity(level, "起步", 50, "水星", R.drawable.bg_card_big_level_1, R.drawable.bg_card_small_level_1);
        }
    }

    @Override
    @OnClick({R.id.iv_back})
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    /**
     * 被覆盖的页面初始化类
     */
    class UpDownHolder {
        TextView mCardUpNameTv;
        TextView mCardUpKmTv;
        TextView mCardDownNameTv;
        TextView mCardDownKmTv;
        ImageView mCardBgIv;
        ImageView mCardShadowUpIv;
        ImageView mCardShadowDownIv;

        public UpDownHolder(View v) {
            mCardUpNameTv = (TextView) v.findViewById(R.id.tv_card_name_up);
            mCardUpKmTv = (TextView) v.findViewById(R.id.tv_card_km_up);
            mCardDownNameTv = (TextView) v.findViewById(R.id.tv_card_name_down);
            mCardDownKmTv = (TextView) v.findViewById(R.id.tv_card_km_down);
            mCardBgIv = (ImageView) v.findViewById(R.id.iv_card_up_down_bg);
            mCardShadowUpIv = (ImageView) v.findViewById(R.id.iv_shadow_up);
            mCardShadowDownIv = (ImageView) v.findViewById(R.id.iv_shadow_down);
        }
    }

    /**
     * 展示的卡片 初始化类
     */
    class ShowHolder {
        ImageView mCardBgIv;
        TextView mCardShowNameTv;
        TextView mCardShowKmTv;
        TextView mCardProgressTv;
        TextView mCardUserNameTv;
        ImageView mCardShadowUpIv;
        ImageView mCardShadowDownIv;
        CircularImage mUserAvatarIv;
        TextView mTotalLength;
        LinearLayout mLevelLl;
        //        LinearLayout mProgressBarBgLl;
//        ImageView mProgressBarProgressIv;
        View mKmProgressPb;

        public ShowHolder(View v) {
            mCardBgIv = (ImageView) v.findViewById(R.id.iv_card_show_bg);
            mCardShowNameTv = (TextView) v.findViewById(R.id.tv_card_name_show);
            mCardShowKmTv = (TextView) v.findViewById(R.id.tv_card_km_show);
            mCardProgressTv = (TextView) v.findViewById(R.id.tv_card_progress);
            mCardUserNameTv = (TextView) v.findViewById(R.id.tv_user_name);
            mCardShadowUpIv = (ImageView) v.findViewById(R.id.iv_show_shadow_up);
            mCardShadowDownIv = (ImageView) v.findViewById(R.id.iv_show_shadow_down);
//            mProgressBarBgLl = (LinearLayout) v.findViewById(R.id.ll_pb_bg);
//            mProgressBarProgressIv = (ImageView) v.findViewById(R.id.ll_pb_progress);
            mKmProgressPb = v.findViewById(R.id.pb_km_progress);
            mUserAvatarIv = (CircularImage) v.findViewById(R.id.iv_user_photo);
            mTotalLength = (TextView) v.findViewById(R.id.tv_total_length);
            mLevelLl = (LinearLayout) v.findViewById(R.id.ll_level);
        }
    }
}
