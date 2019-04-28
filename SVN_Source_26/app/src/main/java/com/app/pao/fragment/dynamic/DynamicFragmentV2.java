package com.app.pao.fragment.dynamic;

import android.content.Intent;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.app.pao.R;
import com.app.pao.activity.main.MessageListActivityReplace;
import com.app.pao.activity.main.RaceWebViewActivity;
import com.app.pao.activity.main.ScanQRCodeActivityReplace;
import com.app.pao.adapter.DynamicListAdapterReplace;
import com.app.pao.config.AppEnum;
import com.app.pao.config.URLConfig;
import com.app.pao.data.db.MaidianData;
import com.app.pao.entity.adapter.DynamicLineItem;
import com.app.pao.entity.db.DBEntityMaidian;
import com.app.pao.entity.network.GetDynamicListResult;
import com.app.pao.entity.network.GetRaceListResult;
import com.app.pao.entity.network.REBase;
import com.app.pao.fragment.BaseFragmentV2;
import com.app.pao.network.api.BaseResponseParser;
import com.app.pao.network.api.MyRequestParamsV2;
import com.app.pao.network.utils.RequestParamsBuilder;
import com.app.pao.ui.widget.BadgeView;
import com.app.pao.ui.widget.FullyLinearLayoutManager;
import com.app.pao.ui.widget.LoadingView;
import com.app.pao.utils.DeviceUtils;
import com.app.pao.utils.Log;
import com.app.pao.utils.TimeUtils;
import com.lidroid.xutils.HttpUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import hwh.com.pulltorefreshlibrary.PullToRefreshLayout;
import hwh.com.pulltorefreshlibrary.PullableScrollViewForRecyclerView;

/**
 * Created by Raul.Fan on 2016/9/13.
 */
public class DynamicFragmentV2 extends BaseFragmentV2 {


    /* contains */
    private static final String TAG = "DynamicFragmentV2";

    private static final int MSG_FRESH_ERROR = 0x01;//刷新或加载更多请求失败
    private static final int MSG_UPDATE_DYNAMIC_VIEW = 0x02;//刷新动态列表
    private static final int MSG_UPDATE_BANNER_VIEW = 0x03;//更新Banner内容


    @BindView(R.id.refresh_layout_dynamic_list)
    PullToRefreshLayout mFreshLayout;//刷新VIew
    @BindView(R.id.lv_dynamic_list)
    RecyclerView mRvView;//动态列表
    @BindView(R.id.ll_simple_runner_base)
    LinearLayout mSimpleRunnerLlBase;
    @BindView(R.id.lv_runner_list)
    RecyclerView mRunnerRv;//跑步人员的列表

    @BindView(R.id.webView)
    WebView mWebView;//今日锻炼天气H5
    @BindView(R.id.race_layout)
    FrameLayout mRaceLayout;

    @BindView(R.id.btn_message)
    ImageButton mMessageBtn;//消息按钮
    BadgeView mBadgeView;//消息数量显示控件

    @BindView(R.id.rl_load)
    LinearLayout mLoadViewRl;//加载View
    @BindView(R.id.loadview)
    LoadingView mLoadV;//动画控件
    @BindView(R.id.tv_reload)
    TextView mReloadV;//显示重新加载按钮


    /* local data */
    //webView 相关
    private String mBarUrl;//bar上显示的H5
    private String mAdUrl;//跳转后显示的H5

    private boolean postAble = true;

    private DynamicListAdapterReplace mAdapter;//动态适配器

    //时间适配数据
    private Date mLastFechDay;// 最后更新日
    private int mLastFechIndexDay = -1;
    private String mLastDate;//最后一个跑步历史日期
    private boolean mNeedLoadMore = true;

    //列表相关
    private ArrayList<DynamicLineItem> mLineDatas = new ArrayList<DynamicLineItem>();//适配器塞入数据

