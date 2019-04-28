package com.app.pao.fragment.history;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.app.pao.LocalApplication;
import com.app.pao.R;
import com.app.pao.activity.main.RaceWebViewActivity;
import com.app.pao.activity.workout.HistoryInfoActivityV2;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.HeartrateData;
import com.app.pao.entity.db.DBEntityHeartrate;
import com.app.pao.entity.db.DBEntityWorkout;
import com.app.pao.entity.model.HistoryHeartrateEntity;
import com.app.pao.entity.model.HistoryInfoEntity;
import com.app.pao.entity.model.PieChartEntity;
import com.app.pao.fragment.BaseFragment;
import com.app.pao.network.api.MyRequestCallBack;
import com.app.pao.network.utils.RequestParamsBuild;
import com.app.pao.ui.widget.HeartRateChartViewReplace;
import com.app.pao.ui.widget.MyFriendHeartRateDountBigChartView;
import com.app.pao.ui.widget.MyFriendHeartRateDountChartView;
import com.app.pao.ui.widget.chart.FriendHeartRateChartViewReplace;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rey.material.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 2015/11/27.
 * 跑步历史 - 心率页面
 */
public class FriendHistoryHeartrateInfoFragmentV2 extends BaseFragment implements View.OnClickListener {

    /**
     * contains
     **/
    private static final int FOCUS_STATUS_0_89 = 0;
    private static final int FOCUS_STATUS_90_108 = 1;
    private static final int FOCUS_STATUS_109_126 = 2;
    private static final int FOCUS_STATUS_127_144 = 3;
    private static final int FOCUS_STATUS_145_162 = 4;
    private static final int FOCUS_STATUS_163_180 = 5;

    /**
     * local data
     **/
    private int count0_89 = 0;
    private int count90_108 = 0;
    private int count109_126 = 0;
    private int count127_144 = 0;
    private int count145_162 = 0;
    private int count163 = 0;
    private int totalHeartCount = 0;
    private int avgHeartrate = 0;
    private int maxHeartrate = 0;

    int weight0_89;
    int weight90_108;
    int weight109_126;
    int weight127_144;
    int weight145_162;
    int weight163_180;


    int MinHR;// 心率计算标准（Min）
    int MaxHR;// 心率计算标准 （Max）

    private long duration;
    private List<HistoryHeartrateEntity> mheartrateList = new ArrayList<HistoryHeartrateEntity>();
    private List<HistoryHeartrateEntity> mDrawList = new ArrayList<HistoryHeartrateEntity>();
    List<PieChartEntity> mDatas = new ArrayList<PieChartEntity>();//用于显示环形图的数据
    private int mDrawStep;//绘画阶梯

    private int mFocusStatus;
    private int mAge;
    private int minHeartrate = 0;
    private int highHeartrate = 0;

    private Typeface typeFace;//字体

    private String mNoneLink;//没有心率的链接

    @ViewInject(R.id.ll_none)
    private LinearLayout mNoneLl;

    @ViewInject(R.id.ll_line_chart)
    private LinearLayout mLineChartLl;// 加载line chart
    @ViewInject(R.id.fl_chart)
    private FrameLayout mChartFl;// chart 父承载

    @ViewInject(R.id.ll_round_chart)
    private LinearLayout mDountChartLl;// 加载line chart
    @ViewInject(R.id.fl_round_chart)
    private FrameLayout mDountChartFl;// chart 父承载
    @ViewInject(R.id.ll_big_round_chart)
    private LinearLayout mBigDountChartLl;//加载大的Pie chart

    @ViewInject(R.id.tv_max_heart)
    private TextView mMaxHeart;// 最大心率值
    @ViewInject(R.id.tv_avg_heart)
    private TextView mAvgHeart;// 平均心率值
    @ViewInject(R.id.tv_learn_more)
    private android.widget.TextView mLinkTv;

