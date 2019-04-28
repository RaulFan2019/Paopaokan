package com.app.pao.activity.settings;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.app.pao.R;
import com.app.pao.activity.BaseAppCompActivity;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.entity.network.GetPersonRecordResult;
import com.app.pao.utils.NumUtils;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by LY on 2016/3/11.
 */
@ContentView(R.layout.activity_mypb)
public class MypbActivity extends BaseAppCompActivity {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式

    @ViewInject(R.id.toolbar)
    private Toolbar mToolbar;//标题栏
    @ViewInject(R.id.iv_maxlength_medal)
    private ImageView mMaxLengthMedalIv;//最长距离奖牌图像
    @ViewInject(R.id.tv_maxlength_ranking)
    private TextView mMaxLengthRankingTv;//最长距离排名范围文本
    @ViewInject(R.id.tv_maxlength_value)
    private TextView mMaxLengthValueTv;//最长距离文本
    @ViewInject(R.id.tv_maxlength_time)
    private TextView mMaxLengthTimeTv;//最长距离时间文本
    @ViewInject(R.id.iv_maxtime_medal)
    private ImageView mMaxTimeMedalIv;//最长时间奖牌图像
    @ViewInject(R.id.tv_maxtime_ranking)
    private TextView mMaxTimeRankingTv;//最长时间排名范围文本
    @ViewInject(R.id.tv_maxtime_value)
    private TextView mMaxTimeValueTv;//最长时间文本
    @ViewInject(R.id.tv_maxtime_time)
    private TextView mMaxTimeTimeTv;//最长时间时间文本
    @ViewInject(R.id.iv_maxpace_medal)
    private ImageView mMaxPaceMedalIv;//最快配速奖牌图像
    @ViewInject(R.id.tv_maxpace_ranking)
    private TextView mMaxPaceRankingTv;//最快配速排名范围文本
    @ViewInject(R.id.tv_maxpace_value)
    private TextView mMaxPaceValueTv;//最快配速文本
    @ViewInject(R.id.tv_maxpace_time)
    private TextView mMaxPaceTimeTv;//最快配速时间文本（下同）
    @ViewInject(R.id.iv_5fast_medal)
    private ImageView m5FastMedalIv;//最快5公里奖牌图像
    @ViewInject(R.id.tv_5fast_ranking)
    private TextView m5FastRankingTv;
    @ViewInject(R.id.tv_5fast_value)
    private TextView m5FastValueTv;
    @ViewInject(R.id.tv_5fast_time)
    private TextView m5FastTimeTv;
    @ViewInject(R.id.iv_10fast_medal)//最快10公里奖牌图像
    private ImageView m10FastMedalIv;
    @ViewInject(R.id.tv_10fast_ranking)
    private TextView m10FastRankingTv;
    @ViewInject(R.id.tv_10fast_value)
    private TextView m10FastValueTv;
    @ViewInject(R.id.tv_10fast_time)
    private TextView m10FastTimeTv;
    @ViewInject(R.id.iv_halffast_medal)//最快半程马拉松奖牌图像
    private ImageView mHalfFastMedalIv;
    @ViewInject(R.id.tv_halffast_ranking)
    private TextView mHalfFastRankingTv;
    @ViewInject(R.id.tv_halffast_value)
    private TextView mHalfFastValueTv;
    @ViewInject(R.id.tv_halffast_time)
    private TextView mHalfFastTimeTv;
    @ViewInject(R.id.iv_holefast_medal)//最快全程马拉松奖牌图像
    private ImageView mHoleFastMedalIv;
    @ViewInject(R.id.tv_holefast_ranking)
    private TextView mHoleFastRankingTv;
    @ViewInject(R.id.tv_holefast_value)
    private TextView mHoleFastValueTv;
    @ViewInject(R.id.tv_holefast_time)
    private TextView mHoleFastTimeTv;
    private GetPersonRecordResult mPersonRecordEntity;//跑步信息

    @Override
    protected void initData() {
        mPersonRecordEntity = (GetPersonRecordResult) getIntent().getExtras().getSerializable("mPersonRecordEntity");
    }

