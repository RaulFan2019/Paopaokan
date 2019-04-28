package com.app.pao.fragment.friend;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.pao.R;
import com.app.pao.config.AppEnum;
import com.app.pao.fragment.BaseFragment;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * Created by LY on 2016/5/6.
 */
public class FriendRankingFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.tv_user_rank_time)
    public TextView mRankTime;
    @ViewInject(R.id.tv_user_rank_totle_people)
    public TextView mRankTotal;
    @ViewInject(R.id.tv_user_rank)
    public TextView mRankTv;
    @ViewInject(R.id.tv_rank_type)
    private TextView mRankTypeTv;
    @ViewInject(R.id.iv_up_or_down)
    private ImageView mUpOrDownIv;
    @ViewInject(R.id.fl_fragment_contaner)
    private FrameLayout mFragmentContanerFl;
    @ViewInject(R.id.fl_base)
    private FrameLayout mBaseFl;
    @ViewInject(R.id.ll_rank_type)
    private LinearLayout mRankTypeLl;
    @ViewInject(R.id.tv_week_rank)
    private TextView mRankWeekTv;
    @ViewInject(R.id.tv_month_rank)
    private TextView mRankMonthTv;
    @ViewInject(R.id.tv_total_rank)
    private TextView mRankTotalTv;

    private boolean isTypeLlShow;
    private int rankTypeInt;
    private FragmentManager fragmentManager;
    private Typeface typeface;//用于设置字体类型

    public static FriendRankingFragment newInstance() {
        FriendRankingFragment friendRankingFragment = new FriendRankingFragment();
        return friendRankingFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_ranking;
    }

    @Override
    protected void initParams() {
        initDate();
        initView();
    }

    private void initDate() {
        isTypeLlShow = false;
        rankTypeInt = AppEnum.friendRankType.WEEK;
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
    }

    private void initView() {
        mRankTv.setTypeface(typeface);
        fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fl_fragment_contaner, FriendRankingWeekFragment.newInstance(AppEnum.friendRankType.WEEK));
        transaction.commit();
    }

    private void setIsTypeShow() {
        if (isTypeLlShow) {
            mRankTypeLl.setVisibility(View.VISIBLE);
            mUpOrDownIv.setBackgroundResource(R.drawable.icon_up);
        } else {
            mRankTypeLl.setVisibility(View.GONE);
            mUpOrDownIv.setBackgroundResource(R.drawable.icon_down);
        }
    }

    private void setTypeColor(final int type) {
        mRankWeekTv.setTextColor(Color.parseColor("#222222"));
        mRankMonthTv.setTextColor(Color.parseColor("#222222"));
        mRankTotalTv.setTextColor(Color.parseColor("#222222"));
        switch (type) {
            case AppEnum.friendRankType.WEEK:
                mRankWeekTv.setTextColor(Color.parseColor("#f06522"));
                break;
            case AppEnum.friendRankType.MONTH:
                mRankMonthTv.setTextColor(Color.parseColor("#f06522"));
                break;
            case AppEnum.friendRankType.TOTAL:
                mRankTotalTv.setTextColor(Color.parseColor("#f06522"));
                break;
        }
    }

    @Override
    @OnClick({R.id.ll_set_rank_type, R.id.tv_week_rank, R.id.tv_month_rank, R.id.tv_total_rank, R.id.fl_base})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_set_rank_type:
                if (isTypeLlShow) {
                    isTypeLlShow = false;
                    setIsTypeShow();
                } else {
                    isTypeLlShow = true;
                    setIsTypeShow();
                    setTypeColor(rankTypeInt);
                }
                break;
            case R.id.tv_week_rank:
                if (isTypeLlShow) {
                    mRankTime.setText("您本周在");
                    isTypeLlShow = false;
                    rankTypeInt = AppEnum.friendRankType.WEEK;
                    mRankTypeTv.setText("本周");
                    setIsTypeShow();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_fragment_contaner, FriendRankingWeekFragment.newInstance(AppEnum.friendRankType.WEEK));
                    transaction.commit();
                }
                break;
            case R.id.tv_month_rank:
                if (isTypeLlShow) {
                    mRankTime.setText("您本月在");
                    isTypeLlShow = false;
                    rankTypeInt = AppEnum.friendRankType.MONTH;
                    mRankTypeTv.setText("本月");
                    setIsTypeShow();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_fragment_contaner, FriendRankingWeekFragment.newInstance(AppEnum.friendRankType.MONTH));
                    transaction.commit();
                }
                break;
            case R.id.tv_total_rank:
                if (isTypeLlShow) {
                    mRankTime.setText("您总计在");
                    isTypeLlShow = false;
                    rankTypeInt = AppEnum.friendRankType.TOTAL;
                    mRankTypeTv.setText("总计");
                    setIsTypeShow();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fl_fragment_contaner, FriendRankingWeekFragment.newInstance(AppEnum.friendRankType.TOTAL));
                    transaction.commit();
                }
                break;
            case R.id.fl_base:
                if (isTypeLlShow) {
                    isTypeLlShow = false;
                    setIsTypeShow();
                }
                break;
        }
    }
}
