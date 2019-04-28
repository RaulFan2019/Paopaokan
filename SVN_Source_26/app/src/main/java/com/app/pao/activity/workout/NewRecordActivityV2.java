package com.app.pao.activity.workout;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.adapter.NewRecordAdapter;
import com.app.pao.config.AppEnum;
import com.app.pao.data.db.RecordData;
import com.app.pao.entity.db.DBEntityRecord;
import com.app.pao.entity.model.UserOptMedalEntity;
import com.app.pao.ui.widget.CircleFlowIndicator;
import com.app.pao.ui.widget.ViewFlow;
import com.app.pao.utils.TTsUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul.Fan on 2016/5/18.
 */
@ContentView(R.layout.activity_new_record_v2)
public class NewRecordActivityV2 extends BaseAppCompActivity implements View.OnClickListener {

    /* local data */
    private List<DBEntityRecord> mRecordList = new ArrayList<>();//记录列表


    /* local view */
    @ViewInject(R.id.v_gender)
    private View mGenderV;//性别背景
    @ViewInject(R.id.v_medal)
    private View mMedalV;//奖牌图标
    @ViewInject(R.id.vf_medal_change)
    private ViewFlow mMedalChangeVf;//新记录滑动页面
    @ViewInject(R.id.cfi_medal_change_point)
    private CircleFlowIndicator mPointCfi;//新纪录滑动对应点标识


    @Override
    @OnClick(R.id.v_finish)
    public void onClick(View v) {
        if (v.getId() == R.id.v_finish) {
            finish();
        }
    }


    @Override
    protected void initData() {
        int userId = LocalApplication.getInstance().getLoginUser(mContext).userId;
        mRecordList = RecordData.getRecords(mContext, userId);
        RecordData.deleteOldRecord(mContext, userId);
        //TEST
//        DBEntityRecord entityRecord1 = new DBEntityRecord("1", 1, 1, 23, 30, 30, 10);
//        DBEntityRecord entityRecord2 = new DBEntityRecord("2", 1, 2, 23, 50, 40, 20);
//        DBEntityRecord entityRecord3 = new DBEntityRecord("3", 1, 3, 23, 32, 50, 30);
//        DBEntityRecord entityRecord4 = new DBEntityRecord("4", 1, 4, 23, 65, 60, 40);
//        DBEntityRecord entityRecord5 = new DBEntityRecord("5", 1, 5, 23, 85, 70, 50);
//        DBEntityRecord entityRecord6 = new DBEntityRecord("6", 1, 6, 23, 115, 86, 60);
//        DBEntityRecord entityRecord7 = new DBEntityRecord("7", 1, 7, 23, 75, 980, 70);
//
//        mRecordList.add(entityRecord1);
//        mRecordList.add(entityRecord2);
//        mRecordList.add(entityRecord3);
//        mRecordList.add(entityRecord4);
//        mRecordList.add(entityRecord5);
//        mRecordList.add(entityRecord6);
//        mRecordList.add(entityRecord7);

        CheckNewData = false;

    }

    @Override
    protected void initViews() {
        //背景
        if (LocalApplication.getInstance().getLoginUser(mContext).gender == AppEnum.UserGander.MAN) {
            mGenderV.setBackgroundResource(R.drawable.bg_new_record_men);
        } else {
            mGenderV.setBackgroundResource(R.drawable.bg_new_record_women);
        }
        //View Flow
        mPointCfi.setFillColor(getResources().getColor(R.color.grey_check));
        mPointCfi.setStrokeColor(getResources().getColor(R.color.grey_uncheck));
        mPointCfi.setInactiveType(CircleFlowIndicator.STYLE_FILL);
        mMedalChangeVf.setmSideBuffer(mRecordList.size());
        mMedalChangeVf.setAdapter(new NewRecordAdapter(mContext, mRecordList));
        mMedalChangeVf.setFlowIndicator(mPointCfi);
        mMedalChangeVf.setOnViewSwitchListener(new ViewFlow.ViewSwitchListener() {
            @Override
            public void onSwitched(View view, int position) {
                startMedalAnim(getMedalRes(mRecordList.get(position)));
            }
        });

        mMedalV.setBackgroundResource(getMedalRes(mRecordList.get(0)));
    }