    @Override
    protected void initViews() {
        setSupportActionBar(mToolbar);
//        mDialogBuilder.showProgressDialog(mContext, "正在加载...", false, true);
//        mDialogBuilder.setListener(new MyDialogBuilderV1.ProgressDilaogListener(){
//
//            @Override
//            public void onCancelBtnClick() {
//
//            }
//
//            @Override
//            public void onCancel() {
//                finish();
//            }
//        });
    }

    @Override
    protected void doMyOnCreate() {
        updateRunningView();
    }

    private void updateRunningView() {
        //最长距离栏的设置
        if (mPersonRecordEntity.getLongest().getLength() == 0) {
            mMaxLengthValueTv.setText("未完成");
            mMaxLengthTimeTv.setVisibility(View.GONE);
            mMaxLengthRankingTv.setVisibility(View.GONE);
        } else {
            mMaxLengthValueTv.setText(NumUtils.retainTheDecimal(mPersonRecordEntity.getLongest().getLength()) + "公里");
            mMaxLengthValueTv.setTextColor(Color.parseColor("#222222"));
            setRankTvAndRankIv(mPersonRecordEntity.getLongest().getRanking(), mMaxLengthRankingTv, mMaxLengthMedalIv);
            try {
                mMaxLengthTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity.getLongest()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //最长时间
        if (mPersonRecordEntity.getMaxduration().getLength() == 0) {
            mMaxTimeValueTv.setText("未完成");
            mMaxTimeTimeTv.setVisibility(View.GONE);
            mMaxTimeRankingTv.setVisibility(View.GONE);
        } else {
            mMaxTimeValueTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getMaxduration().getDuration()));
            mMaxTimeValueTv.setTextColor(Color.parseColor("#222222"));
            setRankTvAndRankIv(mPersonRecordEntity.getMaxduration().getRanking(), mMaxTimeRankingTv, mMaxTimeMedalIv);
            try {
                mMaxTimeTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity.getMaxduration()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //最快配速
        if (mPersonRecordEntity.getFastestpace().getLength() == 0) {
            mMaxPaceValueTv.setText("未完成");
            mMaxPaceTimeTv.setVisibility(View.GONE);
            mMaxPaceRankingTv.setVisibility(View.GONE);
        } else {
            mMaxPaceValueTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastestpace().getPace()) + "/公里");
            mMaxPaceValueTv.setTextColor(Color.parseColor("#222222"));
            setRankTvAndRankIv(mPersonRecordEntity.getFastestpace().getRanking(), mMaxPaceRankingTv, mMaxPaceMedalIv);
            try {
                mMaxPaceTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity.getFastestpace()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //5公里最快
        if (mPersonRecordEntity.getFastest5().getLength() == 0) {
            m5FastValueTv.setText("未完成");
            m5FastTimeTv.setVisibility(View.GONE);
            m5FastRankingTv.setVisibility(View.GONE);
        } else {
            m5FastValueTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastest5().getDuration()));
            m5FastValueTv.setTextColor(Color.parseColor("#222222"));
            setRankTvAndRankIv(mPersonRecordEntity.getFastest5().getRanking(), m5FastRankingTv, m5FastMedalIv);
            try {
                m5FastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity.getFastest5()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //10公里最快
        if (mPersonRecordEntity.getFastest10().getLength() == 0) {
            m10FastValueTv.setText("未完成");
            m10FastTimeTv.setVisibility(View.GONE);
            m10FastRankingTv.setVisibility(View.GONE);
        } else {
            m10FastValueTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastest10().getDuration()));
            m10FastValueTv.setTextColor(Color.parseColor("#222222"));
            setRankTvAndRankIv(mPersonRecordEntity.getFastest10().getRanking(), m10FastRankingTv, m10FastMedalIv);
            try {
                m10FastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity.getFastest10()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //半程最快
        if (mPersonRecordEntity.getFastesthalfmarathon().getLength() == 0) {
            mHalfFastValueTv.setText("未完成");
            mHalfFastTimeTv.setVisibility(View.GONE);
            mHalfFastRankingTv.setVisibility(View.GONE);
        } else {
            mHalfFastValueTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastesthalfmarathon().getDuration
                    ()));
            mHalfFastValueTv.setTextColor(Color.parseColor("#222222"));
            setRankTvAndRankIv(mPersonRecordEntity.getFastesthalfmarathon().getRanking(), mHalfFastRankingTv, mHalfFastMedalIv);
            try {
                mHalfFastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity
                        .getFastesthalfmarathon()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //全程最快
        if (mPersonRecordEntity.getFastestfullmarathon().getLength() == 0) {
            mHoleFastValueTv.setText("未完成");
            mHoleFastTimeTv.setVisibility(View.GONE);
            mHoleFastRankingTv.setVisibility(View.GONE);
        } else {
            mHoleFastValueTv.setText(TimeUtils.formatVoiceString(mPersonRecordEntity.getFastestfullmarathon().getDuration
                    ()));
            mHoleFastValueTv.setTextColor(Color.parseColor("#222222"));
            setRankTvAndRankIv(mPersonRecordEntity.getFastestfullmarathon().getRanking(), mHoleFastRankingTv, mHoleFastMedalIv);
            try {
                mHoleFastTimeTv.setText(TimeUtils.getTimestampString(df.parse(mPersonRecordEntity
                        .getFastestfullmarathon()
                        .getStarttime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void setRankTvAndRankIv(int rank, TextView rankTv, ImageView rankIv) {
        rankTv.setText("战胜了" + rank + "%的人");
        if (rank >= 90) {
            rankIv.setBackgroundResource(R.drawable.icon_big_gold);
        } else if (rank >= 70) {
            rankIv.setBackgroundResource(R.drawable.icon_big_silver);
        } else if (rank >= 40) {
            rankIv.setBackgroundResource(R.drawable.icon_big_copper);
        } else {
            rankIv.setBackgroundResource(R.drawable.icon_big_iron);
        }
    }

    @Override
    protected void updateData() {

    }

    @Override
    protected void updateViews() {

    }

    @Override
    protected void destroy() {

    }

    /**
     * 重写Toolbar方法
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ll_max_length, R.id.ll_max_time, R.id.ll_max_pace, R.id
            .ll_5_fast, R.id.ll_10_fast, R.id.ll_half_fast, R.id.ll_full_fast})
    public void onClick(View v) {
        switch (v.getId()) {
            //最长距离
            case R.id.ll_max_length:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getLongest().getLength() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutname", mPersonRecordEntity.getLongest().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getLongest().getWorkoutid());
                    startActivity(HistoryInfoActivityV2.class, bundle);
                }
                break;
            //最长时间
            case R.id.ll_max_time:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getMaxduration().getLength() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutname", mPersonRecordEntity.getMaxduration().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getMaxduration().getWorkoutid());
                    startActivity(HistoryInfoActivityV2.class, bundle);
                }
                break;
            // 最快配速
            case R.id.ll_max_pace:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastestpace().getLength() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutname", mPersonRecordEntity.getFastestpace().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastestpace().getWorkoutid());
                    startActivity(HistoryInfoActivityV2.class, bundle);
                }
                break;
            //最快5公里
            case R.id.ll_5_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastest5().getLength() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutname", mPersonRecordEntity.getFastest5().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastest5().getWorkoutid());
                    startActivity(HistoryInfoActivityV2.class, bundle);
                }
                break;
            //最快10公里
            case R.id.ll_10_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastest10().getLength() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutname", mPersonRecordEntity.getFastest10().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastest10().getWorkoutid());
                    startActivity(HistoryInfoActivityV2.class, bundle);
                }
                break;
            //最快半程
            case R.id.ll_half_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastesthalfmarathon().getLength() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutname", mPersonRecordEntity.getFastesthalfmarathon().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastesthalfmarathon().getWorkoutid());
                    startActivity(HistoryInfoActivityV2.class, bundle);
                }
                break;
            //最快全程
            case R.id.ll_full_fast:
                if (mPersonRecordEntity != null && mPersonRecordEntity.getFastestfullmarathon().getLength() != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("workoutname", mPersonRecordEntity.getFastestfullmarathon().getStarttime());
                    bundle.putInt("workoutid", mPersonRecordEntity.getFastestfullmarathon().getWorkoutid());
                    startActivity(HistoryInfoActivityV2.class, bundle);
                }
                break;
        }
    }
}