    public static DynamicFragmentV2 newInstance() {
        DynamicFragmentV2 fragment = new DynamicFragmentV2();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_dynamic;
    }

    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.btn_qrcode, R.id.btn_message, R.id.ll_click_webview, R.id.tv_reload})
    public void onClick(View view) {
        switch (view.getId()) {
            //点击二维码扫描按钮
            case R.id.btn_qrcode:
                launchQrCode();
                break;
            //点击消息列表
            case R.id.btn_message:
                startActivity(MessageListActivityReplace.class);
                break;
            //点击H5广告
            case R.id.ll_click_webview:
                MaidianData.saveMaidian(getActivity(), new DBEntityMaidian(System.currentTimeMillis()
                        + "", AppEnum.MaidianType.BannerInto, TimeUtils.NowTime()));
                launchWebView();
                break;
            //点击重新加载
            case R.id.tv_reload:
                doOnFresh();
                break;
        }
    }

    /**
     * 刷新事件
     */
    PullToRefreshLayout.OnRefreshListener mFreshListener = new PullToRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
            doOnFresh();
        }

        @Override
        public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
            if (mNeedLoadMore) {
                doLoadMore();
            }else {
                mFreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                //有网络请求错误
                case MSG_FRESH_ERROR:
                    postAble = true;
                    if ((boolean) msg.obj) {
                        mFreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                        mLoadV.setLoadingText("加载失败");
                        mReloadV.setVisibility(View.VISIBLE);
                    } else {
                        mFreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
                    }
                    break;
                //刷新列表
                case MSG_UPDATE_DYNAMIC_VIEW:
                    postAble = true;
                    if ((boolean) msg.obj) {
                        mFreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        mLoadV.setVisibility(View.GONE);
                        mLoadViewRl.setVisibility(View.GONE);
                    } else {
                        mFreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }

                    //刷新列表
                    mAdapter = new DynamicListAdapterReplace(getActivity(), mLineDatas);
                    mRvView.setAdapter(mAdapter);
                    break;
                //更新Banner页面
                case MSG_UPDATE_BANNER_VIEW:
                    //比赛列表
                    if (mBarUrl == null) {
                        mRaceLayout.setVisibility(View.GONE);
                    } else {
                        mWebView.loadUrl(mBarUrl);
                        mRaceLayout.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    };

    @Override
    protected void initParams() {
        //初始化消息控件
        mBadgeView = new BadgeView(getActivity(), mMessageBtn);
        mBadgeView.setTextSize(9);
//        badgeView.setBackgroundColor(Color.WHITE);
        mBadgeView.setBackgroundResource(R.drawable.icon_red_circle);
        mBadgeView.setTextColor(Color.parseColor("#ffffff"));
        mBadgeView.setBadgeMargin((int) DeviceUtils.dpToPixel(3));
        //增加下拉刷新的监听
        mFreshLayout.setOnRefreshListener(mFreshListener);

        mRvView.setFocusable(false);
        mRvView.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        mRunnerRv.setLayoutManager(new FullyLinearLayoutManager(getActivity()));

        //web view
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRaceLayout.getLayoutParams();
        layoutParams.height = (int) (DeviceUtils.getScreenWidth() * 216 / 640);
        mRaceLayout.setLayoutParams(layoutParams);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //接受证书
                handler.proceed();
            }
        });
        //定义动态列表
        mRvView.setLayoutManager(new FullyLinearLayoutManager(getActivity()));
        doOnFresh();
    }

    @Override
    protected void causeGC() {

    }

    @Override
    protected void onVisible() {

    }

    @Override
    protected void onInVisible() {

    }

    /**
     * 启动扫描页面
     */
    private void launchQrCode() {
        Bundle bundle = new Bundle();
        bundle.putInt("hasScanSys", 1);
        startActivity(ScanQRCodeActivityReplace.class, bundle);
    }

    /**
     * 启动WebView界面
     */
    private void launchWebView() {
        Intent intent = new Intent();
        intent.putExtra("url", mAdUrl);
        intent.setClass(getContext(), RaceWebViewActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 刷新列表动作
     */
    private void doOnFresh() {
        if (!postAble) {
            return;
        }
        mNeedLoadMore = true;
        postAble = false;
        mLastFechIndexDay = -1;
        mLastDate = TimeUtils.NowTime();
        mLastFechDay = TimeUtils.getToday();
        x.task().post(new Runnable() {
            @Override
            public void run() {
                postGetWorkoutList(true);
            }
        });
        x.task().post(new Runnable() {
            @Override
            public void run() {
                postGetRaceList();
            }
        });
    }


    /**
     * 加载更多
     */
    private void doLoadMore(){
        if (!postAble) {
            return;
        }
        postAble = false;
        x.task().post(new Runnable() {
            @Override
            public void run() {
                postGetWorkoutList(false);
            }
        });

    }

    /**
     * 获取跑步历史列表
     */
    private void postGetWorkoutList(final boolean isFresh) {
        postAble = false;
        String limit = 10 + "";
        RequestParams params = RequestParamsBuilder.buildGetDynamicRP(getActivity(), URLConfig.URL_GET_DYNAMIC, mLastDate, limit);
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE) {
                    List<GetDynamicListResult> result = JSON.parseArray(reBase.result, GetDynamicListResult.class);
                    analysisNewWorkout(result, isFresh);
                    Message msg = new Message();
                    msg.what = MSG_UPDATE_DYNAMIC_VIEW;
                    msg.obj = isFresh;
                    mHandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = MSG_FRESH_ERROR;
                    msg.obj = isFresh;
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                Message msg = new Message();
                msg.what = MSG_FRESH_ERROR;
                msg.obj = isFresh;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 获取比赛列表
     */
    private void postGetRaceList() {
        RequestParams params = new MyRequestParamsV2(getActivity(),URLConfig.URL_GET_RACE_LIST);
        x.http().post(params, new Callback.CommonCallback<REBase>() {
            @Override
            public void onSuccess(REBase reBase) {
                if (reBase.errorcode == BaseResponseParser.ERROR_CODE_NONE){
                    Log.v(TAG,"postGetRaceList:" + reBase.result);
                    GetRaceListResult result = JSON.parseObject(reBase.result, GetRaceListResult.class);
                    if (result.race != null && result.race.size() > 0) {
                        mBarUrl = result.getRace().get(0).barh5url;
                        mAdUrl = result.getRace().get(0).h5url;
                    } else {
                        mBarUrl = null;
                        mAdUrl = null;
                    }
                    mHandler.sendEmptyMessage(MSG_UPDATE_BANNER_VIEW);
                }
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    /**
     * 解析新的跑步历史
     *
     * @param resultList
     */
    private void analysisNewWorkout(final List<GetDynamicListResult> resultList, final boolean isFresh) {
        // 若刷新 ，初始化列表
        if (isFresh) {
            mLineDatas = new ArrayList<DynamicLineItem>();
        }
        if (resultList.size() < 10) {
            mNeedLoadMore = false;
        }
        Calendar calendar = Calendar.getInstance();
        //解析数据
        for (int i = 0; i < resultList.size(); i++) {
            GetDynamicListResult dynamicEntity = resultList.get(i);
            Date itemDate = TimeUtils.formatStrToDate(dynamicEntity.starttime, TimeUtils.FORMAT_TYPE_1);
            boolean isTitle = false;
            // 判断时间是否满足目前的日期
            if (itemDate.after(mLastFechDay)) {
                if (mLastFechIndexDay == -1) {
                    calendar.setTime(mLastFechDay);
                    mLastFechIndexDay++;
                    isTitle = true;
                }
                //找到满足日期
            } else {
                for (; itemDate.before(mLastFechDay); ) {
                    calendar.setTime(mLastFechDay);
                    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
                    mLastFechDay = calendar.getTime();
                }
                mLastFechIndexDay++;
                isTitle = true;


            }
            if (isTitle) {
                mLineDatas.add(new DynamicLineItem(TimeUtils
                        .getTimestampDayString(mLastFechDay), dynamicEntity, true));
            }
            mLineDatas.add(new DynamicLineItem(TimeUtils
                    .getTimestampDayString(mLastFechDay), dynamicEntity, false));
        }
        if (mLineDatas.size() > 0) {
            mLastDate = mLineDatas.get(mLineDatas.size() - 1).result.getStarttime();
        }
    }

}