    @Override
    protected void doMyOnCreate() {
        TTsUtils.playNewRecord(mContext);
    }


    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {
        mRecordList.clear();
    }

    /**
     * 获取奖牌的资源文件
     *
     * @param mMedal
     * @return
     */
    private int getMedalRes(DBEntityRecord mMedal) {
        int mMedalRes = R.drawable.icon_gold_longest_big;
        switch (mMedal.type) {
            //最长距离
            case UserOptMedalEntity.TYPE_LONGEST:
                if (mMedal.percentage >= 90) {
                    mMedalRes = R.drawable.icon_gold_longest_big;
                } else if (mMedal.percentage >= 70) {
                    mMedalRes = R.drawable.icon_silver_longest_big;
                } else if (mMedal.percentage >= 40) {
                    mMedalRes = R.drawable.icon_copper_longest_big;
                } else {
                    mMedalRes = R.drawable.icon_iron_longest_big;
                }
                break;
            //最长时间
            case UserOptMedalEntity.TYPE_MAX_DURATION:
                if (mMedal.percentage >= 90) {
                    mMedalRes = R.drawable.icon_gold_max_duration_big;
                } else if (mMedal.percentage >= 70) {
                    mMedalRes = R.drawable.icon_silver_max_duration_big;
                } else if (mMedal.percentage >= 40) {
                    mMedalRes = R.drawable.icon_copper_max_duration_big;
                } else {
                    mMedalRes = R.drawable.icon_iron_max_duration_big;
                }
                break;
            //最快配速
            case UserOptMedalEntity.TYPE_FASTEST_PACE:
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_pace_big;
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_pace_big;
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_pace_big;
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_pace_big;
                }
                break;
            //5公里最快
            case UserOptMedalEntity.TYPE_FASTEST_5:
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_5_big;
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_5_big;
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_5_big;
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_5_big;
                }
                break;
            //10公里最快
            case UserOptMedalEntity.TYPE_FASTEST_10:
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_fastest_10_big;
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_fastest_10_big;
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_fastest_10_big;
                } else {
                    mMedalRes = R.drawable.icon_iron_fastest_10_big;
                }
                break;
            //半马最快
            case UserOptMedalEntity.TYPE_FASTEST_HALF_MARATHON:
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_half_marathon_big;
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_half_marathon_big;
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_half_marathon_big;
                } else {
                    mMedalRes = R.drawable.icon_iron_half_marathon_big;
                }
                break;
            //全马最快
            case UserOptMedalEntity.TYPE_FASTEST_FULL_MARATHON:
                if (mMedal.getPercentage() >= 90) {
                    mMedalRes = R.drawable.icon_gold_full_marathon_big;
                } else if (mMedal.getPercentage() >= 70) {
                    mMedalRes = R.drawable.icon_silver_full_marathon_big;
                } else if (mMedal.getPercentage() >= 40) {
                    mMedalRes = R.drawable.icon_copper_full_marathon_big;
                } else {
                    mMedalRes = R.drawable.icon_iron_full_marathon_big;
                }
                break;
        }
        return mMedalRes;
    }

    /**
     * 奖牌变化动画
     *
     * @param res
     */
    private void startMedalAnim(final int res) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation1 = new AlphaAnimation(1, 0);
        alphaAnimation1.setDuration(500);
        animationSet.addAnimation(alphaAnimation1);
        mMedalV.startAnimation(animationSet);
        alphaAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mMedalV.setBackgroundResource(res);
                AlphaAnimation alphaAnimation1 = new AlphaAnimation(0, 1);
                alphaAnimation1.setDuration(500);
//                TranslateAnimation translateAnimation2 = new TranslateAnimation(0, 0, -DeviceUtils.dpToPixel(470)/3, 0);
//                translateAnimation2.setDuration(500);
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, 1.0f, 0.1f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(500);
                AnimationSet animationSet2 = new AnimationSet(true);
                animationSet2.addAnimation(alphaAnimation1);
                animationSet2.addAnimation(scaleAnimation);
                mMedalV.startAnimation(animationSet2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