    @ViewInject(R.id.tv_range_buttom_0_89)
    private TextView mRangeTv0_89;
    @ViewInject(R.id.tv_range_buttom_90_108)
    private TextView mRangeTv90_108;
    @ViewInject(R.id.tv_range_buttom_109_126)
    private TextView mRangeTv109_126;
    @ViewInject(R.id.tv_range_buttom_127_144)
    private TextView mRangeTv127_144;
    @ViewInject(R.id.tv_range_buttom_145_162)
    private TextView mRangeTv145_162;
    @ViewInject(R.id.tv_range_buttom_163)
    private TextView mRangeTv163_180;

    /* 心率占比例文本 */
    @ViewInject(R.id.tv_tp_buttom_0_89)
    private TextView mButtomTv0_89;
    @ViewInject(R.id.tv_tp_buttom_90_108)
    private TextView mButtomTv90_108;
    @ViewInject(R.id.tv_tp_buttom_109_126)
    private TextView mButtomTv109_126;
    @ViewInject(R.id.tv_tp_buttom_127_144)
    private TextView mButtomTv127_144;
    @ViewInject(R.id.tv_tp_buttom_145_162)
    private TextView mButtomTv145_162;
    @ViewInject(R.id.tv_tp_buttom_163_180)
    private TextView mButtomTv163_180;

    /* 心率描述文本 */
    @ViewInject(R.id.tv_tip_buttom_0_89)
    private TextView mButtomTipTv0_89;
    @ViewInject(R.id.tv_tip_buttom_90_108)
    private TextView mButtomTipTv90_108;
    @ViewInject(R.id.tv_tip_buttom_109_126)
    private TextView mButtomTipTv109_126;
    @ViewInject(R.id.tv_tip_buttom_127_144)
    private TextView mButtomTipTv127_144;
    @ViewInject(R.id.tv_tip_buttom_145_162)
    private TextView mButtomTipTv145_162;
    @ViewInject(R.id.tv_tip_buttom_163_180)
    private TextView mButtomTipTv163_180;


    /* 点击改变焦点的layout */
    @ViewInject(R.id.ll_tp_0_89)
    private LinearLayout mButtomLl0_89;
    @ViewInject(R.id.ll_tp_90_108)
    private LinearLayout mButtomLl90_108;
    @ViewInject(R.id.ll_tp_109_126)
    private LinearLayout mButtomLl109_126;
    @ViewInject(R.id.ll_tp_127_144)
    private LinearLayout mButtomLl127_144;
    @ViewInject(R.id.ll_tp_145_162)
    private LinearLayout mButtomLl145_162;
    @ViewInject(R.id.ll_tp_163_180)
    private LinearLayout mButtomLl163_180;

    /* 心率焦点数据展示 */
    @ViewInject(R.id.tv_heartrate_tip)
    private TextView mTipTv;//心率状态提示文本
    @ViewInject(R.id.tv_heartrate_time)
    private TextView mTimeTv;//心率状态时间文本
    @ViewInject(R.id.tv_heartrate_per)
    private TextView mPerTv;//心率状态百分比文本


    private FriendHeartRateChartViewReplace mLineChartView;
    private MyFriendHeartRateDountChartView mDountChartView;
    private MyFriendHeartRateDountBigChartView mBigDountChartView;

    public static FriendHistoryHeartrateInfoFragmentV2 newInstance() {
        FriendHistoryHeartrateInfoFragmentV2 fragment = new FriendHistoryHeartrateInfoFragmentV2();
        return fragment;
    }


    @OnClick({R.id.ll_tp_0_89, R.id.ll_tp_90_108, R.id.ll_tp_109_126, R.id.ll_tp_127_144, R.id.ll_tp_145_162,
            R.id.ll_tp_163_180, R.id.tv_learn_more})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_learn_more) {
            onClickLearnMore();
        } else {
            switch (v.getId()) {
                case R.id.ll_tp_0_89:
                    mFocusStatus = FOCUS_STATUS_0_89;
                    break;
                case R.id.ll_tp_90_108:
                    mFocusStatus = FOCUS_STATUS_90_108;
                    break;
                case R.id.ll_tp_109_126:
                    mFocusStatus = FOCUS_STATUS_109_126;
                    break;
                case R.id.ll_tp_127_144:
                    mFocusStatus = FOCUS_STATUS_127_144;
                    break;
                case R.id.ll_tp_145_162:
                    mFocusStatus = FOCUS_STATUS_145_162;
                    break;
                case R.id.ll_tp_163_180:
                    mFocusStatus = FOCUS_STATUS_163_180;
                    break;
            }
            updateFouceView();
            updatePieView(mFocusStatus);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_history_heartrate_info;
    }

    @Override
    protected void initParams() {
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/TradeGothicLTStd-BdCn20.otf");
        mPerTv.setTypeface(typeFace);
        mAvgHeart.setTypeface(typeFace);
        mMaxHeart.setTypeface(typeFace);
    }

    public void updateViewFromActivity(final HistoryInfoEntity mHistoryInfo) {
        initData(mHistoryInfo);
        Log.v(TAG, "mheartrateList.size" + mheartrateList.size());
        if (mheartrateList == null || mheartrateList.size() == 0) {
            mNoneLl.setVisibility(View.VISIBLE);
            postGetNoneHeartbeatLink();
            return;
        }
        mNoneLl.setVisibility(View.GONE);
        initViews();
        setUpChartView();
        updatePieView(mFocusStatus);
        mLineChartLl.addView(mLineChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mDountChartLl.addView(mDountChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        updateFouceView();

    }

    private void setUpChartView() {
        mDountChartView = new MyFriendHeartRateDountChartView(mContext);
        mDountChartView.initView(weight0_89, weight90_108, weight109_126, weight127_144, weight145_162, weight163_180);

        mLineChartView = new FriendHeartRateChartViewReplace(mContext);
        mLineChartView.initViews(mheartrateList.get(mheartrateList.size() - 1).getTimeofset(), minHeartrate,
                highHeartrate);
        mLineChartView.setEnabled(false);
        drawLineChartAnim();
    }

    /**
     * 显示曲线的动画
     */
    private void drawLineChartAnim() {
        for (int i = 0; i < mDrawList.size() - 3; ) {
            List<HistoryHeartrateEntity> tempList = new ArrayList<HistoryHeartrateEntity>();
            tempList = mDrawList.subList(i, i + 3);
            mLineChartView.addLine(tempList, getColorByHeartrate(tempList.get
                    (0).getBmp()), false);
            i = i + 2;
        }
        mLineChartView.addLine(new ArrayList<HistoryHeartrateEntity>(), Color.TRANSPARENT, true);
    }


    private int getColorByHeartrate(int bmp) {
        if (bmp < (MaxHR * 0.5)) {
            return Color.parseColor("#c1c1c1");
        }
        if (bmp < (MaxHR * 0.6)) {
            return Color.parseColor("#ffe569");
        }
        if (bmp < (MaxHR * 0.7)) {
            return Color.parseColor("#ffa200");
        }
        if (bmp < (MaxHR * 0.8)) {
            return Color.parseColor("#f07422");
        }
        if (bmp < (MaxHR * 0.9)) {
            return Color.parseColor("#ee5431");
        } else {
            return Color.parseColor("#de2121");
        }

    }

    /**
     * 设置焦点界面
     */
    private void updateFouceView() {
        mButtomLl0_89.setBackgroundColor(Color.TRANSPARENT);
        mButtomLl90_108.setBackgroundColor(Color.TRANSPARENT);
        mButtomLl109_126.setBackgroundColor(Color.TRANSPARENT);
        mButtomLl127_144.setBackgroundColor(Color.TRANSPARENT);
        mButtomLl145_162.setBackgroundColor(Color.TRANSPARENT);
        mButtomLl163_180.setBackgroundColor(Color.TRANSPARENT);

        mButtomTv0_89.setTypeface(Typeface.DEFAULT);
        mButtomTv90_108.setTypeface(Typeface.DEFAULT);
        mButtomTv109_126.setTypeface(Typeface.DEFAULT);
        mButtomTv127_144.setTypeface(Typeface.DEFAULT);
        mButtomTv145_162.setTypeface(Typeface.DEFAULT);
        mButtomTv163_180.setTypeface(Typeface.DEFAULT);

        /* 心率描述文本 */
        mButtomTipTv0_89.setTypeface(Typeface.DEFAULT);
        mButtomTipTv90_108.setTypeface(Typeface.DEFAULT);
        mButtomTipTv109_126.setTypeface(Typeface.DEFAULT);
        mButtomTipTv127_144.setTypeface(Typeface.DEFAULT);
        mButtomTipTv145_162.setTypeface(Typeface.DEFAULT);
        mButtomTipTv163_180.setTypeface(Typeface.DEFAULT);

        switch (mFocusStatus) {
            case FOCUS_STATUS_0_89:
                mButtomLl0_89.setBackgroundColor(Color.parseColor("#f3f3f3"));
                mButtomTv0_89.setTypeface(Typeface.DEFAULT_BOLD);
                mButtomTipTv0_89.setTypeface(Typeface.DEFAULT_BOLD);
                mTipTv.setText("非锻炼");
                mTipTv.setTextColor(Color.parseColor("#c1c1c1"));
                mTimeTv.setText(TimeUtils.formatDurationStr(duration * weight0_89 / 100));
                mPerTv.setText(weight0_89 + "");
                break;
            case FOCUS_STATUS_90_108:
                mButtomLl90_108.setBackgroundColor(Color.parseColor("#f3f3f3"));
                mButtomTv90_108.setTypeface(Typeface.DEFAULT_BOLD);
                mButtomTipTv90_108.setTypeface(Typeface.DEFAULT_BOLD);
                mTipTv.setText("热身");
                mTipTv.setTextColor(Color.parseColor("#ffe569"));
                mTimeTv.setText(TimeUtils.formatDurationStr(duration * weight90_108 /
                        100));
                mPerTv.setText(weight90_108 + "");
                break;
            case FOCUS_STATUS_109_126:
                mButtomLl109_126.setBackgroundColor(Color.parseColor("#f3f3f3"));
                mButtomTv109_126.setTypeface(Typeface.DEFAULT_BOLD);
                mButtomTipTv109_126.setTypeface(Typeface.DEFAULT_BOLD);
                mTipTv.setText("燃脂");
                mTipTv.setTextColor(Color.parseColor("#ffa200"));
                mTimeTv.setText(TimeUtils.formatDurationStr(duration * weight109_126 /
                        100));
                mPerTv.setText(weight109_126 + "");
                break;
            case FOCUS_STATUS_127_144:
                mButtomLl127_144.setBackgroundColor(Color.parseColor("#f3f3f3"));
                mButtomTv127_144.setTypeface(Typeface.DEFAULT_BOLD);
                mButtomTipTv127_144.setTypeface(Typeface.DEFAULT_BOLD);
                mTipTv.setText("有氧");
                mTipTv.setTextColor(Color.parseColor("#f07422"));
                mTimeTv.setText(TimeUtils.formatDurationStr(duration * weight127_144 /
                        100));
                mPerTv.setText(weight127_144 + "");
                break;
            case FOCUS_STATUS_145_162:
                mButtomLl145_162.setBackgroundColor(Color.parseColor("#f3f3f3"));
                mButtomTv145_162.setTypeface(Typeface.DEFAULT_BOLD);
                mButtomTipTv145_162.setTypeface(Typeface.DEFAULT_BOLD);
                mTipTv.setText("无氧");
                mTipTv.setTextColor(Color.parseColor("#ee5431"));
                mTimeTv.setText(TimeUtils.formatDurationStr(duration * weight145_162 /
                        100));
                mPerTv.setText(weight145_162 + "");
                break;
            case FOCUS_STATUS_163_180:
                mButtomLl163_180.setBackgroundColor(Color.parseColor("#f3f3f3"));
                mButtomTv163_180.setTypeface(Typeface.DEFAULT_BOLD);
                mButtomTipTv163_180.setTypeface(Typeface.DEFAULT_BOLD);
                mTipTv.setText("艰辛");
                mTipTv.setTextColor(Color.parseColor("#de2121"));
                mTimeTv.setText(TimeUtils.formatDurationStr(duration * weight163_180 /
                        100));
                mPerTv.setText(weight163_180 + "");
                break;
        }
    }

    private void updatePieView(int focus) {
        mDatas = new ArrayList<PieChartEntity>();
        mDatas.add(new PieChartEntity(weight0_89, Color.parseColor("#00000000")));
        mDatas.add(new PieChartEntity(weight90_108, Color.parseColor("#00000000")));
        mDatas.add(new PieChartEntity(weight109_126, Color.parseColor("#00000000")));
        mDatas.add(new PieChartEntity(weight127_144, Color.parseColor("#00000000")));
        mDatas.add(new PieChartEntity(weight145_162, Color.parseColor("#00000000")));
        mDatas.add(new PieChartEntity(weight163_180, Color.parseColor("#00000000")));
        switch (focus) {
            case FOCUS_STATUS_0_89:
                mDatas.set(0, new PieChartEntity(weight0_89, Color.parseColor("#c1c1c1")));
                break;
            case FOCUS_STATUS_90_108:
                mDatas.set(1, new PieChartEntity(weight90_108, Color.parseColor("#ffe569")));
                break;
            case FOCUS_STATUS_109_126:
                mDatas.set(2, new PieChartEntity(weight109_126, Color.parseColor("#ffa200")));
                break;
            case FOCUS_STATUS_127_144:
                mDatas.set(3, new PieChartEntity(weight127_144, Color.parseColor("#f07422")));
                break;
            case FOCUS_STATUS_145_162:
                mDatas.set(4, new PieChartEntity(weight145_162, Color.parseColor("#ee5431")));
                break;
            case FOCUS_STATUS_163_180:
                mDatas.set(5, new PieChartEntity(weight163_180, Color.parseColor("#de2121")));
                break;
        }
        mBigDountChartView = new MyFriendHeartRateDountBigChartView(mContext, new MyFriendHeartRateDountBigChartView.PieListener() {
            @Override
            public void onRecordClick(int dataId) {
                mFocusStatus = dataId;
                updateFouceView();
                updatePieView(mFocusStatus);
            }
        });
        mBigDountChartView.initView(mDatas);
        mBigDountChartLl.removeAllViews();
        mBigDountChartLl.addView(mBigDountChartView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams
                .MATCH_PARENT));
    }

    /**
     * 初始化数据
     */
    private void initData(final HistoryInfoEntity mHistoryInfo) {
        mAge = TimeUtils.getAgeFromBirthDay(LocalApplication.getInstance().getLoginUser(mContext).getBirthday());
        mheartrateList = mHistoryInfo.heartrateList;
        duration = mHistoryInfo.duration;

        if (mheartrateList == null || mheartrateList.size() == 0) {
            return;
        }

        minHeartrate = mheartrateList.get(0).getBmp();
        if (mheartrateList.size() < 600) {
            mDrawStep = 3;
        } else {
            mDrawStep = mheartrateList.size() / 150;
        }
        for (int i = 0; i < mheartrateList.size(); ) {
            mDrawList.add(mheartrateList.get(i));
            i += mDrawStep;
        }

        MinHR = 70;
        MaxHR = 220 - mAge;
        mFocusStatus = 0;
        mFocusStatus = FOCUS_STATUS_0_89;

        if (mheartrateList.size() != 0) {
            // 心率计算
            for (HistoryHeartrateEntity heartrate : mheartrateList) {
                totalHeartCount += heartrate.bmp;
                if (minHeartrate > heartrate.bmp) {
                    minHeartrate = heartrate.bmp;
                }
                if (heartrate.getBmp() > highHeartrate) {
                    highHeartrate = heartrate.bmp;
                }
                if (heartrate.getBmp() > maxHeartrate) {
                    maxHeartrate = heartrate.bmp;
                }
                if (heartrate.bmp < (MaxHR * 0.5)) {
                    count0_89++;
                    continue;
                }
                if (heartrate.bmp < (MaxHR * 0.6)) {
                    count90_108++;
                    continue;
                }
                if (heartrate.bmp < (MaxHR * 0.7)) {
                    count109_126++;
                    continue;
                }
                if (heartrate.bmp < (MaxHR * 0.8)) {
                    count127_144++;
                    continue;
                }
                if (heartrate.bmp < (MaxHR * 0.9)) {
                    count145_162++;
                    continue;
                }
                count163++;
            }
            avgHeartrate = totalHeartCount / mheartrateList.size();
        }
        if (totalHeartCount != 0) {

            weight90_108 = count90_108 * 100 / mheartrateList.size();
            weight109_126 = count109_126 * 100 / mheartrateList.size();
            weight127_144 = count127_144 * 100 / mheartrateList.size();
            weight145_162 = count145_162 * 100 / mheartrateList.size();
            weight163_180 = count163 * 100 / mheartrateList.size();
            weight0_89 = 100 - (weight90_108 + weight109_126 + weight127_144 + weight145_162 + weight163_180);
        }
    }


    /**
     * 初始化页面
     */
    private void initViews() {
        mRangeTv0_89.setText("0" + "-" + (int) (MaxHR * 0.5));
        mRangeTv90_108.setText((int) (MaxHR * 0.5 + 1) + "-" + (int) (MaxHR * 0.6));
        mRangeTv109_126
                .setText((int) (MaxHR * 0.6 + 1) + "-" + (int) (MaxHR * 0.7));
        mRangeTv127_144
                .setText((int) (MaxHR * 0.7 + 1) + "-" + (int) (MaxHR * 0.8));
        mRangeTv145_162
                .setText((int) (MaxHR * 0.8 + 1) + "-" + (int) (MaxHR * 0.9));
        mRangeTv163_180
                .setText((int) (MaxHR * 0.9 + 1) + "-" + (int) (MaxHR * 1.0));

        mAvgHeart.setText(avgHeartrate + "");
        mMaxHeart.setText(maxHeartrate + "");

        mButtomTv0_89.setText(weight0_89 + "%");
        mButtomTv90_108.setText(weight90_108 + "%");
        mButtomTv109_126.setText(weight109_126 + "%");
        mButtomTv127_144.setText(weight127_144 + "%");
        mButtomTv145_162.setText(weight145_162 + "%");
        mButtomTv163_180.setText(weight163_180 + "%");
    }

    /**
     * 点击了解更多
     */
    private void onClickLearnMore() {
        Bundle bundle = new Bundle();
        bundle.putString("url", mNoneLink);
        bundle.putInt("type", AppEnum.WebViewType.AdViewInto);
        startActivity(RaceWebViewActivity.class, bundle);
    }

    /**
     * 获取点赞和评论列表
     */
    private void postGetNoneHeartbeatLink() {
        HttpUtils http = new HttpUtils();
        String POST_URL = URLConfig.URL_GET_NONE_HEARTBEAT_LINK;
        RequestParams params = RequestParamsBuild.buildGetNoneHeartbeatLinkRequest(mContext);
        http.send(HttpRequest.HttpMethod.POST, POST_URL, params, new MyRequestCallBack(mContext) {
            @Override
            protected void onErrorResponse(int errorCode, String errorMsg) {
            }

            @Override
            protected void onRightResponse(String Response) {
                try {
                    JSONObject object = new JSONObject(Response);
                    mNoneLink = object.getString("link");
                    mLinkTv.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onFailureResponse(HttpException e, String s) {

            }

            @Override
            protected void onFinish() {

            }
        });
    }

}
